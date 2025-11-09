package models;

/**
 * AirlineRanking class to represent an airline's ranking based on RBAR.
 * This class is used by all data structures to return consistent ranking results.
 */
public class AirlineRanking {
    public String airlineName;
    public double rbar;
    public int reviewCount;
    
    public AirlineRanking(String airlineName, double rbar, int reviewCount) {
        this.airlineName = airlineName;
        this.rbar = rbar;
        this.reviewCount = reviewCount;
    }
    
    @Override
    public String toString() {
        return String.format("%s: RBAR=%.3f (%d reviews)", airlineName, rbar, reviewCount);
    }
    
    // Getters for convenience
    public String getAirlineName() { return airlineName; }
    public double getRbar() { return rbar; }
    public int getReviewCount() { return reviewCount; }
}

