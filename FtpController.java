package com.zhuxj.maven_1.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhuxj.maven_1.ftp.FtpUtil;

@Controller
@RequestMapping("ftp")
public class FtpController {
	@RequestMapping("ftpupload")
	@ResponseBody
	public String getFtp(){
		FtpUtil ftp = new FtpUtil();
		ftp.setEncode("GBK");
		ftp.setFtpPath("/temp1");
		ftp.setIp("192.168.10.183");
		ftp.setPort(21);
		ftp.setUsername("test");
		ftp.setPassword("123456");
		try {
			ftp.connectServer();
			FileInputStream input = new FileInputStream(new File("D:/正确使用maven构建web工程.doc"));
			if(ftp.upload("上传.doc", input)){
				System.out.println("success");
			}
			System.getProperties().list(System.out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return "Success";
	}
}
