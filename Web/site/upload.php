<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Panomic Cancer Analyzer</title>
<link href="style.css" rel="stylesheet" type="text/css" />
</head>
<body>
<?php
//$time_start = microtime(true); 

echo "<h1>Panomic Cancer Analyzer Report</h1>";

echo "<center>";
$target_dir = "uploads/"; // /var/www/html/uploads/
$target_file = $target_dir . basename($_FILES["fileToUpload"]["name"]);
$uploadOk = 1;
$imageFileType = pathinfo($target_file,PATHINFO_EXTENSION);
// Check if image file is a actual image or fake image
if(isset($_POST["submit"])) {
}
// Check if file already exists
if (file_exists($target_file)) {
    echo "Sorry, file already exists. ";
    $uploadOk = 0;
}

// Check file size
if ($_FILES["fileToUpload"]["size"] > 500000) {
    echo "Sorry, your file is too large. ";
    $uploadOk = 0;
}
// Allow certain file formats
if($imageFileType != "txt" && $imageFileType != "csv" && $imageFileType != "tsv" ) {
    echo "Sorry, only txt/csv/tsv files are allowed. ";
    $uploadOk = 0;
}
// Check if $uploadOk is set to 0 by an error
if ($uploadOk == 0) {
    echo "Sorry, your file was not uploaded. ";
// if everything is ok, try to upload file
} else {
    if (move_uploaded_file($_FILES["fileToUpload"]["tmp_name"], $target_file)) {
        echo "The file ". basename( $_FILES["fileToUpload"]["name"]). " has been uploaded.";
    } else {
        echo "Sorry, there was an error uploading your file.";
    }
}
echo "</center>";
echo "<p>The recommended treatments are sorted according to the expected outcome.</p>";
//$featureItemSelected =  $_POST ["featureItemSet"] ;
//$featuresImploded = implode (",", $featureItemSelected );
//if( $_POST ["customItem"]!="" ){
//   $CustomItem =  $_POST ["customItem"] ;
//    $featuresImploded = $featuresImploded. "," . $CustomItem;
// }
//$modelID = implode ("", $featureItemSelected ); //. $CustomItem; //rand();
$file_name=$_FILES["fileToUpload"]["name"];

exec("Rscript prog.R $file_name");

//$file = fopen( "/var/www/result/".$modelID , "r");

$file = fopen( "/var/www/result/result.txt" , "r");

echo "<table>";
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

#if($ci_sum>0)

//print " Result=". round( $value ,2) . "<br>";

// Close the file that no longer in use
fclose($file);              

//echo "<center><p>Tai-Hsien Ou Yang, 2014</p></center>"

//remove when release
//$time_end = microtime(true);
//$execution_time = ($time_end - $time_start);
//echo '<p>Total Execution Time: '.$execution_time.' seconds</p>';

?>

<center><img src="images/result.png"></img></center>
<center><p>Tai-Hsien Ou Yang, 2014</p></center>
</body>
</html>