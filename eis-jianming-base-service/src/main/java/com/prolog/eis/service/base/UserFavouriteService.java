package com.prolog.eis.service.base;

import com.prolog.eis.model.base.UserFavourite;

import java.util.List;

public interface UserFavouriteService {

	/**
	 * 新增收藏夹
	 * @date 2018年8月31日 上午10:33:26
	 * @author dengss
	 * @param userFavourite
	 * @throws Exception
	 */
	public void addUserFavourite(UserFavourite userFavourite) throws Exception;
	
	/**
	 * 移除收藏夹
	 * @date 2018年8月31日 上午10:34:56
	 * @author dengss
	 * @param userId
	 * @param firstPrivilegeId
	 * @throws Exception
	 */
	public void removeUserFavourite(int userId, int firstPrivilegeId)throws Exception;
	
	/**
	 * 查询收藏夹
	 * @date 2018年8月31日 上午10:41:07
	 * @author dengss
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<Integer> queryUserFavourite(int userId)throws Exception;
}
