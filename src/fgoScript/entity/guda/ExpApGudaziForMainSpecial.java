package fgoScript.entity.guda;

import commons.entity.Constant;
import fgoScript.constant.GameConstant;
import fgoScript.constant.PointInfo;
import fgoScript.entity.Gates;
import fgoScript.entity.GatesInfo;
import commons.util.GameUtil;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class ExpApGudaziForMainSpecial extends ExpApGudazi{
    private static final PointInfo POINT_INFO = PointInfo.getSpringBean();

    private String gatesDirPath;

    public String getGatesDirPath() {
        return gatesDirPath;
    }

    public void setGatesDirPath(String gatesDirPath) {
        this.gatesDirPath = gatesDirPath;
    }

    @Override
    public void intoAndSelect(int apNum, int acountNum) throws Exception {
        insertIntoExpRoom(apNum);
    }

    @Override
    public Point getSuppotServant() {
        return super.getSuppotServant();
    }

    @Override
    public void insertIntoExpRoom(int apNum) throws Exception {
        GameUtil.mouseMoveByPoint(POINT_INFO.getpScrollRestDown());
        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        GameUtil.mouseMoveByPoint(POINT_INFO.getpScrollRestTop());
        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        // 进入训练场
        // 周回进去
        GameUtil.mouseMoveByPoint(POINT_INFO.getP_WEEK_ENTRANCE());
        GameUtil.mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK);

        GameUtil.delay(GameConstant.DELAY*5);

        GameUtil.mouseMoveByPoint(POINT_INFO.getpScrollRestTop());
        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        GameUtil.mouseMoveByPoint(POINT_INFO.getpScrollRestDown());
        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        // 拖拽画面
        Point start = POINT_INFO.getpDailySliceStrat();
        Point end = POINT_INFO.getpDailySliceEnd();
        moveBySteps(start, end);

        // 点击日常
        Point p6 = POINT_INFO.getpDailyEntrance();
        GameUtil.mouseMoveByPoint(p6);
        GameUtil.mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK);
        //获取入口信息
        GatesInfo gi = getSpecialGatesInfo();
        // 拉滚动条至最上
        GameUtil.mouseMoveByPoint(gi.getSliceTopPoint());
        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
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
            locPoint = gatesTemp.getpSetLoc();
            gatePoint = gatesTemp.getGateByApNum(apNum);
            // 拉滚动条目标位置
            GameUtil.mouseMoveByPoint(locPoint);
            GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
            // 点击ap本
            GameUtil.mouseMoveByPoint(gatePoint);
            GameUtil.mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK);
        }
    }

    @Override
    public String getSpecialGatesFilePath() {
        if (gatesDirPath == null) {
            setGatesDirPath(System.getProperty("user.dir") + "/config/"+ Constant.FGO +"/special_all_exp.json");
        }
        return getGatesDirPath();
    }
}
