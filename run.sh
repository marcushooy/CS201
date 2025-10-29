#!/bin/bash

# CS201 Project - Simple Run Script

echo "╔════════════════════════════════════════════════════════════════════╗"
echo "║         CS201 Performance Benchmarks - REAL DATA                   ║"
echo "╚════════════════════════════════════════════════════════════════════╝"
echo ""

# Find Java
JAVA_CMD="java"
if [ -f "/Library/Java/JavaVirtualMachines/jdk-21.jdk/Contents/Home/bin/java" ]; then
    JAVA_CMD="/Library/Java/JavaVirtualMachines/jdk-21.jdk/Contents/Home/bin/java"
elif [ -f "/usr/bin/java" ]; then
    JAVA_CMD="/usr/bin/java"
elif ! command -v java &> /dev/null; then
    echo "❌ ERROR: Java not found!"
    exit 1
fi

echo "✓ Using Java: $($JAVA_CMD -version 2>&1 | head -1)"
echo ""

# Go to source directory
cd "$(dirname "$0")/src/main/java" || exit 1

# Check if compiled
if [ ! -f "com/reviews/experiments/MasterRunner.class" ]; then
    echo "⚠️  Project not compiled yet!"
    echo ""
    echo "Running compilation..."
    cd ../../.. && ./compile.sh || exit 1
    cd src/main/java || exit 1
fi

# Run
echo "🚀 Running benchmarks..."
echo ""
$JAVA_CMD -cp . com.reviews.experiments.MasterRunner

echo ""
echo "╔════════════════════════════════════════════════════════════════════╗"
echo "║         ✅ COMPLETE!                                               ║"
echo "╚════════════════════════════════════════════════════════════════════╝"
echo ""
echo "📊 Results are in: results/ folder"
echo ""
echo "View results:"
echo "  • results/1_search_comparison.csv  (Import to Excel)"
echo "  • results/2_insertion_comparison.csv"
echo "  • results/3_rbar_comparison.csv"
echo "  • results/4_topk_comparison.csv"
echo "  • results/PERFORMANCE_ANALYSIS.md  (Read this!)"
echo ""
echo "For professional charts:"
echo "  jupyter notebook CS201_Analysis.ipynb"
echo ""
