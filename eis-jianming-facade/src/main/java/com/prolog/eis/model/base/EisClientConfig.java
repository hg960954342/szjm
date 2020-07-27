package com.prolog.eis.model.base;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("EIS_CLIENT_CONFIG")
public class EisClientConfig extends BaseModel{

	@Column("CONFIG_KEY")
	@ApiModelProperty("配置参数Key")
	private String configKey;
	
	@Column("CONFIG_VALUE")
	@ApiModelProperty("配置参数Value")
	private String configValue;
	
	@Column("NOTE")
	@ApiModelProperty("备注")
	private String note;

	public EisClientConfig() {
		super();
	}

	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	public String getConfigValue() {
		return configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((configKey == null) ? 0 : configKey.hashCode());
		result = prime * result + ((configValue == null) ? 0 : configValue.hashCode());
		result = prime * result + ((note == null) ? 0 : note.hashCode());
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
		EisClientConfig other = (EisClientConfig) obj;
		if (configKey == null) {
			if (other.configKey != null)
				return false;
		} else if (!configKey.equals(other.configKey))
			return false;
		if (configValue == null) {
			if (other.configValue != null)
				return false;
		} else if (!configValue.equals(other.configValue))
			return false;
		if (note == null) {
			if (other.note != null)
				return false;
		} else if (!note.equals(other.note))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EisClientConfig [configKey=" + configKey + ", configValue=" + configValue + ", note=" + note + "]";
	}

	public EisClientConfig(String configKey, String configValue, String note) {
		super();
		this.configKey = configKey;
		this.configValue = configValue;
		this.note = note;
	}


}
