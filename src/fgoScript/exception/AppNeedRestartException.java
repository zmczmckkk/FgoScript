package fgoScript.exception;

import commons.util.GameUtil;
import fgoScript.constant.GameConstant;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppNeedRestartException extends Exception {
	private static final long serialVersionUID = 1877932104145533378L;

	@Override
	public String getMessage() {
		GameUtil.fullScreen2file(GameConstant.IMG_EXTEND, GameUtil.getPreFix() + "\\ExceptionPic\\" + System.currentTimeMillis() + ".");
		return "重启模拟器，重新执行脚本！";
	}

	public static void main(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		GameUtil.fullScreen2file(GameConstant.IMG_EXTEND, GameUtil.getPreFix() + "\\ExceptionPic\\" + sdf.format(new Date()) + ".");
	}
}
