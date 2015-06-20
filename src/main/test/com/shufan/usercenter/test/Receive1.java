package com.shufan.usercenter.test;

import haiyan.bill.database.DBBill;
import haiyan.common.config.PathUtil;
import haiyan.common.exception.Warning;
import haiyan.common.intf.database.IDBBill;
import haiyan.common.intf.database.orm.IDBRecord;
import haiyan.common.intf.database.orm.IDBResultSet;
import haiyan.orm.database.DBPage;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
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
				ActiveMQConnection.DEFAULT_PASSWORD,"nio://test.5proapp.com:61616");
		Connection conn = null;
		try {
			conn = factory.createConnection();
			conn.start();
			Session session = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue("shangjie");
			MessageConsumer consumer = session.createConsumer(destination);
			
			consumer.setMessageListener(new MessageListener(){
				public void onMessage(Message message) {
//					TextMessage txt = (TextMessage) message;
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
				double price = json.getDouble("price");
				double huodongPrice = json.getDouble("prefprice");
				String name = json.getString("mealname");
				long time = json.getJSONObject("dispatchingdate").getLong("time");
				IDBResultSet headSet = new DBPage(new ArrayList<IDBRecord>());
				IDBRecord headRecord = headSet.appendRow();
				headRecord.set("NAME", name);
				headRecord.set("PRICE", price);
				headRecord.set("PREFPRICE", huodongPrice);
				headRecord.set("DATE", new Date(time));
				IDBBill bill = new DBBill(null, dao.getSetMealBill());
				bill.setResultSet(0, headSet);
				try {
					dao.addSetMeal(bill);
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
	