package com.prolog.eis.dto.base;

public class BarCodeDto {

	public String barCode;

	public BarCodeDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BarCodeDto(String barCode) {
		super();
		this.barCode = barCode;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((barCode == null) ? 0 : barCode.hashCode());
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
		BarCodeDto other = (BarCodeDto) obj;
		if (barCode == null) {
			if (other.barCode != null)
				return false;
		} else if (!barCode.equals(other.barCode))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BarCodeDto [barCode=" + barCode + "]";
	}
}
