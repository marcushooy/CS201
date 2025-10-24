package com.reviews.datastructures;

import com.reviews.Models.ReviewRecord;
import java.time.LocalDate;

/**
 * Recency-Biased Tree Node implementation for storing reviews.
 * Unlike AVL nodes which group by airline, RBT nodes store individual reviews
 * sorted by date, with recent reviews kept near the root for fast access.
 */
public class RBTNode {
    public ReviewRecord review;
    public String reviewDate;
    public RBTNode left;
    public RBTNode right;
    public RBTNode parent;
    
    public RBTNode(ReviewRecord review) {
        this.review = review;
        this.reviewDate = review.getDate();
        this.left = null;
        this.right = null;
        this.parent = null;
    }
    
    /**
     * Get the review stored in this node.
     */
    public ReviewRecord getReview() {
        return review;
    }
    
    /**
     * Get the date of the review (for BST ordering).
     */
    public String getDate() {
        return reviewDate;
    }
    
    /**
     * Check if this node is a leaf (no children).
     */
    public boolean isLeaf() {
        return left == null && right == null;
    }
    
    /**
     * Check if this node is the root (no parent).
     */
    public boolean isRoot() {
        return parent == null;
    }
    
    /**
     * Check if this node is a left child of its parent.
     */
    public boolean isLeftChild() {
        return parent != null && parent.left == this;
    }
    
    /**
     * Check if this node is a right child of its parent.
     */
    public boolean isRightChild() {
        return parent != null && parent.right == this;
    }
    
    /**
     * Get the sibling of this node (if it exists).
     */
    public RBTNode getSibling() {
        if (parent == null) return null;
        return isLeftChild() ? parent.right : parent.left;
    }
    
    /**
     * Get the grandparent of this node (if it exists).
     */
    public RBTNode getGrandparent() {
        return (parent != null) ? parent.parent : null;
    }
    
    /**
     * Calculate the depth of this node (distance from root).
     * Used for measuring how "buried" old reviews become.
     */
    public int getDepth() {
        int depth = 0;
        RBTNode current = this;
        while (current.parent != null) {
            depth++;
            current = current.parent;
        }
        return depth;
    }
    
    /**
     * Calculate the height of the subtree rooted at this node.
     * Used for analyzing tree balance.
     */
    public int getHeight() {
        int leftHeight = (left != null) ? left.getHeight() : 0;
        int rightHeight = (right != null) ? right.getHeight() : 0;
        return Math.max(leftHeight, rightHeight) + 1;
    }
    
    /**
     * Count the number of nodes in the subtree rooted at this node.
     */
    public int getSubtreeSize() {
        int leftSize = (left != null) ? left.getSubtreeSize() : 0;
        int rightSize = (right != null) ? right.getSubtreeSize() : 0;
        return leftSize + rightSize + 1;
    }
    
    /**
     * Get a string representation showing the review date and airline.
     */
    @Override
    public String toString() {
        return String.format("RBTNode[%s: %s, depth=%d]", 
                           reviewDate, 
                           review.getAirline(), 
                           getDepth());
    }
    
    /**
     * Get a detailed string for debugging tree structure.
     */
    public String toDetailedString() {
        return String.format("RBTNode[date=%s, airline=%s, rating=%.1f, depth=%d, height=%d, parent=%s]",
                           reviewDate,
                           review.getAirline(),
                           review.getOverallRating(),
                           getDepth(),
                           getHeight(),
                           parent != null ? parent.reviewDate.toString() : "null");
    }
}
