package commons.util;

import java.io.File;

public class FileUtils {
    /**
     * 删除指定文件夹下的所有文件，此文件夹内只有文件，没有任何文件夹
     * @param filePath 文件夹路径
     * @return
     */
    public static Boolean deleteAllFiles(String filePath) {
        boolean result = false;
        File file = new File(filePath);
        File temp;
        if (file.exists()) {
            String[] tempList = file.list();
            for (String string : tempList) {
                temp = new File(filePath + "/" + string);
                if (temp.isFile()) {
                    temp.delete();
                }
            }
            tempList = file.list();
            if (tempList.length == 0) {
                result = true;
            }
        }
        return result;
    }

    /**
     *
     * @param path 文件路径
     * @param oldString 要替换的字符串
     * @param newString 新字符串
     */
    public static void recursiveTraversalFolder(String path, String oldString, String newString) {
        File folder = new File(path);
        if (folder.exists()) {
            File[] fileArr = folder.listFiles();
            if (null == fileArr || fileArr.length == 0) {
                System.out.println("文件夹是空的!");
                return;
            } else {
                File newDir = null;
                String newName = "";
                String fileName = null;
                File parentPath = new File("");
                for (File file : fileArr) {
                    if (file.isDirectory()) {
                        System.out.println("文件夹:" + file.getAbsolutePath() + "，继续递归！");
                        recursiveTraversalFolder(file.getAbsolutePath(), oldString, newString);
                    } else {
                        fileName = file.getName();
                        parentPath = file.getParentFile();
                        if (fileName.contains(oldString)) {
                            newName = fileName.replaceAll(oldString, newString);
                            newDir = new File(parentPath + "/" + newName);
                            file.renameTo(newDir);
                            System.out.println("修改后：" + newDir);
                        }
                    }
                }
            }
        } else {
            System.out.println("文件不存在!");
        }
    }

    public static void main(String[] args) {

        FileUtils.recursiveTraversalFolder(
                "C:\\Users\\RENZHEHAO\\OneDrive\\Code\\config\\DC\\12",
                "RENZHEHAO",
                "RZH-SERVER"
        );
    }
}
