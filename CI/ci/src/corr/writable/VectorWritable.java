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
package corr.writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.io.Writable;

/**
 * A vector of double values.
 * @author Jee Vang
 *
 */
public class VectorWritable implements Writable {

	private List<Double> values;
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Iterator<Double> iter = getValues().iterator();
		while(iter.hasNext()) {
			Double d = iter.next();
			sb.append(d.toString());
			if(iter.hasNext())
				sb.append(',');
		}
		return sb.toString();
	}
		
	/**
	 * Adds a value.
	 * @param value {@link Double}.
	 */
	public void add(Double value) {
		getValues().add(value);
	}
	
	/**
	 * Gets the value at the specified index.
	 * @param index Index.
	 * @return {@link Double}.
	 */
	public Double get(int index) {
		return getValues().get(index);
	}
	
	/**
	 * Gets the size of this vector.
	 * @return int.
	 */
	public int size() {
		return getValues().size();
	}
	
	@Override
	public void readFields(DataInput in) throws IOException {
		int size = in.readInt();
		values = new ArrayList<Double>(size);
		
		for(int i=0; i < size; i++) {
			double value = in.readDouble();
			values.add(value);
		}
	}

	@Override
	public void write(DataOutput out) throws IOException {
		List<Double> values = getValues();
		
		int size = values.size();
		out.writeInt(size);
		
		for(int i=0; i < size; i++) {
			out.writeDouble(values.get(i));
		}
	}

	/**
	 * Gets a {@link List} of {@link Double}.
	 * @return {@link List} of {@link Double}.
	 */
	private List<Double> getValues() {
		if(null ==  values)
			values = new ArrayList<Double>();
		return values;
	}

}
