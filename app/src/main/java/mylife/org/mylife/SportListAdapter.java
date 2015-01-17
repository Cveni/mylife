package mylife.org.mylife;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

/**
 * Created by Cveni on 2015-01-07.
 */

public class SportListAdapter extends ArrayAdapter<ActivityModel>
{
    private Context context;
    private LayoutInflater inflater;
    private List<ActivityModel> list;
    private SparseBooleanArray selected;

    public SportListAdapter(Context context, int layoutResource, List<ActivityModel> list)
    {
        super(context, layoutResource, list);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
        selected = new SparseBooleanArray();
    }

    public View getView(int position, View view, ViewGroup parent)
    {
        view = inflater.inflate(R.layout.sport_list_item, null);

        TextView tvId = (TextView)view.findViewById(R.id.sport_list_item_name);
        tvId.setText(list.get(position).getId()+"");

        TextView tvDate = (TextView)view.findViewById(R.id.sport_list_item_date);
        Date d = new Date();
        d.setTime(list.get(position).getDate());
        tvDate.setText(d.toString());

        return view;
    }
}
