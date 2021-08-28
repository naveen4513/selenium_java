package office.sirion.suite.temp;

	import java.io.FileInputStream;
	import java.io.FileOutputStream;
	import java.io.IOException;
	import java.io.InputStream;
	import java.util.Iterator;

	import org.apache.poi.hssf.usermodel.HSSFCell;
	import org.apache.poi.hssf.usermodel.HSSFRow;
	import org.apache.poi.hssf.usermodel.HSSFSheet;
	import org.apache.poi.hssf.usermodel.HSSFWorkbook;

	import org.apache.poi.xssf.usermodel.XSSFCell;
	import org.apache.poi.xssf.usermodel.XSSFRow;
	import org.apache.poi.xssf.usermodel.XSSFSheet;
	import org.apache.poi.xssf.usermodel.XSSFWorkbook;

	public class Read_Data_Excel {
	    @SuppressWarnings("deprecation")
		public static void readXLSXFile() throws IOException
	    {
	        InputStream ExcelFileToRead = new FileInputStream("C:\\Test.xlsx");
	        XSSFWorkbook  wb = new XSSFWorkbook(ExcelFileToRead);
	        
	        XSSFSheet sheet = wb.getSheetAt(0);
	        XSSFRow row; 
	        XSSFCell cell;
	        Iterator rows = sheet.rowIterator();
	        while (rows.hasNext())
	        {
	            row=(XSSFRow) rows.next();
	            Iterator cells = row.cellIterator();
	            while (cells.hasNext())
	            {
	                cell=(XSSFCell) cells.next();   
	                if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING)
	                {
	                    System.out.print(cell.getStringCellValue()+" ");
	                }
	                else if(cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC)
	                {
	                    System.out.print(cell.getNumericCellValue()+" ");
	                }
	                else
	                {
	                }
	            }
	            System.out.println();
	        }
	    }   
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
