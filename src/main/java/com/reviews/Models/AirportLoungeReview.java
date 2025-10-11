package Models;

public class AirportLoungeReview implements ReviewRecord {
    private String airlineName;
    private String loungeName;
    private String date;
    private double overallRating;
    private String country;
    private String content;
    
    public AirportLoungeReview(String airlineName, String loungeName, String date, double overallRating, String country, String content) {
        this.airlineName = airlineName;
        this.loungeName = loungeName;
        this.date = date;
        this.overallRating = overallRating;
        this.country = country;
        this.content = content;
    }
    
    @Override
    public String getName() {
        return airlineName;
    }
    
    public String getLoungeName() {
        return loungeName;
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
