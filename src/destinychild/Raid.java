package destinychild;

import aoshiScript.entity.IWuNa;
import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import commons.entity.Constant;
import commons.entity.NativeCp;
import commons.util.GameUtil;
import commons.util.PropertiesUtil;
import commons.util.ThreadUtil;
import destinychild.entity.RaidFilterMenu;
import destinychild.entity.RaidStartPage;
import fgoScript.entity.PointColor;
import fgoScript.exception.AppNeedRestartException;
import fgoScript.exception.AppNeedStopException;
import fgoScript.exception.AppNeedUpdateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @description: Raid战斗挂机
 * @author: RENZHEHAO
 * @create: 2019-06-03 21:14
 **/
public class Raid implements IRaid{
    private static final Logger LOGGER = LogManager.getLogger(Raid.class);
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private IWuNa wuna;
    private RaidFilterMenu menu;
    private RaidStartPage rsPage;

    @Override
    public void toggle() {
        if (!isFlag()){
            LOGGER.info("启动raid脚本");
            raidBattleStart();
            setFlag(false);
        }else{
            LOGGER.info("关闭raid脚本");
            raidBattleStop();
            ThreadUtil.waitUntilNoneThread(threadPoolTaskExecutor);
            setFlag(false);

        }
    }

    public RaidFilterMenu getMenu() {
        if (menu == null) {
            String filepath = NativeCp.getUserDir() + "/config/RaidFilterMenu_"+NativeCp.getUserName()+".json";
            menu = JSONObject.parseObject(GameUtil.getJsonString(filepath), RaidFilterMenu.class);
        }
        return menu;
    }

    public RaidStartPage getRsPage() {
        if (rsPage == null) {
            String filepath = NativeCp.getUserDir() + "/config/RaidStartPage.json";
            rsPage = JSONObject.parseObject(GameUtil.getJsonString(filepath), RaidStartPage.class);
        }
        return rsPage;
    }

    public void setThreadPoolTaskExecutor(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    public void setWuna(IWuNa wuna) {
        this.wuna = wuna;
    }

    private boolean flag = false;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    private void oneThreadCheckStart(IWuNa wuna, ExecutorService singleThreadPool){
        singleThreadPool.execute(()-> {
            LOGGER.info("开始检测并处理战斗条件!");
            getRsPage();
            List<Color> colors = new ArrayList<>();
            List<Point> points = new ArrayList<>();

            colors.add(rsPage.getLevelColor());
            colors.add(rsPage.getRankColor());

            points.add(rsPage.getLevelPoint());
            points.add(rsPage.getRankPoint());
            int size = colors.size();
            Color tempColor;
            Point tempPoint;
            Color checkColor;
            int rightCount;
            boolean checkTwice = false;
            while(!wuna.isForceStop()){
                rightCount = 0;
                for (int i = 0; i < size; i++) {
                    if(wuna.isForceStop()){
                        break;
                    }
                    tempColor = colors.get(i);
                    tempPoint = points.get(i);
                    checkColor = GameUtil.getScreenPixel(tempPoint);
                    if (GameUtil.likeEqualColor(tempColor,checkColor,5)){
                        rightCount++;
                    }
                }
                checkColor = GameUtil.getScreenPixel(rsPage.getNoTicketPoint());
                boolean noLevel40 = !GameUtil.likeEqualColor(GameUtil.getScreenPixel(rsPage.getLevelPoint()),rsPage.getLevelColor());
                boolean noTicket = GameUtil.likeEqualColor(checkColor,rsPage.getNoTicketColor())
                        && !GameUtil.likeEqualColor(GameUtil.getScreenPixel(rsPage.getTenTicketPoint()),rsPage.getTenTicketColor());
                boolean noRank = !GameUtil.likeEqualColor(GameUtil.getScreenPixel(rsPage.getRankPoint()),rsPage.getRankColor());
                if (size == rightCount ){
                    if(noLevel40 || noTicket || noRank){
                        GameUtil.mouseMoveByPoint(rsPage.getReturnPoint());
                        GameUtil.mousePressAndReleaseByDD();
                        LOGGER.info("返回页面1");
                    }else {
                        wuna.setGO(false);
                        GameUtil.mouseMoveByPoint(rsPage.getStartPoint());
                        GameUtil.mousePressAndReleaseByDD();
                    }
                } else {
                    if (GameUtil.likeEqualColor(GameUtil.getScreenPixel(rsPage.getStartPoint()),rsPage.getStartColor())){
                        LOGGER.info("出现开始战斗页面");
                        if(checkTwice == false){
                            LOGGER.info("等待3秒");
                            GameUtil.delay(3000);
                            checkTwice = true;
                            LOGGER.info("等待3秒结束");
                        }else {
                            LOGGER.info("返回页面2");
                            GameUtil.mouseMoveByPoint(rsPage.getReturnPoint());
                            GameUtil.mousePressAndReleaseByDD();
                            checkTwice = false;
                        }

                    }
                }
                GameUtil.delay(2000);
                LOGGER.info("单线程检测副本等级，是否有人打，都符合则点击战斗！" + System.currentTimeMillis());
            }
        });
    }
    private int getFactor(){
        String multiFactor = PropertiesUtil.getValueFromFileNameAndKey("multiFactor" , "changeButton_" + NativeCp.getUserName(), "");
        int factor = Integer.parseInt(multiFactor.trim());
        return factor;
    }
    @Override
    public void raidBattleStart(){
        setFlag(true);
        wuna.setForceStop(false);
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("option-pool-%d").setDaemon(false).build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(2, 3,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        //单启动一个线程:实时点色教程操作。用来处理断线，重启，升级，补票等操作(脚本文件)
        singleThreadPool.execute(()-> {
            LOGGER.info("开始处理断线，重启，升级，补票!");
            try {
                wuna.alwaysClickForStrategy("optionClick", 2000, true, false, Constant.DC + "/");
            } catch (AppNeedRestartException e) {
                e.printStackTrace();
            }
            LOGGER.info("结束处理断线，重启，升级，补票!");
        });
        //单启动一个线程:检测并处理战斗条件（逻辑判断）
        oneThreadCheckStart(wuna, singleThreadPool);
        while (isFlag()) {
            if(wuna.isForceStop()){
                break;
            }
            // 设置列表过滤项为：未参加，参加人数（降序）
            try {
                setFilterOptions();
            } catch (AppNeedRestartException e) {
                e.printStackTrace();
            } catch (AppNeedUpdateException e) {
                e.printStackTrace();
            } catch (AppNeedStopException e) {
                break;
            }
            ThreadUtil.waitUntilNoneThread(threadPoolTaskExecutor);
            // 执行点击脚本。当完成一场战斗后，点解结束按钮。结束所有线程
            runClick(threadPoolTaskExecutor.getActiveCount());
            // 当完成一场战斗后，点结束按钮。结束所有线程
            stopOneBattle();
            ThreadUtil.waitUntilNoneThread(threadPoolTaskExecutor);
            setFlag(true);
        }
    }
    /**
     * @Description: 递归判断线程池中是否只有一个线程，然后在执行点击脚本
     * @param aliveCount 运行的线程个数
     * @return: void
     * @Author: RENZHEHAO
     * @Date: 2019/6/5
     */
    private void runClick(int aliveCount){
        // 启动另一个线程：实时点色操作方法。
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    wuna.alwaysClickForStrategy("runClick", null, false,true, Constant.DC + "/");
                } catch (AppNeedRestartException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    public void raidBattleStop() {
        wuna.setGO(false);
        wuna.setForceStop(true);
        GameUtil.setSTOP_SCRIPT(true);
    }

    private void setFilterOptions() throws AppNeedUpdateException, AppNeedRestartException, AppNeedStopException {
        // 最大循环次数
        int maxCicle = 999;
        getMenu();
        //等待菜单按钮
        LOGGER.info("等待菜单按钮");
        List<PointColor> pocoList = new ArrayList<PointColor>();
        pocoList.add(new PointColor(menu.getMenuPoint(), menu.getMenuColor(), true));
        GameUtil.waitUntilAllColor(pocoList,500, Constant.FGOMonitor);
        //如果未出现“确认按钮”，点击“菜单按钮”，
        Color tempColor;
        for (int i = 0; i < maxCicle; i++) {
            if(wuna.isForceStop()){
                break;
            }
            tempColor = GameUtil.getScreenPixel(menu.getConfirmPoint());
            if (!GameUtil.likeEqualColor(tempColor,menu.getConfirmColor(),2)) {
                LOGGER.info("未出现确认按钮点击！");
                GameUtil.mouseMoveByPoint(menu.getMenuPoint());
                GameUtil.mousePressAndReleaseByDD();
                GameUtil.delay(2000);
            }else{
                break;
            }
        }
        //单开一个线程用于选择过滤项
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    wuna.alwaysClickForStrategy("filterClick", null, false, true, Constant.DC + "/");
                } catch (AppNeedRestartException e) {
                    e.printStackTrace();
                }
            }
        });
        //如果两个颜色符合条件，点击确认。
        int count;
        Color temp;
        for (int i = 0; i < maxCicle; i++) {
            if(wuna.isForceStop()){
                break;
            }
            count = 0;
            temp = GameUtil.getScreenPixel(menu.getUnDoPoint());
            if (GameUtil.likeEqualColor(temp, menu.getUnDoColor(), 0)) {
                count++;
            }
            temp = GameUtil.getScreenPixel(menu.getPartPoint());
            if (GameUtil.likeEqualColor(temp, menu.getPartColor(), 0)) {
                count++;
            }
            LOGGER.info("过滤符合个数： " + count +" 个");
            if (count == 2) {
                wuna.setGO(false);
                //点击确定按钮
                for (int j = 0; j < maxCicle; j++) {
                    if(wuna.isForceStop()){
                        break;
                    }
                    temp = GameUtil.getScreenPixel(menu.getConfirmPoint());
                    if (GameUtil.likeEqualColor(temp,menu.getConfirmColor())){
                        GameUtil.mouseMoveByPoint(menu.getConfirmPoint());
                        GameUtil.mousePressAndReleaseByDD();
                    }else if (GameUtil.likeEqualColor(GameUtil.getScreenPixel(menu.getLoadPoint()),menu.getLoadColor())){
                    } else {
                        maxCicle = 0;
                    }
                    GameUtil.delay(1000);
                }
            }
        }
    }
    private void stopOneBattle() {
        Color tempColor;
        int size = menu.getStopPointList().size();
        boolean clickFlag = true;
        int count;
        count = 0;
        while (isFlag()) {
            if(wuna.isForceStop()){
                break;
            }
            if (count > 10 && threadPoolTaskExecutor.getActiveCount() == 0 && GameUtil.likeEqualColor(menu.getMenuColor(),GameUtil.getScreenPixel(menu.getMenuPoint()),2)) {
                LOGGER.info("战斗被提前结束，不明原因，发现菜单按钮，重新开始脚本");
                setFlag(false);
            }
            LOGGER.info("检测战斗结束标志_" + count++);
            for (int i = 0; i < size; i++) {
                tempColor = GameUtil.getScreenPixel(menu.getStopPointList().get(i));
                if (GameUtil.likeEqualColor(tempColor,menu.getStopColorList().get(i),2)) {
                    LOGGER.info("战斗结束，点击返回");
                    // 循环点击，防止点击无效。
                    do {
                        tempColor = GameUtil.getScreenPixel(menu.getStopPointList().get(i));
                        if (!GameUtil.likeEqualColor(tempColor,menu.getStopColorList().get(i),2)) {
                            clickFlag=false;
                        }else{
                            GameUtil.mouseMoveByPoint(menu.getStopClickPoint());
                            GameUtil.mousePressAndReleaseByDD();
                        }
                        GameUtil.delay(2000);
                    }while (clickFlag);
                    setFlag(false);
                    break;
                }
            }
            GameUtil.delay(4000);
        }
        //防止过滤超时后，仍然执行runclick脚本
        wuna.setGO(false);
    }

    public static void main(String[] args) {
    }

}
