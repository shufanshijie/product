package com.shufan.usercenter.test;

import haiyan.common.config.PathUtil;
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
		
		testBill();
//		testSetMeal();
		
		System.exit(0);
	}

	private static void testSetMeal() {
		ProductDao dao = new ProductDaoImpl(null);
		IDBResultSet result = dao.getAllMeal(10, 1);
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
		headRecord.set("WEEK", "周日");
		
		
	}


}
