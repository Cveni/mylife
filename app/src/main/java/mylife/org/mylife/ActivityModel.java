package mylife.org.mylife;

/**
 * Created by Cveni on 2015-01-07.
 */

public class ActivityModel
{
    private long id;
    private long date;
    private String type;

    public ActivityModel(long id, long date, String type)
    {
        this.id = id;
        this.date = date;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public long getDate() {
        return date;
    }

    public String getType() {
        return type;
    }
}
