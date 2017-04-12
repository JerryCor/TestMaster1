package com.hanweb.complat.test;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.google.gson.JsonObject;
import com.hanweb.jis.expansion.util.fastjson.JSONObject;

public class TestHttpClient {
  
  private Logger logger = Logger.getLogger(getClass());
  
  private static final String MESSAGEURL = "http://jerryc.iego.net/JMOPEN/ftp/httpClient.do";
  
  private static final String EXCEPTION = "exception";
  /**
   * 这是一个传送json的测试.
   * 
   * @param jsonStr 传输消息
   * @return resData 接口传输结果
   */
  public String responseInfo(String jsonStr) {
    String resData;
    try {
      DefaultHttpClient httpClient = new DefaultHttpClient();
      HttpPost method = new HttpPost(MESSAGEURL);
      StringEntity entity = new StringEntity(jsonStr, "utf-8");// 解决中文乱码问题
      entity.setContentEncoding("UTF-8");
      entity.setContentType("application/json");
      method.setEntity(entity);

      HttpResponse result = httpClient.execute(method);

      // 请求结束，返回结果
      resData = EntityUtils.toString(result.getEntity());
      logger.debug(resData + "--------------resData");
      return resData;
      // JSONObject resJson = (JSONObject)JSONObject.parse(resData);
      // code = resJson.get("result").toString(); // 对方接口请求返回结果
    } catch (Exception e) {
      resData = EXCEPTION;
      logger.debug("发送 POST 请求出现异常！" + e);
      e.printStackTrace();
      return resData;
    }
  }
  
  public static void main(String[] args){
    TestHttpClient httpClient = new TestHttpClient();
    JsonObject json = new JsonObject();
    json.addProperty("UserName", "JerryC");
    json.addProperty("PassWord", "哈哈");
    json.addProperty("Gender", "female");
    String dd = httpClient.responseInfo(json.toString());
    JSONObject DD = JSONObject.parseObject(dd);
    String result = DD.getString("result");
  }
}
