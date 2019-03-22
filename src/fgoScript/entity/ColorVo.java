package fgoScript.entity;

public class ColorVo {
	private String color;
	private int value;
	
	public ColorVo(String color, int value) {
		super();
		this.color = color;
		this.value = value;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
}
