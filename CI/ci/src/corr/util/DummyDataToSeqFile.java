/**
 * Copyright 2012 Jee Vang 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *  http://www.apache.org/licenses/LICENSE-2.0 
 *  
 *  Unless required by applicable law or agreed to in writing, software 
 *  distributed under the License is distributed on an "AS IS" BASIS, 
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *  See the License for the specific language governing permissions and 
 *  limitations under the License. 
 */
package corr.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;

import corr.writable.VectorWritable;

/**
 * Converts dummy data in CSV text form to binary SequentialFile.
 * Use {@link DummyDataGen} to generate the CSV file(s) first. 
 * Then use this one to do the conversion.
 * @author Jee Vang
 *
 */
public class DummyDataToSeqFile {

	/**
	 * Main method.
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		if(args.length < 1) {
			System.err.print("please specify a csv file or directory with csv files");
			return;
		}
		
		String path = args[0];
		File file = new File(path);
		if(file.isDirectory()) {
			export(file);
		} else {
			export(file.getCanonicalPath());
		}
	}
	
	/**
	 * Export all CSV files in the directory to SequenceFile format.
	 * @param directory Directory.
	 */
	private static void export(File directory) throws Exception {
		String[] files = directory.list(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				if(name.endsWith("csv"))
					return true;
				return false;
			}
		});
		
		for(int i=0; i < files.length; i++) {
			String file = files[i];
			export(file);
		}
	}
	
	/**
	 * Export CSV file to SequentialFile.
	 * @param file Name of CSV file.
	 * @throws Exception
	 */
	private static void export(String file) throws Exception {
		String delim = ",";
		BufferedReader reader = null;
		SequenceFile.Writer writer = null;
		
		try {
			Path path = toPath(file);
			Configuration conf = new Configuration();
			LocalFileSystem fs = FileSystem.getLocal(conf);
			writer = SequenceFile.createWriter(
					fs, conf, path, LongWritable.class, VectorWritable.class);
			
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			long counter = 0;
			while(null != (line = reader.readLine())) {
				if("".equals(line))
					continue;
				String[] tokens = line.split(delim);
				
				LongWritable key = new LongWritable(counter);
				VectorWritable val = toVector(tokens);
				writer.append(key, val);
				
				counter++;
			}
		} catch(Exception ex) {
			throw ex;
		} finally {
			if(null != reader) {
				try { reader.close(); }
				catch(Exception ex) { }
			}
			
			if(null != writer) {
				try { writer.close(); }
				catch(Exception ex) { }
			}
		}
	}
	
	/**
	 * Converts string path to {@link Path}.
	 * @param file CSV file path.
	 * @return {@link Path}.
	 */
	private static Path toPath(String file) {
		StringBuilder sb = new StringBuilder();
		int indexOfDot = file.lastIndexOf('.');
		if(-1 != indexOfDot) {
			sb.append(file.subSequence(0, indexOfDot));
		} else {
			sb.append(file);
		}
		sb.append(".seq");
		String s = sb.toString();
		return new Path(s);
	}
	
	/**
	 * Converts an array of string tokens to a {@link VectorWritable}.
	 * @param tokens Array of string where each element can be parsed into a double.
	 * @return {@link VectorWritable}.
	 */
	private static VectorWritable toVector(String[] tokens) {
		VectorWritable vector = new VectorWritable();
		for(int i=0; i < tokens.length; i++) {
			double d = Double.parseDouble(tokens[i]);
			vector.add(d);
		}
		return vector;
	}

}
