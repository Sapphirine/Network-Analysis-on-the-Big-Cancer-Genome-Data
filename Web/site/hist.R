args <- commandArgs(TRUE)


load("/var/www/pancan12.ge.rda")
input_featurename<- as.character(args[1])


png( "/var/www/html/images/hist.png" ,width=800,height=600,units="px" )

hist( log2(ge_mat[input_featurename,]+1), col="cadetblue1" ,breaks =100, xlab=input_featurename, main=paste("Histogram of", input_featurename) )

dev.off()
