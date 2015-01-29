package mylife.org.mylife;

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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class HRMScan {
    Context context;
    AntPlusHeartRatePcc hrmController = null;
    AsyncScanController<AntPlusHeartRatePcc> scanController;
    protected PccReleaseHandle<AntPlusHeartRatePcc> releaseHandle = null;
    String computedHR = "";

    private BaseManager base;
    private long activityIndex;
    String textHeartRate;
    ArrayList<AsyncScanController.AsyncScanResultDeviceInfo> alreadyConnectedDeviceInfos;
    ArrayList<AsyncScanController.AsyncScanResultDeviceInfo> scannedDeviceInfos;
    ArrayAdapter<String> adapter_devNameList;
    ArrayAdapter<String> adapter_connDevNameList;
    Runnable task;
    int computedHeart;
    int time;
    boolean taskStopped=false;
    long lastTime=0;
    int frequency;
    public HRMScan(Context context,BaseManager base, long activityIndex) {
        this.context=context;
        this.base = base;
        this.activityIndex = activityIndex;
        time = 0;
    }
    public HRMScan(Context context,BaseManager base, long activityIndex, int time) {
        this.context=context;
        this.base = base;
        this.activityIndex = activityIndex;
        this.time = time;
    }
    private void initScan() {
        alreadyConnectedDeviceInfos = new ArrayList<AsyncScanController.AsyncScanResultDeviceInfo>();
        scannedDeviceInfos = new ArrayList<AsyncScanController.AsyncScanResultDeviceInfo>();
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String freq=sp.getString(context.getResources().getString(R.string.settings_pulse_freq_key), "0");
        frequency=Integer.parseInt(freq);
    }
    protected void requestConnectToResult(
            final AsyncScanResultDeviceInfo asyncScanResultDeviceInfo) {
        new Thread(new Runnable() {
            public void run() {
                releaseHandle = scanController.requestDeviceAccess(
                        asyncScanResultDeviceInfo,
                        new IPluginAccessResultReceiver<AntPlusHeartRatePcc>() {
                            @Override
                            public void onResultReceived(
                                    AntPlusHeartRatePcc result,
                                    RequestAccessResult resultCode,
                                    DeviceState initialDeviceState) {
                                if (resultCode == RequestAccessResult.SEARCH_TIMEOUT) {
                                    new Thread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(
                                                    context,
                                                    "Timed out attempting to connect, try again",
                                                    Toast.LENGTH_LONG).show();

                                        }
                                    }).run();
                                } else {
                                    base_IPluginAccessResultReceiver
                                            .onResultReceived(result,
                                                    resultCode,
                                                    initialDeviceState);
                                    scanController = null;
                                }
                            }
                        }, base_IDeviceStateChangeReceiver);
            }
        }).run();
    }
    protected void requestAccessToPcc() {
        initScan();
        scanController = AntPlusHeartRatePcc.requestAsyncScanController(
                context, 0, new IAsyncScanResultReceiver() {
                    @Override
                    public void onSearchStopped(
                            RequestAccessResult reasonStopped) {
                        base_IPluginAccessResultReceiver.onResultReceived(null,
                                reasonStopped, DeviceState.DEAD);
                    }
                    @Override
                    public void onSearchResult(
                            final AsyncScanResultDeviceInfo deviceFound) {
                        for (AsyncScanResultDeviceInfo i : scannedDeviceInfos) {
                            if (i.getAntDeviceNumber() == deviceFound
                                    .getAntDeviceNumber()) {
                                return;
                            }
                        }
                        if (deviceFound.isAlreadyConnected()) {
                            alreadyConnectedDeviceInfos.add(deviceFound);
                            requestConnectToResult(deviceFound);
                        } else {
                            scannedDeviceInfos.add(deviceFound);
                            requestConnectToResult(deviceFound);
                        }
                    }
                });
    }
    protected void handleReset() {
        if (releaseHandle != null) {
            releaseHandle.close();
        }
        taskStopped=false;
        requestAccessToPcc();
    }
    public void subscribeToHrEvents() {
        hrmController.subscribeHeartRateDataEvent(new IHeartRateDataReceiver() {
            @Override
            public void onNewHeartRateData(final long estTimestamp,
                                           EnumSet<EventFlag> eventFlags, final int computedHeartRate,
                                           final long heartBeatCount,
                                           final BigDecimal heartBeatEventTime,
                                           final DataState dataState) {
                final String textHeartRate = String.valueOf(computedHeartRate)
                        + ((DataState.ZERO_DETECTED.equals(dataState)) ? "*"
                        : "");
                final String textHeartBeatCount = String
                        .valueOf(heartBeatCount)
                        + ((DataState.INITIAL_VALUE.equals(dataState)) ? "*"
                        : "");
                final String textHeartBeatEventTime = String
                        .valueOf(heartBeatEventTime)
                        + ((DataState.INITIAL_VALUE.equals(dataState)) ? "*"
                        : "");



                task=new Runnable() {

                    @Override
                    public void run() {
                        if (!taskStopped && (System.currentTimeMillis()-lastTime)>frequency) {
                            lastTime=System.currentTimeMillis();
                            computedHR = textHeartRate;
                            base.savePulse(computedHeartRate, activityIndex);
                        }
                    }
                };

                task.run();
            }
        });
        hrmController.subscribePage4AddtDataEvent(new IPage4AddtDataReceiver() {
            @Override
            public void onNewPage4AddtData(final long estTimestamp,
                                           final EnumSet<EventFlag> eventFlags,
                                           final int manufacturerSpecificByte,
                                           final BigDecimal previousHeartBeatEventTime) {
            }
        });
        hrmController
                .subscribeCumulativeOperatingTimeEvent(new ICumulativeOperatingTimeReceiver() {
                    @Override
                    public void onNewCumulativeOperatingTime(
                            final long estTimestamp,
                            final EnumSet<EventFlag> eventFlags,
                            final long cumulativeOperatingTime) {
                    }
                });
        hrmController
                .subscribeManufacturerAndSerialEvent(new IManufacturerAndSerialReceiver() {
                    @Override
                    public void onNewManufacturerAndSerial(
                            final long estTimestamp,
                            final EnumSet<EventFlag> eventFlags,
                            final int manufacturerID, final int serialNumber) {
                    }
                });
        hrmController
                .subscribeVersionAndModelEvent(new IVersionAndModelReceiver() {
                    @Override
                    public void onNewVersionAndModel(final long estTimestamp,
                                                     final EnumSet<EventFlag> eventFlags,
                                                     final int hardwareVersion,
                                                     final int softwareVersion, final int modelNumber) {
                    }
                });
        hrmController
                .subscribeCalculatedRrIntervalEvent(new ICalculatedRrIntervalReceiver() {
                    @Override
                    public void onNewCalculatedRrInterval(
                            final long estTimestamp,
                            EnumSet<EventFlag> eventFlags,
                            final BigDecimal rrInterval, final RrFlag flag) {
                    }
                });
    }
    protected IPluginAccessResultReceiver<AntPlusHeartRatePcc> base_IPluginAccessResultReceiver = new IPluginAccessResultReceiver<AntPlusHeartRatePcc>() {
        @Override
        public void onResultReceived(AntPlusHeartRatePcc result,
                                     RequestAccessResult resultCode, DeviceState initialDeviceState) {
            switch (resultCode) {
                case SUCCESS:
                    //Toast.makeText(context, "SUCCESS",
                    //Toast.LENGTH_SHORT).show();
                    hrmController = result;
                    subscribeToHrEvents();
                    break;
                case CHANNEL_NOT_AVAILABLE:
                    Toast.makeText(context, "Channel Not Available",
                            Toast.LENGTH_SHORT).show();
                    break;
                case ADAPTER_NOT_DETECTED:
                    Toast.makeText(
                            context,
                            "ANT Adapter Not Available. Built-in ANT hardware or external adapter required.",
                            Toast.LENGTH_SHORT).show();
                    break;
                case BAD_PARAMS:
                    Toast.makeText(context, "Bad request parameters.",
                            Toast.LENGTH_SHORT).show();
                    break;
                case OTHER_FAILURE:
                    Toast.makeText(context,
                            "RequestAccess failed. See logcat for details.",
                            Toast.LENGTH_SHORT).show();
                    break;
                case DEPENDENCY_NOT_INSTALLED:
                    AlertDialog.Builder adlgBldr = new AlertDialog.Builder(context);
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
                                    context.startActivity(startStore);
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
                    break;
                case USER_CANCELLED:
                    break;
                case UNRECOGNIZED:
                    Toast.makeText(context,
                            "Failed: UNRECOGNIZED. PluginLib Upgrade Required?",
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(context, "Unrecognized result: " + resultCode,
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    protected IDeviceStateChangeReceiver base_IDeviceStateChangeReceiver = new IDeviceStateChangeReceiver() {
        @Override
        public void onDeviceStateChange(final DeviceState newDeviceState) {

        }
    };
    public void release(){
        if (releaseHandle != null) {
            releaseHandle.close();
        }
    }
    public void kill(){
        if (task!=null){
            taskStopped=true;
        }
    }
}