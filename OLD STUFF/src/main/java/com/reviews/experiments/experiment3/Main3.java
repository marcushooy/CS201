package com.reviews.experiments.experiment3;

/**
 * Main class for Experiment 3: Recency-Biased Tree (RBT) Implementation
 * 
 * This experiment implements and tests a Recency-Biased Tree data structure
 * for storing airline reviews with recency-biased operations.
 * 
 * Time Complexities:
 * - Insertion: O(log N) with splay operations
 * - Search by airline: O(log N) to O(N) depending on recency
 * - Top-k Recent Retrieval: O(k) - recent reviews clustered near root
 * - Recency-Biased Average Rating: O(log N + N) = O(N) but with early-stop optimization
 * 
 * Space Complexity: O(N)
 * 
 * Key Design Philosophy:
 * - Intentionally trades global balance for fast access to recent data
 * - Recent reviews are splayed to root for O(1) access
 * - Old reviews sink to bottom for O(N) worst-case access
 * - Optimized for recency-weighted operations where recent > old
 */
public class Main3 {
    public static void main(String[] args) {
        System.out.println("=== Experiment 3: Recency-Biased Tree (RBT) Implementation ===");
        System.out.println();
        
        // Run the comprehensive demonstration
        RBTDemo.main(args);  
        
        System.out.println();
        System.out.println("=== Experiment 3 Complete ===");
        System.out.println("This RBT implementation provides specialized performance for recency-biased operations.");
        System.out.println("Recent reviews are splayed to the root, providing O(1) to O(log N) access times,");
        System.out.println("while older reviews sink to deeper levels with O(N) worst-case access.");
        System.out.println();
        System.out.println("Key Performance Characteristics:");
        System.out.println("- Recent data access: O(1) to O(log N) - highly optimized");
        System.out.println("- Old data access: O(N) worst case - intentionally slow");
        System.out.println("- Ideal for scenarios where recent reviews are accessed more frequently");
        System.out.println("- Splay operations move frequently accessed data to root automatically");
    }
}
