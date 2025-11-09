package com.reviews.experiments.experiment1;

import com.reviews.Models.AirlineReview;
import com.reviews.Models.ReviewRecord;
import com.reviews.datastructures.LinearListReviewStore;
import com.reviews.utils.CSVLoader;

import java.time.LocalDate;
import java.util.List;

/**
 * Demonstration class for the LinearListReviewStore baseline implementation.
 * Shows how the baseline performs for recency-biased operations.
 */
public class LinearListDemo {
    
    public static void main(String[] args) {
        System.out.println("=== LinearListReviewStore Baseline Demonstration ===");
        System.out.println();
        
        // Create store and load REAL data
        LinearListReviewStore store = new LinearListReviewStore();
        addRealData(store, 10000); // use a cap for fast demo; set to -1 for full file
        
        // Demonstrate basic operations
        demonstrateBasicOperations(store);
        
        // Demonstrate recency-biased operations
        demonstrateRecencyBiasedOperations(store);
        
        // Run performance tests
        demonstratePerformance(store);
        
        // Run comprehensive tests
        System.out.println("\n=== Running Test Suite ===");
        LinearListReviewStoreTest test = new LinearListReviewStoreTest();
        test.runAllTests();
    }
    
    private static void addRealData(LinearListReviewStore store, int limit) {
        String path = CSVLoader.getAirlineCSVPath();
        System.out.println("Loading REAL airline reviews from " + path + "...");
        List<ReviewRecord> all = CSVLoader.loadAirlineReviews(path);
        List<ReviewRecord> reviews = (limit > 0 && limit < all.size()) ? all.subList(0, limit) : all;
        store.addReviews(reviews);
        System.out.println("✓ Loaded " + reviews.size() + " reviews");
        System.out.println("✓ Airlines: " + store.getAllAirlines());
        System.out.println();
    }
    
    private static void demonstrateBasicOperations(LinearListReviewStore store) {
        System.out.println("=== Basic Operations Demo ===");
        
        // Show statistics
        var stats = store.getStatistics();
        System.out.println("Store Statistics:");
        System.out.println("  Total reviews: " + stats.get("totalReviews"));
        System.out.println("  Unique airlines: " + stats.get("uniqueAirlines"));
        System.out.println("  Date range: " + stats.get("oldestReview") + " to " + stats.get("newestReview"));
        System.out.println();
        
        // Show reviews by airline
        for (String airline : store.getAllAirlines()) {
            List<ReviewRecord> reviews = store.getReviewsByAirline(airline);
            System.out.println(airline + " has " + reviews.size() + " reviews");
        }
        System.out.println();
    }
    
    private static void demonstrateRecencyBiasedOperations(LinearListReviewStore store) {
        System.out.println("=== Recency-Biased Operations Demo ===");
        
        // Demonstrate top-k recent retrieval
        System.out.println("Top-3 Most Recent Reviews by Airline:");
        for (String airline : store.getAllAirlines()) {
            List<ReviewRecord> top3 = store.getTopKRecentReviews(airline, 3);
            System.out.println("  " + airline + ":");
            for (int i = 0; i < top3.size(); i++) {
                ReviewRecord review = top3.get(i);
                System.out.println("    " + (i+1) + ". " + review.getDate() + " - Rating: " + 
                                 String.format("%.1f", review.getOverallRating()) + " - " + review.getContent().substring(0, Math.min(50, review.getContent().length())) + "...");
            }
        }
        System.out.println();
        
        // Demonstrate recency-biased average rating calculation
        System.out.println("Recency-Biased Average Ratings (RB-AR):");
        System.out.println("Note: Recent reviews (last 30 days) have high weight, old reviews (3+ years) have low weight");
        System.out.println();
        
        for (String airline : store.getAllAirlines()) {
            double rbar = store.calculateRecencyBiasedAverageRating(airline);
            List<ReviewRecord> reviews = store.getReviewsByAirline(airline);
            
            // Calculate simple average for comparison
            double simpleAvg = reviews.stream()
                    .mapToDouble(ReviewRecord::getOverallRating)
                    .average()
                    .orElse(0.0);
            
            System.out.println("  " + airline + ":");
            System.out.println("    RB-AR: " + String.format("%.3f", rbar));
            System.out.println("    Simple Average: " + String.format("%.3f", simpleAvg));
            System.out.println("    Difference: " + String.format("%+.3f", rbar - simpleAvg));
            System.out.println();
        }
    }
    
    private static void demonstratePerformance(LinearListReviewStore store) {
        System.out.println("=== Performance Demo ===");
        
        // Measure top-k retrieval performance
        System.out.println("Measuring Top-K Recent Retrieval Performance:");
        
        int[] kValues = {5, 10, 25};
        int iterations = 1000;
        
        for (int k : kValues) {
            long startTime = System.nanoTime();
            
            for (int i = 0; i < iterations; i++) {
                for (String airline : store.getAllAirlines()) {
                    store.getTopKRecentReviews(airline, k);
                }
            }
            
            long endTime = System.nanoTime();
            double avgTimeMs = ((endTime - startTime) / (double) (iterations * store.getAllAirlines().size())) / 1_000_000.0;
            
            System.out.println("  Top-" + k + " retrieval: " + String.format("%.3f", avgTimeMs) + " ms per operation");
        }
        
        // Measure RBAR calculation performance
        System.out.println();
        System.out.println("Measuring RBAR Calculation Performance:");
        
        long startTime = System.nanoTime();
        
        for (int i = 0; i < iterations; i++) {
            for (String airline : store.getAllAirlines()) {
                store.calculateRecencyBiasedAverageRating(airline);
            }
        }
        
        long endTime = System.nanoTime();
        double avgTimeMs = ((endTime - startTime) / (double) (iterations * store.getAllAirlines().size())) / 1_000_000.0;
        
        System.out.println("  RBAR calculation: " + String.format("%.3f", avgTimeMs) + " ms per operation");
        System.out.println();
        
        // Show time complexity analysis
        System.out.println("Time Complexity Analysis:");
        System.out.println("  Insertion: O(1) amortized");
        System.out.println("  Top-K Recent Retrieval: O(N log N) due to sorting");
        System.out.println("  RBAR Calculation: O(N)");
        System.out.println("  Search by airline: O(N)");
        System.out.println();
    }
}
