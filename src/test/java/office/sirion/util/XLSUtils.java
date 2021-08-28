package office.sirion.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.mail.MessagingException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import static java.lang.Math.abs;
import static office.sirion.base.TestBase.CONFIG;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Created by Naveen Kumar Gupta on 06/07/2018.
 */

public class XLSUtils {
	private final static Logger logger = LoggerFactory.getLogger(XLSUtils.class);
	public String filePath;
	public String fileName;
	private List<String> sheetNames;
	private Workbook workBook = null;
	private Sheet sheet = null;
	private Row row = null;
	private String xlsDateFormate;

	// Xls_Reader Constructor
	public XLSUtils(String filePath, String fileName) throws IOException {
		logger.info("Internal Method Name is :---->{}", Thread.currentThread().getStackTrace()[1].getMethodName());
		this.filePath = filePath;
		this.fileName = fileName;

		//Create an object of File class to open xlsx file
		File file = new File(filePath + "//" + fileName);
		//Create an object of FileInputStream class to read excel file
		FileInputStream inputStream = new FileInputStream(file);
		//Find the file extension by splitting  file name in substring and getting only extension name
		String fileExtensionName = fileName.substring(fileName.indexOf("."));
		//Check condition if the file is xlsx file
		if (fileExtensionName.equals(".xlsx")) {
			workBook = new XSSFWorkbook(inputStream);
		}//Check condition if the file is xlsm file
		else if (fileExtensionName.equals(".xlsm")) {
			workBook = new XSSFWorkbook(inputStream);
		}//Check condition if the file is xls file
		else if (fileExtensionName.equals(".xls")) {
			workBook = new HSSFWorkbook(inputStream);
		}
		sheetNames = readAllSheetsName(workBook);
	}

	public static void convertCSVToXLSX(String csvFilePath, String csvFileName, String excelFilePath, String excelFileName, String csvFileDelimiter) {
		try {
			XSSFWorkbook workBook = new XSSFWorkbook();
			XSSFSheet sheet = workBook.createSheet("sheet1");
			String currentLine;
			int RowNum = -1;
			BufferedReader br = new BufferedReader(new FileReader(csvFilePath + "/" + csvFileName));
			while ((currentLine = br.readLine()) != null) {
				String str[] = currentLine.split(Pattern.quote(csvFileDelimiter));
				RowNum++;
				XSSFRow currentRow = sheet.createRow(RowNum);
				for (int i = 0; i < str.length; i++) {
					currentRow.createCell(i).setCellValue(str[i]);
				}
			}
			br.close();

			if (excelFileName.contains("."))
				excelFileName = excelFileName.substring(0, excelFileName.lastIndexOf("."));
			FileOutputStream fileOutputStream = new FileOutputStream(excelFilePath + "/" + excelFileName + ".xlsx");
			workBook.write(fileOutputStream);
			fileOutputStream.close();
		} catch (Exception ex) {
			logger.error("Exception while Converting CSV File {} to Excel File. {}", csvFileName, ex.getStackTrace());
		}
	}

	public static List<List<String>> getAllExcelData(String filePath, String fileName, String sheetName) {
		List<List<String>> allData = new ArrayList<>();
		List<String> oneRowData;
		try {
			FileInputStream file = new FileInputStream(new File(filePath + "//" + fileName));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheet(sheetName);
			if (sheet == null) {
				logger.warn("Couldn't locate Sheet {} in File {}.", sheetName, fileName);
				return allData;
			}

			Iterator<Row> rowIterator = sheet.iterator();
			Row row = rowIterator.next();
			int noOfColumns = row.getPhysicalNumberOfCells();

			while (rowIterator.hasNext()) {
				oneRowData = new ArrayList<>();
				row = rowIterator.next();

				for (int i = 0; i < noOfColumns; i++) {
					Cell cell = row.getCell(i);
					if (cell == null) {
						oneRowData.add(null);
						continue;
					}

					if (cell.getCellTypeEnum() == CellType.NUMERIC)
						oneRowData.add(Double.toString(cell.getNumericCellValue()));
					else if (cell.getCellTypeEnum() == CellType.STRING)
						oneRowData.add(cell.getStringCellValue());
					else if (cell.getCellTypeEnum() == CellType.BOOLEAN)
						oneRowData.add(Boolean.toString(cell.getBooleanCellValue()));
					else if (cell.getCellTypeEnum() == CellType.BLANK)
						oneRowData.add("");
				}
				allData.add(oneRowData);
			}
			file.close();
		} catch (Exception e) {
			logger.error("Exception while getting All Excel Data from File {} and Sheet {}. {}", fileName, sheetName, e.getStackTrace());
		}
		return allData;
	}

	//Returns the data of Any One Row. Row No is
	public static List<String> getExcelDataOfOneRow(String filePath, String fileName, String sheetName, int rowNo) {
		List<String> oneRowData = new ArrayList<>();
		try {
			FileInputStream file = new FileInputStream(new File(filePath + "//" + fileName));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheet(sheetName);
			if (sheet == null) {
				logger.warn("Couldn't locate Sheet {} in File {}", sheetName, fileName);
				return oneRowData;
			}
			if (sheet.getPhysicalNumberOfRows() < rowNo) {
				logger.warn("Row No. {} doesn't exist.", rowNo);
				return oneRowData;
			}
			if (rowNo < 1) {
				logger.warn("Row No. can't be less than 1.");
				return oneRowData;
			}
			Iterator<Row> rowIterator = sheet.iterator();
			Row row = null;
			for (int i = 0; i < rowNo; i++)
				row = rowIterator.next();

			int noOfColumns = row.getPhysicalNumberOfCells();
			for (int j = 0; j < noOfColumns; j++) {
				Cell cell = row.getCell(j);
				if (cell == null) {
					oneRowData.add(null);
					continue;
				}

				if (cell.getCellTypeEnum() == CellType.NUMERIC)
					oneRowData.add(Double.toString(cell.getNumericCellValue()));
				else if (cell.getCellTypeEnum() == CellType.STRING)
					oneRowData.add(cell.getStringCellValue().trim());
				else if (cell.getCellTypeEnum() == CellType.BOOLEAN)
					oneRowData.add(Boolean.toString(cell.getBooleanCellValue()));
				else if (cell.getCellTypeEnum() == CellType.BLANK)
					oneRowData.add("");
			}
			file.close();
		} catch (Exception e) {
			logger.error("Exception while getting Data of Row {} from File {} and Sheet {}. {}", rowNo, fileName, sheetName, e.getStackTrace());
		}
		return oneRowData;
	}

    //Returns the data of Any One Row. Row No is
    public static List<String> getExcelDataOfAnyRow(String filePath, String fileName, String sheetName, int rowNo,int noOfColumns) {
        List<String> oneRowData = new ArrayList<>();
        try {
            FileInputStream file = new FileInputStream(new File(filePath + "//" + fileName));
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                logger.warn("Couldn't locate Sheet {} in File {}", sheetName, fileName);
                return oneRowData;
            }

             Row row = sheet.getRow(rowNo);
            for (int j = 0; j <= noOfColumns; j++) {
                Cell cell = row.getCell(j);
                if (cell == null) {
                    oneRowData.add(null);
                    continue;
                }

                if (cell.getCellTypeEnum() == CellType.NUMERIC)
                    oneRowData.add(Double.toString(cell.getNumericCellValue()));
                else if (cell.getCellTypeEnum() == CellType.STRING)
                    oneRowData.add(cell.getStringCellValue().trim());
                else if (cell.getCellTypeEnum() == CellType.BOOLEAN)
                    oneRowData.add(Boolean.toString(cell.getBooleanCellValue()));
                else if (cell.getCellTypeEnum() == CellType.BLANK)
                    oneRowData.add("");
            }
            file.close();
        } catch (Exception e) {
            logger.error("Exception while getting Data of Row {} from File {} and Sheet {}. {}", rowNo, fileName, sheetName, e.getStackTrace());
        }
        return oneRowData;
    }



    public static List<String> getHeaders(String filePath, String fileName, String sheetName) {
		return getExcelDataOfOneRow(filePath, fileName, sheetName, 1);
	}

	public static synchronized List<String> getOneColumnDataFromMultipleRows(String excelFilePath, String excelFileName, String sheetName, int columnNo, int startingRowNo) {
		List<String> columnData = new ArrayList<>();
		try {
			FileInputStream file = new FileInputStream(new File(excelFilePath + "/" + excelFileName));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheet(sheetName);
			if (sheet == null) {
				logger.info("Couldn't locate Sheet {} in File {}.", sheetName, excelFileName);
				return columnData;
			}

			Iterator<Row> rowIterator = sheet.iterator();
			int rowNo = 0;
			while (rowNo < startingRowNo && rowIterator.hasNext()) {
				rowIterator.next();
				rowNo++;
			}

			int i = 0;
			Row row;
			while (rowIterator.hasNext()) {
				row = rowIterator.next();
				columnData.add(row.getCell(columnNo).toString());
				i++;
			}

			file.close();
		} catch (Exception e) {
			logger.error("Exception while Getting Data for Column No {} in Sheet {} at File location [{}]. {}", columnNo, sheetName, excelFilePath + "/" + excelFileName,
					e.getStackTrace());
		}
		return columnData;
	}

    public static synchronized Map<Integer, Integer> getColumnDataFromFailuresRows(String excelFilePath, String excelFileName, String sheetName, int columnNo, int startingRowNo) {
        Map<Integer,Integer> columnData = new HashMap<>();
        try {
            FileInputStream file = new FileInputStream(new File(excelFilePath + "/" + excelFileName));
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                logger.info("Couldn't locate Sheet {} in File {}.", sheetName, excelFileName);
                return columnData;
            }

            Iterator<Row> rowIterator = sheet.iterator();
            int rowNo = 0;
            while (rowNo < startingRowNo && rowIterator.hasNext()) {
                rowIterator.next();
                rowNo++;
            }

            int i = 0;
            Row row;
            while (rowIterator.hasNext()) {
                row = rowIterator.next();

                if (row.getCell(columnNo).getStringCellValue().contains("Failure")) {
                    columnData.put(row.getCell(columnNo).getRowIndex(), row.getCell(columnNo).getColumnIndex());
                    }
                }

            file.close();
        } catch (Exception e) {
            logger.error("Exception while Getting Data for Column No {} in Sheet {} at File location [{}]. {}", columnNo, sheetName, excelFilePath + "/" + excelFileName,
                    e.getStackTrace());
        }
        return columnData;
    }

    public static synchronized Map<String, Integer> getColumnDataFromSuccessRows(String excelFilePath, String excelFileName, String sheetName, int columnNo, int startingRowNo) {
        Map<String, Integer> columnData = new HashMap<>();
        try {
            FileInputStream file = new FileInputStream(new File(excelFilePath + "/" + excelFileName));
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                logger.info("Couldn't locate Sheet {} in File {}.", sheetName, excelFileName);
                return columnData;
            }

            Iterator<Row> rowIterator = sheet.iterator();
            int rowNo = 0;
            while (rowNo < startingRowNo && rowIterator.hasNext()) {
                rowIterator.next();
                rowNo++;
            }

            int i = 0;
            Row row;
            while (rowIterator.hasNext()) {
                row = rowIterator.next();

                if (row.getCell(columnNo).getStringCellValue().contains("Success")) {
                    String [] entityID = row.getCell(columnNo).getStringCellValue().split(" :");

                    columnData.put(entityID[1],row.getCell(columnNo).getRowIndex());
                }
            }

            file.close();
        } catch (Exception e) {
            logger.error("Exception while Getting Data for Column No {} in Sheet {} at File location [{}]. {}", columnNo, sheetName, excelFilePath + "/" + excelFileName,
                    e.getStackTrace());
        }
        return columnData;
    }

	public static List<List<String>> getExcelDataOfMultipleRows(String filePath, String fileName, String sheetName, int startingRowNo, int noOfRows) {
		List<List<String>> allData = new ArrayList<>();
		List<String> oneRowData;
		try {
			FileInputStream file = new FileInputStream(new File(filePath + "//" + fileName));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheet(sheetName);
			if (sheet == null) {
				logger.warn("Couldn't locate Sheet {} in File {}.", sheetName, fileName);
				return allData;
			}

			Iterator<Row> rowIterator = sheet.iterator();
			Row row = rowIterator.next();
			int noOfColumns = row.getPhysicalNumberOfCells();

			int rowNo = 0;
			while (rowNo < startingRowNo - 1) {
				if (rowIterator.hasNext()) {
					rowIterator.next();
					rowNo++;
				} else {
					logger.error("The Excel Sheet {} has lesser no of rows than Starting Row No. {}. Hence couldn't get Excel Data", sheetName, startingRowNo);
					return allData;
				}
			}

			rowNo = 0;
			while (rowIterator.hasNext() && rowNo < noOfRows) {
				rowNo++;
				oneRowData = new ArrayList<>();
				row = rowIterator.next();

				for (int i = 0; i < noOfColumns; i++) {
					Cell cell = row.getCell(i);
					if (cell == null) {
						oneRowData.add(null);
						continue;
					}

					if (cell.getCellTypeEnum() == CellType.NUMERIC)
						oneRowData.add(Double.toString(cell.getNumericCellValue()));
					else if (cell.getCellTypeEnum() == CellType.STRING)
						oneRowData.add(cell.getStringCellValue());
					else if (cell.getCellTypeEnum() == CellType.BOOLEAN)
						oneRowData.add(Boolean.toString(cell.getBooleanCellValue()));
					else if (cell.getCellTypeEnum() == CellType.BLANK)
						oneRowData.add("");
				}
				allData.add(oneRowData);
			}
			file.close();
		} catch (Exception e) {
			logger.error("Exception while getting Excel Data of Multiple Rows (starting Row: {} and No of Rows: {}) from File {} and Sheet {}. {}", startingRowNo, noOfRows,
					fileName, sheetName, e.getStackTrace());
		}
		return allData;
	}


    //Returns the data of Any One Row. Row No is
    public static List<String> getExcelDataOfSpecificColumnOneROW(String filePath, String fileName, String sheetName, int rowNo, int StartColNo, int LastColNo) {
        List<String> oneRowData = new ArrayList<>();
        try {
            FileInputStream file = new FileInputStream(new File(filePath + "//" + fileName));
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                logger.warn("Couldn't locate Sheet {} in File {}", sheetName, fileName);
                return oneRowData;
            }
            if (sheet.getPhysicalNumberOfRows() < rowNo) {
                logger.warn("Row No. {} doesn't exist.", rowNo);
                return oneRowData;
            }
            if (rowNo < 1) {
                logger.warn("Row No. can't be less than 1.");
                return oneRowData;
            }
            Iterator<Row> rowIterator = sheet.iterator();
            Row row = null;
            for (int i = 0; i < rowNo; i++)
                row = rowIterator.next();

           // int noOfColumns = row.getFirstCellNum();
            for (int j = StartColNo; j <= LastColNo; j++) {
                Cell cell = row.getCell(j);
                if (cell == null) {
                    oneRowData.add(null);
                    continue;
                }


                if (cell.getCellTypeEnum() == CellType.NUMERIC)
                    oneRowData.add(Double.toString(cell.getNumericCellValue()));
                else if (cell.getCellTypeEnum() == CellType.STRING)
                    oneRowData.add(cell.getStringCellValue().trim());
                else if (cell.getCellTypeEnum() == CellType.BOOLEAN)
                    oneRowData.add(Boolean.toString(cell.getBooleanCellValue()));
                else if (cell.getCellTypeEnum() == CellType.BLANK)
                    oneRowData.add("");
            }
            file.close();
        } catch (Exception e) {
            logger.error("Exception while getting Data of Row {} from File {} and Sheet {}. {}", rowNo, fileName, sheetName, e.getStackTrace());
        }
        return oneRowData;
    }

	public static Long getNoOfRows(String excelFilePath, String excelFileName, String sheetName) {
		Long noOfRows = -1L;
		try {
			FileInputStream file = new FileInputStream(new File(excelFilePath + "/" + excelFileName));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheet(sheetName);

			noOfRows = Integer.toUnsignedLong(sheet.getPhysicalNumberOfRows());
		} catch (Exception e) {
			logger.error("Exception while getting Total No of Rows in Excel Sheet {}. {}", sheetName, e.getStackTrace());
		}
		return noOfRows;
	}

	public List<String> getSheetNames() {
		return sheetNames;
	}

	// it will return all the sheetsName in WorkBook
	private List<String> readAllSheetsName(Workbook workbook) {

		List<String> sheetNames = new ArrayList<>();
		for (int i = 0; i < workBook.getNumberOfSheets(); i++) {
			sheetNames.add(workBook.getSheetName(i));
		}
		return sheetNames;
	}

	// Xls_Reader - Returns Sheet Existence
	public boolean isSheetExist(String sheetName) {
		int index = workBook.getSheetIndex(sheetName);
		if (index == -1) {
			index = workBook.getSheetIndex(sheetName.toUpperCase());
			if (index == -1)
				return false;

			return true;
		} else
			return true;
	}

	// Xls_Reader - Returns Row Count
	public int getRowCount(String sheetName) {
		int index = workBook.getSheetIndex(sheetName);
		if (index == -1) {
			return 0;
		} else {
			sheet = workBook.getSheetAt(index);
			int number = sheet.getLastRowNum() + 1;
			logger.debug("Row Count is {} ", number);
			return number;
		}
	}

	// Xls_Reader - Returns Column Count
	public int getColumnCount(String sheetName) {
		if (!isSheetExist(sheetName)) {
			return -1;
		}
		sheet = workBook.getSheet(sheetName);
		row = sheet.getRow(0);
		if (row == null) {
			return -1;
		}
		logger.debug("Column Count is {} ", row.getLastCellNum());
		return row.getLastCellNum();
	}

	// Xls_Reader - Returns the data as particular colNum and rowNum
	public String getCellData(String sheetName, int colNum, int rowNum) {
		try {
			if (rowNum < 0)
				return "";

			int index = workBook.getSheetIndex(sheetName);

			if (index == -1)
				return "";

			sheet = workBook.getSheetAt(index);
			row = sheet.getRow(rowNum);

			if (row == null)
				return "";

			Cell cell = row.getCell(colNum);

			if (cell == null)
				return "";

			if (cell.getCellTypeEnum() == CellType.STRING) {
//                logger.debug("{}",cell.getHyperlink().getClass());
				if (cell.getHyperlink() != null) {
					logger.debug("hyperLink is : {}", cell.getHyperlink().getAddress());
					logger.debug("cell value is : {}", cell.getStringCellValue());
					return cell.getStringCellValue() + ":::>" + "Link:" + cell.getHyperlink().getAddress();
				} else {
					logger.debug("cell value is : {}", cell.getStringCellValue());
					return cell.getStringCellValue();
				}
			} else if (cell.getCellTypeEnum() == CellType.NUMERIC || cell.getCellTypeEnum() == CellType.FORMULA) {
				String cellText = String.valueOf(cell.getNumericCellValue());
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					double d = cell.getNumericCellValue();

					Calendar cal = Calendar.getInstance();
					cal.setTime(HSSFDateUtil.getJavaDate(d));
					cellText = (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
					cellText = cal.get(Calendar.MONTH) + 1 + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cellText;
				}
				return cellText;
			} else if (cell.getCellTypeEnum() == CellType.BLANK)
				return "";

			else
				return String.valueOf(cell.getBooleanCellValue());
		} catch (Exception e) {
			e.printStackTrace();
			return "row " + rowNum + " or column " + colNum + " does not exist  in xls";
		}
	}

	// Xls_Reader - Returns the data in list of Strings based on the index Map
	public List<List<String>> getAllExcelData(String sheetName, HashMap<String, String> index) {

		List<List<String>> allData = new ArrayList<>();


		int startRow = Integer.parseInt(index.get("startRow"));
		int lastDataRow = Integer.parseInt(index.get("lastDataRow"));
		int startCol = Integer.parseInt(index.get("startCol"));
		int lastDataCol = Integer.parseInt(index.get("lastDataCol"));

		int c = getColumnCount(sheetName);
		int r = getRowCount(sheetName);
		logger.debug("Number of Columns Count is " + getColumnCount(sheetName));
		logger.debug("Number of Rows Count is " + getRowCount(sheetName));

		for (int i = startRow; i < r + lastDataRow; i++) {
			List<String> oneRowData = new ArrayList<>();
			for (int j = startCol; j < c + lastDataCol; j++) {
				logger.debug(" row_number {} , column_number {}  ", i, j);
				logger.debug(" |  " + getCellData(sheetName, j, i) + " |  ");
				oneRowData.add(getCellData(sheetName, j, i));
			}

			allData.add(oneRowData);
			logger.debug("\n-----------------------------------------------------------------------------------------");
		}
		return allData;
	}

	// function to verify whether Environment is Correct in Excel Sheet
	public boolean verifytheEnvironment(String link) throws MessagingException, IOException {
		logger.info("Internal Method Name is :---->{}", Thread.currentThread().getStackTrace()[1].getMethodName());
		logger.info("Environment Host  is : {} ", link);
		return link.contains(CONFIG.getProperty("endUserURL"));
	}

	// function to check if two days are not more than one day Apart
	public boolean matchDate(String showPageDate, String xlsDate, String showPageDateFormate, String xlsDateFormate) throws ParseException {
		logger.info("Internal Method Name is :---->{}", Thread.currentThread().getStackTrace()[1].getMethodName());
		logger.debug("Inside  matchDate function with showPageDate: {} ,xlsDate: {}", showPageDate, xlsDate);
		Date ShowPagedate, XlsDate;
		xlsDate = xlsDate.replace("UTC", "");
		showPageDate = showPageDate.replace("UTC", "");
		DateFormat df1 = new SimpleDateFormat(showPageDateFormate);
		DateFormat df2 = new SimpleDateFormat(xlsDateFormate);

		ShowPagedate = df1.parse(showPageDate);
		XlsDate = df2.parse(xlsDate);

		int daysApart = (int) (ShowPagedate.getTime() - XlsDate.getTime()) / (1000 * 60 * 60 * 24);
		if (abs(daysApart) >= 1)
			return false;
		else
			return true;

	}

	/**
	 * <h2>This method is to write the data into a cell of Data Sheet </h2>
	 *
	 * @param sheetToWrite is the sheet name where the cell to be written
	 * @param rowToWrite   is the row number for the cell
	 * @param colNum       is the number of column for the cell
	 * @param cellData     is the data to be written in the cell
	 * @return true if the cell is successfully written , or returns false
	 */
	public boolean writeCellData(Sheet sheetToWrite, Row rowToWrite, int colNum, Object cellData)  {
		logger.debug("Writing into sheet : [ {} ],row : [ {} ], cloumn : [ {} ], data : [ {} ]", sheetToWrite.getSheetName(), rowToWrite.getRowNum(), colNum, cellData);
		Cell cellToWrite = rowToWrite.createCell(colNum);
		if (cellData instanceof Integer) {
			cellToWrite.setCellValue((Integer) cellData);
            logger.debug(cellData+" : is of Integer");
		} else if (cellData instanceof Double) {
			cellToWrite.setCellValue((Double) cellData);
            logger.debug(cellData+" : is of Double");
		} else if (cellData instanceof Date) {
			cellToWrite.setCellValue((Date) cellData);
            logger.debug(cellData+" : is of Date");
		} else if (cellData instanceof String) {
			cellToWrite.setCellValue((String) cellData);
            logger.debug(cellData+" : is of String");
		} else {
			logger.error("The data type is  not matching for sheet : [ {} ], row : [ {} ], cloumn : [ {} ], data : [ {} ]", sheetToWrite.getSheetName(), rowToWrite.getRowNum(), colNum, cellData);
			logger.error("The data type is  not matching , please add more data Type [ {} ]", cellData.getClass());
			return false;
		}

		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(filePath + "/" + fileName);
			workBook.write(fileOut);
			fileOut.flush();
			fileOut.close();
			return true;
		} catch (Exception e) {
			logger.error("Got Exception while writing the data in row : [ {} ] , column : [ {} ] , data : [ {} ] , Exception : [ {} ]", rowToWrite.getRowNum(), colNum, cellData, e.getStackTrace());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * <h2>This method is for writing a row data in specified sheet</h2>
	 *
	 * @param sheetName     is the sheet name where the row to be created
	 * @param rowNumber     the number of roe to be created in the sheet
	 * @param columnDataMap , it contains the column number and data to be created for particular column
	 * @return true if the row is successfully written , or returns false
	 */
	public synchronized boolean writeRowData(String sheetName, int rowNumber, Map<Integer, Object> columnDataMap) {
	    boolean status =true;
		Sheet sheetToWrite = workBook.getSheet(sheetName);
		Row rowToWrite = sheetToWrite.createRow(rowNumber);
		for (Map.Entry<Integer, Object> entry : columnDataMap.entrySet()) {
			boolean cellResult = writeCellData(sheetToWrite, rowToWrite, entry.getKey(), entry.getValue());
			if (!cellResult) {
                status= false;
			}
		}
		return status;
	}

	public static synchronized Boolean editRowData(String excelFilePath, String excelFileName, String sheetName, int rowNumber, Map<Integer, Object> columnDataMap) {
		try {
			logger.info("Editing Excel File {}, Sheet {}, Row {}", excelFilePath + "/" + excelFileName, sheetName, rowNumber);
			FileInputStream excelFile = new FileInputStream(new File(excelFilePath + "/" + excelFileName));
			XSSFWorkbook workBook = new XSSFWorkbook(excelFile);
			XSSFSheet sheet = workBook.getSheet(sheetName);

			Row row = sheet.getRow(rowNumber);

			for (Map.Entry<Integer, Object> entry : columnDataMap.entrySet()) {
				if (row.getCell(entry.getKey()) == null) {
					row.createCell(entry.getKey());
				}

				Cell column = row.getCell(entry.getKey());
				String updatedValue = entry.getValue().toString();
				column.setCellValue(updatedValue);
			}

			excelFile.close();
			FileOutputStream out = new FileOutputStream(new File(excelFilePath + "/" + excelFileName));
			workBook.write(out);
			out.close();
		} catch (Exception e) {
			logger.error("Exception while Editing Row Data for Excel File {} and Sheet {}. {}", excelFilePath + "/" + excelFileName, sheetName, e.getStackTrace());
			return false;
		}
		return true;
	}

	public static synchronized Boolean editMultipleRowsData(String excelFilePath, String excelFileName, String sheetName, Map<Integer, Map<Integer, Object>> columnDataMap) {
		try {
			FileInputStream excelFile = new FileInputStream(new File(excelFilePath + "/" + excelFileName));
			XSSFWorkbook workBook = new XSSFWorkbook(excelFile);
			XSSFSheet sheet = workBook.getSheet(sheetName);
			logger.info("Editing Excel File {}, Sheet {}", excelFilePath + "/" + excelFileName, sheetName);

			for (Map.Entry<Integer, Map<Integer, Object>> oneRowData : columnDataMap.entrySet()) {
				Integer rowNumber = oneRowData.getKey();
				Row row = sheet.getRow(rowNumber);
				logger.info("Editing Row No. {}", rowNumber);

				for (Map.Entry<Integer, Object> entry : columnDataMap.get(rowNumber).entrySet()) {
					if (row.getCell(entry.getKey()) == null) {
						row.createCell(entry.getKey());
					}

					Cell column = row.getCell(entry.getKey());
					String updatedValue = entry.getValue().toString();
					column.setCellValue(updatedValue);
				}
			}

			excelFile.close();
			FileOutputStream out = new FileOutputStream(new File(excelFilePath + "/" + excelFileName));
			workBook.write(out);
			out.close();
		} catch (Exception e) {
			logger.error("Exception while Editing Multiple Rows Data for Excel File {} and Sheet {}. {}", excelFilePath + "/" + excelFileName, sheetName, e.getStackTrace());
			return false;
		}
		return true;
	}

}
