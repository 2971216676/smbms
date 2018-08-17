package cn.smbms.dao.role;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.smbms.pojo.Role;

public interface RoleMapper {

	/*
	 * 查询角色列表
	 */
	 List<Role> getRoleList();
	 
	 /**
	  * 进行角色添加
	  * @param role
	  * @return
	  */
	 int addRole(Role role);
	 
	 /**
	  * 查询角色编码是否重复
	  * @param roleCode
	  * @return
	  */
	 int getRoleByRoleCode(@Param("roleCode")String roleCode);
	 
	 /**
	  * 通过ID 查询 出角色信息
	  * @param id
	  * @return
	  */
	 Role getRoleById(@Param("id")Integer id);
	 
	 
	 /**
	  * 进行用户修改
	  * @param role
	  * @return
	  */
	 int upeRole(Role role);
	 
	 /**
	  * 通过ID进行删除
	  * @param id
	  * @return
	  */
	 int delRole(@Param("id")String id);
	 
	 /**
	  * 查询角色是否被 用户引用 
	  * @param id
	  * @return
	  */
	 int getUserRoleByUserRoleId(@Param("roleId")String roleId);
}
