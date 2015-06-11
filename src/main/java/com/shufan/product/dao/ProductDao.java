package com.shufan.product.dao;

import haiyan.common.intf.database.IDBBill;
import haiyan.common.intf.database.orm.IDBRecord;
import haiyan.common.intf.database.orm.IDBResultSet;
import haiyan.config.castorgen.Table;

/**
 * 产品数据访问对象接口
 * @author 商杰
 *
 */
public interface ProductDao {
	
	public abstract IDBResultSet getAllSetMeal(int maxPageSize,int page);
	public abstract IDBResultSet getAllProduct(int maxPageSize,int page);
	public abstract IDBResultSet getProductByType(String type ,int maxPageSize,int page);
	public abstract IDBResultSet getSetMealDetail(String setMealId, int maxPageSize, int page);
	public abstract IDBRecord getProductById(String productId);
	public abstract IDBResultSet getEvaluates(String setMealId,int maxPageSize,int page);
	
	public abstract IDBRecord addEvaluate(IDBRecord record);
	public abstract IDBRecord updateEvaluate(IDBRecord record);
	public abstract boolean deleteEvaluate(String[] ids);
	
	public abstract IDBRecord addProduct(IDBRecord record);
	public abstract IDBRecord updateProduct(IDBRecord record);
	public abstract boolean deleteProduct(String[] ids);
	
	public abstract IDBBill addSetMeal(IDBResultSet head,IDBResultSet detail);
	public abstract IDBBill deleteSetMeal(IDBResultSet head,IDBResultSet detail);
	
	public abstract Table getSetMealEvaluateTable();
}
