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
| **3** | **HashMap + Linked List Hybrid** | ⚠️ **Planned** | Maps airlines to scores while keeping recent updates in an ordered list | Lookup: O(1), Update: O(1), Rebuild ranking: O(N log N) |

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
| **HashMap + Linked List** | O(1)* | O(1)* | O(N log N) | O(N) | O(N) |

\*Average case assuming uniform hashing and efficient updates.

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
│   ├── datastructures/                  # Data structure implementations
│   │   ├── LinearListReviewStore.java  # Experiment 1: ArrayList baseline
│   │   ├── AVLNode.java                # AVL tree node implementation
│   │   └── AVLReviewStore.java         # Experiment 2: AVL tree implementation
│   │
│   ├── experiments/
│   │   ├── LinearListTest/             # Experiment 1 testing & benchmarking
│   │   │   ├── Main.java              # Experiment 1 main runner
│   │   │   ├── LinearListDemo.java    # Demonstration of linear list features
│   │   │   ├── LinearListReviewStoreTest.java  # Comprehensive test suite
│   │   │   └── LinearListPerformanceBenchmark.java  # Performance analysis
│   │   │
│   │   ├── experiment2/                # Experiment 2: AVL Tree
│   │   │   ├── Main2.java             # Experiment 2 main runner
│   │   │   ├── AVLDemo.java           # Demonstration of AVL tree features
│   │   │   ├── AVLReviewStoreTest.java # Comprehensive test suite
│   │   │   ├── AVLPerformanceBenchmark.java # Performance analysis
│   │   │   └── results.csv            # Performance results data
│   │   │
│   │   └── experiment3/                # Experiment 3: HashMap + Linked List (Planned)
│   │       ├── Main3.java             # Placeholder
│   │       └── results.csv            # Empty placeholder
│   │
│   └── NewClass.java                   # Unused placeholder class
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

#### Experiment 1: Linear List Baseline
```bash
# Navigate to the project directory
cd CS201/src/main/java/com/reviews/experiments/LinearListTest/

# Compile and run
javac *.java
java Main
```

#### Experiment 2: AVL Tree Implementation
```bash
# Navigate to the project directory  
cd CS201/src/main/java/com/reviews/experiments/experiment2/

# Compile and run
javac *.java
java Main2
```

### Individual Components
- **Run Tests Only**: `java LinearListReviewStoreTest` or `java AVLReviewStoreTest`
- **Run Benchmarks Only**: `java LinearListPerformanceBenchmark` or `java AVLPerformanceBenchmark`
- **Run Demos Only**: `java LinearListDemo` or `java AVLDemo`

---

## 📊 Current Implementation Status

### ✅ Completed Features
- **Data Models**: Complete interface and 4 review type implementations
- **Experiment 1**: Full linear list implementation with comprehensive testing
- **Experiment 2**: Complete AVL tree implementation with automatic balancing
- **Testing Framework**: Comprehensive test suites for both experiments
- **Performance Benchmarking**: Detailed performance analysis and comparison
- **Recency Weighting**: Consistent algorithm across all implementations

### ⚠️ Planned Features
- **Experiment 3**: HashMap + Linked List hybrid implementation
- **Performance Comparison**: Cross-experiment analysis and visualization
- **Real Dataset Integration**: Loading and processing actual Skytrax data

### 🔬 Key Findings (Preliminary)
- **AVL Tree** shows significant improvement in search operations over linear list
- **Recency weighting** effectively prioritizes recent reviews while maintaining historical context
- **Tree balancing** ensures consistent performance regardless of insertion order
- **Memory usage** is comparable across implementations (O(N) space complexity)

---

## 🎯 Next Steps
1. Implement Experiment 3 (HashMap + Linked List hybrid)
2. Conduct comprehensive performance comparison across all three data structures
3. Analyze real-world performance vs theoretical complexity
4. Generate performance visualization and analysis reports
