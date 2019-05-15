package commons.util;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.alibaba.fastjson.*;
import fgoScript.entity.ColorMonitor;
import fgoScript.entity.GatesInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fgoScript.constant.GameConstant;
import fgoScript.constant.PointInfo;
import fgoScript.entity.PointColor;
import fgoScript.entity.ZButton;
import fgoScript.exception.FgoNeedRestartException;
import fgoScript.exception.FgoNeedStopException;
import fgoScript.service.EventFactors;

public class GameUtil {
	private static final Logger LOGGER = LogManager.getLogger(GameUtil.class);
	private static final int OUT_TIME = Integer.parseInt(GameUtil.getValueFromConfig("OUT_TIME"));// 超时时间(分)
	private static final int CHECK_TIMES = Integer.parseInt(GameUtil.getValueFromConfig("CHECK_TIMES"));
	private static final int EROOR_ROUND = 8;// 循环检查系数(DELAY*X*Y)
	private static boolean GO_FLAG = true;
	private static int CHECK_COUNT = 0;// 检测基数
	private static boolean STOP_SCRIPT = false;
	private static boolean FORCE_OUTTIME = false;
	private static boolean WAIT_FLAG = false;
	private static int OUTTIME_COUNT = 0;
	private static int WAIT_COUNT = 0;
	private static Object lock = new Object();
	private static Map<String, Robot> rbMap = new HashMap<>();
	private static List<ColorMonitor> colorMonitorList;


	public static Point getMousePosition() {
		PointerInfo pinfo = MouseInfo.getPointerInfo();
		return pinfo.getLocation();
	}
	private static Robot rb = null;
	public static Color getScreenPixel(Point p) {
		return getRb().getPixelColor((int) p.getX(), (int) p.getY());
	}public static Color getScreenPixel(Point p, String  className) {
		return getRb(className).getPixelColor((int) p.getX(), (int) p.getY());
	}
	public static void reNewRobot() {
		try {
			rb = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static Robot getRb() {
		if (rb==null) {
			try {
				rb = new Robot();
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return rb;
	}public static Robot getRb(String className) {
		Robot rbTemp;
			try {
				rbTemp = rbMap.get(className);
				if (rbTemp==null){
					rbTemp = new Robot();
					rbMap.put(className, rbTemp);
				}
				rb = new Robot();
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return rb;
	}
	public static void setRb(Robot r) {
		rb = r;
	}
	
	public static boolean isWAIT_FLAG() {
		return WAIT_FLAG;
	}
	
	public static void waitOrContinue(ZButton zt) {
		if (WAIT_COUNT++%2 ==0) {
			WAIT_FLAG = true;
			zt.setText("继续");
		}else {
			synchronized (getLock()) {
				WAIT_FLAG = false;
				lock.notifyAll();
			}
			zt.setText("暂停");
		}
	}
	public static void shutDownALl() {
		synchronized (getLock()) {
			WAIT_FLAG = false;
			lock.notifyAll();
		}
		setSTOP_SCRIPT(true);
	}
	public static void setWAIT_FLAG(boolean wAIT_FLAG) {
		WAIT_FLAG = wAIT_FLAG;
	}
	public static Object getLock() {
		if (lock == null) {
			lock = new Object();
		}
		return lock;
	}
	public static void setLock(Object lock) {
		GameUtil.lock = lock;
	}
	public static boolean isFORCE_OUTTIME() {
		return FORCE_OUTTIME;
	}
	public static void setFORCE_OUTTIME(boolean fORCE_OUTTIME) {
		FORCE_OUTTIME = fORCE_OUTTIME;
	}
	public static boolean isSTOP_SCRIPT() {
		return STOP_SCRIPT;
	}
	public static void setSTOP_SCRIPT(boolean sTOP_SCRIPT) {
		STOP_SCRIPT = sTOP_SCRIPT;
	}
	public static int getCHECK_COUNT() {
		return CHECK_COUNT;
	}
	public static void setCHECK_COUNT(int cHECK_COUNT) {
		CHECK_COUNT = cHECK_COUNT;
	}
	public static boolean isGO_FLAG() {
		return GO_FLAG;
	}
	public static void setGO_FLAG(boolean gO_FLAG) {
		GO_FLAG = gO_FLAG;
	}
	private static PointColor waitUntilOneColorInner(List<PointColor> pocoList) throws FgoNeedRestartException, FgoNeedStopException, InterruptedException {
		setSTOP_SCRIPT(false);
		GO_FLAG = true;
		rb = getRb();
		boolean flag;
		PointColor returnPC = null;
		Point p;
		Color c0;
		Color c1;
		boolean isEqual;
		int count = 0;
		int check = 0;

		StringBuilder Str = new StringBuilder();
		int size = pocoList.size();
		PointColor pointColor;
		for (int i = 0; i < size; i++) {
			pointColor = pocoList.get(i);
			Color c = pointColor.getColor();
			Str.append(c.getRed()).append(":").append(c.getGreen()).append(":").append(c.getBlue()).append("_");
		}
		LOGGER.debug("当前等待颜色组：" + Str);
		do {
			for (int i = 0; i < size; i++) {
				pointColor = pocoList.get(i);
				p = pointColor.getPoint();
				c0 = pointColor.getColor();
				isEqual = pointColor.isEqual();
				c1 = GameUtil.getScreenPixel(p);
				flag = isEqualColor(c1, c0);
				if (!isEqual) {
					flag = !flag;
				}
				if (flag) {
					LOGGER.debug("等待的颜色结果： " + c0.getRed() + ":" + c0.getGreen() + ":" + c0.getBlue());
					count++;
					returnPC = pointColor;
				}
			}
			// 意外检查
			if (check != 0 && (count == 0) && (check % EROOR_ROUND == 0)) {
				waitInteruptSolution();
			}
			check++;
			Thread.sleep(GameConstant.DELAY*CHECK_TIMES);
			if (!GO_FLAG && CHECK_COUNT == 0) {
				return null;
			}
			if (STOP_SCRIPT) {
				throw new FgoNeedStopException();
			}
			if (FORCE_OUTTIME) {
				setFORCE_OUTTIME(false);
				throw new FgoNeedRestartException();
			}
			if (isWAIT_FLAG()) {
				synchronized (getLock()) {
					LOGGER.info("程序暂停运行!");
					lock.wait();
					LOGGER.info("程序继续运行!");
				}
			}
		} while (count == 0);
		CHECK_COUNT++;
		return returnPC;
	}

	public static PointColor waitUntilOneColor(List<PointColor> pocoList) throws FgoNeedRestartException, FgoNeedStopException  {
		PointColor returnPC = null;
		Callable<PointColor> task = () -> waitUntilOneColorInner(pocoList);
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Future<PointColor> future = executorService.submit(task);
		
		try {
			future.get(OUT_TIME, TimeUnit.MINUTES);
			returnPC = future.get();
		}catch (TimeoutException e) {
			boolean flag;
			flag = future.cancel(true);
			LOGGER.info("超时了" + "_" + flag);
			if (OUTTIME_COUNT-->0 || isWAIT_FLAG()) {
				if (isWAIT_FLAG()) {
					LOGGER.info("延长超时！原因：线程需要暂停运行");
				}else {
					LOGGER.info("延长超时！原因：线程需要继续运行");
				}
				returnPC = waitUntilOneColor(pocoList);
			}else {
				throw new FgoNeedRestartException();
			}
		} catch (InterruptedException e) {
			LOGGER.info("终止了颜色检测程序！");
		} catch (ExecutionException e) {
			if (e.getCause() instanceof FgoNeedRestartException) {
				throw new FgoNeedRestartException();
			}else if(e.getCause() instanceof FgoNeedStopException) {
				throw new FgoNeedStopException();
			}
		} finally {
			OUTTIME_COUNT = 0;
			executorService.shutdown();
		}
		
		return returnPC;
	}

	public static void waitUntilAllColor(List<PointColor> pocoList, int delay) throws FgoNeedRestartException, FgoNeedStopException{
		Callable<String> task = () -> {
			waitUntilAllColorInner(pocoList, delay);
			return null;
		};
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Future<String> future = executorService.submit(task);
		try {
			future.get(OUT_TIME, TimeUnit.MINUTES);
		} catch (TimeoutException e) {
			boolean flag;
			flag = future.cancel(true);
			LOGGER.info("超时了" + "_" + flag);
			if (OUTTIME_COUNT-->0 || isWAIT_FLAG()) {
				if (isWAIT_FLAG()) {
					LOGGER.info("延长超时！原因：线程需要暂停运行");
				}else {
					LOGGER.info("延长超时！原因：线程需要继续运行");
				}
				waitUntilOneColor(pocoList);
			}else {
				throw new FgoNeedRestartException();
			}
		} catch (InterruptedException e) {
			LOGGER.info("终止了颜色检测程序！");
		} catch (ExecutionException e) {
			if (e.getCause() instanceof FgoNeedRestartException) {
				throw new FgoNeedRestartException();
			}else if(e.getCause() instanceof FgoNeedStopException) {
				throw new FgoNeedStopException();
			}
		} finally {
			OUTTIME_COUNT = 0;
			executorService.shutdown();
		}
	}

	private static void waitUntilAllColorInner(List<PointColor> pocoList, int delay) throws FgoNeedRestartException, FgoNeedStopException, InterruptedException {
		setSTOP_SCRIPT(false);
		boolean flag;
		Point p;
		Color c0;
		Color c1;
		boolean isEqual;
		int count;
		int check = 0;
		boolean toCheck;

		StringBuilder Str = new StringBuilder();
		int size = pocoList.size();
		PointColor pointColor;
		for (int i = 0; i < size; i++) {
			pointColor = pocoList.get(i);
			Color c = pointColor.getColor();
			Str.append(c.getRed()).append(":").append(c.getGreen()).append(":").append(c.getBlue()).append("_");
		}
		LOGGER.debug("当前等待颜色组：" + Str);
		do {
			count = 0;
			for (int i = 0; i < size; i++) {
				pointColor =pocoList.get(i);
				p = pointColor.getPoint();
				c0 = pointColor.getColor();
				isEqual = pointColor.isEqual();
				c1 = GameUtil.getScreenPixel(p);
				flag = isEqualColor(c1, c0);
				if (!isEqual) {
					flag = !flag;
				}
				if (flag) {
					count++;
				}
			}
			toCheck = (count == pocoList.size());
			// 异常检查
			if ((!toCheck) && (check % EROOR_ROUND == 0)) {
				waitInteruptSolution();
			}
			check++;
			Thread.sleep(delay*CHECK_TIMES);
			if (STOP_SCRIPT) {
				throw new FgoNeedStopException();
			}
			if (FORCE_OUTTIME) {
				setFORCE_OUTTIME(false);
				throw new FgoNeedRestartException();
			}
			if (isWAIT_FLAG()) {
				if (lock != null) {
					synchronized (getLock()) {
						LOGGER.info("程序暂停运行!");
						lock.wait();
						LOGGER.info("程序继续运行!");
					}
				}
			}
		} while (!toCheck);
		LOGGER.debug("所有颜色组匹配成功");
	}
	public static String getJsonString(String path){
		BufferedReader reader = null;
		InputStream inputStream=null;
		StringBuilder jsonStrs = new StringBuilder();
		try {
			inputStream = new FileInputStream(path);
			if(inputStream==null){
				LOGGER.info(path+" is not exist or the json file is wrong");
			}
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			String tempStr = null;
			while ((tempStr = reader.readLine()) != null) {
				jsonStrs.append(tempStr);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return jsonStrs.toString().trim();
	}
	public static JSONArray ConvertToJsonArray(String path){

		JSONArray jsonArray =null;
		BufferedReader reader = null;
		StringBuilder jsonStrs = new StringBuilder();

		try{
			jsonArray=JSONArray.parseArray(getJsonString(path));
		}catch(IllegalStateException ex){
			LOGGER.error(path+"JSON  File is wrong");
		}catch(JSONException ex){
			LOGGER.error(path+"JSON  File is wrong");
		}
		return jsonArray;
	}
	public static JSONObject ConvertToJSONObject(String path){

		JSONObject jSONObject =null;
		BufferedReader reader = null;
		StringBuilder jsonStrs = new StringBuilder();

		try{
			jSONObject=JSONObject.parseObject(getJsonString(path));
		}catch(IllegalStateException ex){
			LOGGER.error(path+"JSON  File is wrong");
		}catch(JSONException ex){
			LOGGER.error(path+"JSON  File is wrong");
		}
		return jSONObject;
	}
	private static List<ColorMonitor> getColorMonitorList(){
		String filepath = System.getProperty("user.dir") + "/config/monitor.json";
		JSONArray monitorJsonArray = ConvertToJsonArray(filepath);
		int size = monitorJsonArray.size();
		JSONArray monitorJSONArray; 			//monitorJSONArray数组

		JSONObject monitorJSONObject;			//monitorJSONObject
		JSONArray checkPointJSONArray;
		int checkPointArraySize;				//checkPointJSONArray数组大小
		JSONArray clickPointJSONArray;
		int clickPointArraySize;				//clickPointJSONArray数组大小
		List<PointColor> checkPcList;
		List<Point> clickPointList;
		String[] xyCoordinate;
		String[] colorRGB;
		ColorMonitor colorMonitor;
		Point tempPoint;
		Color tempColor;
		boolean equal;
		boolean confirm;
		boolean throwException;
		boolean extendOutTime;
		String name;
		JSONObject tempJsonObject;
		PointColor tempPointColor;
		List<ColorMonitor> colorMonitorList= new ArrayList<>();
		for (int i = 0; i < size; i++) {
			colorMonitor = new ColorMonitor();
			monitorJSONObject = monitorJsonArray.getJSONObject(i);
			//检查点坐标 Json数组
			checkPointJSONArray = monitorJSONObject.getJSONArray("checkPointArray");
			checkPointArraySize = checkPointJSONArray.size();
			checkPcList = new ArrayList<>();
			for (int j = 0; j < checkPointArraySize; j++) {

				tempJsonObject = checkPointJSONArray.getJSONObject(j);
				xyCoordinate = tempJsonObject.getString("point").split(",");
				tempPoint = new Point(Integer.parseInt(xyCoordinate[0].trim()), Integer.parseInt(xyCoordinate[1].trim()));
				colorRGB = tempJsonObject.getString("color").split(",");
				tempColor = new Color(Integer.parseInt(colorRGB[0].trim()),
						Integer.parseInt(colorRGB[1].trim()),
						Integer.parseInt(colorRGB[2].trim())
				);

				tempPointColor = new PointColor();
				tempPointColor.setPoint(tempPoint);
				tempPointColor.setColor(tempColor);
				tempPointColor.setEqual(true);
				checkPcList.add(tempPointColor);
			}
			//点击点坐标 Json数组
			clickPointJSONArray =  monitorJSONObject.getJSONArray("clickPointArray");
			clickPointArraySize = clickPointJSONArray.size();
			clickPointList = new ArrayList<>();
			for (int j = 0; j < clickPointArraySize; j++) {
				tempJsonObject = clickPointJSONArray.getJSONObject(j);
				xyCoordinate = tempJsonObject.getString("point").split(",");
				tempPoint = new Point(Integer.parseInt(xyCoordinate[0].trim()), Integer.parseInt(xyCoordinate[1].trim()));
				clickPointList.add(tempPoint);
			}
			// 是否抛异常
			throwException = Boolean.parseBoolean(monitorJSONObject.getString("throwException"));
			// 是否延长超时
			extendOutTime = Boolean.parseBoolean(monitorJSONObject.getString("extendOutTime"));
			name = monitorJSONObject.getString("name");
			colorMonitor.setCheckPointList(checkPcList);
			colorMonitor.setClickPointList(clickPointList);
			colorMonitor.setThrowException(throwException);
			colorMonitor.setExtendOutTime(extendOutTime);
			colorMonitor.setName(name);
			colorMonitorList.add(colorMonitor);
		}
		return colorMonitorList;
	}
	private static String waitInteruptSolution() throws FgoNeedRestartException {
		if (colorMonitorList == null) {
			colorMonitorList = getColorMonitorList();
		}
		int size = colorMonitorList.size();
		ColorMonitor cm;
		List<PointColor> checkPcList;
		int checkPcListSize;
		List<Point> clickPointList;
		int clickPointListSize;
		int count;
		Point tempPoint;
		for (int i = 0; i < size; i++) {
			cm = colorMonitorList.get(i);
			checkPcList = cm.getCheckPointList();
			checkPcListSize = checkPcList.size();
			count = ColorMatchCount(checkPcList);
			if (count == checkPcListSize) {
				clickPointList = cm.getClickPointList();
				clickPointListSize = clickPointList.size();
				for (int j = 0; j < clickPointListSize; j++) {
					tempPoint = clickPointList.get(j);
					mouseMoveByPoint(tempPoint);
					mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK);
					LOGGER.info(cm.getName());
				}
				if (cm.isExtendOutTime()){
					OUTTIME_COUNT = Integer.parseInt(GameUtil.getValueFromConfig("OUTTIME_COUNT"));
				}
				if (cm.isThrowException()){
					throw new FgoNeedRestartException();
				}
			}
		}
		List<PointColor> pocoList = new ArrayList<>();
		// 更新支援
		Point p_no_support = PointInfo.P_NO_SUPPORT;
		Color c_no_support = PointInfo.C_NO_SUPPORT;
		pocoList = new ArrayList<>();
		pocoList.add(new PointColor(p_no_support, c_no_support, true));
		count = ColorMatchCount(pocoList);
		if (count == pocoList.size()) {
			mouseMoveByPoint(EventFactors.supportServant);
			mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
			mouseMoveByPoint(PointInfo.P_SUPPORT_UPDATE);
			mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
			mouseMoveByPoint(PointInfo.P_SUPPORT_UPDATE_YES);
			mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
			GameUtil.delay(3000);
			LOGGER.info("更新支援！");
		}
		// 无法更新支援
		Point p_support_no_confirm = PointInfo.P_SUPPORT_NO_CONFIRM;
		Color c_support_no_confirm = PointInfo.C_SUPPORT_NO_CONFIRM;
		pocoList = new ArrayList<>();
		pocoList.add(new PointColor(p_support_no_confirm, c_support_no_confirm, true));
		count = ColorMatchCount(pocoList);
		if (count == pocoList.size()) {
			mouseMoveByPoint(p_support_no_confirm);
			mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
			LOGGER.info("等待更新支援！");
		}
		return null;
	}

	public static int ColorMatchCount(List<PointColor> pocoList) {
		Color temp;
		PointColor pc;
		Point p;
		Color c;
		int count = 0;
		boolean result;
		for (int i = 0; i < pocoList.size(); i++) {
			pc = pocoList.get(i);
			p = pc.getPoint();
			c = pc.getColor();
			temp = GameUtil.getScreenPixel(p);
			result = isEqualColor(c, temp);
			if (!pc.isEqual()) {
				result = !result;
			}
			if (result) {
				count++;
			}
		}
		return count;
	}

	public static int getColorEveValue(List<Point> pointList) {
		Point p;
		Color c;
		int addtion = 0;
		for (int i = 0; i < pointList.size(); i++) {
			p = pointList.get(i);
			c = GameUtil.getScreenPixel(p);
			addtion += c.getRed() + c.getGreen() + c.getBlue();
		}
		return (int) addtion / (pointList.size() * 3);
	}

	public static int ColorMatchCountProper(List<PointColor> pocoList) {
		Color temp;
		PointColor pc;
		Point p;
		Color c;
		int count = 0;
		boolean result;
		for (int i = 0; i < pocoList.size(); i++) {
			pc = pocoList.get(i);
			p = pc.getPoint();
			c = pc.getColor();
			temp = GameUtil.getScreenPixel(p);
			// 颜色误差
			int DIS_NUM = 25;
			result = Math.abs((c.getRed() + c.getGreen() + c.getBlue() - temp.getRed() - temp.getGreen()
					- temp.getBlue())) < DIS_NUM;
			if (!pc.isEqual()) {
				result = !result;
			}
			if (result) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 
	 * @param extent
	 *            格式(jpg、png)
	 * @param newfile
	 *            test1.jpg
	 */
	public static void img2file(String extent, String newfile) {
		try {
			// Toolkit tk = Toolkit.getDefaultToolkit(); // 获取缺省工具包
			// Dimension di = tk.getScreenSize(); // 屏幕尺寸规格
			Rectangle rec = new Rectangle(45, 54, 1280, 720);
			File file = new File(newfile + extent);
			if (!file.exists()) {
				file.mkdirs();
			}
			BufferedImage bi = getRb().createScreenCapture(rec);
			ImageIO.write(bi, extent, file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void mouseMoveByPoint(Point p) {
			rb = getRb();
			rb.mouseMove((int) p.getX(), (int) p.getY());
			rb.delay(GameConstant.DELAY);
	}

	public static void mousePressAndRelease(int key) {
			rb = getRb();
			rb.mousePress(key);
			rb.delay(GameConstant.DELAY/2);
			rb.mouseRelease(key);
			rb.delay(GameConstant.DELAY);
			rb.delay(GameConstant.DELAY);

	}
	public static void mousePressAndReleaseForConfirm(int key) throws FgoNeedRestartException {
		mousePressAndReleaseForConfirm(key, null);
	}
	public static void mousePressAndReleaseForConfirm(int key, PointColor pc) throws FgoNeedRestartException {
		rb = getRb();
		Point p;
		Color c;
		boolean isEqual;
		if (pc==null) {
			p = MouseInfo.getPointerInfo().getLocation();
			c = GameUtil.getScreenPixel(p);
			isEqual = true;
		}else {
			p = pc.getPoint(); 
			c = pc.getColor();
			isEqual = pc.isEqual();
		}
		Color temp;
		Color temp2;
		int count =10;
		boolean flag;
		boolean flag2;
		do {
			rb.delay(GameConstant.DELAY/2);
			rb.mousePress(key);
			rb.delay(1000);
			temp2 = GameUtil.getScreenPixel(p);
			rb.mouseRelease(key);
			rb.delay(GameConstant.DELAY*3);
			temp = GameUtil.getScreenPixel(p);
			flag = GameUtil.isEqualColor(c, temp);
			flag2 = GameUtil.isEqualColor(temp2, temp);
			if (pc == null) {
				flag = flag || flag2;
			}else {
				if (isEqual) {
					flag = !flag;
				}
			}
			if (count-- < 0) {
				throw new FgoNeedRestartException();
			}
		} while (flag);
		
	}
	public static void mousePressAndReleaseQuick(int key) {
			rb = getRb();
			rb.delay(GameConstant.DELAY / 8);
			rb.mousePress(key);
			rb.delay(GameConstant.DELAY / 8);
			rb.mouseRelease(key);
			rb.delay(GameConstant.DELAY / 8);
	}
	public static void keyPressAndRelease(int key) {
			rb = getRb();
			rb.keyPress(key);
			rb.delay(GameConstant.DELAY/2);
			rb.keyRelease(key);
			rb.delay(GameConstant.DELAY);
			rb.delay(GameConstant.DELAY);
	}
	
	public static boolean isEqualColor(Color c1, Color c2) {
		return (Math.abs(c1.getGreen() - c2.getGreen())
				+ Math.abs(c1.getBlue() - c2.getBlue())
		+ Math.abs(c1.getRed() - c2.getRed())) < 10;
	}
	public static boolean isLargerColor(Color c1, Color c2) {
		return (c1.getRed()+c1.getGreen()+c1.getBlue()
						- c2.getRed()-c2.getGreen()-c2.getBlue())>0;
	}

	public static List<Point> getCommondCards() {
		Point p_support = PointInfo.P_SUPPORT;
		Color color;
		Point point;
		PointColor pc;
		List<PointColor> pcList = new LinkedList<>();
		for (int i = 0; i < 5; i++) {
			point = new Point((int) p_support.getX() + i * 257, (int) p_support.getY());
			color = GameUtil.getScreenPixel(point);
			pc = new PointColor(point, color, true);
			pcList.add(pc);
		}
		pcList.sort((pc1, pc2) -> pc1.getColor().getRed() + pc1.getColor().getBlue() + pc1.getColor().getGreen()
				- pc2.getColor().getRed() - pc2.getColor().getBlue() - pc2.getColor().getGreen());
		List<Point> pList = new LinkedList<>();
		Point pTemp;
		Color cTemp;
		for (PointColor pointColor : pcList) {
			cTemp = pointColor.getColor();
			LOGGER.debug(cTemp.getRed() + "_" + cTemp.getGreen() + "_" + cTemp.getBlue());
			pTemp = pointColor.getPoint();
			pTemp.setLocation(pTemp.getX() - 80, pTemp.getY());
			pList.add(pTemp);
		}
		return pList;
	}

	public static String getStackMsg(Throwable e) {
		StringWriter sw = new StringWriter(); 
        e.printStackTrace(new PrintWriter(sw, true)); 
        String strs = sw.toString(); 
		return strs;
	}
	public static String getPreFix() {
		String ROOT_PATH = GameUtil.getValueFromConfig("ROOT_PATH");

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss\\");
		return System.getenv("USERPROFILE")
				+ ROOT_PATH + "\\"
				+ sdf.format(date).toString();
	}
	public static ImageIcon getBackGroundPreFix(int width, int length) {
		String rootPath= System.getenv("USERPROFILE") + "\\OneDrive\\图片\\桌面背景\\";
		
		File rootFile= new File(rootPath);
		List<File> fileList = new ArrayList<>();
		if (rootFile.isDirectory()) {
			File[] files = rootFile.listFiles();
			int size = files != null ? files.length : 0;
			File tempFile;
			for (int i = 0; i < size; i++) {
				tempFile = files[i];
				if (tempFile.getName().contains("background")) {
					fileList.add(tempFile);
				}
			}
		}else {
			LOGGER.debug("无法获取图片路径！");
		}
		Random rd = new Random(); //创建一个Random类对象实例
        int index = rd.nextInt(fileList.size());
		ImageIcon im = new ImageIcon(
				System.getenv("USERPROFILE") + "\\OneDrive\\图片\\桌面背景\\"+fileList.get(index).getName());
		Image me = im.getImage();
		me = me.getScaledInstance(width, length, Image.SCALE_DEFAULT);
		return new ImageIcon(me);
	}
	public static ImageIcon getBackGroundPreFix(int width, int length, String filePath) {
		ImageIcon im = new ImageIcon(filePath);
		Image me = im.getImage();
		me = me.getScaledInstance(width, length, Image.SCALE_DEFAULT);
		return new ImageIcon(me);
	}
	public static void moveToLeftTop() {
		rb = getRb();
		rb.mouseMove(370, 193);
		rb.delay(GameConstant.DELAY);
		rb.delay(GameConstant.DELAY);
		rb.delay(GameConstant.DELAY);
		rb.mousePress(KeyEvent.BUTTON1_DOWN_MASK);
		rb.delay(GameConstant.DELAY);
		rb.mouseMove(50, 30);
		rb.delay(GameConstant.DELAY);
		rb.mouseRelease(KeyEvent.BUTTON1_DOWN_MASK);
	}
	public static void delay(int delay) {
		GameUtil.getRb().delay(delay);
	}
	/**
	 * 两个颜色差
	 *
	 * @return
	 */
	public static int colorMinus(Point pa, Point pb) {
		Color ca = GameUtil.getScreenPixel(pa);
		Color cb = GameUtil.getScreenPixel(pb);
		return ca.getGreen() + ca.getRed() + ca.getBlue() - cb.getGreen() - cb.getRed() - cb.getBlue();
	}
	public static int[] strToIntArray(String ArrayStr,boolean ifSort) {
		int[] intArray;
		if (!"".equals(ArrayStr)) {
			String[] strArray = ArrayStr.split(",");
			int size = strArray.length;
			int len = size;
			for (int i = 0; i < size; i++) {
				if (StringUtils.isBlank(strArray[i])) {
					len-=1;
				}
			}
			intArray = new int[len];
			int count = 0;
			for (int i = 0; i < size; i++) {
				if (StringUtils.isNoneBlank(strArray[i])) {
					intArray[count] = Integer.parseInt(strArray[i]);
					count++;
				}
			}
		}else {
			intArray = new int[0];
		}
		if (ifSort) {
			Arrays.sort(intArray);
		}
		return intArray;
	}
	public static void main(String[] args) {
		String filepath = System.getProperty("user.dir") + "/config/special_all_train.json";
		String jsonString = getJsonString(filepath);
		GatesInfo gi = JSON.parseObject(jsonString, GatesInfo.class);
	}
	public static String getValueFromConfig(String key) {
		String fgoArrayStr =  PropertiesUtil.getValueFromTempConfig(key);
		if (StringUtils.isBlank(fgoArrayStr)) {
			fgoArrayStr = PropertiesUtil.getValueFromConfig(key);
		}else {
			fgoArrayStr = "null".equals(fgoArrayStr) ? "" : fgoArrayStr;
			Map<String, String> map = new HashMap<>();
			map.put(key, "");
			PropertiesUtil.setValueForInitTemp(map);
		}
		return fgoArrayStr;
	}
}
