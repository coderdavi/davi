<?php

namespace app\admin\controller;

use app\admin\common\Base;

use app\admin\model\Banner as BannerModel;
class Banner extends Base
{
    /**
     * 显示资源列表
     *
     * @return \think\Response
     */
    public function index()
    {
    	$banner =BannerModel::all();
    	$count = BannerModel::count();

    	$this->view->assign('banner',$banner);
    	$this->view->assign('count',$count);
	    return $this->view->fetch("banner_list");
    }

    /**
     * 显示创建资源表单页.
     *
     * @return \think\Response
     */
    public function create()
    {
	    return $this->view->fetch('banner_add');
    }

    /**
     * 保存新建的资源
     *
     * @param  \think\Request  $request
     * @return \think\Response
     */
    public function save()
    {
	    //判断一下提交类型
	    if ($this->request->isPost()) {

		    //1.获取一下提交的数据,包括上传文件
		    $data = $this->request->param(true);

		    //2获取一下上传的文件对象
		    $file = $this->request->file('image');

		    //3判断是否获取到了文件
		    if (empty($file)) {
			    $this->error($file->getError());
		    }

		    //4上传文件
		    $map = [
			    'ext'=>'jpg,png',
			    'size'=> 3000000
		    ];
		    $info = $file->validate($map)->move(ROOT_PATH.'public/uploads/');
		    if (is_null($info)){
			    $this->error($file->getError());
		    }

		    //5向表中新增数据
		    $data['image'] = $info -> getSaveName();

		    $res = BannerModel::create($data);

		    //6判断新增是否成功
		    if (is_null($res)){
			    $this->error('新增失败');
		    }

		    $this->success('新增成功~~');

	    }else {
		    $this -> error('请求类型错误~~');
	    }
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
    public function edit($id)
    {
	    $data =BannerModel::get($id);
    	$this->view->assign('data',$data);
    	return $this->view->fetch('banner_edit');
        //
    }

    /**
     * 保存更新的资源
     *
     * @param  \think\Request  $request
     * @param  int  $id
     * @return \think\Response
     */
    public function update()
    {
    	$data = $this->request->param(true);
    	$file = $this->request->file('image');
    	$info =$file->validate(['ext'=>'jpg,png','size'=>3000000])->move(ROOT_PATH.'public/uploads/');
    	if(is_null($info)){
    		$this->error($file->getError());
	    }
	    $res = BannerModel::update([
	    	'image'=>$info->getSaveName(),
		    'link'=>$data['link'],
		    'desc'=>$data['desc'],
	    ],['id'=>$data['id']]);
    	if (is_null($res)){
    		$this->error('更新失败');
	    }
	    $this->success('更新成功~~');
        //
    }

    /**
     * 删除指定资源
     *
     * @param  int  $id
     * @return \think\Response
     */
    public function delete($id)
    {
        BannerModel::destroy($id);
    }
}
