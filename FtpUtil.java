package com.hanweb.complat.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;


public class FtpUtil{
  private static String encoding = System.getProperty("file.encoding");
  private String ip = "";      
  private String username = "";      
  private String password = "";      
  private int port = -1;      
  private String path = "";      
  FTPClient ftpClient = null;      
  OutputStream os = null;      
   
  public FtpUtil(String serverIP, String username, String password) {      
      this.ip = serverIP;      
      this.username = username;      
      this.password = password;
  }      
        
  public FtpUtil(String serverIP, int port, String username, String password) {      
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
      int reply;
      ftpClient = new FTPClient();      
      try {      
        ftpClient.setControlEncoding(encoding);
          if(this.port != -1){      
              ftpClient.connect(this.ip,this.port);      
          }else{      
              ftpClient.connect(this.ip);      
          } 
          ftpClient.login(this.username, this.password);
          reply = ftpClient.getReplyCode();  
          if (!FTPReply.isPositiveCompletion(reply)) {  
              System.out.println("连接失败");  
              ftpClient.disconnect();  
              return false;  
          }
          return true;      
      }catch (IOException e){      
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
   public boolean upload(String fileName, InputStream input){      
       try{
         boolean result = false;
         boolean change = ftpClient.changeWorkingDirectory("/");  
         ftpClient.setFileType(FTP.BINARY_FILE_TYPE);  
         if (change) {  
             result = ftpClient.storeFile(new String(fileName.getBytes(encoding),"UTF-8"), input);  
             if (result) {  
                 System.out.println("上传成功!");  
             }  
         }  
         input.close();  
         ftpClient.logout();  
         return true;      
       }catch(Exception e){       
              e.printStackTrace();       
              System.err.println("Exception e in Ftp upload(): " + e.toString());       
              return false;      
       }finally{      
           try{      
              ftpClient.disconnect();    
           }catch(IOException e){      
               e.printStackTrace();      
          }       
       }      
   }      
}
