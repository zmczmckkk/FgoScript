package test;

import fgoScript.entity.panel.FgoFrame;
import fgoScript.entity.BaseZButton;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class FgoFrameTest {
    @Test
    public void myTest() {
        BaseZButton[] bts =  FgoFrame.instance().getBts();
        int size = bts.length;
        List<BaseZButton> ztList = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            ztList.add(bts[i]);
        }
        for (BaseZButton zbt : ztList) {
            System.out.println(zbt.getText());
        }
        ztList.remove(3);
    }
}