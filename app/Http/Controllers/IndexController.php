<?php

namespace App\Http\Controllers;

use App\Blog;
use App\Mail\RegMail;
use App\User;
use Illuminate\Http\Request;

class IndexController extends Controller
{
    public function home(){
    //    dd(Blog::find(1)->user);
        $blogs=Blog::orderBy('id','DESC')->with('user')->paginate(10);
//        $user=User::find(1);
//        \Mail::to($user)->send(new RegMail());
       // session()->flash('success','操作成功');
        return view('home',compact('blogs'));
    }
}
