package cn.smbms.service.bill;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.smbms.pojo.Bill;

public interface BillService {

	/**
	 * 通过条件获取供应商列表-providerList
	 * @param productName
	 * @param providerId
	 * @param isPayment
	 * @param currentPageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public List<Bill> getBillList(String productName,Integer providerId,
								Integer isPayment,Integer currentPageNo,Integer pageSize) throws Exception;

	
	/**
	 * 通过条件查询-订单表记录数
	 * @param productName
	 * @param providerId
	 * @param isPayment
	 * @return
	 * @throws Exception
	 */
	public int getBillCount(String productName,Integer providerId,Integer isPayment) throws Exception;
	
	/**
	 * 进行添加订单
	 * @param bill
	 * @return
	 */
	boolean addBill(Bill bill);
	
	/**
	 * 通过ID 查询 订单信息
	 * @param id
	 * @return
	 */
	Bill getBillByid(@Param("id") Integer id);
	
	/**
	 * 修改订单信息
	 * @param id
	 * @return
	 */
	boolean updBill(Bill bill);
	
	 /**
	  * 删除订单信息
	  * @param id
	  * @return
	  */
	boolean delBill(@Param("id") Integer id);
}
