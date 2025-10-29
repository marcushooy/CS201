#!/bin/bash

# CS201 Project - Compilation Script with Error Checking
# This script compiles everything step-by-step and shows exactly where errors occur

echo "╔════════════════════════════════════════════════════════════════════╗"
echo "║         CS201 Project - Step-by-Step Compilation                  ║"
echo "╚════════════════════════════════════════════════════════════════════╝"
echo ""

# Check Java
if ! command -v javac &> /dev/null; then
    echo "❌ ERROR: javac (Java compiler) not found!"
    echo "Please install Java JDK"
    exit 1
fi

echo "✓ Java compiler found: $(javac -version 2>&1)"
echo ""

# Navigate to source directory
cd "$(dirname "$0")/src/main/java" || exit 1
echo "📂 Working directory: $(pwd)"
echo ""

# Clean old files
echo "🧹 Cleaning old .class files..."
find . -name "*.class" -delete
echo "✓ Clean complete"
echo ""

# Compile step by step
echo "🔨 Compiling project components..."
echo ""

echo "1️⃣  Compiling Models..."
javac com/reviews/Models/*.java
if [ $? -ne 0 ]; then
    echo "❌ FAILED: Models compilation error!"
    exit 1
fi
echo "   ✓ Models compiled"

echo "2️⃣  Compiling Data Structures..."
javac -cp . com/reviews/datastructures/*.java
if [ $? -ne 0 ]; then
    echo "❌ FAILED: Data Structures compilation error!"
    exit 1
fi
echo "   ✓ Data Structures compiled"

echo "3️⃣  Compiling Utilities..."
javac -cp . com/reviews/utils/*.java
if [ $? -ne 0 ]; then
    echo "❌ FAILED: Utilities compilation error!"
    exit 1
fi
echo "   ✓ Utilities compiled"

echo "4️⃣  Compiling Benchmark Utils..."
javac -cp . com/reviews/experiments/BenchmarkUtils.java
if [ $? -ne 0 ]; then
    echo "❌ FAILED: BenchmarkUtils compilation error!"
    exit 1
fi
echo "   ✓ BenchmarkUtils compiled"

echo "5️⃣  Compiling Experiment 1..."
javac -cp . com/reviews/experiments/experiment1/*.java
if [ $? -ne 0 ]; then
    echo "❌ FAILED: Experiment 1 compilation error!"
    echo "Check experiment1 files for errors"
    exit 1
fi
echo "   ✓ Experiment 1 compiled"

echo "6️⃣  Compiling Experiment 2..."
javac -cp . com/reviews/experiments/experiment2/*.java
if [ $? -ne 0 ]; then
    echo "❌ FAILED: Experiment 2 compilation error!"
    echo "Check experiment2 files for errors"
    exit 1
fi
echo "   ✓ Experiment 2 compiled"

echo "7️⃣  Compiling Experiment 3..."
javac -cp . com/reviews/experiments/experiment3/*.java
if [ $? -ne 0 ]; then
    echo "❌ FAILED: Experiment 3 compilation error!"
    echo "Check experiment3 files for errors"
    exit 1
fi
echo "   ✓ Experiment 3 compiled"

echo "8️⃣  Compiling Unified Benchmark Runner..."
javac -cp . com/reviews/experiments/UnifiedBenchmarkRunner.java
if [ $? -ne 0 ]; then
    echo "❌ FAILED: UnifiedBenchmarkRunner compilation error!"
    exit 1
fi
echo "   ✓ UnifiedBenchmarkRunner compiled"

echo "9️⃣  Compiling Results Analyzer..."
javac -cp . com/reviews/experiments/ResultsAnalyzer.java
if [ $? -ne 0 ]; then
    echo "❌ FAILED: ResultsAnalyzer compilation error!"
    exit 1
fi
echo "   ✓ ResultsAnalyzer compiled"

echo "🔟 Compiling Chart Generator..."
javac -cp . com/reviews/experiments/ChartGenerator.java
if [ $? -ne 0 ]; then
    echo "❌ FAILED: ChartGenerator compilation error!"
    exit 1
fi
echo "   ✓ ChartGenerator compiled"

echo "1️⃣1️⃣   Compiling Master Runner..."
javac -cp . com/reviews/experiments/MasterRunner.java
if [ $? -ne 0 ]; then
    echo "❌ FAILED: MasterRunner compilation error!"
    exit 1
fi
echo "   ✓ MasterRunner compiled"

echo "1️⃣2️⃣  Compiling Simple Master Runner..."
javac -cp . com/reviews/experiments/SimpleMasterRunner.java
if [ $? -ne 0 ]; then
    echo "❌ FAILED: SimpleMasterRunner compilation error!"
    exit 1
fi
echo "   ✓ SimpleMasterRunner compiled"

echo ""
echo "╔════════════════════════════════════════════════════════════════════╗"
echo "║              ✅ COMPILATION SUCCESSFUL!                            ║"
echo "╚════════════════════════════════════════════════════════════════════╝"
echo ""
echo "You can now run:"
echo "  • java com.reviews.experiments.MasterRunner  ⭐ (All experiments + analysis)"
echo "  • java com.reviews.experiments.UnifiedBenchmarkRunner  (Benchmarks only)"
echo "  • java com.reviews.experiments.ResultsAnalyzer  (Generate analysis reports)"
echo "  • java com.reviews.experiments.SimpleMasterRunner  (Diagnostic test)"
echo ""
echo "📊 ALL benchmarks use REAL CSV data (41,457 airline reviews)!"
echo "📁 Results will be saved to the results/ folder with analysis!"
echo ""

