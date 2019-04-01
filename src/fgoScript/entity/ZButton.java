package fgoScript.entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Name;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicButtonUI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.melloware.jintellitype.JIntellitype;

import aoshiScript.entity.WuNa;
import fgoScript.constant.GameConstant;
import fgoScript.exception.FgoNeedStopException;
import fgoScript.util.GameUtil;
import fgoScript.util.PropertiesUtil;
import sun.swing.SwingUtilities2;

public abstract class ZButton extends JButton implements Runnable{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger(ZButton.class);
	static class MyLock {}
	static final Object LOCK = new MyLock();
	public int shortcunt01;
	public int shortcunt02;
	private boolean excuteble;
	private int markCode;
	public  abstract void runMethod() throws Exception;
	/* 决定圆角的弧度 */
    public static final int radius = 4;
    private Color COLOR1, COLOR2;
    private Color MY_GRID_COLOR01, MY_GRID_COLOR02, MY_FONT_COLOR;
    public static final int pink = 3;
	public static final int ashen = 2;
	public static final int sky = 1;
	public static final int stone = 0;
    /* 粉红 */
	public static final Color mainStyle01 = new Color(100, 0, 0);
	public static final Color mainStyle02 = new Color(0, 0, 100);
	/* 激活颜色 */
	public static final Color active01 = new Color(100, 149, 237);
	public static final Color active02 = new Color(100, 149, 237);
	/* 深宝石蓝 */
	public static final Color stone1 = new Color(255, 0, 0);
	public static final Color stone2 = new Color(39, 121, 181);
	/* 执行颜色 */
	public static final Color excuteStyle01 = new Color(205, 150, 205);
	public static final Color excuteStyle02 = new Color(205, 150, 205);
	/* 技能任务组颜色 */
	public static final Color skillColor01 = new Color(100, 0, 0);
	public static final Color skillColor02 = new Color(0, 0, 80);
	
	private int skillsStatus;
    private boolean isActive;
    private boolean enableStatus;
    private int person;
    private int monster;
    private int disAbleStyle;
    private String fromPerson;
    private int fromSkill;
    private boolean allowRepeat;
    //自定义切换按钮
	private List<String> changeList;
	private String saveFileString;
	private String changeListKeyName;
    
 
    public int getDisAbleStyle() {
		return disAbleStyle;
	}

	public void setDisAbleStyle(int disAbleStyle) {
		this.disAbleStyle = disAbleStyle;
	}
	public final Object getMyLock() {
        return LOCK;
    }
	public ZButton() {
        this("", stone);
    }
 
    public ZButton(String name) {
        this(name, stone);
    }
    public ZButton(String fromPerson,int fromSkill, String name, int shortcunt01, int shortcunt02, boolean excuteble, boolean enableStatus, int style) {
    	this.shortcunt01 = shortcunt01;
		this.shortcunt02 = shortcunt02;
		this.excuteble = excuteble;
		this.enableStatus = enableStatus;
		this.fromPerson = fromPerson;
		this.fromSkill = fromSkill;
        super.setText(name);
        setColor(style);
    }
    public ZButton(String name, int shortcunt01, int shortcunt02, boolean excuteble, boolean enableStatus, int style) {
    	this.shortcunt01 = shortcunt01;
		this.shortcunt02 = shortcunt02;
		this.excuteble = excuteble;
		this.enableStatus = enableStatus;
        super.setText(name);
        setColor(style);
    }
    public void clickButton(){
		String strategy;
		int size = changeList.size();
		int loc=0;
		for (int i = 0; i < size; i++) {
			if (this.getText().equals(changeList.get(i))) {
				loc = i + 1 > size - 1 ? 0 : i + 1;
				this.setText(changeList.get(loc));
				break;
			}
		}
		Map<String, String> map = new HashMap<>();
		map.put(this.getChangeListKeyName(), changeList.get(loc));
		PropertiesUtil.setValueByFileName(map, this.saveFileString);
	}
	public static ZButton getChangeListButton(List<String> changeList, String saveFileString, String changeListKeyName,
									   boolean excuteble, boolean enableStatus, int style){
		return new ZButton(changeList,saveFileString,
				changeListKeyName,
				excuteble,
				enableStatus, style) {
			@Override
			public void runMethod() throws Exception {
				this.clickButton();
			}
		};
	}
	/**
	 * 可变列表按钮构造器
	 * @param changeList  可变内容列表
	 * @param saveFileString  保存的配置文件名称
	 * @param changeListKeyName 配置文件中，列表所对应的key值。
	 * @param excuteble 是否可执行
	 * @param enableStatus 是否需要启动状态
	 * @param style 颜色样式
	 */
    public ZButton(List<String> changeList, String saveFileString, String changeListKeyName,
				   boolean excuteble, boolean enableStatus, int style) {
		this.changeList = changeList;
		this.saveFileString = saveFileString;
		this.changeListKeyName = changeListKeyName;
		this.excuteble = excuteble;
		this.enableStatus = enableStatus;
		String text = PropertiesUtil.getValueFromFileNameAndKey(changeListKeyName, saveFileString);
		try {
			super.setText("".equals(text)? changeList.get(0) : text);
		} catch (NullPointerException e) {
			LOGGER.error("按钮列表不能为空！");
		}
		setColor(style);
    }
    public ZButton(String name, int style) {
        super.setText(name);
        setColor(style);
    }
    public String getSkilStateText() {
    	String value = PropertiesUtil.getValueFromSkillsFile(getFromPerson());
    	if (value.contains("" + getFromSkill())) {
    		setActive(true);
			return "√";
		}
    	return " ";
    }
    public void lightForAwhile() {
    	synchronized (getMyLock()) {
    		boolean flag = this.isEnabled();
			this.setEnabled(!flag);
			this.setDisableStyleByStyle();
    		try {
    			Thread.sleep(300);
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    		this.setEnabled(flag);
		}
	}
    public String getToPersonText() {
    	String value = PropertiesUtil.getValueFromSkillsFile(getText());
    	switch (value) {
		case "0":
			return "英灵1";
		case "1":
			return "英灵2";
		case "2":
			return "英灵3";
		default:
			return "英灵1";
		}
    }
    public String getToMonsterText() {
    	String value = PropertiesUtil.getValueFromSkillsFile(getText());
    	switch (value) {
		case "0":
			return "敌人1";
		case "1":
			return "敌人2";
		case "2":
			return "敌人3";
		default:
			return "敌人1";
		}
    }
    public String getReStartFlag() {
    	String value = PropertiesUtil.getValueFromSkillsFile("ifRestart");
    	switch (value) {
		case "true":
			return "重开";
		case "false":
			return "新开";
		default:
			return "新开";
		}
    }
    public String getskillStrategy() {
		String startegy = PropertiesUtil.getValueFromSkillsFile("SKILL_STRATEGY");
		switch("".equals(startegy)? GameConstant.NO_SKILL : startegy){
			case GameConstant.NO_SKILL : {
				return "T1NoNP";
			}
			case GameConstant.GO_SECOD_SKILL_FOR_FIRST : {
				return "T1SecondNP";
			}
			case GameConstant.GO_ALL_SKILL_FOR_FIRST : {
				return "T1AllNP";
			}
			default : {
				return "";
			}
		}
    }
    public String getIf() {
    	String value = PropertiesUtil.getValueFromSkillsFile("ifRestart");
    	switch (value) {
		case "true":
			return "重开";
		case "false":
			return "新开";
		default:
			return "新开";
		}
    }
	public void activeButton() {
    	setActive(!isActive());
    	String tempFromSkill;
    	String tempValue = PropertiesUtil.getValueFromSkillsFile(getFromPerson());
    	if (isActive) {
    		this.setText("√");
    		if (!tempValue.contains(getFromSkill() + "")) {
    			tempFromSkill = tempValue+","+getFromSkill();
    			tempFromSkill = tempFromSkill.replaceAll(",,", ",");
        	}else {
        		tempFromSkill = tempValue;
        	}
		}else {//2,3
			 this.setText(" ");
			 if (tempValue.contains(getFromSkill() + "")) {
    			tempFromSkill = tempValue.replace(""+getFromSkill(), "");
    			tempFromSkill = tempFromSkill.replaceAll(",,", ",");
        	 }else {
        		tempFromSkill = tempValue;
        	 }
		}
    	Map<String, String> map = new HashMap<>();
    	map.put(getFromPerson(), tempFromSkill);
    	PropertiesUtil.setValueForSkills(map);
    }
	public void selectIfRestart() {
		Map<String, String> map = new HashMap<>();
    	if (this.getText().equals("重开")) {
    		 this.setText("新开");
    		 map.put("ifRestart", "false");
		}else if (this.getText().equals("新开")) {
			 this.setText("重开");
    		 map.put("ifRestart", "true");
		}
    	PropertiesUtil.setValueForSkills(map);
    }
	public void selectPerson(int loc) {
    	if (this.getText().equals("英灵3")) {
    		 this.setText("英灵1");
    		 this.setPerson(0);
		}else if (this.getText().equals("英灵1")) {
			 this.setText("英灵2");
			 this.setPerson(1);
		}else {
			 this.setText("英灵3");
			 this.setPerson(2);
		}
    	Map<String, String> map;
    	switch (loc) {
		case 0:
			map = new HashMap<>();
			map.put("person01", this.getPerson()+"");
			break;
		case 1:
			map = new HashMap<>();
			map.put("person02", this.getPerson()+"");
			break;
		case 2:
			map = new HashMap<>();
			map.put("person03", this.getPerson()+"");
			break;
		default:
			map = new HashMap<>();
			break;
		}
    	PropertiesUtil.setValueForSkills(map);
    }
	public void selectMonster(int loc) {
    	if (this.getText().equals("敌人3")) {
    		 this.setText("敌人1");
    		 this.setMonster(0);
		}else if (this.getText().equals("敌人1")) {
			 this.setText("敌人2");
			 this.setMonster(1);
		}else {
			 this.setText("敌人3");
			 this.setMonster(2);
		}
    	Map<String, String> map;
    	switch (loc) {
		case 0:
			map = new HashMap<>();
			map.put("monster01", this.getMonster()+"");
			break;
		case 1:
			map = new HashMap<>();
			map.put("monster02", this.getMonster()+"");
			break;
		case 2:
			map = new HashMap<>();
			map.put("monster03", this.getMonster()+"");
			break;
		default:
			map = new HashMap<>();
			break;
		}
    	PropertiesUtil.setValueForSkills(map);
    }
	public void selectAccount() {
		int[] FgoRewardArray = GameUtil.strToIntArray(PropertiesUtil.getValueFromConfig("FgoRewardArray"),true);
		String text = this.getText();
		text = text.substring(text.indexOf("账号")+2);
		int account = Integer.valueOf(text);
		int next = 0;
		int size = FgoRewardArray.length;
		for (int i = 0; i < size; i++) {
			if (account ==FgoRewardArray[i] ) {
				next = (++i == size) ? 0 : i;
			}
		}
		this.setText("账号" + FgoRewardArray[next]);
		Map<String, String> map = new HashMap<>();
		map.put("openAccount", "" + FgoRewardArray[next]);
    	PropertiesUtil.setValueForOpen(map);
    }
    public void selectBattleStrategy() {
    	String strategy;
		switch(this.getText()){
			case "T1NoNP" : {
				this.setText("T1SecondNP");
				strategy = GameConstant.GO_SECOD_SKILL_FOR_FIRST;
				break;
			}
			case "T1SecondNP" : {
				this.setText("T1AllNP");
				strategy = GameConstant.GO_ALL_SKILL_FOR_FIRST;
				break;
			}
			case "T1AllNP" : {
				this.setText("T1NoNP");
				strategy = GameConstant.NO_SKILL;
				break;
			}
			case "" : {
				this.setText("T1NoNP");
				strategy = GameConstant.NO_SKILL;
				break;
			}
			default : {
				strategy = "";
				break;
			}
		}
		Map<String, String> map = new HashMap<>();
		map.put("SKILL_STRATEGY", strategy);
    	PropertiesUtil.setValueForSkills(map);
    }
    private void setColor(int style) {
    	 paintcolor();
    	 if (style == pink) {
             COLOR1 = mainStyle01;
             COLOR2 = mainStyle02;
             MY_GRID_COLOR01 = mainStyle01;
             MY_GRID_COLOR02 = mainStyle02;
         }
         if (style == ashen) {
             COLOR1 = active01;
             COLOR2 = active02;
             MY_GRID_COLOR01 = active01;
             MY_GRID_COLOR02 = active02;
         }
         if (style == stone) {
             COLOR1 = stone1;
             COLOR2 = stone2;
             MY_GRID_COLOR01 = stone1;
             MY_GRID_COLOR02 = stone2;
         }
         if (style == sky) {
             COLOR1 = excuteStyle01;
             COLOR2 = excuteStyle02;
             MY_GRID_COLOR01 = excuteStyle01;
             MY_GRID_COLOR02 = excuteStyle02;
         }
    }
    public void setActiveColor() {
    	this.setDisAbleStyle(GameConstant.ACTIVE_STYLE);
    		MY_GRID_COLOR01 = active01;
    		MY_GRID_COLOR02 = active02;
    }
    public void setExcuteColor() {
    	this.setDisAbleStyle(GameConstant.EXCUTE_STYLE);
    	MY_GRID_COLOR01 = excuteStyle01;
    	MY_GRID_COLOR02 = excuteStyle02;
    }
    public void setDisableStyleByStyle() {
    	if (this.getDisAbleStyle()==GameConstant.EXCUTE_STYLE) {
    		setExcuteColor();
		}else if (this.getDisAbleStyle()==GameConstant.ACTIVE_STYLE){
			setActiveColor();
		}else {
			setExcuteColor();
		}
    }
    @Override
	public void setEnabled(boolean b) {
    	if (!b) {
    		MY_FONT_COLOR = Color.WHITE;
		}
		super.setEnabled(b);
	}

	public int getPerson() {
		return person;
	}

	public void setPerson(int person) {
		this.person = person;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public boolean isEnableStatus() {
		return enableStatus;
	}

	public void setEnableStatus(boolean enableStatus) {
		this.enableStatus = enableStatus;
	}
	
	public int getShortcunt01() {
		return shortcunt01;
	}

	public void setShortcunt01(int shortcunt01) {
		this.shortcunt01 = shortcunt01;
	}

	public int getShortcunt02() {
		return shortcunt02;
	}

	public void setShortcunt02(int shortcunt02) {
		this.shortcunt02 = shortcunt02;
	}

	public boolean isExcuteble() {
		return excuteble;
	}

	public void setExcuteble(boolean excuteble) {
		this.excuteble = excuteble;
	}

	public int getMarkCode() {
		return markCode;
	}

	public void setMarkCode(int markCode) {
		this.markCode = markCode;
	}
	
	public int getMonster() {
		return monster;
	}

	public void setMonster(int monster) {
		this.monster = monster;
	}
	
	public boolean isAllowRepeat() {
		return allowRepeat;
	}

	public void setAllowRepeat(boolean allowRepeat) {
		this.allowRepeat = allowRepeat;
	}

	private void paintcolor() {
        setMargin(new Insets(0, 0, 0, 0));
        setFont(new Font("微软雅黑", Font.BOLD, 13));
        setBorderPainted(false);
        setForeground(Color.WHITE);
        setBackground(Color.BLACK);
        setFocusPainted(false);
        setContentAreaFilled(false);
    }
	
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(getMyGraphics(g));
    }
	
	
	private Graphics getMyGraphics(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		float tran = 0.65F;
        int height = getHeight();
        int with = getWidth();
        if (getModel().isRollover()||getModel().isSelected()) { //鼠标离开/进入时的透明度改变量
            tran = 0.85F;
        }
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        /* GradientPaint是颜色渐变类 */
        GradientPaint p1;
        GradientPaint p2;
        if (getModel().isPressed()) {
            p1 = new GradientPaint(0, 0, new Color(0, 0, 0), 0, height, new Color(100, 100, 100), true);
            p2 = new GradientPaint(0, 1, new Color(0, 0, 0, 50), 0, height, new Color(255, 255, 255, 100), true);
        } else {
            p1 = new GradientPaint(0, 0, new Color(100, 100, 100), 0, height, new Color(0, 0, 0), true);
            p2 = new GradientPaint(0, 1, new Color(255, 255, 255, 100), 0, height, new Color(0, 0, 0, 50), true);
        }
        RoundRectangle2D.Float r2d = new RoundRectangle2D.Float(0, 0, with - 1, height - 1, radius, radius);
        // 最后两个参数数值越大，按钮越圆，以下同理
        Shape clip = g2d.getClip();
        g2d.clip(r2d);
        GradientPaint gp;
       
        if (!getModel().isEnabled()) {
        	gp = new GradientPaint(0.0F, 0.0F, MY_GRID_COLOR01, 0.0F, height* 1/5, MY_GRID_COLOR02, true);
        	this.setUI(new BasicButtonUI() //如果你的是windows风格的界面,这里new一个
        	        {    
        		//com.sun.java.swing.plaf.windows.WindowsButtonUI
                @Override
                protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text)
                {
                    AbstractButton b = ((AbstractButton) c);
                    ButtonModel model = b.getModel();
                    if(!model.isEnabled())
                    {
                        FontMetrics fm = SwingUtilities2.getFontMetrics(c, g);
                        int mnemonicIndex = b.getDisplayedMnemonicIndex();
                        g.setColor(MY_FONT_COLOR); //你的字体颜色
                        SwingUtilities2.drawStringUnderlineCharAt(c, g,text, mnemonicIndex,
                                                      textRect.x + getTextShiftOffset(),
                                                      textRect.y + fm.getAscent() + getTextShiftOffset());
                    }else {
                    	super.paintText(g, c, textRect, text);
					}
                }
            });
		}else {
			 if (getSkillsStatus()==2) {
		        	gp = new GradientPaint(0.0F, 0.0F, skillColor01, 0.0F, height / 2, skillColor01, true);
				} else if (getSkillsStatus()==1 ) {
					gp = new GradientPaint(0.0F, 0.0F, skillColor02, 0.0F, height / 2, skillColor02, true);
				}else {
					gp = new GradientPaint(0.0F, 0.0F, COLOR1, 0.0F, height / 2, COLOR2, true);
				}
		}
       
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, tran));
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, with, height);
        g2d.setClip(clip);
        g2d.setPaint(p1);
        g2d.drawRoundRect(0, 0, with - 3, height - 3, radius, radius);
        g2d.setPaint(p2);
        g2d.drawRoundRect(1, 1, with - 3, height - 3, radius, radius);
        g2d.dispose();
        return g;
	}
	
	public String getFromPerson() {
		return fromPerson;
	}

	public void setFromPerson(String fromPerson) {
		this.fromPerson = fromPerson;
	}

	public int getFromSkill() {
		return fromSkill;
	}

	public void setFromSkill(int fromSkill) {
		this.fromSkill = fromSkill;
	}

    
    public int getSkillsStatus() {
		return skillsStatus;
	}

	public void setSkillsStatus(int skillsStatus) {
		this.skillsStatus = skillsStatus;
	}
	
	public Color getMY_FONT_COLOR() {
		return MY_FONT_COLOR;
	}

	public void setMY_FONT_COLOR(Color mY_FONT_COLOR) {
		MY_FONT_COLOR = mY_FONT_COLOR;
	}

	public List<String> getChangeList() {
		return changeList;
	}

	public void setChangeList(List<String> changeList) {
		this.changeList = changeList;
	}

	public String getSaveFileString() {
		return saveFileString;
	}

	public void setSaveFileString(String saveFileString) {
		this.saveFileString = saveFileString;
	}

	public String getChangeListKeyName() {
		return changeListKeyName;
	}

	public void setChangeListKeyName(String changeListKeyName) {
		this.changeListKeyName = changeListKeyName;
	}

	@Override
	public void run() {
			try {
				this.setExcuteColor();
				if (!this.allowRepeat) {
					JIntellitype.getInstance().unregisterHotKey(this.getMarkCode());
				}
				runMethod();
			} catch (FgoNeedStopException e) {
				LOGGER.info(e.getMessage());
			} catch (Exception e) {
				LOGGER.error(GameUtil.getStackMsg(e));
			} finally {
			}
			if (isExcuteble()) {
				this.setEnabled(true);
				if (!this.isEnableStatus()) {
					this.setText(this.getText().replace(" (执行中)", " (已完成)"));
				}
			}else {
				
				if (this.getText().equals("选择结果")) {
					this.setExcuteColor();
				}else {
					this.setActiveColor();
				}
				if (!this.allowRepeat) {
					JIntellitype.getInstance().registerHotKey(this.getMarkCode(), this.getShortcunt01(), this.getShortcunt02());
				}
			}
			this.lightForAwhile();
	}
	public static void main(String[] args) {
		WuNa.instance().alwaysClick();
	}
}
