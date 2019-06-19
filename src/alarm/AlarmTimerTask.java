package alarm;

import java.util.TimerTask;

/**
 * @description: TOD
 * @author: RENZHEHAO
 * @create: 2019-06-17 07:39
 **/
public class AlarmTimerTask extends TimerTask {
    private int times = 0;
    AlarmTimer alTimer = null;

    public AlarmTimerTask(int times, AlarmTimer alTimer) {
        this.times = times;
        this.alTimer = alTimer;
    }

    @Override
    public void run() {
        System.out.println("响铃事件---" + AlarmTimer.getCurrentTime());
        if (!Constants.ALARM_AGAIN) {
            times = 0;
        }
        if (times > 0) {
            System.out.println("倒数第" + times + "次响玲");
            alTimer.playMusic();
            --times;
        } else {
            System.out.println("响铃Over--" + AlarmTimer.getCurrentTime());
            alTimer.stopMusic();
            this.cancel();
            alTimer.getExecutor().shutdown();
        }
    }
}
