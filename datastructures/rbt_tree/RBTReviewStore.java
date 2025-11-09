package datastructures.rbt_tree;

import models.AirlineReview;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * RBTReviewStore = Recency-Biased Tree based implementation
 * for airline reviews.
 */
public class RBTReviewStore {

    // One recency-biased tree per airline, ordered by review date (ascending)
    private final Map<String, RecencyBiasedTree<AirlineReview>> airlineTrees = new HashMap<>();

    // ---------- CRUD-style methods ----------

    /** Insert a new review into the store. */
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

    /** Get all reviews for a specific airline (oldest → newest). */
    public List<AirlineReview> getReviewsByAirline(String airline) {
        if (airline == null) return Collections.emptyList();
        RecencyBiasedTree<AirlineReview> tree = airlineTrees.get(airline);
        if (tree == null) return Collections.emptyList();

        return tree.inOrderTraversal();  // oldest → newest
    }

    /** Get all reviews across all airlines (oldest → newest within each airline). */
    public List<AirlineReview> getAllReviews() {
        List<AirlineReview> all = new ArrayList<>();
        for (RecencyBiasedTree<AirlineReview> tree : airlineTrees.values()) {
            all.addAll(tree.inOrderTraversal());
        }
        return all;
    }

    // ---------- Recency-biased operations ----------

    /**
     * Recency-Biased Average Rating (RBAR) for a given airline.
     *
     * Weighting:
     *  - 0–30 days: weight 1.0
     *  - 30 days–3 years: linear decay to 0.1
     *  - >3 years: weight 0.05
     */
    public double calculateRecencyBiasedAverageRating(String airline, LocalDate now) {
        List<AirlineReview> reviews = getReviewsByAirline(airline);
        if (reviews.isEmpty()) return 0.0;

        double weightedSum = 0.0;
        double weightSum = 0.0;

        for (AirlineReview r : reviews) {
            LocalDate reviewDate = LocalDate.parse(r.getDate()); // "YYYY-MM-DD"
            long days = ChronoUnit.DAYS.between(reviewDate, now);

            double w;
            if (days <= 30) {
                w = 1.0;
            } else if (days <= 1095) { // ~3 years
                w = 1.0 - (0.9 * (days - 30) / 1065.0);
            } else {
                w = 0.05;
            }

            weightedSum += r.getOverallRating() * w;
            weightSum += w;
        }

        return (weightSum == 0.0) ? 0.0 : weightedSum / weightSum;
    }

    /**
     * Get the top K most recent reviews for an airline.
     * Uses the recency-biased tree's reverse in-order traversal.
     */
    public List<AirlineReview> getTopKRecentReviews(String airline, int k) {
        if (airline == null || k <= 0) return Collections.emptyList();
        RecencyBiasedTree<AirlineReview> tree = airlineTrees.get(airline);
        if (tree == null) return Collections.emptyList();

        // Newest → older
        return tree.reverseInOrderTakeK(k);
    }
}
