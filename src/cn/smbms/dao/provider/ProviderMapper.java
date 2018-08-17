package cn.smbms.dao.provider;

import java.sql.Connection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.smbms.pojo.Provider;

public interface ProviderMapper {

	/**
	 * 通过供应商名称、编码获取供应商列表-模糊查询-providerList
	 * 
	 * @param connection
	 * @param proName
	 * @return @
	 */
	public List<Provider> getProviderList(@Param("proName") String proName, @Param("proCode") String proCode,
			@Param("from") Integer currentPageNo, @Param("pageSize") Integer pageSize);

	/**
	 * 通过条件查询-供应商表记录数
	 * 
	 * @param connection
	 * @param proName
	 * @param proCode
	 * @return @
	 */
	public int getProviderCount(@Param("proName") String proName, @Param("proCode") String proCode);

	/**
	 * 增加供应商
	 * 
	 * @param connection
	 * @param provider
	 * @return @
	 */
	public int add(Provider provider);

	/**
	 * 通过proId获取Provider
	 * 
	 * @param connection
	 * @param delId
	 * @return @
	 */
	public Provider getProviderById(@Param("id") Integer delId);

	/**
	 * 修改用户信息
	 * 
	 * @param connection
	 * @param user
	 * @return @
	 */
	public int modify(Provider provider);
	
	/**
	 * 通过供应商id删除供应商信息
	 * @param delId
	 * @return
	 * @throws Exception
	 */
	public int deleteProviderById(@Param("id")Integer delId); 
	
	public int delBill(@Param("providerId")Integer providerId);
	
	public int billByProviderId(@Param("providerId")Integer providerId);
	
	public List<Provider> getProvider();
}
