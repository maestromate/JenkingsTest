package com.acs.vtigercrm.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Ex {

	
	/**
     * Method to get cell data from Excel dynamically
     * @param filePath   : Path of the Excel file
     * @param sheetName  : Sheet name in Excel
     * @param rowIndex   : Row index (0-based)
     * @param cellIndex  : Cell index (0-based)
     * @return           : Cell value as String
     */
    public static String getCellData(String filePath, String sheetName, int rowIndex, int cellIndex) {
        String cellData = "";             // 1. Initialize empty string to store cell value
        FileInputStream fis = null;       // 2. Initialize FileInputStream
        Workbook workbook = null;         // 3. Initialize Workbook object

        try {
            fis = new FileInputStream(filePath);      // 4. Open Excel file
            workbook = new XSSFWorkbook(fis);         // 5. Load workbook

            Sheet sheet = workbook.getSheet(sheetName);   // 6. Get sheet by name
            if(sheet != null) {                           // 7. Check sheet exists
                Row row = sheet.getRow(rowIndex);        // 8. Get row by index
                if(row != null) {                        // 9. Check row exists
                    Cell cell = row.getCell(cellIndex);  // 10. Get cell by index
                    if(cell != null) {                   // 11. Check cell exists
                        cellData = cell.getStringCellValue();  // 12. Read cell value
                    }
                }
            }

        } catch (FileNotFoundException e) {    // 13. Handle if file not found
            e.printStackTrace();
        } catch (IOException e) {              // 14. Handle input/output exceptions
            e.printStackTrace();
        } finally {
            // 15. Close resources safely
            try {
                if(workbook != null) workbook.close();  // Close workbook if it exists
                if(fis != null) fis.close();            // Close file stream if it exists
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return cellData;   // 16. Return the value read from Excel
    }

    public static void main(String[] args) {
        // Example usage:
        String filePath = "src/test/resources/TestData/create contact data.xlsx";
        String sheetName = "Sheet1";
        int rowIndex = 0;
        int cellIndex = 4;

        String data = getCellData(filePath, sheetName, rowIndex, cellIndex);
        System.out.println("Cell Data: " + data);
    }
}

