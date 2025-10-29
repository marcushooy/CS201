# 🚀 Quick Start Guide

## Run Your Analysis in 3 Simple Steps

### Step 1: Compile
```bash
chmod +x compile.sh run.sh
./compile.sh
```
✅ **Already done!** (compilation successful)

### Step 2: Run Benchmarks
```bash
./run.sh
```
✅ **Creates CSV files in `results/` folder** (takes ~30 seconds)

### Step 3: Visualize (optional)
```bash
# Install Jupyter if needed (one time):
pip install jupyter pandas matplotlib seaborn

# Run notebook:
jupyter notebook CS201_Analysis.ipynb

# In Jupyter: Cell → Run All
```
✅ **Generates professional charts** (saved as PNG files)

---

## 📁 Where Are My Results?

After running `./run.sh`, find everything in **`results/`** folder:

```
results/
├── 1_search_comparison.csv          ← Import to Excel
├── 2_insertion_comparison.csv       ← Import to Excel
├── 3_rbar_comparison.csv            ← Import to Excel
├── 4_topk_comparison.csv            ← Import to Excel
├── unified_comparison.csv           ← All data combined
├── PERFORMANCE_ANALYSIS.md          ← Read this!
└── VISUAL_SUMMARY.txt               ← ASCII charts
```

---

## 💡 Don't Need Jupyter?

**Just use Excel!**
1. Open Excel
2. Import `results/1_search_comparison.csv`
3. Select all → Insert → Line Chart
4. Done! Repeat for other CSVs

---

## ✅ That's It!

- Compile → Run → Visualize
- Everything generates automatically
- All results in one place: `results/` folder

Need help? Check `README.md`

