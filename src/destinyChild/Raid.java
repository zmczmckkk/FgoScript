package destinyChild;

import aoshiScript.entity.IWuNa;
import aoshiScript.entity.WuNa;
import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import commons.entity.NativeCp;
import commons.util.GameUtil;
import destinyChild.entity.RaidFilterMenu;
import fgoScript.entity.PointColor;
import fgoScript.exception.FgoNeedRestartException;
import fgoScript.exception.FgoNeedStopException;
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
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private IWuNa wuna;
    private RaidFilterMenu menu;

    public RaidFilterMenu getMenu() {
        if (menu == null) {
            String filepath = NativeCp.getUserDir() + "/config/RaidFilterMenu.json";
            menu = JSONObject.parseObject(GameUtil.getJsonString(filepath), RaidFilterMenu.class);
        }
        return menu;
    }

    public void setMenu(RaidFilterMenu menu) {
        this.menu = menu;
    }

    public void setThreadPoolTaskExecutor(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }
    public void setWuna(WuNa wuna) {
        this.wuna = wuna;
    }

    private boolean flag = true;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    private void optionClik(){
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("option-pool-%d").setDaemon(false).build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 2,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        singleThreadPool.execute(()-> {
                System.out.println("optionClick start!");
                new WuNa("optionClick").alwaysClickForStrategy("optionClick", 2000, true, false);
                System.out.println("optionClick end!");
        });
    }
    @Override
    public void raidBattleStart(){
        //单启动一个线程B:实时点色教程操作。用来处理断线，重启，升级，补票等操作
        optionClik();
        while (isFlag() && wuna.isScucess()) {
            // 设置列表过滤项为：未参加，参加人数（降序）
            try {
                setFilterOptions();
            } catch (FgoNeedRestartException e) {
                e.printStackTrace();
            } catch (FgoNeedStopException e) {
                e.printStackTrace();
            }
            waitUntilNoneThread();
            // 执行点击脚本。当完成一场战斗后，点解结束按钮。结束所有线程
            runClick(threadPoolTaskExecutor.getActiveCount());
        }
    }
    /**
     * @Description: 当线程池无活动时，执行下一步
     * @return: void
     * @throw:
     * @Author: RENZHEHAO
     * @Date: 2019/6/7
     */
    private void waitUntilNoneThread(){
        do{
            GameUtil.delay(3000);
            System.out.println("线程个数: " + threadPoolTaskExecutor.getActiveCount());
        } while (threadPoolTaskExecutor.getActiveCount() != 0);
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
                wuna.alwaysClickForStrategy("runClick", null, false,true);
            }
        });
        // 当完成一场战斗后，点解结束按钮。结束所有线程
        stopOneBattle();
    }
    @Override
    public void raidBattleStop() {
        setFlag(false);
        wuna.setGO(false);
    }

    private void setFilterOptions() throws FgoNeedRestartException, FgoNeedStopException {
        // 最大循环次数
        int maxCicle = 999;
        getMenu();
        //等待菜单按钮
        List<PointColor> pocoList = new ArrayList<PointColor>();
        pocoList.add(new PointColor(menu.getMenuPoint(), menu.getMenuColor(), true));
        GameUtil.waitUntilAllColor(pocoList,500);
        //如果未出现“确认按钮”，点击“菜单按钮”，
        Color tempColor;
        for (int i = 0; i < maxCicle; i++) {
            tempColor = GameUtil.getScreenPixel(menu.getConfirmPoint());
            if (!GameUtil.likeEqualColor(tempColor,menu.getConfirmColor(),2)) {
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
                wuna.alwaysClickForStrategy("filterClick", null, false, true);
            }
        });
        //如果两个颜色符合条件，点击确认。
        int count;
        Color temp;
        for (int i = 0; i < maxCicle; i++) {
            count = 0;
            temp = GameUtil.getScreenPixel(menu.getUnDoPoint());
            if (GameUtil.likeEqualColor(temp, menu.getUnDoColor(), 0)) {
                count++;
            }
            temp = GameUtil.getScreenPixel(menu.getPartPoint());
            if (GameUtil.likeEqualColor(temp, menu.getPartColor(), 0)) {
                count++;
            }
            if (count == 2) {
                wuna.setGO(false);
                //点击确定按钮
                for (int j = 0; j < maxCicle; j++) {
                    temp = GameUtil.getScreenPixel(menu.getConfirmPoint());
                    if (GameUtil.likeEqualColor(temp,menu.getConfirmColor())){
                        GameUtil.mouseMoveByPoint(menu.getConfirmPoint());
                        GameUtil.mousePressAndReleaseByDD();
                    }else if (GameUtil.likeEqualColor(GameUtil.getScreenPixel(menu.getLoadPoint()),menu.getLoadColor())){
                    } else {
                        maxCicle = 0;
                    }
                    GameUtil.delay(1000);
                    System.out.println(maxCicle);
                }
            }
        }
    }
    private void stopOneBattle() {
        Color tempColor;
        int size = menu.getStopPointList().size();
        boolean flag = true;
        boolean clickFlag = true;
        while (flag) {
            for (int i = 0; i < size; i++) {
                System.out.println(Thread.currentThread().getName() + " : scanning! " + i);
                tempColor = GameUtil.getScreenPixel(menu.getStopPointList().get(i));
                if (GameUtil.likeEqualColor(tempColor,menu.getStopColorList().get(i),2)) {
                    wuna.setGO(false);
                    waitUntilNoneThread();
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
                    flag = false;
                    break;
                }
            }
            GameUtil.delay(2000);
        }
    }

    public static void main(String[] args) {
    }

}
