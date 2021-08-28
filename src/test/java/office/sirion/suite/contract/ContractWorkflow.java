package office.sirion.suite.contract;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import office.sirion.util.TestUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ContractWorkflow extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean isTestPass = true;
	String testCaseID;

	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(contract_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(contract_suite_xls, this.getClass().getSimpleName());
		}

	@Test(dataProvider = "getTestData")
	public void ContractWorkflowTest (String coWorkflowSteps, String coAgreementNo,String coContractNo,String coParent) throws InterruptedException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);

		openBrowser();
		endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

		Logger.debug("Executing Test Case Contract Workflow for -- " + coParent);

		driver.get(CONFIG.getProperty("endUserURL"));
		locateElementBy("contracts_quick_link");

		locateElementBy("contracts_quick_link").click();
		locateElementBy("entity_listing_page_display_dropdown_link");


		new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));


		if (!coParent.equalsIgnoreCase("") )
			try{
				driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][./text()='"+coParent+"']/preceding-sibling::td[1]/a")).click();
				fluentWaitMethod(driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][./text()='"+coParent+"']/preceding-sibling::td[1]/a")));
			} catch (NoSuchElementException e){
                driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][div/div[./text()='" + coParent + "']]/preceding-sibling::td[1]/a")).click();
                fluentWaitMethod(driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][div/div[./text()='" + coParent + "']]/preceding-sibling::td[1]/a")));
			}
			else {
		    locateElementBy("global_bulk_listing_page_first_entry_link").click();
        }
		
		locateElementBy("entity_clone_button");
		Assert.assertNotNull(locateElementBy("entity_clone_button"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", locateElementBy("entity_clone_button"));
        locateElementBy("entity_clone_button").click();

		
		addFieldValue("co_create_page_agreement_no_textbox", coAgreementNo.trim());

        if (CONFIG.getProperty("entityDataCreation").equalsIgnoreCase("Yes")) {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("ss");
            String datestr = sdf.format(date);

            addFieldValue("co_create_page_contract_no_textbox", coContractNo.trim() + datestr);
        } else
            addFieldValue("co_create_page_contract_no_textbox", coContractNo.trim());

		locateElementBy("co_create_page_submit_button");
		Assert.assertNotNull(		locateElementBy("co_create_page_submit_button"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", 		locateElementBy("co_create_page_submit_button"));
        locateElementBy("co_create_page_submit_button").click();


		if (driver.findElements(By.className("success-icon")).size()!=0) {
			String entityID = driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).getText();

			Logger.debug("issue cloned successfully with issue ID " + entityID);

			driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).click();
			locateElementBy("issue_show_page_id");
			

			String entityShowPageID = locateElementBy("issue_show_page_id").getText();

			Assert.assertEquals(entityShowPageID, entityID);
			Logger.debug("issue ID on show page has been verified");
		}
	

	fluentWaitMethod(locateElementBy("co_show_page_bottom_tab").findElement(By.cssSelector("div.submit.freeze")));

		if (!coWorkflowSteps.equalsIgnoreCase("")) {
			for (String entityData : coWorkflowSteps.split(";")) {
				WebElement elementWorkflow = locateElementBy("co_show_page_bottom_tab").findElement(By.cssSelector("div.submit.freeze"));
				try {
					fluentWaitMethod(elementWorkflow.findElement(By.xpath(".//button[contains(.,'"+entityData.trim()+"')]")));
					Assert.assertNotNull(elementWorkflow.findElement(By.xpath(".//button[contains(.,'"+entityData.trim()+"')]")));
					elementWorkflow.findElement(By.xpath(".//button[contains(.,'"+entityData.trim()+"')]")).click();


					if (driver.findElements(By.className("success-icon")).size()!=0) {
						String entityWFMessage = locateElementBy("csl_pop_up_id").getText();

						if (entityWFMessage.equalsIgnoreCase("In addition to the present contract, would you like to perform this action for its underlying contracts with the same status?")) {
							Assert.assertEquals(entityWFMessage, "In addition to the present contract, would you like to perform this action for its underlying contracts with the same status?");

							Assert.assertNotNull(locateElementBy("csl_pop_up_yes_option"));
							locateElementBy("csl_pop_up_yes_option").click();

							}

						else if (entityWFMessage.equalsIgnoreCase("This operation may not be performed on some underlying entities locked for bulk operation. Do you want to continue?")) {
							Assert.assertEquals(entityWFMessage, "This operation may not be performed on some underlying entities locked for bulk operation. Do you want to continue?");

							Assert.assertNotNull(locateElementBy("csl_pop_up_yes_option"));
							locateElementBy("csl_pop_up_yes_option").click();


							if (driver.findElements(By.className("success-icon")).size()!=0) {
								String entityPublishMessage = driver.findElement(By.className("success-icon")).getText();

								if (entityPublishMessage.equalsIgnoreCase("In addition to the present contract, would you like to perform this action for its underlying contracts with the same status?")) {
									Assert.assertEquals(entityPublishMessage, "In addition to the present contract, would you like to perform this action for its underlying contracts with the same status?");

									Assert.assertNotNull(locateElementBy("csl_pop_up_yes_option"));
                                    locateElementBy("csl_pop_up_yes_option").click();

									}
								}
							}

						if (entityData.trim().equalsIgnoreCase("Delete")) {
							if (driver.findElements(By.className("success-icon")).size()!=0) {
								String entityDeleteWFMessage = locateElementBy("csl_pop_up_id").getText();

								if (entityDeleteWFMessage.equalsIgnoreCase("Are you sure you would like to delete this entity?")) {
									Assert.assertEquals(entityDeleteWFMessage, "Are you sure you would like to delete this entity?");

									Assert.assertNotNull(locateElementBy("csl_pop_up_yes_option"));
                                    locateElementBy("csl_pop_up_yes_option").click();

									}
								}
							}
						}

					fluentWaitMethod(locateElementBy("co_show_page_bottom_tab").findElement(By.cssSelector("div.submit.freeze")));
					} catch (NoSuchElementException e) {
						Logger.debug("No Such Element with the given Key, -- "+entityData.trim()+" -- Moving onto Next Step");
						}
			    	  }
			      }

		Logger.debug("Test Case Contract Workflow for -- " + coParent + " -- is PASSED");
		driver.get(CONFIG.getProperty("endUserURL"));
		}

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(contract_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(contract_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
			}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(contract_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
			}
		try {
            for (int i = 2; i <= contract_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
                String testCaseID = contract_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
		if(isTestPass)
			TestUtil.reportDataSetResult(contract_suite_xls, "Test Cases", TestUtil.getRowNum(contract_suite_xls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(contract_suite_xls, "Test Cases", TestUtil.getRowNum(contract_suite_xls,this.getClass().getSimpleName()), "FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(contract_suite_xls, this.getClass().getSimpleName());
		}
	}
