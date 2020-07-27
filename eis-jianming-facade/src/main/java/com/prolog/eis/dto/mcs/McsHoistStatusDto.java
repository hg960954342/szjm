package com.prolog.eis.dto.mcs;

/**
 * 小车跨层有在提升机在当前层并且为锁定状态是,才会下发小车进提升机   或 出提升机的任务
 * @author yfpisces
 *
 */
public class McsHoistStatusDto {
	/**
	 * 提升机
	 */
	String hoistId;
	
	/**
	 * 提升机当前楼层
	 */
	int layer;
	
	/**
	 * 提升机状态 0未锁定 1锁定
	 */
	int lock;
	
	/**
	 * 提升机状态 1手动 2自动 3故障
	 */
	int status;

	public String getHoistId() {
		return hoistId;
	}

	public void setHoistId(String hoistId) {
		this.hoistId = hoistId;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public int getLock() {
		return lock;
	}

	public void setLock(int lock) {
		this.lock = lock;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
