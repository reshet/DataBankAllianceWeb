package org.zenika.widget.client.util;

import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * DateLocale implementation for US.
 * @author Zenika
 *
 */
public class DateLocale_ implements DateLocale {

	public int[] DAYS_ORDER = { 0, 1, 2, 3, 4, 5, 6 };

	public int[] getDAY_ORDER() {
		return DAYS_ORDER;
	}

	public DateTimeFormat getDateTimeFormat() {
		DateTimeFormat format = DateTimeFormat.getFormat("MM/dd/yyyy");
		return format;
	}
}
