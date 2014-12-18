
package corr.mapreduce;

import java.io.IOException;

import java.lang.Math;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;

import corr.writable.VariableIndexPairsWritable;
import corr.writable.VariableValuePairsWritable;
import corr.writable.VectorWritable;


/**
 * CI Algorithm A Mapper
 * @author Tai-Hsien Ou Yang
 *
 */

public class CIAMapper extends 
	Mapper<LongWritable, VectorWritable, VariableIndexPairsWritable, VariableValuePairsWritable> {


	public void map(LongWritable key,  VectorWritable value, Context context) throws IOException, InterruptedException {
	

		int size = value.size();
		double one=1;
		double zero=0;
		for(int i=0; i < size; i++) {
			for(int j=i+1; j < size; j++) {
				VariableIndexPairsWritable k2 = new VariableIndexPairsWritable(i, j);
				VariableValuePairsWritable v2 = new VariableValuePairsWritable(zero, zero);

				double time_i= Math.floor(value.get(i));
				double time_j= Math.floor(value.get(j));
				
				double status_i = 10*(value.get(i) - time_i);
				double status_j = 10*(value.get(j) - time_j);


				if( (status_i>0 ) & (time_i < time_j) & (i<j)  ){ //prediction i->j is sorted, no need to compare
					v2.setI(one);
					v2.setJ(one);
				} else if( (status_j>0) & (time_i > time_j) & (i>j)  ){
					v2.setI(one);
					v2.setJ(one);
				}else if( (status_i>0) & (time_i < time_j) & (i>j) ){
					v2.setI(-1*one);
					v2.setJ(-1*one);
				}else if( (status_j>0)  & (time_i > time_j) & (i<j) ){
					v2.setI(-1*one);
					v2.setJ(-1*one);
				}else{
					v2.setI(zero);
					v2.setJ(zero);
				}
				
				context.write(k2, v2);//not write
		
			}
		}
	}
}
