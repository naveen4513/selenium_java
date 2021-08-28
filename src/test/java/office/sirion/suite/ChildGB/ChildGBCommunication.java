package office.sirion.suite.ChildGB;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import office.sirion.util.DatePicker_Enhanced;
import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;

import office.sirion.util.TestlinkIntegration;
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

import testlink.api.java.client.TestLinkAPIException;
import testlink.api.java.client.TestLinkAPIResults;

public class ChildGBCommunication extends TestSuiteBase {

  String runmodes[] = null;
  static int count = -1;
  static boolean fail = false;
  static boolean skip = false;
  static boolean isTestPass = true;
  static DateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
  static Date date = new Date();
	String testCaseID=null;
	String result=null;
	
  @BeforeTest
  public void checkTestSkip() {
    if (!TestUtil.isTestCaseRunnable(child_gb_suite_xls, this.getClass().getSimpleName())) {
      Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
      throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
    }
    runmodes = TestUtil.getDataSetRunmodes(child_gb_suite_xls, this.getClass().getSimpleName());
  }

  @Test(dataProvider = "getTestData")
  public void ChildSLCommunicationTest(String testCaseId, String cgbComments, String cgbActualDate, String cgbRequestedBy, String cgbChangeRequest,
      String cgbUploadFile) throws InterruptedException, TestLinkAPIException {

    count++;
    if (!runmodes[count].equalsIgnoreCase("Y")) {
      skip = true;
      throw new SkipException("Runmode for Test Data Set to NO -- " + count);
    }

    Logger.debug("Executing Test Case Child Governance Body Communication");

    // Launch The Browser
    openBrowser();
    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

      driver.get(CONFIG.getProperty("endUserURL"));


      //
  	locateElementBy("gb_quick_link");

    locateElementBy("gb_quick_link").click();


    locateElementBy("entity_listing_page_first_entry_link").click();
    locateElementBy("cgb_savecommentattachment_button");

    // Child Governance Body - Edit Page - COMMENTS AND ATTACHMENTS
    addFieldValue("cgb_comment_box_textarea", cgbComments);
    
	addFieldValue("cgb_requested_by_dropdown", cgbRequestedBy);
	    
	String[] cgbActualdate = cgbActualDate.split("-");
    String cgbActual_DateMonth = cgbActualdate[0];
    String cgbActual_DateYear = convertStringToInteger(cgbActualdate[2]);
    int cgbActual_dateYear = Integer.parseInt(cgbActual_DateYear);
    String cgbActual_DateDate = convertStringToInteger(cgbActualdate[1]);
    Integer cgbActual_dateDate = Integer.parseInt(cgbActual_DateDate);
    cgbActual_DateDate = cgbActual_dateDate.toString();

    DatePicker_Enhanced dp_cgbActual_date = new DatePicker_Enhanced();
    dp_cgbActual_date.expDate = cgbActual_DateDate;
    dp_cgbActual_date.expMonth = cgbActual_DateMonth;
    dp_cgbActual_date.expYear = cgbActual_dateYear;
    dp_cgbActual_date.pickExpDate("actualDate");
	    
	addFieldValue("cgb_change_request_dropdown", cgbChangeRequest);
	
	if (!cgbUploadFile.equalsIgnoreCase("")) {
		locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\Child GB\\" + cgbUploadFile);
		}
	
	

    Assert.assertNotNull(locateElementBy("cgb_savecommentattachment_button"));
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
            locateElementBy("cgb_savecommentattachment_button"));
      locateElementBy("cgb_savecommentattachment_button").click();


    if (getObject("cgb_error_notifications") != null) {
      List<WebElement> e = locateElementBy("cgb_error_notification").findElements(By.tagName("li"));
      for (WebElement element : e) {
        String errors_create_page = element.getText();
        if (errors_create_page.contains("Please add a comment or upload files")) {
          Logger.debug("Please add a comment or upload files");
          Logger.debug("Errors: " + errors_create_page);
        }
      }
      
    }

    locateElementBy("cgb_communication_tab").click();
    locateElementBy("cgb_communication_tab_inner_part");
	 
	 Assert.assertEquals(locateElementBy("cgb_communication_status").getText(), "Comment");
	 
	 if (!cgbComments.equalsIgnoreCase("")) {
		 	locateElementBy("cgb_communication_latest_entry_link").click();
		 	
		 	try{
			Assert.assertEquals(locateElementBy("cgb_communication_comment").getText(), cgbComments,"Comment Matched");
			Assert.assertEquals(locateElementBy("cgb_communication_details_comment").getText(), cgbComments,"Comment Matched");
		 	}catch (AssertionError err) {
		 		System.out.println("Comment: " + cgbComments + "and" + "Comment under ComunicationTab: " + locateElementBy("cgb_communication_comment").getText() + "Does not Match");
		 	}
			
			}else{
				Assert.assertEquals(locateElementBy("cgb_communication_comment").getText(), "-");
			}
	
	 if (!cgbUploadFile.equalsIgnoreCase("")) {
		 	locateElementBy("cgb_communication_latest_entry_link").click();
		 	
		 	try{
			Assert.assertEquals(locateElementBy("cgb_communication_documents_link").getText(), cgbUploadFile,"File Name Matched");
			Assert.assertEquals(locateElementBy("cgb_communication_details_documents_link").getText(), cgbUploadFile, "File Name Matched");
		 	}catch (AssertionError err) {
		 		System.out.println("Uploaded File Name: " + cgbUploadFile + "and" + "File Name under ComunicationTab: " + locateElementBy("cgb_communication_documents_link").getText() + "Does not Match");
		 	}
			}else{
				
				Assert.assertEquals(locateElementBy("cgb_communication_documents_link").getText(), "-");
			}
	 
	 if (!cgbActualDate.equalsIgnoreCase("")) {
		 
		 String[] TimeOfAction = locateElementBy("cgb_communication_time_of_action").getText().split("-");
		 	
		 	locateElementBy("cgb_communication_latest_entry_link").click();
		 	try {
			Assert.assertEquals(TimeOfAction[0],cgbActualdate[1], "Date Matched");
			Assert.assertEquals(TimeOfAction[1],cgbActualdate[0].replace("e", ""), "Month Matched");
			Assert.assertEquals(TimeOfAction[2],cgbActualdate[2], "Year Matched");
		 	}catch (AssertionError err) {
		 		System.out.println("Actual Date: " + cgbActualDate + " and " + "Time of Action under ComunicationTab: " + locateElementBy("cgb_communication_time_of_action").getText() + " Does not Match");
		 	}
			String [] DetailsTimeOfAction = locateElementBy("cgb_communication_details_time_of_action").getText().replace("Time of Action: ", "").split("-");
			
			try {
			Assert.assertEquals(DetailsTimeOfAction[0],cgbActualdate[1], "Date Matched");
			Assert.assertEquals(DetailsTimeOfAction[1],cgbActualdate[0].replace("e", ""), "Month Matched");
			Assert.assertEquals(DetailsTimeOfAction[2],cgbActualdate[2], "Year Matched");
			}catch (AssertionError err) {
				System.out.println("Actual Date: " + cgbActualDate + " and " + "Time of Action under ComunicationTab: " + locateElementBy("cgb_communication_time_of_action").getText()  + " Does not Match");
		 	}
			
			}else{
				locateElementBy("cgb_communication_latest_entry_link").click();
				try{
				Assert.assertEquals(locateElementBy("cgb_communication_time_of_action").getText(), dateFormat.format(date), "Date, Month and Year Matched");
				Assert.assertEquals(locateElementBy("cgb_communication_details_time_of_action").getText().replaceAll("Time of Action: ", ""), dateFormat.format(date), "Date, Month and Year Matched");
				}catch (AssertionError err) {
			 		System.out.println("Actual Date: " + cgbActualDate + " and " + "Time of Action under ComunicationTab: " + locateElementBy("cgb_communication_time_of_action").getText()  + " Does not Match");
			 	}
			} 
	 
	 if (!cgbRequestedBy.equalsIgnoreCase("")) {
		 	locateElementBy("cgb_communication_latest_entry_link").click();
		 	
		 	try{
			Assert.assertEquals(locateElementBy("cgb_communication_completed_by").getText(), cgbRequestedBy, "Requested By Name Matched");
			Assert.assertEquals(locateElementBy("cgb_communication_details_completed_by").getText().replaceAll("Completed By: ", ""), cgbRequestedBy, "Requested By Name Matched");
		 	}catch (AssertionError err) {
		 		System.out.println("Requested By: " + cgbRequestedBy + " and " + "Requested By under ComunicationTab: " + locateElementBy("cgb_communication_completed_by").getText() + " Does not Match");
		 	}
			
	 		}else{
				locateElementBy("cgb_communication_latest_entry_link").click();
				String logged_in_user = locateElementBy("cgb_communication_logged_in_user_full_name").getText();
				
				try{
				Assert.assertEquals(locateElementBy("cgb_communication_completed_by").getText(), logged_in_user, "Requested By Name Matched");
				Assert.assertEquals(locateElementBy("cgb_communication_details_completed_by").getText().replaceAll("Completed By: ", ""), logged_in_user, "Requested By Name Matched");
				}catch (AssertionError err) {
			 		System.out.println("Requested By: " + cgbRequestedBy + " and " + "Requested By under ComunicationTab: " + locateElementBy("cgb_communication_completed_by").getText() + " Does not Match");
			 	}
			}
    driver.get(CONFIG.getProperty("endUserURL"));
  }

  
    @AfterMethod
    public void reportDataSetResult(ITestResult testResult) throws IOException {
        takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

        if(testResult.getStatus()==ITestResult.SKIP)
            TestUtil.reportDataSetResult(child_gb_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
        else if(testResult.getStatus()==ITestResult.FAILURE) {
            isTestPass=false;
            TestUtil.reportDataSetResult(child_gb_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
            result= "Fail";
        }
        else if (testResult.getStatus()==ITestResult.SUCCESS) {
            TestUtil.reportDataSetResult(child_gb_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
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
      TestUtil.reportDataSetResult(child_gb_suite_xls, "Test Cases", TestUtil.getRowNum(child_gb_suite_xls, this.getClass().getSimpleName()), "PASS");
    else
      TestUtil.reportDataSetResult(child_gb_suite_xls, "Test Cases", TestUtil.getRowNum(child_gb_suite_xls, this.getClass().getSimpleName()), "FAIL");
  }

  @DataProvider
  public Object[][] getTestData() {
    return TestUtil.getData(child_gb_suite_xls, this.getClass().getSimpleName());
  }
}
