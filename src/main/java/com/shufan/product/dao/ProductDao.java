package com.shufan.product.dao;

import haiyan.common.intf.database.orm.IDBRecord;
import haiyan.common.intf.database.orm.IDBResultSet;
import haiyan.config.castorgen.Table;

/**
 * 产品数据访问对象接口
 * @author 商杰
 *
 */
public interface ProductDao {
	/**
	 * 分页查询从明天开始以后的的套餐
	 * @param maxPageSize
	 * @param page
	 * @return
	 */
	public abstract IDBResultSet getAllMeal(int maxPageSize,int page);
	/**
	 * 分页查询所有已上架的单品
	 * @param maxPageSize
	 * @param page
	 * @return
	 */
	public abstract IDBResultSet getAllProduct(int maxPageSize,int page);
	/**
	 * 按指定单品类型分页查询已上架的单品
	 * @param type
	 * @param maxPageSize
	 * @param page
	 * @return
	 */
	public abstract IDBResultSet getProductByType(String type ,int maxPageSize,int page);
	/**
	 * 根据单品ID查询单品信息
	 * @param setMealId
	 * @param maxPageSize
	 * @param page
	 * @return
	 */
	public abstract IDBRecord getProductById(String productId);
	/**
	 * 根据套餐ID分页查询评价信息
	 * @param setMealId
	 * @param maxPageSize
	 * @param page
	 * @return
	 */
	public abstract IDBResultSet getEvaluates(String setMealId,int maxPageSize,int page);
	/**
	 * 添加评论
	 * @param record
	 * @return
	 */
	public abstract IDBRecord addEvaluate(String mealID,IDBRecord record);
	/**
	 * 根据评论ID删除评论
	 * @param ids
	 * @return
	 */
	public abstract boolean deleteEvaluate(String[] ids);
	/**
	 * 添加单品信息
	 * @param record
	 * @return
	 */
	public abstract IDBRecord addProduct(IDBRecord record);
	/**
	 * 修改单品信息
	 * @param record
	 * @return
	 */
	public abstract IDBRecord updateProduct(IDBRecord record);
	/**
	 * 根据单品ID删除单品信息
	 * @param ids
	 * @return
	 */
	public abstract boolean deleteProduct(String[] ids);
	/**
	 * 根据套餐ID查看套餐
	 * @param bill
	 * @return
	 */
	public abstract IDBRecord loadMealById(String mealId);
	/**
	 * 添加套餐
	 * @param record
	 * @return
	 * @throws Throwable
	 */
	public abstract IDBRecord addMeal(IDBRecord record) throws Throwable;
	/**
	 * 根据套餐ID修改套餐
	 * @param record
	 * @return
	 * @throws Throwable
	 */
	public abstract IDBRecord updateMeal(IDBRecord record)throws Throwable;
	/**
	 * 根据套餐ID删除套餐
	 * @param ids
	 * @return
	 * @throws Throwable
	 */
	public abstract boolean deleteMeal(String[] ids)throws Throwable;
	/**
	 * 获取评价表信息
	 * @return
	 */
	public abstract Table getMealEvaluateTable();
	/**
	 * 获取套餐表信息
	 * @return
	 */
	public abstract Table getMealTable();
	/**
	 * 获取单品表信息
	 * @return
	 */
	public abstract Table getProductTable();
}
