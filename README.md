# PDF Table Reader Service

A Spring Boot service for extracting tabular data from PDF files using Apache PDFBox (without using Tabula library).

## Features

- Extract tables from PDF files automatically
- Support for custom column separators
- RESTful API endpoints
- JSON response format
- Multi-page PDF support
- File upload validation

## Technology Stack

- **Spring Boot 3.2.0** - Main framework
- **Apache PDFBox 3.0.1** - PDF processing
- **Java 17** - Programming language
- **Maven** - Build tool

## API Endpoints

### 1. Extract Tables (Automatic Detection)
```
POST /api/pdf/extract-tables
Content-Type: multipart/form-data
```

**Parameters:**
- `file` (MultipartFile): PDF file to process

**Example using curl:**
```bash
curl -X POST "http://localhost:8080/api/pdf/extract-tables" \
     -F "file=@your-table.pdf"
```

### 2. Extract Tables (Custom Separator)
```
POST /api/pdf/extract-tables-with-separator
Content-Type: multipart/form-data
```

**Parameters:**
- `file` (MultipartFile): PDF file to process
- `separator` (String): Column separator (e.g., "|", ",", "\t")

**Example using curl:**
```bash
curl -X POST "http://localhost:8080/api/pdf/extract-tables-with-separator" \
     -F "file=@your-table.pdf" \
     -F "separator=|"
```

### 3. Health Check
```
GET /api/pdf/health
```

### 4. API Information
```
GET /api/pdf/info
```

## Response Format

```json
{
  "tables": [
    {
      "rows": [
        {
          "cells": ["Column1Value", "Column2Value", "Column3Value"],
          "rowNumber": 1
        }
      ],
      "headers": ["Column1", "Column2", "Column3"],
      "pageNumber": 1,
      "fileName": "example.pdf",
      "totalRows": 10,
      "totalColumns": 3
    }
  ],
  "fileName": "example.pdf",
  "totalPages": 1,
  "totalTables": 1,
  "success": true,
  "message": "PDF processed successfully"
}
```

## How to Run

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Build and Run
```bash
# Clone or create the project
cd pdf-table-reader

# Build the project
mvn clean compile

# Run the application
mvn spring-boot:run
```

The service will start on `http://localhost:8080`

### Alternative Run Methods
```bash
# Build JAR and run
mvn clean package
java -jar target/pdf-table-reader-1.0.0.jar

# Run with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## Table Detection Algorithm

The service uses two methods for table detection:

### 1. Automatic Detection
- Identifies table rows by detecting multiple columns separated by 2 or more spaces
- Automatically determines column boundaries
- Groups consecutive table-like lines
- Minimum 2 columns and 2 rows required

### 2. Custom Separator
- Uses provided separator character(s) to split columns
- More precise for PDFs with consistent separators
- Supports any string as separator

## Configuration

### File Upload Limits
Modify `application.properties`:
```properties
spring.servlet.multipart.max-file-size=100MB
spring.servlet.multipart.max-request-size=100MB
```

### Logging
```properties
logging.level.com.example.pdftablereader=DEBUG
logging.level.org.apache.pdfbox=INFO
```

## Testing the Service

### Test with Sample PDF
Create a simple PDF with tabular data or use the provided test examples.

### Using Postman
1. Create a new POST request to `http://localhost:8080/api/pdf/extract-tables`
2. Set Content-Type to `multipart/form-data`
3. Add a file parameter named `file` and select your PDF
4. Send the request

### Using JavaScript/Frontend
```javascript
const formData = new FormData();
formData.append('file', pdfFile);

fetch('http://localhost:8080/api/pdf/extract-tables', {
    method: 'POST',
    body: formData
})
.then(response => response.json())
.then(data => console.log(data));
```

## Error Handling

The service handles various error scenarios:
- Empty files
- Non-PDF files
- Corrupted PDFs
- Large files exceeding limits
- Invalid separators

All errors return appropriate HTTP status codes and descriptive messages.

## Limitations

- Works best with text-based PDFs (not scanned images)
- Table detection relies on consistent formatting
- Complex nested tables may not be detected properly
- Performance depends on PDF size and complexity

## Future Enhancements

- OCR support for scanned PDFs
- Advanced table detection algorithms
- Export to CSV/Excel formats
- Batch processing support
- Table formatting preservation

## License

This project is provided as-is for educational and development purposes.