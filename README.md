# ✈️ CS201 Data Structures & Algorithms Project  
### Project Title: Finding the Best Airline by Review Trends  
**Team:** G3T4
**Course:** CS201 – Data Structures and Algorithms (AY2025/26 Term 1)  
**Institution:** Singapore Management University  

---

## 📌 Project Overview

This project analyzes user reviews from the **Skytrax User Reviews Dataset** to identify and maintain the **top-rated airlines** globally.  
The focus is on studying **how different data structures affect the performance** of real-time ranking updates when new reviews are added, with special emphasis on **recency-biased operations**.

We simulate a **dynamic leaderboard system**, where each new review may change the rankings. By keeping the same algorithmic logic constant but varying the data structure, we can observe the **trade-offs in time and space efficiency** for recency-biased average rating calculations and top-k retrieval operations.

---

## 🎯 Problem Statement

> Given a collection of airline reviews (including ratings, dates, and traveler types), design an algorithm that maintains the top-𝑘 airlines based on **recency-biased average rating** and efficiently retrieves the most recent reviews.  
> Each time a new review is added, the system must efficiently update rankings using a **recency weighting system** that prioritizes recent reviews over older ones.

---

## ⚙️ Algorithmic Context

We use a consistent **recency-biased ranking algorithm** across all experiments:

### Recency Weighting System:
- **Recent reviews (last 30 days)**: weight = 1.0 (full impact)
- **Medium age reviews (30 days to 3 years)**: linear decay from 1.0 to 0.1
- **Old reviews (3+ years)**: minimal weight = 0.05

### Core Operations:
1. **Recency-Biased Average Rating (RB-AR)**: Calculate weighted average considering review age
2. **Top-K Recent Retrieval**: Get k most recent reviews for any airline
3. **Dynamic Updates**: Efficiently handle new review insertions
4. **Search Operations**: Find reviews by airline name

---

## 🧩 Data Structures Compared

| Experiment | Data Structure | Status | Description | Key Operations Compared |
|------------|----------------|--------|--------------|--------------------------|
| **1** | **Linear List (ArrayList)** | ✅ **Complete** | Baseline implementation using ArrayList for storing reviews | Insert: O(1), Search: O(N), Top-K: O(N log N), RBAR: O(N) |
| **2** | **AVL Tree** | ✅ **Complete** | Self-balancing binary search tree with automatic rotations | Insert: O(log N), Search: O(log N), Top-K: O(N log N), RBAR: O(N) |
| **3** | **Recency-Biased Tree (RBT)** | ✅ **Complete** | Splay tree that intentionally biases recent reviews to root for fast access | Insert: O(log N), Recent Search: O(1), Old Search: O(N), Top-K: O(k) |

All implementations use the **same recency weighting algorithm**, allowing fair comparison of performance based solely on data structure design.

---

## 🧠 Experimental Goals

- Measure and compare **runtime performance** for different data structures as dataset size increases.  
- Observe **memory usage differences** across structures.  
- Identify scenarios where theoretical complexity diverges from **real-world performance**.

---

## 🧪 Evaluation Metrics

| Metric | Description |
|---------|--------------|
| **Runtime (ms)** | Time to update rankings after each new review |
| **Memory Usage (MB)** | Space used to store data structure |
| **Scalability** | Performance trend as input size increases |
| **Update Efficiency** | Time complexity for rank recalculation |

---

## 🧮 Theoretical Complexity Summary

| Data Structure | Insert | Search | Top-K Retrieval | RBAR Calculation | Space Complexity |
|----------------|---------|---------|-----------------|------------------|------------------|
| **Linear List** | O(1) amortized | O(N) | O(N log N) | O(N) | O(N) |
| **AVL Tree** | O(log N) | O(log N) | O(N log N) | O(N) | O(N) |
| **RBT Tree** | O(log N) | O(log N)* | O(k) for recent | O(N) | O(N) |

*Recent reviews: O(log N) to O(1), Old reviews: O(N)

### Key Performance Insights:
- **AVL Tree** provides significant improvement in search operations: O(N) → O(log N)
- **Linear List** has fastest insertion but slowest search
- **Top-K and RBAR** operations are dominated by sorting/processing, not data structure choice

---

## 📂 Project Structure

```
CS201/
├── src/main/java/com/reviews/
│   ├── Models/                          # Data model classes
│   │   ├── ReviewRecord.java           # Common interface for all review types
│   │   ├── AirlineReview.java          # Airline review implementation
│   │   ├── AirportReview.java          # Airport review implementation
│   │   ├── AirportLoungeReview.java    # Airport lounge review implementation
│   │   └── SeatReview.java             # Seat review implementation
│   │
│   ├── experiments/                     # Experiment implementations
│   │   ├── BenchmarkUtils.java         # Shared benchmark utilities
│   │   ├── UnifiedBenchmarkRunner.java # Cross-experiment comparison
│   │   ├── LinearListTest/             # Experiment 1: Linear List
│   │   │   ├── Main.java              # Experiment 1 main runner
│   │   │   ├── LinearListDemo.java    # Demonstration
│   │   │   ├── LinearListReviewStoreTest.java  # Test suite
│   │   │   ├── LinearListPerformanceBenchmark.java  # Performance analysis
│   │   │   └── results.csv            # Benchmark results
│   │   ├── experiment2/                # Experiment 2: AVL Tree
│   │   │   ├── Main2.java             # Experiment 2 main runner
│   │   │   ├── AVLDemo.java           # Demonstration
│   │   │   ├── AVLReviewStoreTest.java # Test suite
│   │   │   ├── AVLPerformanceBenchmark.java # Performance analysis
│   │   │   └── results.csv            # Benchmark results
│   │   └── experiment3/                # Experiment 3: RBT
│   │       ├── Main3.java             # Experiment 3 main runner
│   │       ├── RBTDemo.java           # Demonstration
│   │       ├── RBTReviewStoreTest.java # Test suite
│   │       ├── RBTPerformanceBenchmark.java # Performance analysis
│   │       └── results.csv            # Benchmark results
│   │
│   └── datastructures/                  # Data structure implementations
│       ├── LinearListReviewStore.java  # Experiment 1 implementation
│       ├── AVLNode.java                # AVL tree node
│       ├── AVLReviewStore.java         # Experiment 2 implementation
│       ├── RBTNode.java                # RBT tree node
│       └── RBTReviewStore.java         # Experiment 3 implementation
│
├── data/                               # Dataset files
│   ├── airline.csv                    # 41,457 airline reviews
│   ├── airport.csv                    # 17,748 airport reviews  
│   ├── lounge.csv                     # 2,277 lounge reviews
│   └── seat.csv                       # 1,261 seat reviews
│
└── README.md                          # This file
```

---

## 🚀 How to Run the Experiments

### Prerequisites
- Java 8 or higher
- IDE with Java support (IntelliJ IDEA, Eclipse, VS Code)

### Running Experiments

#### Run All Experiments (Recommended)
The unified benchmark runner compares all three data structures:
```bash
# Navigate to the experiments directory
cd src/main/java/com/reviews/experiments/

# Compile and run unified benchmark
javac *.java
java UnifiedBenchmarkRunner
```
This will run comparative benchmarks across all experiments and generate results.csv files for each.

#### Individual Experiments

**Experiment 1: Linear List Baseline**
```bash
cd src/main/java/com/reviews/experiments/experiment1/
javac *.java
java Main1
```

**Experiment 2: AVL Tree Implementation**
```bash
cd src/main/java/com/reviews/experiments/experiment2/
javac *.java
java Main2
```

**Experiment 3: RBT Implementation**
```bash
cd src/main/java/com/reviews/experiments/experiment3/
javac *.java
java Main3
```

### Individual Components
Each experiment directory contains:
- **Main Runner**: `java Main1`, `java Main2`, or `java Main3` - Runs the full demo and tests
- **Tests Only**: `java LinearListReviewStoreTest`, `java AVLReviewStoreTest`, `java RBTReviewStoreTest`
- **Benchmarks Only**: `java LinearListPerformanceBenchmark`, `java AVLPerformanceBenchmark`, `java RBTPerformanceBenchmark`
- **Demos Only**: `java LinearListDemo`, `java AVLDemo`, `java RBTDemo`

---

## 📊 Current Implementation Status

### ✅ Completed Features
- **Data Theory**: Complete interface and 4 review type implementations
- **Experiment 1**: Full linear list implementation with comprehensive testing
- **Experiment 2**: Complete AVL tree implementation with automatic balancing
- **Experiment 3**: Complete RBT (Recency-Biased Tree) implementation with splay operations
- **Testing Framework**: Comprehensive test suites for all three experiments
- **Performance Benchmarking**: Detailed performance analysis and comparison
- **Unified Benchmark Runner**: Cross-experiment comparison with automated CSV generation
- **Recency Weighting**: Consistent algorithm across all implementations

### 🔬 Key Findings
- **AVL Tree** shows significant improvement in search operations over linear list: O(N) → O(log N)
- **RBT** provides O(1) to O(log N) access for recent reviews, optimal for recency-biased workloads
- **Recency weighting** effectively prioritizes recent reviews while maintaining historical context
- **Tree balancing** ensures consistent performance regardless of insertion order in AVL
- **Memory usage** is comparable across all implementations (O(N) space complexity)

---

## 🎯 Future Enhancements
1. Real Dataset Integration: Load and process actual Skytrax CSV data
2. Performance Visualization: Generate charts and graphs from benchmark results
3. Memory Profiling: Measure actual memory usage differences
4. Extended Testing: Add stress tests with larger datasets (100K+ reviews)
