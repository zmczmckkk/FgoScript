package fgoScript.entity;

import java.util.Comparator;

import fgoScript.constant.GameConstant;

public class CardComparator {
	public static Comparator<CommonCard> getBgrComparotor() {
		return (o1, o2) -> {
            return o1.getCardColor() - o2.getCardColor();
        };
	}
	public static Comparator<CommonCard> getRgbComparotor() {
		return (o1, o2) -> {
            return o2.getCardColor() - o1.getCardColor();
        };
	}
}
