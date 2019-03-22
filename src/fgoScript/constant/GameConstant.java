package fgoScript.constant;

import fgoScript.util.PropertiesUtil;

public class GameConstant {
	public static final int DELAY = Integer.parseInt(PropertiesUtil.getValueFromConfig("DELAY"));// 延时
	public static final int THRESHOLD = 220;// 阈值
	public static final int MOVE_STEP = 100;// 阈值
	public static final int MOVE_TIME = DELAY / 3;// 滑动延时
	public static final int CARD_SPACE = 257;// 卡片间隔
	public static final int HEAD_SPACE = 317;// 头像间隔
	public static final int NP_SPACE = 230;// 宝具间隔
	public static final int ACTIVE_STYLE = 1;// 
	public static final int EXCUTE_STYLE = 2;// 
	public static final String IMG_EXTEND = "jpg";
	public static final String RED="RED";
	public static final String GREEN="GREEN";
	public static final String BLUE="BLUE";
	public static final boolean IF_SET_PRE = Boolean.parseBoolean(PropertiesUtil.getValueFromConfig("IF_SET_PRE"));
	public static final int APPLE_COUNT = Integer.parseInt(PropertiesUtil.getValueFromConfig("APPLE_COUNT"));
	
	

	/*
	 * 职介类型
	 */
	public static int CLASSIFY_ALL = 0;
	public static int CLASSIFY_SABER = 1;
	public static int CLASSIFY_ARCHER= 2;
	public static int CLASSIFY_LANCER = 3;
	public static int CLASSIFY_RIDER = 4;
	public static int CLASSIFY_CASTER = 5;
	public static int CLASSIFY_ASSASIN = 6;
	public static int CLASSIFY_BASAKER = 7;
	public static int CLASSIFY_FOUR = 8;
	/*
	 * 房间类型
	 */
	public static int CLASSIFY_TRAIN_ROOM = 10;
	public static int CLASSIFY_QP_ROOM = 11;
	/**
	 * 允许误差
	 */
	public static int CORRECT = 11;
}