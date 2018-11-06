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
	
	 $sql = "select * from elective where sno = '$sno'";
     $rs=mysql_query($sql);
     if(mysql_num_rows($rs)==1){//如果数据库的行数为1则成功否则失败
        echo $sno;
        echo '用户已经存在，请直接登录！';
        }else{
    
    $sqll = "INSERT INTO elective SET sno ='$sno', subject='$subject', type='$type'
	, guidance='$guidance', source='$source', technology='$technology', username='$username'";
     $rss=mysql_query($sqll);
	 $rc = mysql_affected_rows();
	 if($rc==1){
	 	 echo $sno;
        echo '提交成功，请继续操作！';
	 }
	 }
mysqli_close($con);
?> 