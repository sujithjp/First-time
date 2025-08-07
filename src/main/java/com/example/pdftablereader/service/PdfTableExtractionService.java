package com.example.pdftablereader.service;

import com.example.pdftablereader.dto.PdfTableResponse;
import com.example.pdftablereader.dto.TableData;
import com.example.pdftablereader.dto.TableRow;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PdfTableExtractionService {

    private static final Pattern TABLE_ROW_PATTERN = Pattern.compile("\\s{2,}"); // Multiple spaces indicate column separation
    private static final int MIN_COLUMNS = 2; // Minimum columns to consider as a table
    private static final int MIN_ROWS = 2; // Minimum rows to consider as a table

    public PdfTableResponse extractTablesFromPdf(MultipartFile file) {
        if (file.isEmpty()) {
            return PdfTableResponse.error("File is empty");
        }

        if (!file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
            return PdfTableResponse.error("File must be a PDF");
        }

        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            List<TableData> allTables = new ArrayList<>();
            int totalPages = document.getNumberOfPages();

            for (int pageNum = 0; pageNum < totalPages; pageNum++) {
                String pageText = extractTextFromPage(document, pageNum);
                List<TableData> pageTables = extractTablesFromText(pageText, pageNum + 1, file.getOriginalFilename());
                allTables.addAll(pageTables);
            }

            return new PdfTableResponse(allTables, file.getOriginalFilename(), totalPages);

        } catch (IOException e) {
            return PdfTableResponse.error("Error processing PDF: " + e.getMessage());
        } catch (Exception e) {
            return PdfTableResponse.error("Unexpected error: " + e.getMessage());
        }
    }

    private String extractTextFromPage(PDDocument document, int pageIndex) throws IOException {
        PDFTextStripper stripper = new PDFTextStripper();
        stripper.setStartPage(pageIndex + 1);
        stripper.setEndPage(pageIndex + 1);
        return stripper.getText(document);
    }

    private List<TableData> extractTablesFromText(String text, int pageNumber, String fileName) {
        List<TableData> tables = new ArrayList<>();
        String[] lines = text.split("\n");
        
        List<String> currentTable = new ArrayList<>();
        
        for (String line : lines) {
            String trimmedLine = line.trim();
            
            // Skip empty lines
            if (trimmedLine.isEmpty()) {
                if (!currentTable.isEmpty()) {
                    processTable(currentTable, tables, pageNumber, fileName);
                    currentTable.clear();
                }
                continue;
            }
            
            // Check if line looks like a table row (has multiple columns separated by spaces)
            if (isTableRow(trimmedLine)) {
                currentTable.add(trimmedLine);
            } else {
                // Non-table line encountered, process current table if any
                if (!currentTable.isEmpty()) {
                    processTable(currentTable, tables, pageNumber, fileName);
                    currentTable.clear();
                }
            }
        }
        
        // Process any remaining table
        if (!currentTable.isEmpty()) {
            processTable(currentTable, tables, pageNumber, fileName);
        }
        
        return tables;
    }

    private boolean isTableRow(String line) {
        // Check if line has multiple columns (separated by 2+ spaces)
        String[] columns = TABLE_ROW_PATTERN.split(line);
        return columns.length >= MIN_COLUMNS;
    }

    private void processTable(List<String> tableLines, List<TableData> tables, int pageNumber, String fileName) {
        if (tableLines.size() < MIN_ROWS) {
            return; // Too few rows to be a meaningful table
        }

        // Extract headers from first row
        List<String> headers = parseRowColumns(tableLines.get(0));
        
        // Extract data rows
        List<TableRow> rows = new ArrayList<>();
        for (int i = 1; i < tableLines.size(); i++) {
            List<String> columns = parseRowColumns(tableLines.get(i));
            
            // Ensure all rows have same number of columns as headers
            while (columns.size() < headers.size()) {
                columns.add("");
            }
            while (columns.size() > headers.size()) {
                columns.remove(columns.size() - 1);
            }
            
            rows.add(new TableRow(columns, i));
        }
        
        if (!rows.isEmpty()) {
            TableData tableData = new TableData(rows, headers, pageNumber, fileName);
            tables.add(tableData);
        }
    }

    private List<String> parseRowColumns(String row) {
        List<String> columns = new ArrayList<>();
        String[] parts = TABLE_ROW_PATTERN.split(row);
        
        for (String part : parts) {
            columns.add(part.trim());
        }
        
        return columns;
    }

    /**
     * Alternative method for extracting tables with custom column separators
     */
    public PdfTableResponse extractTablesWithCustomSeparator(MultipartFile file, String separator) {
        if (file.isEmpty()) {
            return PdfTableResponse.error("File is empty");
        }

        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            List<TableData> allTables = new ArrayList<>();
            int totalPages = document.getNumberOfPages();

            for (int pageNum = 0; pageNum < totalPages; pageNum++) {
                String pageText = extractTextFromPage(document, pageNum);
                List<TableData> pageTables = extractTablesWithSeparator(pageText, separator, pageNum + 1, file.getOriginalFilename());
                allTables.addAll(pageTables);
            }

            return new PdfTableResponse(allTables, file.getOriginalFilename(), totalPages);

        } catch (IOException e) {
            return PdfTableResponse.error("Error processing PDF: " + e.getMessage());
        } catch (Exception e) {
            return PdfTableResponse.error("Unexpected error: " + e.getMessage());
        }
    }

    private List<TableData> extractTablesWithSeparator(String text, String separator, int pageNumber, String fileName) {
        List<TableData> tables = new ArrayList<>();
        String[] lines = text.split("\n");
        
        List<String> currentTable = new ArrayList<>();
        
        for (String line : lines) {
            String trimmedLine = line.trim();
            
            if (trimmedLine.isEmpty()) {
                if (!currentTable.isEmpty()) {
                    processTableWithSeparator(currentTable, separator, tables, pageNumber, fileName);
                    currentTable.clear();
                }
                continue;
            }
            
            if (trimmedLine.contains(separator)) {
                currentTable.add(trimmedLine);
            } else {
                if (!currentTable.isEmpty()) {
                    processTableWithSeparator(currentTable, separator, tables, pageNumber, fileName);
                    currentTable.clear();
                }
            }
        }
        
        if (!currentTable.isEmpty()) {
            processTableWithSeparator(currentTable, separator, tables, pageNumber, fileName);
        }
        
        return tables;
    }

    private void processTableWithSeparator(List<String> tableLines, String separator, List<TableData> tables, int pageNumber, String fileName) {
        if (tableLines.size() < MIN_ROWS) {
            return;
        }

        List<String> headers = Arrays.asList(tableLines.get(0).split(Pattern.quote(separator)));
        
        List<TableRow> rows = new ArrayList<>();
        for (int i = 1; i < tableLines.size(); i++) {
            List<String> columns = Arrays.asList(tableLines.get(i).split(Pattern.quote(separator)));
            
            // Ensure consistent column count
            List<String> normalizedColumns = new ArrayList<>(columns);
            while (normalizedColumns.size() < headers.size()) {
                normalizedColumns.add("");
            }
            while (normalizedColumns.size() > headers.size()) {
                normalizedColumns.remove(normalizedColumns.size() - 1);
            }
            
            // Trim whitespace from each column
            for (int j = 0; j < normalizedColumns.size(); j++) {
                normalizedColumns.set(j, normalizedColumns.get(j).trim());
            }
            
            rows.add(new TableRow(normalizedColumns, i));
        }
        
        if (!rows.isEmpty()) {
            // Trim headers
            List<String> trimmedHeaders = new ArrayList<>();
            for (String header : headers) {
                trimmedHeaders.add(header.trim());
            }
            
            TableData tableData = new TableData(rows, trimmedHeaders, pageNumber, fileName);
            tables.add(tableData);
        }
    }
}