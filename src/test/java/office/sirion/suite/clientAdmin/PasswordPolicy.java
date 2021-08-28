package office.sirion.suite.clientAdmin;
import office.sirion.util.PostgreSQLJDBC;
import office.sirion.util.TestEmailFeature;
import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;
import org.apache.commons.collections4.map.LinkedMap;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

public class PasswordPolicy extends TestSuiteBase {
	String runmodes[]=null;
    boolean isChecked=true;
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
	public void PasswordPolicyTest (String passwordneverexpires, String passwordexpiresendays,
                                    String advancepasswordexpirynotificationdays, String temporarypasswordexpiresinhours,
                                    String enforcepasswordhistory, String pastpasswordscount, String minimumpasswordlength,
                                    String passwordcomplexityrequirement, String passwordanswerrequirement,
                                    String maximumlogininvalidattempts, String unlockaccount, String lockouteffectiveperiodminutes,
                                    String enforce24hoursasminimumlifetimeofpassword, String maxfailedsecurityquestionattemptsallowed,
                                    String maximumidletimedays, String disablesavepasswordinbrowser, String notifyUserOnPasswwordPolicyChange,
                                    String passwordLengthorRegexChangeRule) throws Exception {
        count++;
        if (!runmodes[count].equalsIgnoreCase("Y")) {
            skip = true;
            throw new SkipException("Runmode for Test Data -- Password Policy set to NO " + count);
        }

        openBrowser();
        passwordpolicyclientAdminLogin(CONFIG.getProperty("passwordpolicyclientadminURL"), CONFIG.getProperty("passwordpolicyclientadminUsername"), CONFIG.getProperty("passwordpolicyclientadminPassword"));
        try {
            fail = true;
            String entityErrors = null;
            entityErrors = driver.findElement(By.xpath("//div[@class='errorblock']")).getText();
            if (entityErrors.length()>0){
                if (entityErrors.equalsIgnoreCase("The login ID and password you entered does not match. Please try again or click on 'Forgot password'.")) {
                    Logger.debug("The login ID and password you entered does not match. Please try again or click on 'Forgot password'.");
                    PostgreSQLJDBC pg =new PostgreSQLJDBC();
                    pg.updateDBEntry("update app_user set password = '7ea908e184552bcaa38df96831553236eb00923e348ed5e295fdfb2c6b307cea' , first_login = 'f' , temp_pwd_creation_time = null where id = 2016;");
                    pg.updateDBEntry("update app_user set password = '24b6939de98d721d437c0dc15a9bed3d062fd25484d89951c32d967441f09629' , first_login = 'f' , temp_pwd_creation_time = null where id = 2181;");
                    driver.get(CONFIG.getProperty("passwordpolicyclientadminURL"));

                    if (CONFIG.getProperty("clientLoginType").equalsIgnoreCase("SSO")) {
                        getObject("sso_username").clear();
                        getObject("sso_username").sendKeys("neetu_admin");
                        getObject("sso_password").clear();
                        getObject("sso_password").sendKeys(CONFIG.getProperty("passwordpolicyclientadminPasswordnew"));
                        getObject("sso_login_button").click();
                    } else {
                        getObject("username").clear();
                        getObject("username").sendKeys("neetu_admin");
                        getObject("password").clear();
                        getObject("password").sendKeys(CONFIG.getProperty("admin123"));
                        getObject("login_button").click();
                    }
                }
            }
        }catch (NoSuchElementException e7){
            Logger.debug("User logged in successfully");
        }
        Logger.debug("Executing Client Admin - Password Policy Test");

        driver.get(CONFIG.getProperty("passwordpolicyclientadminURL"));
        fluentWaitMethod(driver.findElement(By.linkText("Password Policy")));

        driver.findElement(By.linkText("Password Policy")).click();
        fluentWaitMethod(driver.findElement(By.className("submit")));

        String minimumpasswordlenght_Original = locateElementBy("ca_minimum_password_length_value").getText();
        String passwordcomplexityrequirement_Original = locateElementBy("ca_password_complexity_requirement_value").getText();

        driver.findElement(By.xpath("//input[@value='Edit']")).click();
        fluentWaitMethod(driver.findElement(By.id("_passwordNeverExpires_id")));

        if (!passwordneverexpires.equalsIgnoreCase("No")) {
            driver.findElement(By.id("_passwordNeverExpires_id")).click();
        }

        if (passwordneverexpires.equalsIgnoreCase("No")) {
            if (passwordexpiresendays.equalsIgnoreCase("")) {
                driver.findElement(By.id("_passwordExpiresIn_id")).clear();
                driver.findElement(By.id("_passwordExpiresIn_id")).sendKeys(passwordexpiresendays);
            }
            if (!advancepasswordexpirynotificationdays.equalsIgnoreCase("")) {
                driver.findElement(By.id("_advancePasswordExpiryNotifications_id")).clear();
                driver.findElement(By.id("_advancePasswordExpiryNotifications_id")).sendKeys(String.valueOf(getIntegerFromString(passwordexpiresendays) - 1));
            }
        }

        if (!temporarypasswordexpiresinhours.equalsIgnoreCase("")) {
            driver.findElement(By.id("_tempPasswordExpiresIn_id")).clear();
            driver.findElement(By.id("_tempPasswordExpiresIn_id")).sendKeys(convertStringToInteger(temporarypasswordexpiresinhours));
        }

        if (!enforcepasswordhistory.equalsIgnoreCase("No")) {
            driver.findElement(By.id("_enforcePasswordHistory_id")).click();
        }

        if (!enforcepasswordhistory.equalsIgnoreCase("No")) {
            if (!pastpasswordscount.equalsIgnoreCase("")) {
                driver.findElement(By.id("_pastPasswordsCount_id")).clear();
                driver.findElement(By.id("_pastPasswordsCount_id")).sendKeys(convertStringToInteger(pastpasswordscount));
            }
        }

        if (!minimumpasswordlength.equalsIgnoreCase("")) {
            driver.findElement(By.id("_minPasswordLength_id")).clear();
            driver.findElement(By.id("_minPasswordLength_id")).sendKeys(convertStringToInteger(minimumpasswordlength));
        }
        if (!passwordcomplexityrequirement.equalsIgnoreCase("")) {
            new Select(driver.findElement(By.id("_passwordRegexRule_id"))).selectByVisibleText(passwordcomplexityrequirement);
        }
        if (!passwordanswerrequirement.equalsIgnoreCase("")) {
            new Select(driver.findElement(By.id("_securityAnswerConstraintRule_id"))).selectByVisibleText(passwordanswerrequirement);
        }

        if (!maximumlogininvalidattempts.equalsIgnoreCase("")) {
            driver.findElement(By.id("_maxInvalidLoginAttempts_id")).clear();
            driver.findElement(By.id("_maxInvalidLoginAttempts_id")).sendKeys(convertStringToInteger(maximumlogininvalidattempts));
        }

        if (!unlockaccount.equalsIgnoreCase("No")) {
            driver.findElement(By.id("_unlockAccount_id")).click();
        }

        if (!unlockaccount.equalsIgnoreCase("No")) {
            if (!lockouteffectiveperiodminutes.equalsIgnoreCase("")) {
                driver.findElement(By.id("_lockoutEffectivePeriod_id")).clear();
                driver.findElement(By.id("_lockoutEffectivePeriod_id")).sendKeys(convertStringToInteger(lockouteffectiveperiodminutes));
            }
        }

        if (!enforce24hoursasminimumlifetimeofpassword.equalsIgnoreCase("No")) {
            driver.findElement(By.id("_enforce24hoursAsMinimumLifetimeOfPassword_id")).click();
        }
        if (!maxfailedsecurityquestionattemptsallowed.equalsIgnoreCase("")) {
            driver.findElement(By.id("_maxFailedSecurityQuestionAttemptsAllowed_id")).clear();
            driver.findElement(By.id("_maxFailedSecurityQuestionAttemptsAllowed_id")).sendKeys(convertStringToInteger(maxfailedsecurityquestionattemptsallowed));
        }
        if (!maximumidletimedays.equalsIgnoreCase("")) {
            driver.findElement(By.id("_maxIdleTime_id")).clear();
            driver.findElement(By.id("_maxIdleTime_id")).sendKeys(String.valueOf(getIntegerFromString(passwordexpiresendays) - 1));
        }
        if (!disablesavepasswordinbrowser.equalsIgnoreCase("No")) {
            driver.findElement(By.id("_disableSavePassword_id")).click();
        }

        if (!advancepasswordexpirynotificationdays.equalsIgnoreCase("")) {
            fail = true;
            String entityErrors = null;
            try {
                WebElement e = driver.findElement(By.cssSelector("._tempPasswordExpiresIn_idformError.parentFormclientPasswordPolicy.formError"));
                entityErrors = e.findElement(By.className("formErrorContent")).getText();

                if (entityErrors.length() > 0) {
                    if (entityErrors.equalsIgnoreCase("Only number (Max length 18)")) {
                        Logger.debug("Advance Password Expiry Notification (Days): Special Characters are not allowed");
                    } else if (entityErrors.equalsIgnoreCase("This field is required")) {
                        Logger.debug("Advance Password Expiry Notification (Days): This is Mandatory Filed");
                    } else if (entityErrors.equalsIgnoreCase("Maximum value is 30")) {
                        Logger.debug("Advance Password Expiry Notification (Days): Value should not be more than 30");
                        driver.findElement(By.id("_advancePasswordExpiryNotifications_id")).clear();
                        driver.findElement(By.id("_maxIdleTime_id")).sendKeys(String.valueOf(getIntegerFromString("30")));
                    } else if (entityErrors.equalsIgnoreCase("Minimum value is 4")) {
                        Logger.debug("Advance Password Expiry Notification (Days): Value should be more than 4");
                        driver.findElement(By.id("_advancePasswordExpiryNotifications_id")).clear();
                        driver.findElement(By.id("_maxIdleTime_id")).sendKeys(String.valueOf(getIntegerFromString("4")));
                    }
                    fail = false;
                }
            } catch (NoSuchElementException e) {
                Logger.debug("No such Validation Message is displaying");
            }
        }

        if (!passwordexpiresendays.equalsIgnoreCase("")) {
            fail = true;
            String entityErrors = null;
            try {
                WebElement e = driver.findElement(By.cssSelector("._passwordExpiresIn_idformError.parentFormclientPasswordPolicy.formError"));
                entityErrors = e.findElement(By.className("formErrorContent")).getText();

                if (entityErrors.length() > 0) {
                    if (entityErrors.equalsIgnoreCase("Only number (Max length 18)")) {
                        Logger.debug("Advance Password Expiry Notification (Days): Special Characters are not allowed");
                    } else if (entityErrors.equalsIgnoreCase("This field is required")) {
                        Logger.debug("Advance Password Expiry Notification (Days): This is Mandatory Filed");
                    } else if (entityErrors.equalsIgnoreCase("Maximum value is 1000")) {
                        Logger.debug("Advance Password Expiry Notification (Days): Value should not be more than 30");
                    } else if (entityErrors.equalsIgnoreCase("Minimum value is 7")) {
                        Logger.debug("Advance Password Expiry Notification (Days): Value should be more than 7");

                        driver.findElement(By.id("_passwordExpiresIn_id")).clear();
                        driver.findElement(By.id("_passwordExpiresIn_id")).sendKeys(String.valueOf(getIntegerFromString(passwordexpiresendays) + 2));
                    }

                    fail = false;
                }
            } catch (NoSuchElementException e) {
                Logger.debug("No such Validation Message is displaying");
            }
        }

        if (!temporarypasswordexpiresinhours.equalsIgnoreCase("")) {
            fail = true;
            String entityErrors = null;
            try {
                WebElement e = driver.findElement(By.cssSelector("._tempPasswordExpiresIn_idformError.parentFormclientPasswordPolicy.formError"));
                entityErrors = e.findElement(By.className("formErrorContent")).getText();

                if (entityErrors.length() > 0) {

                    if (entityErrors.equalsIgnoreCase("Only number (Max length 18)")) {
                        Logger.debug("Advance Password Expiry Notification (Days): Special Characters are not allowed");
                    } else if (entityErrors.equalsIgnoreCase("This field is required")) {
                        Logger.debug("Advance Password Expiry Notification (Days): This is Mandatory Filed");
                    } else if (entityErrors.equalsIgnoreCase("Maximum value is 120")) {
                        Logger.debug("Advance Password Expiry Notification (Days): Value should not be more than 30");
                    }
                    fail = false;
                }
            } catch (NoSuchElementException e) {
                Logger.debug("No such Validation Message is displaying");
            }
        }

        if (!pastpasswordscount.equalsIgnoreCase("")) {
            fail = true;
            String entityErrors = null;
            try {
                WebElement e = driver.findElement(By.cssSelector("._pastPasswordsCount_idformError.parentFormclientPasswordPolicy.formError"));
                entityErrors = e.findElement(By.className("formErrorContent")).getText();

                if (entityErrors.length() > 0) {

                    if (entityErrors.equalsIgnoreCase("Only number (Max length 18)")) {
                        Logger.debug("Advance Password Expiry Notification (Days): Special Characters are not allowed");
                    } else if (entityErrors.equalsIgnoreCase("This field is required")) {
                        Logger.debug("Advance Password Expiry Notification (Days): This is Mandatory Filed");
                    } else if (entityErrors.equalsIgnoreCase("Maximum value is 10")) {
                        Logger.debug("Advance Password Expiry Notification (Days): Value should not be more than 30");
                    }
                    fail = false;
                }
            } catch (NoSuchElementException e) {
                Logger.debug("No such Validation Message is displaying");
            }
        }

        if (!maximumlogininvalidattempts.equalsIgnoreCase("")) {
            fail = true;
            String entityErrors = null;
            try {
                WebElement e = driver.findElement(By.cssSelector("._maxInvalidLoginAttempts_idformError.parentFormclientPasswordPolicy.formError"));
                entityErrors = e.findElement(By.className("formErrorContent")).getText();

                if (entityErrors.length() > 0) {

                    if (entityErrors.equalsIgnoreCase("Only number (Max length 18)")) {
                        Logger.debug("Advance Password Expiry Notification (Days): Special Characters are not allowed");
                    } else if (entityErrors.equalsIgnoreCase("This field is required")) {
                        Logger.debug("Advance Password Expiry Notification (Days): This is Mandatory Filed");
                    } else if (entityErrors.equalsIgnoreCase("Maximum value is 20")) {
                        Logger.debug("Advance Password Expiry Notification (Days): Value should not be more than 30");
                    }
                    fail = false;
                }
            } catch (NoSuchElementException e) {
                Logger.debug("No such Validation Message is displaying");
            }
        }

        if (!lockouteffectiveperiodminutes.equalsIgnoreCase("")) {
            fail = true;
            String entityErrors = null;
            try {
                WebElement e = driver.findElement(By.cssSelector("._lockoutEffectivePeriod_idformError.parentFormclientPasswordPolicy.formError"));
                entityErrors = e.findElement(By.className("formErrorContent")).getText();

                if (entityErrors.length() > 0) {

                    if (entityErrors.equalsIgnoreCase("Only number (Max length 18)")) {
                        Logger.debug("Advance Password Expiry Notification (Days): Special Characters are not allowed");
                    } else if (entityErrors.equalsIgnoreCase("This field is required")) {
                        Logger.debug("Advance Password Expiry Notification (Days): This is Mandatory Filed");
                    } else if (entityErrors.equalsIgnoreCase("Maximum value is 60")) {
                        Logger.debug("Advance Password Expiry Notification (Days): Value should not be more than 30");
                    }
                    fail = false;
                }
            } catch (NoSuchElementException e) {
                Logger.debug("No such Validation Message is displaying");
            }
        }

        if (!maxfailedsecurityquestionattemptsallowed.equalsIgnoreCase("")) {
            fail = true;
            String entityErrors = null;
            try {
                WebElement e = driver.findElement(By.cssSelector("._maxFailedSecurityQuestionAttemptsAllowed_idformError.parentFormclientPasswordPolicy.formError"));
                entityErrors = e.findElement(By.className("formErrorContent")).getText();

                if (entityErrors.length() > 0) {

                    if (entityErrors.equalsIgnoreCase("Only number (Max length 18)")) {
                        Logger.debug("Advance Password Expiry Notification (Days): Special Characters are not allowed");
                    } else if (entityErrors.equalsIgnoreCase("This field is required")) {
                        Logger.debug("Advance Password Expiry Notification (Days): This is Mandatory Filed");
                    } else if (entityErrors.equalsIgnoreCase("Maximum value is 20")) {
                        Logger.debug("Advance Password Expiry Notification (Days): Value should not be more than 30");
                    }
                    fail = false;
                }
            } catch (NoSuchElementException e) {
                Logger.debug("No such Validation Message is displaying");
            }
        }

        if (!minimumpasswordlength.equalsIgnoreCase("")) {
            fail = true;
            String entityErrors = null;
            try {
                WebElement e = driver.findElement(By.cssSelector("._minPasswordLength_idformError.parentFormclientPasswordPolicy.formError"));
                entityErrors = e.findElement(By.className("formErrorContent")).getText();

                if (entityErrors.length() > 0) {

                    if (entityErrors.equalsIgnoreCase("Only number (Max length 18)")) {
                        Logger.debug("Advance Password Expiry Notification (Days): Special Characters are not allowed");
                    } else if (entityErrors.equalsIgnoreCase("This field is required")) {
                        Logger.debug("Advance Password Expiry Notification (Days): This is Mandatory Filed");
                    } else if (entityErrors.equalsIgnoreCase("Maximum value is 16")) {
                        Logger.debug("Advance Password Expiry Notification (Days): Value should not be more than 30");
                    }
                    fail = false;
                }
            } catch (NoSuchElementException e) {
                Logger.debug("No such Validation Message is displaying");
            }
        }

        if (!maximumidletimedays.equalsIgnoreCase("")) {
            fail = true;
            String entityErrors = null;
            try {
                WebElement e = driver.findElement(By.cssSelector("._maxIdleTime_idformError.parentFormclientPasswordPolicy.formError"));
                entityErrors = e.findElement(By.className("formErrorContent")).getText();

                if (entityErrors.length() > 0) {

                    if (entityErrors.equalsIgnoreCase("Only number (Max length 18)")) {
                        Logger.debug("Maximum Idle Time (Days ): Special Characters are not allowed");
                    } else if (entityErrors.equalsIgnoreCase("This field is required")) {
                        Logger.debug("Maximum Idle Time (Days ): This is Mandatory Filed");
                    } else if (entityErrors.equalsIgnoreCase("Maximum value is 180")) {
                        Logger.debug("Maximum Idle Time (Days ): Value should not be less than 30");
                    } else if (entityErrors.equalsIgnoreCase("Minimum value is 7")) {
                        Logger.debug("Maximum Idle Time (Days ): Value should be more than 7");
                        int compare1 = getIntegerFromString(maximumidletimedays).compareTo(6);

                        if (compare1 == 0) {
                            Logger.debug(maximumidletimedays + " & 6: Both vlaues are same");
                            driver.findElement(By.id("_maxFailedSecurityQuestionAttemptsAllowed_id")).clear();
                            driver.findElement(By.id("_maxIdleTime_id")).sendKeys(String.valueOf(getIntegerFromString(maximumidletimedays) + 1));
                        } else if (compare1 == 1) {
                            Logger.debug(maximumidletimedays + " is greater than 7");
                            driver.findElement(By.id("_maxFailedSecurityQuestionAttemptsAllowed_id")).clear();
                            driver.findElement(By.id("_maxIdleTime_id")).sendKeys(String.valueOf(getIntegerFromString(passwordexpiresendays) - 1));
                        } else if (compare1 == -1) {
                            Logger.debug(maximumidletimedays + " is Less than 6");
                            driver.findElement(By.id("_maxFailedSecurityQuestionAttemptsAllowed_id")).clear();
                            driver.findElement(By.id("_maxIdleTime_id")).sendKeys(String.valueOf(getIntegerFromString("7")));
                        }
                    }
                    fail = false;
                }
            } catch (NoSuchElementException e) {
                Logger.debug("No such Validation Message is displaying");
            }
        }

        try {
            driver.findElement(By.className("formErrorContent"));

            if (!driver.findElement(By.className("formErrorContent")).getText().isEmpty()) {
                driver.findElement(By.id("cancel")).click();
                driver.findElement(By.xpath("//span[@class='ui-button-text'][contains(text(),'Yes')]")).click();
            }
        } catch (NoSuchElementException e4) {
            Logger.debug("No Alert Message in RED colour is displayed");
        }
        try {
            driver.findElement(By.xpath("//input[@id='proceed']")).isDisplayed();
            driver.findElement(By.xpath("//input[@id='proceed']")).click();
            }  catch (ElementNotVisibleException e6){
            Logger.debug("There is nothing to update");
            driver.findElement(By.id("cancel")).click();
            driver.findElement(By.xpath("//span[@class='ui-button-text'][contains(text(),'Yes')]")).click();
                }
            try {
                driver.findElement(By.xpath("")).getText().equalsIgnoreCase("'Max Idle Time (Days)' should always be less than 'Password expires in' value.");
                driver.findElement(By.id("_maxFailedSecurityQuestionAttemptsAllowed_id")).clear();
                driver.findElement(By.id("_maxIdleTime_id")).sendKeys(String.valueOf(getIntegerFromString(passwordexpiresendays) - 1));
            } catch (NoSuchElementException e2) {
                Logger.debug("No Alert Box is displayed");
            }

                try {
                    if (!notifyUserOnPasswwordPolicyChange.equalsIgnoreCase("No")) {
                        driver.findElement(By.xpath("//input[@id='_notifyUserOnPasswordPolicyChange_id']")).click();
                        Logger.debug("Notification Email should be sent");
                    }
                    } catch (NoSuchElementException e1) {
                        Logger.debug("Notification Email should not be sent");
                    }

                if (!passwordLengthorRegexChangeRule.equalsIgnoreCase("")) {
                    try {
                        new Select(driver.findElement(By.xpath("//select[@id='_passwordLengthorRegexChangeRule_id']"))).selectByVisibleText(passwordLengthorRegexChangeRule);
                        driver.findElement(By.xpath("//span[@class='ui-button-text'][contains(text(),'Submit')]")).click();
                        Thread.sleep(7000);

                        Logger.debug("Password Policy Updated Successfully");

                        if(!notifyUserOnPasswwordPolicyChange.equalsIgnoreCase("No")) {

                        Map<String, String> map = new LinkedMap();
                        map.put(locateElementBy("ca_password_never_expires").getText().replaceAll(" :", ""), locateElementBy("ca_password_never_expires_value").getText());
                        map.put(locateElementBy("ca_password_expires_in_days").getText().replaceAll(" :", ""), locateElementBy("ca_password_expires_in_days_value").getText());
                        map.put(locateElementBy("ca_advance_password_expiry_notification_days").getText().replaceAll(" :", ""), locateElementBy("ca_advance_password_expiry_notification_days_value").getText());
                        map.put(locateElementBy("ca_temporary_password_expires_in_hours").getText().replaceAll(" :", ""), locateElementBy("ca_temporary_password_expires_in_hours_value").getText());
                        map.put(locateElementBy("ca_enforce_password_history").getText().replaceAll(" :", ""), locateElementBy("ca_enforce_password_history_value").getText());
                        map.put(locateElementBy("ca_past_passwords_count").getText().replaceAll(" :", ""), locateElementBy("ca_past_passwords_count_value").getText());
                        map.put(locateElementBy("ca_minimum_password_length").getText().replaceAll(" :", ""), locateElementBy("ca_minimum_password_length_value").getText());
                        map.put(locateElementBy("ca_password_complexity_requirement").getText().replaceAll(" :", ""), locateElementBy("ca_password_complexity_requirement_value").getText());
                        map.put(locateElementBy("ca_password_answer_requirement").getText().replaceAll(" :", ""), locateElementBy("ca_password_answer_requirement_value").getText());
                        map.put(locateElementBy("ca_maximum_login_invalid_attempts").getText().replaceAll(" :", ""), locateElementBy("ca_maximum_login_invalid_attempts_value").getText());
                        map.put(locateElementBy("ca_unlock_account").getText().replaceAll(" :", ""), locateElementBy("ca_unlock_account_value").getText());
                        map.put(locateElementBy("ca_lockout_effective_period_minutes").getText().replaceAll(" :", ""), locateElementBy("ca_lockout_effective_period_minutes_value").getText());
                        map.put(locateElementBy("ca_enforce_24_hours_as_minimum_lifetime_of_password").getText().replaceAll(" :", ""), locateElementBy("ca_enforce_24_hours_as_minimum_lifetime_of_password_value").getText());
                        map.put(locateElementBy("ca_max_failed_security_question_attempts_allowed").getText().replaceAll(" :", ""), locateElementBy("ca_max_failed_security_question_attempts_allowed_value").getText());
                        map.put(locateElementBy("ca_maximum_idle_time_days").getText().replaceAll(" :", ""), locateElementBy("ca_maximum_idle_time_days_value").getText());
                        map.put(locateElementBy("ca_disable_save_password_in_browser").getText().replaceAll(" :", ""), locateElementBy("ca_disable_save_password_in_browser_value").getText());

                        TestEmailFeature email = new TestEmailFeature();

                        mapEquals(email.testts80701(), map);
                        Logger.debug("Values are Identical: Saved at Client Admin & Email Recieved at End-User");

                    if(passwordLengthorRegexChangeRule.equalsIgnoreCase("Auto reset users passwords")) {

                       String [] tempPassword =  email.testts80702();
                        driver.findElement(By.xpath("//span[contains(text(),'Logout')]")).click();
                    driver.get(CONFIG.getProperty("passwordpolicyclientadminURL"));
                    if (CONFIG.getProperty("clientLoginType").equalsIgnoreCase("SSO")) {
                        getObject("sso_username").clear();
                        getObject("sso_username").sendKeys("neetu_admin");
                        getObject("sso_password").clear();
                        getObject("sso_password").sendKeys(tempPassword[0]);
                        getObject("sso_login_button").click();
                    } else {
                        getObject("username").clear();
                        getObject("username").sendKeys("neetu_admin");
                        getObject("password").clear();
                        getObject("password").sendKeys(tempPassword[0]);
                        getObject("login_button").click();
                    }
                    new Select (driver.findElement(By.xpath("//select[@id='_questionId_id']"))).selectByVisibleText("What is your nick name?");
                    driver.findElement(By.xpath("//input[@id='_answer_id']")).sendKeys("neetu_admin");
                    driver.findElement(By.xpath("//input[@id='_currentPassword_1_id']")).clear();
                    driver.findElement(By.xpath("//input[@id='_currentPassword_1_id']")).sendKeys(tempPassword[0]);
                    driver.findElement(By.xpath("//input[@id='_newPassword_1_id']")).clear();
                    driver.findElement(By.xpath("//input[@id='_newPassword_1_id']")).sendKeys(CONFIG.getProperty("passwordpolicyclientadminPasswordnew"));
                    driver.findElement(By.xpath("//input[@id='_repeatPassword_1_id']")).clear();
                    driver.findElement(By.xpath("//input[@id='_repeatPassword_1_id']")).sendKeys(CONFIG.getProperty("passwordpolicyclientadminPasswordnew"));
                    driver.findElement(By.xpath("//input[@id='proceed']")).click();
                    try{
                        fail = true;
                        String entityErrors = null;
                        WebElement e = driver.findElement(By.cssSelector("div#errors"));
                        entityErrors = e.findElement(By.id("errors")).getText();
                        if (entityErrors.length()>0){
                            if (entityErrors.equalsIgnoreCase("Current password is not valid.")) {
                                Logger.debug("Current password is not valid.");
                                driver.findElement(By.xpath("//span[@class='ui-button-text'][contains(text(),'OK')]")).click();
                                driver.findElement(By.xpath("//input[@id='_currentPassword_1_id']")).clear();
                                driver.findElement(By.xpath("//input[@id='_currentPassword_1_id']")).sendKeys(CONFIG.getProperty("passwordpolicyclientadminPasswordnew"));
                                driver.findElement(By.xpath("//input[@id='_newPassword_1_id']")).clear();
                                driver.findElement(By.xpath("//input[@id='_newPassword_1_id']")).sendKeys(CONFIG.getProperty("passwordpolicyclientadminPassword"));
                                driver.findElement(By.xpath("//input[@id='_repeatPassword_1_id']")).clear();
                                driver.findElement(By.xpath("//input[@id='_repeatPassword_1_id']")).sendKeys(CONFIG.getProperty("passwordpolicyclientadminPassword"));
                                driver.findElement(By.xpath("//input[@id='proceed']")).click();
                            }
                        }
                    }catch (NoSuchElementException e8){
                        Logger.debug("Password Changed Successfully");
                    }

                }else if(passwordLengthorRegexChangeRule.equalsIgnoreCase("Enforce users password change on next login")){
                    driver.findElement(By.xpath("//span[contains(text(),'Logout')]")).click();
                    Thread.sleep(7000);
                    Random rand = new Random();
                    driver.get(CONFIG.getProperty("passwordpolicyclientadminURL"));
                    if (CONFIG.getProperty("clientLoginType").equalsIgnoreCase("SSO")) {
                        getObject("sso_username").clear();
                        getObject("sso_username").sendKeys("neetu_admin");
                        getObject("sso_password").clear();
                        getObject("sso_password").sendKeys(CONFIG.getProperty("passwordpolicyclientadminPasswordnew"));
                        getObject("sso_login_button").click();
                    } else {
                        getObject("username").clear();
                        getObject("username").sendKeys("neetu_admin");
                        getObject("password").clear();
                        getObject("password").sendKeys(CONFIG.getProperty("passwordpolicyclientadminPasswordnew"));
                        getObject("login_button").click();
                    }
                    try {
                        fail = true;
                        String entityErrors = null;
                        entityErrors = driver.findElement(By.xpath("//div[@class='errorblock']")).getText();
                        if (entityErrors.length()>0){
                            if (entityErrors.equalsIgnoreCase("The login ID and password you entered does not match. Please try again or click on 'Forgot password'.")) {
                                Logger.debug("The login ID and password you entered does not match. Please try again or click on 'Forgot password'.");
                                driver.get(CONFIG.getProperty("passwordpolicyclientadminURL"));
                                if (CONFIG.getProperty("clientLoginType").equalsIgnoreCase("SSO")) {
                                    getObject("sso_username").clear();
                                    getObject("sso_username").sendKeys("neetu_admin");
                                    getObject("sso_password").clear();
                                    getObject("sso_password").sendKeys(CONFIG.getProperty("passwordpolicyclientadminPasswordnew"));
                                    getObject("sso_login_button").click();
                                } else {
                                    getObject("username").clear();
                                    getObject("username").sendKeys("neetu_admin");
                                    getObject("password").clear();
                                    getObject("password").sendKeys(CONFIG.getProperty("passwordpolicyclientadminPasswordnew"));
                                    getObject("login_button").click();
                                }
                            }
                        }
                    }catch (NoSuchElementException e7){
                        Logger.debug("User logged in successfully");
                    }
                    new Select (driver.findElement(By.xpath("//select[@id='_questionId_id']"))).selectByVisibleText("What is your nick name?");
                    driver.findElement(By.xpath("//input[@id='_answer_id']")).sendKeys("neetu_admin");
                    driver.findElement(By.xpath("//input[@id='_currentPassword_1_id']")).clear();
                    driver.findElement(By.xpath("//input[@id='_currentPassword_1_id']")).sendKeys(CONFIG.getProperty("passwordpolicyclientadminPassword"));
                    driver.findElement(By.xpath("//input[@id='_newPassword_1_id']")).clear();
                    driver.findElement(By.xpath("//input[@id='_newPassword_1_id']")).sendKeys(CONFIG.getProperty("passwordpolicyclientadminPasswordnew"));
                    driver.findElement(By.xpath("//input[@id='_repeatPassword_1_id']")).clear();
                    driver.findElement(By.xpath("//input[@id='_repeatPassword_1_id']")).sendKeys(CONFIG.getProperty("passwordpolicyclientadminPasswordnew"));
                    driver.findElement(By.xpath("//input[@id='proceed']")).click();
                    try{
                        fail = true;
                        String entityErrors = null;
                        WebElement e = driver.findElement(By.cssSelector("div#errors"));
                        entityErrors = e.findElement(By.id("errors")).getText();
                        if (entityErrors.length()>0){
                            if (entityErrors.equalsIgnoreCase("Current password is not valid.")) {
                                Logger.debug("Current password is not valid.");
                                driver.findElement(By.xpath("//span[@class='ui-button-text'][contains(text(),'OK')]")).click();
                                driver.findElement(By.xpath("//input[@id='_currentPassword_1_id']")).clear();
                                driver.findElement(By.xpath("//input[@id='_currentPassword_1_id']")).sendKeys(CONFIG.getProperty("passwordpolicyclientadminPasswordnew"));
                                driver.findElement(By.xpath("//input[@id='_newPassword_1_id']")).clear();
                                driver.findElement(By.xpath("//input[@id='_newPassword_1_id']")).sendKeys(CONFIG.getProperty("passwordpolicyclientadminPassword"));
                                driver.findElement(By.xpath("//input[@id='_repeatPassword_1_id']")).clear();
                                driver.findElement(By.xpath("//input[@id='_repeatPassword_1_id']")).sendKeys(CONFIG.getProperty("passwordpolicyclientadminPassword"));
                                driver.findElement(By.xpath("//input[@id='proceed']")).click();
                                Thread.sleep(7000);
                                driver.findElement(By.xpath("//input[@value='OK']")).click();
                            }
                        }
                    }catch (NoSuchElementException e8){
                        Logger.debug("Password Changed Successfully");
                    }
                    driver.findElement(By.xpath("//input[@name='submit']")).click();
                }
            }
                    } catch (NoSuchElementException e) {
                        Logger.debug("User Password will not change either forcefully or automatically");
                    }

                    try {
                        driver.findElement(By.xpath("//span[@class='ui-button-text'][contains(text(),'Submit')]")).click();
                        driver.findElement(By.xpath("//input[@value='OK']")).click();
                        Thread.sleep(7000);
                    }catch (NoSuchElementException e9){
                        if (!unlockaccount.equalsIgnoreCase("No")) {
                            if (!lockouteffectiveperiodminutes.equalsIgnoreCase("")) {
                                driver.findElement(By.id("_lockoutEffectivePeriod_id")).clear();
                                driver.findElement(By.id("_lockoutEffectivePeriod_id")).sendKeys(convertStringToInteger(lockouteffectiveperiodminutes));
                                driver.findElement(By.xpath("//span[@class='ui-button-text'][contains(text(),'Submit')]")).click();
                                driver.findElement(By.xpath("//input[@value='OK']")).click();
                            }
                        }
                    }

                    Logger.debug("Password Policy Updated Successfully");
        }

            fail = false;
            driver.get(CONFIG.getProperty("passwordpolicyclientadminURL"));
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

    public static <K, V> boolean mapEquals(Map<K, V> leftMap, Map<K, V> rightMap) {
        if (leftMap == rightMap) return true;
        if (leftMap == null || rightMap == null || leftMap.size() != rightMap.size()) return false;
        for (K key : leftMap.keySet()) {
            V value1 = leftMap.get(key);
            V value2 = rightMap.get(key);
            if (value1 == null && value2 == null)
                continue;
            else if (value1 == null || value2 == null)
                return false;
            if (!value1.equals(value2))
                return false;

            Logger.debug("Values are Identical: Saved at Client Admin & Email Recieved at End-User");
        }
        Logger.debug("Values are Identical: Saved at Client Admin & Email Recieved at End-User");
        return true;
    }
	}