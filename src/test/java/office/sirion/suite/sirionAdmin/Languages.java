package office.sirion.suite.sirionAdmin;

import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Languages extends TestSuiteBase {
	String runmodes[]=null;
	static int count=-1;
	static boolean isTestPass=true;
	String testCaseID=null;
	String result=null;

	@BeforeTest
	public void checkTestSkip() {
		if(!TestUtil.isTestCaseRunnable(sirion_admin_suite_xls,this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
			throw new SkipException("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(sirion_admin_suite_xls, this.getClass().getSimpleName());
		}

	@Test (dataProvider="getTestData")
	public void LanguagesCreation (String languagesName, String languagesFile
			) throws InterruptedException {

		count++;
		if(!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data -- " +languagesName +" set to NO " +count);

		openBrowser();
		sirionAdminLogin(CONFIG.getProperty("sirionAdminURL"), CONFIG.getProperty("sirionAdminUsername"), CONFIG.getProperty("sirionAdminPassword"));

		Logger.debug("Executing Test Sirion Admin Languages Creation for -- "+languagesName);

		driver.get(CONFIG.getProperty("sirionAdminURL"));
		//
		fluentWaitMethod(driver.findElement(By.linkText("Languages")));

		driver.findElement(By.linkText("Languages")).click();
		//
		fluentWaitMethod(driver.findElement(By.className("plus")));

		driver.findElement(By.className("plus")).click();
		//
		fluentWaitMethod(driver.findElement(By.id("_name_id")));

		if (!languagesName.equalsIgnoreCase("")) {
			driver.findElement(By.id("_name_id")).clear();
			driver.findElement(By.id("_name_id")).sendKeys(languagesName.trim());
			}

		try {
			File file1 = new File(System.getProperty("user.home")+"//Downloads//DataPackTemplate.xls");

			if (file1.exists())
				file1.delete();

			driver.findElement(By.id("downloadTemplate")).click();
			//

			if (driver.findElements(By.className("alertdialog-icon")).size()!=0)
				driver.findElement(By.cssSelector("button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-only")).click();

			File file = new File(languagesFile);
			
			if (file.exists())
				file.delete();

			file1.renameTo(file);

			FileInputStream inputstream = new FileInputStream(file);
			@SuppressWarnings("resource")
			HSSFWorkbook workbook = new HSSFWorkbook(inputstream);

			HSSFSheet sheet = workbook.getSheet("Sheet1");
			int rowCount = sheet.getLastRowNum();

			@SuppressWarnings("unused")
			HSSFRow row = sheet.getRow(0);

			for (int i=1; i<=rowCount; i++) {
				String str = sheet.getRow(i).getCell(0).getStringCellValue();			
				
				Cell cell = sheet.getRow(i).createCell(1);
				cell.setCellValue(str);			
				}

			FileOutputStream outputstream = new FileOutputStream(file);
			//workbook.write(file);

			outputstream.close();
			} catch (Exception e) {
				Logger.error(e.getMessage());
				}

		if (!languagesFile.equalsIgnoreCase("")) {
			driver.findElement(By.id("_multipartFile_id")).clear();
			driver.findElement(By.id("_multipartFile_id")).sendKeys(System.getProperty("user.dir") + "//" + languagesFile);
			//
			}

		driver.findElement(By.xpath(".//*[@id='tabs-inner-sec']/div[2]/div/input")).click();
		//

		if (languagesName.equalsIgnoreCase("")) {
			String entityErrors = driver.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "This field is required");
			Logger.debug("Language Name is Mandatory");
			
		    driver.get(CONFIG.getProperty("sirionAdminURL"));
		    return;
		    }

		if (languagesName.length() > 128) {
			String entityErrors = driver.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "Maximum 128 characters allowed");
			Logger.debug("For Language Name, -- Maximum 128 characters allowed");
			
		    driver.get(CONFIG.getProperty("sirionAdminURL"));
		    return;
		    }
		
		if(driver.findElements(By.id("alertdialog")).size()!=0) {
			String entityErrors = driver.findElement(By.id("errors")).getText();
			
			if (entityErrors.contains("Select A Unique Language Name"))
				Logger.debug("For Language, " +languagesName+" -- Select A Unique Language Name");

			if (entityErrors.contains("Error occurred while parsing workbook."))
				Logger.debug("For Language, " +languagesName+" -- Error occurred while parsing workbook.");
			
			if (entityErrors.contains("Please upload file  "))
				Logger.debug("For Language, " +languagesName+" -- Please upload file");
						
			driver.get(CONFIG.getProperty("sirionAdminURL"));
			return;
			}
		
		if (driver.findElements(By.className("success-icon")).size()!=0)
			driver.findElement(By.cssSelector("button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-only")).click();
		
		driver.findElement(By.className("dataTables_filter")).findElement(By.tagName("label")).findElement(By.tagName("input")).sendKeys(languagesName);
		//
        
        driver.findElement(By.xpath(".//*[@id='l_com_sirionlabs_model_MasterGroup']/tbody/tr[@role='row']/td[contains(.,'"+ languagesName +"')]/preceding-sibling::td[1]/a")).click();

        String entityTypeShowPage = driver.findElement(By.id("_c_com_sirionlabs_model_language_name_name_id")).getText();
        Assert.assertEquals(entityTypeShowPage, languagesName, "Languages Name at show page is -- " +entityTypeShowPage+ " instead of -- " +languagesName);

		Logger.debug("Test Sirion Admin Languages Creation for -- " + languagesName + " is PASSED");
		driver.get(CONFIG.getProperty("sirionAdminURL"));
		}

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
	   takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());
	   
	   if(testResult.getStatus()==ITestResult.SKIP)
	      TestUtil.reportDataSetResult(sirion_admin_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
	   else if(testResult.getStatus()==ITestResult.FAILURE) {
	      isTestPass=false;
	      TestUtil.reportDataSetResult(sirion_admin_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
	      result= "Fail";
	      }
	   else if (testResult.getStatus()==ITestResult.SUCCESS) {
	      TestUtil.reportDataSetResult(sirion_admin_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
	      result= "Pass";
	      }
	   try {
	      if (!testCaseID.equalsIgnoreCase(""))
	         TestlinkIntegration.updateTestLinkResult(testCaseID,"",result);
	      } catch (Exception e) {
	                   Logger.debug(e);
	         }
	   }

	@AfterTest
	public void reportTestResult() {
		if (isTestPass)
			TestUtil.reportDataSetResult(sirion_admin_suite_xls, "Test Cases", TestUtil.getRowNum(sirion_admin_suite_xls, this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(sirion_admin_suite_xls, "Test Cases", TestUtil.getRowNum(sirion_admin_suite_xls, this.getClass().getSimpleName()), "FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(sirion_admin_suite_xls, this.getClass().getSimpleName());
		}
	}