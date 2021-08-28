package office.sirion.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import office.sirion.base.TestBase;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class AuditLogTestUtil extends TestBase {
	static Map<String, Map<String, List<String>>> entityMapOnUpdatePage;
	static Map<String, List<String>> entityMapBeforeUpdate;

	
	public static String getFieldNameOnUpdatePage (String locatorKey) {
		WebElement parentElement = getElementByID(locatorKey);

		String entityFieldNameOnUpdatePage = parentElement.findElement(By.xpath("..")).findElement(By.tagName("span")).findElement(By.tagName("span")).getAttribute("title");

		return entityFieldNameOnUpdatePage;
		}
	
	public static WebElement getElementByID(String locatorKey) {
		String str = OR.getProperty(locatorKey).split("-->")[1];

		WebElement element = driver.findElement(By.id(str));
		
		return element;
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

	public static Map<String, Map<String, List<String>>> getEntityMapBeforeUpdate () {
		entityMapOnUpdatePage = new HashMap<String, Map<String, List<String>>>();
		entityMapBeforeUpdate = new HashMap<String, List<String>>();

		List<WebElement> updatePageElementsList = driver.findElements(By.className("coveringParent"));

		for (WebElement coveringParentElement : updatePageElementsList) {
			String entityFieldName = coveringParentElement.findElement(By.xpath(".//span[1]/span")).getAttribute("title");

			List<String> entityMapBeforeUpdateValuesList = new ArrayList<String>();
			List<String> entityFieldValidationsList = new ArrayList<String>();

			if (coveringParentElement.findElement(By.xpath(".//span[2]")).findElements(By.tagName("input")).size()!=0) {
				entityMapBeforeUpdateValuesList.add(coveringParentElement.findElement(By.xpath(".//span[2]/input")).getAttribute("value"));
				String validationJSON = coveringParentElement.findElement(By.xpath(".//span[2]/input")).getAttribute("validation");

				if (validationJSON!=null) {
					JSONArray validationJSONArray = new JSONArray(validationJSON.toString());
					for(int i=0; i<validationJSONArray.length(); i++) {
						JSONObject validationJSONObject = validationJSONArray.getJSONObject(i);
						entityFieldValidationsList.add(validationJSONObject.get("type").toString());
						}
					}

				entityMapBeforeUpdate.put("Before", entityMapBeforeUpdateValuesList);
				entityMapBeforeUpdate.put("Type", entityFieldValidationsList);
				}

			else if (coveringParentElement.findElement(By.xpath(".//span[2]")).findElements(By.tagName("textarea")).size()!=0) {
				entityMapBeforeUpdateValuesList.add(coveringParentElement.findElement(By.xpath(".//span[2]/textarea")).getAttribute("value"));
				String validationJSON = coveringParentElement.findElement(By.xpath(".//span[2]/textarea")).getAttribute("validation");

				if (validationJSON!=null) {
					JSONArray validationJSONArray = new JSONArray(validationJSON.toString());
					for(int i=0; i<validationJSONArray.length(); i++) {
						JSONObject validationJSONObject = validationJSONArray.getJSONObject(i);
						entityFieldValidationsList.add(validationJSONObject.get("type").toString());
						}
					}
				entityMapBeforeUpdate.put("Before", entityMapBeforeUpdateValuesList);
				entityMapBeforeUpdate.put("Type", entityFieldValidationsList);
				}

			else if (coveringParentElement.findElement(By.xpath(".//span[2]")).findElements(By.tagName("select")).size()!=0) {
				List<WebElement> entityFieldSelectedList = new Select(coveringParentElement.findElement(By.xpath(".//span[2]/select"))).getAllSelectedOptions();
				Iterator<WebElement> entityFieldSelectedListIterator = entityFieldSelectedList.iterator();

				while(entityFieldSelectedListIterator.hasNext())
					entityMapBeforeUpdateValuesList.add(entityFieldSelectedListIterator.next().getText());

				String validationJSON = coveringParentElement.findElement(By.xpath(".//span[2]/select")).getAttribute("validation");

				if (validationJSON!=null) {
					JSONArray validationJSONArray = new JSONArray(validationJSON.toString());
					for(int i=0; i<validationJSONArray.length(); i++) {
						JSONObject validationJSONObject = validationJSONArray.getJSONObject(i);
						entityFieldValidationsList.add(validationJSONObject.get("type").toString());
						}
					}
				entityMapBeforeUpdate.put("Before", entityMapBeforeUpdateValuesList);
				entityMapBeforeUpdate.put("Type", entityFieldValidationsList);
				}

			else
				continue;

			entityMapOnUpdatePage.put(entityFieldName, entityMapBeforeUpdate);
			}

		return entityMapOnUpdatePage;
		}

	public static Map<String, Map<String, List<String>>> getEntityMapAfterUpdate (Map<String, Map<String, List<String>>> entityMapBeforeUpdatePage) {
		Map<String, Map<String, List<String>>> entityMapAfterUpdatePage = entityMapBeforeUpdatePage;
		
		List<WebElement> updatePageElementsList = driver.findElements(By.className("coveringParent"));

		for (WebElement coveringParentElement : updatePageElementsList) {
			String entityFieldName = coveringParentElement.findElement(By.xpath(".//span[1]/span")).getAttribute("title");

			List<String> entityMapBeforeUpdateValuesList = new ArrayList<String>();

			if (coveringParentElement.findElement(By.xpath(".//span[2]")).findElements(By.tagName("input")).size()!=0)
				entityMapBeforeUpdateValuesList.add(coveringParentElement.findElement(By.xpath(".//span[2]/input")).getAttribute("value"));

			else if (coveringParentElement.findElement(By.xpath(".//span[2]")).findElements(By.tagName("textarea")).size()!=0)
				entityMapBeforeUpdateValuesList.add(coveringParentElement.findElement(By.xpath(".//span[2]/textarea")).getAttribute("value"));

			else if (coveringParentElement.findElement(By.xpath(".//span[2]")).findElements(By.tagName("select")).size()!=0) {
				List<WebElement> entityFieldSelectedList = new Select(coveringParentElement.findElement(By.xpath(".//span[2]/select"))).getAllSelectedOptions();
				Iterator<WebElement> entityFieldSelectedListIterator = entityFieldSelectedList.iterator();

				while(entityFieldSelectedListIterator.hasNext())
					entityMapBeforeUpdateValuesList.add(entityFieldSelectedListIterator.next().getText());
				}

			else
				continue;

			entityMapAfterUpdatePage.get(entityFieldName).put("After", entityMapBeforeUpdateValuesList);
			}

		return entityMapAfterUpdatePage;
		}
	}
