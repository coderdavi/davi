<?php

namespace app\admin\controller;

use app\admin\common\Base;
use think\Request;
use app\admin\model\Admin as AdminModel;

class Admin extends Base
{
	//显示管理员首页
    public function index()
    {
    	//1.读取admin管理员表的信息
	    $admin=AdminModel::get(['username'=>'admin']);
	    //2.将当前管理员的信息赋值给模板
	    $this->view->assign('admin',$admin);
	    return $this->view->fetch("admin_list");
    }

	//渲染编辑模板
	public function edit(Request $request){
		//1.读取admin管理员表的信息
		$admin = AdminModel::get($request->param('id'));

		//2.将当前管理员的信息赋值给模板
		$this -> view -> assign('admin', $admin);
		//3.渲染模板
		return $this -> view -> fetch('admin_edit');
	}
	public function update(Request $request){
    	if ($request->isAjax(true)){
    		//获取提交的数据,自动过滤一下空值
		    $data=array_filter($request->param());
		    //设置更新条件
		    $map =['is_update'=> $data['is_update']];
		    //更新用户表
		    $res = AdminModel::update($data,$map);
		    //更新成功提示信息
		    $status=1;
		    $message="更新成功";

		    if (is_null($res)){
		    	$status=0;
		    	$message='更新失败';
		    }
	    }
	    return ['status'=>$status, 'message'=>$message];
	}

}
