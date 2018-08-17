package cn.smbms.service.role;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import cn.smbms.dao.role.RoleMapper;
import cn.smbms.pojo.Role;

@Service
public class RoleServiceImpl implements RoleService {
	@Resource
	private RoleMapper roleMapper;

	@Override
	public List<Role> getRoleList() {
		List<Role> roleList = roleMapper.getRoleList();
		return roleList;
	}

	@Override
	public boolean addRole(Role role) {
		if(roleMapper.addRole(role)>0){
			return true;
		}
		
		return false;
	}

	@Override
	public boolean getRoleByRoleCode(String roleCode) {
		if(roleMapper.getRoleByRoleCode(roleCode)>0){
			return true;
		}
		return false;
	}

	@Override
	public Role getRoleById(Integer id) {
		return roleMapper.getRoleById(id);
	}

	@Override
	public boolean updRole(Role role) {
		if(roleMapper.upeRole(role)>0){
			return true;
		}
		return false;
	}

	@Override
	public boolean delRole(String id) {
		if(roleMapper.delRole(id)>0){
			return true;
		}
		return false;
	}

	@Override
	public boolean getUserByUserRoleId(String roleId) {
		if(roleMapper.getUserRoleByUserRoleId(roleId)>0){
			return true;
		}
		return false;
	}

}
