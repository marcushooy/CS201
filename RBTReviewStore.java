package com.reviews.store;

import com.reviews.model.AirlineReview;
import com.reviews.struct.RecencyBiasedTree;
import com.reviews.util.RecencyUtils;

import java.time.LocalDate;
import java.util.*;

/**
 * RBTReviewStore = Recency-Biased Tree based implementation
 * for airline reviews.
 */
public class RBTReviewStore {

    // One recency-biased tree per airline, ordered by reviewDate
    private final Map<String, RecencyBiasedTree<AirlineReview>> airlineTrees = new HashMap<>();

    // ---------- CRUD-style methods ----------

    public void addReview(AirlineReview review) {
        if (review == null || review.getAirline() == null) return;

        String airline = review.getAirline();
        RecencyBiasedTree<AirlineReview> tree = airlineTrees.computeIfAbsent(
                airline,
                k -> new RecencyBiasedTree<>(
                        (a, b) -> a.getReviewDate().compareTo(b.getReviewDate())
                )
        );

        tree.insert(review);
    }

    public List<AirlineReview> getReviewsByAirline(String airline) {
        if (airline == null) return Collections.emptyList();
        RecencyBiasedTree<AirlineReview> tree = airlineTrees.get(airline);
        if (tree == null) return Collections.emptyList();

        // Oldest → newest
        return tree.inOrderTraversal();
    }

    public List<AirlineReview> getAllReviews() {
        List<AirlineReview> all = new ArrayList<>();
        for (RecencyBiasedTree<AirlineReview> tree : airlineTrees.values()) {
            all.addAll(tree.inOrderTraversal());
        }
        return all;
    }

    // Optional CRUD extras if you need them:
    // public void deleteReview(...) { ... }
    // public void updateReview(...) { ... }

    // ---------- Recency-biased operations ----------

    public double calculateRecencyBiasedAverageRating(String airline, LocalDate now) {
        List<AirlineReview> reviews = getReviewsByAirline(airline);
        return RecencyUtils.calculateRecencyBiasedAverageRating(reviews, now);
    }

    public List<AirlineReview> getTopKRecentReviews(String airline, int k) {
        if (airline == null || k <= 0) return Collections.emptyList();
        RecencyBiasedTree<AirlineReview> tree = airlineTrees.get(airline);
        if (tree == null) return Collections.emptyList();

        // Newest → older
        return tree.reverseInOrderTakeK(k);
    }
}
