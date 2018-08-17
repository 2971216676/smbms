package cn.smbms.dao.bill;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.smbms.pojo.Bill;

public interface BillMapper {
	
	/**
	 * 根据供应商ID查询订单数量
	 * @param providerId
	 * @return
	 * @throws Exception
	 */
	 int getBillCountByProviderId(@Param("providerId")Integer providerId);

	/**
	 * 通过查询条件获取供应商列表-getBillList
	 * @param productName
	 * @param providerId
	 * @param currentPageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	List<Bill> getBillList(@Param("productName") String productName, @Param("providerId") Integer providerId,
			@Param("isPayment") Integer isPayment, @Param("from") Integer currentPageNo,
			@Param("pageSize") Integer pageSize);

	/**
	 * 通过条件查询-订单表记录数
	 * @param userName
	 * @param userRole
	 * @return
	 * @throws Exception
	 */
	 int getBillCount(@Param("productName") String productName, @Param("providerId") Integer providerId,
			@Param("isPayment") Integer isPayment) ;
	
	 /**
	  * 进行订单添加
	  * @param bill
	  * @return
	  */
	 int addBill(Bill bill);
	 
	 /**
	  * 通过ID 获取订单信息
	  * @param id
	  * @return
	  */
	 Bill getBillByid(@Param("id") Integer id);
	 
	 /**
	  * 进行订单信息的修改
	  * @param bill
	  * @return
	  */
	 int updBill(Bill bill);
	 
	 /**
	  * 删除订单信息
	  * @param id
	  * @return
	  */
	 int delBill(@Param("id") Integer id);
}
