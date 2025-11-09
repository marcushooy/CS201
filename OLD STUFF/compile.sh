#!/bin/bash

# CS201 Project - Simple Compilation Script

echo "╔════════════════════════════════════════════════════════════════════╗"
echo "║         CS201 Project - Compilation                                ║"
echo "╚════════════════════════════════════════════════════════════════════╝"
echo ""

# Find Java
JAVAC_CMD="javac"
if [ -f "/Library/Java/JavaVirtualMachines/jdk-21.jdk/Contents/Home/bin/javac" ]; then
    JAVAC_CMD="/Library/Java/JavaVirtualMachines/jdk-21.jdk/Contents/Home/bin/javac"
elif ! command -v javac &> /dev/null; then
    echo "❌ ERROR: Java not found! Please install JDK."
    exit 1
fi

echo "✓ Using Java: $($JAVAC_CMD -version 2>&1)"
echo ""

# Go to source directory
cd "$(dirname "$0")/src/main/java" || exit 1
echo "📂 Working in: $(pwd)"
echo ""

# Clean old files
echo "🧹 Cleaning..."
find . -name "*.class" -delete 2>/dev/null
echo "✓ Clean complete"
echo ""

# Compile all
echo "🔨 Compiling..."
echo ""

echo "1️⃣  Models..."
$JAVAC_CMD com/reviews/Models/*.java || { echo "❌ FAILED!"; exit 1; }
echo "   ✓ Done"

echo "2️⃣  Data Structures..."
$JAVAC_CMD -cp . com/reviews/datastructures/*.java || { echo "❌ FAILED!"; exit 1; }
echo "   ✓ Done"

echo "3️⃣  Utilities..."
$JAVAC_CMD -cp . com/reviews/utils/*.java || { echo "❌ FAILED!"; exit 1; }
echo "   ✓ Done"

echo "4️⃣  Benchmark Utils..."
$JAVAC_CMD -cp . com/reviews/experiments/BenchmarkUtils.java || { echo "❌ FAILED!"; exit 1; }
echo "   ✓ Done"

echo "5️⃣  Experiments..."
$JAVAC_CMD -cp . com/reviews/experiments/experiment1/*.java || { echo "❌ FAILED!"; exit 1; }
$JAVAC_CMD -cp . com/reviews/experiments/experiment2/*.java || { echo "❌ FAILED!"; exit 1; }
$JAVAC_CMD -cp . com/reviews/experiments/experiment3/*.java || { echo "❌ FAILED!"; exit 1; }
echo "   ✓ Done"

echo "6️⃣  Analysis Tools..."
$JAVAC_CMD -cp . com/reviews/experiments/ResultsAnalyzer.java || { echo "❌ FAILED!"; exit 1; }
$JAVAC_CMD -cp . com/reviews/experiments/ChartGenerator.java || { echo "❌ FAILED!"; exit 1; }
echo "   ✓ Done"

echo "7️⃣  Runners..."
$JAVAC_CMD -cp . com/reviews/experiments/UnifiedBenchmarkRunner.java || { echo "❌ FAILED!"; exit 1; }
$JAVAC_CMD -cp . com/reviews/experiments/MasterRunner.java || { echo "❌ FAILED!"; exit 1; }
echo "   ✓ Done"

echo ""
echo "╔════════════════════════════════════════════════════════════════════╗"
echo "║         ✅ COMPILATION SUCCESSFUL!                                ║"
echo "╚════════════════════════════════════════════════════════════════════╝"
echo ""
echo "Next step: Run the project"
echo "  ./run.sh"
echo ""
