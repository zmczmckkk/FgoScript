package aoshiScript.entity;

import java.awt.Color;
import java.awt.Point;
import java.awt.Robot;
import java.util.*;

import commons.entity.NativeCp;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.melloware.jintellitype.JIntellitype;

import fgoScript.entity.panel.FgoFrame;
import fgoScript.constant.GameConstant;
import fgoScript.entity.PointColor;
import fgoScript.entity.BaseZButton;
import commons.util.GameUtil;
import commons.util.PropertiesUtil;

import javax.print.attribute.standard.Finishings;
import javax.swing.*;

public class WuNa implements IWuNa{
	private static final Logger LOGGER = LogManager.getLogger(WuNa.class);
	private static boolean GO = false;
	boolean scucess = true;

	@Override
	public boolean isScucess() {
		return scucess;
	}

	@Override
	public void setScucess(boolean scucess) {
		this.scucess = scucess;
	}

	private String name;
	private int count ;
	private int clickCount ;
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
	@Override
	public boolean isGO() {
		return GO;
	}
	@Override
	public void setGO(boolean go) {
		GO = go;
	}
	
	public int getClickCount() {
		return clickCount;
	}
	public void setClickCount(int clickCount) {
		this.clickCount = clickCount;
	}
	@Override
	public void alwaysClick() {
        if (isGO()) {
			setGO(false);
		} else{
			setGO(true);
		}
        if (isGO()){
            Robot rb = GameUtil.getRb(this.getClass().getName());
            int factor = getFactor();
            String startegy = PropertiesUtil.getValueFromFileNameAndKey("clickStrategy" , "changeButton_" + NativeCp.getUserName());
            if (startegy.equals("判断")) {
				alwaysClickForStrategy("clicks", null, false);
            }else {
                while (isGO()) {
                    rb.delay(600 * factor);
                    GameUtil.mousePressAndReleaseByDD();
                }
            }
        }

	}
	private int getFactor(){
		String multiFactor = PropertiesUtil.getValueFromFileNameAndKey("multiFactor" , "changeButton_" + NativeCp.getUserName());
		int factor = Integer.parseInt(multiFactor.substring(0,multiFactor.indexOf("倍")));
		return factor;
	}
	@Override
	public void alwaysClickForStrategy(String fileName,Integer factor,boolean alwaysGo) {
		// 初始化参数
		setScucess(true);
		setGO(true);
		Robot rb = GameUtil.getRb(this.getClass().getName());
		String condiTion = PropertiesUtil.getValueFromAutoClickFile("condiTion", fileName);
		String action = PropertiesUtil.getValueFromAutoClickFile("action", fileName);
		condiTion = StringUtils.isBlank(condiTion) ? "" : condiTion.substring(0, condiTion.length()-1);
		action = StringUtils.isBlank(action) ? "" : action.substring(0, action.length()-1);
		String[] pcStr = condiTion.split(";");
		String[] acStr = action.split(";");
		int size = pcStr.length;
		int len = acStr.length;
		if (StringUtils.isBlank(condiTion)) {
			JOptionPane.showMessageDialog(null, "请点击设置按键！", "信息", JOptionPane.INFORMATION_MESSAGE);
			setGO(false);
			setScucess(false);
		}else if(StringUtils.isBlank(action) || size!=len){
			JOptionPane.showMessageDialog(null, "按键不完整，请补全或重新设置！", "信息", JOptionPane.WARNING_MESSAGE);
			setGO(false);
			setScucess(false);
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
			Random r=new Random();
			int ri;
			do {
				for (int i = 0; i < minSize; i++) {
					ri = r.nextInt(minSize);
					System.out.println(Thread.currentThread().getName() + " : scanning! " + i);
					pointColor = pcList.get(ri);
					p = pointColor.getPoint();
					c0 = pointColor.getColor();
					isEqual = pointColor.isEqual();
					c1 = GameUtil.getScreenPixel(p,className);
					flag = GameUtil.likeEqualColor(c0, c1, 10);
					if (!isEqual) {
						flag = !flag;
					}
					if (flag) {
						rb.mouseMove((int) pList.get(ri).getX(), (int) pList.get(ri).getY());
						GameUtil.mousePressAndReleaseByDD();
					}
					rb.delay(factor == null ? 200 * getFactor() : factor);
				}
			} while (isGO() || alwaysGo);
		}
		System.out.println("finish auto click sacnning");
	}

	@Override
	public void configClick(BaseZButton bt) {
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
	@Override
	public void falshClick(BaseZButton bt) {
		BaseZButton[] bts = FgoFrame.instance().getBts();
		BaseZButton setbt =bts[bts.length-4];
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