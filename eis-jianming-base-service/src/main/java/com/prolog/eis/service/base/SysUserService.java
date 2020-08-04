package com.prolog.eis.service.base;

import com.prolog.eis.dto.base.SysUserRespDto;
import com.prolog.eis.dto.base.UserLoginRespDto;
import com.prolog.eis.dto.base.UserPwdReqDto;
import com.prolog.eis.model.base.SysUser;
import com.prolog.eis.model.base.UserPicture;

import java.util.List;

public interface SysUserService {

	/**
	 * 用户登录
	 * @date 2018年8月29日 上午10:35:52
	 * @author dengss
	 * @param userName
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	public UserLoginRespDto login (String userName,String pwd) throws Exception;
	
	/**
	 * 用户头像上传
	 * @date 2018年8月29日 上午10:36:08
	 * @author dengss
	 * @throws Exception
	 */
	public void userPictureUpload (int userId ,byte[] pictureBytes,String fileExtend) throws Exception;
	
	/**
	 * 用户图像下载
	 * @date 2018年8月29日 上午10:58:27
	 * @author dengss
	 * @param id
	 * @throws Exception
	 */
	public UserPicture userPictureDownload (int userId) throws Exception;
	
	/**
	 * 用户修改密码
	 * @date 2018年8月30日 上午9:56:00
	 * @author dengss
	 * @param userRequest
	 * @throws Exception
	 */
	public void updatePassWord (UserPwdReqDto userRequest) throws Exception;
	
	/**
	 * 查询所有用户
	 * @date 2018年8月30日 上午10:54:37
	 * @author dengss
	 * @return
	 * @throws Exception
	 */
	public List<SysUserRespDto> findSysUser(String name ,String fullPath)throws Exception;
	
	/**
	 * 新增用户
	 * @date 2018年9月8日 下午5:15:26
	 * @author dengss
	 * @throws Exception
	 */
	public void saveSysUser(SysUser user) throws Exception;
	
	/**
	 * 修改用户
	 * @date 2018年9月8日 下午5:16:26
	 * @author dengss
	 * @param user
	 * @throws Exception
	 */
	public void updateSysUser(SysUser user) throws Exception;
	
	/**
	 * 查询单个用户
	 * @date 2018年9月17日 下午5:23:24
	 * @author dengss
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public SysUser querySysUser(int id) throws Exception;
	
	/**
	 * 查询单个用户（不包含密码）
	 * 
	 * @date 2018年9月26日 下午2:12:24
	 * @author huhao
	 * @param id
	 * @return SysUser
	 * @throws Exception
	 */
	public SysUser findUserNoPwd(int id) throws Exception;
	
	

	/**
	 * 查询所有用户（不包含密码）
	 * 
	 * @date 2018年9月26日 下午2:40:24
	 * @author huhao
	 * @param id
	 * @return List<SysUserRespDto>
	 * @throws Exception
	 */
	public List<SysUserRespDto> queryUserNoPwd(String name ,String fullPath) throws Exception;
	

}
