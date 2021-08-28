package office.sirion.suite.contract;

import java.io.IOException;
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

public class ApplicationUpdate extends TestSuiteBase {
    String result = null;
    static boolean fail = true;
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
    public void ApplicationUpdateTest(String applicationName, String applicationActive, String applicationParentContract,
                                      String applicationParentGroup, String applicationParent
    ) throws InterruptedException {

        count++;
        if (!runmodes[count].equalsIgnoreCase("Y"))
            throw new SkipException("Runmode for Test Data Set to NO -- " + count);

        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

        Logger.debug("Executing Test Case Application Update with Title -- " + applicationName);

        driver.get(CONFIG.getProperty("endUserURL"));

        locateElementBy("contracts_quick_link");

        locateElementBy("contracts_quick_link").click();

        locateElementBy("entity_listing_page_display_dropdown_link");

        new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));


        if (!applicationParentContract.equalsIgnoreCase(""))
            try {
                driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][./text()='" + applicationParentContract + "']/preceding-sibling::td[1]/a")).isDisplayed();
                driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][./text()='" + applicationParentContract + "']/preceding-sibling::td[1]/a")).click();
            } catch (NoSuchElementException e) {
                driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][div/div[./text()='" + applicationParentContract + "']]/preceding-sibling::td[1]/a")).isDisplayed();
                driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][div/div[./text()='" + applicationParentContract + "']]/preceding-sibling::td[1]/a")).click();
            }
        else {
            locateElementBy("global_bulk_listing_page_first_entry_link").click();
        }

        locateElementBy("co_application_group_tab_link");

        locateElementBy("co_application_group_tab_link").click();


        new Select(locateElementBy("ag_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));

        if (!applicationParentGroup.equalsIgnoreCase("")) {
            try {
                driver.findElement(By.xpath(".//*[@id='table_46']/tbody/tr/td[3][./text()='" + applicationParentGroup + "']/preceding-sibling::td[2]/a")).isDisplayed();
                driver.findElement(By.xpath(".//*[@id='table_46']/tbody/tr/td[3][./text()='" + applicationParentGroup + "']/preceding-sibling::td[2]/a")).click();
            } catch (NoSuchElementException e) {
                driver.findElement(By.xpath(".//*[@id='table_46']/tbody/tr/td[3][div/div[./text()='" + applicationParentGroup + "']]/preceding-sibling::td[2]/a")).isDisplayed();
                driver.findElement(By.xpath(".//*[@id='table_46']/tbody/tr/td[3][div/div[./text()='" + applicationParentGroup + "']]/preceding-sibling::td[2]/a")).click();
            }

        locateElementBy("co_application_link_tab");

        locateElementBy("co_application_link_tab").click();


        new Select(locateElementBy("application_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));


        if (!applicationParent.equalsIgnoreCase("")) {
            try {
                driver.findElement(By.xpath(".//*[@id='table_1421']/tbody/tr/td[2][./text()='" + applicationParent + "']/preceding-sibling::td[1]/a")).isDisplayed();
                driver.findElement(By.xpath(".//*[@id='table_1421']/tbody/tr/td[2][./text()='" + applicationParent + "']/preceding-sibling::td[1]/a")).click();
            } catch (NoSuchElementException e) {
                driver.findElement(By.xpath(".//*[@id='table_1421']/tbody/tr/td[2][div/div[./text()='" + applicationParent + "']]/preceding-sibling::td[1]/a")).isDisplayed();
                driver.findElement(By.xpath(".//*[@id='table_1421']/tbody/tr/td[2][div/div[./text()='" + applicationParent + "']]/preceding-sibling::td[1]/a")).click();
            }


            // APPLICATION - SHOW PAGE - EDIT BUTTON
            WebElement elementEdit = locateElementBy("co_show_page_bottom_tab").findElement(By.cssSelector("div.submit.freeze"));
            Assert.assertNotNull(elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
            elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")).click();

            locateElementBy("co_application_create_page_name_textbox");

            // APPLICATION - EDIT PAGE - BASIC INFORMATION
            addFieldValue("co_application_create_page_name_textbox", applicationName.trim());

            addFieldValue("co_application_create_page_active_checkbox", applicationActive.trim());

            // APPLICATION - EDIT PAGE - UPDATE BUTTON
            WebElement elementUpdate = locateElementBy("co_show_page_bottom_tab").findElement(By.cssSelector("div.submit.freeze"));
            Assert.assertNotNull(elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")));
            elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")).click();
            locateElementBy("co_application_create_page_name_textbox");

            String entityShowPageName = locateElementBy("co_application_create_page_name_textbox").getText();

            Assert.assertEquals(entityShowPageName, applicationName);

            Logger.debug("Test Case Application Update with Title -- " + applicationName + " -- is PASSED");
            driver.get(CONFIG.getProperty("endUserURL"));
        }else {
            Logger.debug("No Such Credit n Earnback available for selected Contract:" + applicationParentContract);
            Logger.debug("Test Case Credit/Earnback Update with Title -- " + applicationParent + " -- is not Executed, as TestData is Incorrect");
            fail = false;
            driver.get(CONFIG.getProperty("endUserURL"));
        }

    } else{
            Logger.debug("No Such Credit n Earnback available for selected Contract:"+applicationParentContract);
            Logger.debug("Test Case Credit/Earnback Update with Title -- " + applicationParent + " -- is not Executed, as TestData is Incorrect");
            fail = false;
            driver.get(CONFIG.getProperty("endUserURL"));
        }
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
		if (isTestPass)
			TestUtil.reportDataSetResult(contract_suite_xls, "Test Cases", TestUtil.getRowNum(contract_suite_xls, this.getClass().getSimpleName()),"PASS");
		else
			TestUtil.reportDataSetResult(contract_suite_xls, "Test Cases", TestUtil.getRowNum(contract_suite_xls, this.getClass().getSimpleName()),"FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(contract_suite_xls, this.getClass().getSimpleName());
		}
	}
