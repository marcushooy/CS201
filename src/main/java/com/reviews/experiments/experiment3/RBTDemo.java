package com.reviews.experiments.experiment3;

import com.reviews.Models.AirlineReview;
import com.reviews.Models.ReviewRecord;
import com.reviews.datastructures.RBTReviewStore;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class RBTDemo {

    public static void main(String[] args) {
        System.out.println("=== Recency-Biased Tree (RBT) Experiment 3 Demonstration ===\n");

        RBTReviewStore store = new RBTReviewStore(); // Default: splay to root
        addSampleData(store);

        demonstrateBasicOperations(store);
        demonstrateRBTFeatures(store);
        demonstrateRecencyBiasedOperations(store);
        demonstratePerformance(store);

        // Minimal “tests” (you can copy your AVL test suite and adapt types if you want full parity)
        System.out.println("\n=== Quick Smoke Checks ===");
        if (store.size() <= 0) throw new AssertionError("Size should be > 0");
        System.out.println("✓ RBT smoke checks passed");
    }

    private static void addSampleData(RBTReviewStore store) {
        System.out.println("Adding sample airline review data to RBT...");

        LocalDate now = LocalDate.now();

        store.addReview(new AirlineReview("Delta", "link1", "Excellent recent flight", "John Doe", "USA",
                now.minusDays(5).toString(), "Outstanding service and comfort", "Boeing 737",
                "Business", "Economy", "LAX-JFK", 4.8, 4.5, 4.8, 4.2, 4.0, 4.5, 3.5, 4.8, 1));

        store.addReview(new AirlineReview("Delta", "link2", "Good experience", "Jane Smith", "USA",
                now.minusDays(15).toString(), "Comfortable flight with good service", "Airbus A320",
                "Leisure", "Economy", "SFO-LAX", 4.2, 4.0, 4.2, 3.8, 4.0, 3.8, 3.0, 4.2, 1));

        store.addReview(new AirlineReview("Delta", "link3", "Decent but old", "Bob Johnson", "USA",
                now.minusDays(60).toString(), "Average experience from months ago", "Boeing 777",
                "Business", "Business", "JFK-LHR", 3.5, 3.0, 3.5, 3.0, 3.5, 3.0, 2.0, 3.5, 0));

        store.addReview(new AirlineReview("Delta", "link4", "Very old review", "Charlie Wilson", "USA",
                now.minusYears(4).toString(), "Very outdated experience", "Boeing 747",
                "Business", "First", "ATL-CDG", 2.0, 1.5, 2.0, 1.5, 2.0, 1.5, 1.0, 2.0, 0));

        store.addReview(new AirlineReview("United", "link5", "Recent United flight", "Alice Brown", "USA",
                now.minusDays(10).toString(), "Good recent experience", "Boeing 787",
                "Leisure", "Economy", "ORD-NRT", 4.0, 3.5, 4.0, 3.5, 4.0, 3.5, 2.5, 4.0, 1));

        store.addReview(new AirlineReview("United", "link6", "Old United review", "Diana Davis", "USA",
                now.minusYears(5).toString(), "Very outdated experience", "Boeing 737",
                "Leisure", "Economy", "DEN-LAX", 1.5, 1.0, 1.5, 1.0, 1.5, 1.0, 0.5, 1.5, 0));

        store.addReview(new AirlineReview("American", "link7", "Recent American flight", "Eve Miller", "USA",
                now.minusDays(8).toString(), "Good service recently", "Boeing 737",
                "Business", "Economy", "DFW-LAX", 4.3, 4.0, 4.3, 3.8, 4.0, 3.8, 3.0, 4.3, 1));

        store.addReview(new AirlineReview("American", "link8", "Another recent flight", "Frank Garcia", "USA",
                now.minusDays(20).toString(), "Satisfactory recent experience", "Airbus A321",
                "Leisure", "Economy", "LAX-ORD", 3.9, 3.5, 3.9, 3.5, 3.9, 3.5, 2.8, 3.9, 1));

        store.addReview(new AirlineReview("Southwest", "link9", "Southwest review", "Grace Lee", "USA",
                now.minusDays(12).toString(), "Good value", "Boeing 737",
                "Leisure", "Economy", "DAL-HOU", 4.1, 4.0, 4.1, 3.9, 4.0, 3.9, 3.1, 4.1, 1));

        store.addReview(new AirlineReview("JetBlue", "link10", "JetBlue review", "Henry Kim", "USA",
                now.minusDays(18).toString(), "Nice experience", "Airbus A320",
                "Business", "Economy", "JFK-BOS", 4.3, 4.0, 4.3, 3.8, 4.0, 3.8, 3.0, 4.3, 1));

        System.out.println("✓ Added " + store.size() + " sample reviews");
        System.out.println("✓ Airlines: " + store.getAllAirlines() + "\n");
    }

    private static void demonstrateBasicOperations(RBTReviewStore store) {
        System.out.println("=== Basic Operations Demo ===");
        var stats = store.getTreeStatistics();
        System.out.println("Store Statistics:");
        System.out.println("  Total reviews: " + stats.get("totalReviews"));
        System.out.println("  Unique airlines: " + stats.get("uniqueAirlines"));
        System.out.println("  Date range: " + stats.get("oldestReview") + " to " + stats.get("newestReview"));
        System.out.println();

        for (String airline : store.getAllAirlines()) {
            List<ReviewRecord> reviews = store.getReviewsByAirline(airline);
            System.out.println(airline + " has " + reviews.size() + " reviews");
        }
        System.out.println();
    }

    private static void demonstrateRBTFeatures(RBTReviewStore store) {
        System.out.println("=== RBT-Specific Features Demo ===");
        var treeStats = store.getTreeStatistics();
        System.out.println("RBT Statistics:");
        System.out.println("  Tree Height (max across airlines): " + treeStats.get("treeHeight"));
        System.out.println("  Is Balanced: " + treeStats.get("isBalanced") + "  (intentional)");
        System.out.println("  Avg Reviews per Airline: " + String.format("%.2f", treeStats.get("avgReviewsPerAirline")));
        System.out.println();
    }

    private static void demonstrateRecencyBiasedOperations(RBTReviewStore store) {
        System.out.println("=== Recency-Biased Operations Demo ===");

        System.out.println("Top-3 Most Recent Reviews by Airline:");
        for (String airline : store.getAllAirlines()) {
            List<ReviewRecord> top3 = store.getTopKRecentReviews(airline, 3);
            System.out.println("  " + airline + ":");
            for (int i = 0; i < top3.size(); i++) {
                ReviewRecord r = top3.get(i);
                String preview = r.getContent();
                preview = preview.substring(0, Math.min(50, preview.length()));
                System.out.println("    " + (i + 1) + ". " + r.getDate()
                        + " - Rating: " + String.format("%.1f", r.getOverallRating())
                        + " - " + preview + "...");
            }
        }
        System.out.println();

        System.out.println("Recency-Biased Average Ratings (RB-AR):");
        System.out.println("Note: ≤30d full weight; >3y tiny weight.\n");
        for (String airline : store.getAllAirlines()) {
            double rbar = store.calculateRecencyBiasedAverageRating(airline);
            List<ReviewRecord> rs = store.getReviewsByAirline(airline);
            double flat = rs.stream().mapToDouble(ReviewRecord::getOverallRating).average().orElse(0.0);
            System.out.println("  " + airline + ":");
            System.out.println("    RB-AR: " + String.format("%.3f", rbar));
            System.out.println("    Simple Average: " + String.format("%.3f", flat));
            System.out.println("    Difference: " + String.format("%+.3f", rbar - flat) + "\n");
        }
    }

    private static void demonstratePerformance(RBTReviewStore store) {
        System.out.println("=== Performance Demo (micro) ===");
        // Tiny micro-bench similar to your AVL demo. For rigorous runs, clone AVLPerformanceBenchmark,
        // replace types with RecencyBiasedReviewStore, and compare.
        int iterations = 1_000;

        // Search by airline (retrieval of list)
        long t0 = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            for (String a : store.getAllAirlines()) {
                store.getReviewsByAirline(a);
            }
        }
        long t1 = System.nanoTime();
        double perOpMs = ((t1 - t0) / (double) (iterations * store.getAllAirlines().length)) / 1_000_000.0;
        System.out.println("  getReviewsByAirline: " + String.format("%.3f", perOpMs) + " ms/op");

        // Top-k (k=10)
        int k = 10;
        long t2 = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            for (String a : store.getAllAirlines()) {
                store.getTopKRecentReviews(a, k);
            }
        }
        long t3 = System.nanoTime();
        double perTopK = ((t3 - t2) / (double) (iterations * store.getAllAirlines().length)) / 1_000_000.0;
        System.out.println("  Top-" + k + " retrieval: " + String.format("%.3f", perTopK) + " ms/op");

        // RB-AR
        long t4 = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            for (String a : store.getAllAirlines()) {
                store.calculateRecencyBiasedAverageRating(a);
            }
        }
        long t5 = System.nanoTime();
        double perRBAR = ((t5 - t4) / (double) (iterations * store.getAllAirlines().length)) / 1_000_000.0;
        System.out.println("  RB-AR calculation: " + String.format("%.3f", perRBAR) + " ms/op\n");

        System.out.println("Time Complexity (theoretical):");
        System.out.println("  Insert: O(h) (worst O(N), by design) with splay-on-insert");
        System.out.println("  Top-K Recent: ~O(k)");
        System.out.println("  RB-AR: sublinear in practice due to newest-first early stop");
    }
}
