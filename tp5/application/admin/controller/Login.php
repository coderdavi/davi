<?php

namespace app\admin\controller;

use app\admin\common\Base;

use think\Request;

use app\admin\model\Admin;
use think\Session;

class Login extends Base
{
	//渲染登录界面
	public function index()
	{
		$this->alreadyLogin();
		return $this -> view -> fetch('login');
	}

	//验证用户登录
	public function check(Request $request)
	{
		//设置状态
		$status=0;

		//获取表单提交的数据，并保存在变量中
		$data= $request->param();
		$userName =$data['username'];
		$password = md5($data['password']);
		//在admin表中进行查询 以用户为条件
		$map = ['username'=>$userName];
		$admin =Admin::get($map);

		//若果没有查询到该用户
		if(is_null($admin)){
			//设置返回信息
			$message ='用户名不正确';
		}elseif ($admin->password!=$password){
			$message='密码不正确';
		}else{
			$status=1;
			$message='验证通过，请点击进入后台';

			//更新表中登录次数和最后登录时间
			$admin->setInc("login_count");
			$admin->save(['last_time'=>time()]);
			//将用户登录的信息保存到session中
			Session::set('user_id',$userName);
			Session::set('user_info',$admin->toArray());
		}
		return ['status'=>$status,'message'=>$message];
	}
	//退出登录
	public function logout()

	{
		//
		Session::delete('user_id');
		Session::delete("user_info");
		//执行成功，返回登录界面
		$this->success('注销成功，正在返回','login/index');
	}
}
