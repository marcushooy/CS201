package Models;

public class AirportReview implements ReviewRecord {
    private String airportName;
    private String date;
    private double overallRating;
    private String country;
    private String content;
    
    public AirportReview(String airportName, String date, double overallRating, String country, String content) {
        this.airportName = airportName;
        this.date = date;
        this.overallRating = overallRating;
        this.country = country;
        this.content = content;
    }
    
    @Override
    public String getName() {
        return airportName;
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
