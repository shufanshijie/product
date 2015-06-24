package com.shufan.usercenter.test;

import java.util.Calendar;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Session;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Sender {
	private static final int SEND_NUMBER = 5;

	public static void main(String[] args) {
		ConnectionFactory connectionFactory;
		Connection connection = null;
		Session session;
		Destination destination;
		MessageProducer producer;
		connectionFactory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD, "tcp://localhost:61616");
		try {
			connection = connectionFactory.createConnection();
			connection.start();
			session = connection.createSession(Boolean.TRUE,
					Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue("shangjie");
			producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			sendMessage(session, producer);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != connection)
					connection.close();
			} catch (Throwable ignore) {
			}
		}
	}

	public static void sendMessage(Session session, MessageProducer producer)
			throws Exception {
		for (int i = 1; i <= 30; i++) {
			MapMessage message = session.createMapMessage();
			message.setString("hello","world");
			JSONArray list = new JSONArray();
			JSONObject map = new JSONObject();
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_MONTH, i);
			map.put("dispatchingdate",cal.getTime());
			map.put("mealname","套餐"+i);
			map.put("price",8);
			map.put("prefprice",6);
			map.put("id", "aaa"+i);
			map.put("picture", "http://images.5proapp.com/1004_3");
			list.add(map);
			message.setString("list",list.toString());
			System.out.println(message.toString());
			producer.send(message);
		}
	}
}

