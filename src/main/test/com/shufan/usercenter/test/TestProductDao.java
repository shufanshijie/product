package com.shufan.usercenter.test;

import haiyan.common.config.PathUtil;
import haiyan.common.exception.Warning;
import haiyan.common.intf.database.orm.IDBRecord;
import haiyan.common.intf.database.orm.IDBResultSet;
import haiyan.orm.database.DBPage;
import haiyan.orm.database.DBRecord;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;

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
		Calendar cal = Calendar.getInstance();
		int size = 5;
		for(int i=0;i<size;i++){
			cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)+1);
			Date date = cal.getTime();
			int day = date.getDay();
			String week = null;
			if(day == 1){
				week = "周一";
			}else if(day == 2)
				week = "周二";
			else if(day == 3)
				week = "周三";
			else if(day == 4)
				week = "周四";
			else if(day == 5)
				week = "周五";
			else if(day == 6)
				week = "周六";
			else 
				week = "周日";
			IDBRecord record = new DBRecord();
			record.set("NAME", "套餐"+i);
			record.set("OLDPRICE", "10");
			record.set("PRICE", "8");
			record.set("DATE", date);
			record.set("WEEK", week);
			try {
				dao.addMeal(record);
			} catch (Throwable e) {
				throw new Warning(500,e);
			}
		}
	}


}
