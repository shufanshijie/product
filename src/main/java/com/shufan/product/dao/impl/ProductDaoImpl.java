package com.shufan.product.dao.impl;

import haiyan.bill.database.BillDBContextFactory;
import haiyan.bill.database.IBillDBManager;
import haiyan.bill.database.sql.IBillDBContext;
import haiyan.common.CloseUtil;
import haiyan.common.exception.Warning;
import haiyan.common.intf.config.IBillConfig;
import haiyan.common.intf.database.IDBBill;
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
import java.util.Collection;

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
			context = TableDBContextFactory.createDBContext(parentContext);
			dbm = context.getDBM();
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)+1);
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			Date date = new Date(cal.getTime().getTime());
			IDBFilter filter = new SQLDBFilter(" and DATE >= ?", new Object[]{date});
			IDBResultSet result = dbm.select(context, getSetMealHeadTable(), filter,maxPageSize,page);
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
	public IDBResultSet getSetMealDetail(String setMealId, int maxPageSize, int page) {
		ITableDBContext context = null;
		ITableDBManager dbm = null;
		try {
			context = TableDBContextFactory.createDBContext(parentContext);
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
			context = TableDBContextFactory.createDBContext(parentContext);
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
			context = TableDBContextFactory.createDBContext(parentContext);
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
			context = TableDBContextFactory.createDBContext(parentContext);
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
	public IDBBill addSetMeal(IDBBill bill) throws Throwable {
		IBillDBContext context = null;
		IBillDBManager bbm = null;
		try {
			context = BillDBContextFactory.createBillDBContext(parentContext,getSetMealBill());
			bbm = context.getBBM();
			IDBResultSet[] rs = bill.getResultSets();
			for(IDBResultSet set : rs){
				Collection<IDBRecord> records = set.getRecords();
				for(IDBRecord record : records){
					record.setStatus(IDBRecord.INSERT);
				}
			}
			context.openTransaction();
			bbm.saveBill(context, bill);
			context.commit();
			return bill;
		} catch (Throwable e) {
			if(context != null)
				context.rollback();
			throw Warning.wrapException(e);
		}finally{
			CloseUtil.close(bbm);
			CloseUtil.close(context);
		}
	}
	@Override
	public IDBBill loadSetMeal(IDBBill bill) {
		IBillDBContext context = null;
		IBillDBManager bbm = null;
		try {
			context = BillDBContextFactory.createBillDBContext(parentContext,getSetMealBill());
			bbm = context.getBBM();
			bbm.loadBill(context, bill);
			return bill;
		} catch (Throwable e) {
			throw Warning.wrapException(e);
		}finally{
			CloseUtil.close(bbm);
			CloseUtil.close(context);
		}
	}
	@Override
	public IDBBill updateSetMeal(IDBBill bill) throws Throwable {
		IBillDBContext context = null;
		IBillDBManager bbm = null;
		try {
			context = BillDBContextFactory.createBillDBContext(parentContext,getSetMealBill());
			bbm = context.getBBM();
			IDBResultSet[] rs = bill.getResultSets();
			for(IDBResultSet set : rs){
				Collection<IDBRecord> records = set.getRecords();
				for(IDBRecord record : records){
					record.setStatus(IDBRecord.UPDATE);
				}
			}
			context.openTransaction();
			bbm.saveBill(context, bill);
			context.commit();
			return bill;
		} catch (Throwable e) {
			if(context != null)
				context.rollback();
			throw Warning.wrapException(e);
		}finally{
			CloseUtil.close(bbm);
			CloseUtil.close(context);
		}
	}
	@Override
	public IDBBill deleteSetMeal(IDBBill bill) throws Throwable {
		IBillDBContext context = null;
		IBillDBManager bbm = null;
		try {
			context = BillDBContextFactory.createBillDBContext(parentContext,getSetMealBill());
			bbm = context.getBBM();
			IDBResultSet[] rs = bill.getResultSets();
			for(IDBResultSet set : rs){
				Collection<IDBRecord> records = set.getRecords();
				for(IDBRecord record : records){
					record.setStatus(IDBRecord.DELETE);
				}
			}
			bbm.openTransaction();
			bbm.deleteBill(context, bill);
			bbm.commit();
			return bill;
		} catch (Throwable e) {
			if(context != null)
				context.rollback();
			throw Warning.wrapException(e);
		}finally{
			CloseUtil.close(bbm);
			CloseUtil.close(context);
		}
	}

}
