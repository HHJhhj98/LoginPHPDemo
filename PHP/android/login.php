<?php

if(isset($_POST['button'])){
    $sno=$_POST['sno'];//得到用户输入的用户名
    $password=$_POST['password'];//密码
    $con=mysql_connect('127.0.0.1:3306','root','123456')or die(mysql_error());//连接数据库
    mysql_select_db('android');//选择数据库
    mysql_query('set names utf8');
    
    $sql = "select * from login where sno = '$sno' and password='$password'";
     $rs=mysql_query($sql);
     if(mysql_num_rows($rs)==1){//如果数据库的行数为1则成功否则失败
	 while($row = mysql_fetch_array($rs))
			{
   			 	 $role=$row["role"];
				 //$username=$row["username"];
			}
		echo $role;
        //echo $sno;
        //echo '已经成功登录';
        }else{
         //echo $username;
         echo '登录失败';
}
    }else{
    echo 'test!';
}
mysql_close($con);
?>
