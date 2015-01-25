package mylife.org.mylife;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Cveni on 2015-01-07.
 */

public class SportListAdapter extends ArrayAdapter<ActivityModel>
{
    public static final double iconScale = 1.4;

    private Context context;
    private LayoutInflater inflater;
    private List<ActivityModel> list;
    private int height;

    public SportListAdapter(Context context, int layoutResource, List<ActivityModel> list)
    {
        super(context, layoutResource, list);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
        height = 0;
    }

    public View getView(int position, View view, ViewGroup parent)
    {
        if(view == null)
        {
            view = inflater.inflate(R.layout.sport_list_item, null);
        }

        if(height == 0)
        {
            final View view2 = view;
            ViewTreeObserver vto = view.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
            {
                @Override
                public void onGlobalLayout()
                {
                    height = (int)(view2.getMeasuredHeight()/iconScale);

                    ImageView pulse = (ImageView) view2.findViewById(R.id.sport_list_pulse_icon);
                    RelativeLayout.LayoutParams rllp = (RelativeLayout.LayoutParams) pulse.getLayoutParams();
                    rllp.height = height;
                    pulse.setLayoutParams(rllp);

                    ImageView gps = (ImageView) view2.findViewById(R.id.sport_list_gps_icon);
                    RelativeLayout.LayoutParams rllp2 = (RelativeLayout.LayoutParams) gps.getLayoutParams();
                    rllp2.height = height;
                    gps.setLayoutParams(rllp2);

                    view2.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            });
        }

        ActivityModel acti = list.get(position);

        TextView tvName = (TextView) view.findViewById(R.id.sport_list_item_name);

        String[] types = context.getResources().getStringArray(R.array.sport_activity_types);
        String[] typesdb = context.getResources().getStringArray(R.array.sport_activity_types_db);
        String type = acti.getType();

        for(int i = 0; i < types.length; i++)
        {
            if(type.equals(typesdb[i])) type = types[i];
        }

        tvName.setText("("+type+") "+acti.getName());

        TextView tvDate = (TextView) view.findViewById(R.id.sport_list_item_date);

        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(acti.getDate());
        String timeS = time.get(Calendar.DAY_OF_MONTH)+"."+String.format("%02d", (time.get(Calendar.MONTH)+1))+"."+time.get(Calendar.YEAR)
                +" - "+time.get(Calendar.HOUR_OF_DAY)+":"+String.format("%02d", time.get(Calendar.MINUTE));

        tvDate.setText(timeS);

        ImageView pulse = (ImageView) view.findViewById(R.id.sport_list_pulse_icon);
        RelativeLayout.LayoutParams rllp = (RelativeLayout.LayoutParams) pulse.getLayoutParams();
        rllp.height = height;
        pulse.setLayoutParams(rllp);

        ImageView gps = (ImageView) view.findViewById(R.id.sport_list_gps_icon);
        RelativeLayout.LayoutParams rllp2 = (RelativeLayout.LayoutParams) gps.getLayoutParams();
        rllp2.height = height;
        gps.setLayoutParams(rllp2);

        int device = (int)acti.getDevice();

        switch(device)
        {
            case 0:
                gps.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_action_place));
                pulse.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_action_favorite_light));
                break;
            case 1:
                gps.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_action_place_light));
                pulse.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_action_favorite));
                break;
            case 2:
                gps.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_action_place));
                pulse.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_action_favorite));
                break;
        }

        return view;
    }
}
