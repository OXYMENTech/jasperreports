/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.jasperreports.engine.analytics.dataset;

import java.io.Serializable;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.type.SortOrderEnum;
import net.sf.jasperreports.engine.util.JRClassLoader;
import net.sf.jasperreports.engine.util.JRCloneUtils;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class BaseDataLevelBucket implements DataLevelBucket, Serializable
{
	
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	protected String valueClassName;
	protected String valueClassRealName;
	protected Class<?> valueClass;

	protected SortOrderEnum orderValue = SortOrderEnum.ASCENDING;
	protected JRExpression expression;
	protected JRExpression comparatorExpression;

	protected BaseDataLevelBucket()
	{
	}
	
	public BaseDataLevelBucket(DataLevelBucket bucket, JRBaseObjectFactory factory)
	{
		factory.put(bucket, this);
		
		this.valueClassName = bucket.getValueClassName();
		this.orderValue = bucket.getOrderValue();
		this.expression = factory.getExpression(bucket.getExpression());
		this.comparatorExpression = factory.getExpression(bucket.getComparatorExpression());
	}

	public String getValueClassName()
	{
		return valueClassName;
	}

	public SortOrderEnum getOrderValue()
	{
		return orderValue;
	}

	public JRExpression getExpression()
	{
		return expression;
	}

	public JRExpression getComparatorExpression()
	{
		return comparatorExpression;
	}
	
	public Class<?> getValueClass()
	{
		if (valueClass == null)
		{
			String className = getValueClassRealName();
			if (className != null)
			{
				try
				{
					valueClass = JRClassLoader.loadClassForName(className);
				}
				catch (ClassNotFoundException e)
				{
					throw new JRRuntimeException("Could not load bucket value class", e);
				}
			}
		}
		
		return valueClass;
	}

	private String getValueClassRealName()
	{
		if (valueClassRealName == null)
		{
			valueClassRealName = JRClassLoader.getClassRealName(valueClassName);
		}
		
		return valueClassRealName;
	}

	public Object clone()
	{
		BaseDataLevelBucket clone = null;
		try
		{
			clone = (BaseDataLevelBucket) super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			// never
			throw new JRRuntimeException(e);
		}
		clone.expression = JRCloneUtils.nullSafeClone(expression);
		clone.comparatorExpression = JRCloneUtils.nullSafeClone(comparatorExpression);
		return clone;
	}

}