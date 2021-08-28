package office.sirion.suite.clause;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import office.sirion.util.DatabaseQueryChange;
import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
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

public class ClauseCommunication extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean isTestPass = true;
	String testCaseID;
	
	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(clause_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(clause_suite_xls, this.getClass().getSimpleName());
		}

	@Test(dataProvider = "getTestData")
	public void ClauseCommunicationTest (String testCaseID, String Privacy, String Comments, String ActualDate, String RequestedBy, String ChangeRequest,
			String UploadFiles, String ParentEntity
			) throws InterruptedException, TestLinkAPIException, ClassNotFoundException, SQLException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);
		
		this.testCaseID = testCaseID;

		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
	    
	    Logger.debug("Executing Test Case CLAUSE LIBRARY Communication Tab with Title -- " + ParentEntity);

	    driver.get(CONFIG.getProperty("endUserURL"));
	    //
		fluentWaitMethod(driver.findElement(By.name("searchtext")));

	    String clauseID = DatabaseQueryChange.getClauseDatabaseID("select client_entity_seq_id from clause where name ='" +ParentEntity.trim()+ "' and deleted = false and client_id in (select id from client  where alias ilike'"+CONFIG.getProperty("clientSupplierAlias").trim()+"')" );

	    if (clauseID==null)
	    	Assert.fail("There is no Such Clause Name as " + ParentEntity);

	    driver.findElement(By.name("searchtext")).sendKeys(clauseID);
	    driver.findElement(By.name("searchtext")).sendKeys(Keys.ENTER);
	    //
	    if (driver.findElements(By.className("alertdialog-icon")).size()!=0) {
	    	if (driver.findElement(By.className("alertdialog-icon")).getText().equalsIgnoreCase("Either you do not have the required permissions or this Entity has been deleted or doesn't exist anymore.")) {
	    		Logger.debug("Either you do not have the required permissions or this Entity has been deleted or doesn't exist anymore. for -- " + ParentEntity);

	    		driver.findElement(By.cssSelector("button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-only.ui-state-hover")).click();

	    		driver.get(CONFIG.getProperty("endUserURL"));
	    	    return;
	    		}
	    	}
		fluentWaitMethod(driver.findElement(By.linkText("GENERAL")));

	    driver.findElement(By.linkText("GENERAL")).click();
	    //
		fluentWaitMethod(driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze")));

		// CLAUSE LIBRARY - EDIT PAGE - COMMENTS AND ATTACHMENTS
		addFieldValue("entity_create_page_privacy_checkbox", Privacy.trim());

		addFieldValue("entity_create_page_comments_textarea", Comments.trim());

		addFieldValue("entity_create_page_actual_date", ActualDate.trim());

		addFieldValue("entity_create_page_requested_by_dropdown", RequestedBy.trim());

		addFieldValue("entity_create_page_change_request_dropdown", ChangeRequest.trim());
		
		if (!UploadFiles.equalsIgnoreCase("")) {
			locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\Clause\\" + UploadFiles.trim());
			//
			}

		// CLAUSE LIBRARY - SHOW PAGE - Save Comment/Attachment BUTTON
	    WebElement elementSaveComment = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));		
		Assert.assertNotNull(elementSaveComment.findElement(By.xpath(".//button[contains(.,'Save Comment/Attachment')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementSaveComment.findElement(By.xpath(".//button[contains(.,'Save Comment/Attachment')]")));
	    elementSaveComment.findElement(By.xpath(".//button[contains(.,'Save Comment/Attachment')]")).click();
		//

		// CLAUSE LIBRARY - SHOW PAGE - VALIDATIONS
	    if (locateElementBy("entity_create_page_error_notifications")!=null) {
	    	List<WebElement> elementErrors = driver.findElements(By.xpath(".//*[@class='genericErrors']/list/li"));
			for (WebElement element : elementErrors) {
				String entityErrors = element.getText();
				 
				if (entityErrors.contains("Please add a comment or upload files")) {
					Logger.debug("For CLAUSE LIBRARY -- " +ParentEntity+ " -- Please add a comment or upload files");
			        }
				}

		    driver.get(CONFIG.getProperty("endUserURL"));
		    return;
	    	}
	    //
	    fluentWaitMethod(driver.findElement(By.linkText("COMMUNICATION")));

	    driver.findElement(By.linkText("COMMUNICATION")).click();
		//
	    Logger.debug("Test Case CLAUSE LIBRARY Communication Tab with Title -- " + ParentEntity + " -- is STARTED");

	    verifyCommunicationTab(Comments, UploadFiles);

	    Logger.debug("Test Case CLAUSE LIBRARY Communication Tab with Title -- " + ParentEntity + " -- is PASSED");
	    driver.get(CONFIG.getProperty("endUserURL"));
	    }

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(clause_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(clause_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
			}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(clause_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
			}

		try {
			if (!testCaseID.equalsIgnoreCase(""))
				TestlinkIntegration.updateTestLinkResult(testCaseID,"",result);
			} catch (Exception e) {
				}
		}

	@AfterTest
	public void reportTestResult() {
		if (isTestPass)
			TestUtil.reportDataSetResult(clause_suite_xls, "Test Cases", TestUtil.getRowNum(clause_suite_xls, this.getClass().getSimpleName()),"PASS");
		else
			TestUtil.reportDataSetResult(clause_suite_xls, "Test Cases", TestUtil.getRowNum(clause_suite_xls, this.getClass().getSimpleName()),"FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(clause_suite_xls, this.getClass().getSimpleName());
		}
	}
