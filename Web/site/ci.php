<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Panomic Cancer Analyzer</title>
<link href="style.css" rel="stylesheet" type="text/css" />
</head>
<body>

<?php

echo "<h1>Concordance Index Evaluator</h1>";

$feature_name=$_POST["featureName"];

exec("Rscript hist.R $feature_name");



$file = fopen( "/var/www/result/ciList.csv" , "r");

echo "<center>";
echo "<p>The concordance index (CI) of  ".$feature_name."</p>";
echo "<p>CI>0.5: The value of feature increases along with survival time.<br>CI<0.5: The value of feature decreases along with survival time.</p>";

	echo "<table >";
	echo "<tr><td>Feature Name</td><td>Concordance Index</tr>";
	while (!feof($file)) {
	     $value = fgets($file);
	   //$dummy = fgets($file);
	  //if( $value >0.5 & $value <1){
	  //  $ci_sum= $ci_sum+ $value;
	  //  $ci_count=$ci_count+1;
	  //}

	//print "&nbsp;" . $value . "<br>";
	   $entry = explode(",", $value);
	   if($entry[0]==$feature_name ){
			print "<tr><td>".$entry[0]."</td><td>".$entry[1]."</td></tr>";
		}
	}
	echo "</table>";
	echo "</center>";


#if($ci_sum>0)

//print " Result=". round( $value ,2) . "<br>";

// Close the file that no longer in use
fclose($file);              



?>

</body>
</html>

<center><img src="images/hist.png"></img></center>
<center><p>Tai-Hsien Ou Yang, 2014</p></center>
</body>
</html>