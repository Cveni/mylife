package mylife.org.mylife;

/**
 * Created by Cveni on 2015-01-17.
 */

public class PulseModel
{
    private int value;
    private double dateTimestamp;

    public PulseModel(int value, double dateTimestamp)
    {
        this.value = value;
        this.dateTimestamp = dateTimestamp;
    }

    public double getValue()
    {
        return value;
    }

    public double getDateTimestamp()
    {
        return dateTimestamp;
    }
}
