package Models;

public class AirlineReview implements ReviewRecord {
    private String airlineName;
    private String date;
    private double overallRating;
    private String country;
    private String content;
    
    public AirlineReview(String airlineName, String date, double overallRating, String country, String content) {
        this.airlineName = airlineName;
        this.date = date;
        this.overallRating = overallRating;
        this.country = country;
        this.content = content;
    }
    
    @Override
    public String getName() {
        return airlineName;
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
