package test;

import com.alibaba.fastjson.JSON;
import fgoScript.entity.Gates;
import fgoScript.entity.GatesInfo;
import fgoScript.entity.guda.TrainApGudaziForLittleSpecial;
import fgoScript.util.GameUtil;
import fgoScript.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class TrainApGudaziForLittleSpecialTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void insertIntoTrainingRoomForSpecial() {
        //获取入口信息
        String filepath = System.getProperty("user.dir") + "/config/special_all_train.json";
        String jsonString = GameUtil.getJsonString(filepath);
        GatesInfo gi = JSON.parseObject(jsonString, GatesInfo.class);

        List<Gates> gatesList = gi.getGatesArray();
        int size = gatesList.size();
        Gates gatesTemp;
        int tempId;
        Point locPoint;
        Point gatePoint;
        String idString;
        int classify;
        int acountNum = 10;
        int apNum = 10;
        for (int i = 0; i < size; i++) {
            gatesTemp = gatesList.get(i);
            tempId = gatesTemp.getId();

            //如果已经刷了允许次数跳过
            idString = acountNum + "_" + tempId + "_" + apNum;
            String hasDoString = PropertiesUtil.getValueFromspecialHasDo("hasDo_" + acountNum);

            if(StringUtils.isNotBlank(hasDoString) &&
                    hasDoString.contains(idString) && !hasDoString.contains("(" +idString+")"))     {
                continue;
            }
            locPoint = gatesTemp.getpSetLoc();
//            gatePoint = gatesTemp.getGateByApNum(apNum);
//            // 拉滚动条目标位置
//            GameUtil.mouseMoveByPoint(locPoint);
//            GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
//            // 点击ap本
//            GameUtil.mouseMoveByPoint(gatePoint);
//            GameUtil.mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK);
            //添加hasdo属性
            Map<String, String> hasMap = new HashMap<>();
            if (i == size - 1) {
                hasMap.put("hasDo_" + acountNum, "");
            } else {
                if (hasDoString.contains(idString)){
                    hasMap.put("hasDo_" + acountNum,
                            hasDoString);
                    hasMap.put("hasDo_" + acountNum,
                            hasDoString + acountNum + "_" + tempId + "_" + apNum);
                } else {
                    hasMap.put("hasDo_" + acountNum,
                            hasDoString + acountNum + "_" + tempId + "_" + apNum);
                }
            }
            break;
        }
    }
}