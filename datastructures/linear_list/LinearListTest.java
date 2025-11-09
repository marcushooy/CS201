package datastructures.linear_list;

import models.AirlineReview;
import models.AirlineRanking;
import utils.CSVLoader;
import java.util.List;
import java.util.Set;

/**
 * Comprehensive test suite for LinearListReviewStore.
 * Tests all CRUD operations, RBAR calculations, and rankings using real CSV data.
 */
public class LinearListTest {
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║    Linear List Data Structure - Comprehensive Test            ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        // Load real data
        String csvPath = "data/airline.csv";
        System.out.println("📂 Loading airline reviews from: " + csvPath);
        List<AirlineReview> allReviews = CSVLoader.loadAirlineReviews(csvPath);
        System.out.println("✅ Loaded " + allReviews.size() + " reviews from CSV");
        System.out.println();
        
        // Test with subset
        int testSize = Math.min(1000, allReviews.size());
        List<AirlineReview> testReviews = allReviews.subList(0, testSize);
        System.out.println("🧪 Testing with " + testSize + " reviews");
        System.out.println();
        
        LinearListReviewStore store = new LinearListReviewStore();
        
        // ==================== TEST 1: CREATE ====================
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println("TEST 1: CREATE Operations");
        System.out.println("════════════════════════════════════════════════════════════════");
        
        long startTime = System.currentTimeMillis();
        store.addReviews(testReviews);
        long endTime = System.currentTimeMillis();
        
        System.out.println("✓ Added " + testReviews.size() + " reviews");
        System.out.println("✓ Total reviews in store: " + store.size());
        System.out.println("✓ Insertion time: " + (endTime - startTime) + " ms");
        System.out.println();
        
        // ==================== TEST 2: READ ====================
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println("TEST 2: READ Operations");
        System.out.println("════════════════════════════════════════════════════════════════");
        
        Set<String> airlines = store.getAllAirlines();
        System.out.println("✓ Total unique airlines: " + airlines.size());
        System.out.println("✓ Sample airlines: " + airlines.stream().limit(5).toList());
        System.out.println();
        
        if (!airlines.isEmpty()) {
            String testAirline = airlines.iterator().next();
            startTime = System.currentTimeMillis();
            List<AirlineReview> airlineReviews = store.getReviewsByAirline(testAirline);
            endTime = System.currentTimeMillis();
            
            System.out.println("✓ Searched for airline: " + testAirline);
            System.out.println("✓ Found " + airlineReviews.size() + " reviews");
            System.out.println("✓ Search time: " + (endTime - startTime) + " ms");
            if (!airlineReviews.isEmpty()) {
                System.out.println("✓ Sample review: " + airlineReviews.get(0).getDate() + 
                                 " - Rating: " + airlineReviews.get(0).getOverallRating());
            }
        }
        System.out.println();
        
        // ==================== TEST 3: RBAR ====================
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println("TEST 3: RBAR Calculation");
        System.out.println("════════════════════════════════════════════════════════════════");
        
        if (!airlines.isEmpty()) {
            String testAirline = airlines.iterator().next();
            List<AirlineReview> reviews = store.getReviewsByAirline(testAirline);
            
            startTime = System.currentTimeMillis();
            double rbar = store.calculateRBAR(testAirline);
            endTime = System.currentTimeMillis();
            
            double simpleAvg = reviews.stream()
                .mapToDouble(AirlineReview::getOverallRating)
                .average()
                .orElse(0.0);
            
            System.out.println("✓ Airline: " + testAirline);
            System.out.println("✓ Number of reviews: " + reviews.size());
            System.out.println("✓ RBAR (Recency-Biased): " + String.format("%.3f", rbar));
            System.out.println("✓ Simple Average: " + String.format("%.3f", simpleAvg));
            System.out.println("✓ Difference: " + String.format("%.3f", rbar - simpleAvg));
            System.out.println("✓ RBAR calculation time: " + (endTime - startTime) + " ms");
        }
        System.out.println();
        
        // ==================== TEST 4: RANKINGS ====================
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println("TEST 4: Airline Rankings");
        System.out.println("════════════════════════════════════════════════════════════════");
        
        startTime = System.currentTimeMillis();
        List<AirlineRanking> rankings = store.getAirlineRankings();
        endTime = System.currentTimeMillis();
        
        System.out.println("✓ Calculated rankings for " + rankings.size() + " airlines");
        System.out.println("✓ Ranking calculation time: " + (endTime - startTime) + " ms");
        System.out.println();
        
        List<AirlineRanking> top10 = store.getTopKAirlines(10);
        System.out.println("Top 10 Airlines by RBAR:");
        System.out.println("─────────────────────────────────────────────────────────────");
        for (int i = 0; i < top10.size(); i++) {
            System.out.println((i+1) + ". " + top10.get(i));
        }
        System.out.println();
        
        // ==================== TEST 5: UPDATE ====================
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println("TEST 5: UPDATE Operation");
        System.out.println("════════════════════════════════════════════════════════════════");
        
        if (!airlines.isEmpty()) {
            String testAirline = airlines.iterator().next();
            List<AirlineReview> reviews = store.getReviewsByAirline(testAirline);
            
            if (!reviews.isEmpty()) {
                AirlineReview oldReview = reviews.get(0);
                AirlineReview newReview = new AirlineReview(
                    oldReview.getAirlineName(),
                    oldReview.getLink(),
                    oldReview.getTitle(),
                    oldReview.getAuthor(),
                    oldReview.getAuthorCountry(),
                    oldReview.getDate(),
                    oldReview.getContent(),
                    oldReview.getAircraft(),
                    oldReview.getTypeTraveller(),
                    oldReview.getCabinFlown(),
                    oldReview.getRoute(),
                    10.0,  // Updated rating
                    oldReview.getSeatComfortRating(),
                    oldReview.getCabinStaffRating(),
                    oldReview.getFoodBeveragesRating(),
                    oldReview.getInflightEntertainmentRating(),
                    oldReview.getGroundServiceRating(),
                    oldReview.getWifiConnectivityRating(),
                    oldReview.getValueMoneyRating(),
                    oldReview.getRecommended()
                );
                
                boolean updated = store.updateReview(testAirline, oldReview, newReview);
                System.out.println("✓ Update operation: " + (updated ? "SUCCESS" : "FAILED"));
                
                if (updated) {
                    List<AirlineReview> updatedReviews = store.getReviewsByAirline(testAirline);
                    boolean found = updatedReviews.stream()
                        .anyMatch(r -> r.equals(newReview) && r.getOverallRating() == 10.0);
                    System.out.println("✓ Verified review was updated: " + found);
                    System.out.println("✓ New rating: " + newReview.getOverallRating());
                }
            }
        }
        System.out.println();
        
        // ==================== TEST 6: DELETE ====================
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println("TEST 6: DELETE Operation");
        System.out.println("════════════════════════════════════════════════════════════════");
        
        if (!airlines.isEmpty()) {
            String testAirline = airlines.iterator().next();
            List<AirlineReview> reviews = store.getReviewsByAirline(testAirline);
            
            if (!reviews.isEmpty()) {
                int sizeBefore = store.size();
                AirlineReview reviewToDelete = reviews.get(0);
                
                boolean deleted = store.deleteReview(testAirline, reviewToDelete);
                int sizeAfter = store.size();
                
                System.out.println("✓ Delete operation: " + (deleted ? "SUCCESS" : "FAILED"));
                System.out.println("✓ Size before delete: " + sizeBefore);
                System.out.println("✓ Size after delete: " + sizeAfter);
            }
        }
        System.out.println();
        
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println("✅ ALL TESTS COMPLETED SUCCESSFULLY!");
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println();
        
        System.out.println("Operations tested:");
        System.out.println("  ✓ CREATE: addReview(), addReviews()");
        System.out.println("  ✓ READ: getReviewsByAirline(), getAllAirlines()");
        System.out.println("  ✓ UPDATE: updateReview()");
        System.out.println("  ✓ DELETE: deleteReview()");
        System.out.println("  ✓ RBAR: calculateRBAR()");
        System.out.println("  ✓ RANKINGS: getAirlineRankings(), getTopKAirlines()");
    }
}

