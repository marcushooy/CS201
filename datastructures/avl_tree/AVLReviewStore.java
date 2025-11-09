import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
    private DateTimeFormatter dateFormatter;
    private int totalReviews;
    
    public AVLReviewStore() {
        this.root = null;
        this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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
     * 
     * Weighting scheme:
     * - Recent reviews (last 30 days): weight = 1.0 (full weight)
     * - Medium age reviews (30 days to 3 years): linear decay from 1.0 to 0.1
     * - Old reviews (3+ years): weight = 0.05 (minimal weight)
     * 
     * Time Complexity: O(log N + M) where M is number of reviews for the airline
     */
    public double calculateRBAR(String airlineName) {
        AVLNode node = search(airlineName);
        if (node == null || node.reviews.isEmpty()) {
            return 0.0;
        }
        
        LocalDate now = LocalDate.now();
        LocalDate thirtyDaysAgo = now.minusDays(30);
        LocalDate threeYearsAgo = now.minusYears(3);
        
        double weightedSum = 0.0;
        double totalWeight = 0.0;
        
        for (AirlineReview review : node.reviews) {
            LocalDate reviewDate = parseDate(review.getDate());
            double weight = calculateRecencyWeight(reviewDate, thirtyDaysAgo, threeYearsAgo);
            
            weightedSum += review.getOverallRating() * weight;
            totalWeight += weight;
        }
        
        return totalWeight > 0 ? weightedSum / totalWeight : 0.0;
    }
    
    /**
     * Calculate the weight for a review based on its recency.
     */
    private double calculateRecencyWeight(LocalDate reviewDate, LocalDate thirtyDaysAgo, LocalDate threeYearsAgo) {
        if (reviewDate.isAfter(thirtyDaysAgo)) {
            // Recent reviews (last 30 days): weight = 1.0
            return 1.0;
        } else if (reviewDate.isAfter(threeYearsAgo)) {
            // Medium age reviews (30 days to 3 years): linear decay
            long daysSinceThirtyDays = ChronoUnit.DAYS.between(thirtyDaysAgo, reviewDate);
            long totalDays = ChronoUnit.DAYS.between(threeYearsAgo, thirtyDaysAgo);
            return Math.max(0.1, 1.0 - (double) daysSinceThirtyDays / totalDays);
        } else {
            // Old reviews (3+ years): minimal weight
            return 0.05;
        }
    }
    
    /**
     * Parse date string to LocalDate.
     */
    private LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, dateFormatter);
        } catch (Exception e) {
            // If parsing fails, return a very old date to minimize its weight
            return LocalDate.of(1900, 1, 1);
        }
    }
    
    // ==================== AIRLINE RANKINGS ====================
    
    /**
     * Get airlines ranked by their RBAR (Recency-Biased Average Rating).
     * Returns a list of airline names sorted by RBAR in descending order.
     * 
     * Time Complexity: O(N log N) where N is number of airlines
     */
    public List<AirlineRanking> getAirlineRankings() {
        List<AirlineRanking> rankings = new ArrayList<>();
        
        for (String airline : getAllAirlines()) {
            double rbar = calculateRBAR(airline);
            int reviewCount = getReviewsByAirline(airline).size();
            rankings.add(new AirlineRanking(airline, rbar, reviewCount));
        }
        
        // Sort by RBAR in descending order
        rankings.sort((a, b) -> Double.compare(b.rbar, a.rbar));
        
        return rankings;
    }
    
    /**
     * Get top k airlines ranked by RBAR.
     * Time Complexity: O(N log N) where N is number of airlines
     */
    public List<AirlineRanking> getTopKAirlines(int k) {
        List<AirlineRanking> rankings = getAirlineRankings();
        return rankings.subList(0, Math.min(k, rankings.size()));
    }
    
    /**
     * Inner class to represent airline ranking.
     */
    public static class AirlineRanking {
        public String airlineName;
        public double rbar;
        public int reviewCount;
        
        public AirlineRanking(String airlineName, double rbar, int reviewCount) {
            this.airlineName = airlineName;
            this.rbar = rbar;
            this.reviewCount = reviewCount;
        }
        
        @Override
        public String toString() {
            return String.format("%s: RBAR=%.3f (%d reviews)", airlineName, rbar, reviewCount);
        }
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

