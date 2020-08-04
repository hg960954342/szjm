package com.prolog.eis.controller.base;

import com.prolog.eis.dto.base.*;
import com.prolog.eis.model.base.SysUser;
import com.prolog.eis.model.base.UserPicture;
import com.prolog.eis.service.base.BasePagerService;
import com.prolog.eis.service.base.SysUserService;
import com.prolog.eis.service.delet.DeleteService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags="用户服务")
@RequestMapping("/api/v1/base/user")
public class PrologSysUserController {

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private DeleteService deleteService;
	@Autowired
	private BasePagerService basePagerService;
	
	@ApiOperation(value="用户登录",notes="用户登录")
	@PostMapping("/login")
	public RestMessage<UserLoginRespDto> userlogin(@RequestBody String json) throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		String loginName = helper.getString("loginName");
		String userPassword = helper.getString("userPassword");
		UserLoginRespDto userDto = sysUserService.login(loginName, userPassword);
		return RestMessage.newInstance(true, "查询成功", userDto);
	}
	
	@ApiOperation(value="修改用户密码",notes="修改用户密码")
	@PostMapping("/updatePwd")
	public RestMessage<SysUser> updatePassWord(@RequestBody UserPwdReqDto userRequest) throws Exception{
		sysUserService.updatePassWord(userRequest);
		return RestMessage.newInstance(true, "修改成功", null);
	}
	
	@ApiOperation(value="用户头像上传",notes="用户头像上传")
	@PostMapping("/picture")
	public RestMessage<UserPicture> userPictureUpload(@RequestParam(value = "userId", required = false) int userId,
			@RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam(value = "fileExtend", required = false) String fileExtend) throws Exception{
		byte[] pictureBytes = null;
		if(null != file) {
			InputStream inputStream = file.getInputStream();
			pictureBytes = new byte[(int) file.getSize()];
			inputStream.read(pictureBytes);
		}
		sysUserService.userPictureUpload(userId, pictureBytes,fileExtend);
		return RestMessage.newInstance(true, "保存成功", null);
	}
	
	@ApiOperation(value="用户头像下载",notes="用户头像下载")
	@PostMapping("/pictureDown")
	public ResponseEntity<byte[]> userPictureDownload(@RequestBody UserPwdReqDto userRequest) throws Exception{
		UserPicture userPicture = sysUserService.userPictureDownload(userRequest.getUserId());
		if(null != userPicture && userPicture.getFileExtend() != null) {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment",
					new String(userPicture.getFileExtend().getBytes("UTF-8"), "ISO8859-1"));
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			outStream.write(userPicture.getPictureBytes());
			return new ResponseEntity<byte[]>(outStream.toByteArray(), headers, HttpStatus.CREATED);
		}
		return null;
	}
	
	@ApiOperation(value="查询全部用户",notes="查询全部用户")
	@PostMapping("/findUser")
	public RestMessage<List<SysUserRespDto>> findUser(@RequestBody String json) throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		String name = helper.getString("name");
		String fullPath = helper.getString("fullPath");
		List<SysUserRespDto> users = sysUserService.findSysUser(name,fullPath);
		return RestMessage.newInstance(true, "查询成功", users);
	}
	
	@ApiOperation(value="查询全部用户(不包含密码)",notes="查询全部用户(不包含密码)")
	@PostMapping("/queryUserNoPwd")
	public RestMessage<List<SysUserRespDto>> queryUserNoPwd(@RequestBody String json) throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		String name = helper.getString("name");
		String fullPath = helper.getString("fullPath");
		List<SysUserRespDto> users = sysUserService.queryUserNoPwd(name,fullPath);
		return RestMessage.newInstance(true, "查询成功", users);
	}
	
	@ApiOperation(value="查询单个用户",notes="查询单个用户")
	@PostMapping("/query")
	public RestMessage<SysUser> findSysUser(@RequestBody String json) throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		int id = helper.getInt("id");
		SysUser user = sysUserService.querySysUser(id);
		return RestMessage.newInstance(true, "查询成功", user);
	}
	
	@ApiOperation(value="查询单个用户(不包含密码)",notes="(不包含密码)")
	@PostMapping("/findUserNoPwd")
	public RestMessage<SysUser> findUserNoPwd(@RequestBody String json) throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		int id = helper.getInt("id");
		SysUser user = sysUserService.findUserNoPwd(id);
		return RestMessage.newInstance(true, "查询成功", user);
		
 	}
	
	@ApiOperation(value="用户注册",notes="用户注册")
	@PostMapping("/saveUser")
	public RestMessage<List<SysUser>> saveSysUser(@RequestBody String json) throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		SysUser sysUser = helper.getObject("sysUser", SysUser.class);
		sysUserService.saveSysUser(sysUser);
		return RestMessage.newInstance(true, "注册成功", null);
	}
	
	@ApiOperation(value="修改用户",notes="修改用户")
	@PostMapping("/updateUser")
	public RestMessage<List<SysUser>> updateSysUser(@RequestBody String json) throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		SysUser sysUser = helper.getObject("sysUser", SysUser.class);
		sysUserService.updateSysUser(sysUser);
		return RestMessage.newInstance(true, "修改成功", null);
	}
	
	@ApiOperation(value="删除用户",notes="删除用户")
	@PostMapping("/deleteUser")
	public RestMessage<List<SysUser>> deleteSysUser(@RequestBody String json) throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		int id = helper.getInt("id");
		deleteService.DeleteSysUser(id);
		return RestMessage.newInstance(true, "删除成功", null);
	}
	
	@ApiOperation(value = "加载分页", notes = "加载分页")
	@PostMapping("/loadPager")
	public RestMessage<DataEntityCollectionDto> loadPager(@RequestBody String json) throws Exception {
		String columns = "id,loginName,userName,deptFullPath,sex,roleName,workNo";
		String tableName = "SysUserView";
 		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		Map<String, Object> reStr = helper.getPagerDto(columns,tableName,"condition", BasePagerDto.class);
		//获得拼装后的JSON对象
 		int startRowNum = (int) reStr.get("startRowNum");
 		int endRowNum = (int) reStr.get("endRowNum");
 		String condition = reStr.get("conditions").toString();
 		String orders =  reStr.get("orders").toString();
  		List<DataEntity> rows = basePagerService.getPagers(columns, tableName, condition, orders, startRowNum,endRowNum);
  		DataEntityCollectionDto collectionDto = new DataEntityCollectionDto();
  		collectionDto.setRows(rows);
  		return RestMessage.newInstance(true, "查询成功", collectionDto);
	}
	
	@ApiOperation(value = "第一次加载分页", notes = "第一次加载分页")
	@PostMapping("/firstLoadPager")
	public RestMessage<DataEntityCollectionDto> firstLoadPager(@RequestBody String json) throws Exception {
		String columns = "id,loginName,userName,deptFullPath,sex,roleName,workNo";
		String tableName = "SysUserView";
 		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		Map<String, Object> reStr = helper.getPagerDto(columns,tableName,"condition", BasePagerDto.class);
		//获得拼装后的JSON对象
 		int startRowNum = (int) reStr.get("startRowNum");
 		int endRowNum = (int) reStr.get("endRowNum");
 		String condition = reStr.get("conditions").toString();
 		String orders =  reStr.get("orders").toString();
 		//第一次加载需要获得总数量
 		int totalCount = basePagerService.getTotalCount(tableName,condition);
  		List<DataEntity> rows = basePagerService.getPagers(columns, tableName, condition, orders, startRowNum,endRowNum);
  		DataEntityCollectionDto collectionDto = new DataEntityCollectionDto();
  		collectionDto.setRows(rows);
  		collectionDto.setTotalCount(totalCount);
  		return RestMessage.newInstance(true, "查询成功", collectionDto);
	}
}
