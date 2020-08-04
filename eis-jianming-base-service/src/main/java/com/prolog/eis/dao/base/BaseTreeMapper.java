package com.prolog.eis.dao.base;

import com.prolog.eis.model.base.BaseTreeModel;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface BaseTreeMapper{
	
	
	/**
	 * 查询全部树
	 * @date 2018年9月6日 上午9:43:37
	 * @author dengss
	 * @param tableName
	 * @return
	 */
	@Results({
		@Result(property = "id",  column = "id"),@Result(property = "parentId",  column = "parentId"),
		@Result(property = "sortIndex",  column = "sortIndex"),@Result(property = "fullPath",  column = "fullPath"),
		@Result(property = "objectName",  column = "objectName")
	})
	@Select("select id,parentId,sortIndex,fullPath,objectName from ${tableName} order by sortIndex")
	public List<BaseTreeModel> findBaseTree(@Param ("tableName") String tableName);
	
	/**
	 * 节点保存
	 * @date 2018年9月6日 上午9:43:51
	 * @author dengss
	 * @param tableName
	 * @param baseTreeModel
	 */
	@Insert("insert into ${tableName} (id,objectname,parentid,fullpath,sortindex) values (USERDEPT_SEQ.NEXTVAL,#{objectname},#{parentid},#{fullpath},#{sortindex})")
	public void saveBaseTree(@Param ("tableName") String tableName,@Param ("objectname") String objectname,
			@Param ("parentid") int parentid,@Param ("fullpath") String fullpath,@Param ("sortindex") int sortindex);
	
	/**
	 * 查询父节点下所有子节点id
	 * @date 2018年9月6日 上午9:50:35
	 * @author dengss
	 * @param tableName
	 * @param parentid
	 * @return
	 */
	@Results({
		@Result(property = "id",  column = "id"),@Result(property = "parentId",  column = "parentId"),
		@Result(property = "sortIndex",  column = "sortIndex"),@Result(property = "fullPath",  column = "fullPath"),
		@Result(property = "objectName",  column = "objectName")
	})
	@Select("select id,parentId,sortIndex,fullPath,objectName from ${tableName} where parentId = #{parentId}")
	public List<BaseTreeModel> findSubNode(@Param ("tableName") String tableName,@Param("parentId") int parentId);
	
	/**
	 * 查询父节点Id
	 * @date 2018年9月6日 上午10:14:00
	 * @author dengss
	 * @param tableName
	 * @param parentid
	 * @return
	 */
	@Select("select id,parentId,sortIndex,fullPath,objectName from ${tableName} where id = #{parentId}")
	public BaseTreeModel findParentNode(@Param ("tableName") String tableName,@Param("parentId") int parentId);
	
	/**
	 * 修改节点的名称和路径
	 * @date 2018年9月6日 下午2:05:44
	 * @author dengss
	 * @param tableName
	 * @param id
	 * @param objectName
	 * @param fullPath
	 */
	@Update("update ${tableName} set objectName = #{objectName} where id = #{id}")
	public void updateNode(@Param("tableName") String tableName,@Param("id") int id,
			@Param("objectName") String objectName);
	
	/**
	 * 查询节点
	 * @date 2018年9月6日 下午2:08:22
	 * @author dengss
	 * @param id
	 * @return
	 */
	@Results({
		@Result(property = "id",  column = "id"),@Result(property = "parentId",  column = "parentId"),
		@Result(property = "sortIndex",  column = "sortIndex"),@Result(property = "fullPath",  column = "fullPath"),
		@Result(property = "objectName",  column = "objectName")
	})
	@Select("select id,parentId,sortIndex,fullPath,objectName from ${tableName} where id = #{id}")
	public BaseTreeModel findNode(@Param("tableName") String tableName,@Param("id") int id);

	/**
	 * 查询父节点下是否有相同节点
	 * @date 2018年9月6日 上午9:50:35
	 * @author dengss
	 * @param tableName
	 * @param parentid
	 * @return
	 */
	@Results({
		@Result(property = "id",  column = "id"),@Result(property = "parentId",  column = "parentId"),
		@Result(property = "sortIndex",  column = "sortIndex"),@Result(property = "fullPath",  column = "fullPath"),
		@Result(property = "objectName",  column = "objectName")
	})
	@Select("select id,parentId,sortIndex,fullPath,objectName from ${tableName} where parentId = #{parentId} "
			+ "and objectName = #{objectName} and id != #{id}")
	public List<BaseTreeModel> querySubNode(@Param ("tableName") String tableName,@Param("parentId") int parentId,
			@Param("objectName")String objectName,@Param("id") int id);
	
	/**
	 * 修改fullPath
	 * @date 2018年9月6日 下午3:26:51
	 * @author dengss
	 * @param tableName
	 * @param fullPath
	 * @param oldFullPath
	 */
	@Update("update ${tableName} set fullPath = concat(#{fullPath},substr(fullPath,length(#{oldFullPath}) + 1)) where concat(fullPath, '/' like #{ofp})")
	public void updateFullPath(@Param("tableName")String tableName,@Param("fullPath") String fullPath,
			@Param("oldFullPath") String oldFullPath,@Param("ofp") String  ofp);
	
	
	/**
	 * 修改sortIndex和父节点Id
	 * @date 2018年9月8日 下午2:45:48
	 * @author dengss
	 * @param tableName
	 * @param parentId
	 * @param id
	 */
	@Update("update ${tableName} set sortIndex = #{sortIndex},parentId = #{parentId} where id = #{id}")
	public void updateSortIndex(@Param("tableName")String tableName,@Param("sortIndex")int sortIndex,@Param("parentId")int parentId,@Param("id") int id);
}
