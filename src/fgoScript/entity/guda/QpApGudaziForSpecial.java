package fgoScript.entity.guda;

import fgoScript.constant.GameConstant;
import fgoScript.constant.PointInfo;
import fgoScript.entity.CardComparator;
import fgoScript.entity.CommonCard;
import fgoScript.entity.PointColor;
import fgoScript.service.EventFactors;
import fgoScript.util.GameUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QpApGudaziForSpecial extends QpApGudazi{
    @Override
    public void intoAndSelect(int apNum, int acountNum) throws Exception {
        ExpApGudaziForMainSpecial expGuda = new ExpApGudaziForMainSpecial();
        expGuda.setGatesDirPath(System.getProperty("user.dir") + "/config/special_all_qp.json");
        expGuda.insertIntoExpRoom(apNum);
    }
}
