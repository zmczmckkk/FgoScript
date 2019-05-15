package fgoScript.entity.guda;

import fgoScript.constant.GameConstant;
import fgoScript.constant.PointInfo;
import fgoScript.entity.Gates;
import fgoScript.entity.GatesInfo;
import commons.util.GameUtil;
import commons.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrainApGudaziForLittleSpecial extends TrainApGudazi{
    private Map<String, String> hasMap = new HashMap<>();
    private Point suppPoint;
    public Map<String, String> getHasMap() {
        return hasMap;
    }

    public void setHasMap(Map<String, String> hasMap) {
        this.hasMap = hasMap;
    }

    public Point getSuppPoint() {
        return suppPoint;
    }

    public void setSuppPoint(Point suppPoint) {
        this.suppPoint = suppPoint;
    }

    @Override
    public void intoAndSelect(int apNum, int acountNum) throws Exception {
        insertIntoTrainingRoomForSpecial(apNum,acountNum);
    }

    @Override
    public Point getSuppotServant() {
        return getSuppPoint();
    }
    public void insertIntoTrainingRoomForSpecial(int apNum, int acountNum) throws Exception {
        // 拉滚动条至最下上
        GameUtil.mouseMoveByPoint(PointInfo.P_SCROLL_REST_DOWN);
        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        GameUtil.mouseMoveByPoint(PointInfo.P_SCROLL_REST_TOP);
        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        // 进入训练场
        // 周回进去
        Point p4 = PointInfo.getP_WEEK_ENTRANCE();
        GameUtil.mouseMoveByPoint(p4);
        GameUtil.mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK);

        GameUtil.delay(GameConstant.DELAY*5);

        // 拉滚动条至最下
        Point p16 = PointInfo.P_SCROLL_REST_DOWN;
        GameUtil.mouseMoveByPoint(p16);
        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        // 拖拽画面
        Point start = PointInfo.P_DAILY_SLICE_STRAT;
        Point end = PointInfo.P_DAILY_SLICE_END;
        moveBySteps(start, end);

        // 点击日常
        Point p6 = PointInfo.P_DAILY_ENTRANCE;
        GameUtil.mouseMoveByPoint(p6);
        GameUtil.mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK);
        //获取入口信息
        GatesInfo gi = getSpecialGatesInfo();
        // 拉滚动条至最下
        GameUtil.mouseMoveByPoint(gi.getSliceDownPoint());
        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);

        List<Gates> gatesList = gi.getGatesArray();
        int size = gatesList.size();
        Gates gatesTemp;
        int tempId;
        Point locPoint;
        Point gatePoint;
        String idString;
        int classify;
        for (int i = 0; i < size; i++) {
            gatesTemp = gatesList.get(i);
            tempId = gatesTemp.getId();
            setSuppPoint(gatesTemp.getSuppPoint());

            //如果已经刷了允许次数跳过
            idString = acountNum + "_" + tempId + "_" + apNum;
            String hasDoString = PropertiesUtil.getValueFromspecialHasDo("hasDo_" + acountNum + "_" + apNum);
            if(StringUtils.isNotBlank(hasDoString) &&
                    hasDoString.contains(idString) && !hasDoString.contains("(" +idString+")"))     {
                continue;
            }
            locPoint = gatesTemp.getpSetLoc();
            gatePoint = gatesTemp.getGateByApNum(apNum);
            // 拉滚动条目标位置
            GameUtil.mouseMoveByPoint(locPoint);
            GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
            // 点击ap本
            GameUtil.mouseMoveByPoint(gatePoint);
            GameUtil.mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK);
            //添加hasdo属性
            Map<String, String> hasMap = new HashMap<>();
            if (i == size - 1 ) {
                hasMap.put("hasDo_" + acountNum + "_" + apNum, "");
            } else {
                if (hasDoString.contains(idString)){
                    hasMap.put("hasDo_" + acountNum + "_" + apNum,
                            hasDoString);
                } else {
                    hasMap.put("hasDo_" + acountNum + "_" + apNum,
                            hasDoString + acountNum + "_" + tempId + "_" + apNum);
                }
            }
            setHasMap(hasMap);
            break;
        }
    }

    @Override
    protected void fightOverMethod() {
        PropertiesUtil.setValueForspecialHasDo(getHasMap());
    }

    @Override
    public String getSpecialGatesFilePath() {
        return System.getProperty("user.dir") + "/config/special_all_train.json";
    }
}
