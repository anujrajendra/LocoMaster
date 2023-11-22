package com.cris.loco_master.utils;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.onwbp.adaptation.UnavailableContentError;
import com.onwbp.base.misc.IntegerUtils;
import com.onwbp.org.apache.commons.lang3.StringUtils;
import com.onwbp.org.apache.commons.lang3.time.DateUtils;
import com.orchestranetworks.schema.Path;
import com.orchestranetworks.schema.PathAccessException;
import com.orchestranetworks.service.ValueContextForUpdate;

public class RulesUtils {

	public static void setRecord(ValueContextForUpdate vcfu, String sourceValue, Path destinationPath) {
		if (StringUtils.equals(sourceValue, "null")) {
			vcfu.setValue("", destinationPath);
		} else if (StringUtils.isNotBlank(sourceValue)) {
			vcfu.setValue(sourceValue, destinationPath);
		}
	}

	public static void setRecord(ValueContextForUpdate vcfu, Date sourceValue, Path destinationPath) {
		if (sourceValue != null) {
			vcfu.setValue(sourceValue, destinationPath);
		}
	}

	public static void setDateRecord(ValueContextForUpdate vcfu, String sourceValue, Path destinationPath)
			throws UnavailableContentError, PathAccessException, IllegalArgumentException, ParseException {
		if (StringUtils.equals(sourceValue, "null")) {
			vcfu.setValue(null, destinationPath);
		} else if (StringUtils.isNotBlank(sourceValue)) {
			// vcfu.setValue(DateUtils.parseDate(sourceValue, new String[] { "dd/MM/yyyy"
			// }), destinationPath);
			vcfu.setValue(DateUtils.parseDateStrictly(sourceValue, new String[] { "dd/MM/yyyy" }), destinationPath);
		}
	}

	public static void setRecord(ValueContextForUpdate vcfu, Boolean sourceValue, Path destinationPath) {
		if (sourceValue != null) {
			vcfu.setValue(sourceValue, destinationPath);
		}
	}

	public static void setRecord(ValueContextForUpdate vcfu, List<String> sourceValueList, Path destinationPath) {
		if (sourceValueList != null && !sourceValueList.isEmpty() && !sourceValueList.contains("null")) {
			vcfu.setValue(sourceValueList, destinationPath);
		} else if (sourceValueList != null && !sourceValueList.isEmpty() && sourceValueList.contains("null")) {
			vcfu.setValue(null, destinationPath);
		}
	}

	public static void setIntegerRecord(ValueContextForUpdate vcfu, String sourceValue, Path destinationPath) {
		if (StringUtils.equals(sourceValue, "null")) {
			vcfu.setValue("", destinationPath);
		} else if (StringUtils.isNotBlank(sourceValue)) {
			vcfu.setValue(IntegerUtils.parse(sourceValue), destinationPath);
		}
	}

	public static String toUpper(String sourceValue) {
		if (StringUtils.equals(sourceValue, "null")) {
			return null;
		} else if (StringUtils.isNotBlank(sourceValue)) {
			return sourceValue.toUpperCase();
		}
		return sourceValue.toUpperCase();
	}
}
