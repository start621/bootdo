package com.bootdo.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bootdo.system.domain.MenuDO;
import com.bootdo.system.domain.SysDeptDO;
import com.bootdo.system.service.SysDeptService;
import com.bootdo.common.controller.BaseController;
import com.bootdo.common.domain.Tree;
import com.bootdo.common.utils.R;

/**
 * 部门管理
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-09-27 14:40:36
 */
 
@Controller
@RequestMapping("/system/sysDept")
public class SysDeptController extends BaseController{
	@Autowired
	private SysDeptService sysDeptService;
	
	@GetMapping()
	@RequiresPermissions("system:sysDept:sysDept")
	String SysDept(){
	    return "system/sysDept/sysDept";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("system:sysDept:sysDept")
	public List<SysDeptDO> list(){
		//查询列表数据
       // Query query = new Query(params);
		Map<String, Object> query = new HashMap<>();
		List<SysDeptDO> sysDeptList = sysDeptService.list(query);
	//	int total = sysDeptService.count(query);
		//PageUtils pageUtils = new PageUtils(sysDeptList, total);
		return sysDeptList;
	}
	
	@GetMapping("/add/{pId}")
	@RequiresPermissions("system:sysDept:add")
	String add(@PathVariable("pId") Long pId,Model model){
		model.addAttribute("pId", pId);
		if (pId == 0) {
			model.addAttribute("pName", "总部门");
		} else {
			model.addAttribute("pName", sysDeptService.get(pId).getName());
		}
	    return "system/sysDept/add";
	}

	@GetMapping("/edit/{deptId}")
	@RequiresPermissions("system:sysDept:edit")
	String edit(@PathVariable("deptId") Long deptId,Model model){
		SysDeptDO sysDept = sysDeptService.get(deptId);
		model.addAttribute("sysDept", sysDept);
	    return "system/sysDept/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("system:sysDept:add")
	public R save( SysDeptDO sysDept){
		if ("test".equals(getUsername())) {
			return R.error(1, "演示系统不允许修改,完整体验请部署程序");
		}
		if(sysDeptService.save(sysDept)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("system:sysDept:edit")
	public R update( SysDeptDO sysDept){
		if ("test".equals(getUsername())) {
			return R.error(1, "演示系统不允许修改,完整体验请部署程序");
		}
		if(sysDeptService.update(sysDept)>0){
			return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("system:sysDept:remove")
	public R remove( Long deptId){
		if ("test".equals(getUsername())) {
			return R.error(1, "演示系统不允许修改,完整体验请部署程序");
		}
		if(sysDeptService.remove(deptId)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("system:sysDept:batchRemove")
	public R remove(@RequestParam("ids[]") Long[] deptIds){
		if ("test".equals(getUsername())) {
			return R.error(1, "演示系统不允许修改,完整体验请部署程序");
		}
		sysDeptService.batchRemove(deptIds);
		return R.ok();
	}
	
	
	@GetMapping("/tree")
	@ResponseBody
	public Tree<SysDeptDO> tree() {
		Tree<SysDeptDO> tree = new Tree<SysDeptDO>();
		tree = sysDeptService.getTree();
		return tree;
	}

	@GetMapping("/treeView")
	String treeView() {
		return "system/sysDept/deptTree";
	}
	
}
