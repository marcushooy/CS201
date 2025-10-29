#!/bin/bash

# CS201 Project - One-Click Runner
# This script compiles and runs the entire project
# Uses REAL CSV data (41,457 airline reviews from Skytrax)

echo "╔════════════════════════════════════════════════════════════════════╗"
echo "║         CS201 Project - Automated Build & Run Script              ║"
echo "║         Using REAL CSV Data (41,457 Airline Reviews)              ║"
echo "╚════════════════════════════════════════════════════════════════════╝"
echo ""

# Check if Java is installed
if ! command -v java &> /dev/null
then
    echo "❌ ERROR: Java is not installed!"
    echo ""
    echo "Please install Java first:"
    echo "  Option 1: brew install openjdk@17"
    echo "  Option 2: https://www.oracle.com/java/technologies/downloads/"
    echo ""
    exit 1
fi

if ! command -v javac &> /dev/null
then
    echo "❌ ERROR: Java compiler (javac) is not installed!"
    echo ""
    echo "Please install Java JDK (not just JRE)"
    echo ""
    exit 1
fi

echo "✓ Java detected: $(java -version 2>&1 | head -n 1)"
echo ""

# Navigate to the src/main/java directory
cd "$(dirname "$0")/src/main/java" || exit 1

echo "📂 Current directory: $(pwd)"
echo ""

# Clean old class files
echo "🧹 Cleaning old compiled files..."
find . -name "*.class" -type f -delete
echo "✓ Clean complete"
echo ""

# Compile all Java files
echo "🔨 Compiling all Java files..."
echo "   - Models..."
javac com/reviews/Models/*.java
if [ $? -ne 0 ]; then
    echo "❌ Compilation failed for Models!"
    exit 1
fi

echo "   - Data Structures..."
javac -cp . com/reviews/datastructures/*.java
if [ $? -ne 0 ]; then
    echo "❌ Compilation failed for Data Structures!"
    exit 1
fi

echo "   - Experiment 1..."
javac -cp . com/reviews/experiments/experiment1/*.java
if [ $? -ne 0 ]; then
    echo "❌ Compilation failed for Experiment 1!"
    exit 1
fi

echo "   - Experiment 2..."
javac -cp . com/reviews/experiments/experiment2/*.java
if [ $? -ne 0 ]; then
    echo "❌ Compilation failed for Experiment 2!"
    exit 1
fi

echo "   - Experiment 3..."
javac -cp . com/reviews/experiments/experiment3/*.java
if [ $? -ne 0 ]; then
    echo "❌ Compilation failed for Experiment 3!"
    exit 1
fi

echo "   - Benchmark Utilities..."
javac -cp . com/reviews/experiments/BenchmarkUtils.java
if [ $? -ne 0 ]; then
    echo "❌ Compilation failed for BenchmarkUtils!"
    exit 1
fi

echo "   - Utilities..."
javac -cp . com/reviews/utils/*.java
if [ $? -ne 0 ]; then
    echo "❌ Compilation failed for Utilities!"
    exit 1
fi

echo "   - Unified Benchmark Runner..."
javac -cp . com/reviews/experiments/UnifiedBenchmarkRunner.java
if [ $? -ne 0 ]; then
    echo "❌ Compilation failed for UnifiedBenchmarkRunner!"
    exit 1
fi

echo "   - Results Analyzer..."
javac -cp . com/reviews/experiments/ResultsAnalyzer.java
if [ $? -ne 0 ]; then
    echo "❌ Compilation failed for ResultsAnalyzer!"
    exit 1
fi

echo "   - Master Runner..."
javac -cp . com/reviews/experiments/MasterRunner.java
if [ $? -ne 0 ]; then
    echo "❌ Compilation failed for MasterRunner!"
    exit 1
fi

echo "✓ Compilation successful!"
echo ""

# Run the master runner
echo "🚀 Running CS201 Project..."
echo ""
echo "════════════════════════════════════════════════════════════════════"
echo ""

java -cp . com.reviews.experiments.MasterRunner

echo ""
echo "════════════════════════════════════════════════════════════════════"
echo ""
echo "╔════════════════════════════════════════════════════════════════════╗"
echo "║                    ✅ EXECUTION COMPLETE!                          ║"
echo "╚════════════════════════════════════════════════════════════════════╝"
echo ""
echo "📊 All tests used REAL CSV data (41,457 airline reviews)!"
echo ""
echo "📁 Main Results Folder (Check here first!):"
echo "  ⭐ results/SUMMARY.txt              - Quick reference"
echo "  ⭐ results/ANALYSIS_REPORT.md       - Detailed analysis"
echo "  ⭐ results/unified_comparison.csv   - All experiments combined"
echo "  • results/experiment1_linear_list.csv"
echo "  • results/experiment2_avl_tree.csv"
echo "  • results/experiment3_rbt.csv"
echo ""
echo "📂 Individual experiment folders (backup copies):"
echo "  • src/main/java/com/reviews/experiments/experiment1/results.csv"
echo "  • src/main/java/com/reviews/experiments/experiment2/results.csv"
echo "  • src/main/java/com/reviews/experiments/experiment3/results.csv"
echo ""

