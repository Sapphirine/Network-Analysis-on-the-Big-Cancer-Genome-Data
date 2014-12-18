#
#E6893 Big Data Analytics:
#Network Analysis on the Big Cancer Genome Data 
#                Final Project
#Outcome-Associated Genes Identification and Bayesian Network Inference
#@author Tai-Hsien Ou Yang (to2232@columbia.edu)
#
dirList=dir()

ciList=rep(0.5,length(dirList))
for(i in 1:length(dirList)){
	ci=read.table(paste(dirList[i],"/part-r-00000",sep="")   )
	ciList[i]=as.numeric(ci[2])
	names(ciList)[i]=strsplit(dirList[i],"[.]")[[1]][1]
	cat(i, names(ciList)[i], ciList[i], "\n")
}

ci=cbind(names(ciList),ciList)
ci[,2]=round(1-as.numeric(ci[,2]),2)

write.table(ci, file="ciList.csv", quote=F,row.name=F,sep=",")

civarList=names( sort(abs(ciList-0.5),decreasing=TRUE))[1:30]
cat(civarList, sep=" ")

write(civarList, file="ciListList.top30.csv",sep=",")
#"SCGB2A2","CYP4Z2P","C6orf127","ANKRD30A","VTCN1",,"PIP","CYP4Z1","BNIPL","IRX5","ESR1","TFDP3","CRABP2","CALML5","LOC642587","ZYG11A","PCP2","PIAS3","CLIP2","GATA3","KRT7","FLJ45983","C20orf114","SIX4","MSMB","PRKCA","PLEKHG4B","SATB2","HKDC1","PGLYRP2","METTL7B"

load("pancan12.ge.rda")
load("pancan12.clinical.rda")
load("followup.rda")

colnames(ge_mat)=substr(colnames(ge_mat),1,12)
composite_mat=cbind(clnc, t(ge_mat[civarList, rownames(clnc)] ), followup[rownames(clnc),])

save(composite_mat, file="pancan12_comp_ci.rda")

library("bnlearn")
bnn<-fast.iamb(data.frame(composite_mat[,c(1:3, 350:379)]))

arc.bn<-arcs(bnn)

for(i in 1:nrow(arc.bn))
 cat(paste('{source:"', arc.bn[i,1] ,'", target:"', arc.bn[i,2]  , '", type:"induce"},\n', sep=""))


