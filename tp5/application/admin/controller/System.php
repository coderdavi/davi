<?php

namespace app\admin\controller;

use app\admin\common\Base;
use think\Request;
use app\admin\model\System as SystemModel;

class System extends Base
{

	public function index()
	{
		//1.读取admin管理员表的信息
		$system = SystemModel::get(1);
		//2.将当前管理员的信息赋值给模板
		$this->view->assign('system', $system);
		return $this->view->fetch("system_list");
	}

	//更新配置表
	public function update(Request $request)
	{
		if ($request->isAjax(true)) {
			$data = $request->param();
			$map = ['is_update' => $data['is_update']];
			$res = SystemModel::update($data, $map);
			//设置更新返回信息
			$status = 1;
			$message = '更新成功';

			//如果更新失败
			if (is_null($res)) {
				$status = 0;
				$message = '更新失败';
			}
		}

		//返回更新结果
		return ['status' => $status, 'message' => $message];
	}
}
