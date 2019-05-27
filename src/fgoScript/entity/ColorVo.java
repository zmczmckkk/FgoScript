package fgoScript.entity;

public class ColorVo {
	private int color;
	private int value;
	
	public ColorVo(int color, int value) {
		super();
		this.color = color;
		this.value = value;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
}
