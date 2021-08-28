package office.sirion.suite.contract;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import office.sirion.util.TestUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
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

public class ContractDocumentViewer extends TestSuiteBase {
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
	public void ContractDocumentViewerTest (String coDocumentName, String coViewer, String coSearch, String coDownload, String coFinancial,
			String coLegal, String coBusiness, String coCustomNumbering, String coDocumentRename, String coDocumentReplace, String coParentContract
			) throws Exception {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);



		openBrowser();
		endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

		Logger.debug("Executing Test Case Contract Document Viewer with Name -- " + coParentContract);

		driver.get(CONFIG.getProperty("endUserURL"));
		locateElementBy("contracts_quick_link");

		locateElementBy("contracts_quick_link").click();
		locateElementBy("entity_listing_page_display_dropdown_link");

		new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));


		if (!coParentContract.equalsIgnoreCase("") )
			try{

                driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][./text()='"+coParentContract+"']/preceding-sibling::td[1]/a")).isDisplayed();
				driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][./text()='"+coParentContract+"']/preceding-sibling::td[1]/a")).click();
			}
			catch (NoSuchElementException e){

                driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][div/div[./text()='"+coParentContract+"']]/preceding-sibling::td[1]/a")).isDisplayed();
                driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][div/div[./text()='"+coParentContract+"']]/preceding-sibling::td[1]/a")).click();
            }


			if(driver.findElements(By.className("success-icon")).size() != 0){
				String PromptAlert = driver.findElement(By.className("success-icon")).getText();
				Logger.debug("Show page is not getting dispalyed because: " + PromptAlert);

				driver.get(CONFIG.getProperty("endUserURL"));
				return;
			}

		String currentURL = driver.getCurrentUrl();
        String[] urlID = currentURL.split("/");
        String entityURLID = urlID[urlID.length-1].substring(0, 4);

		WebElement entityDocumentTree;

        try {
			entityDocumentTree = driver.findElement(By.id("j61_" + entityURLID));
		}catch (NoSuchElementException e){
        	Logger.debug("URL does not exist");
        	return;
		}
        entityDocumentTree.findElement(By.tagName("i")).click();
        //

        List<WebElement> entityChildTreeList = driver.findElement(By.id("j61_"+entityURLID)).findElement(By.tagName("ul")).findElements(By.tagName("li"));
        String entityDocumentID = null;

        // Check For Requested Document under Document Tree
        for (WebElement elementDocument : entityChildTreeList) {
        	if (elementDocument.getAttribute("entitytypeid").equalsIgnoreCase("23")) {
        		if (elementDocument.findElement(By.tagName("a")).getAttribute("title").equalsIgnoreCase(coDocumentName.split("\\.")[0])) {
        			entityDocumentID = elementDocument.getAttribute("entityid");
        			new Actions(driver).moveToElement(elementDocument).click().build().perform();
        			break;
                	}
        		}
        	else {
        		Logger.error("There is no such document -- "+coDocumentName+" -- under given Contract");
        		return;
        		}
        	}

        // Document Viewer Test Case
        if (coViewer.equalsIgnoreCase("Yes")) {
            Logger.debug("Contract Document View Testing has Started ...");
            driver.findElement(By.linkText("View")).click();
			locateElementBy("co_doc_viewer_title");

			String coDocShowPage = locateElementBy("co_doc_viewer_title").getText();
            Assert.assertEquals(coDocShowPage,  coDocumentName.split("\\.")[0]);

            if (!coParentContract.equalsIgnoreCase(""))
                Assert.assertEquals(driver.findElement(By.id("processing")).getText(),  "Please wait, system is analyzing the document.");
    		else
    			Assert.assertEquals(driver.findElement(By.id("processing")).getText(),  "Document is getting processed and not yet available. Please try after some time.");

            Logger.debug("Contract Document Viewer has been opened and verified the Document Name");
            driver.navigate().refresh();

            fluentWaitMethod(driver.findElement(By.id("tree")));
            }

        // Document Download Test Case
        if (coDownload.equalsIgnoreCase("Yes")) {
            Logger.debug("Contract Document Download Testing has Started ...");
			File file = new File(System.getProperty("user.home")+"\\Downloads\\"+coDocumentName);

			if (file.exists())
				file.delete();
			String CurrentURL = driver.getCurrentUrl();
			new Actions(driver).moveToElement(driver.findElement(By.id("j23_"+entityDocumentID))).click().build().perform();
            driver.findElement(By.linkText("Download")).click();
            try{
				driver.findElement(By.xpath("//*[@id='_title_title_id']/h2"));

				if(!driver.findElement(By.xpath("//*[@id='_title_title_id']/h2")).getText().equalsIgnoreCase("Internal Error")) {
					Thread.sleep(5000);
					driver.get(CurrentURL);
					//
				}
			} catch (NoSuchElementException ex){

				fluentWaitMethod(driver.findElement(By.id("tree")));

				if (driver.findElements(By.className("alertdialog-icon")).size()!=0)
					driver.findElement(By.cssSelector("button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-only")).click();

				FileInputStream inputstream = new FileInputStream(file);
				inputstream.close();
				Logger.debug("Contract Document has been downloaded and verified the Document");

			}

            Logger.debug("Internal Error is getting displayed on downloading Contract Document");
            Thread.sleep(5000);
            driver.get(CurrentURL);
            //
			fluentWaitMethod(driver.findElement(By.id("tree")));
            
            }

        // Document Rename Test Case
        if (!coDocumentRename.equalsIgnoreCase("")) {
            Logger.debug("Contract Document Rename Testing has Started ...");

            new Actions(driver).moveToElement(driver.findElement(By.id("j23_"+entityDocumentID))).click().build().perform();
            try{
            driver.findElement(By.linkText("Rename / Replace")).click();
            fluentWaitMethod(driver.findElement(By.id("uploadTabs")));

			driver.findElement(By.xpath(".//*[@id='elem_link']/input")).clear();
            driver.findElement(By.xpath(".//*[@id='elem_link']/input")).sendKeys(coDocumentRename);

            driver.findElement(By.cssSelector("input.reset.update")).click();
			fluentWaitMethod(driver.findElement(By.className("alertdialog-icon")));

			Assert.assertEquals(driver.findElement(By.className("alertdialog-icon")).getText(), "Document has been updated successfully");
			driver.findElement(By.cssSelector("button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-only")).click();

			new Actions(driver).moveToElement(driver.findElement(By.id("j23_"+entityDocumentID))).click().build().perform();
            driver.findElement(By.linkText("View")).click();
			fluentWaitMethod(driver.findElement(By.className("docHeader")));

			Assert.assertEquals(driver.findElement(By.className("docHeader")).getText(), coDocumentRename);

			Logger.debug("Contract Document has been Renamed and verified the Document Name");
            }catch (NoSuchElementException e) {
            	Logger.debug("Contract Document does not have an option to rename/repalce");
            }
            }

        // Document Replace Test Case
        if (!coDocumentReplace.equalsIgnoreCase("")) {
            Logger.debug("Contract Document Replace Testing has Started ...");

            new Actions(driver).moveToElement(driver.findElement(By.id("j23_"+entityDocumentID))).click().build().perform();
            try{
            driver.findElement(By.linkText("Rename / Replace")).click();
			fluentWaitMethod(driver.findElement(By.id("uploadTabs")));

			driver.findElement(By.id("uploadedFileEdit")).findElement(By.name("documentFileData")).sendKeys(System.getProperty("user.dir")+"\\file-upload\\Contract Documents\\"+coDocumentReplace);
            //

            // Document Replace File Check Test Case
            if (driver.findElements(By.className("alertdialog-icon")).size()!=0) {
            	if (driver.findElement(By.className("alertdialog-icon")).getText().contains("The file could not be uploaded")) {
                    Logger.debug("The File Cannot be Replaced as the File is still in Indexing Process");
    				driver.findElement(By.cssSelector("button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-only")).click();
    				return;
    				}
            	}

            driver.findElement(By.cssSelector("input.reset.update")).click();
			fluentWaitMethod(driver.findElement(By.className("alertdialog-icon")));

			Assert.assertEquals(driver.findElement(By.className("alertdialog-icon")).getText(), "Document has been updated successfully");
			driver.findElement(By.cssSelector("button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-only")).click();

			Assert.assertEquals(driver.findElement(By.id("23_"+entityDocumentID)).getAttribute("title"), coDocumentReplace);

			new Actions(driver).moveToElement(driver.findElement(By.id("j23_"+entityDocumentID))).click().build().perform();
            driver.findElement(By.linkText("View")).click();
			fluentWaitMethod(driver.findElement(By.className("docHeader")));

			Assert.assertEquals(driver.findElement(By.className("docHeader")).getText(), coDocumentReplace.split("\\.")[0]);

			Logger.debug("Contract Document has been Replaced and verified the Document");
            }
            catch  (NoSuchElementException e){
            	Logger.debug("Contract Document does not have an option to rename/replace");
            }
            }

        Logger.debug("Contract Document Viewer Test Case has been Executed Successfully");
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
