package com.example.pdftablereader.controller;

import com.example.pdftablereader.dto.PdfTableResponse;
import com.example.pdftablereader.service.PdfTableExtractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/pdf")
@CrossOrigin(origins = "*") // Allow CORS for frontend integration
public class PdfTableController {

    private final PdfTableExtractionService pdfTableExtractionService;

    @Autowired
    public PdfTableController(PdfTableExtractionService pdfTableExtractionService) {
        this.pdfTableExtractionService = pdfTableExtractionService;
    }

    /**
     * Extract tables from PDF using automatic detection (spaces as separators)
     */
    @PostMapping(value = "/extract-tables", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PdfTableResponse> extractTables(@RequestParam("file") MultipartFile file) {
        try {
            if (file == null) {
                return ResponseEntity.badRequest()
                        .body(PdfTableResponse.error("No file provided"));
            }

            PdfTableResponse response = pdfTableExtractionService.extractTablesFromPdf(file);
            
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(PdfTableResponse.error("Internal server error: " + e.getMessage()));
        }
    }

    /**
     * Extract tables from PDF using custom column separator
     */
    @PostMapping(value = "/extract-tables-with-separator", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PdfTableResponse> extractTablesWithSeparator(
            @RequestParam("file") MultipartFile file,
            @RequestParam("separator") String separator) {
        try {
            if (file == null) {
                return ResponseEntity.badRequest()
                        .body(PdfTableResponse.error("No file provided"));
            }

            if (separator == null || separator.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(PdfTableResponse.error("Separator cannot be empty"));
            }

            PdfTableResponse response = pdfTableExtractionService.extractTablesWithCustomSeparator(file, separator);
            
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(PdfTableResponse.error("Internal server error: " + e.getMessage()));
        }
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("PDF Table Reader Service is running");
    }

    /**
     * Get API information
     */
    @GetMapping("/info")
    public ResponseEntity<ApiInfo> getApiInfo() {
        ApiInfo info = new ApiInfo();
        info.setServiceName("PDF Table Reader");
        info.setVersion("1.0.0");
        info.setDescription("Spring Boot service for extracting tabular data from PDF files using Apache PDFBox");
        info.addEndpoint("POST /api/pdf/extract-tables", "Extract tables using automatic detection");
        info.addEndpoint("POST /api/pdf/extract-tables-with-separator", "Extract tables using custom separator");
        info.addEndpoint("GET /api/pdf/health", "Health check");
        info.addEndpoint("GET /api/pdf/info", "API information");
        
        return ResponseEntity.ok(info);
    }

    // Inner class for API information
    public static class ApiInfo {
        private String serviceName;
        private String version;
        private String description;
        private java.util.List<String> endpoints = new java.util.ArrayList<>();

        public String getServiceName() { return serviceName; }
        public void setServiceName(String serviceName) { this.serviceName = serviceName; }

        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public java.util.List<String> getEndpoints() { return endpoints; }
        public void setEndpoints(java.util.List<String> endpoints) { this.endpoints = endpoints; }

        public void addEndpoint(String method, String description) {
            this.endpoints.add(method + " - " + description);
        }
    }
}