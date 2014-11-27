package mylife.org.mylife;

/**
 * Created by szymon on 27.11.14.
 */
public class LocationModel {
    private double latitude;
    private double longitude;
    private double altitude;
    private double dateTimestamp;

    public LocationModel(double latitude, double longitude, double altitude, double dateTimestamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.dateTimestamp = dateTimestamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public double getDateTimestamp() {
        return dateTimestamp;
    }
}
