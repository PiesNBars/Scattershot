package io.scattershot.util;

import java.io.Serializable;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TypeParser {

	private static final String TIME = "HH:mm:ss";
	private static final String DATE_SLASH = "yyyy/MM/dd";
	private static final String DATE_DASH = "yyyy-MM-dd";
	private static final String DATE_SPACE = "yyyy MM dd";
	private static final DateFormat D_SLSH;
	private static final DateFormat D_DSH;
	private static final DateFormat D_SPC;
	private static final DateFormat D_SLSH_W_TIME;
	private static final DateFormat D_DSH_W_TIME;
	private static final DateFormat D_SPC_W_TIME;
	private static final DateFormat TIMESTAMP = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	private static final ArrayList<DateFormat> FORMATS;

	static {
		D_SLSH = new SimpleDateFormat(DATE_SLASH);
		D_DSH = new SimpleDateFormat(DATE_DASH);
		D_SPC = new SimpleDateFormat(DATE_SPACE);
		D_SLSH_W_TIME = new SimpleDateFormat(DATE_SLASH + " " + TIME);
		D_DSH_W_TIME = new SimpleDateFormat(DATE_DASH + " " + TIME);
		D_SPC_W_TIME = new SimpleDateFormat(DATE_SPACE + " " + TIME);

		FORMATS = new ArrayList<DateFormat>();
		FORMATS.add(TIMESTAMP);
		FORMATS.add(D_SLSH_W_TIME);
		FORMATS.add(D_DSH_W_TIME);
		FORMATS.add(D_SPC_W_TIME);
		FORMATS.add(D_SLSH);
		FORMATS.add(D_DSH);
		FORMATS.add(D_SPC);
	}

	public static ParseResult<? extends Serializable> parse(String string) {

		// Check if string fits a date format.
		for(int i = 0; i < FORMATS.size(); i++) {
			try {
				DateFormat format = FORMATS.get(i);
				Date date = format.parse(string);
				if(date != null)
					return new ParseResult<Date>(date);
			} catch (ParseException pe) {}
		}

		try {
			Number value = Long.parseLong(string);
			return new ParseResult<Number>(value);
		} catch (NumberFormatException nfe) {}

		try {
			Number value = Double.parseDouble(string);
			return new ParseResult<Number>(value);
		} catch (NumberFormatException nfe) {}

		return new ParseResult<String>(string);
	}

}