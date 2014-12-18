# @author=Tai-Hsien Ou Yang
path="/media/taihsien/My Book/workspace/PanCan12/clinical/"
fnList=dir(path=path,pattern="clinical.txt")

clnc_features=c("age_at_initial_pathologic_diagnosis", "lymph_node_examined_count", "weight","days_to_last_followup","days_to_death","vital_status")



sample.total=0
dose=NULL
sample_id=NULL
for(fn in fnList){
	covar.clnc=read.csv(paste(path,fn,sep=""),sep="\t")
	dose=c(dose, colnames(covar.clnc)[grep(   "dose", colnames(covar.clnc) )])
	sample.total=sample.total+nrow(covar.clnc)
	sample_id=c(sample_id, rownames(covar.clnc))
	cat(fn,  "OK\n")
}

doselist=unique(dose)

col.total=length(clnc_features)+length(doselist)+1
clnc_mat=matrix( NA, sample.total, col.total  ) 
colnames(clnc_mat)=c("source", clnc_features, doselist)
rownames(clnc_mat)=sample_id 

for(fn in fnList){
	covar.clnc=read.csv(paste(path,fn,sep=""),sep="\t")
	sample_int=intersect( rownames(covar.clnc), rownames(clnc_mat))
	feature_int=intersect( colnames( covar.clnc), colnames(clnc_mat) )

	for( sample.itr in sample_int)
		for(feature.itr in feature_int)
			clnc_mat[sample.itr, feature.itr]=covar.clnc[sample.itr, feature.itr]

	clnc_mat[sample_int,"source"]=rep( strsplit(fn, "[.]")[[1]][1] , length(sample_int))
	cat(fn, "OK\n")
}


for(i in 1:nrow(clnc_mat)){
	if(is.na(clnc_mat[i,"days_to_last_followup"]==TRUE))
		clnc_mat[i,"days_to_last_followup"]=clnc_mat[i,"days_to_death"]
	if(is.na(clnc_mat[i,"days_to_last_followup"]==TRUE))
		clnc_mat[i,"days_to_last_followup"]=7000

	if(clnc_mat[i,"vital_status"]=="2"){ #1=NA, 2=DECEASED, 3=LIVING
			clnc_mat[i,"vital_status"]=1
	}else{
			clnc_mat[i,"vital_status"]=0
	}
}

time=clnc_mat[,"days_to_last_followup"]
status=clnc_mat[,"vital_status"]


save(clnc_mat, file="pancan12.clinical.rda")
save(time,status, file="pancan12.surv.rda")

#

fnList=dir(path="/media/taihsien/My Book/workspace/PanCan12/RNAseq.tumor.new"
,pattern=".rda")


sample_id=NULL
for(fn in fnList){
	load(fn)
	gn=rownames(e)
	sample_id=c(sample_id, colnames(e))
	cat(fn,"OK\n")
}


sample_id=sample_id[which(is.na(sample_id)==F)]
ge_mat=matrix(NA, length(gn), length(sample_id) )
rownames(ge_mat)=gn
colnames(ge_mat)=sample_id

for(fn in fnList){
	load(fn)
	sample_int=intersect(colnames(ge_mat),colnames(e))
	ge_mat[,sample_int]=e[,sample_int]
	cat(fn,"OK\n")
}

save(ge_mat,file="pancan12.ge.rda")


#

ge_mat=log2(ge_mat+1)
ge_mat=scale(ge_mat) #scale columwise
library(impute)

ge_mat=impute.knn(ge_mat)$data



#
mvList=rep(0,ncol(ge_mat))
names(mvList)=colnames(ge_mat)
for( i in 1:ncol(ge_mat))
	mvList[i]=length(which(is.na(ge_mat[,i])==TRUE))

ge_mat=ge_mat[, which(mvList<1)]

save(ge_mat,file="pancan12.ge.scaled.rda")


varList=rep(0, nrow(ge_mat))
names(varList)=rownames(ge_mat)

for(i in 1:nrow(ge_mat))
	varList[i]=var(ge_mat[i,])

save(varList, file="pancan12.varList.rda")

#

colnames(ge_mat)=substr(colnames(ge_mat),1,12)
sample_int=intersect(colnames(ge_mat),rownames(clnc_mat))


ge_topvar=t(ge_mat[ names( sort(varList,decreasing=T)[1:1000]),sample_int])
composite_mat=cbind(clnc_mat[sample_int,], ge_topvar)

composite_mat[sample_int,"days_to_last_followup"]=as.numeric(clnc_mat[sample_int,"days_to_last_followup"])
composite_mat[sample_int,"vital_status"]=as.numeric(clnc_mat[sample_int,"vital_status"])
class(composite_mat)="numeric"
#save(composite_mat, file="pancan12_comp.rda")

#

library("impute")

#sample_deceased=which(composite_mat[,"vital_status"] =="1")
df=composite_mat
for( i in 1:ncol(df)){
	if( length(which(is.na(df[,i])==TRUE) >nrow(df)*0.5 ))
	df[which(is.na(df[,i])==TRUE),i]=0
}

df=impute.knn(df)$data
save(df, file="pancan12_comp_topvar_imputed.rda")

#
library("gislkit")

load("pancan12.ge.scaled.rda")

colnames(ge_mat)=substr(colnames(ge_mat),1,12)
sample_int=intersect(colnames(ge_mat),rownames(df))

ge_mat=ge_mat[,sample_int]
clnc_mat=df[sample_int,1:353]

ciList=rep(0.5, nrow(ge_mat))
names(ciList)=rownames(ge_mat)
for(i in 1:nrow(ge_mat))
	ciList[i]=equalci(ge_mat[i,], cbind(clnc_mat[,"days_to_last_followup"],clnc_mat[sample_int,"vital_status"]))

save(ciList, file="pancan12.varList.rda")


ge_topci=t(log2(ge_mat[ names( sort(abs(ciList-0.5),decreasing=T)[1:2000]),sample_int]+1))
composite_mat=cbind(clnc_mat[sample_int,], ge_topci)
class(composite_mat)="numeric"

save(composite_mat, file="pancan12_comp_ci.rda")


dfred=data.frame(composite_mat[,c(2:5,354:1353)]) #354
bnn<-gs(dfred, alpha = 0.05)

library("Rgraphviz")

graphviz.plot(bnn, layout="neato", main="TCGA Breast Cancer Baysian Network (p<0.005)")

#equalci( ge_mat["SUSD3",sample_int],cbind(clnc_mat[,"days_to_last_followup"],clnc_mat[sample_int,"vital_status"]))
#SUSD3 0.4069795
#FGD3 0.3791806



