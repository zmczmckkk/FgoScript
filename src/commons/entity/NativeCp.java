package commons.entity;

public class NativeCp {
    private String UserDir;
    private String UserName;

    public static String getUserDir() {
        return  System.getenv("USERPROFILE");//C:\Users\RENZHEHAO\OneDrive
    }

    public void setUserDir(String userDir) {
        UserDir = userDir;
    }

    public static String getUserName() {
        return System.getenv("USERNAME");//RENZHEHAO
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}
