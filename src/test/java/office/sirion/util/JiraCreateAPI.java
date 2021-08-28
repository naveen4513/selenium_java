package office.sirion.util;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class JiraCreateAPI {
	
	String jiraCreateApiJsonStr = null;
	String statusCode;

	public HttpResponse hitCreateApi(Map<String, String> payload) {
		HttpResponse response = null;
		String hostName = "jira.sirionlabs.office";
		Integer portNumber = 80;
		String protocolScheme = "http";

		try {
			HttpPost postRequest;
			String queryString = "/secure/QuickCreateIssue.jspa?decorator=none";

			System.out.printf("Query string url formed is" + queryString);
			postRequest = new HttpPost(queryString);

			postRequest.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			postRequest.addHeader("Accept", "*/*");
			postRequest.addHeader("Accept-Encoding", "gzip, deflate");
			postRequest.addHeader("Cookie", "JSESSIONID=EE08402A522243C1D1805485682D772D; atlassian.xsrf.token=B5CG-UGD8-T8TP-7CP8|d70d0f201e425c1cfc3d237cb6cd0aff8d38f4e2|lin");
			postRequest.addHeader("X-Requested-With", "XMLHttpRequest");


			String params = getUrlEncodedString(payload);
			postRequest.setEntity(new StringEntity(params, "UTF-8"));

			HttpClient httpClient;
			httpClient = HttpClientBuilder.create().build();
			HttpHost target = new HttpHost(hostName, portNumber, protocolScheme);

			response = httpClient.execute(target, postRequest);

			System.out.printf("Response status is {}"+ response.getStatusLine().toString());
			this.statusCode = String.valueOf(response.getStatusLine().getStatusCode());
			this.jiraCreateApiJsonStr = EntityUtils.toString(response.getEntity());

			System.out.printf("Response status code is {}"+ this.statusCode);
			System.out.printf("Response is {}"+ this.jiraCreateApiJsonStr);

			Header[] headers = response.getAllHeaders();
			for (int i = 0; i < headers.length; i++) {
				System.out.printf("response header {}"+ headers[i].toString());
			}
		} catch (Exception e) {
			System.out.printf("Exception while hitting jiraCreate Api. {}"+ e.getMessage());
		}
		return response;
	}

	public static String getUrlEncodedString(Map<String, String> params) throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (Map.Entry<String, String> entry : params.entrySet()) {

			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
		}

		return result.toString();
	}

	@Test
	public void CreateIssue(String Summary, String Synopsis){
		try{
			Map<String, String> formParam = new HashMap<String, String>();
			formParam.put("issuetype", "1");//Development Bug
			formParam.put("pid", "10300"); //Sirion (SIR)
			formParam.put("summary",Summary);
			formParam.put("components","12209"); //Exceptions
			formParam.put("priority","3"); //Major
			formParam.put("assignee","naveen.gupta");
			formParam.put("description",Synopsis);//Bug Description
			formParam.put("customfield_11715","11669");
			formParam.put("labels","1.25_QA_Rev");
			formParam.put("customfield_11129","anay.jyoti");
			formParam.put("customfield_10113", "naveen.gupta");
			formParam.put("atl_token","B5CG-UGD8-T8TP-7CP8|d70d0f201e425c1cfc3d237cb6cd0aff8d38f4e2|lin");
			formParam.put("formToken", "ce0ac19bef609d6cab6988230ec12b1a7d566a4c");
			hitCreateApi(formParam);
		}catch(Exception e){
			System.out.printf("Exception in test method {}"+ e.getMessage());
		}
	}
}

