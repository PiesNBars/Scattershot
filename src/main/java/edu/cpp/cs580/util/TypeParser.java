package edu.cpp.cs580.util;

import java.io.Serializable;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TypeParser {
	
	private static final String A_SLSH_DT = "MM/DD/YYYY";
	private static final String A_SPC_DT = "MM DD YYYY";
	private static final String E_SLSH_DT = "DD/MM/YYYY";
	private static final String E_SPC_DT = "DD MM YYYY";
	private static final String TIME_OF_DAY = "HH:mm:ss";
	private static final DateFormat AMERICAN_SLASH_DATE;
	private static final DateFormat AMERICAN_SPACE_DATE;
	private static final DateFormat EUROPEAN_SLASH_DATE;
	private static final DateFormat EUROPEAN_SPACE_DATE;
	private static final DateFormat A_SLSH_DT_W_TIME;
	private static final DateFormat A_SPC_DT_W_TIME;
	private static final DateFormat E_SLSH_DT_W_TIME;
	private static final DateFormat E_SPC_DT_W_TIME;
	private static final DateFormat TIMESTAMP = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	private static final ArrayList<DateFormat> FORMATS;
			
	static {
		AMERICAN_SLASH_DATE = new SimpleDateFormat(A_SLSH_DT);
		AMERICAN_SPACE_DATE = new SimpleDateFormat(A_SPC_DT);
		EUROPEAN_SLASH_DATE = new SimpleDateFormat(E_SLSH_DT);
		EUROPEAN_SPACE_DATE = new SimpleDateFormat(E_SPC_DT);
		A_SLSH_DT_W_TIME = new SimpleDateFormat(String.format("%s %s", A_SLSH_DT, TIME_OF_DAY));
		A_SPC_DT_W_TIME = new SimpleDateFormat(String.format("%s %s", A_SPC_DT, TIME_OF_DAY));
		E_SLSH_DT_W_TIME = new SimpleDateFormat(String.format("%s %s", E_SLSH_DT, TIME_OF_DAY));
		E_SPC_DT_W_TIME = new SimpleDateFormat(String.format("%s %s", E_SPC_DT, TIME_OF_DAY));
		
		FORMATS = new ArrayList<DateFormat>();
		FORMATS.add(AMERICAN_SLASH_DATE);
		FORMATS.add(AMERICAN_SPACE_DATE);
		FORMATS.add(A_SLSH_DT_W_TIME);
		FORMATS.add(A_SPC_DT_W_TIME);
		FORMATS.add(EUROPEAN_SLASH_DATE);
		FORMATS.add(EUROPEAN_SPACE_DATE);
		FORMATS.add(E_SLSH_DT_W_TIME);
		FORMATS.add(E_SPC_DT_W_TIME);
		FORMATS.add(TIMESTAMP);
	}
	
	public static ParseResult<? extends Serializable> parse(String string) {
		
		// Check if string fits a date format.
		for(DateFormat format : FORMATS) {
			try {
				Date date = format.parse(string);
				if(date != null)
					return new ParseResult<Date>(date);
			} catch (ParseException pe) {}
		}
		
		try {
			Long value = Long.parseLong(string);
			return new ParseResult<Long>(value);
		} catch (NumberFormatException nfe) {}
		
		try {
			Double value = Double.parseDouble(string);
			return new ParseResult<Double>(value);
		} catch (NumberFormatException nfe) {}
		
		return new ParseResult<String>(string);
	}

}