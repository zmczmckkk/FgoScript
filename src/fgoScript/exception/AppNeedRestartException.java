package fgoScript.exception;

public class AppNeedRestartException extends Exception {
	private static final long serialVersionUID = 1877932104145533378L;

	@Override
	public String getMessage() {
		return "重启模拟器，重新执行脚本！";
	}
}
