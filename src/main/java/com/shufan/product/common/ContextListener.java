package com.shufan.product.common;

import haiyan.bill.database.DBBill;
import haiyan.cache.CacheUtil;
import haiyan.cache.RedisStringDataCache;
import haiyan.common.DebugUtil;
import haiyan.common.LogUtil;
import haiyan.common.PropUtil;
import haiyan.common.VarUtil;
import haiyan.common.cache.AppDataCache;
import haiyan.common.exception.Warning;
import haiyan.common.intf.ILogger;
import haiyan.common.intf.cache.IDataCache;
import haiyan.common.intf.database.IDBBill;
import haiyan.common.intf.database.orm.IDBRecord;
import haiyan.common.intf.database.orm.IDBResultSet;
import haiyan.config.util.ConfigUtil;
import haiyan.exp.ExpUtil;
import haiyan.orm.database.DBPage;

import java.io.File;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.shufan.product.dao.ProductDao;
import com.shufan.product.dao.impl.ProductDaoImpl;


public class ContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}
	@Override
	public void contextInitialized(ServletContextEvent event) {
		String webInfPath = event.getServletContext().getRealPath("WEB-INF");
		init(webInfPath);
	}
	// 使用集群服务
	public static boolean USE_ES = VarUtil.toBool(PropUtil.getProperty("search.engine"));
	public static boolean USE_CACHE = VarUtil.toBool(PropUtil.getProperty("cache.engine"));
	public static boolean USE_DEVLOGGER = VarUtil.toBool(PropUtil.getProperty("logger.develop"));
	public static String JMSADDR = PropUtil.getProperty("jms.addr");
	public static void init(String webInfPath) {
		initLogger();
		initCache();
		initTables(webInfPath);
		initVelocity(webInfPath);
		initJMS();
		DebugUtil.debug("contextInitialized");
	}
	private static void initJMS() {
		ConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD,JMSADDR);
		Connection conn = null;
		try {
			conn = factory.createConnection();
			conn.start();
			Session session = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue("shangjie");
			MessageConsumer consumer = session.createConsumer(destination);
			//TODO 这里需要面向接口编程
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
				private void save(String string) {
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
							IDBResultSet headSet = new DBPage(new ArrayList<IDBRecord>());
							IDBRecord headRecord = headSet.appendRow();
							headRecord.set("NAME", name);
							headRecord.set("OLDPRICE", price);
							headRecord.set("PRICE", huodongPrice);
							headRecord.set("DATE", date);
							headRecord.set("WEEK", week);
							IDBBill bill = new DBBill(null, dao.getSetMealBill());
							bill.setResultSet(0, headSet);
							try {
								dao.addSetMeal(bill);
							} catch (Throwable e) {
								throw new Warning(500,e);
							}
						}
					} catch (JSONException e1) {
						throw new Warning(500,e1);
					}
					
				}
				
			});
		} catch (JMSException e) {
			throw new Warning(500,e);
		}
		
	}
		
	private static void initVelocity(String webInfPath) {
		Velocity.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, webInfPath+File.separator+"velocity");
		Velocity.init();
	}
	private static void initLogger() {
		if (USE_DEVLOGGER==false)
			DebugUtil.logger = new ILogger() { // 强制设置logger接口
	            @Override
	            public void debug(Object info) {
	                LogUtil.info(info);
	            }
	            @Override
	            public void error(Object info, Throwable ex) {
	                LogUtil.error(info, ex);
	            }
	            @Override
	            public void error(Object info) {
	                if (info instanceof Throwable) {
	                    Throwable ex = (Throwable)info;
	                    LogUtil.error(ex.getMessage(), ex);
	                } else
	                    LogUtil.error(info);
	            }
	            @Override
	            public void warn(Object info) {
	                LogUtil.warn(info);
	            }
	        };
	}
	private static void initCache() {
		IDataCache cache = null;
		if (USE_CACHE) {
//			cache = new RedisBinaryDataCacheRemote();
			cache = new RedisStringDataCache();//此缓存存取都是string
			String servers = PropUtil.getProperty("REDISCACHE.SERVERS");
			cache.setServers(servers.split(";"));
	    	try {
	    		cache.initialize();
	    	}catch(Throwable e){
	    		DebugUtil.error(e);
	    		System.exit(0);
	    	}
		} else {
//			cache = new EHDataCache();
			cache = new AppDataCache();
		}
		CacheUtil.setDataCache(cache); // 全局用缓存框架
		ConfigUtil.setDataCache(new AppDataCache()); // 配置用缓存框架
		ConfigUtil.setExpUtil(new ExpUtil()); // 全局用公式引擎
		ConfigUtil.setORMUseCache(true); // 开启ORM自动多级缓存(根据每个缓存级别来使用DataCache实现)
	}
	private static void initTables(String webInfPath) {
		String hyHome = new File(webInfPath).getParent();
		System.setProperty("HAIYAN_HOME", hyHome);
		String rootName = webInfPath + File.separator + "haiyan-config.xml";
		File file = new File(rootName);
		try {
			ConfigUtil.loadRootConfig(file);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		String[] configNames = new String[]{
				"SYS.xml","SYSCACHE.xml","SYSORGA.xml","SYSROLE.xml","SYSOPERATOR.xml","SYSUSERROLE.xml"
				,"T_PRODUCT_SETMEAL_HEAD.xml","T_PRODUCT_SETMEAL_DETAIL.xml","T_PRODUCT_SETMEAL_EVALUATE.xml","T_PRODUCT_PRODUCT.xml"};
		for (String configName:configNames) {
			URL url = ContextListener.class.getClassLoader().getResource(configName);
			if (url==null)
				throw new RuntimeException("config not found:"+configName);
			file = new File(url.getPath());
			if (!file.exists()) {
				throw new RuntimeException("file not found:"+file.getAbsolutePath());
			}
			ConfigUtil.loadTableConfig(file, true);
		}
		{//初始化Bill
			URL url = ContextListener.class.getClassLoader().getResource("B_PRODUCT_SETMEAL.xml");
			if (url==null)
				throw new RuntimeException("config not found:"+"B_PRODUCT_SETMEAL.xml");
			file = new File(url.getPath());
			if (!file.exists()) {
				throw new RuntimeException("file not found:"+file.getAbsolutePath());
			}
			ConfigUtil.loadBillConfig(file, true);
		}
	}

}
