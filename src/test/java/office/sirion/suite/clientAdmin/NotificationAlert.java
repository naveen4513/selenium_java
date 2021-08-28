package office.sirion.suite.clientAdmin;

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
import java.time.ZonedDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NotificationAlert extends TestSuiteBase {
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
	public void NotificationAlertCreation (String entityType, String name, String alert_type, String condition, String toEmail, String emailTemplate, String startDateField,
                                           String startDateOffset, String repeatInterval, String filter_ac_supplier, String filter_ac_priority, String filter_ac_type,
                                           String filter_ac_status, String filter_ac_gb, String filter_cr_supplier, String filter_cr_priority, String filter_cr_class,
                                           String filter_cr_type, String filter_cr_status, String filter_cob_supplier, String filter_cob_status, String filter_cob_category,
                                           String filter_cob_performance_type, String filter_cob_performance_status, String filter_csl_supplioer, String filter_csl_status,
                                           String filter_csl_performance_status, String filter_csl_sl_category, String filter_csl_sl_sub_category,
                                           String filter_con_supplier, String filter_con_status, String filter_con_tcv_min, String filter_con_tcv_max,
                                           String filter_con_term_type, String filter_dispute_supplier, String filter_dispute_type, String filter_dispute_status,
                                           String filter_dispute_gb, String filter_gb_supplier, String filter_gb_status, String filter_gb_type, String filter_gb_meeting_supplier,
                                           String filter_gb_meeting_status, String filter_gb_meeting_gb_type, String filter_issue_supplier, String filter_issue_type,
                                           String filter_issue_status, String filter_issue_governance_bodies, String filter_ob_supplier, String filter_ob_status, String filter_ob_category,
                                           String filter_ob_performance_type, String filter_sla_supplioer, String filter_sla_status, String filter_sla_sl_category,
                                           String filter_sla_sl_sub_category,String filter_wor_supplier, String filter_wor_priority, String filter_wor_type, String filter_wor_status)
                                            throws InterruptedException {
		count++;
		if(!runmodes[count].equalsIgnoreCase("Y")) {
			skip=true;
			throw new SkipException("Runmode for Test Data -- " +entityType +" set to NO " +count);
			}
		
		openBrowser();
		clientAdminLogin(CONFIG.getProperty("clientAdminURL"), CONFIG.getProperty("clientAdminUsername"), CONFIG.getProperty("clientAdminPassword"));
		
		Logger.debug("Executing Client Admin Action Type Creation Test -- "+entityType);
		
		driver.get(CONFIG.getProperty("clientAdminURL"));
		Thread.sleep(8000);
		fluentWaitMethod(driver.findElement(By.linkText("Notification Alerts")));

		driver.findElement(By.linkText("Notification Alerts")).click();
		fluentWaitMethod(driver.findElement(By.className("plus")));

		driver.findElement(By.className("plus")).click();
		fluentWaitMethod(driver.findElement(By.xpath("//span[@id='ui-id-2']")));

		Thread.sleep(2500);

		if (!entityType.equalsIgnoreCase("")) {
			new Select(driver.findElement(By.id("_entityType.id_id"))).selectByVisibleText(entityType.trim());
			}

        Thread.sleep(2500);

        String Title = name.trim()+ZonedDateTime.now().toInstant().toEpochMilli();

		if (!name.equalsIgnoreCase("")) {
			driver.findElement(By.id("_name_id")).clear();
			driver.findElement(By.id("_name_id")).sendKeys(Title);
			}

        Thread.sleep(2500);
		
		if (!alert_type.equalsIgnoreCase("")) {
			new Select (driver.findElement(By.id("_notificationType.id_id"))).selectByVisibleText(alert_type);
			}

        Thread.sleep(2500);

        if (!condition.equalsIgnoreCase("")) {
            driver.findElement(By.id("_condition_id")).clear();
            driver.findElement(By.id("_condition_id")).sendKeys(condition);
            driver.findElement(By.xpath("//div[contains(text(),'Filters')]")).click();
        }

        Thread.sleep(2500);

        if (!toEmail.equalsIgnoreCase("")) {
            for (String entityData : toEmail.split(";")) {
                new Select(driver.findElement(By.id("_emailToList_id"))).selectByVisibleText(entityData);
            }
        }

        Thread.sleep(2500);

        if (!emailTemplate.equalsIgnoreCase("")) {
            new Select (driver.findElement(By.id("_entityActionEmail.id_id"))).selectByVisibleText(emailTemplate);
        }

        Thread.sleep(2500);

        if(alert_type.equalsIgnoreCase("Time Based")) {

            if (!startDateField.equalsIgnoreCase("")) {
                new Select(driver.findElement(By.id("_startDateField.id_id"))).selectByVisibleText(startDateField);
            }

            Thread.sleep(2500);

            if (!startDateOffset.equalsIgnoreCase("")) {
                driver.findElement(By.id("_startDateOffset_id")).clear();
                driver.findElement(By.id("_startDateOffset_id")).sendKeys(convertStringToInteger(startDateOffset));
            }

            Thread.sleep(2500);

            if (!repeatInterval.equalsIgnoreCase("")) {
                driver.findElement(By.id("_repeatInterval_id")).clear();
                driver.findElement(By.id("_repeatInterval_id")).sendKeys(convertStringToInteger(repeatInterval));
            }
        }

        Thread.sleep(2500);

        if(entityType.equalsIgnoreCase("Actions")) {

            //For Actions Entity
            if (!filter_ac_supplier.equalsIgnoreCase("")) {
                for (String entityData : filter_ac_supplier.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[0].valueList_id"))).selectByVisibleText(entityData);
                }
            }

            Thread.sleep(2500);

            if (!filter_ac_priority.equalsIgnoreCase("")) {
                for (String entityData : filter_ac_priority.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[1].valueList_id"))).selectByVisibleText(entityData);
                }
            }

            Thread.sleep(2500);

            if (!filter_ac_type.equalsIgnoreCase("")) {
                for (String entityData : filter_ac_type.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[2].valueList_id"))).selectByVisibleText(entityData);
                }
            }

            Thread.sleep(2500);

            if (!filter_ac_status.equalsIgnoreCase("")) {
                for (String entityData : filter_ac_status.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[3].valueList_id"))).selectByVisibleText(entityData);
                }
            }

            Thread.sleep(2500);

            if (!filter_ac_gb.equalsIgnoreCase("")) {
                for (String entityData : filter_ac_gb.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[4].valueList_id"))).selectByVisibleText(entityData);
                }
            }
        }

        Thread.sleep(2500);

        //For Change Requests

        if(entityType.equalsIgnoreCase("Change Requests")) {

            if (!filter_cr_supplier.equalsIgnoreCase("")) {
                for (String entityData : filter_cr_supplier.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[0].valueList_id"))).selectByVisibleText(entityData);
                }
            }

            Thread.sleep(2500);

            if (!filter_cr_priority.equalsIgnoreCase("")) {
                for (String entityData : filter_cr_priority.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[1].valueList_id"))).selectByVisibleText(entityData);
                }
            }

            Thread.sleep(2500);

            if (!filter_cr_class.equalsIgnoreCase("")) {
                for (String entityData : filter_cr_class.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[2].valueList_id"))).selectByVisibleText(entityData);
                }
            }

            Thread.sleep(2500);

            if (!filter_cr_type.equalsIgnoreCase("")) {
                for (String entityData : filter_cr_type.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[3].valueList_id"))).selectByVisibleText(entityData);
                }
            }

            Thread.sleep(2500);

            if (!filter_cr_status.equalsIgnoreCase("")) {
                for (String entityData : filter_cr_status.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[4].valueList_id"))).selectByVisibleText(entityData);
                }
            }
        }

        Thread.sleep(2500);

        //For Child Obligations

        if(entityType.equalsIgnoreCase("Child Obligations")) {

            if (!filter_cob_supplier.equalsIgnoreCase("")) {
                for (String entityData : filter_cob_supplier.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[0].valueList_id"))).selectByVisibleText(entityData);
                }
            }

            Thread.sleep(2500);

            if (!filter_cob_status.equalsIgnoreCase("")) {
                for (String entityData : filter_cob_status.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[1].valueList_id"))).selectByVisibleText(entityData);
                }
            }

            Thread.sleep(2500);

            if (!filter_cob_category.equalsIgnoreCase("")) {
                for (String entityData : filter_cob_category.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[2].valueList_id"))).selectByVisibleText(entityData);
                }
            }

            Thread.sleep(2500);

            if (!filter_cob_performance_type.equalsIgnoreCase("")) {
                for (String entityData : filter_cob_performance_type.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[3].valueList_id"))).selectByVisibleText(entityData);
                }
            }

            Thread.sleep(2500);

            if (!filter_cob_performance_status.equalsIgnoreCase("")) {
                for (String entityData : filter_cob_performance_status.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[4].valueList_id"))).selectByVisibleText(entityData);
                }
            }
        }

        Thread.sleep(2500);

        //for Child Service Levels

        if(entityType.equalsIgnoreCase("Child Service Levels")) {

            if (!filter_csl_supplioer.equalsIgnoreCase("")) {
                for (String entityData : filter_csl_supplioer.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[0].valueList_id"))).selectByVisibleText(entityData);
                }
            }

            Thread.sleep(2500);

            if (!filter_csl_status.equalsIgnoreCase("")) {
                for (String entityData : filter_csl_status.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[1].valueList_id"))).selectByVisibleText(entityData);
                }
            }

            Thread.sleep(2500);

            if (!filter_csl_performance_status.equalsIgnoreCase("")) {
                for (String entityData : filter_csl_performance_status.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[2].valueList_id"))).selectByVisibleText(entityData);
                }
            }

            Thread.sleep(2500);

            if (!filter_csl_sl_category.equalsIgnoreCase("")) {
                for (String entityData : filter_csl_sl_category.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[3].valueList_id"))).selectByVisibleText(entityData);
                }
            }

            Thread.sleep(2500);

            if (!filter_csl_sl_sub_category.equalsIgnoreCase("")) {
                for (String entityData : filter_csl_sl_sub_category.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[4].valueList_id"))).selectByVisibleText(entityData);
                }
            }
        }

        Thread.sleep(2500);

        //For Contracts

        if(entityType.equalsIgnoreCase("Contracts")) {

            if (!filter_con_supplier.equalsIgnoreCase("")) {
                for (String entityData : filter_con_supplier.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[0].valueList_id"))).selectByVisibleText(entityData);
                }
            }

            Thread.sleep(2500);

            if (!filter_con_status.equalsIgnoreCase("")) {
                for (String entityData : filter_con_status.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[1].valueList_id"))).selectByVisibleText(entityData);
                }
            }

            Thread.sleep(2500);

            if (!filter_con_tcv_min.equalsIgnoreCase("")) {
                driver.findElement(By.id("_commonFilterMetadatas[2].minValue_id")).clear();
                driver.findElement(By.id("_commonFilterMetadatas[2].minValue_id")).sendKeys(filter_con_tcv_min);

            }

            Thread.sleep(2500);

            if (!filter_con_tcv_max.equalsIgnoreCase("")) {
                driver.findElement(By.id("_commonFilterMetadatas[2].maxValue_id")).clear();
                driver.findElement(By.id("_commonFilterMetadatas[2].maxValue_id")).sendKeys(filter_con_tcv_max);

            }

            Thread.sleep(2500);

            if (!filter_con_term_type.equalsIgnoreCase("")) {
                for (String entityData : filter_con_term_type.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[3].valueList_id"))).selectByVisibleText(entityData);
                }
            }
        }

        Thread.sleep(2500);

        //For Disputes

        if(entityType.equalsIgnoreCase("Disputes")) {

            if (!filter_dispute_supplier.equalsIgnoreCase("")) {
                for (String entityData : filter_dispute_supplier.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[0].valueList_id"))).selectByVisibleText(entityData);
                }
            }

            Thread.sleep(2500);

            if (!filter_dispute_type.equalsIgnoreCase("")) {
                for (String entityData : filter_dispute_type.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[1].valueList_id"))).selectByVisibleText(entityData);
                }
            }

            Thread.sleep(2500);

            if (!filter_dispute_status.equalsIgnoreCase("")) {
                for (String entityData : filter_dispute_status.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[2].valueList_id"))).selectByVisibleText(entityData);
                }
            }

            Thread.sleep(2500);

            if (!filter_dispute_gb.equalsIgnoreCase("")) {
                for (String entityData : filter_dispute_gb.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[3].valueList_id"))).selectByVisibleText(entityData);
                }
            }
        }

        Thread.sleep(2500);

        //Governance Bodies

        if(entityType.equalsIgnoreCase("Governance Bodies")) {

            if (!filter_gb_supplier.equalsIgnoreCase("")) {
                for (String entityData : filter_gb_supplier.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[0].valueList_id"))).selectByVisibleText(entityData);
                }
            }

            Thread.sleep(2500);

            if (!filter_gb_status.equalsIgnoreCase("")) {
                for (String entityData : filter_gb_status.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[1].valueList_id"))).selectByVisibleText(entityData);
                }
            }

            Thread.sleep(2500);

            if (!filter_gb_type.equalsIgnoreCase("")) {
                for (String entityData : filter_gb_type.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[2].valueList_id"))).selectByVisibleText(entityData);
                }
            }
        }

        Thread.sleep(2500);

        //Governance Body Meetings

        if(entityType.equalsIgnoreCase("Governance Body Meetings")) {

            if (!filter_gb_meeting_supplier.equalsIgnoreCase("")) {
                for (String entityData : filter_gb_meeting_supplier.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[0].valueList_id"))).selectByVisibleText(entityData);
                }
            }

            Thread.sleep(2500);

            if (!filter_gb_meeting_status.equalsIgnoreCase("")) {
                for (String entityData : filter_gb_meeting_status.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[1].valueList_id"))).selectByVisibleText(entityData);
                }
            }

            Thread.sleep(2500);

            if (!filter_gb_meeting_gb_type.equalsIgnoreCase("")) {
                for (String entityData : filter_gb_meeting_gb_type.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[2].valueList_id"))).selectByVisibleText(entityData);
                }
            }
        }

        Thread.sleep(2500);


        //For Issues

        if(entityType.equalsIgnoreCase("Issues")) {

            if (!filter_issue_supplier.equalsIgnoreCase("")) {
                for (String entityData : filter_issue_supplier.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[0].valueList_id"))).selectByVisibleText(entityData);
                }
            }

            Thread.sleep(2500);

            if (!filter_issue_type.equalsIgnoreCase("")) {
                for (String entityData : filter_issue_type.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[1].valueList_id"))).selectByVisibleText(entityData);
                }
            }

            Thread.sleep(2500);

            if (!filter_issue_status.equalsIgnoreCase("")) {
                for (String entityData : filter_issue_status.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[2].valueList_id"))).selectByVisibleText(entityData);
                }
            }

            Thread.sleep(2500);

            if (!filter_issue_governance_bodies.equalsIgnoreCase("")) {
                for (String entityData : filter_issue_governance_bodies.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[3].valueList_id"))).selectByVisibleText(entityData);
                }
            }
        }

        Thread.sleep(2500);



        //For Obligations

        if(entityType.equalsIgnoreCase("Obligations")) {

            if (!filter_ob_supplier.equalsIgnoreCase("")) {
                for (String entityData : filter_ob_supplier.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[0].valueList_id"))).selectByVisibleText(entityData);
                }
            }

            Thread.sleep(2500);

            if (!filter_ob_status.equalsIgnoreCase("")) {
                for (String entityData : filter_ob_status.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[1].valueList_id"))).selectByVisibleText(entityData);
                }
            }

            Thread.sleep(2500);

            if (!filter_ob_category.equalsIgnoreCase("")) {
                for (String entityData : filter_ob_category.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[2].valueList_id"))).selectByVisibleText(entityData);
                }
            }

            Thread.sleep(2500);

            if (!filter_ob_performance_type.equalsIgnoreCase("")) {
                for (String entityData : filter_ob_performance_type.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[3].valueList_id"))).selectByVisibleText(entityData);
                }
            }
        }

        Thread.sleep(2500);

        //For Service Levels

        if(entityType.equalsIgnoreCase("Service Levels")) {

            if (!filter_sla_supplioer.equalsIgnoreCase("")) {
                for (String entityData : filter_sla_supplioer.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[0].valueList_id"))).selectByVisibleText(entityData);
                }
            }

            Thread.sleep(2500);

            if (!filter_sla_status.equalsIgnoreCase("")) {
                for (String entityData : filter_sla_status.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[1].valueList_id"))).selectByVisibleText(entityData);
                }
            }

            Thread.sleep(2500);

            if (!filter_sla_sl_category.equalsIgnoreCase("")) {
                for (String entityData : filter_sla_sl_category.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[2].valueList_id"))).selectByVisibleText(entityData);
                }
            }

            Thread.sleep(2500);

            if (!filter_sla_sl_sub_category.equalsIgnoreCase("")) {
                for (String entityData : filter_sla_sl_sub_category.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[3].valueList_id"))).selectByVisibleText(entityData);
                }
            }
        }

        Thread.sleep(2500);

        //For Work Order Request

        if(entityType.equalsIgnoreCase("work Order Request")) {

            if (!filter_wor_supplier.equalsIgnoreCase("")) {
                for (String entityData : filter_wor_supplier.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[0].valueList_id"))).selectByVisibleText(entityData);
                }
            }

            Thread.sleep(2500);

            if (!filter_wor_priority.equalsIgnoreCase("")) {
                for (String entityData : filter_wor_priority.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[1].valueList_id"))).selectByVisibleText(entityData);
                }
            }

            Thread.sleep(2500);

            if (!filter_wor_type.equalsIgnoreCase("")) {
                for (String entityData : filter_wor_type.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[2].valueList_id"))).selectByVisibleText(entityData);
                }
            }

            Thread.sleep(2500);

            if (!filter_wor_status.equalsIgnoreCase("")) {
                for (String entityData : filter_wor_status.split(";")) {
                    new Select(driver.findElement(By.id("_commonFilterMetadatas[3].valueList_id"))).selectByVisibleText(entityData);
                }
            }
        }

        Thread.sleep(2500);

        driver.findElement(By.className("submit")).findElement(By.tagName("input")).click();
		Thread.sleep(5000);

		try {
            driver.findElement(By.xpath("/html/body/div[9]/div[3]/div/button/span")).click();
        }catch (NoSuchElementException e3){
		    Logger.debug("Notification alert is not getting created");

            fail=false;
            driver.get(CONFIG.getProperty("clientAdminURL"));
            return;
        }
		
		if (entityType.equalsIgnoreCase("")) {
			fail = true;
			
			WebElement e = driver.findElement(By.cssSelector("div._entityType_id_idformError.parentFormemailRuleNotification.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();
			
			Assertion(entityErrors, "This field is required");
			Logger.debug("Entity Type Field is Mandatory");

            fail=false;
            driver.get(CONFIG.getProperty("clientAdminURL"));

            return;
			
			}
			
		if (name.equalsIgnoreCase("")) {
			fail = true;
			
			WebElement e = driver.findElement(By.cssSelector("div._entityType_id_idformError.parentFormemailRuleNotification.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();
			
			Assertion(entityErrors, "Maximum 256 characters allowed");
			Logger.debug("Value under Name Field should be less than 256 Characters");

            fail=false;
            driver.get(CONFIG.getProperty("clientAdminURL"));

            return;
			}

        if (name.length()>256) {
            fail = true;

            WebElement e = driver.findElement(By.cssSelector("div._entityType_id_idformError.parentFormemailRuleNotification.formError"));
            String entityErrors = e.findElement(By.className("formErrorContent")).getText();

            Assertion(entityErrors, "This field is required");
            Logger.debug("Name Field is Mandatory");

            fail=false;
            driver.get(CONFIG.getProperty("clientAdminURL"));

            return;
        }

		if (alert_type.equalsIgnoreCase("")) {
			fail = true;
			
			WebElement e = driver.findElement(By.cssSelector("div._notificationType_id_idformError.parentFormemailRuleNotification.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();
			
			Assertion(entityErrors, "This field is required");
			Logger.debug("Alert Type Field is Mandatory");
            fail=false;
            driver.get(CONFIG.getProperty("clientAdminURL"));

            return;

			}

        if (condition.equalsIgnoreCase("")) {
            fail = true;

            WebElement e = driver.findElement(By.cssSelector("div._condition_idformError.parentFormemailRuleNotification.formError"));
            String entityErrors = e.findElement(By.className("formErrorContent")).getText();

            Assertion(entityErrors, "This field is required");
            Logger.debug("Condition Field is Mandatory");

            fail=false;
            driver.get(CONFIG.getProperty("clientAdminURL"));

            return;
        }

        if (toEmail.equalsIgnoreCase("")) {
            fail = true;

            WebElement e = driver.findElement(By.cssSelector("div._emailToList_idformError.parentFormemailRuleNotification.formError"));
            String entityErrors = e.findElement(By.className("formErrorContent")).getText();

            Assertion(entityErrors, "This field is required");
            Logger.debug("To_Email Field is Mandatory");

            fail=false;
            driver.get(CONFIG.getProperty("clientAdminURL"));

            return;
        }

        if (emailTemplate.equalsIgnoreCase("")) {
            fail = true;

            WebElement e = driver.findElement(By.cssSelector("div._entityActionEmail_id_idformError.parentFormemailRuleNotification.formError"));
            String entityErrors = e.findElement(By.className("formErrorContent")).getText();

            Assertion(entityErrors, "This field is required");
            Logger.debug("Email Template Field is Mandatory");

            fail=false;
            driver.get(CONFIG.getProperty("clientAdminURL"));

            return;
        }

        if (alert_type.equalsIgnoreCase("Time Based")) {
            if (startDateField.equalsIgnoreCase("")) {
                fail = true;

                WebElement e = driver.findElement(By.cssSelector("div._startDateField_id_idformError.parentFormemailRuleNotification.formError"));
                String entityErrors = e.findElement(By.className("formErrorContent")).getText();

                Assertion(entityErrors, "This field is required");
                Logger.debug("Start Date Field is Mandatory");

                fail=false;
                driver.get(CONFIG.getProperty("clientAdminURL"));

                return;
            }

            if (startDateOffset.equalsIgnoreCase("")) {
                fail = true;

                WebElement e = driver.findElement(By.cssSelector("div._startDateOffset_idformError.parentFormemailRuleNotification.formError"));
                String entityErrors = e.findElement(By.className("formErrorContent")).getText();

                Assertion(entityErrors, "This field is required");
                Logger.debug("Start Date Offset Field is Mandatory");

                fail=false;
                driver.get(CONFIG.getProperty("clientAdminURL"));

                return;
            }

            if (startDateOffset.length() > 18) {
                fail = true;

                WebElement e = driver.findElement(By.cssSelector("div._startDateOffset_idformError.parentFormemailRuleNotification.formError"));
                String entityErrors = e.findElement(By.className("formErrorContent")).getText();

                Assertion(entityErrors, "Maximum 18 characters allowed");
                Logger.debug("Value under Start Date Offset should be less than 18 characters");

                fail=false;
                driver.get(CONFIG.getProperty("clientAdminURL"));

                return;
            }

            Pattern pattern = Pattern.compile(".*[a-zA-Z]+.*");
            Matcher matcher = pattern.matcher(startDateOffset);

            if (matcher.matches()) {
                fail = true;

                WebElement e = driver.findElement(By.cssSelector("div._startDateOffset_idformError.parentFormemailRuleNotification.formError"));
                String entityErrors = e.findElement(By.className("formErrorContent")).getText();

                Assertion(entityErrors, "Only integer values allowed");
                Logger.debug("Value under Start Date Offset contains Alphabets, remove it and Try Again");

                fail=false;
                driver.get(CONFIG.getProperty("clientAdminURL"));

                return;
            }

            if (repeatInterval.equalsIgnoreCase("")) {
                fail = true;

                WebElement e = driver.findElement(By.cssSelector("div._repeatInterval_idformError.parentFormemailRuleNotification.formError"));
                String entityErrors = e.findElement(By.className("formErrorContent")).getText();

                Assertion(entityErrors, "This field is required");
                Logger.debug("Repeat Interval Field is Mandatory");

                fail=false;
                driver.get(CONFIG.getProperty("clientAdminURL"));

                return;
            }

            if (repeatInterval.length() > 18) {
                fail = true;

                WebElement e = driver.findElement(By.cssSelector("div._repeatInterval_idformError.parentFormemailRuleNotification.formError"));
                String entityErrors = e.findElement(By.className("formErrorContent")).getText();

                Assertion(entityErrors, "Maximum 18 characters allowed");
                Logger.debug("Value under Repeat Interval should be less than 18 characters");

                fail=false;
                driver.get(CONFIG.getProperty("clientAdminURL"));

                return;
            }

            Pattern pattern1 = Pattern.compile(".*[a-zA-Z]+.*");
            Matcher matcher1 = pattern1.matcher(repeatInterval);

            if (matcher1.matches()) {
                fail = true;

                WebElement e = driver.findElement(By.cssSelector("div._repeatInterval_idformError.parentFormemailRuleNotification.formError"));
                String entityErrors = e.findElement(By.className("formErrorContent")).getText();

                Assertion(entityErrors, "Only integer values allowed");
                Logger.debug("Value under Repeat Interval contains Alphabets, remove it and Try Again");
            }

            fail=false;
            driver.get(CONFIG.getProperty("clientAdminURL"));

            return;
        }

		if(driver.findElements(By.id("alertdialog")).size()!=0) {
			fail = true;			
			String entityErrors = driver.findElement(By.id("errors")).getText();
			
			if (entityErrors.contains("Invalid syntax :"+condition))
				Logger.debug("Value under Condition field is incorrect");
                driver.findElement(By.xpath("//span[@class='ui-button-text'][contains(text(),'OK')]")).click();

            fail=false;
			driver.get(CONFIG.getProperty("clientAdminURL"));

			return;
			}

        driver.findElement(By.xpath("//input[@type='search']")).sendKeys(Title);
        Thread.sleep(2500);
		driver.findElement(By.xpath("//*[@id='l_com_sirionlabs_model_MasterGroup']/tbody/tr/td/div[@sort='"+Title+"']/a")).click();
        Thread.sleep(5000);
        String entityTypeShowPage = driver.findElement(By.id("__entityType?.name_id")).getText();
        Assertion(entityTypeShowPage,  "Entity Type at show page is -- " +entityTypeShowPage+ " instead of -- " +entityType);

        String nameShowPage = driver.findElement(By.id("__name_id")).getText();
        Assertion(entityTypeShowPage, "Entity Type at show page is -- " +nameShowPage+ " instead of -- " +Title);

        String typeShowPage = driver.findElement(By.id("__notificationType?.name_id")).getText();
        Assertion(entityTypeShowPage, "Entity Type at show page is -- " +typeShowPage+ " instead of -- " +alert_type);

        String conditionShowPage = driver.findElement(By.id("__condition_id")).getText();
        Assertion(entityTypeShowPage,  "Entity Type at show page is -- " +conditionShowPage+ " instead of -- " +condition);

        String toEmailShowPage = driver.findElement(By.id("__selectedEmailTo_id")).getText();
        Assertion(entityTypeShowPage,  "Entity Type at show page is -- " +toEmailShowPage+ " instead of -- " +toEmail);

        String emailTemplateShowPage = driver.findElement(By.id("__entityActionEmail?.name_id")).getText();
        Assertion(entityTypeShowPage, "Entity Type at show page is -- " +emailTemplateShowPage+ " instead of -- " +emailTemplate);

        if(alert_type.equalsIgnoreCase("Time Based")) {
            String startDateShowPage = driver.findElement(By.id("__startDateField?.alias_id")).getText();
            Assertion(entityTypeShowPage, "Entity Type at show page is -- " + startDateShowPage + " instead of -- " + startDateField);

            String startDateOffsetShowPage = driver.findElement(By.id("__startDateOffset_id")).getText();
            Assertion(entityTypeShowPage,  "Entity Type at show page is -- " + startDateOffsetShowPage + " instead of -- " + convertStringToInteger(startDateOffset));

            String repeatIntervalShowPage = driver.findElement(By.id("__repeatInterval_id")).getText();
            Assertion(entityTypeShowPage,  "Entity Type at show page is -- " + repeatIntervalShowPage + " instead of -- " + convertStringToInteger(repeatInterval));
        }


        //For Actions Entity
        String acsupplierValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[0].selectedData.data_id")).getText();
        Assertion(acsupplierValueShowPage,  "Value of Supplier Filter at show page is -- " +acsupplierValueShowPage+ " instead of -- " +filter_ac_supplier);
        Logger.debug("Action Type created successfully, with Name -- " + filter_ac_supplier);

        String acpriorityValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[1].selectedData.data_id")).getText();
        Assertion(acpriorityValueShowPage,  "Value of Priority Filter at show page is -- " +acpriorityValueShowPage+ " instead of -- " +filter_ac_priority);
        Logger.debug("Action Type created successfully, with Name -- " + filter_ac_priority);

        String actypeValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[2].selectedData.data_id")).getText();
        Assertion(actypeValueShowPage,  "Value of Action-Type Filter at show page is -- " +actypeValueShowPage+ " instead of -- " +filter_ac_type);
        Logger.debug("Action Type created successfully, with Name -- " + filter_ac_type);

        String acgbValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[3].selectedData.data_id")).getText();
        Assertion(acgbValueShowPage,  "Value of Governance Bodies Filter at show page is -- " +acgbValueShowPage+ " instead of -- " +filter_ac_gb);
        Logger.debug("Action Type created successfully, with Name -- " + filter_ac_gb);

        //For Change Requests

        String crsupplierValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[0].selectedData.data_id")).getText();
        Assertion(crsupplierValueShowPage,  "Value of Supplier Filter at show page is -- " +crsupplierValueShowPage+ " instead of -- " +filter_cr_supplier);

        String crpriorityValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[1].selectedData.data_id")).getText();
        Assertion(crpriorityValueShowPage,  "Value of Priority Filter at show page is -- " +crpriorityValueShowPage+ " instead of -- " +filter_cr_priority);

        String crclassValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[2].selectedData.data_id")).getText();
        Assertion(crclassValueShowPage,  "Value of Change Request - Class filter at show page is -- " +crclassValueShowPage+ " instead of -- " +filter_cr_class);

        String crtypeValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[3].selectedData.data_id")).getText();
        Assertion(crtypeValueShowPage,  "Value of Change Request Type Filter at show page is -- " +crtypeValueShowPage+ " instead of -- " +filter_cr_type);

        String crstatusValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[4].selectedData.data_id")).getText();
        Assertion(crstatusValueShowPage,  "Value of Change Request Status Filter at show page is -- " +crstatusValueShowPage+ " instead of -- " +filter_cr_status);

        //For Child Obligations

        String cobsupplierValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[0].selectedData.data_id")).getText();
        Assertion(cobsupplierValueShowPage,  "Value of Supplier Filter at show page is -- " +cobsupplierValueShowPage+ " instead of -- " +filter_cob_supplier);

        String cobstatusValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[1].selectedData.data_id")).getText();
        Assertion(cobstatusValueShowPage,  "Value of Status Filter at show page is -- " +cobstatusValueShowPage+ " instead of -- " +filter_cob_status);

        String cobcategoryValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[2].selectedData.data_id")).getText();
        Assertion(cobcategoryValueShowPage,  "Value of Child Obligation - Category filter at show page is -- " +cobcategoryValueShowPage+ " instead of -- " +filter_cob_category);

        String cobperformancetypeValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[3].selectedData.data_id")).getText();
        Assertion(cobperformancetypeValueShowPage,  "Value of Child Obligation Performance - Type Filter at show page is -- " +cobperformancetypeValueShowPage+ " instead of -- " +filter_cob_performance_type);

        String cobperformancestatusValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[4].selectedData.data_id")).getText();
        Assertion(cobperformancestatusValueShowPage,  "Value of Child Obligation Performance - Status Filter at show page is -- " +cobperformancestatusValueShowPage+ " instead of -- " +filter_cob_performance_status);


        //for Child Service Levels

        String cslasupplierValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[0].selectedData.data_id")).getText();
        Assertion(cslasupplierValueShowPage,  "Value of Supplier Filter at show page is -- " +cslasupplierValueShowPage+ " instead of -- " +filter_csl_supplioer);

        String cslastatusValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[1].selectedData.data_id")).getText();
        Assertion(cslastatusValueShowPage,  "Value of Priority Filter at show page is -- " +cslastatusValueShowPage+ " instead of -- " +filter_csl_status);

        String cslaPerformanceStatusValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[2].selectedData.data_id")).getText();
        Assertion(cslaPerformanceStatusValueShowPage,  "Value of Child Service Levels - Performance Status filter at show page is -- " +cslaPerformanceStatusValueShowPage+ " instead of -- " +filter_csl_performance_status);

        String cslaCategoryValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[3].selectedData.data_id")).getText();
        Assertion(cslaCategoryValueShowPage,  "Value of Child Service Levels - Category Filter at show page is -- " +cslaCategoryValueShowPage+ " instead of -- " +filter_csl_sl_category);

        String cslaSubCategoryValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[4].selectedData.data_id")).getText();
        Assertion(cslaSubCategoryValueShowPage,  "Value of Child Service Levels - Sub-Category Filter at show page is -- " +cslaSubCategoryValueShowPage+ " instead of -- " +filter_csl_sl_sub_category);

        //For Contracts

        String consupplierValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[0].selectedData.data_id")).getText();
        Assertion(consupplierValueShowPage,  "Value of Supplier Filter at show page is -- " +consupplierValueShowPage+ " instead of -- " +filter_con_supplier);

        String conStatusValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[1].selectedData.data_id")).getText();
        Assertion(conStatusValueShowPage,  "Value of Status Filter at show page is -- " +conStatusValueShowPage+ " instead of -- " +filter_con_status);

        String conTCVMinValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[2].minValue_id")).getText();
        Assertion(conTCVMinValueShowPage,  "Value of Contract - TCV Min filter at show page is -- " +conTCVMinValueShowPage+ " instead of -- " +filter_con_tcv_min);

        String conTCVMaxValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[2].maxValue_id")).getText();
        Assertion(conTCVMaxValueShowPage,  "Value of Contract - TCV Max Filter at show page is -- " +conTCVMaxValueShowPage+ " instead of -- " +filter_con_tcv_max);

        String conTermTypeValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[3].selectedData.data_id")).getText();
        Assertion(conTermTypeValueShowPage,  "Value of Contract Term Type Filter at show page is -- " +conTermTypeValueShowPage+ " instead of -- " +filter_con_term_type);


        //For Disputes

        String disputesupplierValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[0].selectedData.data_id")).getText();
        Assertion(disputesupplierValueShowPage,  "Value of Supplier Filter at show page is -- " +disputesupplierValueShowPage+ " instead of -- " +filter_dispute_supplier);

        String disputepriorityValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[1].selectedData.data_id")).getText();
        Assertion(disputepriorityValueShowPage,  "Value of Priority Filter at show page is -- " +disputepriorityValueShowPage+ " instead of -- " +filter_dispute_type);

        String disputeclassValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[2].selectedData.data_id")).getText();
        Assertion(disputeclassValueShowPage,  "Value of Dispute - Class filter at show page is -- " +disputeclassValueShowPage+ " instead of -- " +filter_dispute_status);

        String disputetypeValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[3].selectedData.data_id")).getText();
        Assertion(disputetypeValueShowPage,  "Value of Dispute Type Filter at show page is -- " +disputetypeValueShowPage+ " instead of -- " +filter_dispute_gb);

        //Governance Bodies

        String gbsupplierValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[0].selectedData.data_id")).getText();
        Assertion(gbsupplierValueShowPage,  "Value of Supplier Filter at show page is -- " +gbsupplierValueShowPage+ " instead of -- " +filter_gb_supplier);

        String gbStatusValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[1].selectedData.data_id")).getText();
        Assertion(gbStatusValueShowPage,  "Value of Status Filter at show page is -- " +gbStatusValueShowPage+ " instead of -- " +filter_gb_status);

        String gbTypeValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[2].selectedData.data_id")).getText();
        Assertion(gbTypeValueShowPage,  "Value of Governance Body - Type filter at show page is -- " +gbTypeValueShowPage+ " instead of -- " +filter_gb_type);

        //Governance Body Meetings

        String gbmsupplierValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[0].selectedData.data_id")).getText();
        Assertion(gbmsupplierValueShowPage,  "Value of Supplier Filter at show page is -- " +gbmsupplierValueShowPage+ " instead of -- " +filter_gb_meeting_supplier);

        String gbmStatusValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[1].selectedData.data_id")).getText();
        Assertion(gbmStatusValueShowPage,  "Value of Status Filter at show page is -- " +gbmStatusValueShowPage+ " instead of -- " +filter_gb_meeting_status);

        String gbmTypeValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[2].selectedData.data_id")).getText();
        Assertion(gbmTypeValueShowPage,  "Value of Governance Body - Type filter at show page is -- " +gbmTypeValueShowPage+ " instead of -- " +filter_gb_meeting_gb_type);

        //For Issues

        String issuesupplierValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[0].selectedData.data_id")).getText();
        Assertion(issuesupplierValueShowPage,  "Value of Supplier Filter at show page is -- " +issuesupplierValueShowPage+ " instead of -- " +filter_issue_supplier);

        String issueTypeValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[1].selectedData.data_id")).getText();
        Assertion(issueTypeValueShowPage,  "Value of Issue - Type Filter at show page is -- " +issueTypeValueShowPage+ " instead of -- " +filter_issue_type);

        String issueStatusValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[2].selectedData.data_id")).getText();
        Assertion(issueStatusValueShowPage,  "Value of Status filter at show page is -- " +issueStatusValueShowPage+ " instead of -- " +filter_issue_status);

        String issueGBValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[3].selectedData.data_id")).getText();
        Assertion(issueGBValueShowPage,  "Value of Issues - Governance Bodies Filter at show page is -- " +issueGBValueShowPage+ " instead of -- " +filter_issue_governance_bodies);

        //For Obligations

        String obsupplierValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[0].selectedData.data_id")).getText();
        Assertion(obsupplierValueShowPage,  "Value of Supplier Filter at show page is -- " +obsupplierValueShowPage+ " instead of -- " +filter_ob_supplier);

        String obStatusValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[1].selectedData.data_id")).getText();
        Assertion(obStatusValueShowPage,  "Value of Status Filter at show page is -- " +obStatusValueShowPage+ " instead of -- " +filter_ob_status);

        String obCategoryValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[2].selectedData.data_id")).getText();
        Assertion(obCategoryValueShowPage,  "Value of Obligation Category filter at show page is -- " +obCategoryValueShowPage+ " instead of -- " +filter_ob_category);

        String obPerformanceTypeValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[3].selectedData.data_id")).getText();
        Assertion(obPerformanceTypeValueShowPage,  "Value of Obligation Performance Type Filter at show page is -- " +obPerformanceTypeValueShowPage+ " instead of -- " +filter_ob_performance_type);

        //For Service Levels

        String slasupplierValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[0].selectedData.data_id")).getText();
        Assertion(slasupplierValueShowPage,  "Value of Supplier Filter at show page is -- " +slasupplierValueShowPage+ " instead of -- " +filter_sla_supplioer);

        String slaStatusValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[1].selectedData.data_id")).getText();
        Assertion(slaStatusValueShowPage,  "Value of Status Filter at show page is -- " +slaStatusValueShowPage+ " instead of -- " +filter_sla_status);

        String slaCategoryValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[2].selectedData.data_id")).getText();
        Assertion(slaCategoryValueShowPage,  "Value of Service Levels - Category filter at show page is -- " +slaCategoryValueShowPage+ " instead of -- " +filter_sla_sl_category);

        String slaSubCategoryValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[3].selectedData.data_id")).getText();
        Assertion(slaSubCategoryValueShowPage,  "Value of Service Levels - Sub-Category Filter at show page is -- " +slaSubCategoryValueShowPage+ " instead of -- " +filter_sla_sl_sub_category);

        //For Work Order Request

        String worsupplierValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[0].selectedData.data_id")).getText();
        Assertion(worsupplierValueShowPage,  "Value of Supplier Filter at show page is -- " +worsupplierValueShowPage+ " instead of -- " +filter_wor_supplier);

        String worpriorityValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[1].selectedData.data_id")).getText();
        Assertion(worpriorityValueShowPage,  "Value of Priority Filter at show page is -- " +worpriorityValueShowPage+ " instead of -- " +filter_wor_priority);

        String worTypeValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[2].selectedData.data_id")).getText();
        Assertion(worTypeValueShowPage,  "Value of Change Request - Class filter at show page is -- " +worTypeValueShowPage+ " instead of -- " +filter_wor_type);

        String worStatusValueShowPage = driver.findElement(By.id("__commonFilterMetadatas[3].selectedData.data_id")).getText();
        Assertion(worStatusValueShowPage,  "Value of Change Request Type Filter at show page is -- " +worStatusValueShowPage+ " instead of -- " +filter_wor_status);

        Logger.debug("Action Type created successfully, with Name -- " + entityType);
		fail=false;
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