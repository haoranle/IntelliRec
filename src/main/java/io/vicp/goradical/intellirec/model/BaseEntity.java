package io.vicp.goradical.intellirec.model;

import io.vicp.goradical.intellirec.util.StringUtil;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;

/**
 * Created by Kenny on 2017/3/3.
 * <p>
 * 抽象的实体超类，所有需要持久化的数据均实现序列化并且添加代理主键 id, 专门用于继承
 */
public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = 6893731361119450101L;

	/**
	 * 返回对象的代理主键 id
	 *
	 * @return 代理主键 id
	 */
	public abstract Integer getId();

	/**
	 * 设置对象的代理主键 id
	 *
	 * @param id 代理主键 id
	 */
	public abstract void setId(Integer id);

	/**
	 * 使用 json 格式来格式化对象实体
	 *
	 * @return
	 */
//	@Override
//	public String toString() {
//		String jsonStr = JSON.toJSONStringWithDateFormat(this, "yyyy-MM-dd HH:mm:ss");
//		return jsonStr;
//	}

	/**
	 * 利用反射将对象的基本属性按照类 json 格式进行输出
	 * @return
	 */
	@Override
	public String toString() {
		try {
			StringBuffer buffer = new StringBuffer();
			Class clazz = getClass();
			String simpleName = clazz.getSimpleName();
			buffer.append(simpleName);
			buffer.append(" {");
			Field[] fields = clazz.getDeclaredFields();
			Class ftype;
			String fname;
			Object fvalue;
			for (Field field : fields) {
				ftype = field.getType();
				fname = field.getName();
				field.setAccessible(true);
//				fvalue = field.get(this);
				if ((ftype.isPrimitive()
						|| ftype == Integer.class
						|| ftype == Long.class
						|| ftype == Short.class
						|| ftype == Boolean.class
						|| ftype == Character.class
						|| ftype == Float.class
						|| ftype == Double.class
						|| ftype == String.class
						|| ftype == Date.class
						|| ftype.isArray()) && !Modifier.isStatic(field.getModifiers())) {
					//是否是基本数据类型
					buffer.append(fname);
					buffer.append(":");
					Method method = clazz.getMethod("get" + StringUtil.toUpCaseFirstChar(fname));
					fvalue = method.invoke(this);
					buffer.append(fvalue);
					buffer.append(", ");
				}
			}
			buffer.delete(buffer.length() - 2, buffer.length());
			buffer.append("}");
			return buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
