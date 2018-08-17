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

import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;
import cn.smbms.service.role.RoleService;
import cn.smbms.tools.Constants;

@Controller
@RequestMapping("/sys/role")
public class RoleController extends BaseController {

	private Logger logger = Logger.getLogger(RoleController.class);

	@Resource
	private RoleService roleService;

	/**
	 * 显示角色信息
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/roleList.html")
	public String getListRole(Model model) {
		logger.debug("roleList ====================");
		List<Role> roleList = roleService.getRoleList();
		model.addAttribute(roleList);
		return "rolelist";
	}

	/**
	 * 跳转到添加页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/add.html")
	public String add() {
		logger.debug("add ====================");
		return "roleadd";
	}

	/**
	 * 进行添加操作
	 * 
	 * @param role
	 * @return
	 */
	@RequestMapping(value = "/addsave.html", method = RequestMethod.POST)
	public String addSave(Role role, HttpSession session, HttpServletRequest request) {
		role.setCreatedBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
		role.setCreationDate(new Date());
		if (roleService.addRole(role)) {
			return "redirect:/sys/role/roleList.html";
		}
		return "redirect:/sys/bill/syserror.html";
	}

	@RequestMapping(value = "/rcexist.json", method = RequestMethod.GET)
	@ResponseBody
	public Object rcexist(@RequestParam String roleCode) {
		HashMap<String, String> resultMap = new HashMap<String, String>();
		if (roleService.getRoleByRoleCode(roleCode)) {
			resultMap.put("roleCode", "exist");
		} else {
			resultMap.put("roleCode", "noexist");
		}
		return JSONArray.toJSONString(resultMap);
	}

	@RequestMapping(value = "/modify/{roleid}")
	public String rolemodify(@PathVariable String roleid, Model model) {
		Role role = roleService.getRoleById(Integer.parseInt(roleid));
		model.addAttribute(role);
		return "rolemodify";
	}

	@RequestMapping(value = "/modifysave.html")
	public String modify(Role role, HttpSession session, HttpServletRequest request) {
		role.setModifyBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
		role.setModifyDate(new Date());
		if (roleService.updRole(role)) {
			return "redirect:/sys/role/roleList.html";
		}
		return "redirect:/sys/bill/syserror.html";
	}

	@RequestMapping(value = "/delrole.json", method = RequestMethod.GET)
	@ResponseBody
	public Object delrole(@RequestParam String id) {
		HashMap<String, String> resultMap = new HashMap<String, String>();
		if (StringUtils.isNullOrEmpty(id)) {
			resultMap.put("delResult", "notexist");
		} else {
			if (roleService.getUserByUserRoleId(id)) {
				resultMap.put("delResult", "false");
			} else {
				roleService.delRole(id);
				resultMap.put("delResult", "true");
			}
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
