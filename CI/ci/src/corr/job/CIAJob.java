/**
 * Copyright Tai-Hsien Ou Yang 2014 
 * The writable classes and the sequence file conversion class are from Dr. Gee Vang's Blog
 * http://vangjee.wordpress.com/2012/02/29/computing-pearson-correlation-using-hadoops-mapreduce-mr-paradigm/
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
package corr.job;

import java.util.Date;
import java.util.Formatter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.DoubleWritable;
import corr.writable.CIComputationWritable;
import corr.writable.VariableIndexPairsWritable;
import corr.writable.VariableValuePairsWritable;
import corr.mapreduce.CIAMapper;
import corr.mapreduce.CIAReducer;




/**
 * CI Algorithm A job used against SequenceFile.
 * @author Tai-Hsien Ou Yang
 *
 */
public class CIAJob extends Configured implements Tool {


	public static void main(String[] args) throws Exception {
		int result = ToolRunner.run(new Configuration(), new CIAJob(), args);
		System.exit(result);
	}

	@Override
	public int run(String[] args) throws Exception {
		if(args.length != 1) {
			System.err.println("usage: CIAJob <input path>");
			ToolRunner.printGenericCommandUsage(System.err);
			return -1;
		}
		
		String inPath = args[0];
		Formatter formatter = new Formatter();
        String outPath = "." + args[0] + "_"
                + formatter.format("%1$tm%1$td%1$tH%1$tM%1$tS", new Date());
		
        Configuration config = getConf();
		Job job = new Job(config, "Algorithm A Concordance Index");
		job.setJarByClass(CIAJob.class);
		
		FileInputFormat.addInputPath(job, new Path(inPath));
		FileOutputFormat.setOutputPath(job, new Path(outPath));
		
		job.setInputFormatClass(SequenceFileInputFormat.class);
		
		job.setMapperClass(CIAMapper.class);
		job.setReducerClass(CIAReducer.class);
		
		job.setMapOutputKeyClass(VariableIndexPairsWritable.class);
		job.setMapOutputValueClass(VariableValuePairsWritable.class);
		
		job.setOutputKeyClass(VariableIndexPairsWritable.class);
		job.setOutputValueClass(CIComputationWritable.class);
	
		return job.waitForCompletion(true) ? 0 : 1;
	}

}
