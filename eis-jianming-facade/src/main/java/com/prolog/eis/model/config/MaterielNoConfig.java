package com.prolog.eis.model.config;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

@Table("materiel_no_config")
public class MaterielNoConfig {

	@Id
	@ApiModelProperty("主键")
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
	private int id;
	
	@Column("materiel_no")
	@ApiModelProperty("料号")
	private String materielNo;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMaterielNo() {
		return materielNo;
	}

	public void setMaterielNo(String materielNo) {
		this.materielNo = materielNo;
	}
}
