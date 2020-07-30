package com.prolog.eis.model.sxk;

import java.util.Date;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("SX_STORE_LOCATION_GROUP")
public class SxStoreLocationGroup {

	@Id
	@ApiModelProperty("货位组ID")
	private int id;		//货位组ID
	
	@Column("GROUP_NO")
	@ApiModelProperty("货位组编号")
	private String groupNo;		//货位组编号
	
	@Column("ENTRANCE")
	@ApiModelProperty("入口类型：1、仅入口1，2、仅入口2，3、入口1+入口2")
	private int entrance;		//入口类型：1、仅入口1，2、仅入口2，3、入口1+入口2
	
	@Column("IN_OUT_NUM")
	@ApiModelProperty("出入口数量")
	private int inOutNum;		//出入口数量
	
	@Column("IS_LOCK")
	@ApiModelProperty("是否锁定")
	private int isLock;		//是否锁定(1.锁定 、0.不锁定)
	
	@Column("ASCENT_LOCK_STATE")
	@ApiModelProperty("货位组升位锁")
	private int ascentLockState;		//货位组升位锁 (1.锁定 、0.不锁定)
	
	@Column("READY_OUT_LOCK")
	@ApiModelProperty("待出库锁")
	private int readyOutLock;		//待出库锁 (1.锁定 、0.不锁定)
	
	@Column("LAYER")
	@ApiModelProperty("层")
	private int layer;		//层

	@Column("X")
	@ApiModelProperty("X")
	private int x;		//X
	
	@Column("Y")
	@ApiModelProperty("Y")
	private int y;		//Y
	
	@Column("location_num")
	@ApiModelProperty("货位数量")
	private int locationNum;		//货位数量
	
	@Column("entrance1_property1")
	@ApiModelProperty("入口1的属性1(无入口则值为'none')")
	private String entrance1Property1;		//入口1的属性1(无入口则值为'none')
	
	@Column("entrance1_property2")
	@ApiModelProperty("入口1的属性2(无入口则值为'none')")
	private String entrance1Property2;		//入口1的属性2(无入口则值为'none')
	
	@Column("entrance2_property1")
	@ApiModelProperty("入口2的属性1(无入口则值为'none')")
	private String entrance2Property1;		//入口2的属性1(无入口则值为'none')
	
	@Column("entrance2_property2")
	@ApiModelProperty("入口2的属性2(无入口则值为'none')")
	private String entrance2Property2;		//入口2的属性2(无入口则值为'none')
	
	@Column("reserved_location")
	@ApiModelProperty("预留货位")
	private int reservedLocation;		//预留货位1.空托盘预留货位、2.理货预留货位、3.不用预留货位
	
	@Column("belong_area")
	@ApiModelProperty("所属区域")
	private int belongArea;		//所属区域
	
	@Column("CREATE_TIME")
	@ApiModelProperty("创建时间")
	private Date createTime;		//创建时间

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEntrance() {
		return entrance;
	}

	public void setEntrance(int entrance) {
		this.entrance = entrance;
	}

	public int getInOutNum() {
		return inOutNum;
	}

	public void setInOutNum(int inOutNum) {
		this.inOutNum = inOutNum;
	}

	public int getIsLock() {
		return isLock;
	}

	public void setIsLock(int isLock) {
		this.isLock = isLock;
	}

	public int getAscentLockState() {
		return ascentLockState;
	}

	public void setAscentLockState(int ascentLockState) {
		this.ascentLockState = ascentLockState;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	
	public int getLocationNum() {
		return locationNum;
	}

	public void setLocationNum(int locationNum) {
		this.locationNum = locationNum;
	}

	public String getEntrance1Property1() {
		return entrance1Property1;
	}

	public void setEntrance1Property1(String entrance1Property1) {
		this.entrance1Property1 = entrance1Property1;
	}

	public String getEntrance1Property2() {
		return entrance1Property2;
	}

	public void setEntrance1Property2(String entrance1Property2) {
		this.entrance1Property2 = entrance1Property2;
	}

	public String getEntrance2Property1() {
		return entrance2Property1;
	}

	public void setEntrance2Property1(String entrance2Property1) {
		this.entrance2Property1 = entrance2Property1;
	}

	public String getEntrance2Property2() {
		return entrance2Property2;
	}

	public void setEntrance2Property2(String entrance2Property2) {
		this.entrance2Property2 = entrance2Property2;
	}

	public String getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}
	
	

	public int getReservedLocation() {
		return reservedLocation;
	}

	public void setReservedLocation(int reservedLocation) {
		this.reservedLocation = reservedLocation;
	}

	public int getBelongArea() {
		return belongArea;
	}

	public void setBelongArea(int belongArea) {
		this.belongArea = belongArea;
	}

	/**
	 * @return the readyOutLock
	 */
	public int getReadyOutLock() {
		return readyOutLock;
	}

	/**
	 * @param readyOutLock the readyOutLock to set
	 */
	public void setReadyOutLock(int readyOutLock) {
		this.readyOutLock = readyOutLock;
	}

	@Override
	public String toString() {
		return "SxStoreLocationGroup [id=" + id + ", groupNo=" + groupNo + ", entrance=" + entrance + ", inOutNum="
				+ inOutNum + ", isLock=" + isLock + ", ascentLockState=" + ascentLockState + ", readyOutLock="
				+ readyOutLock + ", layer=" + layer + ", x=" + x + ", y=" + y + ", locationNum=" + locationNum
				+ ", entrance1Property1=" + entrance1Property1 + ", entrance1Property2=" + entrance1Property2
				+ ", entrance2Property1=" + entrance2Property1 + ", entrance2Property2=" + entrance2Property2
				+ ", reservedLocation=" + reservedLocation + ", belongArea=" + belongArea + ", createTime=" + createTime
				+ "]";
	}

	public SxStoreLocationGroup(int id, String groupNo, int entrance, int inOutNum, int isLock, int ascentLockState,
			int readyOutLock, int layer, int x, int y, int locationNum, String entrance1Property1,
			String entrance1Property2, String entrance2Property1, String entrance2Property2, int reservedLocation,
			int belongArea, Date createTime) {
		super();
		this.id = id;
		this.groupNo = groupNo;
		this.entrance = entrance;
		this.inOutNum = inOutNum;
		this.isLock = isLock;
		this.ascentLockState = ascentLockState;
		this.readyOutLock = readyOutLock;
		this.layer = layer;
		this.x = x;
		this.y = y;
		this.locationNum = locationNum;
		this.entrance1Property1 = entrance1Property1;
		this.entrance1Property2 = entrance1Property2;
		this.entrance2Property1 = entrance2Property1;
		this.entrance2Property2 = entrance2Property2;
		this.reservedLocation = reservedLocation;
		this.belongArea = belongArea;
		this.createTime = createTime;
	}

	public SxStoreLocationGroup() {
		super();
		// TODO Auto-generated constructor stub
	}
}
