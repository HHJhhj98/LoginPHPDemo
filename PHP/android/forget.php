<?php
if(isset($_POST['button'])){
    $sno=$_POST['sno'];//得到用户输入的手机号 
    $con=mysql_connect('127.0.0.1:3306','root','123456')or die(mysql_error());//连接数据库
    mysql_select_db('android');//选择数据库
    mysql_query('set names utf8');
    
    $sql = "select password from login
     where sno = '$sno' ";
     $rs=mysql_query($sql);
	 //$result = mysql_affected_rows(); 
	 //如果$result 值为-1，表明语句没有成功执行，可能是语句格式有问题等等.
	 //如果$result 值为0，表明语句成功执行，但是查询结果为空.
	 //如果$result 值为x(x>0)，表明成功执行，且查询结果中有x条记录
	 //echo $result;
	 if(mysql_num_rows($rs)==1){
	 while($row = mysql_fetch_array($rs))
			{
   			 	 $password=$row["password"];
			}
		echo $password;
        //echo '成功找回密码!';
        }else{
		echo $username;
		 //echo '用户不存在,请先注册!';
		}
    }else{
    echo 'test!';
}
mysql_close($con);
?>