package alarm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 * @description: TODO
 * @author: RENZHEHAO
 * @create: 2019-06-17 07:38
 **/
public class AlarmTimer {
    private static final Logger LOGGER = LogManager.getLogger(AlarmTimer.class);
    private ScheduledExecutorService executor = null;
    InputStream in = null;
    private AudioStream as = null;
    private boolean isPlay = true;

    public AlarmTimer(int hour, int min) {
        if (hour >= 0 && hour < 24) {
            Constants.ALARM_HOUR = hour;
        }
        if (min >= 0 && min < 60) {
            Constants.ALARM_MIN = min;
        }
        executor = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("Alarm-%d").daemon(false).build());
    }

    public void startAlarm() {
        Date firstDate = null;
        Date now = new Date();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, Constants.ALARM_HOUR);
        c.set(Calendar.MINUTE, Constants.ALARM_MIN);
        c.set(Calendar.SECOND, 0);
        if (now.before(c.getTime())) {
            firstDate = c.getTime();
        } else {
            c.add(Calendar.DAY_OF_MONTH, 1);
            firstDate = c.getTime();
        }
        long initDelay  = firstDate.getTime() - System.currentTimeMillis();
        executor.scheduleAtFixedRate(new AlarmTimerTask(Constants.ALARM_TIMES, this),initDelay,Constants.EVERY_OTHER_TIME * 1000 * 60, TimeUnit.MILLISECONDS);
    }

    public void playMusic() {
        isPlay = true;
        try {
            in = new FileInputStream(Constants.FILE_PATH);// 打 开 一 个 声 音 文 件 流 作 为 输 入
            as = new AudioStream(in);// 用 输 入 流 创 建 一 个AudioStream 对 象
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (as != null) {
            AudioPlayer.player.start(as); //“player” 是AudioPlayer 中 一 静 态 成 员 用 于 控 制 播 放
            int playSecond = Constants.ALARM_SECONDS;
            for (long i = 0; isPlay && i < playSecond; i++) {
                try {
                    LOGGER.info("======音乐还有" + (playSecond - i) + "秒后关闭=====");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            AudioPlayer.player.stop(as);
            LOGGER.info("======音乐已关闭=====");
        }
    }

    public boolean stopMusic() {
        isPlay = false;
        if (as != null) {
            AudioPlayer.player.stop(as);
            LOGGER.info("======音乐已关闭=====");
        }
        return true;
    }

    public ScheduledExecutorService getExecutor() {
        return executor;
    }

    public AudioStream getAs() {
        return as;
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean isPlay) {
        this.isPlay = isPlay;
    }

    public static String getCurrentTime() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return sf.format(new Date());
    }
}
