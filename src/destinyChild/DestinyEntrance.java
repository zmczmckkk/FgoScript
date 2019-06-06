package destinyChild;

import com.alibaba.fastjson.JSON;
import commons.util.ClipBoardUtil;
import commons.util.GameUtil;
import destinyChild.entity.GuestSave;

import java.awt.*;
import java.awt.event.KeyEvent;

public class DestinyEntrance {
    private boolean flag = true;
    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    public void getAllGuestSaves(){
        //线程1（自动按键）
        //线程2
        do {
            String path = System.getProperty("user.dir") + "/config/GuestSave.json";
            String jsonString = GameUtil.getJsonString(path);
            GuestSave gs = JSON.parseObject(jsonString, GuestSave.class);
            Color tempColor = GameUtil.getScreenPixel(gs.getFileManagePoint());
            if (GameUtil.likeEqualColor(tempColor, gs.getFileManageColor(), 0)) {
                // 点击files 2次
                for (int i = 0; i < 2; i++) {
                    GameUtil.mouseMoveByPoint(gs.getFilesPoint());
                    GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
                }
                // 长按save文件
                GameUtil.mouseMoveByPoint(gs.getSaveFilePoint());
                GameUtil.mousePressAndReleaseForLongTime(KeyEvent.BUTTON1_DOWN_MASK,3000);
                // 点击重命名
                GameUtil.mouseMoveByPoint(gs.getRenamePoint());
                GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
                // 等待1秒
                GameUtil.delay(1000);
                // 获取时间戳，并将时间戳复制到剪贴板上。
                long time =System.currentTimeMillis();
                ClipBoardUtil.setSysClipboardText("guest_info.sav" + time);
                // 黏贴时间戳
                GameUtil.ctrlV();
                // 点击确定按钮修改
                GameUtil.mouseMoveByPoint(gs.getSaveYesPoint());
                GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
                // 等待1秒
                GameUtil.delay(1000);
                // 关闭destiny
                GameUtil.mouseMoveByPoint(gs.getCloseDestinyPoint());
                GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
                // 等待1秒
                GameUtil.delay(1000);
                // 点击首页
                GameUtil.mouseMoveByPoint(gs.getHomePagePoint());
                GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
            }
            try {
                waitForDoSomething();
            } catch (InterruptedException e) {
                //如果有INter异常，终止循环。
                break;
            }
        } while(isFlag());
    }
    public void getPicsAndSavesForSummon(){
        //自动按键脚本1开启，开一个线程
        //等待邮箱红色提醒
        //自动按键脚本2开启，
        //等待

    }
    private void waitForDoSomething() throws InterruptedException {
        Thread.sleep(3000);
    }
}
