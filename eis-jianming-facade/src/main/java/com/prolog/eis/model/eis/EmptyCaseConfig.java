package com.prolog.eis.model.eis;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("empty_case_config")
public class EmptyCaseConfig {

	@Id
	@Column("id")
	@ApiModelProperty("主键")
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
	private int id;
	
	@Column("layer")
	@ApiModelProperty("层")
	private int layer;
	
	@Column("min_count")
	@ApiModelProperty("最少数量")
	private int minCount;
	
	@Column("sort_index")
	@ApiModelProperty("排序值")
	private int sortIndex;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public int getMinCount() {
		return minCount;
	}

	public void setMinCount(int minCount) {
		this.minCount = minCount;
	}

	public int getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(int sortIndex) {
		this.sortIndex = sortIndex;
	}

	public EmptyCaseConfig(int id, int layer, int minCount, int sortIndex) {
		super();
		this.id = id;
		this.layer = layer;
		this.minCount = minCount;
		this.sortIndex = sortIndex;
	}

	public EmptyCaseConfig() {
		super();
		// TODO Auto-generated constructor stub
	}
}
