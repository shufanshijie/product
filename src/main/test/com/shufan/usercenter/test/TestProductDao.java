package com.shufan.usercenter.test;

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
		result = dao.getSetMealDetail("aaaaab", 10, 1);
		System.out.println(" size : "+ result.getRecordCount() + "  name: "+ result.getRecord(0).get("NAME"));
	}

	private static void testBill() {
		ProductDao dao = new ProductDaoImpl(null);
		IDBResultSet headSet = new DBPage(new ArrayList<IDBRecord>());
		IDBRecord headRecord = headSet.appendRow();
		headRecord.set("NAME", "套餐2");
		headRecord.set("PRICE", 10);
		headRecord.set("ID", "aaaaif");
		headRecord.set("INTRODUCTION", "好吃不贵2");
		Date date = new Date();
		headRecord.set("DATE", date);
		headRecord.set("WEEK", "周一");
		IDBResultSet detailSet = new DBPage(new ArrayList<IDBRecord>());
		IDBRecord detailRecord = detailSet.appendRow();
		detailRecord.set("NAME", "产品1");
		detailRecord.set("PRICE", 4);
		detailRecord.set("WEIGHT", 200);
		detailRecord.set("INTRODUCTION", "好吃1");
		detailRecord = detailSet.appendRow();
		detailRecord.set("NAME", "产品2");
		detailRecord.set("PRICE", 3);
		detailRecord.set("WEIGHT", 200);
		detailRecord.set("INTRODUCTION", "好吃2");
		detailRecord = detailSet.appendRow();
		detailRecord.set("NAME", "产品3");
		detailRecord.set("PRICE", 4);
		detailRecord.set("WEIGHT", 200);
		detailRecord.set("INTRODUCTION", "好吃3");
		IDBBill bill = dao.addSetMeal(headSet, detailSet);
		IDBResultSet[] sets = bill.getResultSets();
		System.out.println("size: "+sets.length+" INTRODUCTION ：  "+sets[0].getRecord(0).get("INTRODUCTION"));
		
	}


}
