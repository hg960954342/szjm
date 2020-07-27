package com.prolog.eis.model.base;

import java.util.Arrays;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("USERPICTURE")
public class UserPicture extends BaseModel{
	
	@Id
	@AutoKey(sequence = "USERPICTURE_SEQ",type = AutoKey.TYPE_IDENTITY)
	@ApiModelProperty("id")
	private int id;
	
	@ApiModelProperty("用户Id")
	private int userId;
	
	@ApiModelProperty("用户照片")
	private byte[] pictureBytes;
	
	@ApiModelProperty("照片扩展名")
	private String fileExtend;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public byte[] getPictureBytes() {
		return pictureBytes;
	}

	public void setPictureBytes(byte[] pictureBytes) {
		this.pictureBytes = pictureBytes;
	}

	public String getFileExtend() {
		return fileExtend;
	}

	public void setFileExtend(String fileExtend) {
		this.fileExtend = fileExtend;
	}

	public UserPicture() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserPicture(int id, int userId, byte[] pictureBytes, String fileExtend) {
		super();
		this.id = id;
		this.userId = userId;
		this.pictureBytes = pictureBytes;
		this.fileExtend = fileExtend;
	}

	@Override
	public String toString() {
		return "UserPicture [id=" + id + ", userId=" + userId + ", pictureBytes=" + Arrays.toString(pictureBytes)
				+ ", fileExtend=" + fileExtend + "]";
	}

}
