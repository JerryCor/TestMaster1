

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.google.gson.JsonObject;

import net.sf.json.JSONObject;

public class TestHttpClient {
  
  private Logger logger = Logger.getLogger(getClass());
  
  private static final String MESSAGEURL = "http://localhost:8080/Maven_1.0/ftp/httpClient.do";
  
  private static final String EXCEPTION = "exception";
  /**
   * 这是一个传送json的测试.
   * 
   * @param jsonStr 传输消息
   * @return resData 接口传输结果
   */
  public String responseInfo(String jsonStr) {
    String resData= null;
    try {
      //DefaultHttpClient httpClient = new DefaultHttpClient();
      //HttpPost method = new HttpPost(MESSAGEURL);
      URL url=new URL(MESSAGEURL);
      HttpURLConnection httpConn=(HttpURLConnection)url.openConnection();
      httpConn.setDoOutput(true);   //需要输出
      httpConn.setDoInput(true);   //需要输入
      httpConn.setUseCaches(false);  //不允许缓存
      httpConn.setRequestMethod("POST");   //设置POST方式连接
      httpConn.setRequestProperty("Content-Type", "application/json");
      httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
      httpConn.setRequestProperty("Charset", "UTF-8");
//      StringEntity entity = new StringEntity(jsonStr, "utf-8");// 解决中文乱码问题
//      entity.setContentEncoding("UTF-8");
//      entity.setContentType("application/json");
//      method.setEntity(entity);
     /* HttpResponse result = httpClient.execute(method);

      // 请求结束，返回结果
      resData = EntityUtils.toString(result.getEntity());
      logger.debug(resData + "--------------resData");
      return resData;
      // JSONObject resJson = (JSONObject)JSONObject.parse(resData);
      // code = resJson.get("result").toString(); // 对方接口请求返回结果
    } catch (Exception e) {
      resData = EXCEPTION;
      logger.debug("发送 POST 请求出现异常！" + e);
      e.printStackTrace();*/
      httpConn.connect();
      //建立输入流，向指向的URL传入参数
      DataOutputStream dos=new DataOutputStream(httpConn.getOutputStream());
      dos.writeBytes(jsonStr);
      dos.flush();
      dos.close();
      System.out.println(httpConn.getContentType());
      //获得响应状态
      int resultCode=httpConn.getResponseCode();
      StringBuffer sb=new StringBuffer();
      if(HttpURLConnection.HTTP_OK==resultCode){
        String readLine=new String();
        BufferedReader responseReader=new BufferedReader(new InputStreamReader(httpConn.getInputStream(),"utf8"));
        while((readLine=responseReader.readLine())!=null){
          sb.append(readLine).append("\n");
        }
        responseReader.close();
        System.out.println(sb.toString());
      } 
      resData = sb.toString();
      return sb.toString();
    }catch(Exception e){
    	System.out.println(e.getMessage());
    }finally{
    	
    }
	return resData;
  }
  
  public static void main(String[] args){
    TestHttpClient httpClient = new TestHttpClient();
    JsonObject json = new JsonObject();
    json.addProperty("UserName", "JerryC");
    json.addProperty("PassWord", "哈哈");
    json.addProperty("Gender", "female");
    String dd = httpClient.responseInfo(json.toString());
    JSONObject DD = JSONObject.fromObject(dd);
    String result = DD.getString("result");
    System.out.println(DD.getString("msg"));
  }
}
