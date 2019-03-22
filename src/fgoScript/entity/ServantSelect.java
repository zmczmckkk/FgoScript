package fgoScript.entity;

import java.awt.Point;
import java.util.Calendar;
import java.util.Date;

import fgoScript.constant.GameConstant;

public class ServantSelect {
	private int servantClassify;
	private int roomClassify;
	private static Point point;
	private int count;//刷本次数
	
	private Date time;


	public int getServantClassify() {
		return servantClassify;
	}

	public void setServantClassify(int servantClassify) {
		this.servantClassify = servantClassify;
	}

	public int getRoomClassify() {
		int dayOfWeek = getDayOfWeek();
		int type = 0;
		switch (dayOfWeek) {
		case 1: {
			type = GameConstant.CLASSIFY_LANCER;
			break;
		}
		case 2: {
			type = GameConstant.CLASSIFY_SABER;
			break;
		}
		case 3: {
			type = GameConstant.CLASSIFY_ALL;
			break;
		}
		case 4: {
			type = GameConstant.CLASSIFY_ASSASIN;
			break;
		}
		case 5: {
			type = GameConstant.CLASSIFY_RIDER;
			break;
		}
		case 6: {
			type = GameConstant.CLASSIFY_CASTER;
			break;
		}
		case 7: {
			type = GameConstant.CLASSIFY_ARCHER;
			break;
		}
		}
		roomClassify = type;
		return roomClassify;
	}

	public void setRoomClassify(int roomClassify) {
		this.roomClassify = roomClassify;
	}

	public static Point getPoint() {
		if (point==null) {
			int dayOfWeek = getDayOfWeek();
			Point p = new Point(131, 176);// 颜色：72;75;72 Color c = new Color(72, 75, 72); all
			Point p1 = new Point(196, 176);// 颜色：41;40;41 Color c = new Color(41, 40, 41); saber
			Point p2 = new Point(264, 180);// 颜色：176;189;190 Color c = new Color(176, 189, 190); archer
			Point p3 = new Point(334, 177);// 颜色：26;30;26 Color c = new Color(26, 30, 26); lancer
			Point p4 = new Point(400, 180);// 颜色：67;65;67 Color c = new Color(67, 65, 67); rider
			Point p5 = new Point(468, 179);// 颜色：9;13;9 Color c = new Color(9, 13, 9); caster
			Point p6 = new Point(535, 178);// 颜色：253;250;253 Color c = new Color(253, 250, 253); asssion
//			Point p7 = new Point(598, 180);// 颜色：255;251;255 Color c = new Color(255, 251, 255); basker
//			Point p8 = new Point(669, 186);// 颜色：255;251;255 Color c = new Color(255, 251, 255); four
			
			switch (dayOfWeek) {
				case 1: {
					point = p3;
					break;
				}
				case 2: {
					point = p1;
					break;
				}
				case 3: {
					point = p;
					break;
				}
				case 4: {
					point = p6;
					break;
				}
				case 5: {
					point = p4;
					break;
				}
				case 6: {
					point = p5;
					break;
				}
				case 7: {
					point = p2;
					break;
				}
			}
		}
		return point;
	}

	public static void setPoint(Point point) {
		ServantSelect.point = point;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
	private static int getDayOfWeek() {
	    Calendar calendar = Calendar.getInstance();
        int weekDay;
        weekDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (0==weekDay) {
        	weekDay = 7;
        }
        //日本计时
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour==23) {
        	weekDay =weekDay + 1;
		}
        return weekDay;
	}
	
}
