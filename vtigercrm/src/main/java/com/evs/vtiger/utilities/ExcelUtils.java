package com.evs.vtiger.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
	  private static Workbook workbook;
      private static Sheet sheet;

	/**
     * Method to get cell data from Excel dynamically
     * @param filePath   : Path of the Excel file
     * @param sheetName  : Sheet name in Excel
     * @param rowIndex   : Row index (0-based)
     * @param cellIndex  : Cell index (0-based)
     * @return           : Cell value as String
     */
      
        public static void loadExcel(String filePath, String sheetName) {
            try (FileInputStream fis = new FileInputStream(filePath)) {
                workbook = new XSSFWorkbook(fis);
                sheet = workbook.getSheet(sheetName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static String getCellData(int rowIndex, int cellIndex) {
            try {
                Row row = sheet.getRow(rowIndex);
                if (row != null) {
                    Cell cell = row.getCell(cellIndex);
                    
					/*
                    DataFormatter formatter = new DataFormatter();
                    cellData = formatter.formatCellValue(cell);

					 * This ensures:
					 * Numeric cells → "123"
					 * Boolean cells → "TRUE"
					 * Formula cells → evaluated string result
					 */
 
                    if (cell != null) {
                        return cell.getStringCellValue();
                        
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        public static void closeExcel() {
            try {
                if (workbook != null)
                    workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

}

