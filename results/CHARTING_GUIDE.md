# 📊 Quick Charting Guide

## 🎯 4 Pre-Made Charts - Just Import & Click!

I've created **4 separate CSV files** - one for each operation. Each file is ready to import into Excel and create a chart with **one click**!

---

## 📈 How to Create Charts (2 Minutes Each)

### Chart 1: Search Performance ⭐ MOST IMPORTANT

**File:** `1_search_comparison.csv`

**What it shows:** AVL Tree is 289x faster than Linear List!

**Steps:**
1. Open Excel
2. File → Import → `1_search_comparison.csv`
3. Select ALL cells (Ctrl+A / Cmd+A)
4. Insert → **Line Chart** or **Column Chart**
5. Done!

**Chart Title:** "Search Performance by Data Structure"  
**X-axis:** Number of Reviews  
**Y-axis:** Time (milliseconds)  

**Key Finding:** Linear List grows from 0.035ms to 0.291ms (8.3x growth = O(N)), while AVL Tree stays at <0.001ms (O(log N))

---

### Chart 2: Insertion Performance

**File:** `2_insertion_comparison.csv`

**What it shows:** Linear List is fastest for insertion, RBT is slowest

**Steps:**
1. Open Excel
2. File → Import → `2_insertion_comparison.csv`
3. Select ALL cells (Ctrl+A / Cmd+A)
4. Insert → **Column Chart** (better than line for this)
5. Done!

**Chart Title:** "Insertion Performance by Data Structure"  
**X-axis:** Number of Reviews  
**Y-axis:** Time (milliseconds)  

**Key Finding:** Linear List maintains O(1) insertion (0.007ms → 0.021ms), AVL requires rebalancing, RBT is slow due to splay operations

---

### Chart 3: RBAR Calculation Performance

**File:** `3_rbar_comparison.csv`

**What it shows:** RBT and AVL are much faster than Linear List

**Steps:**
1. Open Excel
2. File → Import → `3_rbar_comparison.csv`
3. Select ALL cells (Ctrl+A / Cmd+A)
4. Insert → **Line Chart**
5. Done!

**Chart Title:** "RBAR Calculation Performance"  
**X-axis:** Number of Reviews  
**Y-axis:** Time (milliseconds)  

**Key Finding:** RBT is 43x faster than Linear List because recent reviews (which have higher weight) are at the root

---

### Chart 4: Top-K Retrieval Performance

**File:** `4_topk_comparison.csv`

**What it shows:** RBT dominates Top-K operations (100x faster!)

**Steps:**
1. Open Excel
2. File → Import → `4_topk_comparison.csv`
3. Select ALL cells (Ctrl+A / Cmd+A)
4. Insert → **Line Chart**
5. Done!

**Chart Title:** "Top-K Retrieval Performance (K=50)"  
**X-axis:** Number of Reviews  
**Y-axis:** Time (milliseconds)  

**Key Finding:** RBT keeps recent reviews at root, making Top-K retrieval 100x faster than Linear List

---

## 🎨 Making Charts Look Professional

After creating each chart:

### 1. Format Y-Axis
- Right-click Y-axis → Format Axis
- Set minimum to 0
- For Search chart: Consider log scale if Linear List dominates

### 2. Add Data Labels (Optional)
- Click chart → Chart Design → Add Chart Element → Data Labels
- Shows exact values on chart

### 3. Change Colors
- Click a data series → Format → Fill
- Suggested colors:
  - Linear List: 🔴 Red (baseline/slow)
  - AVL Tree: 🟢 Green (winner/fast)
  - RBT: 🔵 Blue (specialized)

### 4. Add Legend
- Chart Design → Add Chart Element → Legend → Bottom

### 5. Add Title and Axis Labels
- Chart Design → Add Chart Element → Axis Titles
- X-axis: "Number of Reviews"
- Y-axis: "Time (milliseconds)"

---

## 📊 Google Sheets (Alternative)

If using Google Sheets instead of Excel:

1. Open Google Sheets
2. File → Import → Upload → Select CSV file
3. Select "Replace current sheet"
4. Select ALL cells (Ctrl+A)
5. Insert → Chart
6. Google will auto-detect the best chart type!
7. Customize using Chart Editor panel

---

## 🎯 For Your Report - Which Charts to Include

### Minimum (Pick 2):
1. ⭐ **Search Performance** (most important - shows 289x improvement)
2. ⭐ **Insertion Performance** (shows trade-offs)

### Recommended (All 4):
1. Search Performance - main finding
2. Insertion Performance - trade-offs
3. RBAR Calculation - real algorithm performance
4. Top-K Retrieval - specialized use case

### Bonus (Create This Manually):
**Summary Bar Chart** at 41,457 reviews only:
- X-axis: Operations (Search, Insert, RBAR, Top-K)
- Y-axis: Time (ms)
- 3 bars per operation (Linear, AVL, RBT)
- Shows at-a-glance who wins each category

---

## 💡 Chart Interpretation Guide

### Search Chart
**What to say:**
> "As shown in Figure 1, search time for Linear List grows linearly with data size, increasing from 0.035ms to 0.291ms (8.3x growth), validating O(N) complexity. In contrast, AVL Tree maintains sub-microsecond performance (<0.001ms) at all data sizes, confirming O(log N) complexity. This represents a **289x performance improvement** at full scale."

### Insertion Chart
**What to say:**
> "Figure 2 demonstrates the insertion performance trade-off. Linear List maintains O(1) amortized insertion (0.007ms to 0.021ms), while AVL Tree requires O(log N) time due to rebalancing operations. RBT (Splay Tree) shows the highest insertion cost due to splay operations, which optimize for subsequent read operations."

### RBAR Chart
**What to say:**
> "Figure 3 shows RBAR (Recency-Biased Average Rating) calculation performance. While all structures must traverse all reviews (O(N) complexity), RBT performs best (0.008ms) due to recent reviews already positioned at the root. AVL Tree is also significantly faster (0.018ms) than Linear List (0.343ms), representing a **43x improvement** for RBT."

### Top-K Chart
**What to say:**
> "Figure 4 illustrates Top-K retrieval performance, where RBT's recency-biased structure shines. At 41,457 reviews, RBT completes Top-K retrieval in 0.003ms compared to Linear List's 0.301ms - a **100x improvement**. This validates the design choice of moving frequently accessed (recent) nodes toward the root."

---

## 🚀 Quick Reference: File → Chart Type

| File | Recommended Chart | Why |
|------|------------------|-----|
| `1_search_comparison.csv` | Line Chart | Shows growth over time |
| `2_insertion_comparison.csv` | Column Chart | Emphasizes differences |
| `3_rbar_comparison.csv` | Line Chart | Shows scaling behavior |
| `4_topk_comparison.csv` | Line Chart | Shows scaling behavior |

---

## 📱 Screenshots for Presentations

After creating charts:
1. Click chart
2. Right-click → Save as Picture
3. Save as PNG (best quality)
4. Insert into PowerPoint/Google Slides

---

## ✅ Checklist

Before submitting your report, ensure:

- [ ] All charts have titles
- [ ] All axes are labeled (X: "Number of Reviews", Y: "Time (ms)")
- [ ] Legend is visible and clear
- [ ] Colors are distinguishable
- [ ] Font size is readable (>10pt)
- [ ] Charts are referenced in text (Figure 1, Figure 2, etc.)
- [ ] Each chart has interpretation in report text

---

## 🎓 For Academic Reports

### Figure Captions (Copy These):

**Figure 1: Search Performance Comparison**
> Search performance across three data structures with increasing dataset size. Linear List exhibits O(N) growth while AVL Tree maintains O(log N) performance, resulting in 289x improvement at 41,457 reviews.

**Figure 2: Insertion Performance Comparison**
> Insertion time comparison showing Linear List's O(1) advantage vs. tree structures requiring rebalancing (AVL) or splaying (RBT) operations.

**Figure 3: RBAR Calculation Performance**
> Recency-Biased Average Rating calculation performance. RBT's design positions recent high-weight reviews at root, providing 43x improvement over Linear List.

**Figure 4: Top-K Retrieval Performance**
> Top-K most recent review retrieval (K=50). RBT's splay-on-access strategy provides 100x improvement by maintaining recent data near root.

---

## 🎯 One-Sentence Summaries (For Presentations)

1. **Search:** "AVL Tree is 289x faster - search stays constant while Linear List grows with data size"
2. **Insertion:** "Linear List wins insertion speed, but loses overall due to slow searches"
3. **RBAR:** "RBT optimizes for recency, making weighted calculations 43x faster"
4. **Top-K:** "Getting recent reviews? RBT is 100x faster by keeping them at the root"

---

## 💻 Bonus: Command-Line Preview

Want to see the data before importing?

```bash
# View search data
cat 1_search_comparison.csv

# View insertion data
cat 2_insertion_comparison.csv

# View RBAR data
cat 3_rbar_comparison.csv

# View Top-K data
cat 4_topk_comparison.csv
```

---

## 🆘 Troubleshooting

### Chart looks wrong?
- Make sure you imported the **entire CSV** including header row
- Verify "Data Size" is on X-axis, not in legend
- Try clicking chart → "Select Data" → Swap rows/columns

### Can't see AVL/RBT lines on Search chart?
- Values are so small (0.000-0.003ms) they appear flat
- This is GOOD! Shows they don't scale with data size
- Add annotation: "AVL and RBT: <0.003ms (constant)"

### Linear List dominates the scale?
- Create two charts:
  1. All three structures (shows Linear List problem)
  2. Just AVL and RBT (shows their similar performance)

---

**Now go create those charts in 2 minutes each!** 📊✨

Total time for all 4 charts: ~10 minutes

