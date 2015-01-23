package mylife.org.mylife;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Cveni on 2015-01-16.
 */

public class GridAdapter extends ArrayAdapter<GridItem>
{
    private Context context;
    private LayoutInflater inflater;
    private List<GridItem> list;
    private int cellHeight;

    public GridAdapter(Context context, int layoutResource, List<GridItem> list)
    {
        super(context, layoutResource, list);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    public void setCellHeight(int cellHeight)
    {
        this.cellHeight = cellHeight;
    }

    public View getView(int position, View view, ViewGroup parent)
    {
        view = inflater.inflate(R.layout.grid_item, null);
        if(cellHeight != 0) view.setMinimumHeight(cellHeight);

        TextView tvTitle = (TextView)view.findViewById(R.id.grid_item_title);
        tvTitle.setText(list.get(position).getTitle());

        TextView tvValue = (TextView)view.findViewById(R.id.grid_item_value);
        tvValue.setText(list.get(position).getValue());

        return view;
    }

    @Override
    public boolean isEnabled(int position)
    {
        return false;
    }
}
