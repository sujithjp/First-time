package com.example.pdftablereader.dto;

import java.util.List;

public class PdfTableResponse {
    private List<TableData> tables;
    private String fileName;
    private int totalPages;
    private int totalTables;
    private boolean success;
    private String message;

    public PdfTableResponse() {}

    public PdfTableResponse(List<TableData> tables, String fileName, int totalPages) {
        this.tables = tables;
        this.fileName = fileName;
        this.totalPages = totalPages;
        this.totalTables = tables != null ? tables.size() : 0;
        this.success = true;
        this.message = "PDF processed successfully";
    }

    public static PdfTableResponse error(String message) {
        PdfTableResponse response = new PdfTableResponse();
        response.success = false;
        response.message = message;
        return response;
    }

    public List<TableData> getTables() {
        return tables;
    }

    public void setTables(List<TableData> tables) {
        this.tables = tables;
        this.totalTables = tables != null ? tables.size() : 0;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalTables() {
        return totalTables;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "PdfTableResponse{" +
                "tables=" + tables +
                ", fileName='" + fileName + '\'' +
                ", totalPages=" + totalPages +
                ", totalTables=" + totalTables +
                ", success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}