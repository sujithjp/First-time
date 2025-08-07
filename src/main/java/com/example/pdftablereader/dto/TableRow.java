package com.example.pdftablereader.dto;

import java.util.List;

public class TableRow {
    private List<String> cells;
    private int rowNumber;

    public TableRow() {}

    public TableRow(List<String> cells, int rowNumber) {
        this.cells = cells;
        this.rowNumber = rowNumber;
    }

    public List<String> getCells() {
        return cells;
    }

    public void setCells(List<String> cells) {
        this.cells = cells;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    @Override
    public String toString() {
        return "TableRow{" +
                "cells=" + cells +
                ", rowNumber=" + rowNumber +
                '}';
    }
}