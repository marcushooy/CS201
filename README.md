# ✈️ CS201 Data Structures & Algorithms Project  
### Project Title: Finding the Best Airline by Review Trends  
**Team:** G3T4
**Course:** CS201 – Data Structures and Algorithms (AY2025/26 Term 1)  
**Institution:** Singapore Management University  

---

## 📌 Project Overview

This project analyzes user reviews from the **Skytrax User Reviews Dataset** to identify and maintain the **top-rated airlines** globally.  
The focus is not only on finding the best lounges but also on studying **how different data structures affect the performance** of real-time ranking updates when new reviews are added.

We simulate a **dynamic leaderboard system**, where each new review may change the rankings. By keeping the same algorithmic logic constant but varying the data structure, we can observe the **trade-offs in time and space efficiency**.

---

## 🎯 Problem Statement

> Given a collection of lounge reviews (including ratings, dates, and traveler types), design an algorithm that maintains the top-𝑘 airport lounges based on average rating and recency.  
> Each time a new review is added, the system must efficiently update the top-𝑘 ranking.

---

## ⚙️ Algorithmic Context

We use a consistent **ranking algorithm** across all experiments:
1. Aggregate ratings for each lounge.
2. Compute a composite score based on weighted averages (recent reviews are weighted more heavily).
3. Update the top-𝑘 lounges dynamically after each insertion.

---

## 🧩 Data Structures Compared

| Experiment | Data Structure | Description | Key Operations Compared |
|-------------|----------------|--------------|--------------------------|
| **1** | **Min-Heap** | Maintains top-k lounges efficiently; root stores smallest in top set | Insert, Delete-Min |
| **2** | **Balanced BST (AVL Tree)** | Stores lounges sorted by score; rebalances on insert | Insert, Search, Rotation overhead |
| **3** | **HashMap + Linked List Hybrid** | Maps lounges to scores while keeping recent updates in an ordered list | Lookup, Update, Rebuild ranking |

All three use the **same ranking algorithm**, allowing comparison of performance based solely on data structure design.

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

| Data Structure | Insert | Delete | Search | Space Complexity |
|----------------|---------|---------|---------|------------------|
| **Heap** | O(log n) | O(log n) | O(1) | O(n) |
| **AVL Tree** | O(log n) | O(log n) | O(log n) | O(n) |
| **HashMap + Linked List** | O(1)* | O(n)* | O(1) | O(n) |

\*Average case assuming uniform hashing and lazy reordering.

---

## 📂 Folder Structure
│
├── /experiment1_min_heap/
│   ├── MinHeapRanking.java
│   ├── Main.java
│   └── results.csv
│
├── /experiment2_avl_tree/
│   ├── AVLRanking.java
│   ├── Main.java
│   └── results.csv
│
├── /experiment3_hashmap_linkedlist/
│   ├── HashMapRanking.java
│   ├── Main.java
│   └── results.csv
│
├── /data/
│   └── skytrax_reviews_sample.csv
│
├── README.md
└── run_instructions.txt
