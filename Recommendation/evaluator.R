#
#E6893 Big Data Analytics:
#Network Analysis on the Big Cancer Genome Data 
#                Final Project
#Performance Evaluator for the Treatment Recommendation Engine
#@author Tai-Hsien Ou Yang (to2232@columbia.edu)
#
load("pancan12_comp_ci.rda")

total_patient=nrow(composite_mat)
top_Patient_number=20

corList=matrix(0,total_patient,1)
rownames(corList)=rownames(composite_mat)[1:total_patient]


count_pos=0
for( vec.itr in 1:nrow(composite_mat) ){
	output_vec=composite_mat[ vec.itr , c(1:3,350:379)]
	treatment_vec=names( which( composite_mat[ vec.itr , 4:349 ]>0) )


	for(pt.itr in 1:nrow(composite_mat))
		corList[pt.itr,1]  =cor(output_vec,  composite_mat[pt.itr,c(1:3,350:379) ] ,method="kendall"  )

	neighborList=sort(corList[1:total_patient,1],decreasing=TRUE) [1:top_Patient_number]

	neighborList=neighborList[intersect(which(neighborList<1),which(neighborList>0))]

	treatment=rep(0,length(neighborList))
	for(pt.itr in 1:length(neighborList))
		treatment[pt.itr]= names( which( composite_mat[ names(neighborList)[pt.itr], 4:349 ]>0) )

	if(  length( intersect( treatment_vec, treatment[1]) ) )
		count_pos=count_pos+1

	cat( vec.itr,  treatment_vec, count_pos,  "\n")
}

score=count_pos/total_patient

write.table(score, file="TPR.kendall.txt")