package mylife.org.mylife;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by szymon on 13.01.15.
 */
public class StepModel
{
    private Calendar date;
    private double value;

    public StepModel(long timestamp, double value)
    {
        this.value = value;
        date = GregorianCalendar.getInstance();
        date.setTimeInMillis(timestamp);
    }

    public Calendar getDate()
    {
        return date;
    }

    public double getValue()
    {
        return value;
    }
}
