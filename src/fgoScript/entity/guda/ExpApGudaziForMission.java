package fgoScript.entity.guda;

import fgoScript.constant.PointInfo;

import java.awt.*;

public class ExpApGudaziForMission extends QpApGudazi {
    @Override
    public void intoAndSelect(int apNum, int acountNum) throws Exception {
        new ExpApGudazi().intoAndSelect(apNum,0);
    }
}
