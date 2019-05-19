package aoshiScript.entity;

import java.awt.Color;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import commons.entity.NativeCp;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.melloware.jintellitype.JIntellitype;

import fgoScript.FgoPanel;
import fgoScript.constant.GameConstant;
import fgoScript.entity.PointColor;
import fgoScript.entity.ZButton;
import commons.util.GameUtil;
import commons.util.PropertiesUtil;

import javax.swing.*;

public class WuNa {
	private static final Logger LOGGER = LogManager.getLogger(WuNa.class);
	private static boolean GO = false;
	private String name;
	private int count ;
	private int clickCount ;
	private static WuNa wuna;
	public static WuNa instance() {
		if (wuna == null) {
			wuna = new WuNa("wuna");
			return wuna;
		}else {
			return wuna;
		}
	}
	public WuNa(String name) {
		super();
		this.name = name;
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isGO() {
		return GO;
	}
	public void setGO(boolean gO) {
		GO = gO;
	}
	
	public int getClickCount() {
		return clickCount;
	}
	public void setClickCount(int clickCount) {
		this.clickCount = clickCount;
	}
	public void alwaysClick() {
        if (isGO()) {
			setGO(false);
		} else{
			setGO(true);
		}
        if (isGO()){
            Robot rb = GameUtil.getRb(this.getClass().getName());
            String multiFactor = PropertiesUtil.getValueFromFileNameAndKey("multiFactor" , "changeButton_" + NativeCp.getUserName());
            int factor = 0;
            switch(multiFactor){
                case "0倍" : {
                    factor = 0;
                    break;
                }case "1倍" : {
                    factor = 1;
                    break;
                }
                case "2倍" : {
                    factor = 2;
                    break;
                }
                case "3倍" : {
                    factor = 3;
                    break;
                }
                default : {
                    factor = 0;
                    break;
                }
            }
            String startegy = PropertiesUtil.getValueFromFileNameAndKey("clickStrategy" , "changeButton_" + NativeCp.getUserName());
            if (startegy.equals("判断")) {
				String condiTion = PropertiesUtil.getValueFromAutoClickFile("condiTion");
				String action = PropertiesUtil.getValueFromAutoClickFile("action");
				condiTion = StringUtils.isBlank(condiTion) ? "" : condiTion.substring(0, condiTion.length()-1);
				action = StringUtils.isBlank(action) ? "" : action.substring(0, action.length()-1);
				String[] pcStr = condiTion.split(";");
				String[] acStr = action.split(";");
				int size = pcStr.length;
				int len = acStr.length;
				if (StringUtils.isBlank(condiTion)) {
					JOptionPane.showMessageDialog(null, "请点击设置按键！", "信息", JOptionPane.INFORMATION_MESSAGE);
					setGO(false);
				}else if(StringUtils.isBlank(action) || size!=len){
					JOptionPane.showMessageDialog(null, "按键不完整，请补全或重新设置！", "信息", JOptionPane.WARNING_MESSAGE);
					setGO(false);
				}else {
					int minSize = size < len ? size:len;
					if (size!=len) {
						LOGGER.info("配置不成对，忽略最后一次点击");
					}
					List<PointColor> pcList = new ArrayList<>();
					String pointStr;
					String colorStr;

					for (int i = 0; i < minSize; i++) {
						pointStr = pcStr[i].split("_")[0];
						colorStr = pcStr[i].split("_")[1];
						pcList.add(new PointColor(new Point(Integer.parseInt(pointStr.split(",")[0]),
								Integer.parseInt(pointStr.split(",")[1])),
								new Color(Integer.parseInt(colorStr.split(",")[0]),
										Integer.parseInt(colorStr.split(",")[1]),
										Integer.parseInt(colorStr.split(",")[2])), true));

					}
					List<Point> pList = new ArrayList<>();
					for (int i = 0; i < minSize; i++) {
						pList.add(new Point(Integer.parseInt(acStr[i].split(",")[0]),
								Integer.parseInt(acStr[i].split(",")[1])));
					}
					PointColor pointColor = null;
					Point p;
					Color c0;
					Color c1;
					boolean flag = true;
					boolean isEqual;
					String className = this.getClass().getName();
					do {
						for (int i = 0; i < minSize; i++) {
							pointColor = pcList.get(i);
							p = pointColor.getPoint();
							c0 = pointColor.getColor();
							isEqual = pointColor.isEqual();
							c1 = GameUtil.getScreenPixel(p,className);
							flag = GameUtil.likeEqualColor(c0, c1);
							if (!isEqual) {
								flag = !flag;
							}
							if (flag) {
								rb.mouseMove((int) pList.get(i).getX(), (int) pList.get(i).getY());
								mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
							}
							rb.delay(200 * factor);
						}
					} while (isGO());
				}
            }else {
                while (isGO()) {
                    rb.delay(200 * factor);
                    mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
                }
            }
        }

		
	}
	public void configClick(ZButton bt) {
		String text = bt.getText();
		if ("点击设置".equals(text)) {
			bt.setText("选择条件");
		} else if ("选择条件".equals(text)){
			selectConditionPoint();
			bt.setText("选择结果");
		} else if ("选择结果".equals(text)){
			selectActionPoint();
			bt.setText("选择条件");
		}
	}
	public void falshClick(ZButton bt) {
		ZButton[] bts = FgoPanel.instance().getBts();
		ZButton setbt =bts[bts.length-4];
		setbt.setEnabled(true);
		setbt.setText("点击设置");
		JIntellitype.getInstance().unregisterHotKey(setbt.getMarkCode());
		this.setClickCount(0);
		PropertiesUtil.deleteAutoClickFile();
	}
	private void selectConditionPoint() {
		Point p = GameUtil.getMousePosition();
		Color c = GameUtil.getScreenPixel(p);
		String condiTion = (int) p.getX()+"," + (int) p.getY()
		+ "_" + c.getRed() + "," +c.getGreen()	+ "," +c.getBlue()		
		+ ";";
		String temp = PropertiesUtil.getValueFromAutoClickFile("condiTion");
		condiTion = (StringUtils.isBlank(temp)?"":temp) + condiTion;
		Map<String, String> map = new HashMap<>();
		map.put("condiTion", condiTion);
		PropertiesUtil.setValueForAutoClick(map);
	}
	private void selectActionPoint() {
		Point p = GameUtil.getMousePosition();
		String action = (int) p.getX()+"," + (int) p.getY()
		+ ";";
		String temp = PropertiesUtil.getValueFromAutoClickFile("action");
		action = (StringUtils.isBlank(temp)?"":temp) + action;
		Map<String, String> map = new HashMap<>();
		map.put("action", action);
		PropertiesUtil.setValueForAutoClick(map);
	}
	private  void mousePressAndRelease(int key) {
		Robot rb = GameUtil.getRb(this.getClass().getName());
		rb.mousePress(key);
		rb.delay(GameConstant.DELAY/10);
		rb.mouseRelease(key);

	}
	public static void main(String[] args) {
	}
}
class Cts {
	public static Object ob = new Object();
	private static boolean flag = true;

	public static boolean isFlag() {
		return flag;
	}

	public static void setFlag(boolean flag) {
		Cts.flag = flag;
	}
	
}