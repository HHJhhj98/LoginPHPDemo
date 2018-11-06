<?php
$json = file_get_contents("php://input");
    $data = json_decode($json, true);
 $sno=$data['sno'];
    $username=$data['username'];//得到用户输入的手机号
	$password=$data['password'];//密码 
    $con =mysql_connect('127.0.0.1:3306','root','123456')or die(mysql_error());//连接数据库
    mysql_select_db('android');//选择数据库
    mysql_query('set names utf8');
	
	 $sql = "select * from login
     where sno = '$sno' and password='$password'";
     $rs=mysql_query($sql);
     if(mysql_num_rows($rs)==1){//如果数据库的行数为1则成功否则失败
        echo $sno;
        echo '用户已经存在，请直接登录！';
        }else{
    
    $sqll = "INSERT INTO login SET sno ='$sno', password='$password',username='$username'";
     $rss=mysql_query($sqll);
	 $rc = mysql_affected_rows();
	 if($rc==1){
	 	 echo $sno;
        echo '注册成功，请继续登录！';
	 }
    }  
   
mysql_close($con);
?>