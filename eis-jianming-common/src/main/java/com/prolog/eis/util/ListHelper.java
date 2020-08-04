package com.prolog.eis.util;

import java.util.*;
import java.util.function.Function;

/**
 * 
 * @author cb
 *
 */
public class ListHelper {	
	
	/**
	 * 将集合根据某个属性进行分组
	 * @param list
	 * @author cb
	 * @param func
	 * @return
	 * @throws Exception
	 */
	public static <TKey,TType> Map<TKey, List<TType>> buildGroupDictionary(List<TType> list,Function<TType,TKey> func) {

		Map<TKey, List<TType>> map = new HashMap<TKey, List<TType>>();

		for (TType t : list) {
			TKey value = func.apply(t);

			if(map.containsKey(value)) {
				map.get(value).add(t);
			}else {
				List<TType> newList = new ArrayList<TType>();
				newList.add(t);
				map.put(value, newList);
			}
		}
		return map;
	}
	
	/**
	 * 将集合根据某个属性进行分组
	 * @param list
	 * @author cb
	 * @param func
	 * @return
	 * @throws Exception
	 */
	public static <TKey,TType,TType2> Map<TKey, List<TType2>> buildGroupDictionary(List<TType> list,Function<TType,TKey> func,Function<TType,TType2> func2) {

		Map<TKey, List<TType2>> map = new HashMap<TKey, List<TType2>>();

		for (TType t : list) {
			TKey value = func.apply(t);
			if(map.containsKey(value)) {
				TType2 value2 = func2.apply(t);
				map.get(value).add(value2);
			}else {
				List<TType2> newList = new ArrayList<TType2>();
				TType2 value2 = func2.apply(t);
				newList.add(value2);
				map.put(value, newList);
			}
		}
		return map;
	}

	/**
	 * 将集合根据转换成另一个类型的集合
	 * @param list
	 * @author cb
	 * @param func
	 * @return
	 * @throws Exception
	 */
	public static <NewTType,TType> List<NewTType> select(List<TType> list,Function<TType,NewTType> func) {

		List<NewTType> result = new ArrayList<NewTType>();
		for (TType t : list) {
			NewTType value = func.apply(t);
			if(value != null)
				result.add(value);
		}

		return result;
	}
	
	/**
	 * 将集合根据转换成另一个类型的集合
	 * @param list
	 * @author cb
	 * @param func
	 * @return
	 * @throws Exception
	 */
	public static <NewTType,TType1,TType2> List<NewTType> select(List<TType1> list1,List<TType2> list2,MultiFunction<TType1,TType2,NewTType> func) {

		List<NewTType> result = new ArrayList<NewTType>();
		for(int i= 0;i<list1.size();i++) {
			TType1 t1 = list1.get(i);
			TType2 t2 = list2.get(i);
			
			NewTType value = func.apply(t1,t2);
			if(value != null)
				result.add(value);
		}

		return result;
	}

	/**
	 * 将集合根据条件筛选
	 * @param list
	 * @author cb
	 * @param func
	 * @return
	 * @throws Exception
	 */
	public static <TType> List<TType> where(List<TType> list,Function<TType,Boolean> func) {

		List<TType> result = new ArrayList<TType>();
		for (TType t : list) {
			Boolean value = func.apply(t);
			if(value) {
				result.add(t);
			}
		}

		return result;
	}

	/**
	 * 将集合根据条件筛选第一个，如果没有满足条件的数据则返回null
	 * @param list
	 * @author cb
	 * @param func
	 * @return
	 * @throws Exception
	 */
	public static <TType> TType firstOrDefault(List<TType> list,Function<TType,Boolean> func) {

		TType result = null;
		for (TType t : list) {
			Boolean value = func.apply(t);
			if(value) {
				result = t;
				break;
			}
		}

		return result;
	}

	/**
	 * 将集合进行去重
	 * @param list
	 * @author cb
	 * @param func
	 * @return
	 * @throws Exception
	 */
	public static <TType> List<TType> distinct(List<TType> list) {

		HashSet<TType> hashSet = new HashSet<TType>();
		for (TType t : list) {
			if(!hashSet.contains(t)) {
				hashSet.add(t);
			}
		}

		List<TType> result = new ArrayList<TType>(hashSet);

		return result;
	}

	/**
	 * 将集合按某个属性进行去重
	 * @param list
	 * @author cb
	 * @param func
	 * @return
	 * @throws Exception
	 */
	public static <ProTType,TType> List<TType> distinct(List<TType> list,Function<TType,ProTType> func) {

		List<TType> result = new ArrayList<TType>();
		HashSet<ProTType> hashSet = new HashSet<ProTType>();
		for (TType t : list) {
			ProTType value = func.apply(t);
			if(!hashSet.contains(value)) {
				hashSet.add(value);
				result.add(t);
			}
		}

		return result;
	}

	/**
	 * 将集合根据选定属性求和
	 * @param list
	 * @author cb
	 * @param func
	 * @return
	 * @throws Exception
	 */
	public static <TType> int sum(List<TType> list,Function<TType,Integer> func) {

		int sum = 0;
		for (TType t : list) {
			Integer value = func.apply(t);
			if(value !=null)
				sum += value;
		}

		return sum;
	}
}
