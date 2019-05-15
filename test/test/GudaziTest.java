package test;

import com.alibaba.fastjson.JSONObject;
import fgoScript.entity.Gudazi;
import fgoScript.entity.PointColor;
import commons.util.ClipBoardUtil;
import org.junit.Test;

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