package cn.smbms.service.role;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.smbms.pojo.Role;

public interface RoleService {
	
	/**
	 * 
	 * @return
	 */
	 List<Role> getRoleList();
	
	/**
	  * 进行角色添加
	  * @param role
	  * @return
	  */
	 boolean addRole(Role role);
	 
	 /**
	  * 查询角色编码是否重复
	  * @param roleCode
	  * @return
	  */
	 boolean getRoleByRoleCode(String roleCode);
	 
	 /**
	  * 通过ID 查询 出角色信息
	  * @param id
	  * @return
	  */
	 Role getRoleById(Integer id);
	 
	 
	 /**
	  * 进行用户修改
	  * @param role
	  * @return
	  */
	 boolean updRole(Role role);
	 
	 /**
	  * 通过ID进行删除
	  * @param id
	  * @return
	  */
	 boolean delRole(String id);
	 
	 /**
	  * 查询角色是否被 用户引用 
	  * @param id
	  * @return
	  */
	 boolean getUserByUserRoleId(String roleId);
}
