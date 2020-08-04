package com.prolog.eis.model.config;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

@Table("factory_name_config")
public class FactoryNameConfig {

	@Id
	@ApiModelProperty("主键")
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
	private int id;
	
	@Column("factory_name")
	@ApiModelProperty("厂商")
	private String factoryName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFactoryName() {
		return factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}
}
