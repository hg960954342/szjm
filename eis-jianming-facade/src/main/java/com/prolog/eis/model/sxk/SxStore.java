package com.prolog.eis.model.sxk;

import java.util.Date;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("SX_STORE")
public class SxStore {

	@Id
	@ApiModelProperty("四向库库存表")
	private int id;		//四向库库存表
	
	@Column("CONTAINER_NO")
	@ApiModelProperty("容器编号")
	private String containerNo;		//容器编号
	
	@Column("CONTAINER_SUB_NO")
	@ApiModelProperty("子容器编号")
	private String containerSubNo;		//子容器编号
	
	@Column("STORE_LOCATION_ID")
	@ApiModelProperty("货位ID")
	private Integer storeLocationId;		//货位ID
	
	@Column("SX_STORE_TYPE")
	@ApiModelProperty("库存任务类型 1 wms库存 2 eis库存")
	private int sxStoreType;
	
	@Column("TASK_TYPE")
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
	
	@Column("TASK_PROPERTY1")
	@ApiModelProperty("任务属性1（路由）")
	private String taskProperty1;		//任务属性1（路由）
	
	@Column("TASK_PROPERTY2")
	@ApiModelProperty("任务属性2（时效）")
	private String taskProperty2;		//任务属性2（时效）
	
	@Column("BUSINESS_PROPERTY1")
	@ApiModelProperty("业务属性1")
	private String businessProperty1;
	
	@Column("BUSINESS_PROPERTY2")
	@ApiModelProperty("业务属性2")
	private String businessProperty2;
	
	@Column("BUSINESS_PROPERTY3")
	@ApiModelProperty("业务属性3")
	private String businessProperty3;
	
	@Column("BUSINESS_PROPERTY4")
	@ApiModelProperty("业务属性4")
	private String businessProperty4;
	
	@Column("BUSINESS_PROPERTY5")
	@ApiModelProperty("业务属性5")
	private String businessProperty5;
	
	@Column("STORE_STATE")
	@ApiModelProperty("库存状态(10：入库中、 20：已上架、 30：出库中、31:待出库、40：移位中)")
	private int storeState;		
	/*
	 *  库存状态(10：入库中、 20：已上架、 30：出库中、40：移位中)
	 */
	
	@Column("IN_STORE_TIME")
	@ApiModelProperty("入库时间")
	private Date inStoreTime;	//入库时间
	
	
	@Column("HOISTER_NO")
	@ApiModelProperty("提升机编号")
	private String hoisterNo;		//提升机编号
	
	
	@Column("CAR_NO")
	@ApiModelProperty("小车编号")
	private String carNo;		//小车编号
	
	@Column("task_id")
	@ApiModelProperty("任务Id")
	private String taskId;		//任务Id
	
	@Column("EMPTY_PALLET_COUNT")
	@ApiModelProperty("空托盘垛的托盘数量")
	private Integer emptyPalletCount;		
	
	@Column("source_location_id")
	@ApiModelProperty("源货位Id(移位用)")
	private Integer sourceLocationId;		//源货位Id(移位用)
	
	@Column("CREATE_TIME")
	@ApiModelProperty("创建时间")
	private Date createTime;	//创建时间
	
	@Column("WEIGHT")
	@ApiModelProperty("重量")
	private Double weight;		//重量
	
	@Column("item_id")
	@ApiModelProperty("WMS商品编码")
	private String itemId;
	
	@Column("lot_id")
	@ApiModelProperty("wms批号")
	private String lotId;
	
	@Column("owner_id")
	@ApiModelProperty("wms批号")
	private String ownerId;
	
	@Column("qty")
	@ApiModelProperty("数量")
	private Double qty;
	
	@Column("station_id")
	@ApiModelProperty("站台Id")
	private Integer stationId;	//站台Id
	
	@Column("container_state")
	@ApiModelProperty("容器状态 1，合格 2不合格")
	private int containerState;

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

	public int getSxStoreType() {
		return sxStoreType;
	}

	public void setSxStoreType(int sxStoreType) {
		this.sxStoreType = sxStoreType;
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

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getLotId() {
		return lotId;
	}

	public void setLotId(String lotId) {
		this.lotId = lotId;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public Double getQty() {
		return qty;
	}

	public void setQty(Double qty) {
		this.qty = qty;
	}

	public Integer getStationId() {
		return stationId;
	}

	public void setStationId(Integer stationId) {
		this.stationId = stationId;
	}

	public int getContainerState() {
		return containerState;
	}

	public void setContainerState(int containerState) {
		this.containerState = containerState;
	}

	public SxStore(int id, String containerNo, String containerSubNo, Integer storeLocationId, int sxStoreType,
			int taskType, String taskProperty1, String taskProperty2, String businessProperty1,
			String businessProperty2, String businessProperty3, String businessProperty4, String businessProperty5,
			int storeState, Date inStoreTime, String hoisterNo, String carNo, String taskId, Integer emptyPalletCount,
			Integer sourceLocationId, Date createTime, Double weight, String itemId, String lotId, String ownerId,
			Double qty, Integer stationId, int containerState) {
		super();
		this.id = id;
		this.containerNo = containerNo;
		this.containerSubNo = containerSubNo;
		this.storeLocationId = storeLocationId;
		this.sxStoreType = sxStoreType;
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
		this.weight = weight;
		this.itemId = itemId;
		this.lotId = lotId;
		this.ownerId = ownerId;
		this.qty = qty;
		this.stationId = stationId;
		this.containerState = containerState;
	}

	public SxStore() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((businessProperty1 == null) ? 0 : businessProperty1.hashCode());
		result = prime * result + ((businessProperty2 == null) ? 0 : businessProperty2.hashCode());
		result = prime * result + ((businessProperty3 == null) ? 0 : businessProperty3.hashCode());
		result = prime * result + ((businessProperty4 == null) ? 0 : businessProperty4.hashCode());
		result = prime * result + ((businessProperty5 == null) ? 0 : businessProperty5.hashCode());
		result = prime * result + ((carNo == null) ? 0 : carNo.hashCode());
		result = prime * result + ((containerNo == null) ? 0 : containerNo.hashCode());
		result = prime * result + containerState;
		result = prime * result + ((containerSubNo == null) ? 0 : containerSubNo.hashCode());
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + ((emptyPalletCount == null) ? 0 : emptyPalletCount.hashCode());
		result = prime * result + ((hoisterNo == null) ? 0 : hoisterNo.hashCode());
		result = prime * result + id;
		result = prime * result + ((inStoreTime == null) ? 0 : inStoreTime.hashCode());
		result = prime * result + ((itemId == null) ? 0 : itemId.hashCode());
		result = prime * result + ((lotId == null) ? 0 : lotId.hashCode());
		result = prime * result + ((ownerId == null) ? 0 : ownerId.hashCode());
		result = prime * result + ((qty == null) ? 0 : qty.hashCode());
		result = prime * result + ((sourceLocationId == null) ? 0 : sourceLocationId.hashCode());
		result = prime * result + ((stationId == null) ? 0 : stationId.hashCode());
		result = prime * result + ((storeLocationId == null) ? 0 : storeLocationId.hashCode());
		result = prime * result + storeState;
		result = prime * result + sxStoreType;
		result = prime * result + ((taskId == null) ? 0 : taskId.hashCode());
		result = prime * result + ((taskProperty1 == null) ? 0 : taskProperty1.hashCode());
		result = prime * result + ((taskProperty2 == null) ? 0 : taskProperty2.hashCode());
		result = prime * result + taskType;
		result = prime * result + ((weight == null) ? 0 : weight.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SxStore other = (SxStore) obj;
		if (businessProperty1 == null) {
			if (other.businessProperty1 != null)
				return false;
		} else if (!businessProperty1.equals(other.businessProperty1))
			return false;
		if (businessProperty2 == null) {
			if (other.businessProperty2 != null)
				return false;
		} else if (!businessProperty2.equals(other.businessProperty2))
			return false;
		if (businessProperty3 == null) {
			if (other.businessProperty3 != null)
				return false;
		} else if (!businessProperty3.equals(other.businessProperty3))
			return false;
		if (businessProperty4 == null) {
			if (other.businessProperty4 != null)
				return false;
		} else if (!businessProperty4.equals(other.businessProperty4))
			return false;
		if (businessProperty5 == null) {
			if (other.businessProperty5 != null)
				return false;
		} else if (!businessProperty5.equals(other.businessProperty5))
			return false;
		if (carNo == null) {
			if (other.carNo != null)
				return false;
		} else if (!carNo.equals(other.carNo))
			return false;
		if (containerNo == null) {
			if (other.containerNo != null)
				return false;
		} else if (!containerNo.equals(other.containerNo))
			return false;
		if (containerState != other.containerState)
			return false;
		if (containerSubNo == null) {
			if (other.containerSubNo != null)
				return false;
		} else if (!containerSubNo.equals(other.containerSubNo))
			return false;
		if (createTime == null) {
			if (other.createTime != null)
				return false;
		} else if (!createTime.equals(other.createTime))
			return false;
		if (emptyPalletCount == null) {
			if (other.emptyPalletCount != null)
				return false;
		} else if (!emptyPalletCount.equals(other.emptyPalletCount))
			return false;
		if (hoisterNo == null) {
			if (other.hoisterNo != null)
				return false;
		} else if (!hoisterNo.equals(other.hoisterNo))
			return false;
		if (id != other.id)
			return false;
		if (inStoreTime == null) {
			if (other.inStoreTime != null)
				return false;
		} else if (!inStoreTime.equals(other.inStoreTime))
			return false;
		if (itemId == null) {
			if (other.itemId != null)
				return false;
		} else if (!itemId.equals(other.itemId))
			return false;
		if (lotId == null) {
			if (other.lotId != null)
				return false;
		} else if (!lotId.equals(other.lotId))
			return false;
		if (ownerId == null) {
			if (other.ownerId != null)
				return false;
		} else if (!ownerId.equals(other.ownerId))
			return false;
		if (qty == null) {
			if (other.qty != null)
				return false;
		} else if (!qty.equals(other.qty))
			return false;
		if (sourceLocationId == null) {
			if (other.sourceLocationId != null)
				return false;
		} else if (!sourceLocationId.equals(other.sourceLocationId))
			return false;
		if (stationId == null) {
			if (other.stationId != null)
				return false;
		} else if (!stationId.equals(other.stationId))
			return false;
		if (storeLocationId == null) {
			if (other.storeLocationId != null)
				return false;
		} else if (!storeLocationId.equals(other.storeLocationId))
			return false;
		if (storeState != other.storeState)
			return false;
		if (sxStoreType != other.sxStoreType)
			return false;
		if (taskId == null) {
			if (other.taskId != null)
				return false;
		} else if (!taskId.equals(other.taskId))
			return false;
		if (taskProperty1 == null) {
			if (other.taskProperty1 != null)
				return false;
		} else if (!taskProperty1.equals(other.taskProperty1))
			return false;
		if (taskProperty2 == null) {
			if (other.taskProperty2 != null)
				return false;
		} else if (!taskProperty2.equals(other.taskProperty2))
			return false;
		if (taskType != other.taskType)
			return false;
		if (weight == null) {
			if (other.weight != null)
				return false;
		} else if (!weight.equals(other.weight))
			return false;
		return true;
	}
}
