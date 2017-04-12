package com.zhuxj.maven_1.controller;

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

import net.sf.json.JSONObject;

@Controller
@RequestMapping("ftp")
public class TestController {
  @RequestMapping(value="httpClient",method=RequestMethod.POST)
  @ResponseBody
  //,produces="text/html;charset=UTF-8"
  public void returnJson(@RequestBody String jsonStr, HttpServletResponse response) throws IOException{
	JSONObject obj = JSONObject.fromObject(jsonStr);
    JSONObject obj2 = new JSONObject();
    JsonObject jsonObj = new JsonObject();
    if("JerryC".equals(obj.getString("UserName"))){
    	jsonObj.addProperty("result", "success");
    	jsonObj.addProperty("msg", "认证成功");
    	obj2.put("result", "success");
    	obj2.put("msg", "成功");
    }else{
    	jsonObj.addProperty("result", "failed");
    	jsonObj.addProperty("msg", "认证失败");
    	obj2.put("result", "failed");
    	obj2.put("msg", "失败");
    }
    response.setContentType("text/html;charset=utf-8");
    PrintWriter out= null;
    out = response.getWriter();
    out.print(jsonObj.toString());
    out.flush();
    out.close();  
   /* String json= jsonObj.toString();
    String str = new String(json.getBytes("utf-8"),"utf-8");
    return obj2.toString();*/
  }
}
