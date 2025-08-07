package com.example.pdftablereader.dto;

import java.util.List;

public class TableData {
    private List<TableRow> rows;
    private List<String> headers;
    private int pageNumber;
    private String fileName;
    private int totalRows;
    private int totalColumns;

    public TableData() {}

    public TableData(List<TableRow> rows, List<String> headers, int pageNumber, String fileName) {
        this.rows = rows;
        this.headers = headers;
        this.pageNumber = pageNumber;
        this.fileName = fileName;
        this.totalRows = rows != null ? rows.size() : 0;
        this.totalColumns = headers != null ? headers.size() : 0;
    }

    public List<TableRow> getRows() {
        return rows;
    }

    public void setRows(List<TableRow> rows) {
        this.rows = rows;
        this.totalRows = rows != null ? rows.size() : 0;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
        this.totalColumns = headers != null ? headers.size() : 0;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public int getTotalColumns() {
        return totalColumns;
    }

    @Override
    public String toString() {
        return "TableData{" +
                "rows=" + rows +
                ", headers=" + headers +
                ", pageNumber=" + pageNumber +
                ", fileName='" + fileName + '\'' +
                ", totalRows=" + totalRows +
                ", totalColumns=" + totalColumns +
                '}';
    }
}