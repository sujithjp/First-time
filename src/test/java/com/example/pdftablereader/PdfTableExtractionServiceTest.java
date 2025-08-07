package com.example.pdftablereader;

import com.example.pdftablereader.dto.PdfTableResponse;
import com.example.pdftablereader.service.PdfTableExtractionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PdfTableExtractionServiceTest {

    private PdfTableExtractionService pdfTableExtractionService;

    @BeforeEach
    void setUp() {
        pdfTableExtractionService = new PdfTableExtractionService();
    }

    @Test
    void testExtractTablesFromPdf_EmptyFile() {
        MultipartFile emptyFile = new MockMultipartFile(
                "file", 
                "test.pdf", 
                "application/pdf", 
                new byte[0]
        );

        PdfTableResponse response = pdfTableExtractionService.extractTablesFromPdf(emptyFile);

        assertFalse(response.isSuccess());
        assertEquals("File is empty", response.getMessage());
    }

    @Test
    void testExtractTablesFromPdf_NonPdfFile() {
        MultipartFile nonPdfFile = new MockMultipartFile(
                "file", 
                "test.txt", 
                "text/plain", 
                "Some text content".getBytes()
        );

        PdfTableResponse response = pdfTableExtractionService.extractTablesFromPdf(nonPdfFile);

        assertFalse(response.isSuccess());
        assertEquals("File must be a PDF", response.getMessage());
    }

    @Test
    void testExtractTablesWithCustomSeparator_EmptyFile() {
        MultipartFile emptyFile = new MockMultipartFile(
                "file", 
                "test.pdf", 
                "application/pdf", 
                new byte[0]
        );

        PdfTableResponse response = pdfTableExtractionService.extractTablesWithCustomSeparator(emptyFile, "|");

        assertFalse(response.isSuccess());
        assertEquals("File is empty", response.getMessage());
    }

    // Note: For actual PDF testing, you would need to create or include sample PDF files
    // This is a basic test structure that can be extended with real PDF files
}