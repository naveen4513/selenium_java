package office.sirion.util;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import office.sirion.base.TestBase;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

public class TestAuditLogUtil extends TestBase {
	static int flag = 0 ; 
	public static Map<String, Map<String, List<String>>> getEntityMapBeforeUpdate () {
        waitForPageSpinnerToDisappear();
		
		Map<String, Map<String, List<String>>> entityMapOnUpdatePage = new HashMap<String, Map<String, List<String>>>();

		List<WebElement> updatePageElementsList = driver.findElements(By.className("coveringParent"));

		for (WebElement coveringParentElement : updatePageElementsList) {
            String fieldSetClassValue = coveringParentElement.findElement(By.xpath("..")).getAttribute("class");
            if (fieldSetClassValue.contains("stakeholder") || fieldSetClassValue.contains("commentBg"))
                continue;

            try{
            String entityFieldName = coveringParentElement.findElement(By.xpath(".//span[1]/span")).getAttribute("title");
            Logger.debug(entityFieldName);

            Map<String, List<String>> entityMapBeforeUpdate = new HashMap<String, List<String>>();
            List<String> entityMapBeforeUpdateValuesList = new ArrayList<String>();
            List<String> entityFieldValidationsList = new ArrayList<String>();

            try {
                coveringParentElement.findElement(By.xpath(".//span[2]"));
                if (coveringParentElement.findElement(By.xpath(".//span[2]")).findElements(By.tagName("input")).size() != 0) {
                    String validationJSON = null;
                    try {
                        entityMapBeforeUpdateValuesList.add(coveringParentElement.findElement(By.xpath(".//span[2]/input")).getAttribute("value"));

                        validationJSON = coveringParentElement.findElement(By.xpath(".//span[2]/input")).getAttribute("validation");
                    } catch (NoSuchElementException e) {
                        Logger.debug("Problematic Field is: " + entityFieldName);
                    }
                    entityFieldValidationsList = getFieldValidationType(validationJSON);

                    entityMapBeforeUpdate.put("Before", entityMapBeforeUpdateValuesList);
                    entityMapBeforeUpdate.put("Type", entityFieldValidationsList);
                } else if (coveringParentElement.findElement(By.xpath(".//span[2]")).findElements(By.tagName("textarea")).size() != 0) {
                    String validationJSON = null;
                    try {
                        entityMapBeforeUpdateValuesList.add(coveringParentElement.findElement(By.xpath(".//span[2]/textarea")).getAttribute("value"));

                        validationJSON = coveringParentElement.findElement(By.xpath(".//span[2]/textarea")).getAttribute("validation");
                    } catch (NoSuchElementException e) {
                        Logger.debug("Problematic Field is: " + entityFieldName);
                    }
                    entityFieldValidationsList = getFieldValidationType(validationJSON);

                    entityMapBeforeUpdate.put("Before", entityMapBeforeUpdateValuesList);
                    entityMapBeforeUpdate.put("Type", entityFieldValidationsList);
                } else if (coveringParentElement.findElement(By.xpath(".//span[2]")).findElements(By.tagName("select")).size() != 0) {
                    try {
                        entityMapBeforeUpdateValuesList = getSelectedOptionsList(coveringParentElement);
                        String validationJSON = coveringParentElement.findElement(By.xpath(".//span[2]/select")).getAttribute("validation");
                        entityFieldValidationsList = getFieldValidationType(validationJSON);
                    } catch (NoSuchElementException e) {
                        Logger.debug("Problematic Field is: " + entityFieldName);
                    }
                    entityMapBeforeUpdate.put("Before", entityMapBeforeUpdateValuesList);
                    entityMapBeforeUpdate.put("Type", entityFieldValidationsList);
                } else {
                    Logger.debug("Problematic Field is: " + entityFieldName + "input type is not available");
                }
            } catch (NoSuchElementException re) {
                Logger.debug("NoSuchElementException: no such element: Unable to locate element");
            }
            entityMapOnUpdatePage.put(entityFieldName, entityMapBeforeUpdate);
            flag++;
        }catch (NoSuchElementException e){
                Logger.debug("No such field exist");
            }
			}

		return entityMapOnUpdatePage;
		}

	public static Map<String, Map<String, List<String>>> getEntityMapAfterUpdate (Map<String, Map<String, List<String>>> entityMapBeforeUpdatePage) {
		Map<String, Map<String, List<String>>> entityMapAfterUpdatePage = entityMapBeforeUpdatePage;
		
		List<WebElement> updatePageElementsList = driver.findElements(By.className("coveringParent"));

		for (WebElement coveringParentElement : updatePageElementsList) {
			String fieldSetClassValue = coveringParentElement.findElement(By.xpath("..")).getAttribute("class");
			if (fieldSetClassValue.contains("stakeholder") || fieldSetClassValue.contains("commentBg"))
				continue;

			String entityFieldName = coveringParentElement.findElement(By.xpath(".//span[1]/span")).getAttribute("title");
			Logger.debug(entityFieldName);

			List<String> entityMapBeforeUpdateValuesList = new ArrayList<String>();
                try {
                    if (coveringParentElement.findElement(By.xpath(".//span[2]")).findElements(By.tagName("input")).size() != 0) {
                        entityMapBeforeUpdateValuesList.add(coveringParentElement.findElement(By.xpath(".//span[2]/input")).getAttribute("value"));

                        entityMapAfterUpdatePage.get(entityFieldName).put("After", entityMapBeforeUpdateValuesList);
                    } else if (coveringParentElement.findElement(By.xpath(".//span[2]")).findElements(By.tagName("textarea")).size() != 0) {
                        entityMapBeforeUpdateValuesList.add(coveringParentElement.findElement(By.xpath(".//span[2]/textarea")).getAttribute("value"));

                        entityMapAfterUpdatePage.get(entityFieldName).put("After", entityMapBeforeUpdateValuesList);
                    } else if (coveringParentElement.findElement(By.xpath(".//span[2]")).findElements(By.tagName("select")).size() != 0) {
                        entityMapBeforeUpdateValuesList = getSelectedOptionsList(coveringParentElement);

                        entityMapAfterUpdatePage.get(entityFieldName).put("After", entityMapBeforeUpdateValuesList);
                    } else
                        continue;
                }catch (NoSuchElementException e){
                    Logger.debug("Invalid field exist");
                }

			}

		return entityMapAfterUpdatePage;
		}
	
	public static List<String> getFieldValidationType (String validationJSON) {
		List<String> entityFieldValidationsList = new ArrayList<String>();

		if (validationJSON!=null) {
			JSONArray validationJSONArray = new JSONArray(validationJSON.toString());
			for(int i=0; i<validationJSONArray.length(); i++) {
				JSONObject validationJSONObject = validationJSONArray.getJSONObject(i);
				entityFieldValidationsList.add(validationJSONObject.get("type").toString());
				}
			}

		return entityFieldValidationsList;
		}

	public static List<String> getSelectedOptionsList(WebElement ParentElement) {
		List<String> entityMapBeforeUpdateValuesList = new ArrayList<String>();

		List<WebElement> entityFieldSelectedList = new Select(ParentElement.findElement(By.xpath(".//span[2]/select"))).getAllSelectedOptions();
		Iterator<WebElement> entityFieldSelectedListIterator = entityFieldSelectedList.iterator();

		while(entityFieldSelectedListIterator.hasNext())
			entityMapBeforeUpdateValuesList.add(entityFieldSelectedListIterator.next().getText());

		return entityMapBeforeUpdateValuesList;
		}

	public static Map<String, Map<String, String>> getAuditLogViewHistoryData() {
		Map<String, Map<String, String>> auditLogDataMap = new HashMap<String, Map<String,String>>();

		List<WebElement> auditLogDataRowsList = driver.findElement(By.id("historyDataTable")).findElement(By.id("data")).findElements(By.tagName("tr"));
		
		for (WebElement auditLogDataRow : auditLogDataRowsList) {
			Map<String, String> auditLogDataRowMap = new HashMap<String, String>();

			auditLogDataRowMap.put("Action:", auditLogDataRow.findElements(By.tagName("td")).get(1).getText());
			auditLogDataRowMap.put("OLD Value:", auditLogDataRow.findElements(By.tagName("td")).get(2).getText());
			auditLogDataRowMap.put("New Value:", auditLogDataRow.findElements(By.tagName("td")).get(3).getText());
			
			auditLogDataMap.put(auditLogDataRow.findElements(By.tagName("td")).get(0).getText(), auditLogDataRowMap);
			}

		return auditLogDataMap;
		}
	
	public static void isAuditLogWorking(Map<String, Map<String, List<String>>> entityMapAfterUpdatePage, Map<String, Map<String, String>> auditLogDataMap) {
		DecimalFormat dFormat = new DecimalFormat("#.00");

		Iterator<String> itrAuditLog = auditLogDataMap.keySet().iterator();

	    while (itrAuditLog.hasNext()) {
	    	String fieldNameOnAuditLog = itrAuditLog.next();
	    	String entityNewValueOnAuditLog = auditLogDataMap.get(fieldNameOnAuditLog).get("New Value:");
	    	String entityOLDValueOnAuditLog = auditLogDataMap.get(fieldNameOnAuditLog).get("OLD Value:");
	    	String entityActionOnAuditLog = auditLogDataMap.get(fieldNameOnAuditLog).get("Action:");

	    	String entityOLDValueOnUpdatePage = String.join(",", entityMapAfterUpdatePage.get(fieldNameOnAuditLog).get("Before"));
	    	String entityValidationTypeUpdatePage = String.join(",", entityMapAfterUpdatePage.get(fieldNameOnAuditLog).get("Type"));
	    	String entityNewValueUpdatePage = String.join(",", entityMapAfterUpdatePage.get(fieldNameOnAuditLog).get("After"));
	    	
	    	if (entityActionOnAuditLog.equalsIgnoreCase("MODIFIED")) {
		    	if (!entityValidationTypeUpdatePage.contains("decimalLength")) {
		    		Assertion(entityNewValueUpdatePage, entityNewValueOnAuditLog);
		    		Assertion(entityOLDValueOnUpdatePage, entityOLDValueOnAuditLog);
		    		}
		    	else {
		    		Assert.assertEquals(dFormat.format(Double.parseDouble(entityNewValueUpdatePage)), dFormat.format(Double.parseDouble(entityNewValueOnAuditLog)));
		    		Assert.assertEquals(dFormat.format(Double.parseDouble(entityOLDValueOnAuditLog)), dFormat.format(Double.parseDouble(entityOLDValueOnUpdatePage)));
		    		}
		    	}
	    	}
	    }
	}
