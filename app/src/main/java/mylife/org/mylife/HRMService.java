package mylife.org.mylife;

/**
 * Created by Mateusz on 2015-01-12.
 */

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;

import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc;
import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc.DataState;
import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc.ICalculatedRrIntervalReceiver;
import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc.IHeartRateDataReceiver;
import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc.IPage4AddtDataReceiver;
import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc.RrFlag;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceState;
import com.dsi.ant.plugins.antplus.pcc.defines.EventFlag;
import com.dsi.ant.plugins.antplus.pcc.defines.RequestAccessResult;
import com.dsi.ant.plugins.antplus.pccbase.AsyncScanController;
import com.dsi.ant.plugins.antplus.pccbase.PccReleaseHandle;
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc.IDeviceStateChangeReceiver;
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc.IPluginAccessResultReceiver;
import com.dsi.ant.plugins.antplus.pccbase.AntPlusLegacyCommonPcc.ICumulativeOperatingTimeReceiver;
import com.dsi.ant.plugins.antplus.pccbase.AntPlusLegacyCommonPcc.IManufacturerAndSerialReceiver;
import com.dsi.ant.plugins.antplus.pccbase.AntPlusLegacyCommonPcc.IVersionAndModelReceiver;
import com.dsi.ant.plugins.antplus.pccbase.AsyncScanController.AsyncScanResultDeviceInfo;
import com.dsi.ant.plugins.antplus.pccbase.AsyncScanController.IAsyncScanResultReceiver;

//import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
//import android.content.DialogInterface;
import android.content.Intent;
//import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class HRMService extends Service {

    AntPlusHeartRatePcc hrPcc = null;
    AsyncScanController<AntPlusHeartRatePcc> hrScanCtrl;
    protected PccReleaseHandle<AntPlusHeartRatePcc> releaseHandle = null;

    String computedHR = "";
    private PowerManager.WakeLock wl;
    private BaseManager base;
    private long activityIndex;

    ArrayList<AsyncScanController.AsyncScanResultDeviceInfo> mAlreadyConnectedDeviceInfos;
    ArrayList<AsyncScanController.AsyncScanResultDeviceInfo> mScannedDeviceInfos;
    ArrayAdapter<String> adapter_devNameList;
    ArrayAdapter<String> adapter_connDevNameList;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        acquireWakeLock();
        base = new BaseManager(getApplicationContext());
        activityIndex = intent.getExtras().getLong("activityIndex");
        setupHRMListener();

        return Service.START_NOT_STICKY;
    }

    private void acquireWakeLock() {
        PowerManager pm = (PowerManager) getApplicationContext()
                .getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MYLIFE");
        wl.acquire();
    }

    private void setupHRMListener() {
        initScanDisplay();
        handleReset();
    }

    /******************************* HRMCOnnect *****************************/
    private void initScanDisplay() {
        mAlreadyConnectedDeviceInfos = new ArrayList<AsyncScanController.AsyncScanResultDeviceInfo>();
        mScannedDeviceInfos = new ArrayList<AsyncScanController.AsyncScanResultDeviceInfo>();

    }

    /**
     * Requests access to the given search result.
     *
     * @param asyncScanResultDeviceInfo
     *            The search result to attempt to connect to.
     */
    protected void requestConnectToResult(
            final AsyncScanResultDeviceInfo asyncScanResultDeviceInfo) {

        // Inform the user we are connecting
        new Thread(new Runnable() {
            public void run() {
                releaseHandle = hrScanCtrl.requestDeviceAccess(
                        asyncScanResultDeviceInfo,
                        new IPluginAccessResultReceiver<AntPlusHeartRatePcc>() {
                            @Override
                            public void onResultReceived(
                                    AntPlusHeartRatePcc result,
                                    RequestAccessResult resultCode,
                                    DeviceState initialDeviceState) {
                                if (resultCode == RequestAccessResult.SEARCH_TIMEOUT) {
                                    // On a connection timeout the scan
                                    // automatically resumes, so we inform the
                                    // user, and go back to scanning
                                    new Thread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(
                                                    HRMService.this,
                                                    "Timed out attempting to connect, try again",
                                                    Toast.LENGTH_LONG).show();

                                        }
                                    }).run();
                                } else {
                                    // Otherwise the results, including SUCCESS,
                                    // behave the same as
                                    base_IPluginAccessResultReceiver
                                            .onResultReceived(result,
                                                    resultCode,
                                                    initialDeviceState);
                                    hrScanCtrl = null;
                                }
                            }
                        }, base_IDeviceStateChangeReceiver);
            }
        }).run();
    }

    /**
     * Requests the asynchronous scan controller
     */
    protected void requestAccessToPcc() {
        initScanDisplay();
        hrScanCtrl = AntPlusHeartRatePcc.requestAsyncScanController(this, 0,
                new IAsyncScanResultReceiver() {
                    @Override
                    public void onSearchStopped(
                            RequestAccessResult reasonStopped) {
                        // The triggers calling this function use the same codes
                        // and require the same actions as those received by the
                        // standard access result receiver
                        base_IPluginAccessResultReceiver.onResultReceived(null,
                                reasonStopped, DeviceState.DEAD);
                    }

                    @Override
                    public void onSearchResult(
                            final AsyncScanResultDeviceInfo deviceFound) {
                        for (AsyncScanResultDeviceInfo i : mScannedDeviceInfos) {
                            // The current implementation of the async scan will
                            // reset it's ignore list every 30s,
                            // So we have to handle checking for duplicates in
                            // our list if we run longer than that
                            if (i.getAntDeviceNumber() == deviceFound
                                    .getAntDeviceNumber()) {
                                // Found already connected device, ignore
                                return;
                            }
                        }

                        // We split up devices already connected to the plugin
                        // from un-connected devices to make this information
                        // more visible to the user,
                        // since the user most likely wants to be aware of which
                        // device they are already using in another app
                        if (deviceFound.isAlreadyConnected()) {
                            mAlreadyConnectedDeviceInfos.add(deviceFound);
                            requestConnectToResult(deviceFound);
                        } else {
                            mScannedDeviceInfos.add(deviceFound);
                            requestConnectToResult(deviceFound);
                        }
                    }
                });
    }

    protected void handleReset() {
        // Release the old access if it exists
        if (releaseHandle != null) {
            releaseHandle.close();
        }

        requestAccessToPcc();
    }

    protected void showDataDisplay(String status) {

    }

    /**
     * Switches the active view to the data display and subscribes to all the
     * data events
     */
    public void subscribeToHrEvents() {
        hrPcc.subscribeHeartRateDataEvent(new IHeartRateDataReceiver() {
            @Override
            public void onNewHeartRateData(final long estTimestamp,
                                           EnumSet<EventFlag> eventFlags, final int computedHeartRate,
                                           final long heartBeatCount,
                                           final BigDecimal heartBeatEventTime,
                                           final DataState dataState) {
                // Mark heart rate with asterisk if zero detected
                final String textHeartRate = String.valueOf(computedHeartRate)
                        + ((DataState.ZERO_DETECTED.equals(dataState)) ? "*"
                        : "");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (!textHeartRate.equals(computedHR)) {
                            computedHR = textHeartRate;
//							Toast.makeText(getApplicationContext(),
//									textHeartRate, Toast.LENGTH_LONG).show();
                            base.savePulse(computedHeartRate, activityIndex);
                        }
                    }
                }).run();
            }
        });

        hrPcc.subscribePage4AddtDataEvent(new IPage4AddtDataReceiver() {
            @Override
            public void onNewPage4AddtData(final long estTimestamp,
                                           final EnumSet<EventFlag> eventFlags,
                                           final int manufacturerSpecificByte,
                                           final BigDecimal previousHeartBeatEventTime) {

            }
        });

        hrPcc.subscribeCumulativeOperatingTimeEvent(new ICumulativeOperatingTimeReceiver() {
            @Override
            public void onNewCumulativeOperatingTime(final long estTimestamp,
                                                     final EnumSet<EventFlag> eventFlags,
                                                     final long cumulativeOperatingTime) {

            }
        });

        hrPcc.subscribeManufacturerAndSerialEvent(new IManufacturerAndSerialReceiver() {
            @Override
            public void onNewManufacturerAndSerial(final long estTimestamp,
                                                   final EnumSet<EventFlag> eventFlags,
                                                   final int manufacturerID, final int serialNumber) {

            }
        });

        hrPcc.subscribeVersionAndModelEvent(new IVersionAndModelReceiver() {
            @Override
            public void onNewVersionAndModel(final long estTimestamp,
                                             final EnumSet<EventFlag> eventFlags,
                                             final int hardwareVersion, final int softwareVersion,
                                             final int modelNumber) {

            }
        });

        hrPcc.subscribeCalculatedRrIntervalEvent(new ICalculatedRrIntervalReceiver() {
            @Override
            public void onNewCalculatedRrInterval(final long estTimestamp,
                                                  EnumSet<EventFlag> eventFlags, final BigDecimal rrInterval,
                                                  final RrFlag flag) {

            }
        });
    }

    protected IPluginAccessResultReceiver<AntPlusHeartRatePcc> base_IPluginAccessResultReceiver = new IPluginAccessResultReceiver<AntPlusHeartRatePcc>() {
        // Handle the result, connecting to events on success or reporting
        // failure to user.
        @Override
        public void onResultReceived(AntPlusHeartRatePcc result,
                                     RequestAccessResult resultCode, DeviceState initialDeviceState) {
            showDataDisplay("Connecting...");
            switch (resultCode) {
                case SUCCESS:
                    hrPcc = result;
                    subscribeToHrEvents();
                    break;
                case CHANNEL_NOT_AVAILABLE:
                    Toast.makeText(HRMService.this, "Channel Not Available",
                            Toast.LENGTH_SHORT).show();
                    break;
                case ADAPTER_NOT_DETECTED:
                    Toast.makeText(
                            HRMService.this,
                            "ANT Adapter Not Available. Built-in ANT hardware or external adapter required.",
                            Toast.LENGTH_SHORT).show();
                    break;
                case BAD_PARAMS:
                    Toast.makeText(HRMService.this, "Bad request parameters.",
                            Toast.LENGTH_SHORT).show();
                    break;
                case OTHER_FAILURE:
                    Toast.makeText(HRMService.this,
                            "RequestAccess failed. See logcat for details.",
                            Toast.LENGTH_SHORT).show();
                    break;
                case DEPENDENCY_NOT_INSTALLED:
                    /*AlertDialog.Builder adlgBldr = new AlertDialog.Builder(
                            HRMService.this);
                    adlgBldr.setTitle("Missing Dependency");
                    adlgBldr.setMessage("The required service\n\""
                            + AntPlusHeartRatePcc.getMissingDependencyName()
                            + "\"\n was not found. You need to install the ANT+ Plugins service or you may need to update your existing version if you already have it. Do you want to launch the Play Store to get it?");
                    adlgBldr.setCancelable(true);
                    adlgBldr.setPositiveButton("Go to Store",
                            new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Intent startStore = null;
                                    startStore = new Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("market://details?id="
                                                    + AntPlusHeartRatePcc
                                                    .getMissingDependencyPackageName()));
                                    startStore
                                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                    HRMService.this.startActivity(startStore);
                                }
                            });
                    adlgBldr.setNegativeButton("Cancel", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    final AlertDialog waitDialog = adlgBldr.create();
                    waitDialog.show();
                    break;*/
                case USER_CANCELLED:
                    Toast.makeText(HRMService.this,
                            "Cancelled. Do Menu->Reset.",
                            Toast.LENGTH_SHORT).show();
                    break;
                case UNRECOGNIZED:
                    Toast.makeText(HRMService.this,
                            "Failed: UNRECOGNIZED. PluginLib Upgrade Required?",
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(HRMService.this,
                            "Unrecognized result: " + resultCode,
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    // Receives state changes and shows it on the status display line
    protected IDeviceStateChangeReceiver base_IDeviceStateChangeReceiver = new IDeviceStateChangeReceiver() {
        @Override
        public void onDeviceStateChange(final DeviceState newDeviceState) {

        }
    };

    @Override
    public void onDestroy() {
        wl.release();
        if (releaseHandle != null) {
            releaseHandle.close();
        }
        super.onDestroy();
    }

}

