package com.shufan.product.dao.impl;

import haiyan.bill.database.sql.DBBillManagerFactory;
import haiyan.common.CloseUtil;
import haiyan.common.exception.Warning;
import haiyan.common.intf.config.IBillConfig;
import haiyan.common.intf.database.IDBBill;
import haiyan.common.intf.database.bill.IDBBillManager;
import haiyan.common.intf.database.orm.IDBRecord;
import haiyan.common.intf.database.orm.IDBResultSet;
import haiyan.common.intf.session.IContext;
import haiyan.config.castorgen.Table;
import haiyan.config.intf.database.ITableDBManager;
import haiyan.config.intf.session.ITableDBContext;
import haiyan.config.util.ConfigUtil;
import haiyan.orm.database.DBContextFactory;

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
	private static Table setMealHeadTable;
	public Table getSetMealHeadTable() {
		if (setMealHeadTable==null)
			synchronized(ProductDaoImpl.class) {
				if (setMealHeadTable==null)
					setMealHeadTable = ConfigUtil.getTable("T_PRODUCT_SETMEAL_HEAD");
			}
		return setMealHeadTable;
	}
	private static Table setMealDetailTable;
	public Table getSetMealDetailTable() {
		if (setMealDetailTable==null)
			synchronized(ProductDaoImpl.class) {
				if (setMealDetailTable==null)
					setMealDetailTable = ConfigUtil.getTable("T_PRODUCT_SETMEAL_DETAIL");
			}
		return setMealDetailTable;
	}
	private static Table setMealEvaluateTable;
	public Table getSetMealEvaluateTable() {
		if (setMealEvaluateTable==null)
			synchronized(ProductDaoImpl.class) {
				if (setMealEvaluateTable==null)
					setMealEvaluateTable = ConfigUtil.getTable("T_PRODUCT_SETMEAL_EVALUATE");
			}
		return setMealEvaluateTable;
	}
	private static IBillConfig setMealBill;
	public IBillConfig getSetMealBill(){
		if (setMealBill==null)
			synchronized(ProductDaoImpl.class) {
				if (setMealBill==null)
					setMealBill = ConfigUtil.getBill("B_PRODUCT_SETMEAL");
			}
		return setMealBill;
	}

	@Override
	public IDBResultSet getAllSetMeal(int maxPageSize, int page) {
		ITableDBContext context = null;
		ITableDBManager dbm = null;
		try {
			context = DBContextFactory.createDBContext(parentContext);
			dbm = context.getDBM();
			IDBRecord record = dbm.createRecord();
			IDBResultSet result = dbm.select(context, getSetMealHeadTable(), record,maxPageSize,page);
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
			context = DBContextFactory.createDBContext(parentContext);
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
			context = DBContextFactory.createDBContext(parentContext);
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
	public IDBResultSet getSetMealDetail(String setMealId, int maxPageSize, int page) {
		ITableDBContext context = null;
		ITableDBManager dbm = null;
		try {
			context = DBContextFactory.createDBContext(parentContext);
			dbm = context.getDBM();
			IDBRecord record = dbm.createRecord();
			record.set("MEIALID", setMealId);
			IDBResultSet result = dbm.select(context, getSetMealDetailTable(), record,maxPageSize,page);
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
			context = DBContextFactory.createDBContext(parentContext);
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
			context = DBContextFactory.createDBContext(parentContext);
			dbm = context.getDBM();
			IDBRecord record = dbm.createRecord();
			record.set("MEIALID", setMealId);
			IDBResultSet result = dbm.select(context, getSetMealEvaluateTable(), record,maxPageSize,page);
			return result;
		} catch (Throwable e) {
			throw Warning.wrapException(e);
		}finally{
			CloseUtil.close(dbm);
			CloseUtil.close(context);
		}
	}

	@Override
	public IDBRecord addEvaluate(IDBRecord record) {
		ITableDBContext context = null;
		ITableDBManager dbm = null;
		try {
			context = DBContextFactory.createDBContext(parentContext);
			dbm = context.getDBM();
			IDBRecord result = dbm.insert(context, getSetMealEvaluateTable(), record);
			return result;
		} catch (Throwable e) {
			throw Warning.wrapException(e);
		}finally{
			CloseUtil.close(dbm);
			CloseUtil.close(context);
		}
	}

	@Override
	public IDBRecord updateEvaluate(IDBRecord record) {
		ITableDBContext context = null;
		ITableDBManager dbm = null;
		try {
			context = DBContextFactory.createDBContext(parentContext);
			dbm = context.getDBM();
			IDBRecord result = dbm.update(context, getSetMealEvaluateTable(), record);
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
			context = DBContextFactory.createDBContext(parentContext);
			dbm = context.getDBM();
			dbm.openTransaction();
			boolean result = dbm.delete(context, getSetMealEvaluateTable(), ids);
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
			context = DBContextFactory.createDBContext(parentContext);
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
			context = DBContextFactory.createDBContext(parentContext);
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
			context = DBContextFactory.createDBContext(parentContext);
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
	public IDBBill addSetMeal(IDBResultSet head, IDBResultSet detail) {
		IContext context = null;
		IDBBillManager bbm = null;
		try {
			context = DBContextFactory.createDBContext(parentContext);
			bbm = DBBillManagerFactory.createDBBillManager(context);
			IDBBill bill = bbm.createBill(getSetMealBill(), true);
			bill.setResultSet(0, head);
			bill.setResultSet(1, detail);
			bbm.saveBill(bill);
			return bill;
		} catch (Throwable e) {
			throw Warning.wrapException(e);
		}finally{
			CloseUtil.close(bbm);
			CloseUtil.close(context);
		}
	}
	//TODO 删除无效
	@Override
	public IDBBill deleteSetMeal(IDBResultSet head, IDBResultSet detail) {
		IContext context = null;
		IDBBillManager bbm = null;
		try {
			context = DBContextFactory.createDBContext(parentContext);
			bbm = DBBillManagerFactory.createDBBillManager(context);
			bbm.openTransaction();
			IDBBill bill = bbm.createBill(getSetMealBill(), true);
			bill.setResultSet(0, head);
			bill.setResultSet(1, detail);
			bbm.deleteBill(bill);
			bbm.commit();
			return bill;
		} catch (Throwable e) {
			throw Warning.wrapException(e);
		}finally{
			CloseUtil.close(bbm);
			CloseUtil.close(context);
		}
	}

}
