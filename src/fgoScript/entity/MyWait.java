package fgoScript.entity;

public class MyWait implements Runnable {
	private static MyWait mw;
	private static ZButton bt ;
	private ZButton myBt;
	public static MyWait instance(ZButton bt) {
		if (mw == null || !bt.equals(MyWait.bt)) {
			mw = new MyWait(bt);
			mw.setMyBt(bt);
		}
		return mw;
	}
	public MyWait() {
		super();
	}
	public MyWait(ZButton bt) {
		super();
		MyWait.bt = bt;
	}

	public MyWait getMw() {
		return mw;
	}
	
	public ZButton getMyBt() {
		return myBt;
	}
	public void setMyBt(ZButton myBt) {
		this.myBt = myBt;
	}
	public void setMw(MyWait mw) {
		MyWait.mw = mw;
	}
	

	public ZButton getBt() {
		return bt;
	}

	public void setBt(ZButton bt) {
		MyWait.bt = bt;
	}
	@Override
	public void run() {
		if (myBt.isEnabled()) {
			myBt.setEnabled(false);
			myBt.setExcuteColor();
    		try {
    			Thread.sleep(300);
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    		myBt.setEnabled(true);
		}else {
			myBt.setEnabled(true);
			if (myBt.isExcuteble()) {
				myBt.setExcuteColor();
			}else {
				myBt.setActiveColor();
			}
    		try {
    			Thread.sleep(300);
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    		myBt.setEnabled(false);
		}
	}

}
