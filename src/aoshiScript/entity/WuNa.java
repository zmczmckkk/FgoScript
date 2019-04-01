package aoshiScript.entity;

import java.awt.Color;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.melloware.jintellitype.JIntellitype;

import fgoScript.FgoPanel;
import fgoScript.constant.GameConstant;
import fgoScript.entity.PointColor;
import fgoScript.entity.ZButton;
import fgoScript.util.GameUtil;
import fgoScript.util.PropertiesUtil;

public class WuNa {
	private static final Logger LOGGER = LogManager.getLogger(WuNa.class);
	private static boolean GO = true;
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
		if (count++ % 2 == 0) {
			setGO(true);
		} else{
			setGO(false);
		}
		String condiTion = PropertiesUtil.getValueFromAutoClickFile("condiTion");
		String startegy = PropertiesUtil.getValueFromFileNameAndKey("clickStrategy" , "changeButton");
		if (StringUtils.isNotBlank(condiTion) && startegy.equals("判断")) {
			condiTion = condiTion.substring(0, condiTion.length()-1);
			String action = PropertiesUtil.getValueFromAutoClickFile("action");
			action = action.substring(0, action.length()-1);
			String[] pcStr = condiTion.split(";");
			String[] acStr = action.split(";");
			int size = pcStr.length;
			int len = acStr.length;
			if (size!=len) {
				LOGGER.info("配置不成对，请重新配置！");
			}
			List<PointColor> pcList = new ArrayList<>();
			String pointStr;
			String colorStr;
			
			for (int i = 0; i < size; i++) {
				pointStr = pcStr[i].split("_")[0];
				colorStr = pcStr[i].split("_")[1];
				pcList.add(new PointColor(new Point(Integer.parseInt(pointStr.split(",")[0]),
									Integer.parseInt(pointStr.split(",")[1])),
									new Color(Integer.parseInt(colorStr.split(",")[0]),
											Integer.parseInt(colorStr.split(",")[1]), 
											Integer.parseInt(colorStr.split(",")[2])), true));
				
			}
			List<Point> pList = new ArrayList<>();
			for (int i = 0; i < size; i++) {
				pList.add(new Point(Integer.parseInt(acStr[i].split(",")[0]),
						Integer.parseInt(acStr[i].split(",")[1])));
			}
			PointColor pointColor = null;
			Point p;
			Color c0;
			Color c1;
			boolean flag = true;
			boolean isEqual;
			Robot rb = GameUtil.getRb(this.getClass().getName());
			String className = this.getClass().getName();
			do {
 				for (int i = 0; i < size; i++) {
					pointColor = pcList.get(i);
					p = pointColor.getPoint();
					c0 = pointColor.getColor();
					isEqual = pointColor.isEqual();
					c1 = GameUtil.getScreenPixel(p,className);
					flag = GameUtil.isEqualColor(c0, c1);
					if (!isEqual) {
						flag = !flag;
					}
					if (flag) {
						rb.mouseMove((int) pList.get(i).getX(), (int) pList.get(i).getY());
						mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
					}
				}
			} while (isGO());
		}else {
			while (isGO()) {
				mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
			}
		}
		
		
	}
	public void configClick(ZButton bt) {
		if (clickCount++ %2 == 0) {
			selectConditionPoint();
			bt.setText("选择结果");
		}else {
			selectActionPoint();
			bt.setText("选择条件");
		}
	}
	public void falshClick(ZButton bt) {
		ZButton[] bts = FgoPanel.instance().getBts();
		ZButton setbt =bts[bts.length-3];
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
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (Cts.isFlag()) {
					System.out.println("我进去了~啊，啊~~");
					synchronized (Cts.ob) {
						try {
							Cts.ob.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					System.out.println("我出来了，你好棒，射的好满足啊");
				}
			}
		}).start();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					String str;
					for (int i = 0; i < 3; i++) {
						str = "我";
						for (int j = 0; j < i; j++) {
							str+=" "; 
						}
						str+="插~";
						System.out.println(str);
						Thread.sleep(1000);
					}
					synchronized (Cts.ob) {
						Cts.ob.notify();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}).start();
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