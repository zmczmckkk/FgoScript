package destinychild;

import com.alibaba.fastjson.JSONObject;
import commons.entity.NativeCp;
import commons.util.GameUtil;
import fgoScript.entity.PointColor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 百鬼夜行实体
 * @author: RENZHEHAO
 * @create: 2019-06-15 08:58
 **/
public class Light implements IModule {
    private boolean flag = true;
    private boolean tFlag = true;
    private boolean exFlag = true;
    private boolean exploreFlag = false;
    private ThreadPoolTaskExecutor poolExecutor;
    public void settFlag(boolean tFlag) {
        this.tFlag = tFlag;
    }

    private LightData lightData;
    @Override
    public void start() {
        reFreshLightData();
        // 线程1： 检测确认按钮
        poolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                checkConfirmButton();
            }
        });
        // 线程2： 夜行探索点击方法
        poolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                explore();
            }
        });
        LOGGER.info("是否运行状态： " + isFlag());
        while (poolExecutor.getActiveCount() != 0){
            delay(1000);
        }
        LOGGER.info("结束百鬼夜行！");
    }
    /**
     * @Description: 夜行探索点击方法
     * @return: void
     * @Author: RENZHEHAO
     * @Date: 2019/6/16
     */
    private void explore(){
        reFreshLightData();
        int count = 0;
        int exploreCount = 0;
        Color temp;
        // 检测button按钮和免费灯点
        while(isFlag() && isExFlag()){
            temp = GameUtil.getScreenPixel(lightData.getButtonPoint());
            if (GameUtil.likeEqualColor(temp, lightData.getButtonColor(), 1)){
                GameUtil.mouseMoveByPoint(lightData.getClickPoint());
                exploreCount++;
                break;
            }
            temp = GameUtil.getScreenPixel(lightData.getFreeFinishPoint());
            if (GameUtil.likeEqualColor(temp, lightData.getFreeFinishColor(), 1)){
                LOGGER.info("无免费灯，结束夜行");
                GameUtil.mouseMoveByPoint(lightData.getClickPoint());
                setFlag(false);
            }
            delay(1000);

        }
        while(isFlag() && isExFlag()){
            count = checkGreen(count);
            GameUtil.mousePressAndReleaseByDdQuick(lightData.getSmStep());
            delay(lightData.getClickDelay());
        }
        delay(1000);
    }
    /**
     * @Description: 检测确认按钮
     * @return: void
     * @Author: RENZHEHAO
     * @Date: 2019/6/16
     */
    private void checkConfirmButton(){
        // 检测确认按钮
        Color checkColor;
        List<PointColor> pcList;
        int size;
        Point tempPoint;
        Color tempColor;
        do{
            checkColor = GameUtil.getScreenPixel(lightData.getConfirmPoint());
            if (GameUtil.likeEqualColor(checkColor, lightData.getConfirmColor(), 1)){
                this.setExFlag(false);
                // 等待线程<=1
                while(poolExecutor.getActiveCount() > 1){
                    LOGGER.info(poolExecutor.getActiveCount());
                    delay(1000);
                }
                this.setExFlag(true);
                GameUtil.mouseMoveByPoint(lightData.getConfirmPoint());
                GameUtil.mousePressAndReleaseByDD();
                //继续执行探索
                poolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        explore();
                    }
                });
            }

            pcList = new ArrayList<>();
            pcList.add(new PointColor(lightData.getFinishPoint(),lightData.getFinishColor(), true));
            pcList.add(new PointColor(lightData.getStartPoint(),lightData.getStartColor(), true));
            size = pcList.size();
            for (int i = 0; i < size; i++) {
                tempPoint = pcList.get(i).getPoint();
                tempColor = pcList.get(i).getColor();
                checkColor = GameUtil.getScreenPixel(tempPoint);
                if (GameUtil.likeEqualColor(checkColor, tempColor, 1)){
                    GameUtil.mouseMoveByPoint(tempPoint);
                    GameUtil.mousePressAndReleaseByDD();
                }
            }
            delay(1000);
        }while (isFlag());
    }
    @Override
    public void stop() {
        setFlag(false);
    }

    @Override
    public void toggle() {
        if (tFlag) {
            if ( poolExecutor.getActiveCount() == 0) {
                setFlag(true);
                settFlag(false);
                start();
            }
        } else {
            stop();
            while (poolExecutor.getActiveCount()!=0){
                delay(1000);
            }
            settFlag(true);
        }
    }

    private int checkGreen(int count){
        Color leftColor = GameUtil.getScreenPixel(lightData.getLeftPoint());
        Color rightColor = GameUtil.getScreenPixel(lightData.getRightPoint());
        int leftColorValue = leftColor.getGreen()+leftColor.getRed()+leftColor.getBlue();
        int rightColorValue = rightColor.getGreen()+rightColor.getRed()+rightColor.getBlue();
        int time = 0;
        int delay = 0;
        if (leftColorValue < 100 && count > 0){
            time = lightData.getSmStep();
            LOGGER.info("L<100 ");
        }else if(rightColorValue > 100){
            time = lightData.getAmStep();
            LOGGER.info("R>100 ");
            count++;
        }else{
            time = lightData.getSmStep();
            LOGGER.info("L>100,R<100 ");
        }
        time = time > 10 ? time : 10;
        LOGGER.info(time);
        lightData.setSmStep(time);
        return count;
    }
    private void delay(int mscond){
        try {
            Thread.sleep(mscond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public LightData getLightData() {
        return lightData;
    }
    public void reFreshLightData() {
        String filepath = NativeCp.getUserDir() + "/config/LightData.json";
        lightData = JSONObject.parseObject(GameUtil.getJsonString(filepath), LightData.class);
    }
    private static final Logger LOGGER = LogManager.getLogger(Light.class);

    public static void main(String[] args) {
    }

    public void setPoolExecutor(ThreadPoolTaskExecutor poolExecutor) {
        this.poolExecutor = poolExecutor;
    }

    public boolean isExFlag() {
        return exFlag;
    }

    public void setExFlag(boolean exFlag) {
        this.exFlag = exFlag;
    }

    public boolean isExploreFlag() {
        return exploreFlag;
    }

    public void setExploreFlag(boolean exploreFlag) {
        this.exploreFlag = exploreFlag;
    }
}
