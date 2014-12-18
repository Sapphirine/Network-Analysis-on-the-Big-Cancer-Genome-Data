#
#E6893 Big Data Analytics:
#Network Analysis on the Big Cancer Genome Data 
#                Final Project
#Preprocessing Script for the CI Toolkit
#@author Tai-Hsien Ou Yang (to2232@columbia.edu)
#

load("pancan12.ge.rda")
load("followup.rda")
followup.hashed=followup[,1]+0.1*followup[,2]

colnames(ge_mat)=substr(colnames(ge_mat),1,12)
patientID=intersect(names(followup.hashed), colnames(ge_mat))
ge_mat=ge_mat[,patientID]


mat.hashed=matrix(0,nrow(ge_mat),ncol(ge_mat))
for( featureID in 1:nrow(ge_mat) ){
	patientID.sorted=names(sort(ge_mat[featureID,patientID],decreasing=TRUE))
	mat.hashed[featureID,]=followup.hashed[patientID.sorted]
	cat(featureID,"\n")
}

for(i in 1:nrow(ge_mat) )
	write.table(t(mat.hashed[i,]), file=paste("./hashedfeature/",rownames(ge_mat)[i],".csv",sep=""), quote=F, row.name=F,col.name=F,sep=",")
