package com.prolog.eis.dto.base;

public class FileVersionRespDto {

	/**
	 * 文件名称
	 */
	private String fileName;
	
	/**
	 * 文件大小
	 */
	private Long fileLength;
	
	private int part1;
	
	private int part2;
	
	private int part3;
	
	private int part4;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getFileLength() {
		return fileLength;
	}

	public void setFileLength(Long fileLength) {
		this.fileLength = fileLength;
	}

	public int getPart1() {
		return part1;
	}

	public void setPart1(int part1) {
		this.part1 = part1;
	}

	public int getPart2() {
		return part2;
	}

	public void setPart2(int part2) {
		this.part2 = part2;
	}

	public int getPart3() {
		return part3;
	}

	public void setPart3(int part3) {
		this.part3 = part3;
	}

	public int getPart4() {
		return part4;
	}

	public void setPart4(int part4) {
		this.part4 = part4;
	}

	public FileVersionRespDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FileVersionRespDto(String fileName, Long fileLength, int part1, int part2, int part3, int part4) {
		super();
		this.fileName = fileName;
		this.fileLength = fileLength;
		this.part1 = part1;
		this.part2 = part2;
		this.part3 = part3;
		this.part4 = part4;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fileLength == null) ? 0 : fileLength.hashCode());
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + part1;
		result = prime * result + part2;
		result = prime * result + part3;
		result = prime * result + part4;
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
		FileVersionRespDto other = (FileVersionRespDto) obj;
		if (fileLength == null) {
			if (other.fileLength != null)
				return false;
		} else if (!fileLength.equals(other.fileLength))
			return false;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (part1 != other.part1)
			return false;
		if (part2 != other.part2)
			return false;
		if (part3 != other.part3)
			return false;
		if (part4 != other.part4)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return fileName + "@@" + fileLength + "@@" + part1
				+ "@@" + part2 + "@@" + part3 + "@@" + part4 ;
	}
}
