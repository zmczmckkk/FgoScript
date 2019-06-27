package fgoScript.service;

import fgoScript.constant.PointInfo;
import fgoScript.entity.PointColor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CommonMethods {
	private static final PointInfo POINT_INFO = PointInfo.getSpringBean();

	public static PointColor open2GudaOrRestart() throws Exception {
        // 公告×点
		Point p_notice_exit = POINT_INFO.getpNoticeExit();
		Color c_notice_exit = POINT_INFO.getcNoticeExit();
		// 公告×点
		Point p_notice_exit_dark = POINT_INFO.getpNoticeExitDark();
		Color c_notice_exit_dark = POINT_INFO.getcNoticeExitDark();
		// 盲点
		Point dead_point = POINT_INFO.getDeadPoint();
		// 咕哒子
		Point p_guda = POINT_INFO.getpGuda();
		Color c_guda = POINT_INFO.getcGuda();
		// 引继页面
		Point p_transfer = new Point(911, 670);// 颜色：0;60;165
		Color c_transfer = new Color(0, 60, 165);
		// 再开按钮（是）
		Point p_button_restart_yes = POINT_INFO.getpButtonRestartYes();
		Color c_button_restart_yes = POINT_INFO.getcButtonRestartYes();

		List<PointColor> pcList = new ArrayList<>();
		pcList.add(new PointColor(p_guda, c_guda, dead_point, true));
		pcList.add(new PointColor(p_notice_exit, c_notice_exit, p_notice_exit, true));
		pcList.add(new PointColor(p_notice_exit_dark, c_notice_exit_dark, p_notice_exit_dark, true));
		pcList.add(new PointColor(p_transfer, c_transfer, POINT_INFO.getDeadPoint(), true));
		pcList.add(new PointColor(p_button_restart_yes, c_button_restart_yes, p_button_restart_yes, true));
		List<PointColor> finishPCList = new ArrayList<>();
		finishPCList.add(new PointColor(p_guda, c_guda, dead_point, true));
		PointColor pcRestart = new PointColor(p_button_restart_yes, c_button_restart_yes, p_button_restart_yes, true);
		pcRestart.setConfirm(true);
		finishPCList.add(pcRestart);

		AutoAct ac = new AutoAct(pcList, finishPCList) {
			@Override
			public void doSomeThing() {
			}
		};
		ac.autoAct();
		return ac.getPcWait();
    }

}
