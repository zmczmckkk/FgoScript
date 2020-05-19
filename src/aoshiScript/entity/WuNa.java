package aoshiScript.entity;

import java.awt.Color;
import java.awt.Point;
import java.awt.Robot;
import java.util.*;

import commons.entity.Constant;
import commons.entity.NativeCp;
import fgoScript.exception.AppNeedRestartException;
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

import javax.swing.*;

public class WuNa implements IWuNa{
	private static final Logger LOGGER = LogManager.getLogger(WuNa.class);
	private boolean GO = false;
	private boolean forceStop = false;
	private Long lastClickTime;
	boolean scucess = true;
	boolean ifThrowException = false;

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
	public void setLastClickTime(Long lastClickTime) {
		this.lastClickTime = lastClickTime;
	}

	@Override
	public Long getLastClickTime() {
		return lastClickTime;
	}

	@Override
	public boolean isGO() {
		return GO;
	}
	@Override
	public void setGO(boolean go) {
		GO = go;
	}

	@Override
	public boolean isForceStop() {
		return forceStop;
	}

	@Override
	public void setForceStop(boolean forceStop) {
		this.forceStop = forceStop;
	}

	public int getClickCount() {
		return clickCount;
	}
	public void setClickCount(int clickCount) {
		this.clickCount = clickCount;
	}
	@Override
	public void alwaysClick() {
		setForceStop(false);
        if (isGO()) {
			setGO(false);
		} else{
			setGO(true);
		}
        if (isGO()){
            Robot rb = GameUtil.getRb(this.getClass().getName());
            int factor = getFactor();
            String startegy = PropertiesUtil.getValueFromFileNameAndKey("clickStrategy" , "changeButton_" + NativeCp.getUserName(), "");
            if (startegy.equals("判断")) {
				try {
					alwaysClickForStrategy("clicks", null, false, false, "");
				} catch (AppNeedRestartException e) {
					e.printStackTrace();
				}
			}else if (startegy.equals("按住")) {
					GameUtil.dd.HIVM_BTN(1);
            }else {
				while (isGO()) {
					rb.delay(600 * factor);
					GameUtil.mousePressAndReleaseByDD();
				}
			}
        }

	}
	private int getFactor(){
		String multiFactor = PropertiesUtil.getValueFromFileNameAndKey("multiFactor" , "changeButton_" + NativeCp.getUserName(),"");
		int factor = Integer.parseInt(multiFactor.trim());
		return factor;
	}
	@Override
	public void alwaysClickForStrategy(String fileName,Integer factor,boolean alwaysGo, boolean clickWait, String relativePath) throws AppNeedRestartException {
		// 初始化参数
		setScucess(true);
		setGO(true);
		setLastClickTime(System.currentTimeMillis());
		Robot rb = GameUtil.getRb(this.getClass().getName());
		String condiTion = PropertiesUtil.getValueFromAutoClickFile("condiTion", fileName, relativePath);
		String action = PropertiesUtil.getValueFromAutoClickFile("action", fileName, relativePath);
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
//				LOGGER.info("配置不成对，忽略最后一次点击");
			}
			List<PointColor> pcList = new ArrayList<>();
			List<Integer> onceList = new ArrayList<>();
			String pointStr;
			String colorStr;
			String onceStr;
			int conditionSize;
			String[] tempArray;

			for (int i = 0; i < minSize; i++) {
				tempArray = pcStr[i].split("_");
				conditionSize = tempArray.length;
				pointStr = tempArray[0];
				colorStr = tempArray[1];
				if (conditionSize > 2) {
					onceStr = tempArray[2];
				}else {
					onceStr = "-1";
				}
				onceList.add(Integer.valueOf(onceStr));
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
			int[] riArray;
			while ((isGO() && !isForceStop()) || (alwaysGo && !isForceStop())){
				minSize = pcList.size();
				LOGGER.info("执行检测点击，对应脚本文件名： "+fileName);
				riArray = getRandomArray(minSize);
				if(minSize==0){
					LOGGER.info("NO POINTS");
					break;
				}
				for (int i = 0; i < minSize; i++) {
					if (ifThrowException) {
						setIfThrowException(false);
						throw new AppNeedRestartException();
					}
					ri = riArray[i];
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
						setLastClickTime(System.currentTimeMillis());
						LOGGER.info("移动( " + pList.get(ri).getX()+"_"+pList.get(ri).getY()+" )");
						GameUtil.mouseMoveByDD((int) pList.get(ri).getX(), (int) pList.get(ri).getY());
						LOGGER.info("左键单击");
						GameUtil.mousePressAndReleaseByDD();
						if (clickWait) {
							GameUtil.delay(1000);
						}
						int z = onceList.get(ri);
						if((--z) == 0){
							pcList.remove(ri);
							pList.remove(ri);
                            onceList.remove(ri);
							GameUtil.delay(1000);
							break;
						}
						LOGGER.info("当前次数： "+z);
						onceList.set(ri,z);
					}
					rb.delay(factor == null ? 200 * getFactor() : factor);
				}
//				LOGGER.info(alwaysGo + "__"+ isGO() +"__"+ isForceStop());
//				LOGGER.info((isGO() && !isForceStop()) || (alwaysGo && !isForceStop()));
			};
		}
		LOGGER.info("Finish auto click scanning!");
	}
	private int[] getRandomArray(int length){
		int[] arr = new int[length];//10个数的数组
		for (int i = 0; i < arr.length; i++) {
			//生产一个1-20的随机数
			int index = (int)(Math.random() * length );
			arr[i] = index;//把随机数赋值给下标为数组下标为i的值
			//（遍历数组中储存进去的值，i中有几个值则循环几次）
			for (int j = 0; j < i; j++)
			{
				//把储存在数组中的值j 和 随机出的值i 做比较
				if (arr[j] == arr[i])
				{
					i--; //数组的值下标-1，i的循环次数回到上次
					break;
				}
			}
		}
		return arr;
	}
	private static void putRandomNumberIntoArrays(int[] arrays) {
		int datalength =arrays.length;
		for (int i = 0; i < arrays.length; i++) {
			arrays[i]= i+1;
		}
		do{
			int  rand =new Random().nextInt(datalength--);
			int tmp=arrays[rand];
			arrays[rand] = arrays[datalength];
			arrays[datalength] = tmp;
		}while(datalength>0);
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
		BaseZButton setbt =bts[bts.length-5];
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
		String temp = PropertiesUtil.getValueFromAutoClickFile("condiTion", "");
		condiTion = (StringUtils.isBlank(temp)?"":temp) + condiTion;
		Map<String, String> map = new HashMap<>();
		map.put("condiTion", condiTion);
		PropertiesUtil.setValueForAutoClick(map);
	}
	private void selectActionPoint() {
		Point p = GameUtil.getMousePosition();
		String action = (int) p.getX()+"," + (int) p.getY()
		+ ";";
		String temp = PropertiesUtil.getValueFromAutoClickFile("action", "");
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

	public boolean isIfThrowException() {
		return ifThrowException;
	}

	@Override
	public void setIfThrowException(boolean ifThrowException) {
		this.ifThrowException = ifThrowException;
	}

	public static void main(String[] args) {
		String s = "true";
		String b = "false";
		System.out.println(s.length()+"__"+b.length());

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