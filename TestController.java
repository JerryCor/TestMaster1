package com.hanweb.complat.controller.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonObject;
import com.hanweb.complat.test.FtpUtil;
import com.hanweb.complat.test.SunFtpUtil;
import com.hanweb.jis.expansion.util.fastjson.JSON;
import com.hanweb.jis.expansion.util.fastjson.JSONObject;

@Controller
@RequestMapping("ftp")
public class TestController {
  @RequestMapping("ftpload")
  @ResponseBody
  public String getFtp()
  {
  /*  FtpUtil ftp = new FtpUtil();
    ftp.setEncode("GBK");
    ftp.setFtpPath("/home/test/dahan");
    ftp.setIp("123.56.84.192");
    ftp.setPort(21);
    ftp.setUsername("test");
    ftp.setPassword("123456");
    try {
      ftp.connectServer();
      FileInputStream input = new FileInputStream(new File("D:/6dce57c15cd849d9b712ebf6ea035e35.zip"));
      if(ftp.upload("上传.zip", input)){
        System.out.println("success");
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }*/
    SunFtpUtil spt = new SunFtpUtil("192.168.89.176", 111, "JerryC", "hanweb");
    spt.connectServer();
    spt.upload("D:/6dce57c15cd849d9b712ebf6ea035e35.zip", "d.zip");
    return "Success";
  }
  @RequestMapping(value="httpClient",method=RequestMethod.POST)
  @ResponseBody
  public String returnJson(@RequestBody String jsonStr, HttpServletResponse response) throws IOException{
    JSONObject obj = JSONObject.parseObject(jsonStr);
    JSONObject obj2 = new JSONObject();
    JsonObject jsonObj = new JsonObject();
    if("JerryC".equals(obj.getString("UserName"))){
      obj2.put("result", "success");
      obj2.put("mgs", "认证成功");
    }else{
      obj2.put("result", "failed");
      obj2.put("mgs", "认证失败");
    }
   /* response.setCharacterEncoding("utf-8");
    PrintWriter out= null;
    out = response.getWriter();
    out.print(obj2.toString());
    out.flush();
    out.close();  存在乱码问题*/
    return jsonObj.toString(); //直接返回无乱码问题
  }
}
