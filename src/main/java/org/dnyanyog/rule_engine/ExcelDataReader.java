package org.dnyanyog.rule_engine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ExcelDataReader {
	
    private static final Logger logger = LoggerFactory.getLogger(ExcelDataReader.class);
    
    enum RuleHeader {
    	RULE_ID(0), 
    	RULE_NAME(1), 
    	REQUEST_FORMAT(2), 
    	REQUEST_TYPE(3), 
    	END_POINT(4), 
    	RULE_ON_REQ_HEADER(5),
    	RULE_ON_REQ_BODY(6), 
    	RULE_ON_REQ_PARAM(7), 
    	EXPECTED_RESPONSE(8),
    	EXPECTED_RESPONSE_HEADER(9);

    	public final Integer columnId;

    	private RuleHeader(Integer columnId) {
    		this.columnId = columnId;
    	}
    }

	private static HSSFSheet ExcelWSheet;
	private static HSSFWorkbook ExcelWBook;
	private static HSSFCell Cell;

	/*
	 * private static XSSFSheet ExcelWSheet; private static XSSFWorkbook ExcelWBook;
	 * private static XSSFCell Cell;
	 */

	public static void getExcelTableArray(List<Rule> rules) throws Exception {
		String filePath = "/Users/vzodge/Documents/ECLIPSE_WORKSPACES/api-mocking-tool/src/main/resources/Rules/MockingRules.xls";
		String sheetName = "APIMockRules";
		String[][] tabArray = null;
		try {
			logger.info("*************** getExcelTableArray - File path - " + filePath);
			FileInputStream ExcelFile = new FileInputStream(filePath);
			// Access the required test data sheet
			ExcelWBook = new HSSFWorkbook(ExcelFile);
			ExcelWSheet = ExcelWBook.getSheet(sheetName);
			int totalRows = getUsedRows() + 1;
			int totalCols = getUsedColumns();
			tabArray = new String[totalRows][totalCols];
			for (int intRowCounter = 1; intRowCounter < totalRows; intRowCounter++) {

				rules.add(new Rule(getCellData(intRowCounter, RuleHeader.RULE_ID.columnId),
						getCellData(intRowCounter, RuleHeader.RULE_NAME.columnId),
						getCellData(intRowCounter, RuleHeader.REQUEST_FORMAT.columnId),
						getCellData(intRowCounter, RuleHeader.REQUEST_TYPE.columnId),
						getCellData(intRowCounter, RuleHeader.END_POINT.columnId),
						getCellData(intRowCounter, RuleHeader.RULE_ON_REQ_BODY.columnId),
						getCellData(intRowCounter, RuleHeader.EXPECTED_RESPONSE.columnId),
						getCellData(intRowCounter, RuleHeader.EXPECTED_RESPONSE_HEADER.columnId),
						getCellData(intRowCounter, RuleHeader.RULE_ON_REQ_HEADER.columnId),
						getCellData(intRowCounter, RuleHeader.RULE_ON_REQ_PARAM.columnId)
						));


			}
		} catch (FileNotFoundException e) {
			logger.error(
					"!!!!!!!!!!!!!!! Excel File with class name not found, probably you have mentioned excel data provider to TestMethod but excel file not provided or incorrectly provided");
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("Could not read the Excel sheet");
			e.printStackTrace();
		}
	}

	public static int getUsedRows() throws Exception {
		try {
			int RowCount = ExcelWSheet.getLastRowNum();
			return RowCount;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw (e);
		}
	}

	public static int getUsedColumns() throws Exception {
		try {
			int ColCount = ExcelWSheet.getRow(0).getLastCellNum();
			return ColCount;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw (e);
		}
	}

	public static String getCellData(int RowNum, int ColNum) throws Exception {
		try {
			Cell = ExcelWSheet.getRow(RowNum).getCell(ColNum);
			String CellData = Cell.getStringCellValue();
			return CellData;
		} catch (Exception e) {
			return "";
		}
	}

	public static void setExcelFile(String Path, String sheetName) throws Exception {

		try {
			FileInputStream ExcelFile = new FileInputStream(Path);
			ExcelWBook = new HSSFWorkbook(ExcelFile);
			ExcelWSheet = ExcelWBook.getSheet(sheetName);
		} catch (Exception e) {
			throw (e);
		}
	}

}
