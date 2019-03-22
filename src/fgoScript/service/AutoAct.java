package fgoScript.service;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.List;

import fgoScript.entity.PointColor;
import fgoScript.util.GameUtil;

public abstract class AutoAct {
	// 点色判断集合
	private List<PointColor> pcList;
	// 结束点色
	private List<PointColor> finishPCList;
	// 当前点色
	private PointColor pcWait;
	
	private boolean GO_FLAG;
	
	public PointColor getPcWait() {
		return pcWait;
	}
	

	public boolean isGO_FLAG() {
		return GO_FLAG;
	}


	public void setGO_FLAG(boolean gO_FLAG) {
		GO_FLAG = gO_FLAG;
	}


	public void setPcWait(PointColor pcWait) {
		this.pcWait = pcWait;
	}
	public List<PointColor> getPcList() {
		return pcList;
	}
	public void setPcList(List<PointColor> pcList) {
		this.pcList = pcList;
	}

	public List<PointColor> getFinishPCList() {
		return finishPCList;
	}
	public void setFinishPCList(List<PointColor> finishPCList) {
		this.finishPCList = finishPCList;
	}

	public AutoAct(List<PointColor> pcList, List<PointColor> finishPCList) {
		super();
		this.pcList = pcList;
		this.finishPCList = finishPCList;
	}
	/**
	 * 自动情景行动累
	 * @throws Exception
	 * @throws AWTException 
	 */
	public boolean autoAct() throws Exception {
		boolean match=false;
		int size = pcList.size() * 10;
		Point clickPoint = null;
		for (int i = 0; i < size; i++) {
			pcWait = GameUtil.waitUntilOneColor(pcList);
			if (pcWait!=null) {
				doSomeThing();
				clickPoint = pcWait.getClickPoint();
				if (clickPoint!=null) {
					GameUtil.mouseMoveByPoint(pcWait.getClickPoint());
					GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
				}
				PointColor pcTemp;
				for (int j = 0; j < finishPCList.size(); j++) {
					pcTemp = finishPCList.get(j);
					match = GameUtil.isEqualColor(pcWait.getColor(), pcTemp.getColor());
					if (match==true) {
						break;
					}
				}
				if (match) {
					break;
				}
			} else {
				return false;
			}
		
		}
		return true;
	}
	public abstract void doSomeThing() throws Exception;
}
