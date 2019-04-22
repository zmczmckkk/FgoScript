package fgoScript.entity;

import java.awt.Point;

public class CommonCard { 
	//角色名
	private String roleName;
	private int cardColor;
	private boolean isWeak;
	//点击点
	private Point pLoc;
	//构造函数
	public CommonCard() {
		super();
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public int getCardColor() {
		return cardColor;
	}

	public void setCardColor(int cardColor) {
		this.cardColor = cardColor;
	}

	public boolean isWeak() {
		return isWeak;
	}
	public void setWeak(boolean isWeak) {
		this.isWeak = isWeak;
	}
	public Point getpLoc() {
		return pLoc;
	}
	public void setpLoc(Point pLoc) {
		this.pLoc = pLoc;
	}
	
}
