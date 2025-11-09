package datastructures.Recency_Biased_Tree;

import models.AirlineReview;
import models.AirlineRanking;
import utils.RBARCalculator;
import utils.RankingUtils;
import java.util.*;

/**
 * RBTReviewStore = Recency-Biased Tree based implementation
 * for airline reviews.
 * 
 * This data structure provides:
 * - O(log N) time complexity for insert, search operations
 * - O(1) to O(log N) for recent reviews (splayed to root)
 * - Efficient RBAR (Recency-Biased Average Rating) calculations
 * 
 * Core Operations:
 * - Create: addReview() - Insert a new review
 * - Read: getReviewsByAirline() - Get all reviews for an airline
 * - Update: updateReview() - Update an existing review
 * - Delete: deleteReview() - Remove a review
 * - RBAR: calculateRBAR() - Calculate recency-biased average rating
 * - Rankings: getAirlineRankings() - Get airlines ranked by RBAR
 */
public class RBTReviewStore {

    // One recency-biased tree per airline, ordered by review date (ascending)
    private final Map<String, RecencyBiasedTree<AirlineReview>> airlineTrees = new HashMap<>();

    // ---------- CRUD-style methods ----------

    /**
     * Add a new review to the store.
     * Time Complexity: O(log N) with splay operations
     */
    public void addReview(AirlineReview review) {
        if (review == null || review.getAirlineName() == null) return;

        String airline = review.getAirlineName();

        // Comparator: order by date string "YYYY-MM-DD" (lexical == chronological)
        RecencyBiasedTree<AirlineReview> tree = airlineTrees.computeIfAbsent(
                airline,
                k -> new RecencyBiasedTree<>(
                        (a, b) -> a.getDate().compareTo(b.getDate())
                )
        );

        tree.insert(review);
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
     * Get all reviews for a specific airline (oldest → newest).
     * Time Complexity: O(N) where N is number of reviews for the airline
     */
    public List<AirlineReview> getReviewsByAirline(String airline) {
        if (airline == null) return Collections.emptyList();
        RecencyBiasedTree<AirlineReview> tree = airlineTrees.get(airline);
        if (tree == null) return Collections.emptyList();

        return tree.inOrderTraversal();  // oldest → newest
    }
    
    /**
     * Get all unique airline names in the store.
     * Time Complexity: O(1) - just returns map keys
     */
    public Set<String> getAllAirlines() {
        return new HashSet<>(airlineTrees.keySet());
    }

    /** Get all reviews across all airlines (oldest → newest within each airline). */
    public List<AirlineReview> getAllReviews() {
        List<AirlineReview> all = new ArrayList<>();
        for (RecencyBiasedTree<AirlineReview> tree : airlineTrees.values()) {
            all.addAll(tree.inOrderTraversal());
        }
        return all;
    }

    // ==================== UPDATE OPERATION ====================
    
    /**
     * Update a review (replace old review with new one).
     * Time Complexity: O(N) where N is number of reviews for the airline
     */
    public boolean updateReview(String airlineName, AirlineReview oldReview, AirlineReview newReview) {
        if (airlineName == null || oldReview == null || newReview == null) {
            return false;
        }
        
        RecencyBiasedTree<AirlineReview> tree = airlineTrees.get(airlineName);
        if (tree == null) {
            return false;
        }
        
        // Get all reviews, find and replace, then rebuild tree
        List<AirlineReview> reviews = tree.inOrderTraversal();
        boolean found = false;
        for (int i = 0; i < reviews.size(); i++) {
            if (reviews.get(i).equals(oldReview)) {
                reviews.set(i, newReview);
                found = true;
                break;
            }
        }
        
        if (found) {
            // Rebuild tree with updated review
            RecencyBiasedTree<AirlineReview> newTree = new RecencyBiasedTree<>(
                (a, b) -> a.getDate().compareTo(b.getDate())
            );
            for (AirlineReview review : reviews) {
                newTree.insert(review);
            }
            airlineTrees.put(airlineName, newTree);
        }
        
        return found;
    }
    
    // ==================== DELETE OPERATION ====================
    
    /**
     * Delete a specific review.
     * Time Complexity: O(N) where N is number of reviews for the airline
     */
    public boolean deleteReview(String airlineName, AirlineReview review) {
        if (airlineName == null || review == null) {
            return false;
        }
        
        RecencyBiasedTree<AirlineReview> tree = airlineTrees.get(airlineName);
        if (tree == null) {
            return false;
        }
        
        // Get all reviews, remove the one to delete, then rebuild tree
        List<AirlineReview> reviews = tree.inOrderTraversal();
        boolean removed = reviews.remove(review);
        
        if (removed) {
            if (reviews.isEmpty()) {
                // Remove airline if no reviews left
                airlineTrees.remove(airlineName);
            } else {
                // Rebuild tree without the deleted review
                RecencyBiasedTree<AirlineReview> newTree = new RecencyBiasedTree<>(
                    (a, b) -> a.getDate().compareTo(b.getDate())
                );
                for (AirlineReview r : reviews) {
                    newTree.insert(r);
                }
                airlineTrees.put(airlineName, newTree);
            }
        }
        
        return removed;
    }
    
    // ==================== RBAR CALCULATION ====================
    
    /**
     * Calculate Recency-Biased Average Rating (RBAR) for a specific airline.
     * Uses the shared RBARCalculator utility for consistent calculation across all data structures.
     * 
     * Time Complexity: O(N) where N is number of reviews for the airline
     */
    public double calculateRBAR(String airlineName) {
        List<AirlineReview> reviews = getReviewsByAirline(airlineName);
        if (reviews.isEmpty()) {
            return 0.0;
        }
        
        // Use shared RBAR calculation utility
        return RBARCalculator.calculateRBAR(reviews);
    }
    
    /**
     * Legacy method - kept for compatibility, but delegates to calculateRBAR().
     * The 'now' parameter is ignored as RBARCalculator uses LocalDate.now() internally.
     * @deprecated Use calculateRBAR(String airlineName) instead
     */
    @Deprecated
    public double calculateRecencyBiasedAverageRating(String airline, @SuppressWarnings("unused") java.time.LocalDate now) {
        return calculateRBAR(airline);
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

    /**
     * Get the top K most recent reviews for an airline.
     * Uses the recency-biased tree's reverse in-order traversal.
     * Time Complexity: O(k) - recent reviews are near root
     */
    public List<AirlineReview> getTopKRecentReviews(String airline, int k) {
        if (airline == null || k <= 0) return Collections.emptyList();
        RecencyBiasedTree<AirlineReview> tree = airlineTrees.get(airline);
        if (tree == null) return Collections.emptyList();

        // Newest → older
        return tree.reverseInOrderTakeK(k);
    }
    
    // ==================== UTILITY METHODS ====================
    
    /**
     * Get the total number of reviews stored.
     * Time Complexity: O(N) where N is total number of reviews
     */
    public int size() {
        int total = 0;
        for (RecencyBiasedTree<AirlineReview> tree : airlineTrees.values()) {
            total += tree.inOrderTraversal().size();
        }
        return total;
    }
    
    /**
     * Clear all reviews from the store.
     */
    public void clear() {
        airlineTrees.clear();
    }
}
