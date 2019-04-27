package test;

import com.alibaba.fastjson.JSONObject;
import fgoScript.entity.Gudazi;
import fgoScript.entity.PointColor;
import fgoScript.util.ClipBoardUtil;
import org.apache.logging.log4j.core.util.JsonUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class GudaziTest {

    @Test
    public void showPositionAndColor() {
        new Gudazi().showPositionAndColor();
        String text = ClipBoardUtil.getSysClipboardText();
        PointColor pc = JSONObject.parseObject(text, PointColor.class);
        System.out.println(pc);
    }

    @Test
    public void moveToPositionByClipBoard() {
        try {
            new Gudazi().moveToPositionByClipBoard();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}