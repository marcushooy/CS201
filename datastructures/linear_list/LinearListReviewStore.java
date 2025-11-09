import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * LinearListReviewStore - A simple ArrayList-based data structure for storing airline reviews.
 * 
 * This implementation uses an ArrayList as the underlying storage mechanism,
 * providing O(1) insert and O(N) search operations.
 * 
 * Time Complexity:
 * - Insert: O(1) amortized
 * - Search by airline: O(N)
 * - Update: O(1)
 * - Delete: O(N)
 * 
 * Note: CSV loading and RBAR calculation are handled by shared utility classes.
 */
public class LinearListReviewStore {
    private ArrayList<AirlineReview> reviews;
    
    /**
     * Constructor - Initialize empty ArrayList
     */
    public LinearListReviewStore() {
        this.reviews = new ArrayList<>();
    }
    
    // ==================== CRUD Operations ====================
    
    /**
     * Insert a review into the list.
     * Time Complexity: O(1) amortized
     * 
     * @param review The AirlineReview to insert
     */
    public void insert(AirlineReview review) {
        if (review != null) {
            reviews.add(review);
        }
    }
    
    /**
     * Search for all reviews of a specific airline.
     * Time Complexity: O(N)
     * 
     * @param airlineName The name of the airline to search for
     * @return List of all reviews for that airline
     */
    public List<AirlineReview> searchByAirline(String airlineName) {
        if (airlineName == null || airlineName.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        return reviews.stream()
                .filter(review -> review.getAirlineName().equalsIgnoreCase(airlineName))
                .collect(Collectors.toList());
    }
    
    /**
     * Update a review at a specific index.
     * Time Complexity: O(1)
     * 
     * @param index The index of the review to update
     * @param updatedReview The new review data
     * @return true if update was successful, false otherwise
     */
    public boolean update(int index, AirlineReview updatedReview) {
        if (index >= 0 && index < reviews.size() && updatedReview != null) {
            reviews.set(index, updatedReview);
            return true;
        }
        return false;
    }
    
    /**
     * Delete a review at a specific index.
     * Time Complexity: O(N) - requires shifting elements
     * 
     * @param index The index of the review to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean delete(int index) {
        if (index >= 0 && index < reviews.size()) {
            reviews.remove(index);
            return true;
        }
        return false;
    }
    
    /**
     * Get a review by its index.
     * Time Complexity: O(1)
     * 
     * @param index The index of the review
     * @return The AirlineReview at that index, or null if invalid index
     */
    public AirlineReview getReviewByIndex(int index) {
        if (index >= 0 && index < reviews.size()) {
            return reviews.get(index);
        }
        return null;
    }
    
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
    
    // ==================== Helper Methods ====================
    // Note: RBAR calculation will be implemented as a shared utility
    
    /**
     * Get all unique airline names in the store.
     * Time Complexity: O(N)
     * 
     * @return Set of unique airline names
     */
    public Set<String> getAllUniqueAirlines() {
        Set<String> airlines = new HashSet<>();
        for (AirlineReview review : reviews) {
            airlines.add(review.getAirlineName());
        }
        return airlines;
    }
    
    /**
     * Get all reviews in the store (for iteration/analysis).
     * Time Complexity: O(1) - returns reference to the list
     * 
     * @return List of all reviews
     */
    public List<AirlineReview> getAllReviews() {
        return new ArrayList<>(reviews);  // Return a copy to prevent external modification
    }
    
    /**
     * Clear all reviews from the store.
     * Time Complexity: O(1)
     */
    public void clear() {
        reviews.clear();
    }
}

