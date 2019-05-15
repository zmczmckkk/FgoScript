package commons.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fgoScript.entity.Gudazi;

public class PropertiesUtil {
	private static final Logger LOGGER = LogManager.getLogger(Gudazi.class);
	/**
	 * 取出值
	 * 
	 * @param key
	 * @param filepath
	 * @return
	 */
	private static String getValue(String filepath, String key) {
		Properties prop = new Properties();
		InputStream in = null;
		try {
			File file = new File(filepath);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileInputStream fis = new FileInputStream(file);
			in = new BufferedInputStream(fis);
			prop.load(in); /// 加载属性列表
			Iterator<String> it = prop.stringPropertyNames().iterator();
			while (it.hasNext()) {
				String temp = it.next();
				if (temp.equals(key)) {
					return prop.getProperty(temp);
				}
			}
		} catch (Exception e) {
			LOGGER.error("读取配置信息出错！", e);
		} finally {
			try {
				if (in!=null) {
					in.close();
				}
			} catch (IOException e) {
				LOGGER.error(GameUtil.getStackMsg(e));
			}
		}
		return "";
	}
	public static String getValueFromConfig(String key) {
		String filepath = System.getProperty("user.dir") + "/config/init.properties";
		return getValue(filepath, key);
	}
	public static String getValueFromTempConfig(String key) {
		String filepath = System.getProperty("user.dir") + "/config/initTemp.properties";
		return getValue(filepath, key);
	}
	public static String getValueFromColorFile(String key) {
		String filepath = System.getProperty("user.dir") + "/config/colors.properties";
		return getValue(filepath, key);
	}
	public static String getValueFromCommandCardFile(String key) {
		String filepath = System.getProperty("user.dir") + "/config/commandCard.properties";
		return getValue(filepath, key);
	}
	public static String getValueFromOpenFile(String key) {
		String filepath = System.getProperty("user.dir") + "/config/open.properties";
		return getValue(filepath, key);
	}
	public static String getValueFromSkillsFile(String key) {
		String filepath = System.getProperty("user.dir") + "/config/skills.properties";
		return getValue(filepath, key);
	}
	public static String getValueFromHasDoFile(String key) {
		String filepath = System.getProperty("user.dir") + "/config/hasDo.properties";
		return getValue(filepath, key);
	}
	public static String getValueFromAutoClickFile(String key) {
		String filepath = System.getProperty("user.dir") + "/config/clicks.properties";
		return getValue(filepath, key);
	}
	public static String getValueFromspecialHasDo(String key) {
		String filepath = System.getProperty("user.dir") + "/config/specialHasDo.properties";
		return getValue(filepath, key);
	}
	public static String getValueFromFileNameAndKey(String key, String fileName){
		String filepath = System.getProperty("user.dir") + "/config/" + fileName + ".properties";
		return getValue(filepath, key);
	}
	/**
	 *
	 * @param map
	 */
	public static void setValue( Map<String, String> map) {
		Properties prop = new Properties();
		String filepath = System.getProperty("user.dir") + "/config/colors.properties";
		/// 保存属性到b.properties文件
		FileOutputStream oFile = null;
		try {
			LOGGER.info("写入新属性");
			oFile = new FileOutputStream(filepath, false);
			Set<Map.Entry<String, String>> entrySet = map.entrySet();
			for (Map.Entry<String, String> entry : entrySet) {
				prop.put(entry.getKey(), entry.getValue());
			}
			prop.store(oFile, "The New properties file");
		} catch (Exception e) {
			LOGGER.error("写入配置信息出错！", e);
			e.printStackTrace();
		} finally {
			try {
				if (oFile!=null) {
					oFile.close();
				}
			} catch (IOException e) {
				LOGGER.error(GameUtil.getStackMsg(e));
			}
		}
	}
	/**
	 * 
	 * @param map
	 */
	public static void setValueForInitTemp( Map<String, String> map) {
		String filepath = System.getProperty("user.dir") + "/config/initTemp.properties";
		setValueForUpdateAndAdd(map, filepath);
	}
	/**
	 * 
	 * @param map
	 */
	public static void setValueForSkills( Map<String, String> map) {
		String filepath = System.getProperty("user.dir") + "/config/skills.properties";
		setValueForUpdateAndAdd(map, filepath);
	}
	/**
	 * 
	 * @param map
	 */
	public static void setValueForOpen( Map<String, String> map) {
		String filepath = System.getProperty("user.dir") + "/config/open.properties";
		setValueForUpdateAndAdd(map, filepath);
	}
	/**
	 * 
	 * @param map
	 */
	public static void setValueForAutoClick( Map<String, String> map) {
		String filepath = System.getProperty("user.dir") + "/config/clicks.properties";
		setValueForUpdateAndAdd(map, filepath);
	}
	public static void setValueForspecialHasDo( Map<String, String> map) {
		String filepath = System.getProperty("user.dir") + "/config/specialHasDo.properties";
		setValueForUpdateAndAdd(map, filepath);
	}
	public static void setValueByFileName( Map<String, String> map, String saveFileString) {
		String filepath = System.getProperty("user.dir") + "/config/" +saveFileString+".properties";
		setValueForUpdateAndAdd(map, filepath);
	}
	public static void deleteAutoClickFile() {
		String filepath = System.getProperty("user.dir") + "/config/clicks.properties";
		File file = new File(filepath);
		if (file.exists()) {
			file.delete();
		}
	}
	/**
	 * 
	 * @param map
	 */
	public static void setValueForUpdateAndAdd( Map<String, String> map, String filepath) {
		Properties prop = new Properties();
		/// 保存属性到b.properties文件
		FileOutputStream oFile = null;
		try {
			LOGGER.info("加载属性列表");
			FileInputStream fis = new FileInputStream(filepath);
			BufferedInputStream in = new BufferedInputStream(fis);
			prop.load(in); /// 加载属性列表
			LOGGER.info("写入新属性");
			oFile = new FileOutputStream(filepath, false);
			Set<Map.Entry<String, String>> entrySet = map.entrySet();
			for (Map.Entry<String, String> entry : entrySet) {
				if (prop.containsKey(entry.getKey())) {
					prop.setProperty(entry.getKey(), entry.getValue());
				}else {
					prop.put(entry.getKey(), entry.getValue());
				}
			}
			prop.store(oFile, "The New properties file");
		} catch (Exception e) {
			LOGGER.error("写入配置信息出错！", e);
			e.printStackTrace();
		} finally {
			try {
				if (oFile!=null) {
					oFile.close();
				}
			} catch (IOException e) {
				LOGGER.error(GameUtil.getStackMsg(e));
			}
		}
	}
	/**
	 * 
	 * @param map
	 */
	public static void setValueForHasDo( Map<String, String> map) {
		Properties prop = new Properties();
		String filepath = System.getProperty("user.dir") + "/config/hasDo.properties";
		/// 保存属性到b.properties文件
		FileOutputStream oFile = null;
		try {
			LOGGER.info("写入新属性");
			oFile = new FileOutputStream(filepath, false);
			Set<Map.Entry<String, String>> entrySet = map.entrySet();
			for (Map.Entry<String, String> entry : entrySet) {
				prop.put(entry.getKey(), entry.getValue());
			}
			prop.store(oFile, "The New properties file");
		} catch (Exception e) {
			LOGGER.error("写入配置信息出错！", e);
			e.printStackTrace();
		} finally {
			try {
				if (oFile!=null) {
					oFile.close();
				}
			} catch (IOException e) {
				LOGGER.error(GameUtil.getStackMsg(e));
			}
		}
	}
	public static void main(String[] asrgs) {
		getValueFromConfig("EXE_PATH");
	}
}
