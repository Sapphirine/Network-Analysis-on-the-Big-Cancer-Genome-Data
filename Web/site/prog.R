args <- commandArgs(TRUE)
 
load("/var/www/pancan12_comp_ci.rda")

input_filename <- unlist(strsplit(as.character(args[1]), ",", fixed = TRUE))
#input_filename<- as.numeric(args[1])

result_filename <- "/var/www/result/result.txt"

input_vec<-read.table(paste("/var/www/html/uploads/",input_filename ,sep="")) 


#input_vec=read.table("tcga.1.profile.csv")

output_vec=data.matrix(input_vec[,2])
rownames(output_vec)=data.matrix(input_vec[,1])

total_patient=nrow(composite_mat)
top_Patient_number=20

corList=matrix(0,total_patient,1)
rownames(corList)=rownames(composite_mat)[1:total_patient]

for(i in 1:total_patient)
	corList[i,1]=cor(output_vec,  composite_mat [i,c(1:3,350:379)],method="kendall")



neighborList=sort(corList[1:total_patient,1],decreasing=TRUE) [1:top_Patient_number]
neighborList=neighborList[intersect(which(neighborList<1),which(neighborList>0))]

treatment=rep(0,length(neighborList))
for(i in 1:length(neighborList)){
	treatment[i]= paste( names( which( composite_mat[ names(neighborList)[i], 4:349 ]>0) ) , collapse=",")
	if(treatment[i]=="")
		treatment[i]="Resection and Radiotherapy"
}


neighborOutput=cbind( 	names(neighborList), 
						round(neighborList,2),
						composite_mat[names(neighborList), "days_to_last_followup"],
						treatment
						   )

rownames(neighborOutput)=names(neighborList)
neighborOutput= neighborOutput[ order(as.numeric(neighborOutput[,3]),decreasing=TRUE) ,   ]
colnames(neighborOutput)=c("PatientID","Confidence","Followup(Days)","Treatment")


write.table(neighborOutput, file=result_filename, col.names = T, row.names = F ,quote=F,sep="\t")
#write.table(neighborList , file=result_filename,sep="," )


colors <- rainbow(length(output_vec)) 
png( "/var/www/html/images/result.png" ,width=800,height=600,units="px" )
plot(1:length(output_vec),log(output_vec+1), lty=1, lwd=5, type="l", xlab="Features", ylab="Normalized Levels", main="Correlation Analysis for the Input Profile (Thick Line)")
for(i in 1:length(neighborList))
	lines(1:length(output_vec), log(composite_mat[names(neighborList)[i],c(1:3,350:379)]+1) , col=colors[i])

dev.off()
