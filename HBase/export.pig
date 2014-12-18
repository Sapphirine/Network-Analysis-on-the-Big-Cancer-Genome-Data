/** Export data in HBase table to .csv file with PIG.
* For the current case, expression data of gene "SUSD3" for 
* all the patients is exported to a folder "gene-data.csv" in .csv format
* Change the route as you need.
**/

REGISTER /home/kaiyi/Downloads/hbase-0.98.8-hadoop2/lib/hbase-common-0.98.8-hadoop2.jar
REGISTER /home/kaiyi/Downloads/hbase-0.98.8-hadoop2/lib/hbase-client-0.98.8-hadoop2.jar
REGISTER /home/kaiyi/Downloads/hbase-0.98.8-hadoop2/lib/htrace-core-2.04.jar
REGISTER /home/kaiyi/Downloads/pig-0.14.0/lib/piggybank.jar
REGISTER /home/kaiyi/Downloads/hbase-0.98.8-hadoop2/lib/zookeeper-3.4.6.jar

set hbase.zookeeper.quorum '127.0.1.1'
x = LOAD 'hbase://TCGA' USING org.apache.pig.backend.hadoop.hbase.HBaseStorage('mRNA:"SUSD3"','-loadKey true');
STORE x INTO 'gene-data.csv' USING PigStorage(',');