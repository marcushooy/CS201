package com.reviews.experiments.experiment2;

import com.reviews.Models.AirlineReview;
import com.reviews.Models.ReviewRecord;
import com.reviews.datastructures.AVLReviewStore;
import com.reviews.utils.CSVLoader;

import java.time.LocalDate;
import java.util.List;
//import java.util.Map;

/**
 * Demonstration class for the AVLReviewStore implementation.
 * Shows how the AVL tree performs for recency-biased operations and demonstrates
 * the advantages of balanced tree structure over linear list.
 */
public class AVLDemo {
    
    public static void main(String[] args) {
        System.out.println("=== AVLReviewStore Experiment 2 Demonstration ===");
        System.out.println();
        
        // Create store and load REAL data
        AVLReviewStore store = new AVLReviewStore();
        addRealData(store, 10000); // cap for demo speed; use -1 for full file
        
        // Demonstrate basic operations
        demonstrateBasicOperations(store);
        
        // Demonstrate AVL tree specific features
        demonstrateAVLTreeFeatures(store);
        
        // Demonstrate recency-biased operations
        demonstrateRecencyBiasedOperations(store);
        
        // Run performance tests
        demonstratePerformance(store);
        
        // Run comprehensive tests
        System.out.println("\n=== Running Test Suite ===");
        AVLReviewStoreTest test = new AVLReviewStoreTest();
        test.runAllTests();
    }
    
    private static void addRealData(AVLReviewStore store, int limit) {
        String path = CSVLoader.getAirlineCSVPath();
        System.out.println("Loading REAL airline reviews from " + path + "...");
        List<ReviewRecord> all = CSVLoader.loadAirlineReviews(path);
        List<ReviewRecord> reviews = (limit > 0 && limit < all.size()) ? all.subList(0, limit) : all;
        store.addReviews(reviews);
        System.out.println("✓ Loaded " + reviews.size() + " reviews");
        System.out.println("✓ Airlines: " + store.getAllAirlines());
        System.out.println();
    }
    
    private static void demonstrateBasicOperations(AVLReviewStore store) {
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
    
    private static void demonstrateAVLTreeFeatures(AVLReviewStore store) {
        System.out.println("=== AVL Tree Specific Features Demo ===");
        
        // Show AVL tree statistics
        var treeStats = store.getTreeStatistics();
        System.out.println("AVL Tree Statistics:");
        System.out.println("  Tree Height: " + treeStats.get("treeHeight"));
        System.out.println("  Is Balanced: " + treeStats.get("isBalanced"));
        System.out.println("  Average Reviews per Airline: " + String.format("%.2f", treeStats.get("avgReviewsPerAirline")));
        System.out.println();
        
        // Demonstrate search efficiency
        System.out.println("Search Performance Demo:");
        String[] airlines = {"Delta", "United", "American", "Southwest", "JetBlue", "Alaska"};
        
        for (String airline : airlines) {
            long startTime = System.nanoTime();
            List<ReviewRecord> reviews = store.getReviewsByAirline(airline);
            long endTime = System.nanoTime();
            
            double searchTimeMs = (endTime - startTime) / 1_000_000.0;
            System.out.println("  " + airline + ": " + reviews.size() + " reviews found in " + 
                             String.format("%.3f", searchTimeMs) + " ms");
        }
        System.out.println();
        
        // Demonstrate tree balancing with ordered insertion
        System.out.println("AVL Tree Balancing Demo:");
        AVLReviewStore balancedStore = new AVLReviewStore();
        
        // Insert airlines in alphabetical order (worst case for regular BST)
        String[] orderedAirlines = {"American", "Delta", "JetBlue", "Southwest", "United"};
        for (String airline : orderedAirlines) {
            AirlineReview review = new AirlineReview(airline, "link", "Test", "Author", "USA", 
                                                   LocalDate.now().toString(), "Test content", 
                                                   "Boeing 737", "Business", "Economy", "Route", 
                                                   4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 3.0, 4.0, 1);
            balancedStore.addReview(review);
        }
        
        System.out.println("  Ordered insertion height: " + balancedStore.getTreeHeight());
        System.out.println("  Is balanced after ordered insertion: " + balancedStore.isBalanced());
        System.out.println();
    }
    
    private static void demonstrateRecencyBiasedOperations(AVLReviewStore store) {
        System.out.println("=== Recency-Biased Operations Demo ===");
        
        // Demonstrate top-k recent retrieval
        System.out.println("Top-3 Most Recent Reviews by Airline:");
        for (String airline : store.getAllAirlines()) {
            List<ReviewRecord> top3 = store.getTopKRecentReviews(airline, 3);
            System.out.println("  " + airline + ":");
            for (int i = 0; i < top3.size(); i++) {
                ReviewRecord review = top3.get(i);
                System.out.println("    " + (i+1) + ". " + review.getDate() + " - Rating: " + 
                                 String.format("%.1f", review.getOverallRating()) + " - " + 
                                 review.getContent().substring(0, Math.min(50, review.getContent().length())) + "...");
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
    
    private static void demonstratePerformance(AVLReviewStore store) {
        System.out.println("=== Performance Demo ===");
        
        // Measure search performance
        System.out.println("Measuring Search Performance:");
        int iterations = 1000;
        
        long startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            for (String airline : store.getAllAirlines()) {
                store.getReviewsByAirline(airline);
            }
        }
        long endTime = System.nanoTime();
        double avgSearchTimeMs = ((endTime - startTime) / (double) (iterations * store.getAllAirlines().size())) / 1_000_000.0;
        
        System.out.println("  Search by airline: " + String.format("%.3f", avgSearchTimeMs) + " ms per operation");
        
        // Measure top-k retrieval performance
        System.out.println();
        System.out.println("Measuring Top-K Recent Retrieval Performance:");
        
        int[] kValues = {5, 10, 25};
        
        for (int k : kValues) {
            long startTime2 = System.nanoTime();
            
            for (int i = 0; i < iterations; i++) {
                for (String airline : store.getAllAirlines()) {
                    store.getTopKRecentReviews(airline, k);
                }
            }
            
            long endTime2 = System.nanoTime();
            double avgTimeMs = ((endTime2 - startTime2) / (double) (iterations * store.getAllAirlines().size())) / 1_000_000.0;
            
            System.out.println("  Top-" + k + " retrieval: " + String.format("%.3f", avgTimeMs) + " ms per operation");
        }
        
        // Measure RBAR calculation performance
        System.out.println();
        System.out.println("Measuring RBAR Calculation Performance:");
        
        long startTime3 = System.nanoTime();
        
        for (int i = 0; i < iterations; i++) {
            for (String airline : store.getAllAirlines()) {
                store.calculateRecencyBiasedAverageRating(airline);
            }
        }
        
        long endTime3 = System.nanoTime();
        double avgRBARTimeMs = ((endTime3 - startTime3) / (double) (iterations * store.getAllAirlines().size())) / 1_000_000.0;
        
        System.out.println("  RBAR calculation: " + String.format("%.3f", avgRBARTimeMs) + " ms per operation");
        System.out.println();
        
        // Show time complexity analysis
        System.out.println("Time Complexity Analysis:");
        System.out.println("  Insert: O(log N)");
        System.out.println("  Search by airline: O(log N)");
        System.out.println("  Top-K Recent Retrieval: O(log N + N log N) = O(N log N) due to sorting");
        System.out.println("  RBAR Calculation: O(log N + N) = O(N)");
        System.out.println();
        
        // Compare with linear list theoretical performance
        System.out.println("Theoretical Performance Comparison:");
        System.out.println("  Linear List Search: O(N)");
        System.out.println("  AVL Tree Search: O(log N)");
        System.out.println("  Improvement Factor: O(N/log N)");
        System.out.println("  For " + store.getAllAirlines().size() + " airlines: ~" + 
                         String.format("%.1f", store.getAllAirlines().size() / Math.log(store.getAllAirlines().size())) + "x faster");
        System.out.println();
    }
}
