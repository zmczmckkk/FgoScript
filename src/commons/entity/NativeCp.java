package commons.entity;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class NativeCp {
    /** 操作系统用户目录 **/
    private String userProfile;
    /** 程序执行目录 **/
    private String userDir;
    /** 操作系统用户名 **/
    private String userName;

    public static String getUserDir() {
        return   System.getProperty("user.dir");//C:\Users\RENZHEHAO\OneDrive
    }

    public void setUserDir(String userDir) {
        this.userDir = userDir;
    }

    public static String getUserName() {
        return System.getenv("USERNAME");//RENZHEHAO
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserProfile() {
        return System.getenv("USERPROFILE");
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public static void openByssociatedExe(String fileNamePath){
        Desktop desk=Desktop.getDesktop();
        File file = new File(fileNamePath);
        try {
            desk.open(file); //调用open（File f）方法打开文件
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "打开文件失败！", "错误", JOptionPane.WARNING_MESSAGE);
        }
    }
}
