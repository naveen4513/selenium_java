package office.sirion.suite.sirionAdmin;

import java.io.IOException;

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

import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;
import testlink.api.java.client.TestLinkAPIResults;

public class UpdateFrequency extends TestSuiteBase {
	String runmodes[]=null;
	static int count=-1;
	static boolean isTestPass=true;
	String testCaseID=null;
	String result=null;

	@BeforeTest
	public void checkTestSkip() {
		if(!TestUtil.isTestCaseRunnable(sirion_admin_suite_xls,this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
			throw new SkipException("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(sirion_admin_suite_xls, this.getClass().getSimpleName());
		}

	@Test (dataProvider="getTestData")
	public void UpdateFrequencyTest (String frequencyName, String frequencyNameUpdate, String frequencyType, String frequencyRepeats, String frequencyRepeatEvery,
			String frequencyRepeatOn, String frequencyWeeklyRepeatOnAdvancedDay, String frequencyAdvancedFirstDropdown, String frequencyAdvancedSecondDropdown,
			String frequencyAdvanceCreation, String frequencyBelowMonthly) throws InterruptedException {
		count++;
		if(!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data -- " +frequencyName +" set to NO " +count);

		openBrowser();
		sirionAdminLogin(CONFIG.getProperty("sirionAdminURL"), CONFIG.getProperty("sirionAdminUsername"), CONFIG.getProperty("sirionAdminPassword"));
		
		Logger.debug("Executing Sirion Admin Frequency Update Test -- "+frequencyName);
		
		driver.get(CONFIG.getProperty("sirionAdminURL"));
		fluentWaitMethod(driver.findElement(By.linkText("Frequency")));

		driver.findElement(By.linkText("Frequency")).click();
		fluentWaitMethod(driver.findElement(By.className("dataTables_filter")).findElement(By.tagName("label")).findElement(By.tagName("input")));
        
		driver.findElement(By.className("dataTables_filter")).findElement(By.tagName("label")).findElement(By.tagName("input")).sendKeys(frequencyName);
		Thread.sleep(2000);
        
        driver.findElement(By.xpath(".//*[@id='l_com_sirionlabs_model_MasterFrequency']/tbody/tr/td[1]/a/div[@sort='"+ frequencyName +"']")).click();

        String entityTypeShowPage = driver.findElement(By.id("_s_com_sirionlabs_model_MasterFrequency_name_name_id")).getText();
        Assert.assertEquals(entityTypeShowPage, frequencyName, "Frequency Name at show page is -- " +entityTypeShowPage+ " instead of -- " +frequencyName);
        
        driver.findElement(By.className("submit")).findElement(By.tagName("input")).click();
		fluentWaitMethod(driver.findElement(By.id("_name_id")));
        
		if (!frequencyNameUpdate.equalsIgnoreCase("")) {
			driver.findElement(By.id("_name_id")).clear();
			driver.findElement(By.id("_name_id")).sendKeys(frequencyNameUpdate.trim());
			}
		
		if (!frequencyType.equalsIgnoreCase("")) {
			new Select(driver.findElement(By.id("_frequencyType.id_id"))).selectByVisibleText(frequencyType);
			}

		driver.findElement(By.id("_recurrenceData_id")).click();

		if (!frequencyRepeats.equalsIgnoreCase("")) {
			new Select(driver.findElement(By.name("rirtemplate"))).selectByVisibleText(frequencyRepeats);
			Thread.sleep(2000);
			
			if (!frequencyRepeats.equalsIgnoreCase("Repeat Once")) {
				Double temp_frequencyRepeatEvery_double = Double.parseDouble(frequencyRepeatEvery);
				int temp_frequencyRepeatEvery_int = temp_frequencyRepeatEvery_double.intValue();
				String temp_frequencyRepeatEvery_string = Integer.toString(temp_frequencyRepeatEvery_int);	

				if (!frequencyRepeatEvery.equalsIgnoreCase("")) {					
					if (frequencyRepeats.equalsIgnoreCase("Daily")) {
						driver.findElement(By.id("ridailyinterval")).clear();
						driver.findElement(By.id("ridailyinterval")).sendKeys(temp_frequencyRepeatEvery_string);
						}
					else if (frequencyRepeats.equalsIgnoreCase("Weekly")) {
						driver.findElement(By.id("riweeklyinterval")).clear();
						driver.findElement(By.id("riweeklyinterval")).sendKeys(temp_frequencyRepeatEvery_string);

						if(frequencyRepeatOn.equalsIgnoreCase("Advanced")) {
							driver.findElement(By.id("recurrenceDataweeklytype:WEEKDAYOFWEEK")).click();
							String[] frequencyData = frequencyWeeklyRepeatOnAdvancedDay.split(";");
							for(int i=0; i<frequencyData.length; i++) {
								if(frequencyData[i].trim().equalsIgnoreCase("Sunday")) {
									driver.findElement(By.id("recurrenceDataweeklyWeekdaysSU")).click();
									}
								if(frequencyData[i].trim().equalsIgnoreCase("Monday")) {
									driver.findElement(By.id("recurrenceDataweeklyWeekdaysMO")).click();
									}
								if(frequencyData[i].trim().equalsIgnoreCase("Tuesday")) {
									driver.findElement(By.id("recurrenceDataweeklyWeekdaysTU")).click();
									}
								if(frequencyData[i].trim().equalsIgnoreCase("Wednesday")) {
									driver.findElement(By.id("recurrenceDataweeklyWeekdaysWE")).click();
									}
								if(frequencyData[i].trim().equalsIgnoreCase("Thursday")) {
									driver.findElement(By.id("recurrenceDataweeklyWeekdaysTH")).click();
									}
								if(frequencyData[i].trim().equalsIgnoreCase("Friday")) {
									driver.findElement(By.id("recurrenceDataweeklyWeekdaysFR")).click();
									}
								if(frequencyData[i].trim().equalsIgnoreCase("Saturday")) {
									driver.findElement(By.id("recurrenceDataweeklyWeekdaysSA")).click();
									}
								}
							}
						}
					else if (frequencyRepeats.equalsIgnoreCase("Monthly")) {
						driver.findElement(By.name("rimonthlyinterval")).clear();
						driver.findElement(By.name("rimonthlyinterval")).sendKeys(temp_frequencyRepeatEvery_string);
						
						if(frequencyRepeatOn.equalsIgnoreCase("Date")) {
							driver.findElement(By.id("recurrenceDatamonthlytype:WEEKDAYOFMONTH")).click();
							}

						if(frequencyRepeatOn.equalsIgnoreCase("Advanced")) {
							driver.findElement(By.id("recurrenceDatamonthlytype:MONTHADVANCED")).click();
							if (!frequencyAdvancedFirstDropdown.equalsIgnoreCase("")) {
								new Select(driver.findElement(By.id("rimonthlyweekdayofmonthindexadvanced"))).selectByVisibleText(frequencyAdvancedFirstDropdown);
								}
							if (!frequencyAdvancedSecondDropdown.equalsIgnoreCase("")) {
								new Select(driver.findElement(By.id("rimonthlyweekdayofmonthadvancedFinal"))).selectByVisibleText(frequencyAdvancedSecondDropdown);
								}
							}
						}
					
					else if (frequencyRepeats.equalsIgnoreCase("Yearly")) {
						driver.findElement(By.id("riyearlyinterval")).clear();
						driver.findElement(By.id("riyearlyinterval")).sendKeys(temp_frequencyRepeatEvery_string);
						
						if(frequencyRepeatOn.equalsIgnoreCase("Date")) {
							driver.findElement(By.id("recurrenceDatayearlytype:DATEOFYEAR")).click();
							}

						if(frequencyRepeatOn.equalsIgnoreCase("Advanced")) {
							driver.findElement(By.id("recurrenceDatayearlytype:YEARADVANCED")).click();
							if (!frequencyAdvancedFirstDropdown.equalsIgnoreCase("")) {
								new Select(driver.findElement(By.id("riyearlyweekdayofmonthindexadvanced"))).selectByVisibleText(frequencyAdvancedFirstDropdown);
								}
							if (!frequencyAdvancedSecondDropdown.equalsIgnoreCase("")) {
								new Select(driver.findElement(By.id("riyearlyweekdayofmonthadvancedFinal"))).selectByVisibleText(frequencyAdvancedSecondDropdown);
								}
							}
						}
					Thread.sleep(2000);
					}
				}
			}
		
		driver.findElement(By.className("risavebutton")).click();
		Thread.sleep(2000);		
		
		if (!frequencyAdvanceCreation.equalsIgnoreCase("")) {
			Double temp_frequencyAdvanceCreation_double = Double.parseDouble(frequencyAdvanceCreation);
			int temp_frequencyAdvanceCreation_int = temp_frequencyAdvanceCreation_double.intValue();
			String temp_frequencyAdvanceCreation_string = Integer.toString(temp_frequencyAdvanceCreation_int);
			
			driver.findElement(By.id("_advanceCreation_id")).clear();
			driver.findElement(By.id("_advanceCreation_id")).sendKeys(temp_frequencyAdvanceCreation_string);
			}

		if (frequencyBelowMonthly.equalsIgnoreCase("Yes")) {
			driver.findElement(By.id("_belowMonthly_id")).click();
			}

		driver.findElement(By.id("proceed")).click();
		//
		
		if (frequencyNameUpdate.length() > 128) {
			WebElement e = driver.findElement(By.cssSelector("div._name_idformError.parentFormmasterFrequency.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "Maximum 128 characters allowed");
			Logger.debug("For Frequency Name, --- Maximum 128 characters allowed");
			
		    driver.get(CONFIG.getProperty("sirionAdminURL"));
		    return;
		    }

		if (frequencyAdvanceCreation.length() > 8) {
			WebElement e = driver.findElement(By.cssSelector("div._advanceCreation_idformError.parentFormmasterFrequency.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "Maximum 8 characters allowed");
			Logger.debug("For Frequency Advance Creation, --- Maximum 8 characters allowed");
			
		    driver.get(CONFIG.getProperty("sirionAdminURL"));
		    return;
		    }
		
		if(driver.findElements(By.id("errorDialog")).size()!=0 || driver.findElements(By.id("alertdialog")).size()!=0) {
			String entityErrors = driver.findElement(By.id("errors")).getText();
			
			Assert.assertEquals(entityErrors, "This name already exists in the system.");
			Logger.debug("For Frequency Name, " +frequencyNameUpdate+" --- This name already exists in the system.");
									
			driver.get(CONFIG.getProperty("sirionAdminURL"));
			return;
			}

        String entityTypeUpdateShowPage = driver.findElement(By.id("_s_com_sirionlabs_model_MasterFrequency_name_name_id")).getText();
        Assert.assertEquals(entityTypeUpdateShowPage, frequencyNameUpdate, "Frequency Name at show page is -- " +entityTypeUpdateShowPage+ " instead of -- " +frequencyNameUpdate);
        
        Logger.debug("Frequency updated successfully, with Name -- " +frequencyNameUpdate);
        
		driver.get(CONFIG.getProperty("sirionAdminURL"));
		}
	
	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
	   takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());
	   
	   if(testResult.getStatus()==ITestResult.SKIP)
	      TestUtil.reportDataSetResult(sirion_admin_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
	   else if(testResult.getStatus()==ITestResult.FAILURE) {
	      isTestPass=false;
	      TestUtil.reportDataSetResult(sirion_admin_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
	      result= "Fail";
	      }
	   else if (testResult.getStatus()==ITestResult.SUCCESS) {
	      TestUtil.reportDataSetResult(sirion_admin_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
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
			TestUtil.reportDataSetResult(sirion_admin_suite_xls, "Test Cases", TestUtil.getRowNum(sirion_admin_suite_xls, this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(sirion_admin_suite_xls, "Test Cases", TestUtil.getRowNum(sirion_admin_suite_xls, this.getClass().getSimpleName()), "FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(sirion_admin_suite_xls, this.getClass().getSimpleName());
		}
	}