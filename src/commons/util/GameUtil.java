package commons.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import commons.entity.DD;
import fgoScript.constant.GameConstant;
import fgoScript.constant.PointInfo;
import fgoScript.entity.BaseZButton;
import fgoScript.entity.ColorMonitor;
import fgoScript.entity.PointColor;
import fgoScript.exception.AppNeedRestartException;
import fgoScript.exception.AppNeedStopException;
import fgoScript.exception.AppNeedUpdateException;
import fgoScript.service.EventFactors;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.concurrent.*;

public class GameUtil {
	private static final Logger LOGGER = LogManager.getLogger(GameUtil.class);
	private static final int OUT_TIME = Integer.parseInt(GameUtil.getValueFromConfig("OUT_TIME"));// 超时时间(分)
	private static final int CHECK_TIMES = Integer.parseInt(GameUtil.getValueFromConfig("CHECK_TIMES"));
	private static final int EROOR_ROUND = 8;// 循环检查系数(DELAY*X*Y)
	private static final PointInfo POINT_INFO = PointInfo.getSpringBean();
	private static boolean GO_FLAG = true;
	private static int CHECK_COUNT = 0;// 检测基数
	private static boolean STOP_SCRIPT = false;
	private static boolean FORCE_OUTTIME = false;
	private static boolean WAIT_FLAG = false;
	private static int OUTTIME_COUNT = 0;
	private static int WAIT_COUNT = 0;
	private static Object lock = new Object();
	private static Map<String, Robot> rbMap = new HashMap<>();
	public static List<ColorMonitor> colorMonitorList;
	public static final DD dd = getDD();
	private static DD getDD(){
		return (DD) MySpringUtil.getApplicationContext().getBean("dd");
	}

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
	
	public static void waitOrContinue(BaseZButton zt) {
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
	private static PointColor waitUntilOneColorInner(List<PointColor> pocoList, String monitorName) throws AppNeedUpdateException, AppNeedRestartException, AppNeedStopException, InterruptedException {
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
				flag = likeEqualColor(c1, c0);
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
				waitInteruptSolution(check, monitorName);
			}
			check++;
			Thread.sleep(GameConstant.DELAY*CHECK_TIMES);
			if (!GO_FLAG && CHECK_COUNT == 0) {
				return null;
			}
			if (STOP_SCRIPT) {
				throw new AppNeedStopException();
			}
			if (FORCE_OUTTIME) {
				setFORCE_OUTTIME(false);
				throw new AppNeedRestartException();
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
	/**
	 * @Description: 等待颜色
	 * @param pocoList PointColor对象集合
	 * @return: fgoScript.entity.PointColor
	 * @Author: RENZHEHAO
	 * @Date: 2019/6/5
	 */
	public static PointColor waitUntilOneColor(List<PointColor> pocoList, String monitorName) throws AppNeedUpdateException, AppNeedRestartException, AppNeedStopException {
		PointColor returnPC = null;
		Callable<PointColor> task = () -> waitUntilOneColorInner(pocoList, monitorName);
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
				returnPC = waitUntilOneColor(pocoList, monitorName);
			}else {
				throw new AppNeedRestartException();
			}
		} catch (InterruptedException e) {
			LOGGER.info("终止了颜色检测程序！");
		} catch (ExecutionException e) {
			if (e.getCause() instanceof AppNeedRestartException) {
				throw new AppNeedRestartException();
			}else if(e.getCause() instanceof AppNeedStopException) {
				throw new AppNeedStopException();
			}else if(e.getCause() instanceof AppNeedUpdateException) {
				throw new AppNeedUpdateException();
			}
		} finally {
			OUTTIME_COUNT = 0;
			executorService.shutdown();
		}
		
		return returnPC;
	}

	public static void waitUntilAllColor(List<PointColor> pocoList, int delay, String monitorName) throws AppNeedUpdateException, AppNeedRestartException, AppNeedStopException {
		Callable<String> task = () -> {
			waitUntilAllColorInner(pocoList, delay, monitorName);
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
				waitUntilAllColor(pocoList, delay, monitorName);
			}else {
				throw new AppNeedRestartException();
			}
		} catch (InterruptedException e) {
			LOGGER.info("终止了颜色检测程序！");
		} catch (ExecutionException e) {
			if (e.getCause() instanceof AppNeedRestartException) {
				throw new AppNeedRestartException();
			}else if(e.getCause() instanceof AppNeedStopException) {
				throw new AppNeedStopException();
			}
		} finally {
			OUTTIME_COUNT = 0;
			executorService.shutdown();
		}
	}

	private static void waitUntilAllColorInner(List<PointColor> pocoList, int delay, String monitorName) throws AppNeedUpdateException, AppNeedRestartException, AppNeedStopException, InterruptedException {
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
				flag = likeEqualColor(c1, c0);
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
				waitInteruptSolution(check, monitorName);
			}
			check++;
			Thread.sleep(delay*CHECK_TIMES);
			if (STOP_SCRIPT) {
				throw new AppNeedStopException();
			}
			if (FORCE_OUTTIME) {
				setFORCE_OUTTIME(false);
				throw new AppNeedRestartException();
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
	private static List<ColorMonitor> getColorMonitorList(String monitorName){
		String filepath = System.getProperty("user.dir") + "/config/"+ monitorName +".json";
		JSONArray monitorJsonArray = ConvertToJsonArray(filepath);

		int size = monitorJsonArray.size();
		ColorMonitor colorMonitor;
		List<ColorMonitor> colorMonitorList= new ArrayList<>();
		for (int i = 0; i < size; i++) {
			colorMonitor = JSONArray.parseObject(monitorJsonArray.getJSONObject(i).toJSONString(), ColorMonitor.class);
			colorMonitorList.add(colorMonitor);
		}
		return colorMonitorList;
	}
	public static String waitInteruptSolution(int check, String monitorName) throws AppNeedRestartException, AppNeedUpdateException {
		if (colorMonitorList == null || check == 0) {
			colorMonitorList = getColorMonitorList(monitorName);
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
					if ("选择人物".equals(cm.getName())) {
						mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
					}else {
						mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
					}
					LOGGER.info(cm.getName());
				}
				if (cm.isExtendOutTime()){
					OUTTIME_COUNT = Integer.parseInt(GameUtil.getValueFromConfig("OUTTIME_COUNT"));
				}
				if (cm.isThrowException()){
					delay(5000);
					throw new AppNeedRestartException();
				}
				if (cm.isNeedUpdate()){
					delay(5000);
					throw new AppNeedUpdateException();
				}
			}
		}
		List<PointColor> pocoList = new ArrayList<>();
		// 更新支援
		Point p_no_support = POINT_INFO.getpNoSupport();
		Color c_no_support = POINT_INFO.getcNoSupport();
		pocoList = new ArrayList<>();
		pocoList.add(new PointColor(p_no_support, c_no_support, true));
		count = ColorMatchCount(pocoList);
		if (count == pocoList.size()) {
			mouseMoveByPoint(EventFactors.supportServant);
			mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
			mouseMoveByPoint(POINT_INFO.getpSupportUpdate());
			mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
			mouseMoveByPoint(POINT_INFO.getpSupportUpdateYes());
			mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
			GameUtil.delay(3000);
			LOGGER.info("更新支援！");
		}
		// 无法更新支援
		Point p_support_no_confirm = POINT_INFO.getpSupportNoConfirm();
		Color c_support_no_confirm = POINT_INFO.getcSupportNoConfirm();
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
			result = likeEqualColor(c, temp);
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
		Rectangle rec = new Rectangle(45, 54, 1280, 720);
		Img2file(rec, extent, newfile);
	}
	public static void fullScreen2file(String extent, String newfile) {
		Dimension d = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
		int width = (int) d.getWidth();
		int height = (int) d.getHeight();
		Rectangle rec = new Rectangle(0, 0, width, height);
		Img2file(rec, extent, newfile);
	}
	private static void Img2file(Rectangle rec, String extent, String newfile) {
		try {
			// Toolkit tk = Toolkit.getDefaultToolkit(); // 获取缺省工具包
			// Dimension di = tk.getScreenSize(); // 屏幕尺寸规格
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
			mouseMoveByDD((int) p.getX(), (int) p.getY());
			rb.delay(GameConstant.DELAY);
	}

	public static void mousePressAndRelease(int key) {
			rb = getRb();
			rb.mousePress(key);
			rb.delay(GameConstant.DELAY/4);
			rb.mouseRelease(key);
			rb.delay(GameConstant.DELAY);
			rb.delay(GameConstant.DELAY);

	}
	public static void mousePressAndReleaseForConfirm(int key) throws AppNeedRestartException {
		mousePressAndReleaseForConfirm(key, null);
	}
	public static void mousePressAndReleaseForConfirm(int key, PointColor pc) throws AppNeedRestartException {
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
			flag = GameUtil.likeEqualColor(c, temp);
			flag2 = GameUtil.likeEqualColor(temp2, temp);
			if (pc == null) {
				flag = flag || flag2;
			}else {
				if (isEqual) {
					flag = !flag;
				}
			}
			if (count-- < 0) {
				throw new AppNeedRestartException();
			}
		} while (flag);
		
	}

	public static void mousePressAndReleaseByDD() {
		rb = getRb();
		dd.HIVM_BTN(1);
		rb.delay(GameConstant.DELAY/10);
		dd.HIVM_BTN(2);
	}
	public static void mouseMoveByDD(int x, int y) {
		dd.HIVM_MOV(x+1, y+1);
	}
	public static void mousePressAndReleaseByDdQuick(int delay) {
		rb = getRb();
		dd.HIVM_BTN(1);
		rb.delay(delay);
		dd.HIVM_BTN(2);
	}
	public static void mousePressAndReleaseQuick(int key) {
			rb = getRb();
			rb.delay(GameConstant.DELAY / 6);
			rb.mousePress(key);
			rb.delay(GameConstant.DELAY / 6);
			rb.mouseRelease(key);
			rb.delay(GameConstant.DELAY / 6);
	}
	public static void mousePressAndReleaseForLongTime(int key,int milliSeconds) {
		rb = getRb();
		rb.delay(GameConstant.DELAY / 8);
		rb.mousePress(key);
		rb.delay(milliSeconds);
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
	
	public static boolean likeEqualColor(Color c1, Color c2) {
		return likeEqualColor(c1, c2, 10);
	}
	public static boolean likeEqualColor(Color c1, Color c2, int minusValue) {
		return (Math.abs(c1.getGreen() - c2.getGreen())
				+ Math.abs(c1.getBlue() - c2.getBlue())
		+ Math.abs(c1.getRed() - c2.getRed())) <= minusValue;
	}
	public static boolean isLargerColor(Color c1, Color c2) {
		return (c1.getRed()+c1.getGreen()+c1.getBlue()
						- c2.getRed()-c2.getGreen()-c2.getBlue())>0;
	}

	public static List<Point> getCommondCards() {
		Point p_support = POINT_INFO.getpSupport();
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
		rb.mouseMove(339, 157);
		rb.delay(GameConstant.DELAY);
		rb.delay(GameConstant.DELAY);
		rb.delay(GameConstant.DELAY);
		rb.mousePress(KeyEvent.BUTTON1_DOWN_MASK);
		rb.delay(GameConstant.DELAY);
		rb.mouseMove(50, 30);
		rb.delay(GameConstant.DELAY);
		rb.mouseRelease(KeyEvent.BUTTON1_DOWN_MASK);
	}
	public static void moveToDestinyPoint(Point from , Point to) {
		rb = getRb();
		rb.mouseMove((int) from.getX(), (int) from.getY());
		rb.delay(GameConstant.DELAY);
		rb.delay(GameConstant.DELAY);
		rb.delay(GameConstant.DELAY);
		rb.mousePress(KeyEvent.BUTTON1_DOWN_MASK);
		rb.delay(GameConstant.DELAY);
		rb.mouseMove((int) to.getX(), (int) to.getY());
		rb.delay(GameConstant.DELAY);
		rb.mouseRelease(KeyEvent.BUTTON1_DOWN_MASK);
	}
	public static void ctrlV() {
		rb.keyPress(KeyEvent.VK_CONTROL);
		rb.keyPress(KeyEvent.VK_Z);
		rb.delay(GameConstant.DELAY/4);
		rb.keyRelease(KeyEvent.VK_Z);
		rb.keyRelease(KeyEvent.VK_CONTROL);
	}
	public static void delay(int delay) {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
		System.out.println();
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
