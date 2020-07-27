package com.prolog.eis.model.eis;

import java.util.Date;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("lxk_zt_store")
public class LxkZtStore {
	@Id
	@ApiModelProperty("主键")
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
	private int id;

	@Column("wms_push")
	@ApiModelProperty("是否wms下发，0不是，1是")
	private int wmsPush;

	@Column("hangdao")
	@ApiModelProperty("巷道")
	private int hangdao;

	@Column("station_id")
	@ApiModelProperty("站台id")
	private Integer stationId;

	@Column("zt_state")
	@ApiModelProperty("在途状态(10已出库,20播种排队,30到达播种台,40离开播种台,50循环中,60回库中)")
	private int ztState;
	
	@Column("box_no")
	@ApiModelProperty("料箱号")
	private String boxNo;

	@Column("lot_id")
	@ApiModelProperty("批次id")
	private Integer lotId;

	@Column("store_count")
	@ApiModelProperty("库存数量")
	private int storeCount;
	
	@Column("inbound_time")
	@ApiModelProperty("入库时间")
	private Date inboundTime;

	@Column("sp_id")
	@ApiModelProperty("商品Id")
	private int spId;

	@Column("owner")
	@ApiModelProperty("业主")
	private String owner;

	@Column("create_time")
	@ApiModelProperty("创建时间")
	private Date createTime;

	@Column("lastck_time")
	@ApiModelProperty("最后一次出库时间")
	private Date lastckTime = null;

	@Column("production_date")
	@ApiModelProperty("生产日期")
	private Date productionDate;

	@Column("expiry_date")
	@ApiModelProperty("有效日期")
	private Date expiryDate;

	@Column("task_type")
	@ApiModelProperty("任务类型 10一般作业 20质检作业  30空托作业")
	private int taskType;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getWmsPush() {
		return wmsPush;
	}

	public void setWmsPush(int wmsPush) {
		this.wmsPush = wmsPush;
	}

	public int getHangdao() {
		return hangdao;
	}

	public void setHangdao(int hangdao) {
		this.hangdao = hangdao;
	}

	public Integer getStationId() {
		return stationId;
	}

	public void setStationId(Integer stationId) {
		this.stationId = stationId;
	}

	public int getZtState() {
		return ztState;
	}

	public void setZtState(int ztState) {
		this.ztState = ztState;
	}

	public String getBoxNo() {
		return boxNo;
	}

	public void setBoxNo(String boxNo) {
		this.boxNo = boxNo;
	}

	public Integer getLotId() {
		return lotId;
	}

	public void setLotId(Integer lotId) {
		this.lotId = lotId;
	}

	public int getStoreCount() {
		return storeCount;
	}

	public void setStoreCount(int storeCount) {
		this.storeCount = storeCount;
	}

	public Date getInboundTime() {
		return inboundTime;
	}

	public void setInboundTime(Date inboundTime) {
		this.inboundTime = inboundTime;
	}

	public int getSpId() {
		return spId;
	}

	public void setSpId(int spId) {
		this.spId = spId;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastckTime() {
		return lastckTime;
	}

	public void setLastckTime(Date lastckTime) {
		this.lastckTime = lastckTime;
	}

	public Date getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}
}
