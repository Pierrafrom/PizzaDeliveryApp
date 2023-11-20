package model;

public class Pizza {

    private GPS GPScoordinates;
    private int RestTime;

    public Pizza(GPS GPScoordinates, int RestTime) {
        this.GPScoordinates = GPScoordinates;
        this.RestTime = RestTime;
    }

    public GPS getGPS() {
        return GPScoordinates;
    }

    public int getRestTime() {
        return RestTime;
    }

    public String toString() {
        return "Pizza [GPS=" + GPScoordinates + ", RestTime=" + RestTime + "]";
    }
}

