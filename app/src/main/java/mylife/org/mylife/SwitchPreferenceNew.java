package mylife.org.mylife;

import android.content.Context;
import android.preference.SwitchPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Cveni on 2015-01-06.
 */

public class SwitchPreferenceNew extends SwitchPreference
{
    public SwitchPreferenceNew(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SwitchPreferenceNew(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwitchPreferenceNew(Context context) {
        super(context);
    }

    protected void onBindView(View v)
    {
        super.onBindView(v);
        adjustViews(v);
    }

    protected void adjustViews(View v)
    {
        if(v instanceof TextView)
        {
            TextView tv = (TextView)v;
            tv.setSingleLine(false);
        }
        else if(v instanceof ViewGroup)
        {
            ViewGroup vg = (ViewGroup)v;

            for(int i = 0; i < vg.getChildCount(); i++)
            {
                adjustViews(vg.getChildAt(i));
            }
        }
    }
}
