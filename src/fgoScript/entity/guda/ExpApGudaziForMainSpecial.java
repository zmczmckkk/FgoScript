package fgoScript.entity.guda;

import fgoScript.constant.GameConstant;
import fgoScript.constant.PointInfo;
import fgoScript.entity.Gates;
import fgoScript.entity.GatesInfo;
import commons.util.GameUtil;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class ExpApGudaziForMainSpecial extends ExpApGudazi{
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
    protected void fightOverMethod() {
        super.fightOverMethod();
    }

    public void insertIntoExpRoom(int apNum) throws Exception {
        GameUtil.mouseMoveByPoint(PointInfo.P_SCROLL_REST_DOWN);
        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        GameUtil.mouseMoveByPoint(PointInfo.P_SCROLL_REST_TOP);
        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        // 进入训练场
        // 周回进去
        GameUtil.mouseMoveByPoint(PointInfo.getP_WEEK_ENTRANCE());
        GameUtil.mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK);

        GameUtil.delay(GameConstant.DELAY*5);

        GameUtil.mouseMoveByPoint(PointInfo.P_SCROLL_REST_TOP);
        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        GameUtil.mouseMoveByPoint(PointInfo.P_SCROLL_REST_DOWN);
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
            setGatesDirPath(System.getProperty("user.dir") + "/config/special_all_exp.json");
        }
        return getGatesDirPath();
    }
}
