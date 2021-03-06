package com.hanweb.jmopen.publish.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipFile;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

public class ZipTool {
  
  private final Log logger = LogFactory.getLog(getClass());
  
  /**使用GBK编码可以避免压缩中文文件名乱码*/
  private static final String CHINESE_CHARSET = "GBK";
  /**文件读取缓冲区大小*/
  private static final int CACHE_SIZE = 1024;
  
  /** 
   * 递归压缩文件夹 
   * @param srcRootDir 压缩文件夹根目录的子路径 
   * @param file 当前递归压缩的文件或目录对象 
   * @param zos 压缩文件存储对象 
   * @throws Exception 
   */  
  private void zipFile(File parentFile, String basePath, ZipOutputStream zos) throws Exception  
  {  
    File[] files = new File[0];
    if (parentFile.isDirectory()) {
        files = parentFile.listFiles();
    } else {
        files = new File[1];
        files[0] = parentFile;
    }
    String pathName;
    InputStream is;
    BufferedInputStream bis;
    byte[] cache = new byte[CACHE_SIZE];
    for (File file : files) {
        if (file.isDirectory()) {
            pathName = file.getPath().substring(basePath.length() + 1) + File.separator;
            zos.putNextEntry(new ZipEntry(new String(pathName.getBytes(),"utf-8")));
            zipFile(file, basePath, zos);
        } else {
            pathName = file.getPath().substring(basePath.length() + 1);
            is = new FileInputStream(file);
            bis = new BufferedInputStream(is);
            zos.putNextEntry(new ZipEntry(new String(pathName.getBytes(),"utf-8")));
            int nRead = 0;
            while ((nRead = bis.read(cache, 0, CACHE_SIZE)) != -1) {
                zos.write(cache, 0, nRead);
            }
            
            bis.close();
            is.close();
        }
    }
  }  
  /** 
   * 对文件或文件目录进行压缩 
   * @param srcPath 要压缩的源文件路径。如果压缩一个文件，则为该文件的全路径；如果压缩一个目录，则为该目录的顶层目录路径 
   * @param zipPath 压缩文件保存的路径。注意：zipPath不能是srcPath路径下的子文件夹 
   * @param zipFileName 压缩文件名 
   * @throws Exception 
   */  
  public void zip(String srcPath, String zipPath) throws Exception  
  {  
      if (StringUtils.isEmpty(srcPath) || StringUtils.isEmpty(zipPath))  
      {  
        System.out.println("Param Exception");
        return ;
      }  
      OutputStream os = null;
      CheckedOutputStream cos = null;
      BufferedOutputStream bos = null;
      ZipOutputStream zos = null;
      try {
          os = new FileOutputStream(zipPath);
          cos = new CheckedOutputStream(os,new CRC32()); 
          bos = new BufferedOutputStream(cos);
          zos = new ZipOutputStream(bos);
          // 解决中文文件名乱码
          zos.setEncoding(CHINESE_CHARSET);
          File file = new File(srcPath);
          String basePath = null;
          if (file.isDirectory()) {//压缩文件夹
              basePath = file.getPath();
          } else {
              basePath = file.getParent();
          }
          zipFile(file, basePath, zos);
          zos.flush();
           
      } catch (Exception e) {
          logger.info("zip exception" + e.getMessage());
          e.printStackTrace();
      } finally{
          try {
              if (zos != null) {
                  zos.closeEntry();
                  zos.close();
              }
              if (cos != null) {
                  cos.close();
              }
              if (bos != null) {
                bos.close();
            }
              if (os != null) {
                  os.close();
              }
          } catch (IOException e) {
              e.printStackTrace();
          }
      }
      
  }
  public void compressExe(String srcPathName, String finalFile) {   
    File zipFile = new File(finalFile);    
    File srcdir = new File(srcPathName);    
    if (!srcdir.exists()){  
      throw new RuntimeException(srcPathName + "不存在！");    
    }   
    
    Project prj = new Project();    
    Zip zip = new Zip();    
    zip.setProject(prj);    
    zip.setDestFile(zipFile);    
    FileSet fileSet = new FileSet();    
    fileSet.setProject(prj);    
    fileSet.setDir(srcdir);    
    fileSet.setIncludes(""); //包括哪些文件或文件夹 eg:zip.setIncludes("*.java");    
    fileSet.setExcludes(""); //排除哪些文件或文件夹    
    zip.addFileset(fileSet);    
    zip.execute();    
  }    
  public static void main(String[] args){
    ZipTool tool = new ZipTool();
    /*tool.compressExe("C:/Users/admin/Desktop/qzbanshi2", "C:/Users/admin/Desktop/qzbanshi2.zip");*/
    try {
      tool.zip("C:/Users/admin/Desktop/qzbanshi2", "C:/Users/admin/Desktop/qzbanshi2.zip");
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
