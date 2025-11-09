package datastructures.AVL_Tree;

import models.AirlineReview;
import java.util.ArrayList;
import java.util.List;

/**
 * AVLNode represents a node in the AVL tree.
 * Each node stores reviews for a specific airline and maintains
 * balance information for the AVL tree structure.
 */
public class AVLNode {
    public String airlineName;
    public List<AirlineReview> reviews;  // All reviews for this airline
    public AVLNode left;
    public AVLNode right;
    public int height;
    
    /**
     * Constructor to create a new AVLNode with an initial review.
     */
    public AVLNode(String airlineName, AirlineReview review) {
        this.airlineName = airlineName;
        this.reviews = new ArrayList<>();
        this.reviews.add(review);
        this.left = null;
        this.right = null;
        this.height = 1;  // New node is a leaf, height = 1
    }
    
    /**
     * Add a review to this node's review list.
     */
    public void addReview(AirlineReview review) {
        reviews.add(review);
    }
    
    /**
     * Get the height of this node.
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Update the height of this node based on its children.
     */
    public void updateHeight() {
        int leftHeight = (left != null) ? left.getHeight() : 0;
        int rightHeight = (right != null) ? right.getHeight() : 0;
        this.height = Math.max(leftHeight, rightHeight) + 1;
    }
    
    /**
     * Get the balance factor of this node.
     * Balance factor = leftHeight - rightHeight
     * Positive = left-heavy, Negative = right-heavy
     */
    public int getBalanceFactor() {
        int leftHeight = (left != null) ? left.getHeight() : 0;
        int rightHeight = (right != null) ? right.getHeight() : 0;
        return leftHeight - rightHeight;
    }
    
    /**
     * Check if this node is balanced (balance factor between -1 and 1).
     */
    public boolean isBalanced() {
        int balance = getBalanceFactor();
        return balance >= -1 && balance <= 1;
    }
    
    /**
     * Get the number of reviews stored in this node.
     */
    public int getReviewCount() {
        return reviews.size();
    }
    
    /**
     * Get all reviews for this airline.
     */
    public List<AirlineReview> getAllReviews() {
        return new ArrayList<>(reviews);
    }
    
    @Override
    public String toString() {
        return String.format("AVLNode[%s: %d reviews, height=%d, balance=%d]", 
                           airlineName, reviews.size(), height, getBalanceFactor());
    }
}

