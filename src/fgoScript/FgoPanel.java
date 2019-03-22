package fgoScript;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingConstants;

import org.apache.commons.lang3.StringUtils;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;

import aoshiScript.entity.WuNa;
import fgoScript.entity.Gudazi;
import fgoScript.entity.ZButton;
import fgoScript.entity.Zpanel;
import fgoScript.service.TimerManager;
import fgoScript.util.GameUtil;
import fgoScript.util.PropertiesUtil;
//创建全局快捷键
public class FgoPanel extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static FgoPanel p;
	public static FgoPanel instance() {
		if (p == null) {
			p = new FgoPanel();
			return p;
		}else {
			return p;
		}
	}
    private Icon wallpaper; 
    private HotkeyListener listener;
    protected void paintComponent(Graphics g) {  
        if (null != wallpaper) {  
            processBackground(g);  
        }  
    }  
  
    public void setBackground(Icon wallpaper) {  
        this.wallpaper = wallpaper;  
        this.repaint();  
    }  
  
    private void processBackground(Graphics g) {  
        ImageIcon icon = (ImageIcon) wallpaper;  
        Image image = icon.getImage();  
        int cw = getWidth();  
        int ch = getHeight();  
        int iw = image.getWidth(this);  
        int ih = image.getHeight(this);  
        int x = 0;  
        int y = 0;  
        while (y <= ch) {  
            g.drawImage(image, x, y, this);  
            x += iw;  
            if (x >= cw) {  
                x = 0;  
                y += ih;  
            }  
        }  
    }  
//	private static final Logger LOGGER = LogManager.getLogger(FgoPanel.class);
	private ZButton[] bts = {
			new ZButton("(小号)材料所有号",JIntellitype.MOD_SHIFT, (int) 'P',true, false, ZButton.pink) {
				private static final long serialVersionUID = 3981539681889014623L;
				@Override
				public void runMethod() throws Exception {
					new Gudazi().openAllFGO();
				}
			},
			new ZButton("(小号+主号)领取奖励抽免费池",JIntellitype.MOD_SHIFT, (int) 'L',true, false, ZButton.pink) {
				private static final long serialVersionUID = 8200576435327725059L;
				@Override
				public void runMethod() throws Exception {
					new Gudazi().allRewardAndRoll();
				}
			},
			new ZButton("(小号+主号)签到所有号",JIntellitype.MOD_SHIFT, (int) 'S',true, false, ZButton.pink) {
				private static final long serialVersionUID = 53933841194261802L;
				@Override
				public void runMethod() throws Exception {
					new Gudazi().signAllFGO();
				}
			},
			new ZButton("(小号+主号)执行所有",JIntellitype.MOD_SHIFT, (int) 'T',true, false, ZButton.pink) {
				private static final long serialVersionUID = 2069565531679104865L;
				@Override
				public void runMethod() throws Exception {
					new Gudazi().goAll();
				}
			},
			new ZButton("开启账号",JIntellitype.MOD_SHIFT, (int) 'O',true, false, ZButton.pink) {
				private static final long serialVersionUID = 432750026975346042L;
				@Override
				public void runMethod() throws Exception {
					new Gudazi().openEvent();
				}
			},
			new ZButton("(账号10)开始刷活动(Shift+K)",JIntellitype.MOD_SHIFT, (int) 'K',false, false, ZButton.pink) {
				private static final long serialVersionUID = 1369499050729104861L;
				@Override
				public void runMethod() throws Exception {
					new Gudazi().eventingFgo();
				}
			},
			new ZButton("(账号10)无限刷qp",JIntellitype.MOD_SHIFT, (int) 'Q',true, false, ZButton.pink) {
				private static final long serialVersionUID = -7555639858773795825L;
				@Override
				public void runMethod() throws Exception {
					new Gudazi().mainAccountQP40();
				}
			},
			new ZButton("(账号10)无限刷EXP",JIntellitype.MOD_SHIFT, (int) 'P',true, false, ZButton.pink) {
				private static final long serialVersionUID = 6504858991507730448L;
				@Override
				public void runMethod() throws Exception {
					new Gudazi().mainAccountEXP40();
				}
			},
			new ZButton("获取鼠标位置颜色(Shift+E)",JIntellitype.MOD_SHIFT, (int) 'E',false, false, ZButton.pink) {
				private static final long serialVersionUID = -7389326247723796445L;
				@Override
				public void runMethod() {
					new Gudazi().showPositionAndColor();
				}
			},
			new ZButton("自动战斗(Shift+Q)",JIntellitype.MOD_SHIFT, (int) 'Q',false, false, ZButton.pink) {
				private static final long serialVersionUID = -7389326247723796445L;
				@Override
				public void runMethod() throws Exception {
					new Gudazi().onlyFight();
				}
			},
			new ZButton("自动按键(Alt+N)设置(Alt+1)",JIntellitype.MOD_ALT, (int) 'N', false, false, ZButton.pink) {
				private static final long serialVersionUID = -1900866258318497377L;
				@Override
				public void runMethod() {
					WuNa.instance().alwaysClick();
				}
			},
			new ZButton("skills01",0," ", JIntellitype.MOD_SHIFT, (int) 'i', true, true, ZButton.pink) {
				private static final long serialVersionUID = 7839671713774463047L;
				@Override
				public void runMethod() {
					this.activeButton();
				}
			},new ZButton("skills01",1," ", JIntellitype.MOD_SHIFT, (int) 'i', true, true, ZButton.pink) {
				private static final long serialVersionUID = -7414070501363863901L;
				@Override
				public void runMethod() {
					this.activeButton();
				}
			},new ZButton("skills01",2," ", JIntellitype.MOD_SHIFT, (int) 'i', true, true, ZButton.pink) {
				private static final long serialVersionUID = 4355835973077552249L;

				@Override
				public void runMethod() {
					this.activeButton();
				}
			},new ZButton("skills02",0," ", JIntellitype.MOD_SHIFT, (int) 'i', true, true, ZButton.pink) {
				private static final long serialVersionUID = 4947072762437664728L;
				@Override
				public void runMethod() {
					this.activeButton();
				}
			},new ZButton("skills02",1," ", JIntellitype.MOD_SHIFT, (int) 'i', true, true, ZButton.pink) {
				private static final long serialVersionUID = 7846182532074063143L;
				@Override
				public void runMethod() {
					this.activeButton();
				}
			},new ZButton("skills02",2," ", JIntellitype.MOD_SHIFT, (int) 'i', true, true, ZButton.pink) {
				private static final long serialVersionUID = 9020476815603182132L;
				@Override
				public void runMethod() {
					this.activeButton();
				}
			},new ZButton("skills03",0," ", JIntellitype.MOD_SHIFT, (int) 'i', true, true, ZButton.pink) {
				private static final long serialVersionUID = 1575767177788722669L;
				@Override
				public void runMethod() {
					this.activeButton();
				}
			},new ZButton("skills03",1," ", JIntellitype.MOD_SHIFT, (int) 'i', true, true, ZButton.pink) {
				private static final long serialVersionUID = 638308570629886471L;

				@Override
				public void runMethod() {
					this.activeButton();
				}
			},new ZButton("skills03",2," ", JIntellitype.MOD_SHIFT, (int) 'i', true, true, ZButton.pink) {
				private static final long serialVersionUID = 8648030433776394191L;

				@Override
				public void runMethod() {
					this.activeButton();
				}
			},new ZButton("monster01", JIntellitype.MOD_SHIFT, (int) 'i', true, true, ZButton.pink) {
				private static final long serialVersionUID = -2891221341872701498L;
				@Override
				public void runMethod() {
					this.selectMonster(0);
				}
			}
			,new ZButton("monster02", JIntellitype.MOD_SHIFT, (int) 'i', true, true, ZButton.pink) {
				private static final long serialVersionUID = -3891221341872701498L;
				@Override
				public void runMethod() {
					this.selectMonster(1);
				}
			}
			,new ZButton("monster03", JIntellitype.MOD_SHIFT, (int) 'i', true, true, ZButton.pink) {
				private static final long serialVersionUID = -7491221341872701498L;
				@Override
				public void runMethod() {
					this.selectMonster(2);
				}
			}
			,new ZButton("person01", JIntellitype.MOD_SHIFT, (int) 'i', true, true, ZButton.pink) {
				private static final long serialVersionUID = 8554261580079318513L;

				@Override
				public void runMethod() {
					this.selectPerson(0);
				}
			},new ZButton("person02", JIntellitype.MOD_SHIFT, (int) 'i', true, true, ZButton.pink) {
				private static final long serialVersionUID = -7891221341872701498L;
				@Override
				public void runMethod() {
					this.selectPerson(1);
				}
			},new ZButton("person03", JIntellitype.MOD_SHIFT, (int) 'i', true, true, ZButton.pink) {
				private static final long serialVersionUID = -1891221341872701498L;
				@Override
				public void runMethod() {
					this.selectPerson(2);
				}
			}
			,new ZButton("终止", JIntellitype.MOD_ALT, KeyEvent.VK_0, true, true, ZButton.pink) {
				private static final long serialVersionUID = -7491221341872701498L;
				@Override
				public void runMethod() {
					GameUtil.shutDownALl();
				}
			},new ZButton("暂停", JIntellitype.MOD_ALT, KeyEvent.VK_9, true, true, ZButton.pink) {
				private static final long serialVersionUID = -7491221341872701498L;
				@Override
				public void runMethod() {
					GameUtil.waitOrContinue(this);
				}
			},new ZButton("超时", JIntellitype.MOD_SHIFT, (int) 'i', true, true, ZButton.pink) {
				private static final long serialVersionUID = -7491221341872701498L;
				@Override
				public void runMethod() {
					GameUtil.setFORCE_OUTTIME(true);
				}
			},new ZButton("新开", JIntellitype.MOD_SHIFT, (int) 'i', true, true, ZButton.pink) {
				private static final long serialVersionUID = -7491221341872701498L;
				@Override
				public void runMethod() {
					this.selectIfRestart();
				}
			},new ZButton("坐标",JIntellitype.MOD_ALT, (int) KeyEvent.VK_1,false, false, ZButton.pink) {
				private static final long serialVersionUID = 6504858991507730448L;
				@Override
				public void runMethod() throws Exception {
					WuNa.instance().configClick(this);
				}
			},new ZButton("清空",JIntellitype.MOD_SHIFT, (int) 'P',true, true, ZButton.pink) {
				private static final long serialVersionUID = 6504858991507730448L;
				@Override
				public void runMethod() throws Exception {
					WuNa.instance().falshClick(this);
				}
			},new ZButton("解除激活",JIntellitype.MOD_SHIFT, (int) 'P',true, true, ZButton.pink) {
				private static final long serialVersionUID = 6504858991507730448L;
				@Override
				public void runMethod() throws Exception {
					deActiveAll();
				}
			}
	};
	private Zpanel backPanel = new Zpanel();
	private Zpanel centerPanel = new Zpanel();
	private Zpanel buttonPanel = new Zpanel();
	private Zpanel southPanel01 = new Zpanel();
	private Zpanel southPanel02 = new Zpanel();
	private Zpanel southPanel03 = new Zpanel();
	private Zpanel southPanel04 = new Zpanel();
	private Zpanel eastPanel01 = new Zpanel();
	
	// 初始化
	public FgoPanel() {
		init();
	}
	// 初始化
	private void init() {
		// 面板对象
		backPanel.setLayout(new BorderLayout());
		centerPanel.setLayout(null);
		buttonPanel.setLayout(new GridLayout(2, 1));
		southPanel01.setLayout(new GridLayout(1, 9));
		southPanel02.setLayout(new GridLayout(1, 3));
		southPanel03.setLayout(new GridLayout(1, 3));
		southPanel04.setLayout(new GridLayout(1, 1));
		eastPanel01.setLayout(new GridLayout(1, 3));
		// 添加按钮
		ZButton jbTemp = null;
		int size = bts.length;
		int skillcount = 0;
		int len = 2;
		for (int i = 0; i < size; i++) {
			jbTemp = bts[i];
			jbTemp.setMarkCode(i);
			if (" ".equals(jbTemp.getText())) {
				jbTemp.setText(jbTemp.getSkilStateText());
				jbTemp.addActionListener(this);
				southPanel01.add(jbTemp);
				if (2 < skillcount && skillcount < 6) {
					jbTemp.setSkillsStatus(1);
				}else {
					jbTemp.setSkillsStatus(2);
				}
				skillcount++;
			}else if (jbTemp.getText().indexOf("monster")!=-1) {
				jbTemp.setText(jbTemp.getToMonsterText());
				jbTemp.addActionListener(this);
				southPanel03.add(jbTemp);
			}else if (jbTemp.getText().indexOf("person")!=-1) {
				jbTemp.setText(jbTemp.getToPersonText());
				jbTemp.addActionListener(this);
				southPanel02.add(jbTemp);
			}else if ("终止".equals(jbTemp.getText())) {
				jbTemp.setText("终止");
				jbTemp.addActionListener(this);
				jbTemp.setAllowRepeat(true);
				JIntellitype.getInstance().registerHotKey(jbTemp.getMarkCode(), jbTemp.getShortcunt01(), jbTemp.getShortcunt02());
				southPanel04.add(jbTemp);
				
			}else if ("暂停".equals(jbTemp.getText())) {
				jbTemp.setText("暂停");
				jbTemp.addActionListener(this);
				jbTemp.setAllowRepeat(true);
				JIntellitype.getInstance().registerHotKey(jbTemp.getMarkCode(), jbTemp.getShortcunt01(), jbTemp.getShortcunt02());
				southPanel04.add(jbTemp);
				
			}else if ("超时".equals(jbTemp.getText())) {
				jbTemp.setText("超时");
				jbTemp.addActionListener(this);
				southPanel04.add(jbTemp);
			}
			else if ("坐标".equals(jbTemp.getText())) {
				jbTemp.setText("点击设置");
				jbTemp.addActionListener(this);
				eastPanel01.add(jbTemp);
			}
			else if ("清空".equals(jbTemp.getText())) {
				jbTemp.setText("重置");
				jbTemp.addActionListener(this);
				eastPanel01.add(jbTemp);
			}
			else if ("解除激活".equals(jbTemp.getText())) {
				jbTemp.setText("解除激活");
				jbTemp.addActionListener(this);
				jbTemp.setVerticalTextPosition(SwingConstants.CENTER);
				eastPanel01.add(jbTemp);
			}
			else if ("新开".equals(jbTemp.getText())) {
				jbTemp.setText(jbTemp.getReStartFlag());
				jbTemp.addActionListener(this);
				southPanel04.add(jbTemp);
			}
			else {
				if ("自动按键(Alt+N)设置(Alt+1)".equals(jbTemp.getText())) {
					jbTemp.setAllowRepeat(true);
				}
				if ("开启账号".equals(jbTemp.getText())) {
					jbTemp.setBounds(5, 5 + (i * (30+len)), 200, 25+len);
				}else {
					jbTemp.setBounds(5, 5 + (i * (30+len)), 320, 25+len);
				}
				jbTemp.addActionListener(this);
				centerPanel.add(jbTemp);
			}
		}
		jbTemp = new ZButton("",JIntellitype.MOD_SHIFT, (int) 'P',true, true, ZButton.pink) {
			private static final long serialVersionUID = 8438279990543323489L;
			@Override
			public void runMethod() throws Exception {
				this.selectAccount();
			}
		};
		String openAccountStr = PropertiesUtil.getValueFromOpenFile("openAccount");
		int[] FgoRewardArray = GameUtil.strToIntArray(GameUtil.getValueFromConfig("FgoRewardArray"),true);
		int account =	StringUtils.isBlank(openAccountStr)?
					FgoRewardArray[0]:
					Integer.valueOf(openAccountStr);
		jbTemp.setText("账号" + account);
		jbTemp.setBounds(210, 5 + (4 * (30+len)), 115, 25+len);
		jbTemp.addActionListener(this);
		centerPanel.add(jbTemp);
		//显示背景按钮
		ZButton showPicBt = new ZButton("显示背景",JIntellitype.MOD_SHIFT, (int) 'P',true, true, ZButton.pink) {
			private static final long serialVersionUID = 6504858991507730448L;
			@Override
			public void runMethod() throws Exception {
				showBackGround();
			}
		};
		//显示背景按钮
		ZButton changePicBt = new ZButton("切换背景",JIntellitype.MOD_SHIFT, (int) 'P',true, true, ZButton.pink) {
			private static final long serialVersionUID = 6504858991507730448L;
			@Override
			public void runMethod() throws Exception {
				changeGround();
			}
		};
		showPicBt.addActionListener(this);
		changePicBt.addActionListener(this);
		eastPanel01.add(showPicBt);
		eastPanel01.add(changePicBt);
		// 添加热键监听器
		listener = new HotkeyListener() {
			public void onHotKey(int markCode) {
				ZButton zbt = bts[markCode];
				new Thread(zbt).start();
			}
		};
		centerPanel.setOpaque(false);
		buttonPanel.setOpaque(false);
		southPanel01.setOpaque(false);
		southPanel02.setOpaque(false);
		southPanel03.setOpaque(false);
		southPanel04.setOpaque(false);
		eastPanel01.setOpaque(false);
		buttonPanel.add(southPanel01);
		buttonPanel.add(southPanel04);
		buttonPanel.add(southPanel02);
		buttonPanel.add(southPanel03);
		buttonPanel.setBounds(5, 10 + (12 * (30+len)), 320, 55+len);
		centerPanel.add(buttonPanel);
		eastPanel01.setBounds(5, 5 + (11 * (30+len)), 320, 25+len);
		centerPanel.add(eastPanel01);
		backPanel.add(centerPanel);
		backPanel.setBackground(GameUtil.getBackGroundPreFix((int)(333*1.1), (int)(470*1.1)));
		JIntellitype.getInstance().addHotKeyListener(listener);
		this.add(backPanel);
		this.setTitle("FGO辅助大师-Jai制作");
		this.setSize(335, 500);
		this.setLocation(1537, 516);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		
		// 启动定时任务
		new TimerManager();
	}  
	private void showBackGround() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				centerPanel.setVisible(false);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
				}
				centerPanel.setVisible(true);
			}
		}).start();
	}
	private void changeGround() {
		backPanel.setBackground(GameUtil.getBackGroundPreFix((int)(333*1.1), (int)(470*1.1)));
	}
	// 事件监听
	@Override
	public void actionPerformed(ActionEvent e) {
		ZButton btTemp = (ZButton) e.getSource();
		if (e.getActionCommand().indexOf(btTemp.getText()) != -1) {
			btTemp.setEnabled(btTemp.isEnableStatus());
			if (btTemp.isExcuteble()) {
				btTemp.setExcuteColor();
				new Thread(btTemp).start();
				if (!btTemp.isEnableStatus()) {
					String text = btTemp.getText();
					if (text.lastIndexOf("(") != -1 && text.lastIndexOf("(") != 0) {
						btTemp.setText(text.substring(0, text.lastIndexOf("(")) + "(执行中)");
					}else {
						btTemp.setText(text + " (执行中)");
					}
				}
			}else {
				if ("点击设置".equals(btTemp.getText())) {
					btTemp.setText("选择条件");
				}else {
					btTemp.setText(btTemp.getText()+" (已激活)");
				}
				btTemp.setActiveColor();
				JIntellitype.getInstance().registerHotKey(btTemp.getMarkCode(), btTemp.getShortcunt01(), btTemp.getShortcunt02());
			}
		}
	}
	
	public ZButton[] getBts() {
		return bts;
	}

	public void setBts(ZButton[] bts) {
		this.bts = bts;
	}
	
	public HotkeyListener getListener() {
		return listener;
	}

	public void setListener(HotkeyListener listener) {
		this.listener = listener;
	}
	private void deActiveAll() {
		ZButton[] zts = this.getBts();
		ZButton btTemp;
		String textTemp;
		for (int i = 0; i < zts.length; i++) {
			btTemp = zts[i];
			if (!btTemp.isExcuteble()) {
				JIntellitype.getInstance().unregisterHotKey(btTemp.getMarkCode());
				btTemp.setEnabled(true);
				textTemp = btTemp.getText();
				btTemp.setText(textTemp.replace(" (已激活)", ""));
			}
		}
//		bt.lightForAwhile();
	}
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				p = new FgoPanel();
			}
		});
	}
}