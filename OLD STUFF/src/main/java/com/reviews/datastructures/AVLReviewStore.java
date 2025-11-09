package com.reviews.datastructures;

import com.reviews.Models.ReviewRecord;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * AVL Tree implementation for storing reviews.
 * This provides O(log N) time complexity for most operations and serves as our
 * second experiment for performance comparison against the linear list baseline.
 * 
 * Time Complexities:
 * - Insert: O(log N)
 * - Top-k Recent Retrieval: O(log N + N log N) = O(N log N) due to sorting
 * - Recency-Biased Average Rating: O(log N + N) = O(N)
 * - Search by airline: O(log N)
 * 
 * Space Complexity: O(N)
 */
public class AVLReviewStore {
    private AVLNode root;
    private DateTimeFormatter dateFormatter;
    private int totalReviews;
    
    public AVLReviewStore() {
        this.root = null;
        this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.totalReviews = 0;
    }
    
    /**
     * Add a new review to the store.
     * Time Complexity: O(log N)
     */
    public void addReview(ReviewRecord review) {
        root = insert(root, review);
        totalReviews++;
    }
    
    /**
     * Add multiple reviews at once.
     * Time Complexity: O(m log N) where m is the number of reviews to add
     */
    public void addReviews(List<ReviewRecord> newReviews) {
        for (ReviewRecord review : newReviews) {
            addReview(review);
        }
    }
    
    /**
     * Get the total number of reviews stored.
     * Time Complexity: O(1)
     */
    public int size() {
        return totalReviews;
    }
    
    /**
     * Insert a review into the AVL tree.
     * Time Complexity: O(log N)
     */
    private AVLNode insert(AVLNode node, ReviewRecord review) {
        // Base case: create new node
        if (node == null) {
            return new AVLNode(review.getName(), review);
        }
        
        // Compare airline names
        int comparison = review.getName().compareTo(node.airlineName);
        
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
        if (balance > 1 && review.getName().compareTo(node.left.airlineName) < 0) {
            return rightRotate(node);
        }
        
        // Right Right Case
        if (balance < -1 && review.getName().compareTo(node.right.airlineName) > 0) {
            return leftRotate(node);
        }
        
        // Left Right Case
        if (balance > 1 && review.getName().compareTo(node.left.airlineName) > 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        
        // Right Left Case
        if (balance < -1 && review.getName().compareTo(node.right.airlineName) < 0) {
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
     * Retrieve the k most recent reviews for a specific airline.
     * Time Complexity: O(log N + N log N) = O(N log N) due to sorting
     */
    public List<ReviewRecord> getTopKRecentReviews(String airlineName, int k) {
        AVLNode node = search(airlineName);
        if (node == null) {
            return new ArrayList<>();
        }
        
        return node.getAllReviews().stream()
                .sorted((r1, r2) -> parseDate(r2.getDate()).compareTo(parseDate(r1.getDate())))
                .limit(k)
                .collect(Collectors.toList());
    }
    
    /**
     * Calculate Recency-Biased Average Rating (RB-AR) for a specific airline.
     * This heavily weights recent reviews (last 30 days) and deprioritizes old reviews (3+ years).
     * Time Complexity: O(log N + N) = O(N)
     */
    public double calculateRecencyBiasedAverageRating(String airlineName) {
        AVLNode node = search(airlineName);
        if (node == null) {
            return 0.0;
        }
        
        LocalDate now = LocalDate.now();
        LocalDate thirtyDaysAgo = now.minusDays(30);
        LocalDate threeYearsAgo = now.minusYears(3);
        
        List<ReviewRecord> airlineReviews = node.getAllReviews();
        
        if (airlineReviews.isEmpty()) {
            return 0.0;
        }
        
        double weightedSum = 0.0;
        double totalWeight = 0.0;
        
        for (ReviewRecord review : airlineReviews) {
            LocalDate reviewDate = parseDate(review.getDate());
            double weight = calculateRecencyWeight(reviewDate, thirtyDaysAgo, threeYearsAgo);
            
            weightedSum += review.getOverallRating() * weight;
            totalWeight += weight;
        }
        
        return totalWeight > 0 ? weightedSum / totalWeight : 0.0;
    }
    
    /**
     * Calculate the weight for a review based on its recency.
     * Recent reviews (last 30 days) get high weight, old reviews (3+ years) get low weight.
     */
    private double calculateRecencyWeight(LocalDate reviewDate, LocalDate thirtyDaysAgo, LocalDate threeYearsAgo) {
        if (reviewDate.isAfter(thirtyDaysAgo)) {
            // Recent reviews (last 30 days): weight = 1.0
            return 1.0;
        } else if (reviewDate.isAfter(threeYearsAgo)) {
            // Medium age reviews (30 days to 3 years): linear decay
            long daysSinceThirtyDays = java.time.temporal.ChronoUnit.DAYS.between(thirtyDaysAgo, reviewDate);
            long totalDays = java.time.temporal.ChronoUnit.DAYS.between(threeYearsAgo, thirtyDaysAgo);
            return Math.max(0.1, 1.0 - (double) daysSinceThirtyDays / totalDays);
        } else {
            // Old reviews (3+ years): minimal weight
            return 0.05;
        }
    }
    
    /**
     * Get all reviews for a specific airline.
     * Time Complexity: O(log N)
     */
    public List<ReviewRecord> getReviewsByAirline(String airlineName) {
        AVLNode node = search(airlineName);
        if (node == null) {
            return new ArrayList<>();
        }
        return node.getAllReviews();
    }
    
    /**
     * Get all unique airline names in the store.
     * Time Complexity: O(N) - must traverse entire tree
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
    
    /**
     * Get reviews within a specific date range.
     * Time Complexity: O(N) - must traverse entire tree
     */
    public List<ReviewRecord> getReviewsInDateRange(LocalDate startDate, LocalDate endDate) {
        List<ReviewRecord> result = new ArrayList<>();
        getReviewsInDateRangeHelper(root, startDate, endDate, result);
        return result;
    }
    
    private void getReviewsInDateRangeHelper(AVLNode node, LocalDate startDate, LocalDate endDate, List<ReviewRecord> result) {
        if (node != null) {
            for (ReviewRecord review : node.getAllReviews()) {
                LocalDate reviewDate = parseDate(review.getDate());
                if (!reviewDate.isBefore(startDate) && !reviewDate.isAfter(endDate)) {
                    result.add(review);
                }
            }
            getReviewsInDateRangeHelper(node.left, startDate, endDate, result);
            getReviewsInDateRangeHelper(node.right, startDate, endDate, result);
        }
    }
    
    /**
     * Parse date string to LocalDate.
     * Handles common date formats used in review data.
     */
    private LocalDate parseDate(String dateStr) {
        try {
            // Try the standard format first
            return LocalDate.parse(dateStr, dateFormatter);
        } catch (Exception e) {
            try {
                // Try alternative format
                DateTimeFormatter altFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                return LocalDate.parse(dateStr, altFormatter);
            } catch (Exception e2) {
                try {
                    // Try another common format
                    DateTimeFormatter altFormatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    return LocalDate.parse(dateStr, altFormatter2);
                } catch (Exception e3) {
                    // If all parsing fails, return a very old date to minimize its weight
                    return LocalDate.of(1900, 1, 1);
                }
            }
        }
    }
    
    /**
     * Clear all reviews from the store.
     * Time Complexity: O(1)
     */
    public void clear() {
        root = null;
        totalReviews = 0;
    }
    
    /**
     * Get all reviews sorted by date (most recent first).
     * Time Complexity: O(N log N) - must traverse entire tree and sort
     */
    public List<ReviewRecord> getAllReviewsSortedByDate() {
        List<ReviewRecord> allReviews = new ArrayList<>();
        getAllReviewsHelper(root, allReviews);
        return allReviews.stream()
                .sorted((r1, r2) -> parseDate(r2.getDate()).compareTo(parseDate(r1.getDate())))
                .collect(Collectors.toList());
    }
    
    private void getAllReviewsHelper(AVLNode node, List<ReviewRecord> allReviews) {
        if (node != null) {
            allReviews.addAll(node.getAllReviews());
            getAllReviewsHelper(node.left, allReviews);
            getAllReviewsHelper(node.right, allReviews);
        }
    }
    
    /**
     * Get statistics about the review store.
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalReviews", totalReviews);
        stats.put("uniqueAirlines", getAllAirlines().size());
        
        if (totalReviews > 0) {
            List<ReviewRecord> allReviews = getAllReviewsSortedByDate();
            if (!allReviews.isEmpty()) {
                LocalDate oldestDate = parseDate(allReviews.get(allReviews.size() - 1).getDate());
                LocalDate newestDate = parseDate(allReviews.get(0).getDate());
                
                stats.put("oldestReview", oldestDate);
                stats.put("newestReview", newestDate);
            }
        }
        
        return stats;
    }
    
    /**
     * Get the height of the AVL tree.
     * Time Complexity: O(1)
     */
    public int getTreeHeight() {
        return root != null ? root.getHeight() : 0;
    }
    
    /**
     * Check if the AVL tree is balanced.
     * Time Complexity: O(N) - must check all nodes
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
     * Get detailed tree statistics for analysis.
     */
    public Map<String, Object> getTreeStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalReviews", totalReviews);
        stats.put("uniqueAirlines", getAllAirlines().size());
        stats.put("treeHeight", getTreeHeight());
        stats.put("isBalanced", isBalanced());
        
        // Calculate average reviews per airline
        Set<String> airlines = getAllAirlines();
        if (!airlines.isEmpty()) {
            double avgReviewsPerAirline = (double) totalReviews / airlines.size();
            stats.put("avgReviewsPerAirline", avgReviewsPerAirline);
        }
        
        return stats;
    }
}
