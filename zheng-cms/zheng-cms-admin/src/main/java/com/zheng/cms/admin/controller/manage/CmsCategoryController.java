package com.zheng.cms.admin.controller.manage;

import com.zheng.cms.dao.model.CmsCategory;
import com.zheng.cms.dao.model.CmsCategoryExample;
import com.zheng.cms.rpc.api.CmsCategoryService;
import com.zheng.common.base.BaseController;
import com.zheng.common.util.Paginator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 类目控制器
 * Created by shuzheng on 2016/11/14.
 */
@Controller
@RequestMapping("/manage/category")
@Api(value = "类目控制器", description = "类目管理")
public class CmsCategoryController extends BaseController {

	private final static Logger _log = LoggerFactory.getLogger(CmsCategoryController.class);
	
	@Autowired
	private CmsCategoryService cmsCategoryService;

	/**
	 * 列表
	 * @param page 当前页码
	 * @param rows 每页条数
	 * @param desc 降序排序
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@ApiOperation(value = "类目列表", notes = "获取类目列表并分页")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(
			@RequestParam(required = false, defaultValue = "1", value = "page") int page,
			@RequestParam(required = false, defaultValue = "20", value = "rows") int rows,
			@RequestParam(required = false, defaultValue = "false", value = "desc") boolean desc,
			HttpServletRequest request, ModelMap modelMap) {

		// 数据列表
		CmsCategoryExample cmsCategoryExample = new CmsCategoryExample();
		cmsCategoryExample.setOffset((page - 1) * rows);
		cmsCategoryExample.setLimit(rows);
		cmsCategoryExample.setOrderByClause(desc ? "orders desc" : "orders asc");
		List<CmsCategory> categorys = cmsCategoryService.selectByExample(cmsCategoryExample);

		// 分页对象
		long total = cmsCategoryService.countByExample(cmsCategoryExample);
		Paginator paginator = new Paginator(total, page, rows, request);

		modelMap.put("categorys", categorys);
		modelMap.put("paginator", paginator);
		return "/manage/category/list";
	}
	
	/**
	 * 新增get
	 * @return
	 */
	@ApiOperation(value = "新增类目", notes = "新增类目页")
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add() {
		return "/manage/category/add";
	}
	
	/**
	 * 新增post
	 * @param cmsCategory
	 * @param modelMap
	 * @return
	 */
	@ApiOperation(value = "新增类目", notes = "新增类目提交接口")
	@ApiImplicitParam(name = "cmsCategory", value = "类目实体cmsCategory", required = true, dataType = "CmsCategory")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(CmsCategory cmsCategory, ModelMap modelMap) {
		long time = System.currentTimeMillis();
		cmsCategory.setCtime(time);
		cmsCategory.setOrders(time);
		int count = cmsCategoryService.insertSelective(cmsCategory);
		modelMap.put("count", count);
		_log.info("新增记录id为：{}", cmsCategory.getCategoryId());
		return "redirect:/manage/category/list";
	}

	/**
	 * 删除
	 * @param ids
	 * @param modelMap
	 * @return
	 */
	@ApiOperation(value = "删除类目", notes = "批量删除类目")
	@RequestMapping(value = "/delete/{ids}",method = RequestMethod.GET)
	public String delete(@PathVariable("ids") String ids, ModelMap modelMap) {
		int count = cmsCategoryService.deleteByPrimaryKeys(ids);
		modelMap.put("count", count);
		return "redirect:/manage/category/list";
	}
	
	/**
	 * 修改get
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@ApiOperation(value = "修改类目", notes = "根据id修改类目页")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String update(@PathVariable("id") int id, ModelMap modelMap) {
		CmsCategory category = cmsCategoryService.selectByPrimaryKey(id);
		modelMap.put("category", category);
		return "/manage/category/update";
	}
	
	/**
	 * 修改post
	 * @param id
	 * @param cmsCategory
	 * @param modelMap
	 * @return
	 */
	@ApiOperation(value = "修改类目", notes = "根据id修改类目提交接口")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public String update(@PathVariable("id") int id, CmsCategory cmsCategory, ModelMap modelMap) {
		int count = cmsCategoryService.updateByPrimaryKeySelective(cmsCategory);
		modelMap.put("count", count);
		modelMap.put("id", id);
		return "redirect:/manage/category/list";
	}

}