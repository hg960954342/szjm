package com.prolog.eis.model.eis;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

@Table("port_info")
public class PortInfo {

	@Id
	@Column("id")
	@ApiModelProperty("主键")
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
	private int id;
	
	@Column("port_type")
	@ApiModelProperty("出库口类型 （1入库口 2出库口）")
	private int portType;
	
	@Column("task_type")
	@ApiModelProperty("任务类型 （1任务托 2包材 3 空拖 4质检 ）")
	private int taskType;
	
	@Column("work_type")
	@ApiModelProperty("工作类型 （1人工 2AGV）")
	private int workType;
	
	@Column("reback")
	@ApiModelProperty("1 可以退回  0 不可退回")
	private int reback;
	
	@Column("show_led")
	@ApiModelProperty("1 显示Led屏 2 0不显示Led屏")
	private int showLed;
	
	@Column("dir_mode")
	@ApiModelProperty("0 不允许切换方向  1自动切换方向 2手动切换方向")
	private int dirMode;
	
	@Column("call_car")
	@ApiModelProperty("是否呼叫agv小车 1呼叫  0 不呼叫")
	private int callCar;
	
	@Column("detection")
	@ApiModelProperty("0 不进行高度检测 1进行高度检测")
	private int detection;
	
	@Column("position")
	@ApiModelProperty("1 西码头 2 7号口 0 其他")
	private int position;
	
	@Column("area")
	@ApiModelProperty("区域 （1 料箱库 2四向库）")
	private int area;
	
	@Column("default_weight")
	@ApiModelProperty("默认重量")
	private Double defaultWeight;
	
	@Column("wms_port_no")
	@ApiModelProperty("wms出库口编号")
	private String wmsPortNo;
	
	@Column("junction_port")
	@ApiModelProperty("接驳口号")
	private String junctionPort;
	
	@Column("layer")
	@ApiModelProperty("层")
	private int layer;
	
	@Column("x")
	@ApiModelProperty("x")
	private int x;
	
	@Column("y")
	@ApiModelProperty("y")
	private int y;
	
	@Column("port_lock")
	@ApiModelProperty("是否锁定 1锁定 2不锁定")
	private int portlock;
	
	@Column("task_lock")
	@ApiModelProperty("是否锁定 1锁定 2不锁定")
	private int taskLock;
	
	@Column("max_ck_count")
	@ApiModelProperty("port口最大出库数量")
	private int maxCkCount;
	
	@Column("max_rk_count")
	@ApiModelProperty("port口最大入库数量")
	private int maxRkCount;
	
	@Column("remarks")
	@ApiModelProperty("备注")
	private String remarks;
	
	@Column("led_ip")
	@ApiModelProperty("备注")
	private String ledIp;
	
	@Column("error_port")
	@ApiModelProperty("是否为异常口  1 异常口 0非异常口")
	private int errorPort;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPortType() {
		return portType;
	}

	public void setPortType(int portType) {
		this.portType = portType;
	}

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	public int getWorkType() {
		return workType;
	}

	public void setWorkType(int workType) {
		this.workType = workType;
	}

	public int getReback() {
		return reback;
	}

	public void setReback(int reback) {
		this.reback = reback;
	}

	public int getShowLed() {
		return showLed;
	}

	public void setShowLed(int showLed) {
		this.showLed = showLed;
	}

	public int getDirMode() {
		return dirMode;
	}

	public void setDirMode(int dirMode) {
		this.dirMode = dirMode;
	}

	public int getCallCar() {
		return callCar;
	}

	public void setCallCar(int callCar) {
		this.callCar = callCar;
	}

	public int getDetection() {
		return detection;
	}

	public void setDetection(int detection) {
		this.detection = detection;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getArea() {
		return area;
	}

	public void setArea(int area) {
		this.area = area;
	}

	public Double getDefaultWeight() {
		return defaultWeight;
	}

	public void setDefaultWeight(Double defaultWeight) {
		this.defaultWeight = defaultWeight;
	}

	public String getWmsPortNo() {
		return wmsPortNo;
	}

	public void setWmsPortNo(String wmsPortNo) {
		this.wmsPortNo = wmsPortNo;
	}

	public String getJunctionPort() {
		return junctionPort;
	}

	public void setJunctionPort(String junctionPort) {
		this.junctionPort = junctionPort;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getPortlock() {
		return portlock;
	}

	public void setPortlock(int portlock) {
		this.portlock = portlock;
	}

	public int getTaskLock() {
		return taskLock;
	}

	public void setTaskLock(int taskLock) {
		this.taskLock = taskLock;
	}

	public int getMaxCkCount() {
		return maxCkCount;
	}

	public void setMaxCkCount(int maxCkCount) {
		this.maxCkCount = maxCkCount;
	}

	public int getMaxRkCount() {
		return maxRkCount;
	}

	public void setMaxRkCount(int maxRkCount) {
		this.maxRkCount = maxRkCount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getLedIp() {
		return ledIp;
	}

	public void setLedIp(String ledIp) {
		this.ledIp = ledIp;
	}

	public int getErrorPort() {
		return errorPort;
	}

	public void setErrorPort(int errorPort) {
		this.errorPort = errorPort;
	}

	public PortInfo(int id, int portType, int taskType, int workType, int reback, int showLed, int dirMode,
			int position, int area, String wmsPortNo, String junctionPort, int layer, int x, int y, int portlock,
			int taskLock, int maxCkCount, int maxRkCount, String remarks, String ledIp, int errorPort) {
		super();
		this.id = id;
		this.portType = portType;
		this.taskType = taskType;
		this.workType = workType;
		this.reback = reback;
		this.showLed = showLed;
		this.dirMode = dirMode;
		this.position = position;
		this.area = area;
		this.wmsPortNo = wmsPortNo;
		this.junctionPort = junctionPort;
		this.layer = layer;
		this.x = x;
		this.y = y;
		this.portlock = portlock;
		this.taskLock = taskLock;
		this.maxCkCount = maxCkCount;
		this.maxRkCount = maxRkCount;
		this.remarks = remarks;
		this.ledIp = ledIp;
		this.errorPort = errorPort;
	}

	public PortInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
}
