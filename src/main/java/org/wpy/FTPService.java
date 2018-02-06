package org.wpy;

import org.apache.commons.net.ftp.FTPClient;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.net.SocketException;

/**
 * DESC linux ftp 上传工具
 *
 * @author wpy
 * @create 2017-07-31 上午10:45
 **/
public class FTPService {

    public static String ftpHost;
    public static int port;
    public static String userName;
    public static String passWord;
    public static String path;
    public static String ftpEncode;
    public static int defaultTimeout;


    static {
        try {

            ftpHost = "172.19.110.223";//linux                   
            //ftpHost = "192.168.1.103";//windows                
            port = 21;
            userName = "root";
            passWord = "123456";
            path = "/tmp/"; //ftp下哪个目录------------------         
            ftpEncode = "UTF-8";
            defaultTimeout = 30000;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        System.out.println(isImage(new File("/Users/wpy/Desktop/2.jpg")));
    }

    /**
     * 通过读取文件并获取其width及height的方式，来判断判断当前文件是否图片.
     *
     * @param imageFile
     * @return
     */
    public static boolean isImage(File imageFile) {
        if (!imageFile.exists()) {
            return false;
        }
        Image img = null;
        try {
            img = ImageIO.read(imageFile);
            if (img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            img = null;
        }
    }

    /**
     * 上传ftp
     *
     * @param localFile
     * @param fileNewName
     * @return
     * @throws SocketException
     * @throws IOException
     */
    public boolean uploadFile(File localFile, String fileNewName) throws IOException {
        boolean flag;
        FileInputStream is = new FileInputStream(localFile);
        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding(ftpEncode);
        ftpClient.connect(ftpHost);
        ftpClient.login(userName, passWord);
        ftpClient.setDefaultPort(port);
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
        ftpClient.enterLocalPassiveMode();
        ftpClient.setDefaultTimeout(defaultTimeout);
        ftpClient.setBufferSize(1024 * 1024);
        boolean chagenDirFlag = ftpClient.changeWorkingDirectory(path);
        if (chagenDirFlag == false) {
            ftpClient.makeDirectory(path);
            ftpClient.changeWorkingDirectory(path);
        }
        String newFileName = fileNewName;
        flag = ftpClient.storeFile(newFileName, is);
        is.close();
        ftpClient.logout();
        ftpClient.disconnect();
        if (flag) {
            System.out.println(fileNewName + "上传图片成功");
        } else {
            System.out.println(fileNewName + "上传图片失败.......");
        }
        return flag;
    }

    /**
     * 下载FTP
     *
     * @param ftpName   ftp上的文件名
     * @param localFile 保存的本地地址
     * @return
     * @throws SocketException
     * @throws IOException
     */
    public boolean downloadFile(String ftpName, File localFile) throws IOException {
        boolean flag;
        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding(ftpEncode);
        ftpClient.connect(ftpHost);
        ftpClient.login(userName, passWord);
        ftpClient.setDefaultPort(port);
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
        ftpClient.enterLocalPassiveMode();
        ftpClient.setDefaultTimeout(defaultTimeout);
        ftpClient.setBufferSize(1024 * 1024);
        boolean chagenDirFlag = ftpClient.changeWorkingDirectory(path);
        if (chagenDirFlag == false) {
            System.out.println("ftp上目录切换失败");
            return false;
        }
        OutputStream os = new FileOutputStream(localFile);
        flag = ftpClient.retrieveFile(ftpName, os);
        if (true == flag) {
            System.out.println(ftpName + "   文件下载成功");
        } else {
            System.out.println(ftpName + "   文件下载失败");
        }
        os.flush();
        os.close();
        ftpClient.logout();
        ftpClient.disconnect();
        return flag;
    }

    /**
     * 删除FTP
     *
     * @param ftpName ftp上的文件名
     * @return
     * @throws SocketException
     * @throws IOException
     */
    public boolean deleteFile(String ftpName) throws IOException {
        boolean flag;
        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding(ftpEncode);
        ftpClient.connect(ftpHost);
        ftpClient.login(userName, passWord);
        ftpClient.setDefaultPort(port);
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
        ftpClient.enterLocalPassiveMode();
        ftpClient.setDefaultTimeout(defaultTimeout);
        boolean chagenDirFlag = ftpClient.changeWorkingDirectory(path);
        if (chagenDirFlag == false) {
            System.out.println("ftp上目录切换失败");
            return false;
        }
        flag = ftpClient.deleteFile(ftpName);
        if (true == flag) {
            System.out.println(ftpName + "   文件删除成功");
        } else {
            System.out.println(ftpName + "   文件删除失败");
        }
        ftpClient.logout();
        ftpClient.disconnect();
        return flag;
    }
}