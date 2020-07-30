package com.prolog.eis.dto.sxk;

public class LocationDetailDto {

	private String containerNo;
	
	private int deptNum;
	
	private int depth;

	public LocationDetailDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LocationDetailDto(String containerNo, int deptNum, int depth) {
		super();
		this.containerNo = containerNo;
		this.deptNum = deptNum;
		this.depth = depth;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public int getDeptNum() {
		return deptNum;
	}

	public void setDeptNum(int deptNum) {
		this.deptNum = deptNum;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}
}
