package com.prolog.eis.dto.eis;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

public class SxStoreDto {

	@ApiModelProperty("四向库库存表")
	private int id;		//四向库库存表
	
	@ApiModelProperty("容器编号")
	private String containerNo;		//容器编号
	
	@ApiModelProperty("子容器编号")
	private String containerSubNo;		//子容器编号
	
	@ApiModelProperty("货位ID")
	private Integer storeLocationId;		//货位ID
	
	@ApiModelProperty("托盘任务类型(-1.空托盘、10.融合入融合库（原点上半部分(无预留货位)）\\r\\n\" + \n" + 
			"    		\"20.融合入理货台、\\r\\n\" + \n" + 
			"    		\"30.融合入暂存区 (原点上（预留货位）)\\r\\n\" + \n" + 
			"    		\"40.HUB进HUB库（原点上(无预留货位)）\\r\\n\" + \n" + 
			"    		\"50.HUB进暂存区(原点上（有预留货位）)\\r\\n\" + \n" + 
			"    		\"60.MIT入库（原点下(无预留货位)）\\r\\n\" + \n" + 
			"    		\"70.MIT入二楼理货台(原点下(有预留货位))\\r\\n\" + \n" + 
			"    		\"80.MIT到暂存区(原点下(有预留货位))\\r\\n\" + \n" + 
			"    		\"90.MIT入库并出库\\r\\n\")")
	private int taskType;		
	
	@ApiModelProperty("任务属性1（路由）")
	private String taskProperty1;		//任务属性1（路由）
	
	@ApiModelProperty("任务属性2（时效）")
	private String taskProperty2;		//任务属性2（时效）
	
	@ApiModelProperty("业务属性1")
	private String businessProperty1;
	
	@ApiModelProperty("业务属性2")
	private String businessProperty2;
	
	@ApiModelProperty("业务属性3")
	private String businessProperty3;
	
	@ApiModelProperty("业务属性4")
	private String businessProperty4;
	
	@ApiModelProperty("业务属性5")
	private String businessProperty5;
	
	@ApiModelProperty("库存状态(10：入库中、 20：已上架、 30：出库中、31:待出库、40：移位中)")
	private int storeState;		
	/*
	 *  库存状态(10：入库中、 20：已上架、 30：出库中、40：移位中)
	 */
	
	@ApiModelProperty("入库时间")
	private Date inStoreTime;	//入库时间
	
	@ApiModelProperty("提升机编号")
	private String hoisterNo;		//提升机编号
	
	@ApiModelProperty("小车编号")
	private String carNo;		//小车编号
	
	@ApiModelProperty("任务Id")
	private String taskId;		//任务Id
	
	@ApiModelProperty("空托盘垛的托盘数量")
	private Integer emptyPalletCount;		
	
	@ApiModelProperty("源货位Id(移位用)")
	private Integer sourceLocationId;		//源货位Id(移位用)
	
	@ApiModelProperty("创建时间")
	private Date createTime;	//创建时间
	
	private int layer;
	
	private int x;
	
	private int y;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public String getContainerSubNo() {
		return containerSubNo;
	}

	public void setContainerSubNo(String containerSubNo) {
		this.containerSubNo = containerSubNo;
	}

	public Integer getStoreLocationId() {
		return storeLocationId;
	}

	public void setStoreLocationId(Integer storeLocationId) {
		this.storeLocationId = storeLocationId;
	}

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	public String getTaskProperty1() {
		return taskProperty1;
	}

	public void setTaskProperty1(String taskProperty1) {
		this.taskProperty1 = taskProperty1;
	}

	public String getTaskProperty2() {
		return taskProperty2;
	}

	public void setTaskProperty2(String taskProperty2) {
		this.taskProperty2 = taskProperty2;
	}

	public String getBusinessProperty1() {
		return businessProperty1;
	}

	public void setBusinessProperty1(String businessProperty1) {
		this.businessProperty1 = businessProperty1;
	}

	public String getBusinessProperty2() {
		return businessProperty2;
	}

	public void setBusinessProperty2(String businessProperty2) {
		this.businessProperty2 = businessProperty2;
	}

	public String getBusinessProperty3() {
		return businessProperty3;
	}

	public void setBusinessProperty3(String businessProperty3) {
		this.businessProperty3 = businessProperty3;
	}

	public String getBusinessProperty4() {
		return businessProperty4;
	}

	public void setBusinessProperty4(String businessProperty4) {
		this.businessProperty4 = businessProperty4;
	}

	public String getBusinessProperty5() {
		return businessProperty5;
	}

	public void setBusinessProperty5(String businessProperty5) {
		this.businessProperty5 = businessProperty5;
	}

	public int getStoreState() {
		return storeState;
	}

	public void setStoreState(int storeState) {
		this.storeState = storeState;
	}

	public Date getInStoreTime() {
		return inStoreTime;
	}

	public void setInStoreTime(Date inStoreTime) {
		this.inStoreTime = inStoreTime;
	}

	public String getHoisterNo() {
		return hoisterNo;
	}

	public void setHoisterNo(String hoisterNo) {
		this.hoisterNo = hoisterNo;
	}

	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Integer getEmptyPalletCount() {
		return emptyPalletCount;
	}

	public void setEmptyPalletCount(Integer emptyPalletCount) {
		this.emptyPalletCount = emptyPalletCount;
	}

	public Integer getSourceLocationId() {
		return sourceLocationId;
	}

	public void setSourceLocationId(Integer sourceLocationId) {
		this.sourceLocationId = sourceLocationId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public SxStoreDto(int id, String containerNo, String containerSubNo, Integer storeLocationId, int taskType,
			String taskProperty1, String taskProperty2, String businessProperty1, String businessProperty2,
			String businessProperty3, String businessProperty4, String businessProperty5, int storeState,
			Date inStoreTime, String hoisterNo, String carNo, String taskId, Integer emptyPalletCount,
			Integer sourceLocationId, Date createTime, int layer, int x, int y) {
		super();
		this.id = id;
		this.containerNo = containerNo;
		this.containerSubNo = containerSubNo;
		this.storeLocationId = storeLocationId;
		this.taskType = taskType;
		this.taskProperty1 = taskProperty1;
		this.taskProperty2 = taskProperty2;
		this.businessProperty1 = businessProperty1;
		this.businessProperty2 = businessProperty2;
		this.businessProperty3 = businessProperty3;
		this.businessProperty4 = businessProperty4;
		this.businessProperty5 = businessProperty5;
		this.storeState = storeState;
		this.inStoreTime = inStoreTime;
		this.hoisterNo = hoisterNo;
		this.carNo = carNo;
		this.taskId = taskId;
		this.emptyPalletCount = emptyPalletCount;
		this.sourceLocationId = sourceLocationId;
		this.createTime = createTime;
		this.layer = layer;
		this.x = x;
		this.y = y;
	}

	public SxStoreDto() {
		super();
		// TODO Auto-generated constructor stub
	}
}
