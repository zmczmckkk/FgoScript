package alarm;

/**
 * @description: 常量
 * @author: RENZHEHAO
 * @create: 2019-06-17 07:40
 **/
public class Constants {
    /**
     * 名称
     */
    public static String TITLE = "J AI";//
    /**
     * 闹钟是否启动
     */
    public static boolean ALARM_OK = false;
    /**
     * 播放音乐文件
     */
    public static String FILE_PATH = "";
    /**
     * 响铃时间，格式：HH:mm:ss,默认8点
     */
    public static String ALARM_TIME = "08:00:00";
    /**
     * 响铃时，为ALARM_TIME的分解
     */
    public static int ALARM_HOUR = 8;
    /**
     * 响铃分，为ALARM_TIME的分解
     */
    public static int ALARM_MIN = 0;
    /**
     * 每隔多久响一次,单位分钟
     */
    public static int EVERY_OTHER_TIME = 1;
    /**
     * 每次响铃秒数
     */
    public static int ALARM_SECONDS = 54;
    /**
     * 响铃次数
     */
    public static int ALARM_TIMES = 2;
    /**
     * 闹钟最小宽
     */
    public static int ALARM_WIDTH = 350;
    /**
     * 闹钟最小高
     */
    public static int ALARM_HEIGHT = 250;
    /**
     * 闹钟是否会再次响
     */
    public static boolean ALARM_AGAIN = true;
}
