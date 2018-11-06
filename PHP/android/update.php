<?php  
 $json = file_get_contents("php://input");
    $data = json_decode($json, true);
	$sno=$data['sno'];
$username=$data['username'];
	$subject=$data['subject'];
	$type=$data['type'];
	$guidance=$data['guidance'];
	$source=$data['source'];
	$technology=$data['technology'];
    $con =mysql_connect('127.0.0.1:3306','root','123456')or die(mysql_error());//连接数据库
    mysql_select_db('android');//选择数据库
    mysql_query('set names utf8');
    
    $sqll = "UPDATE elective SET subject='$subject', type='$type', guidance='$guidance'
	, source='$source', technology='$technology' , username='$username'where sno='$sno'";
     $rss=mysql_query($sqll);
	 $rc = mysql_affected_rows();
	 if($rc==1){
	 	 echo $sno;
        echo '提交成功，请继续操作！';
	 }
	 
mysqli_close($con);
?> 