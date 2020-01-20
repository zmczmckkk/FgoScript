package destinychild;

import aoshiScript.entity.IWuNa;
import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import commons.entity.Constant;
import commons.entity.NativeCp;
import commons.util.*;
import destinychild.entity.DcPointInfo;
import destinychild.entity.DcTask;
import destinychild.entity.SimoLocation;
import destinychild.entity.TaskInfo;
import fgoScript.entity.PointColor;
import fgoScript.exception.AppNeedRestartException;
import fgoScript.exception.AppNeedStopException;
import fgoScript.exception.AppNeedUpdateException;
import fgoScript.exception.AppNeedNextException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.awt.*;
import java.util.ArrayList;
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
    private DcPointInfo dcPointInfo;
    private DcTask dcTask;
    private int delaySeconds;
    private boolean ifrestart;
    private TaskInfo tempTaskInfo;
    private final DcPointInfo DC_POINTINFO = DcPointInfo.getSpringBean();
    public static DaillyMission getSpringBean(){
        return (DaillyMission) MySpringUtil.getApplicationContext().getBean("daillyMission");
    }
    public void daillyMissionStart(){
        String accountStr = PropertiesUtil.getValueFromFileNameAndKey("account", "DCinit", Constant.DC + "/");
        String[] accounts = accountStr.split(",");
        int size = accounts.length;
        int account;
        for (int i = 0; i < size; i++) {
            account = Integer.valueOf(accounts[i]);
            openDC(account);
            startOneAccount(account);
        }
    }
    private void startOneAccount(int account){
        setDelaySeconds(5);
        wuna.setLastClickTime(null);
        // 启动一个线程：用于终止超时点击。
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("option-pool-%d").setDaemon(false).build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(2, 3,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        singleThreadPool.execute(()-> {
            try {
                stopOutTimeClick();
            } catch (AppNeedRestartException e) {
                wuna.setIfThrowException(true);
                e.printStackTrace();
            } catch (AppNeedUpdateException e) {
                e.printStackTrace();
            }
        });
        // main线程：执行主要日常任务。
        goMainMission(account);
        ProcessDealUtil.closeDC(account);
    }
    private void goMainMission(int account) {
        getDcTask(true, account);
        List<TaskInfo> taskInfoList = dcTask.getTasklist();
        /** 任务列表大小 **/
        int size = taskInfoList.size();
        LOGGER.info("任务列表个数位为" + size);
        for (int i = 0; i < size && isFlag(); i++) {
            try {
                startOneMission(i, account,isIfrestart());
            } catch (AppNeedRestartException e) {
                i--;
                setIfrestart(true);
                openDC(account);
                wuna.setLastClickTime(System.currentTimeMillis());
                continue;
            } catch (AppNeedNextException e) {
                setIfrestart(true);
                openDC(account);
                wuna.setLastClickTime(System.currentTimeMillis());
                continue;
            }
        }
    }
    public void startOneMission(int index, int account, boolean ifRestart) throws AppNeedRestartException, AppNeedNextException {
        if (ifRestart && index != 0){
            startOneMission(0,account,false);
            setIfrestart(false);
        }
        Point taskReturnPoint = dcTask.getTaskReturnPoint();
        String accountStr = PropertiesUtil.getValueFromFileNameAndKey("returnMaxCount", "DCinit", Constant.DC + "/");
        int returnMaxcount = Integer.valueOf(accountStr);
        /** 重复次数 **/
        int repeat;
        /** 返回点击次数 **/
        int returnCount;
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
        tempTaskInfo = dcTask.getTasklist().get(index);
        /** 如果未启用就不执行 **/
        if (!tempTaskInfo.isEnable()){
            LOGGER.info("任务名称：" + tempTaskInfo.getTaskName() + " 任务未启用，跳过");
            return;
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
            wuna.alwaysClickForStrategy("" + tempTaskInfo.getTaskName()+"_into", 0, false, false, Constant.DC + "/" + account + "/");
            if (!GameUtil.likeEqualColor(
                                            tempTaskInfo.getMissionPc().getColor(),
                                            GameUtil.getScreenPixel(tempTaskInfo.getMissionPc().getPoint())
                                        )
                ){
                if (tempTaskInfo.isCheckSmallHome()){
                    LOGGER.info("未到达任务页面位置，重新启动！");
                    throw new AppNeedRestartException();
                }else{
                    LOGGER.info("未到达任务页面位置，该任务忽略，直接跳到下一任务！");
                    throw new AppNeedNextException();
                }

            }
            /** 设定自动点击最大延时 **/
            setDelaySeconds(tempTaskInfo.getTaskAutoClickDelaySeconds());
            /** 便利单任务 点击点，执行点击 **/
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
                    //检测网络不稳定
                    for (int i = 0; i < 100; i++) {
                        GameUtil.delay(1000);
                        if (GameUtil.likeEqualColor(GameUtil.getScreenPixel(DC_POINTINFO.getLoading().getPoint()),DC_POINTINFO.getLoading().getColor(),10)){
                            LOGGER.info("进度圈等待中！延后5秒执行脚本");
                            GameUtil.delay(5000);
                        } else {
                            break;
                        }
                    }
                    startModuleName = tempTaskInfo.getStartModleName();
                    if (!"".equals(startModuleName)){
                        IModule module = (IModule) MySpringUtil.getApplicationContext().getBean(startModuleName);
                        module.start();
                    } else {
                        wuna.alwaysClickForStrategy("" + tempTaskInfo.getTaskName()+"_click", 500, false, true, Constant.DC + "/" + account + "/");
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
            wuna.alwaysClickForStrategy("" + tempTaskInfo.getTaskName()+"_click", 0, false, false, Constant.DC + "/" + account + "/");
        }
        returnCount = 0;
        while (tempTaskInfo.isReturnTop() && isFlag() && !GameUtil.likeEqualColor(GameUtil.getScreenPixel(dcTask.getTaskPagePoint()),dcTask.getTaskPageColor())){
            GameUtil.mouseMoveByPoint(taskReturnPoint);
            GameUtil.mousePressAndReleaseByDD();
            GameUtil.delay(5000);
            if (returnCount++ > returnMaxcount) {
                throw new AppNeedRestartException();
            }
        }
        ThreadUtil.waitUntilNoneThread(threadPoolTaskExecutor);
        LOGGER.info("任务： " + tempTaskInfo.getTaskName() + " 执行完了！");
    }
    private void stopOutTimeClick() throws AppNeedRestartException, AppNeedUpdateException {
        int check = 0;
        do {
            long minus = wuna.getLastClickTime() == null ? 0 : System.currentTimeMillis() - wuna.getLastClickTime();
            if ( minus > getDelaySeconds()*1000) {
                if (GameUtil.likeEqualColor(GameUtil.getScreenPixel(DC_POINTINFO.getLoading().getPoint()),DC_POINTINFO.getLoading().getColor(),10)){
                    LOGGER.info("进度圈等待中！");
                    GameUtil.delay(3000);
                } else {
                    LOGGER.info("自动点击超时了！" +getDelaySeconds()+"秒");
                    wuna.setGO(false);
                }
            }
            // 意外检查
            if (check != 0 && (check % 8 == 0)) {
                GameUtil.waitInteruptSolution(check, "DCMonitor");
            }
            //进入时，首页检测
            if(tempTaskInfo!=null && tempTaskInfo.getTaskName().equals("gameInto") && GameUtil.likeEqualColor(GameUtil.getScreenPixel(dcTask.getTaskPagePoint()),dcTask.getTaskPageColor())){
                LOGGER.info("已经到达了任务首页！");
                wuna.setGO(false);
                GameUtil.delay(3000);
            }
            GameUtil.delay(1000);
            check++;
    } while (isFlag());
    }
    public void openDC(int account) {
        SimoLocation simoLocation = SimoLocation.getInstance();
        ProcessDealUtil.startDC(account);
        List<PointColor> pocoList = new ArrayList<PointColor>();
        pocoList.add(new PointColor(simoLocation.getOriginalPoint(), simoLocation.getTargetPCList().get(0).getColor(),true));
        try {
            GameUtil.waitUntilOneColor(pocoList, Constant.DCMonitor);
        } catch (AppNeedUpdateException e) {
            e.printStackTrace();
        } catch (AppNeedRestartException e) {
            openDC(account);
            e.printStackTrace();
            return;
        } catch (AppNeedStopException e) {
            e.printStackTrace();
        }
        simoLocation.moveToDestinyPoint();
    }
    public void daillyMissionStop(){
        wuna.setGO(false);
        wuna.setForceStop(true);
        GameUtil.setSTOP_SCRIPT(true);
    }
    public void toggle(){
        if (!isFlag()){
            LOGGER.info("启动每日脚本");
            setFlag(true);
            daillyMissionStart();
            setFlag(false);
        }else{
            LOGGER.info("关闭每日脚本");
            daillyMissionStop();
            try {
                ThreadUtil.waitUntilNoneThread(threadPoolTaskExecutor);
            } catch (AppNeedRestartException e) {
                e.printStackTrace();
            }
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

    public DcTask getDcTask(boolean reload,int account) {
        if (reload || dcTask == null) {
            String filepath = NativeCp.getUserDir() + "/config/"+ Constant.DC +"/"+ account +"/" +"dcTask_"+NativeCp.getUserName()+".json";
            LOGGER.info("JSON路径为： " + filepath);
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

    public void setDcPointInfo(DcPointInfo dcPointInfo) {
        this.dcPointInfo = dcPointInfo;
    }

    public boolean isIfrestart() {
        return ifrestart;
    }

    public void setIfrestart(boolean ifrestart) {
        this.ifrestart = ifrestart;
    }

    public DcPointInfo getDcPointInfo() {
        DcPointInfo dpi = DcPointInfo.getInstance();
        return dpi;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            if (i==1){
              i-- ;
              continue;
            }
        }
    }
}
