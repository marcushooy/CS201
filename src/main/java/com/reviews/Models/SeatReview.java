package Models;

public class SeatReview implements ReviewRecord {
    private String airlineName;
    private String aircraftType;
    private String seatConfiguration;
    private String date;
    private double overallRating;
    private String country;
    private String content;
    
    public SeatReview(String airlineName, String aircraftType, String seatConfiguration, String date, double overallRating, String country, String content) {
        this.airlineName = airlineName;
        this.aircraftType = aircraftType;
        this.seatConfiguration = seatConfiguration;
        this.date = date;
        this.overallRating = overallRating;
        this.country = country;
        this.content = content;
    }
    
    @Override
    public String getName() {
        return airlineName;
    }
    
    public String getAircraftType() {
        return aircraftType;
    }
    
    public String getSeatConfiguration() {
        return seatConfiguration;
    }
    
    @Override
    public String getDate() {
        return date;
    }
    
    @Override
    public double getOverallRating() {
        return overallRating;
    }
    
    @Override
    public String getCountry() {
        return country;
    }
    
    @Override
    public String getContent() {
        return content;
    }
}
