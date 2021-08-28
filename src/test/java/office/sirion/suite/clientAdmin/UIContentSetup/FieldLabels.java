package office.sirion.suite.clientAdmin.UIContentSetup;

import java.io.IOException;
import java.util.List;

import office.sirion.util.TestUtil;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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

public class FieldLabels extends TestSuiteBase {
	String result=null;
	String runmodes[]=null;
	static int count=-1;
	static boolean isTestPass=true;

	@BeforeTest
	public void checkTestSkip() {
		if(!TestUtil.isTestCaseRunnable(client_ui_content_setup_suite_xls,this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
			throw new SkipException("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(client_ui_content_setup_suite_xls, this.getClass().getSimpleName());
		}
	
	@Test (dataProvider="getTestData")
	public void FieldLabelsTest (String languageName,String clientAlias) throws InterruptedException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data -- " + languageName + " is set to NO " + count);

		openBrowser();
		clientAdminLogin(CONFIG.getProperty("clientAdminURL"), CONFIG.getProperty("clientAdminUsername"), CONFIG.getProperty("clientAdminPassword"));

		Logger.debug("Executing Test Field Labels Test for -- "+languageName);

		driver.get(CONFIG.getProperty("clientAdminURL"));
		Thread.sleep(5000);
		fluentWaitMethod(driver.findElement(By.linkText("Field Labels")));

		driver.findElement(By.linkText("Field Labels")).click();


		List<WebElement> list = driver.findElement(By.id("supplier")).findElements(By.tagName("option"));
		
		for (WebElement e : list) {

			if (e.getAttribute("value").equalsIgnoreCase("") || e.getAttribute("value").equalsIgnoreCase("1079") || e.getAttribute("value").equalsIgnoreCase("1083"))
				continue;

			Thread.sleep(5000);
			fluentWaitMethod(driver.findElement(By.id("language")));

			new Select(driver.findElement(By.id("language"))).selectByVisibleText(languageName);
			Thread.sleep(5000);

			Logger.debug("Executing Field Renaming for -- "+e.getText());
			System.out.println("Executing Field Renaming for -- "+e.getText());
			new Select(driver.findElement(By.id("supplier"))).selectByVisibleText(e.getText());


			List<WebElement> optionsList = driver.findElement(By.className("rendr-part")).findElements(By.tagName("input"));
			for (WebElement element : optionsList) {
				if (element.getAttribute("id").contains("cnt")) {
					WebElement elementParent = element.findElement(By.xpath("..")).findElement(By.xpath(".."));
				

					if (elementParent.getText().contains(">")){

                        element.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), elementParent.getText().replace(">","Greater Than").trim() + clientAlias);
                    }else if (elementParent.getText().contains("<")){

                        element.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), elementParent.getText().replace("<","Less Than").trim() + clientAlias);
                    }

					element.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), elementParent.getText().trim() + clientAlias);
					//element.sendKeys(elementParent.getText().trim() + clientAlias);
					}
				}

			driver.findElement(By.id("updateBtn")).click();


			driver.findElement(By.id("alertdialog"));
			String entityAlert = driver.findElement(By.id("alertdialog")).getText();
			
			System.out.println("" +entityAlert);

		//	Assert.assertEquals(entityAlert, "Successfully updated");

			driver.findElement(By.cssSelector("button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-only")).click();

			Thread.sleep(5000);
			}

		Logger.debug("Execution of Field Labels Test for -- "+languageName + " -- is PASSED");
		driver.get(CONFIG.getProperty("clientAdminURL"));
		}

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(client_ui_content_setup_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(client_ui_content_setup_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(client_ui_content_setup_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			}
	}

	@AfterTest
	public void reportTestResult() {
		if (isTestPass)
			TestUtil.reportDataSetResult(client_ui_content_setup_suite_xls, "Test Cases", TestUtil.getRowNum(client_ui_content_setup_suite_xls, this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(client_ui_content_setup_suite_xls, "Test Cases", TestUtil.getRowNum(client_ui_content_setup_suite_xls, this.getClass().getSimpleName()), "FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(client_ui_content_setup_suite_xls, this.getClass().getSimpleName());
		}
	}