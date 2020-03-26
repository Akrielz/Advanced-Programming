package com.company;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApachePOIExcelWrite {

    public static void createExcel(String fileName, Catalog catalog) {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(fileName);

        int rowNum = 0;
        System.out.println("Creating excel");

        Row row0 = sheet.createRow(rowNum++);
        int colNum = 0;
        String[] description = {"path", "url", "name", "tags", "id"};
        for (String column : description){
            Cell cell = row0.createCell(colNum++);
            cell.setCellValue(column);
        }

        for (Document document : catalog.getDocuments()){
            Row row = sheet.createRow(rowNum++);
            List<String> desc = new ArrayList<>();
            desc.add(document.getPath());
            desc.add(document.getUrl());
            desc.add(document.getName());
            desc.add(document.getTagsToString());
            desc.add(String.valueOf(document.getId()));

            colNum = 0;
            for (String column : desc){
                Cell cell = row.createCell(colNum++);
                cell.setCellValue(column);
            }
        }

        try {
            FileOutputStream outputStream = new FileOutputStream(fileName);
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
