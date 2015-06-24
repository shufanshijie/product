package com.shufan.usercenter.test;

import haiyan.common.config.PathUtil;
import haiyan.common.exception.Warning;
import haiyan.common.intf.database.orm.IDBRecord;
import haiyan.orm.database.DBRecord;

import java.io.File;
import java.sql.Date;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.shufan.product.common.ContextListener;
import com.shufan.product.dao.ProductDao;
import com.shufan.product.dao.impl.ProductDaoImpl;

public class Receive1 {
	static int i =0;
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
		
		ConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD,"nio://localhost:61616");
		Connection conn = null;
		try {
			conn = factory.createConnection();
			conn.start();
			Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue("shangjie");
			MessageConsumer consumer = session.createConsumer(destination);
			
			consumer.setMessageListener(new MessageListener(){
				public void onMessage(Message message) {
					if(message instanceof MapMessage){
						MapMessage map = (MapMessage) message;
						try {
							save(map.getString("list"));
						} catch (JMSException e) {
							e.printStackTrace();
						}
					}else{
						System.out.println("收到非map消息");
					}
				}

				
			});
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
	}
	private static void save(String string) {
		ProductDao dao = new ProductDaoImpl(null);
		JSONArray arr;
		try {
			arr = new JSONArray(string);
			int size = arr.length();
			for(int i=0;i<size;i++){
				JSONObject json = arr.getJSONObject(i);
				String id = json.getString("id");
				double price = json.getDouble("price");
				double huodongPrice = json.getDouble("prefprice");
				String name = json.getString("mealname");
				long time = json.getJSONObject("dispatchingdate").getLong("time");
				String picture = json.getString("picture");
				Date date = new Date(time);
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
				record.set("NAME", name);
				record.set("OLDPRICE", price);
				record.set("PRICE", huodongPrice);
				record.set("DATE", date);
				record.set("WEEK", week);
				record.set("PICTURE", picture);
				record.set("ID", id);
				try {
					dao.addMeal(record);
				} catch (Throwable e) {
					throw new Warning(500,e);
				}
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

}
	