package corr.mapreduce;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.mapreduce.Reducer;

import corr.writable.CIComputationWritable;
import corr.writable.VariableIndexPairsWritable;
import corr.writable.VariableValuePairsWritable;

/**
 * CI Algorithm A Mapper
 * @author Tai-Hsien Ou Yang
 *
 */

public class CIAReducer extends Reducer<VariableIndexPairsWritable, VariableValuePairsWritable, VariableIndexPairsWritable, CIComputationWritable> {

	
	@Override
	public void reduce(VariableIndexPairsWritable key, Iterable<VariableValuePairsWritable> values, Context context) throws IOException, InterruptedException {
		double x = 0.0d;
		String y = "";
		double n = 0.0d;

		//sort i first
	
		for(VariableValuePairsWritable pairs : values) {
			//change here, idx and GetI,J

			//change here, idx and GetI,J

			x+=pairs.getI();
			y+=pairs.getJ()+" ";
			n += 1.0d;

		}

		
		CIComputationWritable pearson = new CIComputationWritable(x, y, n);
		context.write(key, pearson);


	}
}
