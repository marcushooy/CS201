package com.reviews.datastructures;

import com.reviews.Models.ReviewRecord;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Recency-Biased Tree (RBT) implementation for storing reviews.
 * Unlike AVL trees that maintain perfect balance, RBT intentionally favors recent reviews
 * by splaying newly inserted nodes toward the root, making recent data fast to access
 * at the cost of slower access to older data.
 * 
 * Time Complexities:
 * - Insert: O(log N) + O(k) splay operations, where k is the splay depth
 * - Top-k Recent Retrieval: O(k) - recent reviews are clustered near root!
 * - Old Review Access: O(N) worst case - old reviews sink to the bottom
 * - Search by date: O(depth of node) - varies dramatically by recency
 * 
 * Space Complexity: O(N)
 * 
 * THE EXPERIMENT: We trade general-purpose efficiency for specialized performance
 * on the high-value problem of analyzing recent trends.
 */
public class RBTReviewStore {
    private RBTNode root;
    private DateTimeFormatter dateFormatter;
    private int totalReviews;
    private int splayDepth; // How many levels to splay new nodes up (configurable)
    
    /**
     * Create a new RBT with default splay depth (splay to root).
     */
    public RBTReviewStore() {
        this(Integer.MAX_VALUE); // Default: splay all the way to root
    }
    
    /**
     * Create a new RBT with custom splay depth.
     * @param splayDepth Number of levels to splay new nodes up (0 = no splay, MAX_VALUE = to root)
     */
    public RBTReviewStore(int splayDepth) {
        this.root = null;
        this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.totalReviews = 0;
        this.splayDepth = splayDepth;
    }
    
    /**
     * Add a new review to the store.
     * Time Complexity: O(log N) for insertion + O(splayDepth) for splaying
     */
    public void addReview(ReviewRecord review) {
        RBTNode newNode = insertAndReturnNode(root, review, null);
        totalReviews++;
        
        // CRITICAL FIX: Actually perform the splay operation!
        if (newNode != null && splayDepth > 0) {
            splay(newNode);
        }
    }
    
    /**
     * Add multiple reviews at once.
     */
    public void addReviews(List<ReviewRecord> newReviews) {
        for (ReviewRecord review : newReviews) {
            addReview(review);
        }
    }
    
    /**
     * Get the total number of reviews stored.
     */
    public int size() {
        return totalReviews;
    }
    
    /**
     * Insert a review into the RBT (BST insertion by date).
     * Returns the newly inserted node for splaying.
     */
    private RBTNode insertAndReturnNode(RBTNode node, ReviewRecord review, RBTNode parent) {
        // Base case: create new node
        if (node == null) {
            RBTNode newNode = new RBTNode(review);
            newNode.parent = parent;
            
            // Update parent's child reference
            if (parent != null) {
                LocalDate reviewDate = parseDate(review.getDate());
                LocalDate parentDate = parseDate(parent.getReview().getDate());
                if (reviewDate.compareTo(parentDate) <= 0) {
                    parent.left = newNode;
                } else {
                    parent.right = newNode;
                }
            } else {
                root = newNode;
            }
            
            return newNode;
        }
        
        // FIX: Compare LocalDate objects, not Strings
        LocalDate reviewDate = parseDate(review.getDate());
        LocalDate nodeDate = parseDate(node.getReview().getDate());
        int comparison = reviewDate.compareTo(nodeDate);
        
        if (comparison < 0) {
            // Insert into left subtree (older)
            return insertAndReturnNode(node.left, review, node);
        } else {
            // Insert into right subtree (newer or equal)
            return insertAndReturnNode(node.right, review, node);
        }
    }
    
    /**
     * Splay a node toward the root by performing rotations.
     * This is THE KEY OPERATION that makes recent reviews fast to access.
     */
    private void splay(RBTNode node) {
        if (node == null) return;
        
        int rotations = 0;
        while (node.parent != null && rotations < splayDepth) {
            if (node.parent.parent == null) {
                // Zig: node's parent is root
                if (node.isLeftChild()) {
                    rotateRight(node.parent);
                } else {
                    rotateLeft(node.parent);
                }
                rotations++;
            } else if (node.isLeftChild() && node.parent.isLeftChild()) {
                // Zig-zig: both are left children
                rotateRight(node.parent.parent);
                rotateRight(node.parent);
                rotations += 2;
            } else if (node.isRightChild() && node.parent.isRightChild()) {
                // Zig-zig: both are right children
                rotateLeft(node.parent.parent);
                rotateLeft(node.parent);
                rotations += 2;
            } else if (node.isLeftChild() && node.parent.isRightChild()) {
                // Zig-zag: left then right
                rotateRight(node.parent);
                rotateLeft(node.parent);
                rotations += 2;
            } else {
                // Zig-zag: right then left
                rotateLeft(node.parent);
                rotateRight(node.parent);
                rotations += 2;
            }
        }
        
        // Update root if node reached the top
        if (node.parent == null) {
            root = node;
        }
    }
    
    /**
     * Right rotation.
     */
    private void rotateRight(RBTNode y) {
        RBTNode x = y.left;
        if (x == null) return;
        
        RBTNode parent = y.parent;
        
        // Perform rotation
        y.left = x.right;
        if (x.right != null) {
            x.right.parent = y;
        }
        
        x.right = y;
        y.parent = x;
        x.parent = parent;
        
        // Update parent's reference
        if (parent != null) {
            if (y == parent.left) {
                parent.left = x;
            } else {
                parent.right = x;
            }
        } else {
            root = x;
        }
    }
    
    /**
     * Left rotation.
     */
    private void rotateLeft(RBTNode x) {
        RBTNode y = x.right;
        if (y == null) return;
        
        RBTNode parent = x.parent;
        
        // Perform rotation
        x.right = y.left;
        if (y.left != null) {
            y.left.parent = x;
        }
        
        y.left = x;
        x.parent = y;
        y.parent = parent;
        
        // Update parent's reference
        if (parent != null) {
            if (x == parent.left) {
                parent.left = y;
            } else {
                parent.right = y;
            }
        } else {
            root = y;
        }
    }
    
    /**
     * THE KILLER FEATURE: Get k most recent reviews in O(k) time!
     * Since recent reviews are clustered near the root, we can just traverse
     * the rightmost path (newest dates) without searching the entire tree.
     * 
     * Time Complexity: O(k) in the best case when recent reviews are near root,
     *                  O(k log N) in worst case if tree is balanced
     */
    public List<ReviewRecord> getTopKRecentReviews(String airline, int k) {
        List<ReviewRecord> result = new ArrayList<>();
        getTopKRecentHelper(root, airline, k, result);
        return result;
    }
    
    /**
     * In-order traversal (reverse) to get most recent reviews.
     */
    private void getTopKRecentHelper(RBTNode node, String airline, int k, List<ReviewRecord> result) {
        if (node == null || result.size() >= k) {
            return;
        }
        
        // Traverse right first (newer dates)
        getTopKRecentHelper(node.right, airline, k, result);
        
        // Add current node if we haven't hit k yet and matches airline
        if (result.size() < k && node.getReview().getAirline().equals(airline)) {
            result.add(node.getReview());
        }
        
        // Traverse left (older dates)
        getTopKRecentHelper(node.left, airline, k, result);
    }
    
    /**
     * THE EXPERIMENT MEASUREMENT: Find a review by date and return its depth.
     * This is how we prove that recent reviews are fast (low depth) and
     * old reviews are slow (high depth).
     * 
     * @return Depth of the found node, or -1 if not found
     */
    public int findDepthByDate(LocalDate targetDate) {
        RBTNode node = findByDate(root, targetDate);
        return node != null ? node.getDepth() : -1;
    }
    
    /**
     * Search for a review by date.
     */
    private RBTNode findByDate(RBTNode node, LocalDate targetDate) {
        if (node == null) {
            return null;
        }

        LocalDate nodeDate = parseDate(node.getReview().getDate());
        int comparison = targetDate.compareTo(nodeDate);
        
        if (comparison == 0) {
            return node;
        } else if (comparison < 0) {
            return findByDate(node.left, targetDate);
        } else {
            return findByDate(node.right, targetDate);
        }
    }
    
    /**
     * Calculate average rating for recent reviews (last 30 days).
     * This should be VERY fast since recent reviews are near the root.
     */
    public double getRecentAverageRating(int days) {
        LocalDate cutoff = LocalDate.now().minusDays(days);
        List<ReviewRecord> recentReviews = getReviewsAfterDate(cutoff);
        
        if (recentReviews.isEmpty()) {
            return 0.0;
        }
        
        double sum = 0.0;
        for (ReviewRecord review : recentReviews) {
            sum += review.getOverallRating();
        }
        
        return sum / recentReviews.size();
    }
    
    /**
     * Get all reviews after a specific date.
     */
    private List<ReviewRecord> getReviewsAfterDate(LocalDate cutoff) {
        List<ReviewRecord> result = new ArrayList<>();
        getReviewsAfterDateHelper(root, cutoff, result);
        return result;
    }
    
    private void getReviewsAfterDateHelper(RBTNode node, LocalDate cutoff, List<ReviewRecord> result) {
        if (node == null) {
            return;
        }
        
        // FIX: Parse the date string to LocalDate before comparison
        LocalDate nodeDate = parseDate(node.getReview().getDate());
        
        // If current node is after cutoff, add it and check both subtrees
        if (nodeDate.isAfter(cutoff)) {
            result.add(node.getReview());
            getReviewsAfterDateHelper(node.left, cutoff, result);
            getReviewsAfterDateHelper(node.right, cutoff, result);
        } else {
            // Current node is before cutoff, only check right subtree (newer)
            getReviewsAfterDateHelper(node.right, cutoff, result);
        }
    }
    
    /**
     * Get all reviews sorted by date (most recent first).
     */
    public List<ReviewRecord> getAllReviewsSortedByDate() {
        List<ReviewRecord> result = new ArrayList<>();
        getAllReviewsHelper(root, result);
        return result;
    }
    
    private void getAllReviewsHelper(RBTNode node, List<ReviewRecord> result) {
        if (node == null) {
            return;
        }
        
        // Reverse in-order traversal (right, root, left) for newest first
        getAllReviewsHelper(node.right, result);
        result.add(node.getReview());
        getAllReviewsHelper(node.left, result);
    }
    
    /**
     * Parse date string to LocalDate.
     */
    private LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, dateFormatter);
        } catch (Exception e) {
            try {
                DateTimeFormatter altFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                return LocalDate.parse(dateStr, altFormatter);
            } catch (Exception e2) {
                try {
                    DateTimeFormatter altFormatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    return LocalDate.parse(dateStr, altFormatter2);
                } catch (Exception e3) {
                    return LocalDate.of(1900, 1, 1);
                }
            }
        }
    }
    
    /**
     * Clear all reviews.
     */
    public void clear() {
        root = null;
        totalReviews = 0;
    }
    
    /**
     * THE ANALYSIS GOLDMINE: Get statistics about tree structure.
     * This is what proves our experiment worked!
     */
    public Map<String, Object> getTreeStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalReviews", totalReviews);
        stats.put("treeHeight", getTreeHeight());
        stats.put("splayDepth", splayDepth);
        stats.put("uniqueAirlines", getAllAirlines().length);
        stats.put("isBalanced", false); // RBT is intentionally unbalanced
        
        // Date range
        List<ReviewRecord> allReviews = getAllReviewsSortedByDate();
        if (!allReviews.isEmpty()) {
            stats.put("newestReview", allReviews.get(0).getDate());
            stats.put("oldestReview", allReviews.get(allReviews.size() - 1).getDate());
        }
        
        // Average reviews per airline
        String[] airlines = getAllAirlines();
        if (airlines.length > 0) {
            stats.put("avgReviewsPerAirline", (double) totalReviews / airlines.length);
        } else {
            stats.put("avgReviewsPerAirline", 0.0);
        }
        
        if (root != null) {
            // Measure depth distribution
            Map<String, Integer> depthStats = analyzeDepthDistribution();
            stats.put("avgDepth", depthStats.get("avgDepth"));
            stats.put("maxDepth", depthStats.get("maxDepth"));
            stats.put("recentAvgDepth", depthStats.get("recentAvgDepth"));
            stats.put("oldAvgDepth", depthStats.get("oldAvgDepth"));
        }
        
        return stats;
    }
    
    /**
     * Analyze how depths vary by review age.
     * This proves that recent reviews are shallow (fast) and old reviews are deep (slow).
     */
    private Map<String, Integer> analyzeDepthDistribution() {
        List<Integer> allDepths = new ArrayList<>();
        List<Integer> recentDepths = new ArrayList<>();
        List<Integer> oldDepths = new ArrayList<>();
        
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        
        analyzeDepthHelper(root, allDepths, recentDepths, oldDepths, thirtyDaysAgo);
        
        Map<String, Integer> result = new HashMap<>();
        result.put("avgDepth", average(allDepths));
        result.put("maxDepth", allDepths.stream().max(Integer::compareTo).orElse(0));
        result.put("recentAvgDepth", average(recentDepths));
        result.put("oldAvgDepth", average(oldDepths));
        
        return result;
    }
    
    private void analyzeDepthHelper(RBTNode node, List<Integer> allDepths, 
                                   List<Integer> recentDepths, List<Integer> oldDepths,
                                   LocalDate cutoff) {
        if (node == null) return;
        
        int depth = node.getDepth();
        allDepths.add(depth);
        
        LocalDate nodeDate = parseDate(node.getReview().getDate());
        if (nodeDate.isAfter(cutoff)) {
            recentDepths.add(depth);
        } else {
            oldDepths.add(depth);
        }
        
        analyzeDepthHelper(node.left, allDepths, recentDepths, oldDepths, cutoff);
        analyzeDepthHelper(node.right, allDepths, recentDepths, oldDepths, cutoff);
    }
    
    private int average(List<Integer> values) {
        if (values.isEmpty()) return 0;
        return (int) values.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }
    
    /**
     * Get the height of the tree.
     */
    public int getTreeHeight() {
        return root != null ? root.getHeight() : 0;
    }
    
    /**
     * Get the root node (for testing/visualization).
     */
    public RBTNode getRoot() {
        return root;
    }

    /**
     * Get all unique airlines in the store.
     */
    public String[] getAllAirlines() {
        Set<String> airlines = new HashSet<>();
        collectAirlines(root, airlines);
        return airlines.toArray(new String[0]);
    }
    
    private void collectAirlines(RBTNode node, Set<String> airlines) {
        if (node == null) return;
        airlines.add(node.getReview().getAirline());
        collectAirlines(node.left, airlines);
        collectAirlines(node.right, airlines);
    }

    /**
     * Get all reviews for a specific airline.
     */
    public List<ReviewRecord> getReviewsByAirline(String airline) {
        List<ReviewRecord> result = new ArrayList<>();
        collectReviewsByAirline(root, airline, result);
        return result;
    }
    
    private void collectReviewsByAirline(RBTNode node, String airline, List<ReviewRecord> result) {
        if (node == null) return;
        
        if (node.getReview().getAirline().equals(airline)) {
            result.add(node.getReview());
        }
        
        collectReviewsByAirline(node.left, airline, result);
        collectReviewsByAirline(node.right, airline, result);
    }

    /**
     * Calculate recency-biased average rating for an airline.
     * Recent reviews (≤30 days) get full weight (1.0)
     * Reviews decay exponentially: weight = e^(-age_in_days / 365)
     * Very old reviews (>3 years) get minimal weight
     */
    public double calculateRecencyBiasedAverageRating(String airline) {
        List<ReviewRecord> reviews = getReviewsByAirline(airline);
        
        if (reviews.isEmpty()) {
            return 0.0;
        }
        
        LocalDate now = LocalDate.now();
        double weightedSum = 0.0;
        double totalWeight = 0.0;
        
        for (ReviewRecord review : reviews) {
            LocalDate reviewDate = parseDate(review.getDate());
            long daysOld = ChronoUnit.DAYS.between(reviewDate, now);
            
            // Calculate weight using exponential decay
            double weight;
            if (daysOld <= 30) {
                weight = 1.0; // Full weight for recent reviews
            } else {
                // Exponential decay: e^(-days/365)
                weight = Math.exp(-daysOld / 365.0);
            }
            
            weightedSum += review.getOverallRating() * weight;
            totalWeight += weight;
        }
        
        return totalWeight > 0 ? weightedSum / totalWeight : 0.0;
    }
}