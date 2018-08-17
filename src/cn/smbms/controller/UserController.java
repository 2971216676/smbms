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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;

import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;
import cn.smbms.service.role.RoleService;
import cn.smbms.service.user.UserService;
import cn.smbms.tools.Constants;
import cn.smbms.tools.PageSupport;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

	private Logger logger = Logger.getLogger(UserController.class);

	@Resource
	private UserService userService;

	@Resource
	private RoleService roleService;

	@RequestMapping(value = "/sys/userlist.html")
	public String getUserList(Model model, @RequestParam(value = "queryname", required = false) String queryUserName,
			@RequestParam(value = "queryUserRole", required = false) String queryUserRole,
			@RequestParam(value = "pageIndex", required = false) String pageIndex) {
		logger.info("getUserList ---- > queryUserName: " + queryUserName);
		logger.info("getUserList ---- > queryUserRole: " + queryUserRole);
		logger.info("getUserList ---- > pageIndex: " + pageIndex);
		Integer _queryUserRole = null;
		List<User> userList = null;
		List<Role> roleList = null;
		// 璁剧疆椤甸潰瀹归噺
		int pageSize = Constants.pageSize;
		// 褰撳墠椤电爜
		int currentPageNo = 1;

		if (queryUserName == null) {
			queryUserName = "";
		}
		if (queryUserRole != null && !queryUserRole.equals("")) {
			_queryUserRole = Integer.parseInt(queryUserRole);
		}

		if (pageIndex != null) {
			try {
				currentPageNo = Integer.valueOf(pageIndex);
			} catch (NumberFormatException e) {
				return "redirect:/syserror.html";
			}
		}
		// 鎬绘暟閲忥紙琛級
		int totalCount = 0;
		try {
			totalCount = userService.getUserCount(queryUserName, _queryUserRole);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		try {
			userList = userService.getUserList(queryUserName, _queryUserRole, currentPageNo, pageSize);

			roleList = roleService.getRoleList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("userList", userList);
		model.addAttribute("roleList", roleList);
		model.addAttribute("queryUserName", queryUserName);
		model.addAttribute("queryUserRole", queryUserRole);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("currentPageNo", currentPageNo);
		return "userlist";
	}

	@RequestMapping(value = "/sys/view/{id}")
	public String userById(@PathVariable int id, Model model) {
		logger.debug("view id===================== " + id);
		User user = userService.getUserById(id);
		model.addAttribute(user);
		return "userview";
	}

	@RequestMapping(value = "/sys/deluser.json", method = RequestMethod.GET)
	@ResponseBody
	public Object deluser(@RequestParam String id) {
		HashMap<String, String> resultMap = new HashMap<String, String>();
		if (StringUtils.isNullOrEmpty(id)) {
			resultMap.put("delResult", "notexist");
		} else {
			if (userService.deleteUserById(Integer.parseInt(id)))
				resultMap.put("delResult", "true");
			else
				resultMap.put("delResult", "false");
		}
		return JSONArray.toJSONString(resultMap);
	}

	/**
	 * 鑾峰彇瑙掕壊鍒楄〃
	 * 
	 * @return JSON瀵硅薄
	 */
	@RequestMapping(value = "/sys/rolelist", method = RequestMethod.GET)
	@ResponseBody
	public Object getRoleList() {
		List<Role> roleList = null;
		roleList = roleService.getRoleList();
		logger.debug("roleList size: " + roleList.size());
		return JSONArray.toJSONString(roleList);
	}

	@RequestMapping(value = "/sys/useraddsave.html", method = RequestMethod.POST)
	public String addUserSave(User user, HttpSession session, HttpServletRequest request,
			@RequestParam(value = "attachs", required = false) MultipartFile[] attachs) {
		String idPicPath = null;
		String workPicPath = null;
		String errorInfo = null;
		boolean flag = true;
		String path = request.getSession().getServletContext().getRealPath("statics" + File.separator + "uploadfiles");
		logger.info("uploadFile path ============== > " + path);
		for (int i = 0; i < attachs.length; i++) {
			MultipartFile attach = attachs[i];
			if (!attach.isEmpty()) {
				if (i == 0) {
					errorInfo = "uploadFileError";
				} else if (i == 1) {
					errorInfo = "uploadWpError";
				}
				String oldFileName = attach.getOriginalFilename();// 鍘熸枃浠跺悕
				logger.info("uploadFile oldFileName ============== > " + oldFileName);
				String prefix = FilenameUtils.getExtension(oldFileName);// 鍘熸枃浠跺悗缂�
				logger.debug("uploadFile prefix============> " + prefix);
				int filesize = 500000;
				logger.debug("uploadFile size============> " + attach.getSize());
				if (attach.getSize() > filesize) {// 涓婁紶澶у皬涓嶅緱瓒呰繃 500k
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
						idPicPath = path + File.separator + fileName;
					} else if (i == 1) {
						workPicPath = path + File.separator + fileName;
					}
					logger.debug("idPicPath: " + idPicPath);
					logger.debug("workPicPath: " + workPicPath);

				} else {
					request.setAttribute(errorInfo, " * 涓婁紶鍥剧墖鏍煎紡涓嶆纭�");
					flag = false;
				}
			}
		}
		if (flag) {
				user.setCreatedBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
				user.setCreationDate(new Date());
				user.setIdPicPath(idPicPath);
				user.setWorkPicPath(workPicPath);
			
			if (userService.add(user)) {
				return "redirect:/user/sys/userlist.html";
			}
		}
		return "useradd";
	}

	@RequestMapping(value = "/sys/useradd.html")
	public String useradd() {
		logger.debug("useradd ===============");
		return "useradd";
	}

	@RequestMapping(value = "/sys/pwdmodify.html")
	public String updPwd() {
		logger.debug("updPwd ===============");
		return "pwdmodify";
	}

	@RequestMapping(value = "/sys/pwdmodify.json", method = RequestMethod.POST)
	@ResponseBody
	public Object getPwdByUserId(@RequestParam String oldpassword, HttpSession session) {
		logger.debug("getPwdByUserId oldpassword ===================== " + oldpassword);
		User user = (User) session.getAttribute(Constants.USER_SESSION);
		HashMap<String, String> resultMap = new HashMap<String, String>();
		if (null == user) {// session杩囨湡
			resultMap.put("result", "sessionerror");
		} else if (StringUtils.isNullOrEmpty(oldpassword)) {// 鏃у瘑鐮佽緭鍏ヤ负绌�
			resultMap.put("result", "error");
		} else {
			String sessionPwd = user.getUserPassword();
			if (oldpassword.equals(sessionPwd)) {
				resultMap.put("result", "true");
			} else {// 鏃у瘑鐮佽緭鍏ヤ笉姝ｇ‘
				resultMap.put("result", "false");
			}
		}
		return JSONArray.toJSONString(resultMap);
	}

	@RequestMapping(value = "/sys/pwdsave.html", method = RequestMethod.POST)
	public String pwdSave(@RequestParam(value = "newpassword") String newPassword, HttpSession session,
			HttpServletRequest request) {
		boolean flag = false;
		Object o = session.getAttribute(Constants.USER_SESSION);
		if (o != null && !StringUtils.isNullOrEmpty(newPassword)) {
			flag = userService.updatePwd(((User) o).getId(), newPassword);
			if (flag) {
				request.setAttribute(Constants.SYS_MESSAGE, "淇敼瀵嗙爜鎴愬姛,璇烽��鍑哄苟浣跨敤鏂板瘑鐮侀噸鏂扮櫥褰曪紒");
				session.removeAttribute(Constants.USER_SESSION);// session娉ㄩ攢
				return "login";
			} else {
				request.setAttribute(Constants.SYS_MESSAGE, "淇敼瀵嗙爜澶辫触锛�");
			}
		} else {
			request.setAttribute(Constants.SYS_MESSAGE, "淇敼瀵嗙爜澶辫触锛�");
		}
		return "pwdmodify";
	}

	@RequestMapping(value = "/login.html")
	public String login() {
		logger.debug("UserController welcome SMBMS===============");
		return "login";
	}

	@RequestMapping(value = "/dologin.html", method = RequestMethod.POST)
	public String doLogin(@RequestParam String userCode, @RequestParam String userPassword, HttpSession session,
			HttpServletRequest requrst) {
		logger.debug("doLogin==================================");

		User user = userService.login(userCode, userPassword);
		if (null == user) {
			throw new RuntimeException("閻€劍鍩涢崥宥勭瑝鐎涙ê婀敍锟�");
		} else if (null != user && !user.getUserPassword().equals(userPassword)) {
			throw new RuntimeException("鐎靛棛鐖滄潏鎾冲弳闁挎瑨顕ら敍锟�");
		}
		session.setAttribute(Constants.USER_SESSION, user);
		return "redirect:/user/sys/main.html";
	}

	@RequestMapping(value = "/sys/main.html")
	public String main() {
		return "frame";
	}

	@ExceptionHandler(value = { Exception.class })
	public String ex(RuntimeException e, HttpServletRequest req) {
		req.setAttribute("error", e);
		return "login";
	}

	@RequestMapping(value = "/sys/logout.html")
	public String logout(HttpSession session) {
		session.removeAttribute(Constants.USER_SESSION);
		return "login";
	}
	
	
	@RequestMapping(value="/sys/ucexist.json")
	@ResponseBody
	public Object userCodeIsExit(@RequestParam String userCode){
		logger.debug("userCodeIsExit userCode===================== "+userCode);
		HashMap<String, String> resultMap = new HashMap<String, String>();
		if(StringUtils.isNullOrEmpty(userCode)){
			resultMap.put("userCode", "exist");
		}else{
			User user = null;
			try {
				user = userService.selectUserCodeExist(userCode);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(null != user)
				resultMap.put("userCode", "exist");
			else
				resultMap.put("userCode", "noexist");
		}
		return JSONArray.toJSONString(resultMap);
	}
}
