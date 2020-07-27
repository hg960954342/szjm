package com.prolog.eis.dto.eis;

import java.util.Date;
import java.util.List;

import com.prolog.eis.model.eis.QcSxPathPlanningTaskDto;
import com.prolog.eis.model.path.SxPathPlanningTaskHz;

public class SxPathPlanningTaskPriorityDto {

	private SxPathPlanningTaskHz hz;
	
	private int priority;
	
	private Date createTime;
	
	private List<QcSxPathPlanningTaskDto> vs;
	
	public SxPathPlanningTaskHz getHz() {
		return hz;
	}

	public void setHz(SxPathPlanningTaskHz hz) {
		this.hz = hz;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public List<QcSxPathPlanningTaskDto> getVs() {
		return vs;
	}

	public void setVs(List<QcSxPathPlanningTaskDto> vs) {
		this.vs = vs;
	}
}
