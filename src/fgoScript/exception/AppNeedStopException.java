package fgoScript.exception;

import org.apache.commons.lang3.StringUtils;

public class AppNeedStopException extends Exception {
	private static final long serialVersionUID = 3087643542341510436L;
	private String stopReason;
	
	public String getStopReason() {
		return stopReason;
	}

	public void setStopReason(String stopReason) {
		this.stopReason = stopReason;
	}

	public AppNeedStopException(String stopReason) {
		super();
		this.stopReason = stopReason;
	}
	public AppNeedStopException() {
		super();
	}
	@Override
	public String getMessage() {
		return "程序终止运行!" + (StringUtils.isBlank(getStopReason())?"":" 终止原因：" + getStopReason());
	}
	
}
