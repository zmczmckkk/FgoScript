package fgoScript.entity;

import javax.swing.JButton;

public abstract class ButtonInfo implements Runnable{
	private String buttonName;
	public int shortcunt01;
	public int shortcunt02;
	private boolean excuteble;
	private JButton jbutton;
	public abstract void runMethod();
	
	public ButtonInfo(String buttonName, int shortcunt01, int shortcunt02, boolean excuteble) {
		super();
		this.buttonName = buttonName;
		this.shortcunt01 = shortcunt01;
		this.shortcunt02 = shortcunt02;
		this.excuteble = excuteble;
	}
	
	public JButton getJbutton() {
		
		return jbutton;
	}

	public void setJbutton(JButton jbutton) {
		this.jbutton = jbutton;
	}

	public boolean isExcuteble() {
		return excuteble;
	}

	public void setExcuteble(boolean excuteble) {
		this.excuteble = excuteble;
	}

	public String getButtonName() {
		return buttonName;
	}
	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
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

	@Override
	public void run() {
		runMethod();
		if (isExcuteble()) {
			getJbutton().setEnabled(true);
			getJbutton().setText(getJbutton().getText().replace(" (执行中)", " (已完成)"));
		}
	}
	
}
