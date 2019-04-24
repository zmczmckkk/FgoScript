package fgoScript.constant;

import fgoScript.util.PropertiesUtil;

public class GameConstant {
	public static final int DELAY = Integer.parseInt(PropertiesUtil.getValueFromConfig("DELAY"));// 延时
	public static final int THRESHOLD = 220;// 阈值
	public static final int MOVE_STEP = 100;// 阈值
	public static final int MOVE_TIME = DELAY / 3;// 滑动延时
//	public static final int CARD_SPACE = 257;// 卡片间隔
	public static final int HEAD_SPACE = 317;// 头像间隔
	public static final int NP_SPACE = 230;// 宝具间隔
	public static final int ACTIVE_STYLE = 1;// 
	public static final int EXCUTE_STYLE = 2;// 
	public static final String IMG_EXTEND = "jpg";
	public static final int RED= 2;
	public static final int GREEN= 1;
	public static final int BLUE= 0;
	public static final boolean IF_SET_PRE = Boolean.parseBoolean(PropertiesUtil.getValueFromConfig("IF_SET_PRE"));
	public static final int APPLE_COUNT = Integer.parseInt(PropertiesUtil.getValueFromConfig("APPLE_COUNT"));
	public static final String NO_SKILL="0";
	public static final String GO_SECOD_SKILL_FOR_FIRST="1";
	public static final String GO_ALL_SKILL_FOR_FIRST="2";
	public static final String ONLY_CLICK="0";
	public static final String SCRIPT_CLICK="1";


	/*
	 * 职介类型
	 */
	public static final int CLASSIFY_ALL = 0;
	public static final int CLASSIFY_SABER = 1;
	public static final int CLASSIFY_ARCHER= 2;
	public static final int CLASSIFY_LANCER = 3;
	public static final int CLASSIFY_RIDER = 4;
	public static final int CLASSIFY_CASTER = 5;
	public static final int CLASSIFY_ASSASIN = 6;
	public static final int CLASSIFY_BASAKER = 7;
	public static int CLASSIFY_FOUR = 8;
}
