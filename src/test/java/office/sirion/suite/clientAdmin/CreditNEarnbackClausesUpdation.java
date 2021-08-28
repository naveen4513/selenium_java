package office.sirion.suite.clientAdmin;

import office.sirion.util.PostgreSQLJDBC;
import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.*;
import testlink.api.java.client.TestLinkAPIResults;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CreditNEarnbackClausesUpdation extends TestSuiteBase {
	String runmodes[]=null;
	static int count=-1;
	static boolean fail=true;
	static boolean skip=false;
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
	public void CreditNEarnbackClausesUpdationT (String CETitle,String CEName,String CEType,String CEDescription,String CEMinMax,String CEActive,String CEMeasurementType,
                                              String CECreditOption,String CEPerformanceCriteria,String CEOperand1,String CEOperand2,String CENoOfOccurence,
                                              String CEFromLastAppliedCredit,String CESLMetStatus,String CENoOfInstances,String CEAddtionalCriteria,
                                              String CEPercentage) throws InterruptedException, SQLException {
		count++;
		if(!runmodes[count].equalsIgnoreCase("Y")) {
			skip=true;
			throw new SkipException("Runmode for Test Data -- " +CEName +" set to NO " +count);
			}
		
		openBrowser();
		clientAdminLogin(CONFIG.getProperty("clientAdminURL"), CONFIG.getProperty("clientAdminUsername"), CONFIG.getProperty("clientAdminPassword"));
		
		Logger.debug("Executing Client Admin Credit And Clause Creation Test -- "+CEName);
		
		driver.get(CONFIG.getProperty("clientAdminURL"));
		fluentWaitMethod(driver.findElement(By.linkText("Credit And Earnback Clauses")));
		
		driver.findElement(By.linkText("Credit And Earnback Clauses")).click();

		String entityID=driver.findElement(By.xpath("//tbody//tr[6]//td[1]/a")).getAttribute("href");
		String[] id = entityID.split("/");

		String a = id[6].substring(0,4);

        PostgreSQLJDBC pg = new PostgreSQLJDBC();

        if(pg.doSelect("select id from contract_credit_earnback_clauses  where clause_id = "+id[6].substring(0,4)).size()==0) {

            driver.findElement(By.xpath("//a[contains(text(),'" + CETitle + "')]")).click();

            try {

                fluentWaitMethod(driver.findElement(By.name("name")));

                if (!CEName.equalsIgnoreCase("")) {
                    driver.findElement(By.name("name")).clear();
                    driver.findElement(By.name("name")).sendKeys(CEName.trim() + getSaltString());
                }

                if (!CEType.equalsIgnoreCase("")) {
                    new Select(driver.findElement(By.name("creditMode"))).selectByVisibleText(CEType);
                }

                if (!CEDescription.equalsIgnoreCase("")) {
                    driver.findElement(By.name("description")).clear();
                    driver.findElement(By.name("description")).sendKeys(CEDescription);
                    driver.findElement(By.xpath("//a[@class='ng-binding'][contains(text(),'CLAUSE')]")).click();
                }

                if (CEActive.equalsIgnoreCase("Yes")) {
                    driver.findElement(By.name("active")).click();
                }
                if (!CEMinMax.equalsIgnoreCase("")) {
                    new Select(driver.findElement(By.name("minMax"))).selectByVisibleText(CEMinMax);
                }
                if (!CEMeasurementType.equalsIgnoreCase("")) {
                    new Select(driver.findElement(By.name("measurementType"))).selectByVisibleText(CEMeasurementType);
                    if (CEMeasurementType.equalsIgnoreCase("SL Met Status")) {
                        String[] SLMetStatus = CESLMetStatus.split(";");
                        for (String Status : SLMetStatus) {
                            new Select(driver.findElement(By.name("slMetStatus"))).selectByVisibleText(Status);
                        }
                    }
                    if (CEMeasurementType.equalsIgnoreCase("Actual Performance")) {
                        new Select(driver.findElement(By.name("creditOption"))).selectByVisibleText(CECreditOption);
                        new Select(driver.findElement(By.name("performanceCriteria"))).selectByVisibleText(CEPerformanceCriteria);
                        new Select(driver.findElement(By.name("operand1"))).selectByVisibleText(CEOperand1);
                    }
                    if (CEMeasurementType.equalsIgnoreCase("Average Performance")) {
                        new Select(driver.findElement(By.name("creditOption"))).selectByVisibleText("After X Occurences");
                        new Select(driver.findElement(By.name("performanceCriteria"))).selectByVisibleText(CEPerformanceCriteria);
                        new Select(driver.findElement(By.name("operand1"))).selectByVisibleText(CEOperand1);
                    }
                    if (!CECreditOption.equalsIgnoreCase("")) {
                        driver.findElement(By.name("creditOption")).sendKeys(CECreditOption);
                    }
                    if (CECreditOption.equalsIgnoreCase("After X Occurences in Y Instances")) {
                        driver.findElement(By.name("instanceCount")).sendKeys(convertStringToInteger(CENoOfInstances));
                    }
                    if (CEPerformanceCriteria.equalsIgnoreCase("Between")) {
                        driver.findElement(By.name("operand2")).sendKeys(CEOperand2);
                    }
                    if (!CENoOfOccurence.equalsIgnoreCase("")) {
                        driver.findElement(By.name("occurenceCount")).sendKeys(convertStringToInteger(CENoOfOccurence));
                    }
                    if (CEFromLastAppliedCredit.equalsIgnoreCase("Yes")) {
                        driver.findElement(By.name("fromLastAppliedCredit")).click();
                    }
                    if (CEOperand1.equalsIgnoreCase("Other Benchmark")) {
                        driver.findElement(By.name("percentage")).sendKeys(convertStringToInteger(CEPercentage));
                    }
                }
                if (!convertStringToInteger(CEAddtionalCriteria).equalsIgnoreCase("")) {
                    for (int i = 1; i <= Integer.parseInt(convertStringToInteger(CEAddtionalCriteria)); i++) {
                        driver.findElement(By.xpath("//fieldset[@class='fieldset_3931']//fieldset[1]//input[2]")).click();
                    }
                }

                driver.findElement(By.xpath("//button[contains(text(),'Save')]")).click();


                if (CEMinMax.equalsIgnoreCase("")) {
                    fail = true;

                    try {
                        driver.findElement(By.xpath("//select[@ng-class='{errorClass: errors.fieldErrors.minMax.message}']"));
                        Logger.debug("Min Max field of Credit And Earnback Clause is Mandatory");
                    } catch (NoSuchElementException NE) {
                        Logger.debug("No Such Validation Occurs");
                        fail = false;
                        driver.get(CONFIG.getProperty("clientAdminURL"));
                        return;
                    }
                    fail = true;
                    driver.get(CONFIG.getProperty("clientAdminURL"));
                    return;
                }

                if (CEName.length() > 128) {
                    fail = true;
                    try {
                        driver.findElement(By.xpath("//select[@ng-class='{errorClass: errors.fieldErrors.name.message}']"));
                        Logger.debug("Name field of Credit And Earnback Clause has length more than 128");
                    } catch (NoSuchElementException NE) {
                        Logger.debug("No Such Validation Occurs");
                        fail = false;
                        driver.get(CONFIG.getProperty("clientAdminURL"));
                        return;
                    }
                    fail = true;
                    driver.get(CONFIG.getProperty("clientAdminURL"));
                    return;
                }

                if (CEName.equalsIgnoreCase("")) {
                    fail = true;
                    try {
                        driver.findElement(By.xpath("//select[@ng-class='{errorClass: errors.fieldErrors.name.message}']"));
                        Logger.debug("Name field of Credit And Earnback Clause is Required Field");
                    } catch (NoSuchElementException NE) {
                        Logger.debug("No Such Validation Occurs");
                        fail = false;
                        driver.get(CONFIG.getProperty("clientAdminURL"));
                        return;
                    }
                    fail = true;
                    driver.get(CONFIG.getProperty("clientAdminURL"));
                    return;
                }

                if (CEType.equalsIgnoreCase("")) {
                    fail = true;
                    try {
                        driver.findElement(By.xpath("//select[@ng-class='{errorClass: errors.fieldErrors.creditMode.message}']"));
                        Logger.debug("Credit Type field of Credit And Earnback Clause is Required Field");
                    } catch (NoSuchElementException NE) {
                        Logger.debug("No Such Validation Occurs");
                        fail = false;
                        driver.get(CONFIG.getProperty("clientAdminURL"));
                        return;
                    }
                    fail = true;
                    driver.get(CONFIG.getProperty("clientAdminURL"));
                    return;
                }

                try {

                    String message = driver.findElement(By.xpath("//span[@class='success-icon']")).getText();
                    if (message.contains("Server Error")) {
                        Logger.debug("Unable to Create Credit and Earnback Clause, Server Error is displayed");
                        fail = false;
                        driver.get(CONFIG.getProperty("clientAdminURL"));
                        return;
                    } else if (message.contains(" Created Successfully.")) {
                        String CEClientID = driver.findElement(By.xpath("//a[@id='hrefElemId']")).getText();
                        Logger.debug(CEClientID + " Has been created successfully");
                        driver.findElement(By.xpath("//a[@id='hrefElemId']")).click();
                        //
                    }

                } catch (NoSuchElementException NSE) {
                    Logger.debug("Entity creation process is not successful");
                }

                try {
                    driver.findElement(By.xpath("//span[@class='errorLabel']"));
                    List<WebElement> e = driver.findElements(By.xpath("//li[@class='ng-binding ng-scope']"));
                    for (WebElement s : e) {
                        String error = s.getText();
                        if (error.contains("Clause Name Cannot be Duplicate")) {
                            Logger.debug("Clause Name Cannot be Duplicate");
                        } else if (error.contains("Description")) {
                            Logger.debug("Description is Required field");
                        }
                    }
                    fail = true;
                    driver.get(CONFIG.getProperty("clientAdminURL"));
                    return;
                } catch (NoSuchElementException e1) {
                    Logger.debug("No Such error Message is displayed");
                }
                String clauseNameShowPage = driver.findElement(By.id("//span[@id='elem_11866']//span")).getText();
                Assert.assertEquals(clauseNameShowPage, CEName, "Action Type at show page is -- " +clauseNameShowPage+ " instead of -- " +CEName);

                String clauseTypeShowPage = driver.findElement(By.id("//span[@id='elem_11867']//span")).getText();
                Assert.assertEquals(clauseTypeShowPage, CEType, "Action Type Name at show page is -- " +clauseTypeShowPage+ " instead of -- " +CEType);

                String clauseDescriptionShowPage = driver.findElement(By.id("//div[@class='relative truncate content-left']//span")).getText();
                Assert.assertEquals(clauseDescriptionShowPage, CEDescription, "Action Type Name at show page is -- " +clauseDescriptionShowPage+ " instead of -- " +CEDescription);

                String clauseMinMaxShowPage = driver.findElement(By.id("//span[@id='elem_11869']/span")).getText();
                Assert.assertEquals(clauseMinMaxShowPage, CEMinMax, "Action Type Name at show page is -- " +clauseMinMaxShowPage+ " instead of -- " +CEMinMax);

                String clauseActiveShowPage = driver.findElement(By.id("//span[@id='elem_11870']//span")).getText();
                Assert.assertEquals(clauseActiveShowPage, CEActive, "Action Type Name at show page is -- " +clauseActiveShowPage+ " instead of -- " +CEActive);

                String clauseMeasurementTypeShowPage = driver.findElement(By.id("//span[@id='elem_11871']//span")).getText();
                Assert.assertEquals(clauseMeasurementTypeShowPage, CEMeasurementType, "Action Type Name at show page is -- " +clauseMeasurementTypeShowPage+ " instead of -- " +CEMeasurementType);

                fail = false;
                driver.get(CONFIG.getProperty("clientAdminURL"));
            } catch (NoSuchElementException e) {
                Logger.debug("Credit And Earnback Clause does not have Edit button, so does not proceed");
                fail = true;
                driver.get(CONFIG.getProperty("clientAdminURL"));
            }
        }

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
		if(isTestPass)
			TestUtil.reportDataSetResult(client_admin_suite_xls, "Test Cases", TestUtil.getRowNum(client_admin_suite_xls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(client_admin_suite_xls, "Test Cases", TestUtil.getRowNum(client_admin_suite_xls,this.getClass().getSimpleName()), "FAIL");
		}
	
	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(client_admin_suite_xls, this.getClass().getSimpleName());
		}
	}