package fgoScript.service;

import commons.util.GameUtil;
import commons.util.PropertiesUtil;
import fgoScript.constant.GameConstant;
import fgoScript.constant.PointInfo;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.util.List;
import java.util.*;

public class EventFactors {
	private static final PointInfo POINT_INFO = PointInfo.getSpringBean();

	public static final Point P_INSIDE = new Point(1218,564);
	public static final Color C_INSIDE = new Color(240, 167, 17);
	public static final Point P_OUTSIDE = new Point(1215,490);
	public static final Color C_OUTSIDE = new Color(247, 173, 14);
	public static final Point P_BBSIDE = new Point(1219,632);
	public static final Color C_BBSIDE = new Color(247, 166, 24);
	public static int INSIDE = 0;
	public static int OUTSIDE = 1;
	public static final int BBSIDE = 2;
	public static Point[] sidePoints= {P_INSIDE, P_OUTSIDE, P_BBSIDE};
	public static Color[] sideColors= {C_INSIDE, C_OUTSIDE, C_BBSIDE};
	/**
	 * 选择的面
	 */
	public static int SELECT_SIDE = BBSIDE;
	/**
	 * 拖拽点
	 */
	public static Point[][] dragPoints = {
//			{ new Point(972, 685), new Point(972, 182) },
//			{ new Point(972, 685), new Point(972, 182) },
//			{ new Point(215, 181), new Point(808, 181) },
//			{ new Point(972, 685), new Point(972, 182) }
	};
	/**
	 * 拖拽后目标点
	 *  new Point(456,436);
	 *  new Point(583,251);
	 * 	new Point(306,341);
	 *  new Point(717,183);//混沌 樱
	 *  new Point(680,449);//卡米尔
	 *  new Point(678,449);// 中心
	 *  new Point(348,391);
	 *  new Point(263,262);
	 *  new Point(344,557)脚
	 *  new Point(574,602)大河
	 *  new Point(242,545)屁股
	 *  new Point(154,419)里腿
	 */
	public static Point obPoint = new Point(678,449);
	
	/**
	 * 拖拽后选哪个副本
	 */
	public static Point p_room_select = POINT_INFO.getpRoomSelect01();
	/**
	 * 进入副本后支援的职介
	 */
	public static Point supportServant = POINT_INFO.getpServantFour();
	public static final int battleRounds = 3;
	/**
	 * 第一回事是否仍副宝具
	 * 
	 */
	public static final boolean ifNP = false;
	/**
	 * 第二回合是否允许主宝具释放
	 * 
	 */
	public static final boolean ifSecondNP = false;
	/**
	 * 连续技能Map null为自动选3
	 * @return
	 */
	public static List<Map<String, Object>> getPreSkills(Color[][] DEFAULT_SKILL_COLORS){
	
		List<Map<String, Object>> list = new LinkedList<>();
		Map<String, Object> tempMap;
		//位置3	
		int[] array03 = GameUtil.strToIntArray(PropertiesUtil.getValueFromSkillsFile("skills03"),true);
		tempMap = new HashMap<>();
		tempMap.put("from", 2);
		String toP = PropertiesUtil.getValueFromSkillsFile("person03");
		tempMap.put("to", StringUtils.isBlank(toP) ? 2 : Integer.valueOf(toP));
		tempMap.put("skills", array03);
		list.add(tempMap);
		int[] array02 = GameUtil.strToIntArray(PropertiesUtil.getValueFromSkillsFile("skills02"),true);
		tempMap = new HashMap<>();
		tempMap.put("from", 1);
		toP = PropertiesUtil.getValueFromSkillsFile("person02");
		tempMap.put("to", StringUtils.isBlank(toP) ? 1 : Integer.valueOf(toP));
		tempMap.put("skills", array02);
		list.add(tempMap);
		int[] array01 = GameUtil.strToIntArray(PropertiesUtil.getValueFromSkillsFile("skills01"),true);
		tempMap = new HashMap<>();
		tempMap.put("from", 0);
		toP = PropertiesUtil.getValueFromSkillsFile("person01");
		tempMap.put("to", StringUtils.isBlank(toP) ? 0 : Integer.valueOf(toP));
		tempMap.put("skills", array01);
		list.add(tempMap);
		
		setUsefulSkills(DEFAULT_SKILL_COLORS, list);
		return list;
	}

	public static List<Map<String, Object>> getQpPreSkills(Color[][] DEFAULT_SKILL_COLORS){
		List<Map<String, Object>> list = new LinkedList<>();
		Map<String, Object> tempMap;
		//位置3
		tempMap = new HashMap<>();
		int[] array03 = {0,1};
		tempMap.put("from", 2);
		tempMap.put("to", 2);
		tempMap.put("skills", array03);
		list.add(tempMap);

		tempMap = new HashMap<>();
		int[] array01 = {};
		tempMap.put("from", 1);
		tempMap.put("to", 1);
		tempMap.put("skills", array01);
		list.add(tempMap);

		tempMap = new HashMap<>();
		int[] array02 = {};
		tempMap.put("from", 0);
		tempMap.put("to", 0);
		tempMap.put("skills", array02);
		list.add(tempMap);

		setUsefulSkills(DEFAULT_SKILL_COLORS, list);
		return list;
	}
	public static List<Map<String, Object>> getExpPreSkills(Color[][] DEFAULT_SKILL_COLORS){
		List<Map<String, Object>> list = new LinkedList<>();
		Map<String, Object> tempMap;
		//位置3
		tempMap = new HashMap<>();
		int[] array03 = {0};
		tempMap.put("from", 2);
		tempMap.put("to", 2);
		tempMap.put("skills", array03);
		list.add(tempMap);
		
		tempMap = new HashMap<>();
		int[] array01 = {};
		tempMap.put("from", 1);
		tempMap.put("to", 1);
		tempMap.put("skills", array01);
		list.add(tempMap);
		
		tempMap = new HashMap<>();
		int[] array02 = {};
		tempMap.put("from", 0);
		tempMap.put("to", 0);
		tempMap.put("skills", array02);
		list.add(tempMap);
		
		setUsefulSkills(DEFAULT_SKILL_COLORS, list);
		return list;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static Color[][] getDefaultSkillColorsFromFile(){
		Color[][] fileColors = new Color[3][3];
		String skillColors = PropertiesUtil.getValueFromColorFile("skillColors");
		String[] roles = skillColors.split(";");
		String[] skills;
		String[] colors;
		Color temp;
		int roleSize = roles.length;
		int skillSize;
		for (int i = 0; i < roleSize; i++) {
			skills = roles[i].split("-");
			skillSize = skills.length;
			for (int j = 0; j < skillSize; j++) {
				colors = skills[j].split("_");
				temp = new Color(Integer.parseInt(colors[0])
						, Integer.parseInt(colors[1])
						, Integer.parseInt(colors[2])
					);
				fileColors[i][j]=temp;
			}
		}
		return fileColors;
	}
	private static void setUsefulSkills(Color[][] DEFAULT_SKILL_COLORS, List<Map<String, Object>> list) {
 		Color[][] NEW_SKILL_COLORS = getSkillColors();
		Color defaultTemp;
		Color newTemp;
		List<Integer> newSkillsList;
		int[] newSkills;
		int size = list.size();
		for (int j = 0; j < size; j++) {
			Map<String, Object> map = list.get(j);
			int[] a =(int[]) map.get("skills");
			newSkillsList= new LinkedList<>();
			int num = a.length;
			for (int i = 0; i < num; i++) {
				defaultTemp = DEFAULT_SKILL_COLORS[(int) map.get("from")][a[i]];
				newTemp = NEW_SKILL_COLORS[(int) map.get("from")][a[i]];
				if (GameUtil.likeEqualColor(defaultTemp, newTemp)
						||GameUtil.isLargerColor(newTemp, defaultTemp)) {
					newSkillsList.add(a[i]);
				}
			}
			int num02 = newSkillsList.size();
			newSkills = new int[num02];
			for (int i = 0; i < num02; i++) {
				newSkills[i] = newSkillsList.get(i);
			}
			map.put("skills", newSkills);
		}
	}
	/**
	 * 2回合，第1轮技能
	 */
	public static List<Map<String, Object>> getRound2ClothSkill(){
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> tempMap;
		
		tempMap = new HashMap<>();
		int[] array01 = {0};
		tempMap.put("to", 0);
		tempMap.put("skills", array01);
		list.add(tempMap);
		return list;
	}
	/**
	 * 2回合，第1轮技能
	 */
	public static List<Map<String, Object>> getRound2ClothSkillto3(){
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> tempMap;

		tempMap = new HashMap<>();
		int[] array01 = {0};
		tempMap.put("to", 2);
		tempMap.put("skills", array01);
		list.add(tempMap);
		return list;
	}


	/**
	 * 宝具技能Map01
	 * @return
	 */
	public static List<Map<String, Object>> getNPSkills01(Color[][] DEFAULT_SKILL_COLORS){
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> tempMap;
		
		tempMap = new HashMap<>();
		int[] array01 = {0,1,2};
		tempMap.put("from", 0);
		tempMap.put("to", 0);
		tempMap.put("skills", array01);
		list.add(tempMap);
		
		tempMap = new HashMap<>();
		int[] array02 = {0,1,2};
		tempMap.put("from", 2);
		tempMap.put("to", 0);
		tempMap.put("skills", array02);
		list.add(tempMap);
		
		setUsefulSkills(DEFAULT_SKILL_COLORS, list);
		
		return list;
	}
	/**
	 * 宝具技能Map03
	 * @return
	 */
	public static List<Map<String, Object>> getNPSkills03(Color[][] DEFAULT_SKILL_COLORS){
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> tempMap;
		
		tempMap = new HashMap<>();
		int[] array01 = {0,1,2};
		tempMap.put("from", 2);
		tempMap.put("to", 2);
		tempMap.put("skills", array01);
		list.add(tempMap);
		
		setUsefulSkills(DEFAULT_SKILL_COLORS, list);
		return list;
	}
	/**
	 * 宝具技能Map02
	 * @return
	 */
	public static List<Map<String, Object>> getNPSkills02(Color[][] DEFAULT_SKILL_COLORS){
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> tempMap;

		tempMap = new HashMap<>();
		int[] array01 = {0,1,2};
		tempMap.put("from", 1);
		tempMap.put("to", 1);
		tempMap.put("skills", array01);
		list.add(tempMap);

		setUsefulSkills(DEFAULT_SKILL_COLORS, list);
		return list;
	}
	public static void writeDefaultSkillColors(Color[][] colors) {
		StringBuilder colorStrs = new StringBuilder();
		Color[] tempColors;
		Color tempColor;
		int size = colors.length;
		int len;
		for (int i = 0; i < size; i++) {
			tempColors = colors[i];
			len = colors[i].length;
			for (int j = 0; j < len; j++) {
				tempColor = tempColors[j];
				colorStrs.append(tempColor.getRed()).append("_").append(tempColor.getGreen()).append("_").append(tempColor.getBlue()).append("-");
			}
			colorStrs.append(";");
		}
		Map<String, String> maps = new HashMap<>();
		maps.put("skillColors", colorStrs.toString());
		PropertiesUtil.setValue(maps);
	}
	public static Color[][] getSkillColors() {
		Point temp = null;
		Color[][] DEFAULT_SKILL_COLORS= new Color[3][3];
		int size = DEFAULT_SKILL_COLORS.length;
		for (int i = 0; i < size; i++) {
			Color[] tempColors = DEFAULT_SKILL_COLORS[i];
			int num = tempColors.length;
			for (int j = 0; j < num; j++) {
				switch (j) {
					case 0:{
						temp = POINT_INFO.getpSkill01();
						break;
					}
					case 1:{
						temp = POINT_INFO.getpSkill02();
						break;
					}
					case 2:{
						temp = POINT_INFO.getpSkill03();
						break;
					}
					default:{
						break;
					}
				}
				DEFAULT_SKILL_COLORS[i][j]= GameUtil.getScreenPixel(new Point(
								  (int) temp.getX() + GameConstant.HEAD_SPACE*i
								, (int) temp.getY()
								));
			}
		}
		return DEFAULT_SKILL_COLORS;
	}

	public static Point getMonsterPoint() {
		Point p =null;
		String toM = PropertiesUtil.getValueFromSkillsFile("monster03");
		switch (StringUtils.isBlank(toM) ? 2 : Integer.valueOf(toM)) {
		case 0:
			p = POINT_INFO.getpMoster01();
			break;
		case 1:
			p = POINT_INFO.getpMoster02();
			break;
		case 2:
			p = null;
			break;
		default:
			break;
		}
		return p;
	}
}
