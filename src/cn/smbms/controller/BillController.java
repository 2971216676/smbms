package cn.smbms.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;

import cn.smbms.pojo.Bill;
import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
import cn.smbms.service.bill.BillService;
import cn.smbms.service.provider.ProviderService;
import cn.smbms.tools.Constants;
import cn.smbms.tools.PageSupport;

@Controller
@RequestMapping("/sys/bill")
public class BillController extends BaseController {
	private Logger logger = Logger.getLogger(BillController.class);

	@Resource
	private BillService billService;

	@Resource
	private ProviderService providerService;

	/**
	 * 显示订单
	 * 
	 * @param model
	 * @param productName
	 * @param providerId
	 * @param isPayment
	 * @param pageIndex
	 * @return
	 */
	@RequestMapping(value = "/list.html")
	public String getBillList(Model model,
			@RequestParam(value = "queryProductName", required = false) String productName,
			@RequestParam(value = "queryProviderId", required = false) String providerId,
			@RequestParam(value = "queryIsPayment", required = false) String isPayment,
			@RequestParam(value = "pageIndex", required = false) String pageIndex) {
		logger.info("getBillList ---- > ProductName: " + productName);
		logger.info("getUserList ---- > ProviderId: " + providerId);
		logger.info("getUserList ---- > IsPayment: " + isPayment);
		logger.info("getUserList ---- > pageIndex: " + pageIndex);
		Integer _queryProviderId = null;
		Integer _queryIsPayment = null;
		List<Bill> billList = null;
		List<Provider> providerList = null;
		// 设置页面容量
		int pageSize = Constants.pageSize;
		// 当前页码
		int currentPageNo = 1;

		if (productName == null) {
			productName = "";
		}
		if (providerId != null && !providerId.equals("")) {
			_queryProviderId = Integer.parseInt(providerId);
		}
		if (isPayment != null && !isPayment.equals("")) {
			_queryIsPayment = Integer.parseInt(isPayment);
		}

		if (pageIndex != null) {
			try {
				currentPageNo = Integer.valueOf(pageIndex);
			} catch (NumberFormatException e) {
				return "redirect:/syserror.html";
			}
		}
		// 总数量（表）
		int totalCount = 0;
		try {
			totalCount = billService.getBillCount(productName, _queryProviderId, _queryIsPayment);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 总页数
		PageSupport pages = new PageSupport();
		pages.setCurrentPageNo(currentPageNo);
		pages.setPageSize(pageSize);
		pages.setTotalCount(totalCount);
		int totalPageCount = pages.getTotalPageCount();
		// 控制首页和尾页
		if (currentPageNo < 1) {
			currentPageNo = 1;
		} else if (currentPageNo > totalPageCount) {
			currentPageNo = totalPageCount;
		}
		try {
			billList = billService.getBillList(productName, _queryProviderId, _queryIsPayment, currentPageNo, pageSize);
			providerList = providerService.getProviderList(); // 获取供应商列表
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("billList", billList);
		model.addAttribute("providerList", providerList);
		model.addAttribute("queryProductName", productName);
		model.addAttribute("queryProviderId", providerId);
		model.addAttribute("queryIsPayment", isPayment);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("currentPageNo", currentPageNo);
		return "billlist";
	}

	/**
	 * 跳转到订单添加页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "billadd.html")
	public String addBill() {
		logger.debug("addBill ===================");
		return "billadd";
	}

	/**
	 * 获取供应商列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "providerName.json", method = RequestMethod.GET)
	@ResponseBody
	public Object getProviderName() {
		List<Provider> providerList = providerService.getProviderList();
		logger.debug("providerList size: " + providerList.size());
		return JSONArray.toJSONString(providerList);
	}

	@RequestMapping(value = "addsave.html", method = RequestMethod.POST)
	public String addsave(Bill bill, HttpSession session, HttpServletRequest request) {
		bill.setCreationDate(new Date());
		bill.setCreatedBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
		if (billService.addBill(bill)) {
			return "redirect:/sys/bill/list.html";
		}
		return "redirect:/sys/bill/syserror.html";
	}

	@RequestMapping("/modify/{id}")
	public String modify(@PathVariable Integer id, Model model) {
		logger.debug("modify id===================== " + id);
		Bill bill = billService.getBillByid(id);
		model.addAttribute(bill);
		return "billmodify";
	}

	@RequestMapping(value = "/modifysave.html", method = RequestMethod.POST)
	public String modifysave(Bill bill, HttpSession session, HttpServletRequest request) {
		bill.setModifyBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
		bill.setModifyDate(new Date());
		if (billService.updBill(bill)) {
			return "redirect:/sys/bill/list.html";
		}
		return "redirect:/sys/bill/syserror.html";
	}

	@RequestMapping(value="/view/{id}")
	public String view(@PathVariable Integer id, Model model){
		logger.debug("view id===================== " + id);
		Bill bill = billService.getBillByid(id);
		model.addAttribute(bill);
		return "billview";
	}
	
	
	@RequestMapping(value="delBill.json",method=RequestMethod.GET)
	@ResponseBody
	public Object delBill(@RequestParam("billid") String billid){
		HashMap<String, String> resultMap = new HashMap<String, String>();
		if(StringUtils.isNullOrEmpty(billid)){
			resultMap.put("delResult", "notexist");
		}else{
			if(billService.delBill(Integer.parseInt(billid)))
				resultMap.put("delResult", "true");
			else
				resultMap.put("delResult", "false");
		}
		return JSONArray.toJSONString(resultMap);
	}
	
	/**
	 * 出现错误 跳到 error 页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "syserror.html")
	public String error() {
		return "error";
	}
}
