package com.shufan.product.controller;

import haiyan.cache.CacheUtil;
import haiyan.common.CloseUtil;
import haiyan.common.exception.Warning;
import haiyan.common.intf.database.orm.IDBRecord;
import haiyan.common.intf.database.orm.IDBResultSet;
import haiyan.common.intf.web.IWebContext;
import haiyan.web.orm.RequestRecord;
import haiyan.web.session.WebContextFactory;

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
import org.springframework.web.bind.annotation.RequestMethod;

import com.shufan.product.dao.ProductDao;
import com.shufan.product.dao.impl.ProductDaoImpl;


@Controller
public class EvaluateController {
	/**
	 * 根据套餐Id获取评论列表
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "evaluates/{mealId}/{page}", method = RequestMethod.GET)
	public void getEvaluateList(HttpServletRequest req, HttpServletResponse res
			,@PathVariable("mealId")String mealId,@PathVariable("page")int page){
		IWebContext context = null;
		FileReader reader = null;
		BufferedReader br = null;
		PrintWriter writer = null;
		try {
			String backData = null;
			String callBack = req.getParameter("jsonCallBack");
			Object obj = CacheUtil.getData("product", "evaluate_"+mealId);
			if(obj != null){
				backData = obj.toString();
			}else{
				Template pdTemplate = Velocity.getTemplate("evaluateList.vm", "utf-8");
				VelocityContext ctx = new VelocityContext();
				context = WebContextFactory.createDBContext(req, res);
				String sPageSize = req.getParameter("maxPageSize");
				int maxPageSize = sPageSize == null ? 20 : Integer.parseInt(sPageSize);
				ProductDao dao = new ProductDaoImpl(context);
				IDBResultSet list = dao.getEvaluates(mealId, maxPageSize, page);
				if(list.getRecordCount()>0){
					ctx.put("list", list.getRecords());
				}
				StringWriter sw = new StringWriter();
				BufferedWriter bw = new BufferedWriter(sw);
				pdTemplate.merge(ctx, bw);
				bw.flush();
				backData = sw.toString().replaceAll("\\n", "").replaceAll("\\t", "").replaceAll("\\r", "");
				CacheUtil.setData("gifted", "evaluate_"+mealId, backData);
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
	 * 添加评论
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "evaluate/add/{mealId}", method = RequestMethod.POST)
	public void addEvaluate(HttpServletRequest req, HttpServletResponse res,@PathVariable("mealId")String mealId){
		IWebContext context = null;
		FileReader reader = null;
		BufferedReader br = null;
		PrintWriter writer = null;
		try {
			String backData = null;
			Template pdTemplate = Velocity.getTemplate("evaluate.vm", "utf-8");
			VelocityContext ctx = new VelocityContext();
			context = WebContextFactory.createDBContext(req, res);
			ProductDao dao = new ProductDaoImpl(context);
			IDBRecord record = new RequestRecord(req, res, dao.getMealEvaluateTable());
			record = dao.addEvaluate(mealId,record);
			if(record != null){
				ctx.put("evaluate", record);
			}
			StringWriter sw = new StringWriter();
			BufferedWriter bw = new BufferedWriter(sw);
			pdTemplate.merge(ctx, bw);
			bw.flush();
			backData = sw.toString().replaceAll("\\n", "").replaceAll("\\t", "").replaceAll("\\r", "");
			String callBack = req.getParameter("jsonCallBack");
			writer = res.getWriter();
			if(callBack == null)
				writer.write(backData);
			else
				writer.write(callBack+"("+backData+")");
			CacheUtil.deleteData("gifted", "evaluate_"+record.get("MEIALID"));
		}  catch (Throwable e) {
			throw new Warning(500, e);
		}finally{
			CloseUtil.close(br);
			CloseUtil.close(reader);
			CloseUtil.close(writer);
			CloseUtil.close(context);
		}
	}
}
