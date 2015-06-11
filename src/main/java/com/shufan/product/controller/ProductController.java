package com.shufan.product.controller;

import haiyan.cache.CacheUtil;
import haiyan.common.CloseUtil;
import haiyan.common.exception.Warning;
import haiyan.common.intf.database.orm.IDBRecord;
import haiyan.common.intf.database.orm.IDBResultSet;
import haiyan.common.intf.session.IContext;
import haiyan.web.session.WebContext;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shufan.product.dao.ProductDao;
import com.shufan.product.dao.impl.ProductDaoImpl;

@Controller
public class ProductController {
	/**
	 * 套餐浏览主页面
	 * @param req
	 * @param res
	 */
	@RequestMapping(value="/mealHome")
	public void mealHome(HttpServletRequest req, HttpServletResponse res){
		IContext context = null;
		FileReader reader = null;
		BufferedReader br = null;
		PrintWriter writer = null;
		try {
			Object obj = CacheUtil.getData("product", "meal_home");
			if(obj != null){
				res.getWriter().write(obj.toString());
				return;
			}
			context = new WebContext(req, res);
			int size = 20;
			String pageSize = req.getParameter("pageSize");
			if(pageSize != null)
				size = Integer.parseInt(pageSize);
			Template pdTemplate = Velocity.getTemplate("mealHomeList.vm", "utf-8");
			VelocityContext ctx = new VelocityContext();
			ProductDao dao = new ProductDaoImpl(context);
			IDBResultSet result = dao.getAllSetMeal(size, 1);
			if(result.getRecordCount()>0){
				ctx.put("list", result.getRecords());
			}
			StringWriter sw = new StringWriter();
			BufferedWriter bw = new BufferedWriter(sw);
			writer = res.getWriter();
			pdTemplate.merge(ctx, bw);
			bw.flush();
			String backData = sw.toString().replaceAll("\\n", "").replaceAll("\\t", "").replaceAll("\\r", "");
			writer.write(backData);
			CacheUtil.setData("product", "meal_home", backData);
			
		}  catch (Exception e) {
			throw new Warning(500, e);
		}finally{
			CloseUtil.close(br);
			CloseUtil.close(reader);
			CloseUtil.close(writer);
			CloseUtil.close(context);
		}
	}
	/**
	 * 套餐浏览追加页面
	 * @param req
	 * @param res
	 * @param page
	 */
	@RequestMapping("/meal/{page}")
	public void meal(HttpServletRequest req, HttpServletResponse res,@PathVariable("page")int page){
		IContext context = null;
		FileReader reader = null;
		BufferedReader br = null;
		PrintWriter writer = null;
		try {
			if(page == 1){//浏览首页
				mealHome(req,res);
				return;
			}
			String backData = null;
			String callBack = req.getParameter("jsonCallBack");
			Object obj = CacheUtil.getData("product", "meal_"+page);
			if(obj != null){
				backData = obj.toString();
			}else{
				int size = 20;
				String pageSize = req.getParameter("pageSize");
				if(pageSize != null)
					size = Integer.parseInt(pageSize);
				Template pdTemplate = Velocity.getTemplate("mealList.vm", "utf-8");
				VelocityContext ctx = new VelocityContext();
				context = new WebContext(req, res);
				ProductDao dao = new ProductDaoImpl(context);
				IDBResultSet result = dao.getAllSetMeal(size, page);
				if(result.getRecordCount()>0){
					ctx.put("list", result.getRecords());
				}
				StringWriter sw = new StringWriter();
				BufferedWriter bw = new BufferedWriter(sw);
				pdTemplate.merge(ctx, bw);
				bw.flush();
				backData = sw.toString().replaceAll("\\n", "").replaceAll("\\t", "").replaceAll("\\r", "");
				CacheUtil.setData("product", "meal_"+page, backData);
			}
			writer = res.getWriter();
			if(callBack == null)
				writer.write(backData);
			else
				writer.write(callBack+"("+backData+")");
		}  catch (Exception e) {
			throw new Warning(500, e);
		}finally{
			CloseUtil.close(br);
			CloseUtil.close(reader);
			CloseUtil.close(writer);
			CloseUtil.close(context);
		}
	}
	/**
	 * 套餐明细页面
	 * @param req
	 * @param res
	 * @param page
	 */
	@RequestMapping("/mealDetail/{mealId}/{page}")
	public void mealDetail(HttpServletRequest req, HttpServletResponse res,@PathVariable("mealId")String mealId,@PathVariable("page")int page){
		IContext context = null;
		FileReader reader = null;
		BufferedReader br = null;
		PrintWriter writer = null;
		try {
			String backData = null;
			String callBack = req.getParameter("jsonCallBack");
			Object obj = CacheUtil.getData("product", "mealDetail_"+mealId);
			if(obj != null){
				backData = obj.toString();
			}else{
				int size = 20;
				String pageSize = req.getParameter("pageSize");
				if(pageSize != null)
					size = Integer.parseInt(pageSize);
				Template pdTemplate = Velocity.getTemplate("mealDetail.vm", "utf-8");
				VelocityContext ctx = new VelocityContext();
				context = new WebContext(req, res);
				ProductDao dao = new ProductDaoImpl(context);
				IDBResultSet result = dao.getSetMealDetail(mealId, size, page);
				if(result.getRecordCount()>0){
					ctx.put("list", result.getRecords());
				}
				StringWriter sw = new StringWriter();
				BufferedWriter bw = new BufferedWriter(sw);
				pdTemplate.merge(ctx, bw);
				bw.flush();
				backData = sw.toString().replaceAll("\\n", "").replaceAll("\\t", "").replaceAll("\\r", "");
				CacheUtil.setData("product", "mealDetail_"+mealId, backData);
			}
			writer = res.getWriter();
			if(callBack == null)
				writer.write(backData);
			else
				writer.write(callBack+"("+backData+")");
		}  catch (Exception e) {
			throw new Warning(500, e);
		}finally{
			CloseUtil.close(br);
			CloseUtil.close(reader);
			CloseUtil.close(writer);
			CloseUtil.close(context);
		}
	}
	/**
	 * 按产品类型查询产品
	 * @param req
	 * @param res
	 * @param page
	 */
	@RequestMapping("/products/{type}")
	public void products(HttpServletRequest req, HttpServletResponse res,@PathVariable("type")String type){
		IContext context = null;
		FileReader reader = null;
		BufferedReader br = null;
		PrintWriter writer = null;
		try {
			String backData = null;
			String callBack = req.getParameter("jsonCallBack");
			Object obj = CacheUtil.getData("product", "productByType_"+type);
			if(obj != null){
				backData = obj.toString();
			}else{
				int size = 100;
				String pageSize = req.getParameter("pageSize");
				if(pageSize != null)
					size = Integer.parseInt(pageSize);
				Template pdTemplate = Velocity.getTemplate("productList.vm", "utf-8");
				VelocityContext ctx = new VelocityContext();
				context = new WebContext(req, res);
				ProductDao dao = new ProductDaoImpl(context);
				IDBResultSet result = dao.getProductByType(type, size, 1);
				if(result.getRecordCount()>0){
					ctx.put("list", result.getRecords());
				}
				StringWriter sw = new StringWriter();
				BufferedWriter bw = new BufferedWriter(sw);
				pdTemplate.merge(ctx, bw);
				bw.flush();
				backData = sw.toString().replaceAll("\\n", "").replaceAll("\\t", "").replaceAll("\\r", "");
				CacheUtil.setData("product", "productByType_"+type, backData);
			}
			writer = res.getWriter();
			if(callBack == null)
				writer.write(backData);
			else
				writer.write(callBack+"("+backData+")");
		}  catch (Exception e) {
			throw new Warning(500, e);
		}finally{
			CloseUtil.close(br);
			CloseUtil.close(reader);
			CloseUtil.close(writer);
			CloseUtil.close(context);
		}
	}
	/**
	 * 按产品ID查询产品详情
	 * @param req
	 * @param res
	 * @param page
	 */
	@RequestMapping("/product/{id}")
	public void product(HttpServletRequest req, HttpServletResponse res,@PathVariable("id")String productId){
		IContext context = null;
		FileReader reader = null;
		BufferedReader br = null;
		PrintWriter writer = null;
		try {
			String backData = null;
			String callBack = req.getParameter("jsonCallBack");
			Object obj = CacheUtil.getData("product", "product_"+productId);
			if(obj != null){
				backData = obj.toString();
			}else{
				Template pdTemplate = Velocity.getTemplate("product.vm", "utf-8");
				VelocityContext ctx = new VelocityContext();
				context = new WebContext(req, res);
				ProductDao dao = new ProductDaoImpl(context);
				IDBRecord result = dao.getProductById(productId);
				if(result != null){
					ctx.put("product", result);
				}
				StringWriter sw = new StringWriter();
				BufferedWriter bw = new BufferedWriter(sw);
				pdTemplate.merge(ctx, bw);
				bw.flush();
				backData = sw.toString().replaceAll("\\n", "").replaceAll("\\t", "").replaceAll("\\r", "");
				CacheUtil.setData("product", "product_"+productId, backData);
			}
			writer = res.getWriter();
			if(callBack == null)
				writer.write(backData);
			else
				writer.write(callBack+"("+backData+")");
		}  catch (Exception e) {
			throw new Warning(500, e);
		}finally{
			CloseUtil.close(br);
			CloseUtil.close(reader);
			CloseUtil.close(writer);
			CloseUtil.close(context);
		}
	}
}
