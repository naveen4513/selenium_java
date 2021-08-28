package office.sirion.suite.clientAdmin;

import java.io.IOException;

import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;

import org.openqa.selenium.By;
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

import testlink.api.java.client.TestLinkAPIResults;

public class AdvancedOrganization extends TestSuiteBase {
	String runmodes[]=null;
	static int count=-1;
	static boolean isTestPass=true;
	String testCaseID=null;
	String result=null;

	@BeforeTest
	public void checkTestSkip() {
		if(!TestUtil.isTestCaseRunnable(client_admin_suite_xls,this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
			throw new SkipException("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(client_admin_suite_xls, this.getClass().getSimpleName());
		}

	@Test (dataProvider="getTestData")
	public void AdvancedOrganizationTest (String clientName, String clientAddress, String clientApplyEncryption, String clientTimeZone, String clientIndustries,
			String clientAllowSameName, String clientBranding, String clientLanguage, String clientHomePageView, String clientEnableDate, String clientFunctions,
			String clientServices, String clientRegions, String clientCountries, String clientCurrencies, String clientReportingCurrency, String clientConversionType,
			String clientConversionRatesApplicable, String clientConversionMatrixFromDate, String clientConversionMatrixToDate
			) throws Throwable {

		count++;
		if(!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data -- " +clientName +" set to NO " +count);

		openBrowser();
		clientAdminLogin(CONFIG.getProperty("clientAdminURL"), CONFIG.getProperty("clientAdminUsername"), CONFIG.getProperty("clientAdminPassword"));

		Logger.debug("Executing Test Client Admin Advanced Organization Update For -- " + clientName);

		driver.get(CONFIG.getProperty("clientAdminURL"));
		//
		fluentWaitMethod(driver.findElement(By.linkText("Advanced Organization")));

		driver.findElement(By.linkText("Advanced Organization")).click();
		//
		fluentWaitMethod(driver.findElement(By.className("submit")).findElement(By.tagName("input")));

		driver.findElement(By.className("submit")).findElement(By.tagName("input")).click();
		//
		fluentWaitMethod(driver.findElement(By.id("_name_id")));

		if (!clientName.equalsIgnoreCase("")) {
			driver.findElement(By.id("_name_id")).clear();
			driver.findElement(By.id("_name_id")).sendKeys(clientName.trim());
			}

		if (!clientAddress.equalsIgnoreCase("")) {
			driver.findElement(By.id("_address_id")).clear();
			driver.findElement(By.id("_address_id")).sendKeys(clientAddress.trim());
			}

		if (clientApplyEncryption.equalsIgnoreCase("Yes")) {
			if (!driver.findElement(By.id("_clientEncryptionMetadata.applyEncryption_id")).isSelected())
				driver.findElement(By.id("_clientEncryptionMetadata.applyEncryption_id")).click();
			}

		if (!clientTimeZone.equalsIgnoreCase(""))
			new Select(driver.findElement(By.id("_primaryTimeZone.id_id"))).selectByVisibleText(clientTimeZone);

		if(!clientIndustries.equalsIgnoreCase("")) {
			for(String entityData : clientIndustries.split(";")) {
				new Select(driver.findElement(By.id("_industries_id"))).selectByVisibleText(entityData.trim());
				}
			}

		if (clientAllowSameName.equalsIgnoreCase("Yes")) {
			if (!driver.findElement(By.id("_duplicateDocumentName_id")).isSelected())
				driver.findElement(By.id("_duplicateDocumentName_id")).click();
			}

		if (clientBranding.equalsIgnoreCase("Yes")) {
			if (!driver.findElement(By.id("_allowBranding_id")).isSelected())
				driver.findElement(By.id("_allowBranding_id")).click();
			}

		if (driver.findElements(By.id("_selectedLanguage.id_id")).size()!=0) {
			if (!clientLanguage.equalsIgnoreCase(""))
				new Select(driver.findElement(By.id("_selectedLanguage.id_id"))).selectByVisibleText(clientLanguage);
			}

		if (!clientHomePageView.equalsIgnoreCase(""))
			new Select(driver.findElement(By.id("_homePageView.id_id"))).selectByVisibleText(clientHomePageView);

		if (clientEnableDate.equalsIgnoreCase("Yes")) {
			if (!driver.findElement(By.id("_datetimeEnabled_id")).isSelected())
				driver.findElement(By.id("_datetimeEnabled_id")).click();
			}

		if(!clientFunctions.equalsIgnoreCase("")) {
			for(String entityData : clientFunctions.split(";")) {
				new Select(driver.findElement(By.id("_contractTypes_id"))).selectByVisibleText(entityData.trim());
				}			
			if(!clientServices.equalsIgnoreCase("")) {
				for(String entityData : clientServices.split(";")) {
					new Select(driver.findElement(By.name("contractSubTypes"))).selectByVisibleText("  "+entityData.trim());
					}
				}
			}

		if(!clientRegions.equalsIgnoreCase("")) {
			for(String entityData : clientRegions.split(";")) {
				new Select(driver.findElement(By.id("_regions_id"))).selectByVisibleText(entityData.trim());
				}			
			if(!clientCountries.equalsIgnoreCase("")) {
				for(String entityData : clientCountries.split(";")) {
					new Select(driver.findElement(By.name("countries"))).selectByVisibleText("  "+entityData.trim());
							}
				}
			}

		if(!clientCurrencies.equalsIgnoreCase("")) {
			for(String entityData : clientCurrencies.split(";")) {
				new Select(driver.findElement(By.id("_clientCurrencyIds_id"))).selectByVisibleText(entityData.trim());
				}
			if(!clientReportingCurrency.equalsIgnoreCase(""))
				new Select(driver.findElement(By.id("_primaryCurrency_id"))).selectByVisibleText(clientReportingCurrency.trim());
			}

		if (!clientConversionType.equalsIgnoreCase(""))
			new Select(driver.findElement(By.id("_conversionRuleId_id"))).selectByVisibleText(clientConversionType);

		if(!clientConversionRatesApplicable.equalsIgnoreCase("")) {
			new Select(driver.findElement(By.id("_rateCardList[0].rateCard.id_id"))).selectByVisibleText(clientConversionRatesApplicable);

			if (!clientConversionMatrixFromDate.equalsIgnoreCase("")) {
		    	driver.findElement(By.id("c_com_sirionlabs_model_ratecard_startdate_id")).click();
		        String[] entityDate = clientConversionMatrixFromDate.split("-");

		        String entityMonth = driver.findElement(By.xpath(".//*[@id='ui-datepicker-div']/div/div/span")).getText();
		        while (!entityMonth.equalsIgnoreCase(entityDate[0])) {
		          driver.findElement(By.xpath(".//*[@id='ui-datepicker-div']/div/a[2]/span")).click();
		          entityMonth = driver.findElement(By.xpath(".//*[@id='ui-datepicker-div']/div/div/span")).getText();
		          }

		        new Select(driver.findElement(By.xpath(".//*[@id='ui-datepicker-div']/div/div/select"))).selectByVisibleText(entityDate[2]);

		        driver.findElement(By.linkText(entityDate[1])).click();
		        }

			if (!clientConversionMatrixToDate.equalsIgnoreCase("")) {
		    	driver.findElement(By.id("c_com_sirionlabs_model_ratecard_enddate_id")).click();
		        String[] entityDate = clientConversionMatrixToDate.split("-");

		        String entityMonth = driver.findElement(By.xpath(".//*[@id='ui-datepicker-div']/div/div/span")).getText();
		        while (!entityMonth.equalsIgnoreCase(entityDate[0])) {
		          driver.findElement(By.xpath(".//*[@id='ui-datepicker-div']/div/a[2]/span")).click();
		          entityMonth = driver.findElement(By.xpath(".//*[@id='ui-datepicker-div']/div/div/span")).getText();
		          }

		        new Select(driver.findElement(By.xpath(".//*[@id='ui-datepicker-div']/div/div/select"))).selectByVisibleText(entityDate[2]);

		        driver.findElement(By.linkText(entityDate[1])).click();
		        }
		    }

		    if(driver.findElements(By.id("ui-id-8")).size()!=0){
                String entityErrors = driver.findElement(By.className("success-icon")).getText();

                Assert.assertEquals(entityErrors, "You have already entered values for the date range that you just selected. New Values will override the values entered in the past.");
                Logger.debug("For Advanced Organization, " +clientName+" --  You have already entered values for the date range that you just selected. New Values will override the values entered in the past.");

                driver.findElement(By.xpath("//button[@type='button']")).click();
            }


		driver.findElement(By.id("proceed")).click();
		Thread.sleep(2000);

		driver.findElement(By.id("confirmsubmit")).click();
		//

		if(driver.findElements(By.id("alertdialog")).size()!=0) {
			String entityErrors = driver.findElement(By.id("errors")).getText();

			Assert.assertTrue(entityErrors.contains("This name already exists in the system."));
			Logger.debug("For Advanced Organization, " +clientName+" -- This name already exists in the system.");

			driver.get(CONFIG.getProperty("sirionAdminURL"));
			return;
			}

		String entityTypeNameShowPage = driver.findElement(By.id("_s_com_sirionlabs_model_TblClient_name_name_id")).getText();
        Assert.assertEquals(entityTypeNameShowPage, clientName, "Client Name at Advanced Organization show page is -- " +entityTypeNameShowPage+ " instead of -- " +clientName);

		Logger.debug("Test Client Admin Advanced Organization Update For -- " + clientName + " is PASSED");
        driver.get(CONFIG.getProperty("clientAdminURL"));
		}

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
	   takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());
	   
	   if(testResult.getStatus()==ITestResult.SKIP)
	      TestUtil.reportDataSetResult(client_admin_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
	   else if(testResult.getStatus()==ITestResult.FAILURE) {
	      isTestPass=false;
	      TestUtil.reportDataSetResult(client_admin_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
	      result= "Fail";
	      }
	   else if (testResult.getStatus()==ITestResult.SUCCESS) {
	      TestUtil.reportDataSetResult(client_admin_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
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
			TestUtil.reportDataSetResult(client_admin_suite_xls, "Test Cases", TestUtil.getRowNum(client_admin_suite_xls, this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(client_admin_suite_xls, "Test Cases", TestUtil.getRowNum(client_admin_suite_xls, this.getClass().getSimpleName()), "FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(client_admin_suite_xls, this.getClass().getSimpleName());
		}
	}