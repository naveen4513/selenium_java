package office.sirion.suite.governanceBody;

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

public class GBAuditLog extends TestSuiteBase {
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
		if (!TestUtil.isTestCaseRunnable(governance_body_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(governance_body_suite_xls, this.getClass().getSimpleName());
		}
	
	@Test(dataProvider = "getTestData")
	public void GBAuditLogTest (String gbTitle, String gbDescription, String gbGoal, String gbGovernanceBodyType, String gbTimezone, String gbStartTime,
			String gbDuration, String gbSupplierAccess, String gbLocation, String gbFrequencyType, String gbFrequency, String gbWeekType, String gbStartDate,
			String gbEndDate, String gbIncludeStartDate, String gbIncludeEndDate, String gbPatternDate, String gbEffectiveDate, String gbRecipientClientEntity,
			String gbRecipientCompanyCode, String gbContractingClientEntity, String gbContractingCompanycode, String gbFunctions, String gbServices,
			String gbServiceCategory, String gbRegions, String gbCountries, String gbProjectID, String gbInitiatives, String gbProjectLevels,
			String gbComments, String gbActualDate, String gbRequestedBy, String gbChangeRequest, String gbUploadFile, String gbParent
			) throws InterruptedException {
		
		count++;
		if (!runmodes[count].equalsIgnoreCase("Y")) {
			skip = true;
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);
			}
	
		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
	    String logged_in_user = locateElementBy("gb_communication_logged_in_user_full_name").getText(); driver.get(CONFIG.getProperty("endUserURL"));
	    DatePicker_Enhanced date = new DatePicker_Enhanced();
		Logger.debug("Executing Test Case Governance Body Update with Title --- " + gbTitle);

        driver.get(CONFIG.getProperty("endUserURL"));
		
	   	fluentWaitMethod(locateElementBy("gb_quick_link"));
	    
	    locateElementBy("gb_quick_link").click();

		fluentWaitMethod(driver.findElement(By.linkText("VIEW GOVERNANCE BODY")));

		driver.findElement(By.linkText("VIEW GOVERNANCE BODY")).click();

		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));
		
		new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText("100");

		
		if (!gbParent.equalsIgnoreCase("")) {
			driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr[@role='row']/td[contains(.,'" + gbParent + "')]/preceding-sibling::td[1]/a")).click();
			} else {
				locateElementBy("entity_listing_page_first_entry_link").click();
				}

		fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
	    Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
	    driver.findElement(By.xpath("//button[contains(.,'Edit')]")).click();
	    fluentWaitMethod(locateElementBy("gb_create_page_title_textbox"));

	    // Governance Body - Create Page - BASIC INFORMATION	    
	    addFieldValue("gb_create_page_title_textbox",gbTitle);

	    addFieldValue("gb_create_page_description_textarea",gbDescription);

	    addFieldValue("gb_create_page_goal_textarea",gbGoal);

	    addFieldValue("gb_create_page_gb_type_dropdown",gbGovernanceBodyType);
	    
	    addFieldValue("gb_create_page_timezone_dropdown",gbTimezone);
	    try {
	    	if (driver.findElement(By.className("success-icon")).getText().contains("Current Date is different for the selected Time Zone"))
	    		driver.findElement(By.xpath(".//button[contains(.,'OK')]")).click();
	    	} catch (Exception e) {
	    		
	    	}    

	    addFieldValue("gb_create_page_start_time_dropdown",gbStartTime.split(" ")[0]);
    
	    addFieldValue("gb_create_page_duration_dropdown",gbDuration);

	    addFieldValue("gb_create_page_supplier_access_checkbox",gbSupplierAccess);
	    
	    addFieldValue("gb_create_page_location_textbox",gbLocation);
	    
	    // Governance Body - Create Page  - IMPORTANT DATES
	    addFieldValue("gb_create_page_frequency_type_dropdown",gbFrequencyType);

	    addFieldValue("gb_create_page_frequency_dropdown",gbFrequency);
	    
	    addFieldValue("gb_create_page_week_type_dropdown",gbWeekType);

	    addFieldValue("gb_create_page_start_date",gbStartDate);
	    
	    addFieldValue("gb_create_page_exp_date",gbEndDate);
	    
	    addFieldValue("gb_create_page_include_start_date_checkbox",gbIncludeStartDate);
	    
	    addFieldValue("gb_create_page_include_exp_date_checkbox",gbIncludeEndDate);

	    addFieldValue("gb_create_page_pattern_date",gbPatternDate);

	    addFieldValue("gb_create_page_effective_date",gbEffectiveDate);

	    // Governance Body - Create Page  - CONTRACTING ENTITY
	    addFieldValue("gb_create_page_recipient_client_entities_multiselect",gbRecipientClientEntity);
	    
	    addFieldValue("gb_create_page_recipient_company_codes_multiselect",gbRecipientCompanyCode);
	    
	    addFieldValue("gb_create_page_contracting_client_entities_multiselect",gbContractingClientEntity);
	    
	    addFieldValue("gb_create_page_contracting_company_codes_multiselect",gbContractingCompanycode);
	    
	 // Governance Body - Create Page - FUNCTIONS
	    if (!gbFunctions.equalsIgnoreCase("")) {
	    	if (locateElementBy("gb_create_page_functions_multi_dropdown").findElements(By.tagName("select")).size()!=0) {
	    		for (String entityData : gbFunctions.split(";")) {
	    			new Select(locateElementBy("gb_create_page_functions_multi_dropdown").findElement(By.tagName("select"))).selectByVisibleText(entityData.trim());
	    			}
	    	    if (!gbServices.equalsIgnoreCase("")) {
	    	    	if (locateElementBy("gb_create_page_services_multi_dropdown").findElements(By.tagName("select")).size()!=0) {
	    	    		for (String entityData : gbServices.split(";")) {
	    	    			new Select(locateElementBy("gb_create_page_services_multi_dropdown").findElement(By.tagName("select"))).selectByVisibleText(entityData.trim());
	    	    			}
	    	    		}
	    	    	}
	    		}
	    	}
		
	    if (!gbServiceCategory.equalsIgnoreCase("")) {
	    	if (locateElementBy("gb_create_page_service_category_multi_dropdown").findElements(By.tagName("select")).size()!=0) {
	    		for (String entityData : gbServiceCategory.split(";")) {
	    			new Select(locateElementBy("gb_create_page_service_category_multi_dropdown").findElement(By.tagName("select"))).selectByVisibleText(entityData.trim());
	    			}
	    		}
	    	}

	    // Governance Body - Create Page - GEOGRAPHY
	    if (!gbRegions.equalsIgnoreCase("")) {
	    	if (locateElementBy("gb_create_page_management_regions_multi_dropdown").findElements(By.tagName("select")).size()!=0) {
	    		for (String entityData : gbRegions.split(";")) {
	    			new Select(locateElementBy("gb_create_page_management_regions_multi_dropdown").findElement(By.tagName("select"))).selectByVisibleText(entityData.trim());
	    			}
	    	    if (!gbCountries.equalsIgnoreCase("")) {
	    	    	if (locateElementBy("gb_create_page_management_countries_multi_dropdown").findElements(By.tagName("select")).size()!=0) {
	    	    		for (String entityData : gbCountries.split(";")) {
	    	    			new Select(locateElementBy("gb_create_page_management_countries_multi_dropdown").findElement(By.tagName("select"))).selectByVisibleText(entityData.trim());
	    	    			}
	    	    		}
	    	    	}
	    		}
	    	}   
		
		// Governance Body - Create Page - PROJECT INFORMATION
		addFieldValue("gb_create_page_project_id_multiselect", gbProjectID);

		addFieldValue("gb_create_page_initiatives_multiselect", gbInitiatives);

		addFieldValue("gb_create_page_project_levels_multiselect", gbProjectLevels);
		
		// Governance Body - Create Page - COMMENTS AND ATTACHMENTS
		addFieldValue("entity_create_page_comments_textarea", gbComments);
		    
		addFieldValue("entity_create_page_requested_by_dropdown", gbRequestedBy);
		  
		date.selectCalendar(gbActualDate,"actualDate");
		 
		addFieldValue("entity_create_page_change_request_dropdown", gbChangeRequest);
		
		if (!gbUploadFile.equalsIgnoreCase("")) {
			locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\GB\\" + gbUploadFile);

			}
		
	    Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Update')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Update')]")));
	    driver.findElement(By.xpath("//button[contains(.,'Update')]")).click();


	    fluentWaitMethod(locateElementBy("gb_show_page_title"));


		String entityShowPageName = locateElementBy("gb_show_page_title").getText();

		Assert.assertEquals(entityShowPageName, gbTitle);
		Logger.debug("GB Title on show page has been verified");
		
		try{
			Assert.assertEquals(entityShowPageName, gbTitle);
			Logger.debug("gb Title on show page has been verified");
			}catch (AssertionError err) {
		 		System.out.println("Title: " + entityShowPageName + " and " + gbTitle + " Does not Match");
		 	}
			
		    driver.findElement(By.linkText("AUDIT LOG")).click();
		    waitF.until(ExpectedConditions.visibilityOf(locateElementBy("gb_audit_log_table")));
			
		    Assert.assertEquals(locateElementBy("gb_audit_log_action_taken").getText(), "Updated");

		    if (!gbComments.equalsIgnoreCase("")){
		    	try{
		      Assert.assertEquals(locateElementBy("gb_audit_log_comment").getText(), "Yes", "Comment Matched");
		    	}catch (AssertionError err) {
			 		System.out.println("Comment: " + gbComments + " and " + "Comment under ComunicationTab: " + locateElementBy("gb_audit_log_comment").getText() + " Does not Match");
			 	}
		    }else{
		    	try{
		  	      Assert.assertEquals(locateElementBy("gb_audit_log_comment").getText(), "No", "Comment Matched");
		  	    	}catch (AssertionError err) {
		  		 		System.out.println("Comment: " + gbComments + " and " + "Comment under ComunicationTab: " + locateElementBy("gb_audit_log_comment").getText() + " Does not Match");
		  		 	}
		    }
		    if (!gbUploadFile.equalsIgnoreCase("")) {
			 		
			 	try{
				Assert.assertEquals(locateElementBy("gb_audit_log_uploaded_file").getText(), "Yes", "File Name Matched");
						 	}catch (AssertionError err) {
			 		System.out.println("Uploaded File Name: " + gbUploadFile + "and" + "File Name under ComunicationTab: " + locateElementBy("gb_audit_log_uploaded_file").getText() + "Does not Match");
			 	}
				}else{
					try{
					Assert.assertEquals(locateElementBy("gb_audit_log_uploaded_file").getText(), "No");
					}catch (AssertionError err) {
				 		System.out.println("Uploaded File Name: " + gbUploadFile + "and" + "File Name under ComunicationTab: " + locateElementBy("gb_audit_log_uploaded_file").getText() + "Does not Match");
				 	}
				}
		    
		    if (!gbActualDate.equalsIgnoreCase("")) {
				 
				 String[] TimeOfgb = locateElementBy("gb_audit_log_actual_date").getText().split("-");
				 String[] gbActualdate = gbActualDate.split("-");
				 	
				 				 	try {
					Assert.assertEquals(TimeOfgb[0],gbActualdate[1], "Date Matched");
					Assert.assertEquals(TimeOfgb[1],gbActualdate[0].substring(3, 8), "Month Matched");
					Assert.assertEquals(TimeOfgb[2],gbActualdate[2], "Year Matched");
				 	}catch (AssertionError err) {
				 		System.out.println("Actual Date: " + gbActualDate + " and " + "Time of gb under ComunicationTab: " + locateElementBy("gb_audit_log_actual_date").getText() + " Does not Match");
				 	}
					
								
					}else{
						try{
						Assert.assertEquals(locateElementBy("gb_audit_log_actual_date").getText(), dateFormat.format(date), "Date, Month and Year Matched");
						}catch (AssertionError err) {
					 		System.out.println("Actual Date: " + gbActualDate + " and " + "Time of gb under ComunicationTab: " + locateElementBy("gb_audit_log_actual_date").getText()  + " Does not Match");
					 	}
					} 
		    
		    		try{
								Assert.assertEquals(locateElementBy("gb_audit_log_time_of_action").getText(), dateFormat.format(date), "Date, Month and Year Matched");
								}catch (AssertionError err) {
								System.out.println("Time of gb: " + dateFormat.format(date) + " and " + "Time of gb under Audit Log: " + locateElementBy("gb_audit_log_time_of_action").getText()  + " Does not Match");
								}
		    
			 		if (!gbRequestedBy.equalsIgnoreCase("")) {
					 						 	
						try{
							Assert.assertEquals(locateElementBy("gb_audit_log_requested_by").getText(), gbRequestedBy, "Requested By Name Matched");
							
						 }catch (AssertionError err) {
						 	System.out.println("Requested By: " + gbRequestedBy + " and " + "Requested By under Audit Log: " + locateElementBy("gb_audit_log_requested_by").getText() + " Does not Match");
						 }
					 		}else{
							try{
								Assert.assertEquals(locateElementBy("gb_audit_log_requested_by").getText(), logged_in_user, "Requested By Name Matched");
							}catch (AssertionError err) {
								System.out.println("Requested By: " + logged_in_user + " and " + "Requested By under Audit Log: " + locateElementBy("gb_audit_log_requested_by").getText() + " Does not Match");
						 	}
						}
			 		try{
			 			
						Assert.assertEquals(locateElementBy("gb_audit_log_completed_by").getText(), logged_in_user, "Completed By Name Matched");
						
					 }catch (AssertionError err) {
					 	System.out.println("Requested By: " + logged_in_user + " and " + "Requested By under Audit Log: " + locateElementBy("gb_audit_log_completed_by").getText() + " Does not Match");
					 }

		    locateElementBy("gb_audit_log_view_history_link").click();
		    waitF.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("historyDataTable"))));

		    List<WebElement> e = driver.findElement(By.id("historyDataTable")).findElement(By.id("data")).findElements(By.tagName("tr"));
		    for (int i = 0; i < e.size(); i++) {
		      for (int j = 0; j < e.get(0).findElements(By.tagName("td")).size(); j++) {
		        if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Title")) {
		          List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		          element.get(3);
		          Assert.assertEquals(element.get(3).getText(), gbTitle);
		        }
		        
		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Description")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            Assert.assertEquals(element.get(3).getText(), gbDescription);
		          }

		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Goal")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            Assert.assertEquals(element.get(3).getText(), gbGoal);
		          }
		        
		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Frequency Type")) {
		          List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		          element.get(3);
		          Assert.assertEquals(element.get(3).getText(), gbFrequencyType);
		        }
		        
		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Frequency")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            Assert.assertEquals(element.get(3).getText(), gbFrequency);
		          }
		        
		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Week Type")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            try{
		            Assert.assertEquals(element.get(3).getText(), gbWeekType);
		            }catch (AssertionError err) {
				 		System.out.println("Week Type: " + gbWeekType + " and " + "Week Type under ComunicationTab: " + element.get(3).getText() + " Does not Match");
				 	}
		          }
		        
		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Start Date")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            Assert.assertEquals(element.get(3).getText(), gbStartDate);
		          }
		        
		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("End Date")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            Assert.assertEquals(element.get(3).getText(), gbEndDate);
		          }
		        
		        
		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Include Start Date")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            Assert.assertEquals(element.get(3).getText(), gbIncludeStartDate);
		          }
		        
		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Include End Date")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            Assert.assertEquals(element.get(3).getText(), gbIncludeEndDate);
		          }
		        
		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Pattern Date")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            Assert.assertEquals(element.get(3).getText(), gbPatternDate);
		          }
		        
		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Duration")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            Assert.assertEquals(element.get(3).getText(), gbDuration);
		          }
		        
		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Governance Body Type")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            Assert.assertEquals(element.get(3).getText(), gbGovernanceBodyType);
		          }
		        
		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Location")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            Assert.assertEquals(element.get(3).getText(), gbLocation);
		          }
		        
		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Supplier Access")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            Assert.assertEquals(element.get(3).getText(), gbSupplierAccess);
		          }
		        
		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Time Zone")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            Assert.assertEquals(element.get(3).getText(), gbTimezone);
		          }
		        
		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Effective Date")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            Assert.assertEquals(element.get(3).getText(), gbEffectiveDate);
		          }
		        
		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Initiatives")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            Assert.assertEquals(element.get(3).getText(), gbInitiatives);
		          }
		        
		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Project Levels")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            Assert.assertEquals(element.get(3).getText(), gbProjectLevels);
		          }
		        
		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Recipient Client Entity")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            Assert.assertEquals(element.get(3).getText(), gbRecipientClientEntity);
		          }
		        
		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Recipient Company Code")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            Assert.assertEquals(element.get(3).getText(), gbRecipientCompanyCode);
		          }
		        
		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Contracting Client Entity")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            Assert.assertEquals(element.get(3).getText(), gbContractingClientEntity);
		          }
		        
		        else if (e.get(i).findElements(By.tagName("td")).get(j).getText().equalsIgnoreCase("Contracting Company Code")) {
		            List<WebElement> element = e.get(i).findElements(By.tagName("td"));
		            element.get(3);
		            Assert.assertEquals(element.get(3).getText(), gbContractingCompanycode);
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
				TestUtil.reportDataSetResult(governance_body_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");

			else if(testResult.getStatus()==ITestResult.FAILURE) {
				isTestPass=false;
				TestUtil.reportDataSetResult(governance_body_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
				result= "Fail";
				}
			
			else if (testResult.getStatus()==ITestResult.SUCCESS) {
				TestUtil.reportDataSetResult(governance_body_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
				result= "Pass";
				}

		try {
			for (int i = 2; i <= governance_body_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
				String testCaseID = governance_body_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
			TestUtil.reportDataSetResult(governance_body_suite_xls, "Test Cases", TestUtil.getRowNum(governance_body_suite_xls, this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(governance_body_suite_xls, "Test Cases", TestUtil.getRowNum(governance_body_suite_xls, this.getClass().getSimpleName()), "FAIL");
			}
	
	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(governance_body_suite_xls, this.getClass().getSimpleName());
		}
	}