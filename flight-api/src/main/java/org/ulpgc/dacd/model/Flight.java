package org.ulpgc.dacd.model;

public class Flight {

    private final String icao;
    private final String callsign;
    private final String country;
    private final double latitude;
    private final double longitude;
    private final double altitude;
    private final double velocity;
    private final long lastUpdate;
    private final long capturedAt;

    public Flight(String icao, String callsign, String country,
                  double latitude, double longitude, double altitude,
                  double velocity, long lastUpdate, long capturedAt) {
        this.icao = icao;
        this.callsign = callsign;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.velocity = velocity;
        this.lastUpdate = lastUpdate;
        this.capturedAt = capturedAt;
    }

    public String getIcao() { return icao; }
    public String getCallsign() { return callsign; }
    public String getCountry() { return country; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public double getAltitude() { return altitude; }
    public double getVelocity() { return velocity; }
    public long getLastUpdate() { return lastUpdate; }
    public long getCapturedAt() { return capturedAt; }
}
