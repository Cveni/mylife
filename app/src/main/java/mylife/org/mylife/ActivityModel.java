package mylife.org.mylife;

/**
 * Created by Cveni on 2015-01-07.
 */

public class ActivityModel
{
    private long id;
    private String name;
    private long date;
    private long device;
    private String type;

    public ActivityModel(long id, String name, long date, long device, String type)
    {
        this.id = id;
        this.name = name;
        this.date = date;
        this.device = device;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getDate() {
        return date;
    }

    public long getDevice() {
        return device;
    }

    public String getType() {
        return type;
    }
}
