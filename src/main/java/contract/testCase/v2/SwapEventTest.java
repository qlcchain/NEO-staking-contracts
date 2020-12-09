/** 
 * @Package contract 
 * @Description 
 * @author yfhuang521@gmail.com
 * @date 2020年8月26日 下午3:12:08 
 * @version V1.0 
 */ 
package contract.testCase.v2;

import java.io.IOException;
import java.util.UUID;

import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import io.neow3j.contract.ScriptHash;
import io.neow3j.utils.Numeric;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import qlc.network.QlcException;

/** 
 * @Description SWAP 测试
 * @author yfhuang521@gmail.com
 * @date 2020年8月26日 下午3:12:08 
 */
public class SwapEventTest {

	@Test
	public void swapEvent() {
		
		JSONArray params = new JSONArray();
		// 交易txid
		params.add("001056cb854347fd719685125b8785b503d7d7b6e96b3e0c63395942ae347a76");
		
		JSONObject reqParams = makeRequest("getapplicationlog", params);
		JSONObject response = send(reqParams);
		System.out.println(response.toJSONString());
		JSONObject json = JSONObject.parseObject(response.toJSONString());
		if (json.containsKey("result")) {
			json = json.getJSONObject("result");
			JSONArray executions = json.getJSONArray("executions");
			json = executions.getJSONObject(0);
			JSONArray notifications = json.getJSONArray("notifications");
			if (notifications.size() > 0) {
				json = notifications.getJSONObject(0);
				System.out.println("contactHash: " + json.getString("contract"));
				json = json.getJSONObject("state");
				JSONArray values = json.getJSONArray("value");
				json = values.getJSONObject(0);
				System.out.println("value1：" + json.getString("value"));
				System.out.println("method: " + Numeric.hexToString(json.getString("value")));
				json = values.getJSONObject(1);
				System.out.println("value2：" + Numeric.reverseHexString(json.getString("value")));
				System.out.println("from: " + new ScriptHash(Numeric.hexStringToByteArray(json.getString("value"))).toAddress());
				json = values.getJSONObject(2);
				System.out.println("value3：" + Numeric.reverseHexString(json.getString("value")));
				System.out.println("to: " + new ScriptHash(Numeric.hexStringToByteArray(json.getString("value"))).toAddress());
				json = values.getJSONObject(3);
				System.out.println("value4：" + json.getString("value"));
				System.out.println("amount: " + Numeric.fromFixed8ToDecimal(Numeric.reverseHexString(json.getString("value"))));			
				
			}
		}
	}
	

	public JSONObject send(JSONObject params) {
		
		OkHttpClient client = new OkHttpClient();
		
		try {
			
			RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), params.toJSONString());
			
			Request request = new Request.Builder().url("http://seed2.ngd.network:20332").post(body).build();
			
			Response response = client.newCall(request).execute();
			if (response == null) 
				throw new QlcException(900, "Invalid response type");
			
			int statusCode = response.code();
			if (statusCode != 200) {
				throw new QlcException(statusCode, response.code() + "");
			}

			JSONObject result = JSONObject.parseObject(response.body().string());
			if (result.containsKey("result") || result.containsKey("error"))
				return result;
			else
				throw new IOException();
		} catch (IOException e) {
			throw new QlcException(901, e.getMessage());
		}
	}
	
	private JSONObject makeRequest(String method, JSONArray params) {
		
		JSONObject request = new JSONObject();
		request.put("jsonrpc", "2.0");
		request.put("id", UUID.randomUUID().toString().replace("-", ""));
		request.put("method", method);
		if (params!=null && !params.isEmpty())
			request.put("params", params);
		return request;
		
	}
	
}
