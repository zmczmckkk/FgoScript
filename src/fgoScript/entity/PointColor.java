package fgoScript.entity;

import java.awt.Color;
import java.awt.Point;

public class PointColor {
	private String name;
	private Point point;
	private Color color;
	private boolean equal;
	private Point clickPoint;
	
	
	public PointColor(Point point, Color color, Point clickPoint, boolean equal) {
		super();
		this.point = point;
		this.color = color;
		this.clickPoint = clickPoint;
		this.equal = equal;
	}
	public PointColor(Point point, Color color, boolean equal) {
		super();
		this.point = point;
		this.color = color;
		this.equal = equal;
	}
	public PointColor(Point point, Color color, boolean equal,String name) {
		super();
		this.point = point;
		this.color = color;
		this.equal = equal;
		this.name = name;
	}
	public Point getPoint() {
		return point;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isEqual() {
		return equal;
	}
	public void setEqual(boolean equal) {
		this.equal = equal;
	}
	public void setPoint(Point point) {
		this.point = point;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
//	private int getDayOfWeek() {
//	    Calendar calendar = Calendar.getInstance();
//        int weekDay;
//        weekDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
//        if ("0".equals(weekDay)) {
//        	weekDay = 7;
//        }
//        return weekDay;
//	}
	public Point getClickPoint() {
		return clickPoint;
	}
	public void setClickPoint(Point clickPoint) {
		this.clickPoint = clickPoint;
	}
	
}
