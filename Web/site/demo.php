<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Panomic Cancer Analyzer</title>
<link href="style.css" rel="stylesheet" type="text/css" />
</head>
<body>

<?php

echo "<h1>Panomic Cancer Analyzer Report</h1>";


$file_name=$_POST["PatientID"];

exec("Rscript prog_demo.R $file_name");

$file = fopen( "/var/www/result/result.txt" , "r");

echo "<center>";
echo "<p>Profiles of the patients who is similar to selected profile (ID:".$file_name.")</p>";
echo "<p>The recommended treatments are sorted according to the expected outcome.</p>";
echo "</center>";

if( intval($file_name)<3316 & intval($file_name)>0 ){
	echo "<table >";
	while (!feof($file)) {
	     $value = fgets($file);
	   //$dummy = fgets($file);
	  //if( $value >0.5 & $value <1){
	  //  $ci_sum= $ci_sum+ $value;
	  //  $ci_count=$ci_count+1;
	  //}

	//print "&nbsp;" . $value . "<br>";
	   $entry = explode("\t", $value);
	print "<tr><td>".$entry[0]."</td><td>".$entry[1]."</td><td>".$entry[2]."</td><td>".$entry[3]."</td></tr>";

	}

	echo "</table>";
}else{
	echo "The input patient ID is illegal. Please return to the previous page.";
}
#if($ci_sum>0)

//print " Result=". round( $value ,2) . "<br>";

// Close the file that no longer in use
fclose($file);              

//echo "<center><p>Tai-Hsien Ou Yang, 2014</p></center>"


?>


<center><img src="images/result.png"></img></center>
<center><p>Tai-Hsien Ou Yang, 2014</p></center>
</body>
</html>