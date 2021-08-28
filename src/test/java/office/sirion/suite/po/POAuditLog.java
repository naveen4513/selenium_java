package office.sirion.suite.po;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import office.sirion.util.DatePicker_Enhanced;
import office.sirion.util.TestUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

public class POAuditLog extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean fail = false;
	static boolean skip = false;
	static boolean isTestPass = true;
	static DateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
	static Date date = new Date();

	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(po_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(po_suite_xls, this.getClass().getSimpleName());
		}
	
	@Test(dataProvider = "getTestData")
	public void POAuditLogTest (String poPONumber, String poName, String poRequisitionNumber, String poTrackingNumber, 
			String poDescription, String poContractingEntity, String poServiceSubCategory, String poTimeZone, 
			String poCurrency, String poActive, String poTier, String poBusinessUnit, String poDepartment, String poCostCenter, 
			String poStartDate, String poEndDate, String poCountries, String poStates, String poFunctions, String poServices, 
			String poPOTotal, String poExpectedBurn, String poBurn, String poAvailable, String poComments, String poActualDate, String poRequestedBy,
			String poChangeRequest, String poUploadFile, String poParent) throws InterruptedException {
		
		count++;
		if (!runmodes[count].equalsIgnoreCase("Y")) {
			skip = true;
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);
			}
	
		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
	    String logged_in_user = locateElementBy("po_communication_logged_in_user_full_name").getText(); driver.get(CONFIG.getProperty("endUserURL"));
	    
        Logger.debug("Executing Test Case PO Update with Name --- " + poName);
	    
	    driver.get(CONFIG.getProperty("endUserURL"));
		fluentWaitMethod(locateElementBy("po_quick_link"));

		locateElementBy("po_quick_link").click();
		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));
		
		new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText("100");

		
		if (!poParent.equalsIgnoreCase("")) {
			driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr[@role='row']/td[contains(.,'" + poParent + "')]/preceding-sibling::td[2]/a")).click();
			} else {
				locateElementBy("po_listing_page_first_link").click();
				}

		fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
	    Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
	    driver.findElement(By.xpath("//button[contains(.,'Edit')]")).click();
	    fluentWaitMethod(locateElementBy("po_create_page_po_number_textbox"));
		
		// Purchase Order - Create Page - BASIC INFORMATION		
		addFieldValue("po_create_page_po_number_textbox", poPONumber+getSaltString());
	    
		/*addFieldValue("po_create_page_name_textbox", poName+getSaltString());
		
		addFieldValue("po_create_page_requisition_number_textbox", poRequisitionNumber+getSaltString());
		
		addFieldValue("po_create_page_tracking_number_textbox", poTrackingNumber);
		
		addFieldValue("po_create_page_description_textarea", poDescription);
		
		addFieldValue("po_create_page_contracting_entity_dropdown", poContractingEntity);
		
		addFieldValue("po_create_page_service_sub_category_dropdown", poServiceSubCategory);
		
		addFieldValue("po_create_page_timezone_dropdown", poTimeZone);
		
		try {
	    	if (driver.findElement(By.className("success-icon")).getText().contains("Current Date is different for the selected Time Zone"))
	    		driver.findElement(By.xpath(".//button[contains(.,'OK')]")).click();
	    	} catch (Exception e) {
	    			}
		
		addFieldValue("po_create_page_currency_dropdown", poCurrency);
		
		addFieldValue("po_create_page_active_checkbox", poActive);
		
		addFieldValue("po_create_page_tier_dropdown", poTier);
		
		// Purchase Order - Create Page - ORGANIZATION INFORMATION		
		addFieldValue("po_create_page_business_unit_multiselect", poBusinessUnit);
		
		addFieldValue("po_create_page_department_multiselect", poDepartment);
		
		addFieldValue("po_create_page_cost_center_multiselect", poCostCenter);*/
		
		// Purchase Order - Show Page - IMPORTANT DATES
		
		DatePicker_Enhanced date = new DatePicker_Enhanced();
        date.selectCalendar(poStartDate,"startDate");
		date.selectCalendar(poEndDate,"endDate");
		
		// Purchase Order - Create Page - GEOGRAPHY
		addFieldValue("po_create_page_country_multiselect", poCountries);
		
		addFieldValue("po_create_page_state_multiselect", poStates);
		
		// Purchase Order - Create Page - FUNCTION
		addFieldValue("po_create_page_function_multiselect", poFunctions);
		
		addFieldValue("po_create_page_service_multiselect", poServices);
		
		// Purchase Order - Create Page - FINANCIAL INFORMATION
		addFieldValue("po_create_page_po_total_textbox", poPOTotal);
		
		addFieldValue("po_create_page_expected_po_burn_textbox", poExpectedBurn);
		
		addFieldValue("po_create_page_po_burn_textbox", poBurn);
		
		addFieldValue("po_create_page_po_available_textbox", poAvailable);

		// Governance Body - Create Page - COMMENTS AND ATTACHMENTS
				addFieldValue("entity_create_page_comments_textarea", poComments);
				    
				addFieldValue("entity_create_page_requested_by_dropdown", poRequestedBy);
				  
				date.selectCalendar(poActualDate,"actualDate");
				
				addFieldValue("entity_create_page_change_request_dropdown", poChangeRequest);
				
				if (!poUploadFile.equalsIgnoreCase("")) {
					locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\po\\" + poUploadFile);

					}

			Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Update')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Update')]")));
	    driver.findElement(By.xpath("//button[contains(.,'Update')]")).click();

		
		String entityShowPageName = locateElementBy("po_show_page_name").getText();
		

		Assertion(entityShowPageName.substring(0, entityShowPageName.length()-2), poName);
		
				
	   		
		    driver.findElement(By.linkText("AUDIT LOG")).click();
		    waitF.until(ExpectedConditions.visibilityOf(locateElementBy("po_audit_log_table")));
			
		    Assertion(locateElementBy("po_audit_log_action_taken").getText(), "Updated");

		    if (!poComments.equalsIgnoreCase("")){
		    	try{
		    		Assert.assertEquals(locateElementBy("po_audit_log_comment").getText(), "Yes", "Comment Matched");
		    	}catch (AssertionError err) {
			 		System.out.println("Comment: " + poComments + " and " + "Comment under ComunicationTab: " + locateElementBy("po_audit_log_comment").getText() + " Does not Match");
			 	}
		    }else{
		    	try{
		    		Assert.assertEquals(locateElementBy("po_audit_log_comment").getText(), "No", "Comment Matched");
		  	    	}catch (AssertionError err) {
		  		 		System.out.println("Comment: " + poComments + " and " + "Comment under ComunicationTab: " + locateElementBy("po_audit_log_comment").getText() + " Does not Match");
		  		 	}
		    }
		    if (!poUploadFile.equalsIgnoreCase("")) {
			 		
			 	try{
			 		Assert.assertEquals(locateElementBy("po_audit_log_uploaded_file").getText(), "Yes", "File Name Matched");
						 	}catch (AssertionError err) {
			 		System.out.println("Uploaded File Name: " + poUploadFile + "and" + "File Name under ComunicationTab: " + locateElementBy("po_audit_log_uploaded_file").getText() + "Does not Match");
			 	}
				}else{
					try{
					Assertion(locateElementBy("po_audit_log_uploaded_file").getText(), "No");
					}catch (AssertionError err) {
				 		System.out.println("Uploaded File Name: " + poUploadFile + "and" + "File Name under ComunicationTab: " + locateElementBy("po_audit_log_uploaded_file").getText() + "Does not Match");
				 	}
				}
		    
		    if (!poActualDate.equalsIgnoreCase("")) {
				 
				 String[] TimeOfpo = locateElementBy("po_audit_log_actual_date").getText().split("-");
				 String[] poActualdate = poActualDate.split("-");
				 	
				 				 	try {
				 				 		Assert.assertEquals(TimeOfpo[0],poActualdate[1], "Date Matched");
				 				 		Assert.assertEquals(TimeOfpo[1],poActualdate[0].substring(3, 8), "Month Matched");
				 				 		Assert.assertEquals(TimeOfpo[2],poActualdate[2], "Year Matched");
				 	}catch (AssertionError err) {
				 		System.out.println("Actual Date: " + poActualDate + " and " + "Time of po under ComunicationTab: " + locateElementBy("po_audit_log_actual_date").getText() + " Does not Match");
				 	}
					
								
					}else{
						try{
							Assert.assertEquals(locateElementBy("po_audit_log_actual_date").getText(), dateFormat.format(date), "Date, Month and Year Matched");
						}catch (AssertionError err) {
					 		System.out.println("Actual Date: " + poActualDate + " and " + "Time of po under ComunicationTab: " + locateElementBy("po_audit_log_actual_date").getText()  + " Does not Match");
					 	}
					} 
		    
		    		try{
		    			Assert.assertEquals(locateElementBy("po_audit_log_time_of_action").getText(), dateFormat.format(date), "Date, Month and Year Matched");
								}catch (AssertionError err) {
								System.out.println("Time of po: " + dateFormat.format(date) + " and " + "Time of po under Audit Log: " + locateElementBy("po_audit_log_time_of_action").getText()  + " Does not Match");
								}
		    
			 		if (!poRequestedBy.equalsIgnoreCase("")) {
					 						 	
						try{
							Assert.assertEquals(locateElementBy("po_audit_log_requested_by").getText(), poRequestedBy, "Requested By Name Matched");
							
						 }catch (AssertionError err) {
						 	System.out.println("Requested By: " + poRequestedBy + " and " + "Requested By under Audit Log: " + locateElementBy("po_audit_log_requested_by").getText() + " Does not Match");
						 }
					 		}else{
							try{
								Assert.assertEquals(locateElementBy("po_audit_log_requested_by").getText(), logged_in_user, "Requested By Name Matched");
							}catch (AssertionError err) {
								System.out.println("Requested By: " + logged_in_user + " and " + "Requested By under Audit Log: " + locateElementBy("po_audit_log_requested_by").getText() + " Does not Match");
						 	}
						}
			 		try{
			 			
			 			Assert.assertEquals(locateElementBy("po_audit_log_completed_by").getText(), logged_in_user, "Completed By Name Matched");
						
					 }catch (AssertionError err) {
					 	System.out.println("Requested By: " + logged_in_user + " and " + "Requested By under Audit Log: " + locateElementBy("po_audit_log_completed_by").getText() + " Does not Match");
					 }

		    locateElementBy("po_audit_log_view_history_link").click();
		    waitF.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("historyDataTable"))));

		    List<WebElement> e = driver.findElement(By.id("historyDataTable")).findElement(By.id("data")).findElements(By.tagName("tr"));
		    for (int i = 0; i < e.size(); i++) {
		      for (int j = 0; j < e.get(0).findElements(By.tagName("td")).size(); j++) {
		        if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("PO Number")) {
		          List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		          element.get(3);
		          String Actual = element.get(3).getText();
		          Assertion(Actual.substring(0, Actual.length()-2), poPONumber);
		        }
		        
		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Name")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            String Actual = element.get(3).getText();
		            Assertion(Actual.substring(0, Actual.length()-2), poName);
		          }

		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Requisition Number")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            String Actual = element.get(3).getText();
		            Assertion(Actual.substring(0, Actual.length()-2), poRequisitionNumber);
		          }

		        
		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Tracking Number")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            String Actual = element.get(3).getText();
		            Assertion(Actual.substring(0, Actual.length()-2), poDescription);
		          }

		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Start Date")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            Assertion(element.get(3).getText(), poStartDate);
		          }
		        
		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("End Date")) {
		          List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		          element.get(3);
		          Assertion(element.get(3).getText(), poEndDate);
		        }
		        
		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("States")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            Assertion(element.get(3).getText(), poStates);
		          }
		        
		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Countries")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            Assertion(element.get(3).getText(), poCountries);
		          }
		        
		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Contracting Entity")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            Assertion(element.get(3).getText(), poContractingEntity);
		          }
		        
		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Business Units")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            Assertion(element.get(3).getText(), poBusinessUnit);
		          }
		        
		        
		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Service Sub Category")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            Assertion(element.get(3).getText(), poServiceSubCategory);
		          }
		        
		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("PO Total")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            Assertion(element.get(3).getText(), poPOTotal);
		          }
		        
		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("PO Burn")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            Assertion(element.get(3).getText(), poBurn);
		          }
		        
		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("PO Available")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            Assertion(element.get(3).getText(), poAvailable);
		          }
		        
		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Time Zone")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            Assertion(element.get(3).getText(), poTimeZone);
		          }
		        
		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Currency")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            Assertion(element.get(3).getText(), poCurrency);
		          }
		        
		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Active")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            Assertion(element.get(3).getText(), poActive);
		          }
		        
		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Tier")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            Assertion(element.get(3).getText(), poTier);
		          }
		        
		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Expected PO Burn")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            Assertion(element.get(3).getText(), poExpectedBurn);
		          }
		        
		        		
		fail = false;

		}
		    }	
		
	 driver.get(CONFIG.getProperty("endUserURL"));
	}

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(po_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(po_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
			}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(po_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
			}
		try {
          for (int i = 2; i <= po_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
              String testCaseID = po_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
			TestUtil.reportDataSetResult(po_suite_xls, "Test Cases", TestUtil.getRowNum(po_suite_xls, this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(po_suite_xls, "Test Cases", TestUtil.getRowNum(po_suite_xls, this.getClass().getSimpleName()), "FAIL");
			}
	
	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(po_suite_xls, this.getClass().getSimpleName());
		}
	}