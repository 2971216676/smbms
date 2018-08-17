package cn.smbms.service.provider;

import java.io.File;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import cn.smbms.dao.provider.ProviderMapper;
import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
@Service
public class ProviderServiceImpl implements ProviderService {
	
	@Resource
	private ProviderMapper providerMapper;

	@Override
	public List<Provider> getProviderList(String proName, String proCode,
			int currentPageNo, int pageSize) {
		
		System.out.println("query proName ---- > " + proName);
		System.out.println("query proCode ---- > " + proCode);
		currentPageNo = (currentPageNo - 1) * pageSize;
		return providerMapper.getProviderList( proName,proCode,currentPageNo,pageSize);
	}
	@Override
	public int getproviderCount(String proName, String proCode) {
		return providerMapper.getProviderCount(proName, proCode);
	}
	@Override
	public boolean add(Provider provider) {
		// TODO Auto-generated method stub
		boolean flag = false;
		try {
			if(providerMapper.add(provider) > 0)
				flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	@Override
	public Provider getProviderById(Integer id) {
		Provider provider = null;
		try{
			provider = providerMapper.getProviderById(id);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return provider;
	}
	
	@Override
	public boolean modify(Provider provider) {
		boolean flag = false;
		try {
			if(providerMapper.modify(provider) > 0)
				flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	@Override
	public boolean deleteProviderById(Integer delId) {
		boolean flag = true;
		
		Provider provider = providerMapper.getProviderById(delId);
		if (provider.getCompanyLicPicPath() != null && !provider.getCompanyLicPicPath().equals("")) {
			
			File file = new File(provider.getCompanyLicPicPath());
			if (file.exists()) {
				flag = file.delete();
			}
		}
		if (provider.getOrgCodePicPath() != null && !provider.getOrgCodePicPath().equals("")) {
			
			File file = new File(provider.getOrgCodePicPath());
			if (file.exists()) {
				flag = file.delete();
			}
		}
		if (flag) {
			if (providerMapper.deleteProviderById(delId) < 1)
				flag = false;
		}
		return flag;
	}
	@Override
	public void delBill(Integer billId) {
		providerMapper.delBill(billId);
	}
	@Override
	public boolean billByProviderId(Integer providerId) {
		if(providerMapper.billByProviderId(providerId)>0){
			return true;
		}
		return false;
	}
	
	@Override
	public List<Provider> getProviderList(){
		// TODO Auto-generated method stub
		return providerMapper.getProvider();
	}


}
