package com.reviews.experiments.experiment2;

import com.reviews.Models.AirlineReview;
import com.reviews.Models.ReviewRecord;
import com.reviews.datastructures.AVLReviewStore;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Demonstration class for the AVLReviewStore implementation.
 * Shows how the AVL tree performs for recency-biased operations and demonstrates
 * the advantages of balanced tree structure over linear list.
 */
public class AVLDemo {
    
    public static void main(String[] args) {
        System.out.println("=== AVLReviewStore Experiment 2 Demonstration ===");
        System.out.println();
        
        // Create store and add sample data
        AVLReviewStore store = new AVLReviewStore();
        addSampleData(store);
        
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
    
    private static void addSampleData(AVLReviewStore store) {
        System.out.println("Adding sample airline review data to AVL tree...");
        
        LocalDate now = LocalDate.now();
        
        // Add reviews for Delta with different dates
        store.addReview(new AirlineReview("Delta", "link1", "Excellent recent flight", "John Doe", "USA", 
                        now.minusDays(5).toString(), "Outstanding service and comfort", "Boeing 737", 
                        "Business", "Economy", "LAX-JFK", 4.8, 4.5, 4.8, 4.2, 4.0, 4.5, 3.5, 4.8, 1));
        
        store.addReview(new AirlineReview("Delta", "link2", "Good experience", "Jane Smith", "USA", 
                        now.minusDays(15).toString(), "Comfortable flight with good service", "Airbus A320", 
                        "Leisure", "Economy", "SFO-LAX", 4.2, 4.0, 4.2, 3.8, 4.0, 3.8, 3.0, 4.2, 1));
        
        store.addReview(new AirlineReview("Delta", "link3", "Decent but old", "Bob Johnson", "USA", 
                        now.minusDays(60).toString(), "Average experience from months ago", "Boeing 777", 
                        "Business", "Business", "JFK-LHR", 3.5, 3.0, 3.5, 3.0, 3.5, 3.0, 2.0, 3.5, 0));
        
        store.addReview(new AirlineReview("Delta", "link4", "Very old review", "Charlie Wilson", "USA", 
                        now.minusYears(4).toString(), "Very outdated experience", "Boeing 747", 
                        "Business", "First", "ATL-CDG", 2.0, 1.5, 2.0, 1.5, 2.0, 1.5, 1.0, 2.0, 0));
        
        // Add reviews for United
        store.addReview(new AirlineReview("United", "link5", "Recent United flight", "Alice Brown", "USA", 
                        now.minusDays(10).toString(), "Good recent experience", "Boeing 787", 
                        "Leisure", "Economy", "ORD-NRT", 4.0, 3.5, 4.0, 3.5, 4.0, 3.5, 2.5, 4.0, 1));
        
        store.addReview(new AirlineReview("United", "link6", "Old United review", "Diana Davis", "USA", 
                        now.minusYears(5).toString(), "Very outdated experience", "Boeing 737", 
                        "Leisure", "Economy", "DEN-LAX", 1.5, 1.0, 1.5, 1.0, 1.5, 1.0, 0.5, 1.5, 0));
        
        // Add reviews for American
        store.addReview(new AirlineReview("American", "link7", "Recent American flight", "Eve Miller", "USA", 
                        now.minusDays(8).toString(), "Good service recently", "Boeing 737", 
                        "Business", "Economy", "DFW-LAX", 4.3, 4.0, 4.3, 3.8, 4.0, 3.8, 3.0, 4.3, 1));
        
        store.addReview(new AirlineReview("American", "link8", "Another recent flight", "Frank Garcia", "USA", 
                        now.minusDays(20).toString(), "Satisfactory recent experience", "Airbus A321", 
                        "Leisure", "Economy", "LAX-ORD", 3.9, 3.5, 3.9, 3.5, 3.9, 3.5, 2.8, 3.9, 1));
        
        // Add more airlines to demonstrate AVL tree balancing
        store.addReview(new AirlineReview("Southwest", "link9", "Southwest review", "Grace Lee", "USA", 
                        now.minusDays(12).toString(), "Good value", "Boeing 737", 
                        "Leisure", "Economy", "DAL-HOU", 4.1, 4.0, 4.1, 3.9, 4.0, 3.9, 3.1, 4.1, 1));
        
        store.addReview(new AirlineReview("JetBlue", "link10", "JetBlue review", "Henry Kim", "USA", 
                        now.minusDays(18).toString(), "Nice experience", "Airbus A320", 
                        "Business", "Economy", "JFK-BOS", 4.3, 4.0, 4.3, 3.8, 4.0, 3.8, 3.0, 4.3, 1));
        
        store.addReview(new AirlineReview("Alaska", "link11", "Alaska review", "Ivy Chen", "USA", 
                        now.minusDays(7).toString(), "Great service", "Boeing 737", 
                        "Leisure", "Economy", "SEA-LAX", 4.4, 4.0, 4.4, 3.9, 4.0, 3.9, 3.1, 4.4, 1));
        
        System.out.println("✓ Added " + store.size() + " sample reviews");
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
