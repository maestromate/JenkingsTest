package com.acs.vtigercrm.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelDataUtils {

	public static void main(String[] args) {
		FileInputStream fis=null;
		try {
			fis= new FileInputStream("src/test/resources/TestData/create contact data.xlsx");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Workbook wbook = null;
		try {
			wbook = new XSSFWorkbook(fis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Sheet sheet =wbook.getSheet("Sheet1");
		Row row =sheet.getRow(0);
		Cell cell =row.getCell(4);
		String celldata=cell.getStringCellValue();
		System.out.println(celldata);

	}

	
	
	
	
	
}
