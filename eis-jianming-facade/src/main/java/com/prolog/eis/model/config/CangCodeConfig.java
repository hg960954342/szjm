package com.prolog.eis.model.config;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

@Table("cang_code_config")
public class CangCodeConfig {

	@Id
	@ApiModelProperty("主键")
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
	private int id;
	
	@Column("cang_code")
	@ApiModelProperty("仓码")
	private String cangCode;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCangCode() {
		return cangCode;
	}

	public void setCangCode(String cangCode) {
		this.cangCode = cangCode;
	}
}
