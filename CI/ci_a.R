#
#E6893 Big Data Analytics:
#Network Analysis on the Big Cancer Genome Data 
#                Final Project
#Performance Evaluator for the Treatment Recommendation Engine
#@author Tai-Hsien Ou Yang (to2232@columbia.edu)
#

ciList<-read.table("part-r-00000", sep="\t")
ci=length(ciList[which(ciList[,2]>0),2])/(length(ciList[which(ciList[,2]>0),2])+length(ciList[which(ciList[,2]<0),2]))

write.table(ci, file="ci.txt")