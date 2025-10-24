package com.reviews.experiments.LinearListTest;

/**
 * Main class for Experiment 1: Linear List Baseline Implementation
 * 
 * This experiment implements and tests a baseline linear list data structure
 * for storing airline reviews with recency-biased operations.
 * 
 * Time Complexities:
 * - Insertion: O(1) amortized
 * - Top-k Recent Retrieval: O(N log N) due to sorting
 * - Recency-Biased Average Rating: O(N)
 * - Search by airline: O(N)
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Experiment 1: Linear List Baseline ===");
        System.out.println();
        
        // Run the comprehensive demonstration
        LinearListDemo.main(args);  
        
        System.out.println();
        System.out.println("=== Experiment 1 Complete ===");
        System.out.println("This baseline establishes O(N log N) performance for top-k retrieval");
        System.out.println("and O(N) performance for recency-biased average rating calculations.");
        System.out.println("Future experiments will compare this against more sophisticated");
        System.out.println("data structures optimized for recency-biased access patterns.");
    }
}
