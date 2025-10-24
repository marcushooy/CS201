package com.reviews.experiments.experiment2;

import com.reviews.experiments.experiment2.AVLDemo;
/**
 * Main class for Experiment 2: AVL Tree Implementation
 * 
 * This experiment implements and tests an AVL tree data structure
 * for storing airline reviews with recency-biased operations.
 * 
 * Time Complexities:
 * - Insertion: O(log N)
 * - Search by airline: O(log N)
 * - Top-k Recent Retrieval: O(log N + N log N) = O(N log N) due to sorting
 * - Recency-Biased Average Rating: O(log N + N) = O(N)
 * 
 * Space Complexity: O(N)
 * 
 * Key Advantages over Linear List:
 * - O(log N) search vs O(N) search
 * - Maintains balance automatically
 * - Better performance for large datasets
 */
public class Main2 {
    public static void main(String[] args) {
        System.out.println("=== Experiment 2: AVL Tree Implementation ===");
        System.out.println();
        
        // Run the comprehensive demonstration
        AVLDemo.main(args);  
        
        System.out.println();
        System.out.println("=== Experiment 2 Complete ===");
        System.out.println("This AVL tree implementation provides O(log N) performance for search operations");
        System.out.println("and maintains automatic balance, making it superior to the linear list baseline");
        System.out.println("for datasets with many unique airlines. The recency-biased operations maintain");
        System.out.println("the same weighting algorithm as Experiment 1 for fair comparison.");
        System.out.println();
        System.out.println("Key Performance Improvements:");
        System.out.println("- Search: O(N) → O(log N)");
        System.out.println("- Insertion: O(1) → O(log N) (with automatic balancing)");
        System.out.println("- Tree height guaranteed to be O(log N)");
        System.out.println("- Balanced tree ensures consistent performance");
    }
}
