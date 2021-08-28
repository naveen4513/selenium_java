package office.sirion.suite.interpretation;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import office.sirion.util.DatePicker_Enhanced;
import office.sirion.util.TestUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class InterpretationCommunication extends TestSuiteBase {
  String result = null;
  String runmodes[] = null;
  static int count = -1;
  static boolean fail = false;
  static boolean skip = false;
  static boolean isTestPass = true;
  static DateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
  static Date date = new Date();
  @BeforeTest
  public void checkTestSkip() {
    if (!TestUtil.isTestCaseRunnable(int_suite_xls, this.getClass().getSimpleName())) {
      Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
      throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
    }
    runmodes = TestUtil.getDataSetRunmodes(int_suite_xls, this.getClass().getSimpleName());
  }

  @Test(dataProvider = "getTestData")
  public void InterpretationCommunicationTest(String ipComments, String ipActualDate, String ipRequestedBy, String ipChangeRequest,
      String ipUploadFile) throws InterruptedException {

    count++;
    if (!runmodes[count].equalsIgnoreCase("Y")) {
      skip = true;
      throw new SkipException("Runmode for Test Data Set to NO -- " + count);
    }

    Logger.debug("Executing Test Case Change Request Communication");

    // Launch The Browser
    openBrowser();
    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassintd"));

      driver.get(CONFIG.getProperty("endUserURL"));

  	fluentWaitMethod(locateElementBy("ip_quick_link"));

    locateElementBy("ip_quick_link").click();
    fluentWaitMethod(locateElementBy("entity_listing_page_first_entry_link"));
    locateElementBy("entity_listing_page_first_entry_link").click();
    fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'Save Comment/Attachment')]")));

    // Child Governance Body - Edit Page - COMMENTS AND ATTACHMENTS
    addFieldValue("ip_comment_box_textarea", ipComments);
    locateElementBy("ip_blank_area").click();
	addFieldValue("ip_requested_by_dropdown", ipRequestedBy);
	    
	  DatePicker_Enhanced date = new DatePicker_Enhanced();
		date.selectCalendar(ipActualDate,"actualDate");
		
	    
	addFieldValue("ip_change_request_dropdown", ipChangeRequest);
	
	if (!ipUploadFile.equalsIgnoreCase("")) {
		locateElementBy("entity_inteate_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\Child GB\\" + ipUploadFile);

		}
	
    Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Save Comment/Attachment')]")));
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
        driver.findElement(By.xpath("//button[contains(.,'Save Comment/Attachment')]")));
    driver.findElement(By.xpath("//button[contains(.,'Save Comment/Attachment')]")).click();


    if (getObject("ip_error_notifications") != null) {
      List<WebElement> e = driver.findElement(By.xpath(".//*[@id='genericErrors']/list")).findElements(By.tagName("li"));
      for (WebElement element : e) {
        String errors_inteate_page = element.getText();
        if (errors_inteate_page.contains("Please add a comment or upload files")) {
          Logger.debug("Please add a comment or upload files");
          Logger.debug("Errors: " + errors_inteate_page);
        }
      }
      
    }

    driver.findElement(By.linkText("COMMUNICATION")).click();
    waitF.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("table_728_wrapper"))));
	 
	 Assert.assertEquals(locateElementBy("ip_communication_status").getText(), "Comment");
	 
	 if (!ipComments.equalsIgnoreCase("")) {
		 	locateElementBy("ip_communication_latest_entry_link").click();
		 	
		 	try{
			Assert.assertEquals(locateElementBy("ip_communication_comment").getText(), ipComments,"Comment Matched");
			Assert.assertEquals(locateElementBy("ip_communication_details_comment").getText(), ipComments,"Comment Matched");
		 	}catch (AssertionError err) {
		 		System.out.println("Comment: " + ipComments + "and" + "Comment under ComunicationTab: " + locateElementBy("ip_communication_comment").getText() + "Does not Match");
		 	}
			
			}else{
				Assert.assertEquals(locateElementBy("ip_communication_comment").getText(), "-");
			}
	
	 if (!ipUploadFile.equalsIgnoreCase("")) {
		 	locateElementBy("ip_communication_latest_entry_link").click();
		 	
		 	try{
			Assert.assertEquals(locateElementBy("ip_communication_documents_link").getText(), ipUploadFile,"File Name Matched");
			Assert.assertEquals(locateElementBy("ip_communication_details_documents_link").getText(), ipUploadFile, "File Name Matched");
		 	}catch (AssertionError err) {
		 		System.out.println("Uploaded File Name: " + ipUploadFile + "and" + "File Name under ComunicationTab: " + locateElementBy("ip_communication_documents_link").getText() + "Does not Match");
		 	}
			}else{
				
				Assert.assertEquals(locateElementBy("ip_communication_documents_link").getText(), "-");
			}
	 
	 if (!ipActualDate.equalsIgnoreCase("")) {
		 String[] intActualdate = ipActualDate.split("-");
		 String[] TimeOfint = locateElementBy("ip_communication_time_of_action").getText().split("-");
		 	
		 	locateElementBy("ip_communication_latest_entry_link").click();
		 	try {
			Assert.assertEquals(TimeOfint[0],intActualdate[1], "Date Matched");
			Assert.assertEquals(TimeOfint[1],intActualdate[0].replace("e", ""), "Month Matched");
			Assert.assertEquals(TimeOfint[2],intActualdate[2], "Year Matched");
		 	}catch (AssertionError err) {
		 		System.out.println("Actual Date: " + ipActualDate + " and " + "Time of int under ComunicationTab: " + locateElementBy("ip_communication_time_of_action").getText() + " Does not Match");
		 	}
			String [] DetailsTimeOfint = locateElementBy("ip_communication_details_time_of_action").getText().replace("Time of int: ", "").split("-");
			
			try {
			Assert.assertEquals(DetailsTimeOfint[0],intActualdate[1], "Date Matched");
			Assert.assertEquals(DetailsTimeOfint[1],intActualdate[0].replace("e", ""), "Month Matched");
			Assert.assertEquals(DetailsTimeOfint[2],intActualdate[2], "Year Matched");
			}catch (AssertionError err) {
				System.out.println("Actual Date: " + ipActualDate + " and " + "Time of int under ComunicationTab: " + locateElementBy("ip_communication_time_of_action").getText()  + " Does not Match");
		 	}
			
			}else{
				locateElementBy("ip_communication_latest_entry_link").click();
				try{
				Assert.assertEquals(locateElementBy("ip_communication_time_of_action").getText(), dateFormat.format(date), "Date, Month and Year Matched");
				Assert.assertEquals(locateElementBy("ip_communication_details_time_of_action").getText().replaceAll("Time of int: ", ""), dateFormat.format(date), "Date, Month and Year Matched");
				}catch (AssertionError err) {
			 		System.out.println("Actual Date: " + ipActualDate + " and " + "Time of int under ComunicationTab: " + locateElementBy("ip_communication_time_of_action").getText()  + " Does not Match");
			 	}
			} 
	 
	 if (!ipRequestedBy.equalsIgnoreCase("")) {
		 	locateElementBy("ip_communication_latest_entry_link").click();
		 	
		 	try{
			Assert.assertEquals(locateElementBy("ip_communication_completed_by").getText(), ipRequestedBy, "Requested By Name Matched");
			Assert.assertEquals(locateElementBy("ip_communication_details_completed_by").getText().replaceAll("Completed By: ", ""), ipRequestedBy, "Requested By Name Matched");
		 	}catch (AssertionError err) {
		 		System.out.println("Requested By: " + ipRequestedBy + " and " + "Requested By under ComunicationTab: " + locateElementBy("ip_communication_completed_by").getText() + " Does not Match");
		 	}
			
	 		}else{
				locateElementBy("ip_communication_latest_entry_link").click();
				String logged_in_user = locateElementBy("ip_communication_logged_in_user_full_name").getText();
				
				try{
				Assert.assertEquals(locateElementBy("ip_communication_completed_by").getText(), logged_in_user, "Requested By Name Matched");
				Assert.assertEquals(locateElementBy("ip_communication_details_completed_by").getText().replaceAll("Completed By: ", ""), logged_in_user, "Requested By Name Matched");
				}catch (AssertionError err) {
			 		System.out.println("Requested By: " + ipRequestedBy + " and " + "Requested By under ComunicationTab: " + locateElementBy("ip_communication_completed_by").getText() + " Does not Match");
			 	}
			}
    driver.get(CONFIG.getProperty("endUserURL"));
  }

  @AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(int_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(int_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
		}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(int_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
		}
		try {
			for (int i = 2; i <= int_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
				String testCaseID = int_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
				if (!testCaseID.equalsIgnoreCase("")) {
					updateRun(convertStringToInteger(testCaseID), new java.lang.Exception().toString(), result);
				}
			}
		}catch(Exception e){
			Logger.debug(e);
		}
	}

  @AfterTest
  public void reportTestResult() {
    if (isTestPass)
      TestUtil.reportDataSetResult(int_suite_xls, "Test Cases", TestUtil.getRowNum(int_suite_xls, this.getClass().getSimpleName()), "PASS");
    else
      TestUtil.reportDataSetResult(int_suite_xls, "Test Cases", TestUtil.getRowNum(int_suite_xls, this.getClass().getSimpleName()), "FAIL");
  }

  @DataProvider
  public Object[][] getTestData() {
    return TestUtil.getData(int_suite_xls, this.getClass().getSimpleName());
  }
}
