package cn.smbms.dao.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.smbms.pojo.User;

public interface UserMapper {

	/**
	 * 通过userCode获取User
	 * 
	 * @param userCode
	 * @return @
	 */
	public User getLoginUser(@Param("userCode") String userCode);

	/**
	 * 增加用户信息
	 * 
	 * @param user
	 * @return @
	 */
	public int add(User user);

	/**
	 * 通过条件查询-userList
	 * 
	 * @param userName
	 * @param userRole
	 * @param currentPageNo
	 * @param pageSize
	 * @return @
	 */
	public List<User> getUserList(@Param("userName") String userName, @Param("userRole") Integer userRole,
			@Param("from") Integer currentPageNo, @Param("pageSize") Integer pageSize);

	/**
	 * 通过条件查询-用户表记录数
	 * 
	 * @param userName
	 * @param userRole
	 * @return @
	 */
	public int getUserCount(@Param("userName") String userName, @Param("userRole") Integer userRole);

	/**
	 * 通过userId删除user
	 * 
	 * @param delId
	 * @return @
	 */
	public int deleteUserById(@Param("id") Integer delId);

	/**
	 * 通过userId获取user
	 * 
	 * @param id
	 * @return @
	 */
	public User getUserById(@Param("id") Integer id);

	/**
	 * 修改用户信息
	 * 
	 * @param user
	 * @return @
	 */
	public int modify(User user);

	/**
	 * 修改当前用户密码
	 * 
	 * @param id
	 * @param pwd
	 * @return @
	 */
	public int updatePwd(@Param("id") Integer id, @Param("userPassword") String pwd);

}
