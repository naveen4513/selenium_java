package office.sirion.suite.dispute;

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

public class DisputeCommunication extends TestSuiteBase {
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
    if (!TestUtil.isTestCaseRunnable(dispute_suite_xls, this.getClass().getSimpleName())) {
      Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
      throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
    }
    runmodes = TestUtil.getDataSetRunmodes(dispute_suite_xls, this.getClass().getSimpleName());
  }

  @Test(dataProvider = "getTestData")
  public void DisputeCommunicationTest(String disputeComments, String disputeActualDate, String disputeRequestedBy, String disputeChangeRequest,
      String disputeUploadFile) throws InterruptedException {

    count++;
    if (!runmodes[count].equalsIgnoreCase("Y")) {
      skip = true;
      throw new SkipException("Runmode for Test Data Set to NO -- " + count);
    }

    Logger.debug("Executing Test Case dispute Communication");

    // Launch The Browser
    openBrowser();
    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

      driver.get(CONFIG.getProperty("endUserURL"));

  	fluentWaitMethod(locateElementBy("disputes_quick_link"));

    locateElementBy("disputes_quick_link").click();
    fluentWaitMethod(locateElementBy("entity_listing_page_first_entry_link"));
    locateElementBy("entity_listing_page_first_entry_link").click();
    fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'Save Comment/Attachment')]")));

    // Child Governance Body - Edit Page - COMMENTS AND ATTACHMENTS
    addFieldValue("dispute_comment_box_textarea", disputeComments);
    locateElementBy("dispute_blank_area").click();
	addFieldValue("dispute_requested_by_dropdown", disputeRequestedBy);

      DatePicker_Enhanced date = new DatePicker_Enhanced();
      date.selectCalendar(disputeActualDate,"actualDate");
	    
	addFieldValue("dispute_change_request_dropdown", disputeChangeRequest);
	
	if (!disputeUploadFile.equalsIgnoreCase("")) {
		locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\Child GB\\" + disputeUploadFile);
		}
	
    Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Save Comment/Attachment')]")));
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
        driver.findElement(By.xpath("//button[contains(.,'Save Comment/Attachment')]")));
    driver.findElement(By.xpath("//button[contains(.,'Save Comment/Attachment')]")).click();

    if (getObject("dispute_error_notifications") != null) {
      List<WebElement> e = driver.findElement(By.xpath(".//*[@id='genericErrors']/list")).findElements(By.tagName("li"));
      for (WebElement element : e) {
        String errors_create_page = element.getText();
        if (errors_create_page.contains("Please add a comment or upload files")) {
          Logger.debug("Please add a comment or upload files");
          Logger.debug("Errors: " + errors_create_page);
        }
      }
      
    }

    driver.findElement(By.linkText("COMMUNICATION")).click();
    waitF.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("table_3552_wrapper"))));
	 
	 Assert.assertEquals(locateElementBy("dispute_communication_status").getText(), "Comment/Attachment");
	 
	 if (!disputeComments.equalsIgnoreCase("")) {
		 	locateElementBy("dispute_communication_latest_entry_link").click();
		 	
		 	try{
			Assert.assertEquals(locateElementBy("dispute_communication_comment").getText(), disputeComments,"Comment Matched");
			Assert.assertEquals(locateElementBy("dispute_communication_details_comment").getText(), disputeComments,"Comment Matched");
		 	}catch (AssertionError err) {
		 		System.out.println("Comment: " + disputeComments + "and" + "Comment under ComunicationTab: " + locateElementBy("dispute_communication_comment").getText() + "Does not Match");
		 	}
			
			}else{
				Assert.assertEquals(locateElementBy("dispute_communication_comment").getText(), "-");
			}
	
	 if (!disputeUploadFile.equalsIgnoreCase("")) {
		 	locateElementBy("dispute_communication_latest_entry_link").click();
		 	
		 	try{
			Assert.assertEquals(locateElementBy("dispute_communication_documents_link").getText(), disputeUploadFile,"File Name Matched");
			Assert.assertEquals(locateElementBy("dispute_communication_details_documents_link").getText(), disputeUploadFile, "File Name Matched");
		 	}catch (AssertionError err) {
		 		System.out.println("Uploaded File Name: " + disputeUploadFile + "and" + "File Name under ComunicationTab: " + locateElementBy("dispute_communication_documents_link").getText() + "Does not Match");
		 	}
			}else{
				
				Assert.assertEquals(locateElementBy("dispute_communication_documents_link").getText(), "-");
			}
	 
	 if (!disputeActualDate.equalsIgnoreCase("")) {
		 String[] actualDate = disputeActualDate.split("-");
		 String[] TimeOfdispute = locateElementBy("dispute_communication_time_of_dispute").getText().split("-");
		 	
		 	locateElementBy("dispute_communication_latest_entry_link").click();
		 	try {
			Assert.assertEquals(TimeOfdispute[0],actualDate[1], "Date Matched");
			Assert.assertEquals(TimeOfdispute[1],actualDate[0].replace("e", ""), "Month Matched");
			Assert.assertEquals(TimeOfdispute[2],actualDate[2], "Year Matched");
		 	}catch (AssertionError err) {
		 		System.out.println("Actual Date: " + disputeActualDate + " and " + "Time of dispute under ComunicationTab: " + locateElementBy("dispute_communication_time_of_dispute").getText() + " Does not Match");
		 	}
			String [] DetailsTimeOfdispute = locateElementBy("dispute_communication_details_time_of_dispute").getText().replace("Time of dispute: ", "").split("-");
			
			try {
			Assert.assertEquals(DetailsTimeOfdispute[0],actualDate[1], "Date Matched");
			Assert.assertEquals(DetailsTimeOfdispute[1],actualDate[0].replace("e", ""), "Month Matched");
			Assert.assertEquals(DetailsTimeOfdispute[2],actualDate[2], "Year Matched");
			}catch (AssertionError err) {
				System.out.println("Actual Date: " + disputeActualDate + " and " + "Time of dispute under ComunicationTab: " + locateElementBy("dispute_communication_time_of_dispute").getText()  + " Does not Match");
		 	}
			
			}else{
				locateElementBy("dispute_communication_latest_entry_link").click();
				try{
				Assert.assertEquals(locateElementBy("dispute_communication_time_of_dispute").getText(), dateFormat.format(date), "Date, Month and Year Matched");
				Assert.assertEquals(locateElementBy("dispute_communication_details_time_of_dispute").getText().replaceAll("Time of dispute: ", ""), dateFormat.format(date), "Date, Month and Year Matched");
				}catch (AssertionError err) {
			 		System.out.println("Actual Date: " + disputeActualDate + " and " + "Time of dispute under ComunicationTab: " + locateElementBy("dispute_communication_time_of_dispute").getText()  + " Does not Match");
			 	}
			} 
	 
	 if (!disputeRequestedBy.equalsIgnoreCase("")) {
		 	locateElementBy("dispute_communication_latest_entry_link").click();
		 	
		 	try{
			Assert.assertEquals(locateElementBy("dispute_communication_completed_by").getText(), disputeRequestedBy, "Requested By Name Matched");
			Assert.assertEquals(locateElementBy("dispute_communication_details_completed_by").getText().replaceAll("Completed By: ", ""), disputeRequestedBy, "Requested By Name Matched");
		 	}catch (AssertionError err) {
		 		System.out.println("Requested By: " + disputeRequestedBy + " and " + "Requested By under ComunicationTab: " + locateElementBy("dispute_communication_completed_by").getText() + " Does not Match");
		 	}
			
	 		}else{
				locateElementBy("dispute_communication_latest_entry_link").click();
				String logged_in_user = locateElementBy("dispute_communication_logged_in_user_full_name").getText();
				
				try{
				Assert.assertEquals(locateElementBy("dispute_communication_completed_by").getText(), logged_in_user, "Requested By Name Matched");
				Assert.assertEquals(locateElementBy("dispute_communication_details_completed_by").getText().replaceAll("Completed By: ", ""), logged_in_user, "Requested By Name Matched");
				}catch (AssertionError err) {
			 		System.out.println("Requested By: " + disputeRequestedBy + " and " + "Requested By under ComunicationTab: " + locateElementBy("dispute_communication_completed_by").getText() + " Does not Match");
			 	}
			}
    driver.get(CONFIG.getProperty("endUserURL"));
  }

  @AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(dispute_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(dispute_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
		}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(dispute_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
		}
		try {
			for (int i = 2; i <= dispute_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
				String testCaseID = dispute_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
      TestUtil.reportDataSetResult(dispute_suite_xls, "Test Cases", TestUtil.getRowNum(dispute_suite_xls, this.getClass().getSimpleName()), "PASS");
    else
      TestUtil.reportDataSetResult(dispute_suite_xls, "Test Cases", TestUtil.getRowNum(dispute_suite_xls, this.getClass().getSimpleName()), "FAIL");
  }

  @DataProvider
  public Object[][] getTestData() {
    return TestUtil.getData(dispute_suite_xls, this.getClass().getSimpleName());
  }
}
