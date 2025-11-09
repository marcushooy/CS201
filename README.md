# CS201 Project - Data Structures for Airline Reviews

This project implements and compares three data structures (Linear List, AVL Tree, RBT Tree) for storing and managing airline reviews with RBAR (Recency-Biased Average Rating) calculations.

## 📋 Prerequisites

- **Java JDK 8 or higher** (check with `java -version`)
- **Git** (to clone the repository)

## 🚀 Quick Start - Run All Tests

### Step 1: Clone the Repository
```bash
git clone <repository-url>
cd "201 Project"
```

### Step 2: Make the Script Executable
```bash
chmod +x run_tests.sh
```

### Step 3: Run All Tests
```bash
./run_tests.sh
```

**That's it!** The script will:
- ✅ Compile all necessary files
- ✅ Run tests for Linear List, AVL Tree, and RBT Tree
- ✅ Display results for all data structures

---

## 📝 Manual Steps (Alternative)

If you prefer to run tests manually:

### Step 1: Compile All Files
```bash
javac models/*.java utils/*.java datastructures/linear_list/*.java datastructures/avl_tree/*.java datastructures/rbt_tree/*.java
```

### Step 2: Run Individual Tests

**Linear List Test:**
```bash
java -cp . datastructures.linear_list.LinearListTest
```

**AVL Tree Test:**
```bash
java -cp . datastructures.avl_tree.AVLTest
```

**RBT Tree Test:**
```bash
java -cp . datastructures.rbt_tree.RBTTest
```

---

## 📊 What the Tests Do

Each test suite verifies:
- ✅ **CREATE**: Adding reviews (`addReview()`, `addReviews()`)
- ✅ **READ**: Retrieving reviews by airline (`getReviewsByAirline()`, `getAllAirlines()`)
- ✅ **UPDATE**: Updating existing reviews (`updateReview()`)
- ✅ **DELETE**: Removing reviews (`deleteReview()`)
- ✅ **RBAR**: Calculating Recency-Biased Average Rating (`calculateRBAR()`)
- ✅ **RANKINGS**: Getting airline rankings (`getAirlineRankings()`, `getTopKAirlines()`)

All tests use **real data** from `data/airline.csv` (41,350+ reviews).

---

## 📁 Project Structure

```
201 Project/
├── data/
│   └── airline.csv              # Real airline review data (41,350+ reviews)
├── models/
│   ├── AirlineReview.java       # Review data model
│   └── AirlineRanking.java      # Ranking data model
├── utils/
│   ├── CSVLoader.java          # Load data from CSV
│   ├── RBARCalculator.java     # RBAR calculation logic
│   └── RankingUtils.java       # Ranking calculation logic
├── datastructures/
│   ├── linear_list/
│   │   ├── LinearListReviewStore.java
│   │   └── LinearListTest.java
│   ├── avl_tree/
│   │   ├── AVLNode.java
│   │   ├── AVLReviewStore.java
│   │   └── AVLTest.java
│   └── rbt_tree/
│       ├── RecencyBiasedTree.java
│       ├── RBTReviewStore.java
│       └── RBTTest.java
└── run_tests.sh                 # One-click test runner
```

---

## 🔍 Troubleshooting

### "Command not found: javac"
- Install Java JDK (not just JRE)
- On macOS: `brew install openjdk`
- On Linux: `sudo apt-get install default-jdk`

### "Permission denied" when running script
```bash
chmod +x run_tests.sh
```

### "FileNotFoundException: data/airline.csv"
- Make sure you're in the project root directory
- Verify `data/airline.csv` exists

### Compilation Errors
- Make sure you're in the project root directory
- Check that all files are present (run `git pull` if needed)

---

## 📚 Understanding RBAR

**RBAR (Recency-Biased Average Rating)** is a weighted average that gives more importance to recent reviews:

- **Recent reviews (last 30 days)**: Full weight (1.0)
- **Medium age (30 days - 3 years)**: Linear decay (1.0 → 0.1)
- **Old reviews (3+ years)**: Minimal weight (0.05)

This ensures current service quality is reflected more accurately than old reviews.

---

## ✅ Expected Output

When you run `./run_tests.sh`, you should see:

```
╔════════════════════════════════════════════════════════════════╗
║         CS201 Project - Running All Tests                     ║
╚════════════════════════════════════════════════════════════════╝

🔨 Compiling...
✅ Compilation successful!

════════════════════════════════════════════════════════════════
TEST 1: Linear List
════════════════════════════════════════════════════════════════
📂 Loading airline reviews from: data/airline.csv
✅ Loaded 41350 reviews from CSV
...
✅ ALL TESTS COMPLETED SUCCESSFULLY!
```

All three data structures should show **✅ ALL TESTS COMPLETED SUCCESSFULLY!**

---

## 🤝 Contributing

When making changes:
1. Test your changes: `./run_tests.sh`
2. Commit: `git commit -m "Your message"`
3. Push: `git push origin main`

---

## 📧 Questions?

Check the code comments in each file for detailed explanations of:
- Time complexity
- Method signatures
- Implementation details

