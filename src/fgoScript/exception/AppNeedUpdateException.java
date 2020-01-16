package fgoScript.exception;

public class AppNeedUpdateException extends Exception {
	private static final long serialVersionUID = 1877932104145533378L;

	@Override
	public String getMessage() {
		return "更新FGO，重新执行脚本！";
	}
}
