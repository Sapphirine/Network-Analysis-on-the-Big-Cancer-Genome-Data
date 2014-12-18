
package corr.mapreduce;

import java.io.IOException;

import java.lang.Math;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import corr.writable.VectorWritable;

/**
 * CI Mapper
 * @author Tai-Hsien Ou Yang
 *
 */

public class CIMapper extends 
	Mapper<LongWritable, VectorWritable, LongWritable, DoubleWritable> {

	private static final Log _log = LogFactory.getLog(CIMapper.class);
	

	public void map(LongWritable key,  VectorWritable value, Context context) throws IOException, InterruptedException {
	
		//Text key_to_Reducer = new Text("ci"); //new Text(key.toString())
		int size = value.size();
		double count_pos=0;
		double count_neg=0;
		DoubleWritable ci= new DoubleWritable();
		LongWritable key_to_Reducer = new LongWritable();
		//Text ci_to_Reducer = new Text("0.5"); 

				for(int i=0; i < size; i++) {
			for(int j=0; j < size; j++) {

				double time_i= Math.floor(value.get(i));
				double time_j= Math.floor(value.get(j));
				
				double status_i = 10*(value.get(i) - time_i);
				double status_j = 10*(value.get(j) - time_j);

				if( (status_i>0 ) & (time_i < time_j) ){ //prediction i->j is sorted, no need to compare
					if(i<j){
						count_pos++;
					}else{
						count_neg++;
					}
				}

				if( (status_j>0) & (time_i > time_j)   ){
					if(i>j){
						count_pos++;
					}else{
						count_neg++;
					}
				}

			}//BUG!!!
		}	//BUG!!!
			ci.set(count_pos/(count_pos+count_neg));
				//ci_to_Reducer.set(  ci.toString()   );
			key_to_Reducer.set(1);
			context.write(key_to_Reducer,ci );
	}
}
