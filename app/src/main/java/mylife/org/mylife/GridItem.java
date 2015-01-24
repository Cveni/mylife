package mylife.org.mylife;

/**
 * Created by Cveni on 2015-01-16.
 */

public class GridItem
{
    private String title;
    private String value;
    private boolean small;

    public GridItem(String title, String value, boolean small)
    {
        this.title = title;
        this.value = value;
        this.small = small;
    }

    public String getTitle()
    {
        return title.toUpperCase();
    }

    public String getValue()
    {
        return value;
    }

    public boolean getSmall()
    {
        return small;
    }
}
