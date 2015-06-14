package com.shufan.usercenter.test;

import haiyan.bill.database.DBBill;
import haiyan.common.config.PathUtil;
import haiyan.common.intf.database.IDBBill;
import haiyan.common.intf.database.orm.IDBRecord;
import haiyan.common.intf.database.orm.IDBResultSet;
import haiyan.orm.database.DBPage;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import com.shufan.product.common.ContextListener;
import com.shufan.product.dao.ProductDao;
import com.shufan.product.dao.impl.ProductDaoImpl;

public class TestProductDao {

	/**
	 * @param args
	 * @throws Throwable 
	 */
	public static void main(String[] args) throws Throwable {
		String s = System.getProperty("user.dir");
		Properties p = PathUtil.getEnvVars();
		p.setProperty("HAIYAN_HOME", s+File.separator+"WebContent");
		ContextListener.init(s+File.separator+"WebContent"+File.separator+"WEB-INF");
		ContextListener.USE_ES=true; 
		
//		testBill();
		testSetMeal();
		
		System.exit(0);
	}

	private static void testSetMeal() {
		ProductDao dao = new ProductDaoImpl(null);
		IDBResultSet result = dao.getAllSetMeal(10, 1);
		System.out.println(" size : "+ result.getRecordCount() + "  name: "+ result.getRecord(0).get("NAME"));
		result = dao.getSetMealDetail("aaaaab", 100, 1);
		System.out.println(" size : "+ result.getRecordCount() + "  name: "+ result.getRecord(0).get("NAME"));
	}

	private static void testBill() throws Throwable {
		ProductDao dao = new ProductDaoImpl(null);

//		IDBBill bill = dao.createSetMeal();
		IDBResultSet headSet = new DBPage(new ArrayList<IDBRecord>());
		IDBRecord headRecord = headSet.appendRow();
		headRecord.set("NAME", "套餐5");
		headRecord.set("PRICE", 10);
		headRecord.set("INTRODUCTION", "好吃不贵5");
		Date date = new Date();
		headRecord.set("DATE", date);
		headRecord.set("WEEK", "周一");
		
		IDBResultSet detailSet = new DBPage(new ArrayList<IDBRecord>());
		IDBRecord detailRecord = detailSet.appendRow();
		detailRecord.set("NAME", "产品3");
		detailRecord.set("PRICE", 4);
		detailRecord.set("WEIGHT", 200);
		detailRecord.set("INTRODUCTION", "好吃3");
		detailRecord = detailSet.appendRow();
		detailRecord.set("NAME", "产品4");
		detailRecord.set("PRICE", 3);
		detailRecord.set("WEIGHT", 200);
		detailRecord.set("INTRODUCTION", "好吃4");
		detailRecord.setStatus(IDBRecord.DELETE);
		detailRecord = detailSet.appendRow();
		detailRecord.set("NAME", "产品5");
		detailRecord.set("PRICE", 4);
		detailRecord.set("WEIGHT", 200);
		detailRecord.set("INTRODUCTION", "好吃5");
		IDBBill bill = new DBBill(null, dao.getSetMealBill());
		bill.setResultSet(0, headSet);
		bill.setResultSet(1, detailSet);
		bill.setBillID("aaaa4F");
		bill = dao.loadSetMeal(bill);
		
//		headRecord.set("MEALID", "aaaa4F");
//		IDBBill success = dao.deleteSetMeal(bill);
//		System.out.println(success);
//		detailRecord.set("INTRODUCTION", "修改的值");
//		dao.saveSetMeal();
		IDBResultSet[] sets = bill.getResultSets();
		System.out.println("size: "+sets.length+" INTRODUCTION ：  "+sets[0].getRecord(0).get("INTRODUCTION"));
		
	}


}
