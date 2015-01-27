package mylife.org.mylife;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Cveni on 2015-01-27.
 */

public class StepListAdapter extends ArrayAdapter<Calendar>
{
    private Context context;
    private LayoutInflater inflater;
    private List<Calendar> list;

    public StepListAdapter(Context context, int layoutResource, List<Calendar> list)
    {
        super(context, layoutResource, list);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    public View getView(int position, View view, ViewGroup parent)
    {
        if (view == null)
        {
            view = inflater.inflate(R.layout.step_list_item, null);
        }

        Calendar day = list.get(position);

        TextView tvDate = (TextView)view.findViewById(R.id.step_list_item_date);
        tvDate.setText(day.get(Calendar.DAY_OF_MONTH) + "." + String.format("%02d", (day.get(Calendar.MONTH) + 1)) + "." + day.get(Calendar.YEAR));

        return view;
    }
}
