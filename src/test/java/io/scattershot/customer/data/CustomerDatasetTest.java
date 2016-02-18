package io.scattershot.customer.data;

import io.scattershot.customer.data.CustomerDataset;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.Before;

/**
 *
 * @author Ian Riley
 *
 */
public class CustomerDatasetTest {

	private CustomerDataset testObject;
	private Map<String, Serializable> testRow;
	private String intKey;
	private Integer intValue;
	private String stringValue;
	private String stringKey;

	@Before
	public void setUp() {
		intValue = 1;
		intKey = "intKey";
		stringValue = "value";
		stringKey = "stringKey";
		testObject = new CustomerDataset();
		testRow= new HashMap<>();

		testRow.put(stringKey, stringValue);
		testRow.put(intKey, intValue);

	}

	@Test
	public void testPush() {
		testObject.push(testRow);

		List<Map<String, ? extends Serializable>> data = testObject.getDataset();

		Map<String, ? extends Serializable> row = data.get(0);
		String retString = String.class.cast(row.get(stringKey));
		Integer retInt = Integer.class.cast(row.get(intKey));

		assertEquals(retString, stringValue);
		assertEquals(retInt, intValue);
	}

	@Test
	public void testSetDataset() {
		List<Map<String, ? extends Serializable>> dataset = new ArrayList<>();

		dataset.add(testRow);

		testObject.setDataset(dataset);

		Map<String, String> typeMap = testObject.getTypeMap();
		assertEquals(typeMap.get(intKey), Integer.class.getName());
		assertEquals(typeMap.get(stringKey), String.class.getName());
	}

	@Test
	public void testGetColumns() {
		testObject.push(testRow);

		CustomerDataset ret = testObject.getColumns(stringKey);
		Map<String, String> types = ret.getTypeMap();
		Map<String, ? extends Serializable> row = ret.getDataset().get(0);

		assertEquals(types.get(stringKey), String.class.getName());
		assertEquals(row.get(stringKey), stringValue);
	}

	@After
	public void tearDown() {
		testObject = null;
	}
}
