package com.shufan.product.dao.impl;

import haiyan.common.CloseUtil;
import haiyan.common.exception.Warning;
import haiyan.common.intf.database.IDBFilter;
import haiyan.common.intf.database.orm.IDBRecord;
import haiyan.common.intf.database.orm.IDBResultSet;
import haiyan.common.intf.session.IContext;
import haiyan.config.castorgen.Table;
import haiyan.config.util.ConfigUtil;
import haiyan.orm.database.TableDBContextFactory;
import haiyan.orm.database.sql.SQLDBFilter;
import haiyan.orm.intf.database.ITableDBManager;
import haiyan.orm.intf.session.ITableDBContext;

import java.sql.Date;
import java.util.Calendar;

import com.shufan.product.dao.ProductDao;

public class ProductDaoImpl implements ProductDao {
	
	protected IContext parentContext;
	protected ProductDaoImpl() {
	}
	public ProductDaoImpl(IContext parentContext) {
		this.parentContext = parentContext;
	}
	public IContext getParentContext() {
		return parentContext;
	}
	private static Table productTable;
	public Table getProductTable() {
		if (productTable==null)
			synchronized(ProductDaoImpl.class) {
				if (productTable==null)
					productTable = ConfigUtil.getTable("T_PRODUCT_PRODUCT");
			}
		return productTable;
	}
	private static Table setMealTable;
	public Table getMealTable() {
		if (setMealTable==null)
			synchronized(ProductDaoImpl.class) {
				if (setMealTable==null)
					setMealTable = ConfigUtil.getTable("T_PRODUCT_SETMEAL");
			}
		return setMealTable;
	}
	private static Table setMealEvaluateTable;
	public Table getMealEvaluateTable() {
		if (setMealEvaluateTable==null)
			synchronized(ProductDaoImpl.class) {
				if (setMealEvaluateTable==null)
					setMealEvaluateTable = ConfigUtil.getTable("T_PRODUCT_SETMEAL_EVALUATE");
			}
		return setMealEvaluateTable;
	}

	@Override
	public IDBResultSet getAllMeal(int maxPageSize, int page) {
		ITableDBContext context = null;
		ITableDBManager dbm = null;
		try {
			context = TableDBContextFactory.createDBContext(parentContext);
			dbm = context.getDBM();
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)+1);
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			Date date = new Date(cal.getTime().getTime());
			IDBFilter filter = new SQLDBFilter(" and DATE >= ?", new Object[]{date});
			IDBResultSet result = dbm.select(context, getMealTable(), filter,maxPageSize,page);
			return result;
		} catch (Throwable e) {
			throw Warning.wrapException(e);
		}finally{
			CloseUtil.close(dbm);
			CloseUtil.close(context);
		}
	}

	@Override
	public IDBResultSet getAllProduct(int maxPageSize, int page) {
		ITableDBContext context = null;
		ITableDBManager dbm = null;
		try {
			context = TableDBContextFactory.createDBContext(parentContext);
			dbm = context.getDBM();
			IDBRecord record = dbm.createRecord();
			IDBResultSet result = dbm.select(context, getProductTable(), record,maxPageSize,page);
			return result;
		} catch (Throwable e) {
			throw Warning.wrapException(e);
		}finally{
			CloseUtil.close(dbm);
			CloseUtil.close(context);
		}
	}

	@Override
	public IDBResultSet getProductByType(String type, int maxPageSize, int page) {
		ITableDBContext context = null;
		ITableDBManager dbm = null;
		try {
			context = TableDBContextFactory.createDBContext(parentContext);
			dbm = context.getDBM();
			IDBRecord record = dbm.createRecord();
			record.set("TYPE", type);
			IDBResultSet result = dbm.select(context, getProductTable(), record,maxPageSize,page);
			return result;
		} catch (Throwable e) {
			throw Warning.wrapException(e);
		}finally{
			CloseUtil.close(dbm);
			CloseUtil.close(context);
		}
	}


	@Override
	public IDBRecord getProductById(String productId) {
		ITableDBContext context = null;
		ITableDBManager dbm = null;
		try {
			context = TableDBContextFactory.createDBContext(parentContext);
			dbm = context.getDBM();
			IDBRecord result = dbm.select(context, getProductTable(), productId);
			return result;
		} catch (Throwable e) {
			throw Warning.wrapException(e);
		}finally{
			CloseUtil.close(dbm);
			CloseUtil.close(context);
		}
	}

	@Override
	public IDBResultSet getEvaluates(String setMealId, int maxPageSize, int page) {
		ITableDBContext context = null;
		ITableDBManager dbm = null;
		try {
			context = TableDBContextFactory.createDBContext(parentContext);
			dbm = context.getDBM();
			IDBRecord record = dbm.createRecord();
			record.set("MEIALID", setMealId);
			IDBResultSet result = dbm.select(context, getMealEvaluateTable(), record,maxPageSize,page);
			return result;
		} catch (Throwable e) {
			throw Warning.wrapException(e);
		}finally{
			CloseUtil.close(dbm);
			CloseUtil.close(context);
		}
	}

	@Override
	public IDBRecord addEvaluate(String mealID,IDBRecord record) {
		ITableDBContext context = null;
		ITableDBManager dbm = null;
		try {
			context = TableDBContextFactory.createDBContext(parentContext);
			dbm = context.getDBM();
			record.set("MEIALID", mealID);
			IDBRecord result = dbm.insert(context, getMealEvaluateTable(), record);
			return result;
		} catch (Throwable e) {
			throw Warning.wrapException(e);
		}finally{
			CloseUtil.close(dbm);
			CloseUtil.close(context);
		}
	}

	@Override
	public boolean deleteEvaluate(String[] ids) {
		ITableDBContext context = null;
		ITableDBManager dbm = null;
		try {
			context = TableDBContextFactory.createDBContext(parentContext);
			dbm = context.getDBM();
			dbm.openTransaction();
			boolean result = dbm.delete(context, getMealEvaluateTable(), ids);
			dbm.commit();
			return result;
		} catch (Throwable e) {
			throw Warning.wrapException(e);
		}finally{
			CloseUtil.close(dbm);
			CloseUtil.close(context);
		}
	}
	@Override
	public IDBRecord addProduct(IDBRecord record) {
		ITableDBContext context = null;
		ITableDBManager dbm = null;
		try {
			context = TableDBContextFactory.createDBContext(parentContext);
			dbm = context.getDBM();
			IDBRecord result = dbm.insert(context, getProductTable(), record);
			return result;
		} catch (Throwable e) {
			throw Warning.wrapException(e);
		}finally{
			CloseUtil.close(dbm);
			CloseUtil.close(context);
		}
	}
	@Override
	public IDBRecord updateProduct(IDBRecord record) {
		ITableDBContext context = null;
		ITableDBManager dbm = null;
		try {
			context = TableDBContextFactory.createDBContext(parentContext);
			dbm = context.getDBM();
			IDBRecord result = dbm.update(context, getProductTable(), record);
			return result;
		} catch (Throwable e) {
			throw Warning.wrapException(e);
		}finally{
			CloseUtil.close(dbm);
			CloseUtil.close(context);
		}
	}
	@Override
	public boolean deleteProduct(String[] ids) {
		ITableDBContext context = null;
		ITableDBManager dbm = null;
		try {
			context = TableDBContextFactory.createDBContext(parentContext);
			dbm = context.getDBM();
			dbm.openTransaction();
			boolean result = dbm.delete(context, getProductTable(), ids);
			dbm.commit();
			return result;
		} catch (Throwable e) {
			throw Warning.wrapException(e);
		}finally{
			CloseUtil.close(dbm);
			CloseUtil.close(context);
		}
	}
	@Override
	public IDBRecord addMeal(IDBRecord record) throws Throwable {
		ITableDBContext context = null;
		ITableDBManager dbm = null;
		try {
			context = TableDBContextFactory.createDBContext(parentContext);
			dbm = context.getDBM();
			context.openTransaction();
			record = dbm.insert(context, getMealTable(), record);
			context.commit();
			return record;
		} catch (Throwable e) {
			throw Warning.wrapException(e);
		}finally{
			CloseUtil.close(context);
		}
	}
	@Override
	public IDBRecord loadMealById(String mealId) {
		ITableDBContext context = null;
		ITableDBManager dbm = null;
		try {
			context = TableDBContextFactory.createDBContext(parentContext);
			dbm = context.getDBM();
			return dbm.select(context, getMealTable(), mealId);
		} catch (Throwable e) {
			throw Warning.wrapException(e);
		}finally{
			CloseUtil.close(context);
		}
	}
	@Override
	public IDBRecord updateMeal(IDBRecord record) throws Throwable {
		ITableDBContext context = null;
		ITableDBManager dbm = null;
		try {
			context = TableDBContextFactory.createDBContext(parentContext);
			dbm = context.getDBM();
			context.openTransaction();
			record = dbm.update(context, getMealTable(), record);
			context.commit();
			return record;
		} catch (Throwable e) {
			throw Warning.wrapException(e);
		}finally{
			CloseUtil.close(context);
		}
	}
	@Override
	public boolean deleteMeal(String[] ids) throws Throwable {
		ITableDBContext context = null;
		ITableDBManager dbm = null;
		try {
			context = TableDBContextFactory.createDBContext(parentContext);
			dbm = context.getDBM();
			dbm.openTransaction();
			boolean success = dbm.delete(context, getMealTable(), ids);
			context.commit();
			return success;
		} catch (Throwable e) {
			throw Warning.wrapException(e);
		}finally{
			CloseUtil.close(context);
		}
	}

}
