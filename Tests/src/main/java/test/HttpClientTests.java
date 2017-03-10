package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class HttpClientTests {

//	HttpClient client = new HttpClient();
	CloseableHttpClient httpClient = getHttpClient();

	@Test
	public void testQiXinBaoRequest() {

		// ����Ĭ�ϵ�httpClientʵ��
		try {
			/*// ��get��������http����
			HttpGet get = new HttpGet("http://114.55.50.145/APIService/lfenterprise/getDetailByKeywords?appkey=cd51cb57272f479da475a1395d66eda8&operName=�׾�&cityCode=1101&domain=������");
			
			System.out.println("ִ��get����:...." + get.getURI());
			CloseableHttpResponse httpResponse = null;
			// ����get����
			httpResponse = httpClient.execute(get);
			try {
				// responseʵ��
				HttpEntity entity = httpResponse.getEntity();
				if (null != entity) {
					System.out.println("��Ӧ״̬��:" + httpResponse.getStatusLine());
					System.out.println("-------------------------------------------------");
					System.out.println("��Ӧ����:" + EntityUtils.toString(entity));
					System.out.println("-------------------------------------------------");
				}
			} finally {
				httpResponse.close();
			}*/
			Map<String, String> params = new HashMap<String, String>();
			CloseableHttpClient httpclient = HttpClients.custom().build();
			params.put("appkey", "cd51cb57272f479da475a1395d66eda8");
			params.put("operName", "�׾�");
			params.put("cityCode", "1101");
			params.put("domain", "������");
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(params.keySet().size());
			for (String key : params.keySet()) {
				nameValuePairs.add(new BasicNameValuePair(key, params.get(key) == null ? "" : params.get(key)));
			}
			RequestBuilder builder = null;
			builder = RequestBuilder.post();
			builder.setUri("http://http://api.qixin007.com/APIService/lfenterprise/getDetailByKeywords");
			builder.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			builder.setConfig(RequestConfig.custom().setConnectionRequestTimeout(60000).setConnectTimeout(60000).setSocketTimeout(60000).build());
			HttpResponse response = httpclient.execute(builder.build());
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() >= 300) {
				throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
			}
			HttpEntity entity = response.getEntity();
			String reutrnResult = entity == null ? null : EntityUtils.toString(entity, "UTF-8");
			System.out.println(reutrnResult);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				closeHttpClient(httpClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private CloseableHttpClient getHttpClient() {
		return HttpClients.createDefault();
	}

	private void closeHttpClient(CloseableHttpClient client) throws IOException {
		if (client != null) {
			client.close();
		}
	}

}
