package com.pgm.project;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class Read_excel {
	
	public int[][] get_data_as_matrix (String filepath) {

		
		try {
			int rows = 0;
			int cols = 0;
			
			FileInputStream file = new FileInputStream(new File(filepath));
			HSSFWorkbook workbook = new HSSFWorkbook(file);
			HSSFSheet sheet = workbook.getSheetAt(0);
			
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()){
					cellIterator.next();
					
//					switch (cell.getCellType()) {
//					case Cell.CELL_TYPE_NUMERIC:
//						System.out.print(cell.getNumericCellValue() + "\t");
//					break;
//					case Cell.CELL_TYPE_STRING:
//					System.out.print(cell.getStringCellValue() + "\t");
//					break;
//					}
					cols ++;
				} 
				rows++;
			}
//			System.out.println("YOYO");
			file.close();
			
		
			// IMPORTANT -- 
			cols = cols/rows;
			
//			System.out.println(rows);
//			System.out.println(cols);
			
			int data[][] = new int[rows][cols];
			file = new FileInputStream(new File(filepath));
			workbook.close();
//			
			workbook = new HSSFWorkbook(file);
			sheet = workbook.getSheetAt(0);
			rowIterator = sheet.iterator();
			int i = 0; 
			int j = 0;
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					data[i][j] = (int) cell.getNumericCellValue();
					j++;
				}
				i++;
				j=0;
			}	
			workbook.close();
			return data;
		} 

		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;

	}


}
