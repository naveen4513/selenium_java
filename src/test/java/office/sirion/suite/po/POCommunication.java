package office.sirion.suite.po;

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

public class POCommunication extends TestSuiteBase {
	static int count = -1;
	String result = null;
	static boolean fail = true;
	static boolean skip = false;
	static boolean isTestPass = true;
	String runmodes[] = null;
	static DateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
	static Date date = new Date();
	
	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(po_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(po_suite_xls, this.getClass().getSimpleName());
		}
	
	@Test(dataProvider = "getTestData")
	public void POCommunicationTest(String poComments, String poActualDate, String poRequestedBy, String poChangeRequest,
			String poUploadFile, String poTitle) throws InterruptedException {
		
		count++;
		if (!runmodes[count].equalsIgnoreCase("Y")) {
			skip = true;
			throw new SkipException("Runmode for test set data set to no " + count);
			}
	
		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

        driver.get(CONFIG.getProperty("endUserURL"));
	    
	    fluentWaitMethod(locateElementBy("po_quick_link"));

	    locateElementBy("po_quick_link").click();
	    fluentWaitMethod(locateElementBy("po_listing_page_first_link"));
	    locateElementBy("po_listing_page_first_link").click();
	    fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'Save Comment/Attachment')]")));

	    // Child Governance Body - Edit Page - COMMENTS AND ATTACHMENTS
	    addFieldValue("po_comment_box_textarea", poComments);
	    locateElementBy("po_blank_area").click();
		addFieldValue("po_requested_by_dropdown", poRequestedBy);
		    
		DatePicker_Enhanced date = new DatePicker_Enhanced();
		date.selectCalendar(poActualDate,"actualDate");
		
		addFieldValue("po_change_request_dropdown", poChangeRequest);
		
		if (!poUploadFile.equalsIgnoreCase("")) {
			locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\Child po\\" + poUploadFile);

			}
		
	    Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Save Comment/Attachment')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
	        driver.findElement(By.xpath("//button[contains(.,'Save Comment/Attachment')]")));
	    driver.findElement(By.xpath("//button[contains(.,'Save Comment/Attachment')]")).click();


	    if (getObject("po_error_notifications") != null) {
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
	    waitF.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("table_3652_wrapper"))));
		 
		 Assertion(locateElementBy("po_communication_status").getText(), "Comment/Attachment");
		 
		 if (!poComments.equalsIgnoreCase("")) {
			 	locateElementBy("po_communication_latest_entry_link").click();
			 	
			 	try{
				Assert.assertEquals(locateElementBy("po_communication_comment").getText(), poComments,"Comment Matched");
				driver.switchTo().frame("iframe_textEditor_showpage");
				Assert.assertEquals(locateElementBy("po_communication_details_comment").getText(), poComments,"Comment Matched");
				driver.switchTo().defaultContent();
			 	}catch (AssertionError err) {
			 		System.out.println("Comment: " + poComments + "and" + "Comment under ComunicationTab: " + locateElementBy("po_communication_comment").getText() + "Does not Match");
			 	}
				
				}else{
					Assert.assertEquals(locateElementBy("po_communication_comment").getText(), "-");
				}
		
		 if (!poUploadFile.equalsIgnoreCase("")) {
			 	locateElementBy("po_communication_latest_entry_link").click();
			 	
			 	try{
				Assertion(locateElementBy("po_communication_documents_link").getText(), poUploadFile);
				Assertion(locateElementBy("po_communication_details_documents_link").getText(), poUploadFile);
			 	}catch (AssertionError err) {
			 		System.out.println("Uploaded File Name: " + poUploadFile + "and" + "File Name under ComunicationTab: " + locateElementBy("po_communication_documents_link").getText() + "Does not Match");
			 	}
				}else{
					
					Assert.assertEquals(locateElementBy("po_communication_documents_link").getText(), "-");
				}
		 
		 if (!poActualDate.equalsIgnoreCase("")) {
			 
			 String[] cobActualdate = poActualDate.split("-");
			    
			 if (!poActualDate.equalsIgnoreCase("")) {
				 
				 String[] TimeOfcob = locateElementBy("po_communication_time_of_action").getText().split("-");
				 
				 
				 	locateElementBy("po_communication_latest_entry_link").click();
				 	try {
					Assert.assertEquals(TimeOfcob[0],cobActualdate[1], "Date Matched");
					Assert.assertEquals(TimeOfcob[1],cobActualdate[0].replace("e", ""), "Month Matched");
					Assert.assertEquals(TimeOfcob[2],cobActualdate[2], "Year Matched");
				 	}catch (AssertionError err) {
				 		System.out.println("Actual Date: " + cobActualdate + " and " + "Time of Action under ComunicationTab: " + locateElementBy("po_communication_time_of_action").getText() + " Does not Match");
				 	}
					String [] DetailsTimeOfwor = locateElementBy("po_communication_details_time_of_action").getText().replace("Time of Action: ", "").split("-");
					
					try {
					Assert.assertEquals(DetailsTimeOfwor[0],cobActualdate[1], "Date Matched");
					Assert.assertEquals(DetailsTimeOfwor[1],cobActualdate[0].replace("e", ""), "Month Matched");
					Assert.assertEquals(DetailsTimeOfwor[2],cobActualdate[2], "Year Matched");
					}catch (AssertionError err) {
						System.out.println("Actual Date: " + cobActualdate + " and " + "Time of Action under ComunicationTab: " + locateElementBy("po_communication_time_of_action").getText()  + " Does not Match");
				 	}
					
					}else{
						locateElementBy("po_communication_latest_entry_link").click();
						try{
						Assert.assertEquals(locateElementBy("po_communication_time_of_action").getText(), dateFormat.format(date), "Date, Month and Year Matched");
						Assert.assertEquals(locateElementBy("po_communication_details_time_of_action").getText().replaceAll("Time of wor: ", ""), dateFormat.format(date), "Date, Month and Year Matched");
						}catch (AssertionError err) {
					 		System.out.println("Actual Date: " + cobActualdate + " and " + "Time of Action under ComunicationTab: " + locateElementBy("po_communication_time_of_action").getText()  + " Does not Match");
					 	}
					} 
				} 
		 
		 if (!poRequestedBy.equalsIgnoreCase("")) {
			 	locateElementBy("po_communication_latest_entry_link").click();
			 	
			 	try{
				Assert.assertEquals(locateElementBy("po_communication_completed_by").getText(), poRequestedBy, "Requested By Name Matched");
				Assert.assertEquals(locateElementBy("po_communication_details_completed_by").getText().replaceAll("Completed By: ", ""), poRequestedBy, "Requested By Name Matched");
			 	}catch (AssertionError err) {
			 		System.out.println("Requested By: " + poRequestedBy + " and " + "Requested By under ComunicationTab: " + locateElementBy("po_communication_completed_by").getText() + " Does not Match");
			 	}
				
		 		}else{
					locateElementBy("po_communication_latest_entry_link").click();
					String logged_in_user = locateElementBy("po_communication_logged_in_user_full_name").getText();
					
					try{
					Assert.assertEquals(locateElementBy("po_communication_completed_by").getText(), logged_in_user, "Requested By Name Matched");
					Assert.assertEquals(locateElementBy("po_communication_details_completed_by").getText().replaceAll("Completed By: ", ""), logged_in_user, "Requested By Name Matched");
					}catch (AssertionError err) {
				 		System.out.println("Requested By: " + poRequestedBy + " and " + "Requested By under ComunicationTab: " + locateElementBy("po_communication_completed_by").getText() + " Does not Match");
				 	}
				}
	    driver.get(CONFIG.getProperty("endUserURL"));
	}
	
	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(po_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(po_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
			}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(po_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
			}
		try {
          for (int i = 2; i <= po_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
              String testCaseID = po_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
              if (!testCaseID.equalsIgnoreCase("")) {
                  updateRun(convertStringToInteger(testCaseID),new java.lang.Exception().toString(), result);
              }
          }
	   }catch(Exception e){
          Logger.debug(e);
      }
		}
	@AfterTest
	public void reportTestResult() {
		if (isTestPass)
			TestUtil.reportDataSetResult(po_suite_xls, "Test Cases", TestUtil.getRowNum(po_suite_xls, this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(po_suite_xls, "Test Cases", TestUtil.getRowNum(po_suite_xls, this.getClass().getSimpleName()), "FAIL");
			}
	
	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(po_suite_xls, this.getClass().getSimpleName());
		}
}
