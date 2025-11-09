package datastructures.avl_tree;

import models.AirlineReview;
import models.AirlineRanking;
import utils.RBARCalculator;
import utils.RankingUtils;
import java.util.*;

/**
 * AVLReviewStore - AVL Tree implementation for storing and managing airline reviews.
 * 
 * This data structure provides:
 * - O(log N) time complexity for search, insert, update, delete operations
 * - Efficient RBAR (Recency-Biased Average Rating) calculations
 * - Automatic tree balancing to maintain O(log N) performance
 * 
 * Core Operations:
 * - Create: addReview() - Insert a new review
 * - Read: getReviewsByAirline() - Get all reviews for an airline
 * - Update: updateReview() - Update an existing review
 * - Delete: deleteReview() - Remove a review
 * - RBAR: calculateRBAR() - Calculate recency-biased average rating
 * - Rankings: getAirlineRankings() - Get airlines ranked by RBAR
 */
public class AVLReviewStore {
    private AVLNode root;
    private int totalReviews;
    
    public AVLReviewStore() {
        this.root = null;
        this.totalReviews = 0;
    }
    
    // ==================== CREATE OPERATION ====================
    
    /**
     * Add a new review to the store.
     * Time Complexity: O(log N)
     */
    public void addReview(AirlineReview review) {
        root = insert(root, review);
        totalReviews++;
    }
    
    /**
     * Add multiple reviews at once.
     * Time Complexity: O(m log N) where m is the number of reviews
     */
    public void addReviews(List<AirlineReview> reviews) {
        for (AirlineReview review : reviews) {
            addReview(review);
        }
    }
    
    /**
     * Insert a review into the AVL tree.
     * Maintains AVL balance property.
     */
    private AVLNode insert(AVLNode node, AirlineReview review) {
        // Base case: create new node
        if (node == null) {
            return new AVLNode(review.getAirlineName(), review);
        }
        
        // Compare airline names
        int comparison = review.getAirlineName().compareTo(node.airlineName);
        
        if (comparison < 0) {
            // Insert into left subtree
            node.left = insert(node.left, review);
        } else if (comparison > 0) {
            // Insert into right subtree
            node.right = insert(node.right, review);
        } else {
            // Same airline, add review to existing node
            node.addReview(review);
            return node; // No need to rebalance since we didn't add a new node
        }
        
        // Update height of current node
        node.updateHeight();
        
        // Get balance factor and perform rotations if necessary
        int balance = node.getBalanceFactor();
        
        // Left Left Case
        if (balance > 1 && node.left.getBalanceFactor() >= 0) {
            return rightRotate(node);
        }
        
        // Right Right Case
        if (balance < -1 && node.right.getBalanceFactor() <= 0) {
            return leftRotate(node);
        }
        
        // Left Right Case
        if (balance > 1 && node.left.getBalanceFactor() < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        
        // Right Left Case
        if (balance < -1 && node.right.getBalanceFactor() > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        
        return node;
    }
    
    /**
     * Right rotation for AVL tree balancing.
     */
    private AVLNode rightRotate(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;
        
        // Perform rotation
        x.right = y;
        y.left = T2;
        
        // Update heights
        y.updateHeight();
        x.updateHeight();
        
        return x;
    }
    
    /**
     * Left rotation for AVL tree balancing.
     */
    private AVLNode leftRotate(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;
        
        // Perform rotation
        y.left = x;
        x.right = T2;
        
        // Update heights
        x.updateHeight();
        y.updateHeight();
        
        return y;
    }
    
    // ==================== READ OPERATION ====================
    
    /**
     * Get all reviews for a specific airline.
     * Time Complexity: O(log N)
     */
    public List<AirlineReview> getReviewsByAirline(String airlineName) {
        AVLNode node = search(airlineName);
        if (node == null) {
            return new ArrayList<>();
        }
        return node.getAllReviews();
    }
    
    /**
     * Search for a specific airline in the AVL tree.
     * Time Complexity: O(log N)
     */
    private AVLNode search(String airlineName) {
        return searchHelper(root, airlineName);
    }
    
    private AVLNode searchHelper(AVLNode node, String airlineName) {
        if (node == null) {
            return null;
        }
        
        int comparison = airlineName.compareTo(node.airlineName);
        if (comparison == 0) {
            return node;
        } else if (comparison < 0) {
            return searchHelper(node.left, airlineName);
        } else {
            return searchHelper(node.right, airlineName);
        }
    }
    
    /**
     * Get all unique airline names in the store.
     * Time Complexity: O(N)
     */
    public Set<String> getAllAirlines() {
        Set<String> airlines = new HashSet<>();
        getAllAirlinesHelper(root, airlines);
        return airlines;
    }
    
    private void getAllAirlinesHelper(AVLNode node, Set<String> airlines) {
        if (node != null) {
            airlines.add(node.airlineName);
            getAllAirlinesHelper(node.left, airlines);
            getAllAirlinesHelper(node.right, airlines);
        }
    }
    
    // ==================== UPDATE OPERATION ====================
    
    /**
     * Update a review (replace old review with new one).
     * Time Complexity: O(log N)
     */
    public boolean updateReview(String airlineName, AirlineReview oldReview, AirlineReview newReview) {
        AVLNode node = search(airlineName);
        if (node == null) {
            return false;
        }
        
        // Find and replace the old review
        List<AirlineReview> reviews = node.reviews;
        for (int i = 0; i < reviews.size(); i++) {
            if (reviews.get(i).equals(oldReview)) {
                reviews.set(i, newReview);
                return true;
            }
        }
        return false;
    }
    
    // ==================== DELETE OPERATION ====================
    
    /**
     * Delete a specific review.
     * Time Complexity: O(log N)
     */
    public boolean deleteReview(String airlineName, AirlineReview review) {
        AVLNode node = search(airlineName);
        if (node == null) {
            return false;
        }
        
        boolean removed = node.reviews.remove(review);
        if (removed) {
            totalReviews--;
            // If no reviews left for this airline, remove the node
            if (node.reviews.isEmpty()) {
                root = deleteNode(root, airlineName);
            }
        }
        return removed;
    }
    
    /**
     * Delete a node from the AVL tree.
     */
    private AVLNode deleteNode(AVLNode node, String airlineName) {
        if (node == null) {
            return null;
        }
        
        int comparison = airlineName.compareTo(node.airlineName);
        
        if (comparison < 0) {
            node.left = deleteNode(node.left, airlineName);
        } else if (comparison > 0) {
            node.right = deleteNode(node.right, airlineName);
        } else {
            // Node to delete found
            if (node.left == null || node.right == null) {
                AVLNode temp = (node.left != null) ? node.left : node.right;
                if (temp == null) {
                    temp = node;
                    node = null;
                } else {
                    node = temp;
                }
            } else {
                // Node with two children: get inorder successor
                AVLNode temp = getMinValueNode(node.right);
                node.airlineName = temp.airlineName;
                node.reviews = temp.reviews;
                node.right = deleteNode(node.right, temp.airlineName);
            }
        }
        
        if (node == null) {
            return null;
        }
        
        // Update height and balance
        node.updateHeight();
        int balance = node.getBalanceFactor();
        
        // Rebalance if needed
        if (balance > 1 && node.left.getBalanceFactor() >= 0) {
            return rightRotate(node);
        }
        if (balance > 1 && node.left.getBalanceFactor() < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        if (balance < -1 && node.right.getBalanceFactor() <= 0) {
            return leftRotate(node);
        }
        if (balance < -1 && node.right.getBalanceFactor() > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        
        return node;
    }
    
    private AVLNode getMinValueNode(AVLNode node) {
        AVLNode current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }
    
    // ==================== RBAR CALCULATION ====================
    
    /**
     * Calculate Recency-Biased Average Rating (RBAR) for a specific airline.
     * Uses the shared RBARCalculator utility for consistent calculation across all data structures.
     * 
     * Time Complexity: O(log N + M) where M is number of reviews for the airline
     */
    public double calculateRBAR(String airlineName) {
        AVLNode node = search(airlineName);
        if (node == null || node.reviews.isEmpty()) {
            return 0.0;
        }
        
        // Use shared RBAR calculation utility
        return RBARCalculator.calculateRBAR(node.reviews);
    }
    
    // ==================== AIRLINE RANKINGS ====================
    
    /**
     * Get airlines ranked by their RBAR (Recency-Biased Average Rating).
     * Returns a list of airline names sorted by RBAR in descending order.
     * Uses shared RankingUtils to avoid code duplication.
     * 
     * Time Complexity: O(N log N) where N is number of airlines
     */
    public List<AirlineRanking> getAirlineRankings() {
        // Build map of airline -> reviews for the utility method
        Map<String, List<AirlineReview>> airlineReviewsMap = new HashMap<>();
        for (String airline : getAllAirlines()) {
            airlineReviewsMap.put(airline, getReviewsByAirline(airline));
        }
        
        // Use shared ranking utility
        return RankingUtils.calculateRankings(airlineReviewsMap);
    }
    
    /**
     * Get top k airlines ranked by RBAR.
     * Uses shared RankingUtils to avoid code duplication.
     * 
     * Time Complexity: O(N log N) where N is number of airlines
     */
    public List<AirlineRanking> getTopKAirlines(int k) {
        List<AirlineRanking> rankings = getAirlineRankings();
        return RankingUtils.getTopK(rankings, k);
    }
    
    // ==================== UTILITY METHODS ====================
    
    /**
     * Get the total number of reviews stored.
     */
    public int size() {
        return totalReviews;
    }
    
    /**
     * Get the height of the AVL tree.
     */
    public int getTreeHeight() {
        return root != null ? root.getHeight() : 0;
    }
    
    /**
     * Check if the AVL tree is balanced.
     */
    public boolean isBalanced() {
        return isBalancedHelper(root);
    }
    
    private boolean isBalancedHelper(AVLNode node) {
        if (node == null) {
            return true;
        }
        return node.isBalanced() && 
               isBalancedHelper(node.left) && 
               isBalancedHelper(node.right);
    }
    
    /**
     * Clear all reviews from the store.
     */
    public void clear() {
        root = null;
        totalReviews = 0;
    }
}

