package com.prolog.eis.model.eis;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("pallet_info")
public class PalletInfo {

	@Id
	@Column("container_code")
	@ApiModelProperty("母托编号")
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
	private String containerCode;

	public String getContainerCode() {
		return containerCode;
	}

	public void setContainerCode(String containerCode) {
		this.containerCode = containerCode;
	}

	public PalletInfo(String containerCode) {
		super();
		this.containerCode = containerCode;
	}

	public PalletInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
}
