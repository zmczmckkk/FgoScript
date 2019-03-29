package aoshiScript.entity.httpDownload;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
public class HttpRequest {
    /**
     * 从网络Url中下载文件
     * @param urlStr
     * @param fileName
     * @param savePath
     * @throws IOException
     */
    public static void  downLoadFromUrl(String urlStr,String fileName,String savePath,String toekn) throws IOException{
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        conn.setRequestProperty("lfwywxqyh_token",toekn);

        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);

        //文件保存位置
        File saveDir = new File(savePath);
        if(!saveDir.exists()){
            saveDir.mkdir();
        }
        File file = new File(saveDir+File.separator+fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if(fos!=null){
            fos.close();
        }
        if(inputStream!=null){
            inputStream.close();
        }


        System.out.println("info:"+url+" download success");

    }


    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    public static void main(String[] args) {
        for (int j = 1; j < 13; j++) {
            String str  = "" + j;
            String temp = str.length() == 1 ? "0" + j : "" + j;
            for (int n = 2000; n < 2019; n++) {
                int m = n;
                for (int i = 0; i < 1000; i++) {
                    String url = "https://mokotopgirls.files.wordpress.com/"+n+"/"+ temp +"/" + i + ".jpg";
                    String token="v32Eo2Tw+qWI/eiKW3D8ye7l19mf1NngRLushO6CumLMHIO1aryun0/Y3N3YQCv/TqzaO/TFHw4=";
                    final int k = i;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    HttpRequest.downLoadFromUrl(url, m+"_" +temp + "_" + k + ".jpg", "D:\\down\\MOKO\\ALL", token);
                                } catch (IOException e) {
                                    System.out.println("没有图片");
                                }
                            }
                        }).start();
                }
            }
        }
    }
}
