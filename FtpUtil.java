package com.hanweb.complat.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FtpUtil {
  private final Log logger = LogFactory.getLog(getClass());
	private String encode = new String("GBK");
	private String ftpPath = new String("/home/test/dahan");
	private String ip;
	private String username;
	private String password;
	private int port = -1;
	FTPClient ftpClient;

	public FtpUtil() {
	}

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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

	public String getFtpPath() {
		return ftpPath;
	}

	public void setFtpPath(String ftpPath) {
		this.ftpPath = ftpPath;
	}

	/**
	 * 连接ftp服务器
	 * 
	 * @return 连接结果
	 */
	public boolean connectServer() {
		int reply;
		ftpClient = new FTPClient();
		try {
			ftpClient.setControlEncoding(encode);
			if (this.port != -1) {
				ftpClient.connect(this.ip, this.port);
			} else {
				ftpClient.connect(this.ip);
			}
			ftpClient.login(this.username, this.password);
			reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				logger.debug("连接失败");
				ftpClient.disconnect();
				return false;
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * ftp上传 如果服务器段已存在名为newName的文件夹，该文件夹中与要上传的文件夹中同名的文件将被替换
	 * 
	 * @param fileName
	 *            服务器段要生成的文件（或文件夹）名
	 * @param input
	 *            要上传的文件（或文件夹）路径（含路径）
	 * @return result 上传结果
	 */
	public boolean upload(String fileName, InputStream input) {
		boolean result = false;
		try {
			boolean change = ftpClient.changeWorkingDirectory(ftpPath);
			if(!change){
				ftpClient.makeDirectory(ftpPath);
				change = ftpClient.changeWorkingDirectory(ftpPath);
			}
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			if (change) {
				result = ftpClient.storeFile(fileName, input);
				if (result) {
					logger.debug("上传成功!");
					return result;
				}
			}
			input.close();
			ftpClient.logout();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Ftp出现异常:" +e.toString());
			return result;
		} finally {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
   * ftp下载 
   * 
   * @param fileName
   *            本地要生成的文件（或文件夹）名
   * @return result 上传结果
   */
  public boolean download(String fileName) {
    boolean result = false;
    try {
      boolean change = ftpClient.changeWorkingDirectory(ftpPath);
      if(!change){
        FTPFile[] fs = ftpClient.listFiles();
        for(FTPFile ff:fs){
          String name = ff.getName();
          System.out.println(name);
        }
        ftpClient.makeDirectory(ftpPath);
        change = ftpClient.changeWorkingDirectory(ftpPath);
      }
      ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
      if (change) {
        FTPFile[] fs = ftpClient.listFiles(); 
        for(FTPFile ff:fs){ 
        if(ff.getName().equals(fileName)){ 
        File localFile = new File("d:"+"/temp"+ff.getName()); 
        OutputStream is = new FileOutputStream(localFile); 
        ftpClient.retrieveFile(ff.getName(), is); 
        is.close(); 
        } 
        } 
      }
      ftpClient.logout();
      return result;
    } catch (Exception e) {
      e.printStackTrace();
      logger.debug("Ftp出现异常:" +e.toString());
      return result;
    } finally {
      try {
        ftpClient.disconnect();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
	
	/**
	 * 删除上传文件
	 * 
	 * @param pathName 文件路径含文件名
	 * @return 删除结果
	 */
	public boolean deleteFile(String pathName){
	  try {
      if(ftpClient.deleteFile(pathName)){
        logger.debug("删除成功。");
      }
    } catch (IOException e) {
      logger.debug("删除失败。" +e.getMessage());
      return false;
    }finally{
      try {
        ftpClient.disconnect();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
	  return true;
	}
}
