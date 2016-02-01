package edu.cpp.cs580.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import edu.cpp.cs580.customer.data.CustomerDataset;

public class CSVMapper {
	
	private static final String CHAR_FORMAT = "UTF-8";
	
	public static CustomerDataset mapCSV(MultipartFile file,
			boolean fileContainsHeader) throws Exception {
		
		if(fileContainsHeader)
			return mapCSV(file);
		
		int columnCount = countColumns(file, ',');
		String[] header = generateHeader(columnCount);
		
		return mapCSV(file, header);
	}	
	
	public static CustomerDataset mapCSV(MultipartFile file)
			throws Exception {
		
		List<Map<String, ? extends Serializable>> table = new ArrayList<>();
		InputStream in = file.getInputStream();
		InputStreamReader reader = new InputStreamReader(in, CHAR_FORMAT);
		CSVFormat format = CSVFormat.RFC4180.withHeader();
		CSVParser parser = new CSVParser(reader, format);

		for(CSVRecord tuple : parser) {
			table.add(tuple.toMap());
			
		}
		
		parser.close();
		
		return new CustomerDataset(table);
	}
	
	public static CustomerDataset mapCSV(MultipartFile file,
			String[] columnNames) throws Exception {

		List<Map<String, ? extends Serializable>> table = new ArrayList<>();
		InputStream in = file.getInputStream();
		InputStreamReader reader = new InputStreamReader(in, CHAR_FORMAT);
		CSVFormat format = CSVFormat.RFC4180.withHeader(columnNames);
		CSVParser parser = new CSVParser(reader, format);
		
		for(CSVRecord tuple : parser)
			table.add(tuple.toMap());
		
		parser.close();
		
		return new CustomerDataset(table);
	}
	
	private static String[] generateHeader(int columns) {
		String[] header = new String[columns];
		for(int i = 0; i < columns; i++)
			header[i] = "c" + (i+1);
		return header;
	}
	
	private static int countColumns(MultipartFile file, char separator)
			throws IOException {
		
		InputStream fileStream = file.getInputStream();
		InputStreamReader input = new InputStreamReader(fileStream);
		BufferedReader reader = new BufferedReader(input);
		String row = reader.readLine();
		
		if(row == null)
			return 0;
		
		// For n columns there are n - 1 separators, so add 1.
		int cols = countChars(row, separator) + 1;
			
		reader.close();
		input.close();
		
		return cols;
	}
	
	private static int countChars(String s, char c) {
		int count = 0;
		for(int i = 0; i < s.length(); i++)
			if(s.charAt(i) == c)
				++count;
		return count;				
	}
}
