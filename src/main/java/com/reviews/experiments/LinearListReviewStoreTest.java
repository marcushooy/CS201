package com.reviews.experiments;

import com.reviews.Models.AirlineReview;
import com.reviews.Models.ReviewRecord;
import com.reviews.datastructures.LinearListReviewStore;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Simple test suite for the LinearListReviewStore baseline implementation.
 * Tests all core functionality including recency-biased operations.
 */
public class LinearListReviewStoreTest {
    
    private LinearListReviewStore store;
    private List<ReviewRecord> testReviews;
    
    public void setUp() {
        store = new LinearListReviewStore();
        testReviews = createTestReviews();
        store.addReviews(testReviews);
    }
    
    /**
     * Create a comprehensive set of test reviews with different dates and airlines
     * to thoroughly test recency-biased operations.
     */
    private List<ReviewRecord> createTestReviews() {
        LocalDate now = LocalDate.now();
        
        return List.of(
            // Recent reviews (last 30 days) - should have high weight
            new AirlineReview("Delta", "link1", "Great flight!", "John Doe", "USA", 
                            now.minusDays(5).toString(), "Excellent service", "Boeing 737", 
                            "Business", "Economy", "LAX-JFK", 4.5, 4.0, 4.5, 4.0, 3.5, 4.0, 3.0, 4.5, 1),
            
            new AirlineReview("Delta", "link2", "Good experience", "Jane Smith", "USA", 
                            now.minusDays(15).toString(), "Comfortable flight", "Airbus A320", 
                            "Leisure", "Economy", "SFO-LAX", 4.0, 3.5, 4.0, 3.5, 4.0, 3.5, 2.5, 4.0, 1),
            
            // Medium age reviews (30 days to 3 years) - should have medium weight
            new AirlineReview("Delta", "link3", "Decent flight", "Bob Johnson", "USA", 
                            now.minusDays(60).toString(), "Average experience", "Boeing 777", 
                            "Business", "Business", "JFK-LHR", 3.5, 3.0, 3.5, 3.0, 3.5, 3.0, 2.0, 3.5, 0),
            
            new AirlineReview("United", "link4", "Not bad", "Alice Brown", "USA", 
                            now.minusDays(120).toString(), "Could be better", "Boeing 787", 
                            "Leisure", "Economy", "ORD-NRT", 3.0, 2.5, 3.0, 2.5, 3.0, 2.5, 1.5, 3.0, 0),
            
            // Old reviews (3+ years) - should have low weight
            new AirlineReview("Delta", "link5", "Old review", "Charlie Wilson", "USA", 
                            now.minusYears(4).toString(), "Very old experience", "Boeing 747", 
                            "Business", "First", "ATL-CDG", 2.0, 1.5, 2.0, 1.5, 2.0, 1.5, 1.0, 2.0, 0),
            
            new AirlineReview("United", "link6", "Ancient review", "Diana Davis", "USA", 
                            now.minusYears(5).toString(), "Very outdated", "Boeing 737", 
                            "Leisure", "Economy", "DEN-LAX", 1.5, 1.0, 1.5, 1.0, 1.5, 1.0, 0.5, 1.5, 0),
            
            // More recent reviews for better testing
            new AirlineReview("American", "link7", "Recent American flight", "Eve Miller", "USA", 
                            now.minusDays(10).toString(), "Good service", "Boeing 737", 
                            "Business", "Economy", "DFW-LAX", 4.2, 4.0, 4.2, 3.8, 4.0, 3.8, 3.0, 4.2, 1),
            
            new AirlineReview("American", "link8", "Another recent flight", "Frank Garcia", "USA", 
                            now.minusDays(25).toString(), "Satisfactory", "Airbus A321", 
                            "Leisure", "Economy", "LAX-ORD", 3.8, 3.5, 3.8, 3.5, 3.8, 3.5, 2.8, 3.8, 1)
        );
    }
    
    public void testBasicOperations() {
        System.out.println("Testing basic store operations...");
        
        if (store.size() != 8) {
            throw new AssertionError("Expected size 8, got " + store.size());
        }
        
        store.clear();
        if (store.size() != 0) {
            throw new AssertionError("Expected size 0 after clear, got " + store.size());
        }
        
        store.addReview(testReviews.get(0));
        if (store.size() != 1) {
            throw new AssertionError("Expected size 1 after adding one review, got " + store.size());
        }
        
        // Restore all test data for subsequent tests
        store.clear();
        store.addReviews(testReviews);
        
        System.out.println("✓ Basic operations test passed");
    }
    
    public void testTopKRecentRetrieval() {
        System.out.println("Testing top-k recent retrieval for Delta...");
        
        List<ReviewRecord> top2Delta = store.getTopKRecentReviews("Delta", 2);
        
        if (top2Delta.size() != 2) {
            throw new AssertionError("Expected 2 reviews, got " + top2Delta.size());
        }
        
        // Verify they are sorted by date (most recent first)
        LocalDate firstDate = LocalDate.parse(top2Delta.get(0).getDate());
        LocalDate secondDate = LocalDate.parse(top2Delta.get(1).getDate());
        
        if (firstDate.isBefore(secondDate)) {
            throw new AssertionError("Reviews not sorted by date correctly");
        }
        
        // Verify all returned reviews are for Delta
        for (ReviewRecord review : top2Delta) {
            if (!review.getName().equals("Delta")) {
                throw new AssertionError("Found non-Delta review in Delta results");
            }
        }
        
        System.out.println("✓ Top-k recent retrieval test passed");
    }
    
    public void testTopKRecentRetrievalNonExistent() {
        System.out.println("Testing top-k recent retrieval for non-existent airline...");
        
        List<ReviewRecord> result = store.getTopKRecentReviews("NonExistentAirline", 5);
        if (!result.isEmpty()) {
            throw new AssertionError("Expected empty result for non-existent airline");
        }
        
        System.out.println("✓ Non-existent airline test passed");
    }
    
    public void testRecencyBiasedAverageRating() {
        System.out.println("Testing recency-biased average rating calculation...");
        
        double deltaRBAR = store.calculateRecencyBiasedAverageRating("Delta");
        double americanRBAR = store.calculateRecencyBiasedAverageRating("American");
        double nonExistentRBAR = store.calculateRecencyBiasedAverageRating("NonExistent");
        
        // Delta should have a higher RBAR due to recent high-rated reviews
        if (deltaRBAR <= 0 || deltaRBAR >= 5.0) {
            throw new AssertionError("Delta RBAR out of valid range: " + deltaRBAR);
        }
        
        // American should also have a reasonable RBAR
        if (americanRBAR <= 0 || americanRBAR >= 5.0) {
            throw new AssertionError("American RBAR out of valid range: " + americanRBAR);
        }
        
        // Non-existent airline should return 0
        if (nonExistentRBAR != 0.0) {
            throw new AssertionError("Expected 0.0 for non-existent airline, got " + nonExistentRBAR);
        }
        
        // Delta should have higher RBAR than United due to more recent reviews
        double unitedRBAR = store.calculateRecencyBiasedAverageRating("United");
        if (deltaRBAR <= unitedRBAR) {
            throw new AssertionError("Delta RBAR should be higher than United RBAR");
        }
        
        System.out.println("✓ Recency-biased average rating test passed");
        System.out.println("  Delta RBAR: " + String.format("%.3f", deltaRBAR));
        System.out.println("  American RBAR: " + String.format("%.3f", americanRBAR));
        System.out.println("  United RBAR: " + String.format("%.3f", unitedRBAR));
    }
    
    public void testGetReviewsByAirline() {
        System.out.println("Testing get reviews by airline...");
        
        List<ReviewRecord> deltaReviews = store.getReviewsByAirline("Delta");
        List<ReviewRecord> americanReviews = store.getReviewsByAirline("American");
        List<ReviewRecord> nonExistentReviews = store.getReviewsByAirline("NonExistent");
        
        if (deltaReviews.size() != 4) {
            throw new AssertionError("Expected 4 Delta reviews, got " + deltaReviews.size());
        }
        if (americanReviews.size() != 2) {
            throw new AssertionError("Expected 2 American reviews, got " + americanReviews.size());
        }
        if (!nonExistentReviews.isEmpty()) {
            throw new AssertionError("Expected empty result for non-existent airline");
        }
        
        // Verify all returned reviews are for the correct airline
        for (ReviewRecord review : deltaReviews) {
            if (!review.getName().equals("Delta")) {
                throw new AssertionError("Found non-Delta review in Delta results");
            }
        }
        for (ReviewRecord review : americanReviews) {
            if (!review.getName().equals("American")) {
                throw new AssertionError("Found non-American review in American results");
            }
        }
        
        System.out.println("✓ Get reviews by airline test passed");
    }
    
    public void testGetAllAirlines() {
        System.out.println("Testing get all airlines...");
        
        var airlines = store.getAllAirlines();
        
        if (airlines.size() != 3) {
            throw new AssertionError("Expected 3 airlines, got " + airlines.size());
        }
        if (!airlines.contains("Delta") || !airlines.contains("United") || !airlines.contains("American")) {
            throw new AssertionError("Missing expected airlines");
        }
        
        System.out.println("✓ Get all airlines test passed");
    }
    
    public void testStatistics() {
        System.out.println("Testing statistics generation...");
        
        Map<String, Object> stats = store.getStatistics();
        
        if (!stats.get("totalReviews").equals(8)) {
            throw new AssertionError("Expected 8 total reviews in stats");
        }
        if (!stats.get("uniqueAirlines").equals(3)) {
            throw new AssertionError("Expected 3 unique airlines in stats");
        }
        
        if (stats.get("oldestReview") == null || stats.get("newestReview") == null) {
            throw new AssertionError("Missing date information in stats");
        }
        
        LocalDate oldestDate = (LocalDate) stats.get("oldestReview");
        LocalDate newestDate = (LocalDate) stats.get("newestReview");
        
        if (newestDate.isBefore(oldestDate)) {
            throw new AssertionError("Newest date is before oldest date");
        }
        
        System.out.println("✓ Statistics test passed");
        System.out.println("  Total reviews: " + stats.get("totalReviews"));
        System.out.println("  Unique airlines: " + stats.get("uniqueAirlines"));
        System.out.println("  Date range: " + oldestDate + " to " + newestDate);
    }
    
    public void runAllTests() {
        System.out.println("=== Running LinearListReviewStore Tests ===");
        System.out.println();
        
        setUp();
        
        try {
            testBasicOperations();
            testTopKRecentRetrieval();
            testTopKRecentRetrievalNonExistent();
            testRecencyBiasedAverageRating();
            testGetReviewsByAirline();
            testGetAllAirlines();
            testStatistics();
            
            System.out.println();
            System.out.println("🎉 All tests passed successfully!");
            
        } catch (AssertionError e) {
            System.err.println("❌ Test failed: " + e.getMessage());
            throw e;
        }
    }
    
    public static void main(String[] args) {
        LinearListReviewStoreTest test = new LinearListReviewStoreTest();
        test.runAllTests();
    }
}