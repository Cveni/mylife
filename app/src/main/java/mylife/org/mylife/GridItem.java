package mylife.org.mylife;

/**
 * Created by Cveni on 2015-01-16.
 */

public class GridItem
{
    private String title;
    private String value;

    public GridItem(String title, String value)
    {
        this.title = title;
        this.value = value;
    }

    public String getTitle()
    {
        return title.toUpperCase();
    }

    public String getValue()
    {
        return value;
    }
}
