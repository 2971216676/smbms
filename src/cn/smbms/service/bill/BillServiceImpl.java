package cn.smbms.service.bill;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.smbms.dao.bill.BillMapper;
import cn.smbms.pojo.Bill;

@Service
public class BillServiceImpl implements BillService {

	@Resource
	private BillMapper billMapper;

	@Override
	public List<Bill> getBillList(String productName, Integer providerId, Integer isPayment, Integer currentPageNo,
			Integer pageSize) throws Exception {
		currentPageNo = (currentPageNo - 1) * pageSize;
		return billMapper.getBillList(productName, providerId, isPayment, currentPageNo, pageSize);
	}

	@Override
	public int getBillCount(String productName, Integer providerId, Integer isPayment) throws Exception {
		// TODO Auto-generated method stub
		return billMapper.getBillCount(productName, providerId, isPayment);
	}

	@Override
	public boolean addBill(Bill bill) {
		if(billMapper.addBill(bill)>0){
			return true;
		}	
		return false;
	}

	@Override
	public Bill getBillByid(Integer id) {
		return billMapper.getBillByid(id);
	}

	@Override
	public boolean updBill(Bill bill) {
		if(billMapper.updBill(bill)>0){
			return true;
		}
		return false;
	}

	@Override
	public boolean delBill(Integer id) {
		if(billMapper.delBill(id)>0){
			return true;
		}
		return false;
	}

}
