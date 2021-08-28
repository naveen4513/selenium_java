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

public class CreditNEarnbackClauseAuditLog extends TestSuiteBase {
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
	public void CreditNEarnbackClauseAuditLogT (String CETitle,String CEName,String CEType,String CEDescription,String CEMinMax,String CEActive) throws InterruptedException, SQLException {
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
                fluentWaitMethod(driver.findElement(By.linkText("AUDIT LOG")));
                driver.findElement(By.linkText("AUDIT LOG")).click();
                //
                Logger.debug("Executing Test Case Application Update with Title -- " + CEName + " -- is STARTED");

                fluentWaitMethod(driver.findElement(By.className("dataTables_scrollBody")));
                WebElement elementOddRowSelected = driver.findElement(By.id("table_3935")).findElements(By.className("odd")).get(0);

                Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_action_name")).getText(), "Updated");

                Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_requested_by")).getText(), driver.findElement(By.xpath("//div[@class='username_sl ng-binding']")).getText());

                Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_completed_by")).getText(), driver.findElement(By.xpath("//div[@class='username_sl ng-binding']")).getText());

                Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_comment")).getText(), "No");

                Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_document")).getText(), "No");

                elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_history")).findElement(By.className("serverHistoryView")).click();
                fluentWaitMethod(driver.findElement(By.id("historyDataTable")));

                List<WebElement> auditRowElementList = driver.findElement(By.id("historyDataTable")).findElement(By.id("data")).findElements(By.tagName("tr"));
                for (int iAuditRow = 0; iAuditRow < auditRowElementList.size(); iAuditRow++) {
                    for (int jAuditCol = 0; jAuditCol < auditRowElementList.get(0).findElements(By.tagName("td")).size(); jAuditCol++) {
                        // APPLICATION GROUP - BASIC INFORMATION
                        if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Clause Name")) {
                            List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
                            Assert.assertEquals(elementDataList.get(3).getText(), CEName);
                        }else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Clause Type")) {
                            List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
                            Assert.assertEquals(elementDataList.get(3).getText(), CEType);
                        }else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Description")) {
                            List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
                            Assert.assertEquals(elementDataList.get(3).getText(), CEDescription);
                        }else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Min/Max")) {
                            List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
                            Assert.assertEquals(elementDataList.get(3).getText(), CEActive);
                        }else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Active")) {
                            List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
                            Assert.assertEquals(elementDataList.get(3).getText(), CEMinMax);
                        }
                    }
                }

                Logger.debug("Executing Test Case Application Update with Title -- " + CEName + " -- is PASSED");
                driver.get(CONFIG.getProperty("endUserURL"));
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