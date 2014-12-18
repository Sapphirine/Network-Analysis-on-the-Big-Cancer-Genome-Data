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

import org.apache.hadoop.io.Writable;

/**
 * A pair of values corresponding to the i-th and j-th columns/variables.
 * @author Jee Vang
 *
 */
public class VariableValuePairsWritable implements Writable {

	private double i;
	private double j;
	
	/**
	 * Empty constructor.
	 */
	public VariableValuePairsWritable() {
		this(0.0d, 0.0d);
	}
	
	/**
	 * Constructor.
	 * @param i i-th variable value.
	 * @param j j-th variable value.
	 */
	public VariableValuePairsWritable(double i, double j) {
		this.i = i;
		this.j = j;
	}
	
	@Override
	public int hashCode() {
		return 37 + (new Double(i)).hashCode() + (new Double(j)).hashCode();
	}
	
	@Override
	public String toString() {
		return (new StringBuilder())
				.append('{')
				.append(getI())
				.append(',')
				.append(getJ())
				.append('}')
				.toString();
	}
	
	@Override
	public void readFields(DataInput in) throws IOException {
		i = in.readDouble();
		j = in.readDouble();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeDouble(getI());
		out.writeDouble(getJ());
	}

	/**
	 * Gets the i-th variable value.
	 * @return double.
	 */
	public double getI() {
		return i;
	}

	/**
	 * Sets the i-th variable value.
	 * @param i double.
	 */
	public void setI(double i) {
		this.i = i;
	}

	/**
	 * Gets the j-th variable value.
	 * @return double.
	 */
	public double getJ() {
		return j;
	}

	/**
	 * Sets the j-th variable value.
	 * @param j double.
	 */
	public void setJ(double j) {
		this.j = j;
	}

}
