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

public class OrganizationProperties extends TestSuiteBase {
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
	public void OrganizationPropertiesTest (String emailUsername, String emailPassword, String emailAttachmentDir, String emailAttachmentLocation, String emailFolderToRead,
			String emailMailStoreProtocol, String emailMailImapHost, String reportDisclaimer1, String reportDisclaimer2, String reportDisclaimer3, String emailFooter,
			String forgotPasswordHelpLink, String lockedAccountHelpLink, String entityDateFormat,
			String entityTimestampFormat, String todoDueDateTimestampFormat, String volumeTrendMonthFormat, String excelDateFormat, String slaPerformanceDateFormat,
			String emailDateFormat, String systemBccEmail, String bulkDefaultEmail, String systemEmail, String fromEmail, String userActivityEmailRecipient,
			String emailMailImapPort, String smtpUsername, String smtpPassword, String smtpHost, String smtpPort, String mailSmtpAuth, String mailSmtpConnectiontimeout,
			String mailSmtpTimeout, String mailSmtpStarttlsEnable, String mailSmtpQuitwait, String antivirusScan, String samlUserRoleGroupAttributeName,
			String entityDatetimeFormat, String bulkmaxselectcount, String emailDatetimeFormat,
			String weeklyReminderEmailFileName, String ssoNewlogin, String ReportGraphViewDataLimit, String oandaAPIKey, String oandaConversionQuoteType,
			String conversionRateApiUpdateDate, String timeFormat, String maxcharlimittextareamultiselect,String notificationEmailHour,String versionPrefix,String versionStartsFrom,
            String appendInvoiceAndPONumberBulkAttachment,String supportContactInformationEmail,String dateConfigurationAuditandCommunication) throws InterruptedException {

		count++;
		if(!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data -- Organization Properties -- set to NO " +count);
		
		openBrowser();
		clientAdminLogin(CONFIG.getProperty("clientAdminURL"), CONFIG.getProperty("clientAdminUsername"), CONFIG.getProperty("clientAdminPassword"));

		Logger.debug("Executing Test Client Admin Organization Properties Test for -- " + CONFIG.getProperty("clientAdminURL"));

		driver.get(CONFIG.getProperty("clientAdminURL"));
		//
		fluentWaitMethod(driver.findElement(By.linkText("Organization Properties")));

		driver.findElement(By.linkText("Organization Properties")).click();
		//
		fluentWaitMethod(locateElementBy("ca_orgnisation_properties_email_username"));

		if (!emailUsername.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_email_username").clear();
			locateElementBy("ca_orgnisation_properties_email_username").sendKeys(emailUsername);
			}

		if (!emailPassword.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_email_password").clear();
			locateElementBy("ca_orgnisation_properties_email_password").sendKeys(emailPassword);
			}

		if (!emailAttachmentDir.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_email_attachment_directory").clear();
			locateElementBy("ca_orgnisation_properties_email_attachment_directory").sendKeys(emailAttachmentDir);
			}

		if (!emailAttachmentLocation.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_email_attachment_location").clear();
			locateElementBy("ca_orgnisation_properties_email_attachment_location").sendKeys(emailAttachmentLocation);
			}

		if (!emailFolderToRead.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_email_folder_to_read").clear();
			locateElementBy("ca_orgnisation_properties_email_folder_to_read").sendKeys(emailFolderToRead);
			}

		if (!emailMailStoreProtocol.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_email_mail_store_protocol").clear();
			locateElementBy("ca_orgnisation_properties_email_mail_store_protocol").sendKeys(emailMailStoreProtocol);
			}

		if (!emailMailImapHost.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_email_mail_imap_host").clear();
			locateElementBy("ca_orgnisation_properties_email_mail_imap_host").sendKeys(emailMailImapHost);
			}

		if (!reportDisclaimer1.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_report_disclaimer_1").clear();
			locateElementBy("ca_orgnisation_properties_report_disclaimer_1").sendKeys(reportDisclaimer1);
			}

		if (!reportDisclaimer2.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_report_disclaimer_2").clear();
			locateElementBy("ca_orgnisation_properties_report_disclaimer_2").sendKeys(reportDisclaimer2);
			}

		if (!reportDisclaimer3.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_report_disclaimer_3").clear();
			locateElementBy("ca_orgnisation_properties_report_disclaimer_3").sendKeys(reportDisclaimer3);
			}

		if (!emailFooter.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_email_footer").clear();
			locateElementBy("ca_orgnisation_properties_email_footer").sendKeys(emailFooter);
			}

		if (!forgotPasswordHelpLink.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_forgot_passwowrd_help_link").clear();
			locateElementBy("ca_orgnisation_properties_forgot_passwowrd_help_link").sendKeys(forgotPasswordHelpLink);
			}

		if (!lockedAccountHelpLink.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_locked_account_help_link").clear();
			locateElementBy("ca_orgnisation_properties_locked_account_help_link").sendKeys(lockedAccountHelpLink);
			}

		if (!entityDateFormat.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_entity_date_format").clear();
			locateElementBy("ca_orgnisation_properties_entity_date_format").sendKeys(entityDateFormat);
			}

		if (!entityTimestampFormat.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_entity_time_stamp_format").clear();
			locateElementBy("ca_orgnisation_properties_entity_time_stamp_format").sendKeys(entityTimestampFormat);
			}

		if (!todoDueDateTimestampFormat.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_todo_due_date_time_stamp_format").clear();
			locateElementBy("ca_orgnisation_properties_todo_due_date_time_stamp_format").sendKeys(todoDueDateTimestampFormat);
			}

		if (!volumeTrendMonthFormat.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_volume_trend_month_format").clear();
			locateElementBy("ca_orgnisation_properties_volume_trend_month_format").sendKeys(volumeTrendMonthFormat);
			}

		if (!excelDateFormat.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_excel_date_format").clear();
			locateElementBy("ca_orgnisation_properties_excel_date_format").sendKeys(excelDateFormat);
			}

		if (!slaPerformanceDateFormat.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_sla_performance_date_format").clear();
			locateElementBy("ca_orgnisation_properties_sla_performance_date_format").sendKeys(slaPerformanceDateFormat);
			}

		if (!emailDateFormat.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_email_date_format").clear();
			locateElementBy("ca_orgnisation_properties_email_date_format").sendKeys(emailDateFormat);
			}

		if (!systemBccEmail.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_system_bcc_email").clear();
			locateElementBy("ca_orgnisation_properties_system_bcc_email").sendKeys(systemBccEmail);
			}

		if (!bulkDefaultEmail.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_bulk_default_email").clear();
			locateElementBy("ca_orgnisation_properties_bulk_default_email").sendKeys(bulkDefaultEmail);
			}

		if (!systemEmail.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_system_email").clear();
			locateElementBy("ca_orgnisation_properties_system_email").sendKeys(systemEmail);
			}

		if (!fromEmail.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_from_email").clear();
			locateElementBy("ca_orgnisation_properties_from_email").sendKeys(fromEmail);
			}

		if (!userActivityEmailRecipient.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_user_activity_email_recipient").clear();
			locateElementBy("ca_orgnisation_properties_user_activity_email_recipient").sendKeys(userActivityEmailRecipient);
			}

		if (!emailMailImapPort.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_email_mail_imap_port").clear();
			locateElementBy("ca_orgnisation_properties_email_mail_imap_port").sendKeys(convertStringToInteger(emailMailImapPort));
			}

		if (!smtpUsername.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_smtp_username").clear();
			locateElementBy("ca_orgnisation_properties_smtp_username").sendKeys(smtpUsername);
			}

		if (!smtpPassword.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_smtp_password").clear();
			locateElementBy("ca_orgnisation_properties_smtp_password").sendKeys(smtpPassword);
			}

		if (!smtpHost.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_smtp_host").clear();
			locateElementBy("ca_orgnisation_properties_smtp_host").sendKeys(smtpHost);
			}

		if (!smtpPort.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_smtp_port").clear();
			locateElementBy("ca_orgnisation_properties_smtp_port").sendKeys(convertStringToInteger(smtpPort));
			}

		if (!mailSmtpAuth.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_mail_smtp_auth").clear();
			locateElementBy("ca_orgnisation_properties_mail_smtp_auth").sendKeys(mailSmtpAuth);
			}

		if (!mailSmtpConnectiontimeout.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_mail_smtp_connection_timeout").clear();
			locateElementBy("ca_orgnisation_properties_mail_smtp_connection_timeout").sendKeys(convertStringToInteger(mailSmtpConnectiontimeout));
			}

		if (!mailSmtpTimeout.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_mail_smtp_time_out").clear();
			locateElementBy("ca_orgnisation_properties_mail_smtp_time_out").sendKeys(convertStringToInteger(mailSmtpTimeout));
			}

		if (!mailSmtpStarttlsEnable.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_mail_smtp_start_tls_enable").clear();
			locateElementBy("ca_orgnisation_properties_mail_smtp_start_tls_enable").sendKeys(mailSmtpStarttlsEnable);
			}

		if (!mailSmtpQuitwait.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_mail_smtp_quit_wait").clear();
			locateElementBy("ca_orgnisation_properties_mail_smtp_quit_wait").sendKeys(mailSmtpQuitwait);
			}

		if (!antivirusScan.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_antivirus_scan").clear();
			locateElementBy("ca_orgnisation_properties_antivirus_scan").sendKeys(antivirusScan);
			}

		if (!samlUserRoleGroupAttributeName.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_saml_user_role_group_attribute_name").clear();
			locateElementBy("ca_orgnisation_properties_saml_user_role_group_attribute_name").sendKeys(samlUserRoleGroupAttributeName);
			}

		if (!entityDatetimeFormat.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_entity_date_time_format").clear();
			locateElementBy("ca_orgnisation_properties_entity_date_time_format").sendKeys(entityDatetimeFormat);
			}

		if (!bulkmaxselectcount.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_bulk_max_select_count").clear();
			locateElementBy("ca_orgnisation_properties_bulk_max_select_count").sendKeys(convertStringToInteger(bulkmaxselectcount));
			}

		if (!emailDatetimeFormat.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_email_date_time_format").clear();
			locateElementBy("ca_orgnisation_properties_email_date_time_format").sendKeys(emailDatetimeFormat);
			}

		if (!weeklyReminderEmailFileName.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_weekly_reminder_email_file_name").clear();
			locateElementBy("ca_orgnisation_properties_weekly_reminder_email_file_name").sendKeys(weeklyReminderEmailFileName);
			}

		if (!ssoNewlogin.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_sso_new_login").clear();
			locateElementBy("ca_orgnisation_properties_sso_new_login").sendKeys(ssoNewlogin);
			}

		if (!ReportGraphViewDataLimit.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_report_graph_view_data_limit").clear();
			locateElementBy("ca_orgnisation_properties_report_graph_view_data_limit").sendKeys(convertStringToInteger(ReportGraphViewDataLimit));
			}

		if (!oandaAPIKey.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_oanda_api_key").clear();
			locateElementBy("ca_orgnisation_properties_oanda_api_key").sendKeys(oandaAPIKey);
			}

		if (!oandaConversionQuoteType.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_oanda_conversion_quote_type").clear();
			locateElementBy("ca_orgnisation_properties_oanda_conversion_quote_type").sendKeys(oandaConversionQuoteType);
			}

		if (!conversionRateApiUpdateDate.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_conversion_rate_api_update_date").clear();
			locateElementBy("ca_orgnisation_properties_conversion_rate_api_update_date").sendKeys(convertStringToInteger(conversionRateApiUpdateDate));
			}

		if(!timeFormat.equalsIgnoreCase("")){
		        new Select(locateElementBy("ca_organissation_properties_time_format_drop_down")).selectByVisibleText(timeFormat);
            }

		if (!maxcharlimittextareamultiselect.equalsIgnoreCase("")) {
			locateElementBy("ca_orgnisation_properties_max_char_limit_text_area_multi_select").clear();
			locateElementBy("ca_orgnisation_properties_max_char_limit_text_area_multi_select").sendKeys(convertStringToInteger(maxcharlimittextareamultiselect));
			}

        if (!notificationEmailHour.equalsIgnoreCase("")) {
            locateElementBy("ca_orgnisation_properties_notification_email_hour").clear();
            locateElementBy("ca_orgnisation_properties_notification_email_hour").sendKeys(convertStringToInteger(notificationEmailHour));
        }

        if (!versionPrefix.equalsIgnoreCase("")) {
            locateElementBy("ca_orgnisation_properties_version_prefix").clear();
            locateElementBy("ca_orgnisation_properties_version_prefix").sendKeys(convertStringToInteger(versionPrefix));
        }

        if (!versionStartsFrom.equalsIgnoreCase("")) {
            locateElementBy("ca_orgnisation_properties_version_starts_from").clear();
            locateElementBy("ca_orgnisation_properties_version_starts_from").sendKeys(convertStringToInteger(versionStartsFrom));
        }

        if (!appendInvoiceAndPONumberBulkAttachment.equalsIgnoreCase("")) {
            locateElementBy("ca_orgnisation_properties_append_invoice_and_po_number_bulk_attachment").clear();
            locateElementBy("ca_orgnisation_properties_append_invoice_and_po_number_bulk_attachment").sendKeys(appendInvoiceAndPONumberBulkAttachment);
        }

        if (!supportContactInformationEmail.equalsIgnoreCase("")) {
            locateElementBy("ca_orgnisation_properties_support_contact_information_email").clear();
            locateElementBy("ca_orgnisation_properties_support_contact_information_email").sendKeys(supportContactInformationEmail);
        }

        if (!dateConfigurationAuditandCommunication.equalsIgnoreCase("")) {
            locateElementBy("ca_orgnisation_properties_date_configuration_audit_and_communication").clear();
            locateElementBy("ca_orgnisation_properties_date_configuration_audit_and_communication").sendKeys(dateConfigurationAuditandCommunication);
        }

		driver.findElement(By.id("proceed")).click();
		//
		fluentWaitMethod(locateElementBy("ca_orgnisation_properties_popup_ok_button"));

		
		fluentWaitMethod(driver.findElement(By.className("alertdialog-icon")));
		String entityAlert = driver.findElement(By.className("alertdialog-icon")).getText();

		Assertion(entityAlert, "Organization Properties updated successfully");

		driver.findElement(By.cssSelector("button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-only")).click();

		Logger.debug("Test Client Admin Organization Properties Test for -- " + CONFIG.getProperty("clientAdminURL") + " is PASSED");
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