package office.sirion.suite.issue;

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

public class IssueCommunication extends TestSuiteBase {
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
    if (!TestUtil.isTestCaseRunnable(issue_suite_xls, this.getClass().getSimpleName())) {
      Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
      throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
    }
    runmodes = TestUtil.getDataSetRunmodes(issue_suite_xls, this.getClass().getSimpleName());
  }

  @Test(dataProvider = "getTestData")
  public void IssueCommunicationTest(String issueComments, String issueActualDate, String issueRequestedBy, String issueChangeRequest,
      String issueUploadFile) throws InterruptedException {

    count++;
    if (!runmodes[count].equalsIgnoreCase("Y")) {
      skip = true;
      throw new SkipException("Runmode for Test Data Set to NO -- " + count);
    }

    Logger.debug("Executing Test Case issue Communication");

    // Launch The Browser
    openBrowser();
    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

      driver.get(CONFIG.getProperty("endUserURL"));

  	fluentWaitMethod(locateElementBy("issues_quick_link"));

    locateElementBy("issues_quick_link").click();
    fluentWaitMethod(locateElementBy("entity_listing_page_first_entry_link"));
    locateElementBy("entity_listing_page_first_entry_link").click();
    fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'Save Comment/Attachment')]")));

    // Child Governance Body - Edit Page - COMMENTS AND ATTACHMENTS
    addFieldValue("issue_comment_box_textarea", issueComments);
    locateElementBy("issue_blank_area").click();
	addFieldValue("issue_requested_by_dropdown", issueRequestedBy);
	    
	String[] issueActualdate = issueActualDate.split("-");
    String issueActual_DateMonth = issueActualdate[0];
    String issueActual_DateYear = convertStringToInteger(issueActualdate[2]);
    int issueActual_dateYear = Integer.parseInt(issueActual_DateYear);
    String issueActual_DateDate = convertStringToInteger(issueActualdate[1]);
    Integer issueActual_dateDate = Integer.parseInt(issueActual_DateDate);
    issueActual_DateDate = issueActual_dateDate.toString();

    DatePicker_Enhanced dp_issueActual_date = new DatePicker_Enhanced();
    dp_issueActual_date.expDate = issueActual_DateDate;
    dp_issueActual_date.expMonth = issueActual_DateMonth;
    dp_issueActual_date.expYear = issueActual_dateYear;
    dp_issueActual_date.pickExpDate("actualDate");
	    
	addFieldValue("issue_change_request_dropdown", issueChangeRequest);
	
	if (!issueUploadFile.equalsIgnoreCase("")) {
		locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\Child GB\\" + issueUploadFile);

		}
	
    Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Save Comment/Attachment')]")));
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
        driver.findElement(By.xpath("//button[contains(.,'Save Comment/Attachment')]")));
    driver.findElement(By.xpath("//button[contains(.,'Save Comment/Attachment')]")).click();


    if (getObject("issue_error_notifications") != null) {
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
    waitF.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("table_121_wrapper"))));
	 
	 Assert.assertEquals(locateElementBy("issue_communication_status").getText(), "Comment");
	 
	 if (!issueComments.equalsIgnoreCase("")) {
		 	locateElementBy("issue_communication_latest_entry_link").click();
		 	
		 	try{
			Assert.assertEquals(locateElementBy("issue_communication_comment").getText(), issueComments,"Comment Matched");
			Assert.assertEquals(locateElementBy("issue_communication_details_comment").getText(), issueComments,"Comment Matched");
		 	}catch (AssertionError err) {
		 		System.out.println("Comment: " + issueComments + "and" + "Comment under ComunicationTab: " + locateElementBy("issue_communication_comment").getText() + "Does not Match");
		 	}
			
			}else{
				Assert.assertEquals(locateElementBy("issue_communication_comment").getText(), "-");
			}
	
	 if (!issueUploadFile.equalsIgnoreCase("")) {
		 	locateElementBy("issue_communication_latest_entry_link").click();
		 	
		 	try{
			Assert.assertEquals(locateElementBy("issue_communication_documents_link").getText(), issueUploadFile,"File Name Matched");
			Assert.assertEquals(locateElementBy("issue_communication_details_documents_link").getText(), issueUploadFile, "File Name Matched");
		 	}catch (AssertionError err) {
		 		System.out.println("Uploaded File Name: " + issueUploadFile + "and" + "File Name under ComunicationTab: " + locateElementBy("issue_communication_documents_link").getText() + "Does not Match");
		 	}
			}else{
				
				Assert.assertEquals(locateElementBy("issue_communication_documents_link").getText(), "-");
			}
	 
	 if (!issueActualDate.equalsIgnoreCase("")) {
		 
		 String[] TimeOfissue = locateElementBy("issue_communication_time_of_issue").getText().split("-");
		 	
		 	locateElementBy("issue_communication_latest_entry_link").click();
		 	try {
			Assert.assertEquals(TimeOfissue[0],issueActualdate[1], "Date Matched");
			Assert.assertEquals(TimeOfissue[1],issueActualdate[0].replace("e", ""), "Month Matched");
			Assert.assertEquals(TimeOfissue[2],issueActualdate[2], "Year Matched");
		 	}catch (AssertionError err) {
		 		System.out.println("Actual Date: " + issueActualDate + " and " + "Time of issue under ComunicationTab: " + locateElementBy("issue_communication_time_of_issue").getText() + " Does not Match");
		 	}
			String [] DetailsTimeOfissue = locateElementBy("issue_communication_details_time_of_issue").getText().replace("Time of issue: ", "").split("-");
			
			try {
			Assert.assertEquals(DetailsTimeOfissue[0],issueActualdate[1], "Date Matched");
			Assert.assertEquals(DetailsTimeOfissue[1],issueActualdate[0].replace("e", ""), "Month Matched");
			Assert.assertEquals(DetailsTimeOfissue[2],issueActualdate[2], "Year Matched");
			}catch (AssertionError err) {
				System.out.println("Actual Date: " + issueActualDate + " and " + "Time of issue under ComunicationTab: " + locateElementBy("issue_communication_time_of_issue").getText()  + " Does not Match");
		 	}
			
			}else{
				locateElementBy("issue_communication_latest_entry_link").click();
				try{
				Assert.assertEquals(locateElementBy("issue_communication_time_of_issue").getText(), dateFormat.format(date), "Date, Month and Year Matched");
				Assert.assertEquals(locateElementBy("issue_communication_details_time_of_issue").getText().replaceAll("Time of issue: ", ""), dateFormat.format(date), "Date, Month and Year Matched");
				}catch (AssertionError err) {
			 		System.out.println("Actual Date: " + issueActualDate + " and " + "Time of issue under ComunicationTab: " + locateElementBy("issue_communication_time_of_issue").getText()  + " Does not Match");
			 	}
			} 
	 
	 if (!issueRequestedBy.equalsIgnoreCase("")) {
		 	locateElementBy("issue_communication_latest_entry_link").click();
		 	
		 	try{
			Assert.assertEquals(locateElementBy("issue_communication_completed_by").getText(), issueRequestedBy, "Requested By Name Matched");
			Assert.assertEquals(locateElementBy("issue_communication_details_completed_by").getText().replaceAll("Completed By: ", ""), issueRequestedBy, "Requested By Name Matched");
		 	}catch (AssertionError err) {
		 		System.out.println("Requested By: " + issueRequestedBy + " and " + "Requested By under ComunicationTab: " + locateElementBy("issue_communication_completed_by").getText() + " Does not Match");
		 	}
			
	 		}else{
				locateElementBy("issue_communication_latest_entry_link").click();
				String logged_in_user = locateElementBy("issue_communication_logged_in_user_full_name").getText();
				
				try{
				Assert.assertEquals(locateElementBy("issue_communication_completed_by").getText(), logged_in_user, "Requested By Name Matched");
				Assert.assertEquals(locateElementBy("issue_communication_details_completed_by").getText().replaceAll("Completed By: ", ""), logged_in_user, "Requested By Name Matched");
				}catch (AssertionError err) {
			 		System.out.println("Requested By: " + issueRequestedBy + " and " + "Requested By under ComunicationTab: " + locateElementBy("issue_communication_completed_by").getText() + " Does not Match");
			 	}
			}
    driver.get(CONFIG.getProperty("endUserURL"));
  }

  @AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(issue_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(issue_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
		}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(issue_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
		}
		try {
			for (int i = 2; i <= issue_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
				String testCaseID = issue_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
      TestUtil.reportDataSetResult(issue_suite_xls, "Test Cases", TestUtil.getRowNum(issue_suite_xls, this.getClass().getSimpleName()), "PASS");
    else
      TestUtil.reportDataSetResult(issue_suite_xls, "Test Cases", TestUtil.getRowNum(issue_suite_xls, this.getClass().getSimpleName()), "FAIL");
  }

  @DataProvider
  public Object[][] getTestData() {
    return TestUtil.getData(issue_suite_xls, this.getClass().getSimpleName());
  }
}
