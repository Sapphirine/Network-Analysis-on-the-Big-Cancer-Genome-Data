
package corr.mapreduce;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;

/**
 * CI Reducer
 * @author Tai-Hsien Ou Yang
 *
 */
public class CIReducer extends Reducer<LongWritable,  DoubleWritable, LongWritable, Text> {

	private static final Log _log = LogFactory.getLog(CIReducer.class);
	
	@Override
	public void reduce(LongWritable key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
		double y = 0;
		//DoubleWritable ci= new DoubleWritable();
		Text ci = new Text("0.5"); 

		//sort i first
	
		for(DoubleWritable pairs : values) {

			y=pairs.get();

		}

		ci.set( String.valueOf(y) );
		context.write(key,ci  );
		

	}
}
