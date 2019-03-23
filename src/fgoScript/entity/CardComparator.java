package fgoScript.entity;

import java.util.Comparator;

import fgoScript.constant.GameConstant;

public class CardComparator {
	public static Comparator<CommonCard> getBgrComparotor() {
		return (o1, o2) -> {
            if (o1.getCardColor().equals(o2.getCardColor())) {
                return 0;
            } else if (o1.getCardColor().equals(GameConstant.BLUE)) {
                return -1;
            } else if (o1.getCardColor().equals(GameConstant.RED)) {
                return 1;
            } else if (o1.getCardColor().equals(GameConstant.GREEN)) {
                if (o2.getCardColor().equals(GameConstant.BLUE)) {
                    return 1;
                } else if (o1.getCardColor().equals(GameConstant.RED)) {
                    return -1;
                }
            }
            return 0;
        };
	}
	public static Comparator<CommonCard> getRbgComparotor() {
		return (o1, o2) -> {
            if (o1.getCardColor().equals(o2.getCardColor())) {
                return 0;
            } else if (o1.getCardColor().equals(GameConstant.RED)) {//
                return -1;
            } else if (o1.getCardColor().equals(GameConstant.GREEN)) {//
                return 1;
            } else if (o1.getCardColor().equals(GameConstant.BLUE)) {//
                if (o2.getCardColor().equals(GameConstant.RED)) {//
                    return 1;
                } else if (o1.getCardColor().equals(GameConstant.GREEN)) {//
                    return -1;
                }
            }
            return 0;
        };
	}
}
