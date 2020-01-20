package fgoScript.exception;

public class AppNeedNextException extends Exception {
	private static final long serialVersionUID = -6206300050263108056L;
	@Override
	public String getMessage() {
		return "该原始坐标点下，不匹配坐标颜色组，请重新定位！";
	}
	
}
