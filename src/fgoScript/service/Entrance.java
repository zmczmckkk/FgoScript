package fgoScript.service;

import fgoScript.entity.panel.FgoFrame;

/**
 * @description: 程序main函数执行入口
 * @author: RENZHEHAO
 * @create: 2019-05-22 04:06
 **/
public class Entrance {
    public static void main(String[] args) {
        FgoFrame fp = FgoFrame.instance();
        fp.setVisible(true);
        // 启动定时任务
        new TimerManager();
    }

}
