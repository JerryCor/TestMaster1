package com.hanweb.complat.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import sun.net.TelnetOutputStream;
import sun.net.ftp.FtpClient;

public class SunFtpUtil {
  private String ip = "";  
  
  private String username = "";  

  private String password = "";  

  private int port = -1;  

  private String path = "/";  

  FtpClient ftpClient = null;  

  OutputStream os = null;  

  FileInputStream is = null;  

  public SunFtpUtil(String serverIP, String username, String password) {  
      this.ip = serverIP;  
      this.username = username;  
      this.password = password;  
  }  
    
  public SunFtpUtil(String serverIP, int port, String username, String password) {  
      this.ip = serverIP;  
      this.username = username;  
      this.password = password;  
      this.port = port;  
  }
  /** 
   * 连接ftp服务器 
   *  
   * @throws IOException 
   */  
  public boolean connectServer(){  
      ftpClient = new FtpClient();  
      try {  
          if(this.port != -1){  
                  ftpClient.openServer(this.ip,this.port);  
          }else{  
              ftpClient.openServer(this.ip);  
          }  
          ftpClient.login(this.username, this.password);  
          if (this.path.length() != 0){  
              ftpClient.cd(this.path);// path是ftp服务下主目录的子目录             
          }  
          ftpClient.binary();// 用2进制上传、下载  
          System.out.println("已登录到\"" + ftpClient.pwd() + "\"目录");  
          return true;  
      }catch (IOException e){  
          e.printStackTrace();  
          return false;  
      }  
  }  
  /** 
   * 断开与ftp服务器连接 
   *  
   * @throws IOException 
   */  
  public boolean closeServer(){  
      try{  
          if (is != null) {  
              is.close();  
          }  
          if (os != null) {  
              os.close();  
          }  
          if (ftpClient != null) {  
              ftpClient.closeServer();  
          }  
          System.out.println("已从服务器断开");  
          return true;  
      }catch(IOException e){  
          e.printStackTrace();  
          return false;  
      }  
  }  
  /** 
   * ftp上传 
   * 如果服务器段已存在名为newName的文件夹，该文件夹中与要上传的文件夹中同名的文件将被替换 
   *  
   * @param fileName 要上传的文件（或文件夹）名 
   * @param newName 服务器段要生成的文件（或文件夹）名 
   * @return 
   */  
  public boolean upload(String fileName, String newName){  
      try{  
          String savefilename = new String(fileName.getBytes("GBK"), "GBK");   
          File file_in = new File(savefilename);//打开本地待长传的文件  
          if(!file_in.exists()){  
              throw new Exception("此文件或文件夹[" + file_in.getName() + "]有误或不存在!");  
          }  
          uploadFile(file_in.getPath(),newName);  
            
          if(is != null){  
              is.close();  
          }  
          if(os != null){  
              os.close();  
          }  
          return true;  
      }catch(Exception e){   
             e.printStackTrace();   
             System.err.println("Exception e in Ftp upload(): " + e.toString());   
             return false;  
      }finally{  
          try{  
              if(is != null){  
                  is.close();  
              }  
              if(os != null){   
                  os.close();   
              }  
          }catch(IOException e){  
              e.printStackTrace();  
         }   
      }  
  }  
  
  /** 
   *  upload 上传文件 
   *  
   * @param filename 要上传的文件名 
   * @param newname 上传后的新文件名 
   * @return -1 文件不存在 >=0 成功上传，返回文件的大小 
   * @throws Exception 
   */  
  public long uploadFile(String filename, String newname) throws Exception{  
      long result = 0;  
      TelnetOutputStream os = null;  
      FileInputStream is = null;  
      try {  
         File file_in = new File(filename);  
          if(!file_in.exists()){
            return -1;  
          }
          os = ftpClient.put(newname);  
          result = file_in.length();  
          is = new FileInputStream(file_in);  
          byte[] bytes = new byte[1024];  
          int c;  
          while((c = is.read(bytes)) != -1){  
              os.write(bytes, 0, c);  
          }  
      }catch(Exception e){
        System.out.println(e.toString());
      }finally{  
          if(is != null){  
              is.close();  
          }  
          if(os != null){  
              os.close();  
          }  
      }  
      return result;  
  }  
}
