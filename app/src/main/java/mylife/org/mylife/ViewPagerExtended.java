package mylife.org.mylife;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Cveni on 2015-01-20.
 */

public class ViewPagerExtended extends ViewPager
{
    public ViewPagerExtended(Context context)
    {
        super(context);
    }

    public ViewPagerExtended(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0)
    {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return false;
    }
}
