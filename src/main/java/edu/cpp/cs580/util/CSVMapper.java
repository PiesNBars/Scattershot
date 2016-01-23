package edu.cpp.cs580.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

public class CSVMapper {
	
	private static final String CHAR_FORMAT = "UTF-8";
	
	public static List<Map<String, ? extends Object>> mapCSV(MultipartFile file) throws Exception {
		InputStream in = file.getInputStream();
		InputStreamReader reader = new InputStreamReader(in, CHAR_FORMAT);
		CSVParser parser = new CSVParser(reader, CSVFormat.RFC4180.withHeader("col1", "col2", "col3"));
		List<Map<String, ? extends Object>> table = new ArrayList<Map<String, ? extends Object>>();

		for(CSVRecord tuple : parser)
			table.add(tuple.toMap());
		
		parser.close();
		
		return table;
	}
	
	
}
