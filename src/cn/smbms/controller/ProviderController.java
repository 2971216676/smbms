package cn.smbms.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;

import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
import cn.smbms.service.provider.ProviderService;
import cn.smbms.tools.Constants;
import cn.smbms.tools.PageSupport;

@Controller
@RequestMapping("/sys/provider")
public class ProviderController extends BaseController {
	private Logger logger = Logger.getLogger(ProviderController.class);

	@Resource
	private ProviderService providerService;

	@RequestMapping(value = "/providerlist.html")
	public String getProviderList(Model model,
			@RequestParam(value = "queryProCode", required = false) String queryProCode,
			@RequestParam(value = "queryProName", required = false) String queryProName,
			@RequestParam(value = "pageIndex", required = false) String pageIndex) {
		logger.info("getProviderList ---- > queryProCode: " + queryProCode);
		logger.info("getProviderList ---- > queryProName: " + queryProName);
		logger.info("getProviderList ---- > pageIndex: " + pageIndex);
		List<Provider> providerList = null;
		// 璁剧疆椤甸潰瀹归噺
		int pageSize = Constants.pageSize;
		// 褰撳墠椤电爜
		int currentPageNo = 1;

		if (queryProCode == null) {
			queryProCode = "";
		}
		if (queryProName == null) {
			queryProName = "";
		}
		if (pageIndex != null) {
			try {
				currentPageNo = Integer.valueOf(pageIndex);
			} catch (NumberFormatException e) {
				return "redirect:/provider/syserror.html";
			}
		}
		// 鎬绘暟閲忥紙琛級
		int totalCount = providerService.getproviderCount(queryProCode, queryProName);
		// 鎬婚〉鏁�
		PageSupport pages = new PageSupport();
		pages.setCurrentPageNo(currentPageNo);
		pages.setPageSize(pageSize);
		pages.setTotalCount(totalCount);
		int totalPageCount = pages.getTotalPageCount();
		// 鎺у埗棣栭〉鍜屽熬椤�
		if (currentPageNo < 1) {
			currentPageNo = 1;
		} else if (currentPageNo > totalPageCount) {
			currentPageNo = totalPageCount;
		}
		providerList = providerService.getProviderList(queryProName, queryProCode, currentPageNo, pageSize);
		model.addAttribute("providerList", providerList);
		model.addAttribute("queryProCode", queryProCode);
		model.addAttribute("queryProName", queryProName);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("currentPageNo", currentPageNo);
		return "providerlist";
	}

	@RequestMapping(value = "/syserror.html")
	public String sysError() {
		return "syserror";
	}

	/**
	 * 杩涜鏌ョ湅渚涘簲鍟� 鏄庣粏
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public String view(@PathVariable Integer id, Model model) {
		Provider provider = providerService.getProviderById(id);
		model.addAttribute(provider);
		return "providerview";
	}

	/**
	 * 閫氳繃ID 鏌ヨ鎵�鐐瑰嚮鐨勪緵搴斿晢鐨勪俊鎭�
	 * 
	 * @param id
	 * @param modelw's
	 * @return
	 */
	@RequestMapping(value = "/modify/{id}", method = RequestMethod.GET)
	public String modify(@PathVariable Integer id, Model model) {
		Provider provider = providerService.getProviderById(id);
		model.addAttribute(provider);
		return "providermodify";
	}

	@RequestMapping(value = "/modifysave.html", method = RequestMethod.POST)
	public String modifysave(Provider provider, HttpSession session) {
		provider.setModifyBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
		provider.setModifyDate(new Date());
		if (providerService.modify(provider)) {
			return "redirect:/sys/provider/providerlist.html";
		}
		return "providermodify";
	}

	/**
	 * 璺宠浆鍒颁緵搴斿晢 娣诲姞椤甸潰
	 * 
	 * @param provider
	 * @return
	 */
	@RequestMapping(value = "/provideradd.html", method = RequestMethod.GET)
	public String add(@ModelAttribute("provider") Provider provider) {
		return "provideradd";
	}

	@RequestMapping(value = "/provideraddsave.html", method = RequestMethod.POST)
	public String addSave(Provider provider, HttpSession session, HttpServletRequest request,
			@RequestParam(value = "attachs", required = false) MultipartFile[] attachs) {

		String companyLicPicPath = null;
		String orgCodePicPath = null;
		String errorInfo = null;
		boolean flag = true;
		String path = request.getSession().getServletContext().getRealPath("statice" + File.separator + "uploadfiles");
		logger.info("uploadFile path===========>" + path);
		for (int i = 0; i < attachs.length; i++) {
			MultipartFile attach = attachs[i];
			if (!attach.isEmpty()) {
				if (i == 0) {
					errorInfo = "uploadFileError";
				} else if (i == 1) {
					errorInfo = "uploadOcError";
				}

				String oldFileName = attach.getOriginalFilename();// 鍘熸枃浠跺悕
				String prefix = FilenameUtils.getExtension(oldFileName);// 鍘熸枃浠跺悗缂�
				int filesize = 500000;
				if (attach.getSize() > filesize) { // 涓婁紶澶у皬涓嶅緱瓒呰繃 500k
					request.setAttribute(errorInfo, " * 涓婁紶澶у皬涓嶅緱瓒呰繃 500k");
					flag = false;
				} else if (prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")
						|| prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")) {// 涓婁紶鍥剧墖鏍煎紡涓嶆纭�
					String fileName = System.currentTimeMillis() + RandomUtils.nextInt(1000000) + "_Personal.jpg";
					logger.debug("new fileName======== " + attach.getName());
					File targetFile = new File(path, fileName);
					if (!targetFile.exists()) {
						targetFile.mkdirs();
					}
					// 淇濆瓨
					try {
						attach.transferTo(targetFile);
					} catch (Exception e) {
						e.printStackTrace();
						request.setAttribute(errorInfo, " * 涓婁紶澶辫触锛�");
						flag = false;
					}
					if (i == 0) {
						companyLicPicPath = path + File.separator + fileName;
					} else if (i == 1) {
						orgCodePicPath = path + File.separator + fileName;
					}
					logger.debug("companyLicPicPath: " + companyLicPicPath);
					logger.debug("orgCodePicPath: " + orgCodePicPath);

				} else {
					request.setAttribute(errorInfo, " * 涓婁紶鍥剧墖鏍煎紡涓嶆纭�");
					flag = false;
				}
			}
		}
		if (flag) {
			provider.setCreatedBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
			provider.setCreationDate(new Date());
			provider.setCompanyLicPicPath(companyLicPicPath);
			provider.setOrgCodePicPath(orgCodePicPath);
			if (providerService.add(provider)) {
				return "redirect:/sys/provider/providerlist.html";
			}
		}
		return "provideradd";
	}

	@RequestMapping(value = "/delprovider.html", method = RequestMethod.GET)
	@ResponseBody
	public Object delProvider(@RequestParam String proid) {

		HashMap<String, String> resultMap = new HashMap<String, String>();
		if (StringUtils.isNullOrEmpty(proid)) {
			resultMap.put("delResult", "notexist");
		} else {
			if (providerService.billByProviderId(Integer.parseInt(proid))) { // 鍒ゆ柇鏄惁鏈夎鍗�
				if (providerService.deleteProviderById(Integer.parseInt(proid))) { // 鍒犻櫎渚涘簲鍟嗗拰璁㈠崟
					providerService.delBill(Integer.parseInt(proid)); //鍒犻櫎璁㈠崟
					resultMap.put("delResult", "true");
				}
				
			}else{ //娌℃湁鍒� 鐩存帴鍒犻櫎 渚涘簲鍟�
				if (providerService.deleteProviderById(Integer.parseInt(proid))) {
					resultMap.put("delResult", "true");
				}
			}
		}
		
		return JSONArray.toJSONString(resultMap);
	}

}
