#!/bin/bash

# PDF Table Reader Service - Example Usage Script

echo "PDF Table Reader Service - Example Usage"
echo "========================================"

# Set the base URL (change if running on different host/port)
BASE_URL="http://localhost:8080/api/pdf"

echo ""
echo "1. Checking if service is running..."
curl -s "$BASE_URL/health"
echo ""

echo ""
echo "2. Getting API information..."
curl -s "$BASE_URL/info" | json_pp 2>/dev/null || curl -s "$BASE_URL/info"
echo ""

echo ""
echo "3. Example commands for testing:"
echo ""

echo "# Test with automatic table detection:"
echo "curl -X POST \"$BASE_URL/extract-tables\" \\"
echo "     -F \"file=@your-sample.pdf\""
echo ""

echo "# Test with custom separator (pipe |):"
echo "curl -X POST \"$BASE_URL/extract-tables-with-separator\" \\"
echo "     -F \"file=@your-sample.pdf\" \\"
echo "     -F \"separator=|\""
echo ""

echo "# Test with custom separator (tab):"
echo "curl -X POST \"$BASE_URL/extract-tables-with-separator\" \\"
echo "     -F \"file=@your-sample.pdf\" \\"
echo "     -F \"separator=\\t\""
echo ""

echo "# Test with custom separator (comma):"
echo "curl -X POST \"$BASE_URL/extract-tables-with-separator\" \\"
echo "     -F \"file=@your-sample.pdf\" \\"
echo "     -F \"separator=,\""
echo ""

echo "4. To test with an actual PDF file:"
echo "   - Place your PDF file in the current directory"
echo "   - Replace 'your-sample.pdf' with your actual filename"
echo "   - Run one of the curl commands above"
echo ""

echo "5. Expected response format:"
echo "{"
echo "  \"tables\": [...],"
echo "  \"fileName\": \"your-file.pdf\","
echo "  \"totalPages\": 1,"
echo "  \"totalTables\": 1,"
echo "  \"success\": true,"
echo "  \"message\": \"PDF processed successfully\""
echo "}"