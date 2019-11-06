<?php

namespace app\admin\controller;

use app\admin\common\Base;

use think\Request;

use app\admin\model\Category as CategoryModel;

class Category extends Base
{
	/**
	 * 显示资源列表
	 *
	 * @return \think\Response
	 */
	public function index()
	{
		//1、获取分类信息
		$cate = CategoryModel::getCate();

		//2.用模型获取分页数据
		$cate_list=CategoryModel::order(['id'])->  paginate(5);

		//3.获取记录数量
		$count = CategoryModel::count();

		//4.模板赋值
		$this -> view -> assign('cate', $cate);
		$this -> view -> assign('cate_list', $cate_list);
		$this -> view -> assign('count', $count);


		//3.模板渲染
		return $this -> view -> fetch('category_list');
	}

	/**
	 * 显示创建资源表单页.
	 *
	 * @return \think\Response
	 */
	public function create(Request $request)
	{
		//1.设置返回的值
		$status = 1;
		$message = '添加成功';

		//2.添加数据到分类表中
		$res = CategoryModel::create([
			'cate_name'=> $request->param('cate_name'),
			'pid'=> $request->param('pid')
		]);

		//3.添加失败的处理
		if (is_null($res)) {
			$status = 0;
			$message = '添加失败';
		}

		return ['status'=> $status, 'message'=> $message, 'res'=> $res->toJson()];
	}

	/**
	 * 保存新建的资源
	 *
	 * @param  \think\Request  $request
	 * @return \think\Response
	 */
	public function save(Request $request)
	{
		//
	}

	/**
	 * 显示指定的资源
	 *
	 * @param  int  $id
	 * @return \think\Response
	 */
	public function read($id)
	{
		//
	}

	/**
	 * 显示编辑资源表单页.
	 *
	 * @param  int  $id
	 * @return \think\Response
	 */
	public function edit(Request $request)
	{
		//1.获取一下分类id
		$cate_id = $request -> param('id');

		//2.查询要更新的数据
		$cate_now = CategoryModel::get($cate_id);

		//3.递归查询所有的分类信息
		$cate_level = CategoryModel::getCate();

		//4.模板赋值
		$this -> view -> assign('cate_now', $cate_now);
		$this -> view -> assign('cate_level', $cate_level);


		//5.渲染模板
		return $this -> view -> fetch('category_edit');
	}

	/**
	 * 保存更新的资源
	 *
	 * @param  \think\Request  $request
	 * @param  int  $id
	 * @return \think\Response
	 */
	public function update(Request $request)
	{
		//1.获取一下提交的数据
		$data = $request -> param();

		//2.更新操作
		$res = CategoryModel::update([
			'cate_name' => $data['cate_name'],
			'cate_order' => $data['cate_order'],
			'pid' => $data['pid'],
		],['id'=> $data['id']]);

		//3.设置返回值
		$status = 1;
		$message = '更新成功';

		//4.设置更新失败的返回值
		if (is_null($res)) {
			$status = 0;
			$message = '更新失败';
		}

		//5.返回结果
		return ['status'=>$status, 'message'=> $message];
	}

	/**
	 * 删除指定资源
	 *
	 * @param  int  $id
	 * @return \think\Response
	 */
	public function delete($id)
	{
		//1. 删除以当前ID为父ID的所有子分类
		CategoryModel::destroy(function ($query) use ($id){
			$query->where(['pid'=> $id]) -> field('id');
		});

		//2.删除当前分类
		CategoryModel::destroy($id);

	}


}
