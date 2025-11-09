package datastructures.Linear_List;

import models.AirlineReview;
import models.AirlineRanking;
import utils.RBARCalculator;
import utils.RankingUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * LinearListReviewStore - ArrayList-based data structure for storing airline reviews.
 * 
 * This implementation uses an ArrayList as the underlying storage mechanism,
 * providing O(1) insert and O(N) search operations.
 * 
 * This data structure provides:
 * - O(1) amortized time complexity for insert operations
 * - O(N) time complexity for search, update, delete operations
 * - Efficient RBAR (Recency-Biased Average Rating) calculations using shared utilities
 * 
 * Core Operations:
 * - Create: addReview() - Insert a new review
 * - Read: getReviewsByAirline() - Get all reviews for an airline
 * - Update: updateReview() - Update an existing review
 * - Delete: deleteReview() - Remove a review
 * - RBAR: calculateRBAR() - Calculate recency-biased average rating
 * - Rankings: getAirlineRankings() - Get airlines ranked by RBAR
 */
public class LinearListReviewStore {
    private ArrayList<AirlineReview> reviews;
    
    /**
     * Constructor - Initialize empty ArrayList
     */
    public LinearListReviewStore() {
        this.reviews = new ArrayList<>();
    }
    
    // ==================== CREATE OPERATION ====================
    
    /**
     * Add a new review to the store.
     * Time Complexity: O(1) amortized
     * 
     * @param review The AirlineReview to add
     */
    public void addReview(AirlineReview review) {
        if (review != null) {
            reviews.add(review);
        }
    }
    
    /**
     * Add multiple reviews at once.
     * Time Complexity: O(m) where m is the number of reviews
     * 
     * @param reviews List of AirlineReview objects to add
     */
    public void addReviews(List<AirlineReview> reviews) {
        if (reviews != null) {
            for (AirlineReview review : reviews) {
                addReview(review);
            }
        }
    }
    
    // ==================== READ OPERATION ====================
    
    /**
     * Get all reviews for a specific airline.
     * Time Complexity: O(N) where N is total number of reviews
     * 
     * @param airlineName The name of the airline to search for
     * @return List of all reviews for that airline
     */
    public List<AirlineReview> getReviewsByAirline(String airlineName) {
        if (airlineName == null || airlineName.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        return reviews.stream()
                .filter(review -> review.getAirlineName().equalsIgnoreCase(airlineName))
                .collect(Collectors.toList());
    }
    
    /**
     * Get all unique airline names in the store.
     * Time Complexity: O(N) where N is total number of reviews
     * 
     * @return Set of unique airline names
     */
    public Set<String> getAllAirlines() {
        Set<String> airlines = new HashSet<>();
        for (AirlineReview review : reviews) {
            airlines.add(review.getAirlineName());
        }
        return airlines;
    }
    
    /**
     * Get all reviews in the store (for iteration/analysis).
     * Time Complexity: O(N) - returns a copy of the list
     * 
     * @return List of all reviews (copy to prevent external modification)
     */
    public List<AirlineReview> getAllReviews() {
        return new ArrayList<>(reviews);
    }
    
    // ==================== UPDATE OPERATION ====================
    
    /**
     * Update a review (replace old review with new one).
     * Time Complexity: O(N) where N is total number of reviews
     * 
     * @param airlineName The airline name (for consistency with AVL/RBT interface)
     * @param oldReview The review to be replaced
     * @param newReview The new review data
     * @return true if update was successful, false otherwise
     */
    public boolean updateReview(String airlineName, AirlineReview oldReview, AirlineReview newReview) {
        if (airlineName == null || oldReview == null || newReview == null) {
            return false;
        }
        
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
     * Time Complexity: O(N) where N is total number of reviews
     * 
     * @param airlineName The airline name (for consistency with AVL/RBT interface)
     * @param review The review to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteReview(String airlineName, AirlineReview review) {
        if (airlineName == null || review == null) {
            return false;
        }
        
        return reviews.remove(review);
    }
    
    // ==================== RBAR CALCULATION ====================
    
    /**
     * Calculate Recency-Biased Average Rating (RBAR) for a specific airline.
     * Uses the shared RBARCalculator utility for consistent calculation across all data structures.
     * 
     * Time Complexity: O(N) where N is total number of reviews
     */
    public double calculateRBAR(String airlineName) {
        List<AirlineReview> airlineReviews = getReviewsByAirline(airlineName);
        if (airlineReviews.isEmpty()) {
            return 0.0;
        }
        
        // Use shared RBAR calculation utility
        return RBARCalculator.calculateRBAR(airlineReviews);
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
     * Time Complexity: O(N) where N is total number of reviews
     * 
     * @param airline The airline name
     * @param k Number of recent reviews to return
     * @return List of k most recent reviews, sorted by date (newest first)
     */
    public List<AirlineReview> getTopKRecentReviews(String airline, int k) {
        if (airline == null || k <= 0) {
            return new ArrayList<>();
        }
        
        List<AirlineReview> airlineReviews = getReviewsByAirline(airline);
        if (airlineReviews.isEmpty()) {
            return new ArrayList<>();
        }
        
        // Sort by date in descending order (newest first)
        airlineReviews.sort((a, b) -> b.getDate().compareTo(a.getDate()));
        
        // Return top k
        return airlineReviews.subList(0, Math.min(k, airlineReviews.size()));
    }
    
    // ==================== UTILITY METHODS ====================
    
    /**
     * Get the total number of reviews in the store.
     * Time Complexity: O(1)
     * 
     * @return The number of reviews
     */
    public int size() {
        return reviews.size();
    }
    
    /**
     * Check if the store is empty.
     * Time Complexity: O(1)
     * 
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return reviews.isEmpty();
    }
    
    /**
     * Clear all reviews from the store.
     * Time Complexity: O(1)
     */
    public void clear() {
        reviews.clear();
    }
}
