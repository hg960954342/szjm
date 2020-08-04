package com.prolog.eis.controller.base;

import com.prolog.eis.model.base.UserFavourite;
import com.prolog.eis.service.base.UserFavouriteService;
import com.prolog.framework.common.message.RestMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags="用户服务")
@RequestMapping("/api/v1/base/favourite")
public class PrologUserFavouriteController {
	
	@Autowired
	private UserFavouriteService userFavouriteService;

	@ApiOperation(value="添加收藏夹",notes="添加收藏夹")
	@PostMapping("/add")
	public RestMessage<UserFavourite> saveUserFavourite(@RequestBody UserFavourite userFavourite) throws Exception{
		userFavouriteService.addUserFavourite(userFavourite);
		return RestMessage.newInstance(true, "添加成功", null);
	}
	
	@ApiOperation(value="移除收藏夹",notes="移除收藏夹")
	@PostMapping("/remove")
	public RestMessage<UserFavourite> removeUserFavourite(@RequestBody UserFavourite userFavourite) throws Exception{
		userFavouriteService.removeUserFavourite(userFavourite.getUserId(), userFavourite.getFirstPrivilegeId());
		return RestMessage.newInstance(true, "移除成功", null);
	}
	
	@ApiOperation(value="查询收藏夹",notes="查询收藏夹")
	@PostMapping("/query")
	public RestMessage<List<Integer>> queryUserFavourite(@RequestBody UserFavourite userFavourite) throws Exception{
		List<Integer> userFavourites = userFavouriteService.queryUserFavourite(userFavourite.getUserId());
		return RestMessage.newInstance(true, "查询成功", userFavourites);
	}
}
