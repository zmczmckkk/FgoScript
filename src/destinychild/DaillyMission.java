package destinychild;

import aoshiScript.entity.IWuNa;
import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import commons.entity.NativeCp;
import commons.util.GameUtil;
import commons.util.MySpringUtil;
import commons.util.ThreadUtil;
import destinychild.entity.DcTask;
import destinychild.entity.TaskInfo;
import fgoScript.entity.panel.FgoFrame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.awt.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author RENZHEHAO
 * @version 1.0.0
 * @ClassName DaillyMission.java
 * @Description 每日任务自动执行实体
 * @createTime 2019年11月06日 11:56:00
 */
public class DaillyMission {
    private static final Logger LOGGER = LogManager.getLogger(DaillyMission.class);
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private boolean flag = false;
    private IWuNa wuna;
    private IModule light;
    private DcTask dcTask;
    private int delaySeconds;
    public void daillyMissionStart(){
        setDelaySeconds(5);
        wuna.setLastClickTime(null);
        // 启动一个线程：用于终止超时点击。
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("option-pool-%d").setDaemon(false).build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(2, 3,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        singleThreadPool.execute(()-> {
            stopOutTimeClick();
        });
        // main线程：执行主要日常任务。
        goMainMission();
    }

    private void goMainMission() {
        DcTask dcTask = getDcTask(true);
        List<TaskInfo> taskInfoList = dcTask.getTasklist();
        Point taskReturnPoint = dcTask.getTaskReturnPoint();
        /** 任务列表大小 **/
        int size = taskInfoList.size();
        /** 重复次数 **/
        int repeat;
        /** 单个任务 **/
        TaskInfo tempTaskInfo;
        /** 单个任务点击点集合（点） **/
        List<Point> tempTaskClickPoints;
        /** 单个任务点击点集合（色） **/
        List<Color> tempTaskClickColors;
        /** 单个任务点击点集合 大小 **/
        int clickPointsSize;
        int clickColorsSize;
        Point temPoint;
        Color tempColor;
        setFlag(true);
        wuna.setForceStop(false);
        String startModuleName;
        for (int i = 0; i < size && isFlag(); i++) {
            tempTaskInfo = taskInfoList.get(i);
            /** 如果未启用就不执行 **/
            if (!tempTaskInfo.isEnable()){
                continue;
            }
            repeat = tempTaskInfo.getTaskPointRepetitions();
            LOGGER.info("开始执行任务： " + tempTaskInfo.getTaskName());
            tempTaskClickPoints = tempTaskInfo.getTaskClickPoints();
            tempTaskClickColors = tempTaskInfo.getTaskClickColors();
            clickPointsSize = tempTaskClickPoints.size();
            clickColorsSize = tempTaskClickColors.size();
            if (clickPointsSize > 0) {
                /** 设定自动点击最大延时 **/
                setDelaySeconds(tempTaskInfo.getTaskInToDelaySeconds());
                /** 进入任务场景 **/
                LOGGER.info("进入任务场景");
                wuna.alwaysClickForStrategy("" + tempTaskInfo.getTaskName()+"_into", 0, false, false);
                GameUtil.delay(5000);
                /** 设定自动点击最大延时 **/
                setDelaySeconds(tempTaskInfo.getTaskAutoClickDelaySeconds());
                for (int k = 0; k < repeat && isFlag(); k++) {
                    for (int j = 0; j < clickPointsSize && isFlag(); j++) {
                        temPoint = tempTaskClickPoints.get(j);
                        tempColor = GameUtil.getScreenPixel(temPoint);
                        if (clickColorsSize > 0 ) {
                            if(GameUtil.likeEqualColor(tempColor, tempTaskClickColors.get(j))){
                                GameUtil.mouseMoveByPoint(temPoint);
                                GameUtil.mousePressAndReleaseByDD();
                            }else{
                                continue;
                            }
                        } else {
                            GameUtil.mouseMoveByPoint(temPoint);
                            GameUtil.mousePressAndReleaseByDD();
                        }

                        startModuleName = tempTaskInfo.getStartModleName();
                        if (!"".equals(startModuleName)){
                            IModule module = (IModule) MySpringUtil.getApplicationContext().getBean(startModuleName);
                            module.start();
                        } else {
                            wuna.alwaysClickForStrategy("" + tempTaskInfo.getTaskName()+"_click", 500, false, true);
                        }
                        if (tempTaskInfo.isSmallReturn()){
                            GameUtil.mouseMoveByPoint(taskReturnPoint);
                            GameUtil.mousePressAndReleaseByDD();
                        }
                        GameUtil.delay(5000);
                    }
                }
            } else {
                /** 设定自动点击最大延时 **/
                setDelaySeconds(tempTaskInfo.getTaskInToDelaySeconds());
                wuna.alwaysClickForStrategy("" + tempTaskInfo.getTaskName()+"_click", 0, false, false);
            }
            while (isFlag() && !GameUtil.likeEqualColor(GameUtil.getScreenPixel(dcTask.getTaskPagePoint()),dcTask.getTaskPageColor())){
                GameUtil.mouseMoveByPoint(taskReturnPoint);
                GameUtil.mousePressAndReleaseByDD();
                GameUtil.delay(5000);
            }
            ThreadUtil.waitUntilNoneThread(threadPoolTaskExecutor);
            LOGGER.info("任务： " + tempTaskInfo.getTaskName() + " 执行完了！");
        }
    }
    private void stopOutTimeClick(){
        do {
            long minus = wuna.getLastClickTime() == null ? 0 : System.currentTimeMillis() - wuna.getLastClickTime();
            if ( minus > getDelaySeconds()*1000) {
                LOGGER.info("自动点击超过了！" +getDelaySeconds()+"秒");
                wuna.setGO(false);
            }
            GameUtil.delay(1000);
        } while (isFlag());
    }
    public void daillyMissionStop(){
        wuna.setGO(false);
        wuna.setForceStop(true);
        GameUtil.setSTOP_SCRIPT(true);
    }
    public void toggle(){
        if (!isFlag()){
            LOGGER.info("启动每日脚本");
            daillyMissionStart();
            setFlag(false);
        }else{
            LOGGER.info("关闭每日脚本");
            daillyMissionStop();
            ThreadUtil.waitUntilNoneThread(threadPoolTaskExecutor);
            setFlag(false);
        }
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void setThreadPoolTaskExecutor(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    public void setWuna(IWuNa wuna) {
        this.wuna = wuna;
    }

    public DcTask getDcTask(boolean reload) {
        if (reload || dcTask == null) {
            String filepath = NativeCp.getUserDir() + "/config/dcTask_"+NativeCp.getUserName()+".json";
            dcTask = JSONObject.parseObject(GameUtil.getJsonString(filepath), DcTask.class);
        }
        return dcTask;
    }

    public int getDelaySeconds() {
        return delaySeconds;
    }

    public void setDelaySeconds(int delaySeconds) {
        this.delaySeconds = delaySeconds;
    }
    public void setLight(IModule light) {
        this.light = light;
    }
}
