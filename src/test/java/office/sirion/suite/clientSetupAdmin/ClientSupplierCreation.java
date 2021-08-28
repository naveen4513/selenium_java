package office.sirion.suite.clientSetupAdmin;

import java.io.IOException;
import office.sirion.util.TestUtil;
import org.openqa.selenium.By;
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

public class ClientSupplierCreation extends TestSuiteBase {
	String runmodes[]=null;
	static int count=-1;
	static boolean isTestPass=true;
	String testCaseID=null;
	String result=null;

	@BeforeTest
	public void checkTestSkip() {
		if(!TestUtil.isTestCaseRunnable(client_setup_admin_suite_xls,this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
			throw new SkipException("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(client_setup_admin_suite_xls, this.getClass().getSimpleName());
		}

	@Test (dataProvider="getTestData")
	public void ClientSupplierCreationTest (String clientName, String clientTimezone, String clientAlias, String clientSupplierType,
			String clientLanguagePack,String clientHomePage, String clientEnableDate,String clientDocViewerSequencing
	) throws InterruptedException {

		count++;
		if(!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data -- " +clientName +" set to NO " +count);

		openBrowser();
		clientSetupAdminLogin(CONFIG.getProperty("ClientSetupAdminURL"), CONFIG.getProperty("ClientSetupAdminUserName"), CONFIG.getProperty("ClientSetupAdminPassword"));

		Logger.debug("Executing Test Sirion Client Setup Admin Client/Supplier Creation for -- "+clientName);

		driver.get(CONFIG.getProperty("ClientSetupAdminURL"));

		fluentWaitMethod(driver.findElement(By.linkText("Client/Supplier Creation")));

		driver.findElement(By.linkText("Client/Supplier Creation")).click();

		fluentWaitMethod(driver.findElement(By.className("plus")));
	
		driver.findElement(By.className("plus")).click();

		fluentWaitMethod(driver.findElement(By.id("_name_id")));

		if (!clientName.equalsIgnoreCase("")) {
			driver.findElement(By.id("_name_id")).clear();
			driver.findElement(By.id("_name_id")).sendKeys(clientName.trim());
			}
		
		if (!clientTimezone.equalsIgnoreCase(""))
			new Select(driver.findElement(By.id("_primaryTimeZone.id_id"))).selectByVisibleText(clientTimezone.trim());
	
		if (!clientAlias.equalsIgnoreCase("")) {
			driver.findElement(By.id("_alias_id")).clear();
			driver.findElement(By.id("_alias_id")).sendKeys(clientAlias.trim());
			}

		if (clientSupplierType.equalsIgnoreCase("Yes")) {
			if (!driver.findElement(By.id("_supplierSideView_id")).isSelected())
				driver.findElement(By.id("_supplierSideView_id")).click();
			}
		else if (clientSupplierType.equalsIgnoreCase("No")) {
			if (driver.findElement(By.id("_supplierSideView_id")).isSelected())
				driver.findElement(By.id("_supplierSideView_id")).click();
			}

        if(!clientLanguagePack.equalsIgnoreCase("")){
            new Select(driver.findElement(By.id("_selectedLanguage.id_id"))).selectByVisibleText(clientLanguagePack);
        }
		
		if (!clientHomePage.equalsIgnoreCase(""))
			new Select(driver.findElement(By.id("_homePageView.id_id"))).selectByVisibleText(clientHomePage.trim());

		if (clientEnableDate.equalsIgnoreCase("Yes")) {
			if (!driver.findElement(By.id("_datetimeEnabled_id")).isSelected())
				driver.findElement(By.id("_datetimeEnabled_id")).click();
			}
		else if (clientEnableDate.equalsIgnoreCase("No")) {
			if (driver.findElement(By.id("_datetimeEnabled_id")).isSelected())
				driver.findElement(By.id("_datetimeEnabled_id")).click();
			}
		
		
		driver.findElement(By.className("showpopup")).click();


		if (clientName.equalsIgnoreCase("")) {
			WebElement e = driver.findElement(By.cssSelector("div._name_idformError.parentFormtblClient.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();

			Assert.assertEquals(entityErrors, "This field is required");
			Logger.debug("Client Name is Mandatory");

		    driver.get(CONFIG.getProperty("ClientSetupAdminURL"));
		    return;
		    }
			
		if (clientName.length() > 128) {
			WebElement e = driver.findElement(By.cssSelector("div._name_idformError.parentFormtblClient.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "Maximum 128 characters allowed");
			Logger.debug("For Client Name, -- Maximum 128 characters allowed");
			
		    driver.get(CONFIG.getProperty("ClientSetupAdminURL"));
		    return;
		    }
		
		if (clientTimezone.equalsIgnoreCase("")) {
			WebElement e = driver.findElement(By.cssSelector("div._primaryTimeZone_id_idformError.parentFormtblClient.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();

			Assert.assertEquals(entityErrors, "This field is required");
			Logger.debug("Client Timezone is Mandatory");

		    driver.get(CONFIG.getProperty("ClientSetupAdminURL"));
		    return;
		    }

		if (clientAlias.equalsIgnoreCase("") || clientAlias.startsWith("-")) {
			WebElement e = driver.findElement(By.cssSelector("div._alias_idformError.parentFormtblClient.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();

			Assert.assertEquals(entityErrors, "Only lower case alphanumeric and hiphen are allowed. It Should not start with a hiphen & consecutive hiphens are not allowed.");
			Logger.debug("For Client Alias -- Only lower case alphanumeric and hiphen are allowed. It Should not start with a hiphen & consecutive hiphens are not allowed.");

		    driver.get(CONFIG.getProperty("ClientSetupAdminURL"));
		    return;
		    }

		if (clientAlias.length() > 5) {
			WebElement e = driver.findElement(By.cssSelector("div._alias_idformError.parentFormtblClient.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();

			Assert.assertEquals(entityErrors, "Maximum 5 characters allowed");
			Logger.debug("For Client Alias, --- Maximum 5 characters allowed");
			
		    driver.get(CONFIG.getProperty("ClientSetupAdminURL"));
		    return;
		    }

		if (clientHomePage.equalsIgnoreCase("")) {
			WebElement e = driver.findElement(By.cssSelector("div._homePageView_id_idformError.parentFormtblClient.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();

			Assert.assertEquals(entityErrors, "This field is required");
			Logger.debug("Client Home Page View is Mandatory");

		    driver.get(CONFIG.getProperty("ClientSetupAdminURL"));
		    return;
		    }

		fluentWaitMethod(driver.findElement(By.id("commentdialog")).findElement(By.id("confirmsubmit")));
		driver.findElement(By.id("commentdialog")).findElement(By.id("confirmsubmit")).click();
		Thread.sleep(20000);

		if(driver.findElements(By.id("alertdialog")).size()!=0) {
			String entityErrors = driver.findElement(By.id("errors")).getText();

			if (entityErrors.contains("This name already exists in the system."))
				Logger.debug("For Client Name, " +clientName+" -- This name already exists in the system.");
			if (entityErrors.contains("This alias already exists in the system."))
				Logger.debug("For Client Alias, " +clientAlias+" -- This alias already exists in the system.");
									
		    driver.get(CONFIG.getProperty("ClientSetupAdminURL"));
			return;
			}


		driver.findElement(By.id("hrefElemId")).click();

		waitF.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("_s_com_sirionlabs_model_TblClient_name_name_id"))));

		String entityNameShowPage = driver.findElement(By.id("_s_com_sirionlabs_model_TblClient_name_name_id")).getText();

		Assert.assertEquals(entityNameShowPage, clientName);

		Logger.debug("Test Sirion Client Setup Admin Client/Supplier Creation for -- " + clientName + " is PASSED");
		driver.get(CONFIG.getProperty("ClientSetupAdminURL"));
		}

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(client_setup_admin_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(client_setup_admin_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
		}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(client_setup_admin_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
		}
		try {
			for (int i = 2; i <= client_setup_admin_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
				String testCaseID = client_setup_admin_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
			TestUtil.reportDataSetResult(client_setup_admin_suite_xls, "Test Cases", TestUtil.getRowNum(client_setup_admin_suite_xls, this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(client_setup_admin_suite_xls, "Test Cases", TestUtil.getRowNum(client_setup_admin_suite_xls, this.getClass().getSimpleName()), "FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(client_setup_admin_suite_xls, this.getClass().getSimpleName());
		}
	}