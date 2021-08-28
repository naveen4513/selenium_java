package office.sirion.util;

public class TestUtil {

	// TestUtil - Returns is Test Suite Runnable
	public static boolean isSuiteRunnable(Xls_Reader xls, String suiteName) {
		boolean isExecutable = false;
		for (int i = 2; i <= xls.getRowCount("Test Suite"); i++) {
			if (xls.getCellData("Test Suite", "TSID", i).equalsIgnoreCase(suiteName)) {
				if (xls.getCellData("Test Suite", "Runmode", i).equalsIgnoreCase("Y")) {
					isExecutable = true;
					} else {
						isExecutable = false;
						}
				}
			}
		xls = null;
		return isExecutable;
		}

	// TestUtil - Returns is Test Case Runnable
	public static boolean isTestCaseRunnable(Xls_Reader xls, String testCaseName) {
		boolean isExecutable = false;
		for (int i = 2; i <= xls.getRowCount("Test Cases"); i++) {
			if (xls.getCellData("Test Cases", "TCID", i).equalsIgnoreCase(testCaseName)) {
				if (xls.getCellData("Test Cases", "Runmode", i).equalsIgnoreCase("Y")) {
					isExecutable = true;
					} else {
						isExecutable = false;
						}
				}
			}
		return isExecutable;
		}

	// TestUtil - Returns is Test Data of Object Type
	public static Object[][] getData(Xls_Reader xls, String testCaseName) {
		if (!xls.isSheetExist(testCaseName)) {
			xls = null;
			return new Object[1][0];
			}

		int rows = xls.getRowCount(testCaseName);
		int cols = xls.getColumnCount(testCaseName);
		Object[][] data = new Object[rows - 1][cols - 4];

		for (int rowNum = 2; rowNum <= rows; rowNum++) {
			for (int colNum = 0; colNum < cols - 4; colNum++) {
				data[rowNum - 2][colNum] = xls.getCellData(testCaseName, colNum, rowNum);
				}
			}
		return data;
		}

	// TestUtil - Returns is Test Data Run mode
	public static String[] getDataSetRunmodes(Xls_Reader xlsFile, String sheetName) {
		String[] runmodes = null;
		if (!xlsFile.isSheetExist(sheetName)) {
			xlsFile = null;
			sheetName = null;
			runmodes = new String[1];
			runmodes[0] = "Y";
			xlsFile = null;
			sheetName = null;
			return runmodes;
			}
		
		runmodes = new String[xlsFile.getRowCount(sheetName) - 1];
		
		for (int i = 2; i <= runmodes.length + 1; i++) {
			runmodes[i - 2] = xlsFile.getCellData(sheetName, "Runmode", i);
			}
		
		xlsFile = null;
		sheetName = null;
		
		return runmodes;
		}

	// TestUtil - Sets Test Data Results
	public static void reportDataSetResult(Xls_Reader xls, String testCaseName, int rowNum, String result) {
		xls.setCellData(testCaseName, "Results", rowNum, result);
		}
	
	// return the row num for a test
	public static int getRowNum(Xls_Reader xls, String id){
		for(int i=2; i<= xls.getRowCount("Test Cases") ; i++){
			String tcid=xls.getCellData("Test Cases", "TCID", i);
			
			if(tcid.equals(id)){
				xls=null;
				return i;
			}			
		}
		return -1;
		}	
	}
