// HTableExample.java - import big data from a CSV formatted file to a HBase table
// @author Kaiyi Zhu (kz2232@columbia.edu)

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;


public class HTableExample {

	public static void main(String[] args) throws IOException {
		
		/* Intantiate an HTable object that connects to 
		 * "TCGA" table created in HBase
		 */
		Configuration config = HBaseConfiguration.create();
		@SuppressWarnings("resource")
		HTable table = new HTable(config,"TCGA");
		
		/* Read csv file into a 2D Arraylist,
		 * facilating the next step of HBase import
		 */
		List<List<String>> csvArray = new ArrayList<List<String>>();
		csvArray = readCSV(TCGA.csv");		
		
		
		for (int col = 1; col < csvArray.get(1).size(); col++) {
			/* Use "Put" to add a row.
			 * Here patient_ID is row_key.
			 */
			Put p = new Put(Bytes.toBytes(csvArray.get(0).get(col)));
			/* Set values in the row, 
			 * specify (column family, column qualifier, value of cell).
			 * The column family must already exist in the HBase table.
			 */
			for (int row = 1; row < csvArray.size(); row++) {
				// Here we set ("mRNA", gene_name, expression level) 
				p.add(Bytes.toBytes("mRNA"),Bytes.toBytes(csvArray.get(row).get(0)),Bytes.toBytes(csvArray.get(row).get(col)));
			}
			/* Once finish updating a row, 
			 * commit the Put instance to the following
			 */
			table.put(p);
		}
		
		}
	
	private static List<List<String>> readCSV(String csvFileName) throws IOException {
		/* This function could read a .csv file 
		 * and return a 2D Arraylist
		 */
		String line = null;
		BufferedReader stream = null;
		List<List<String>> csvData = new ArrayList<List<String>>();
		
		try {
			stream = new BufferedReader(new FileReader(csvFileName));
			while ((line = stream.readLine()) != null)
				csvData.add(Arrays.asList(line.split(",")));	
		} finally {
			if(stream != null)
				stream.close();
		}
		
		return csvData;
	}

}
