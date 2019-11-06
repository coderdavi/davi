<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Validation\ValidationException;

class LoginController extends Controller
{
    //
    public function login(){
        return view('login');
    }
    public function store(Request $request){
        try {
            $data = $this->validate(
                $request,
                [
                    'email' => 'email|required',
                    'password' => 'required|min:5',
                ]
            );
        } catch (ValidationException $e) {
            return redirect()->back();
        }
        if(\Auth::attempt($data)){
            session()->flash('success','登录成功');
            return redirect('/');
        }
        session()->flash('danger','账号或密码错误');
        return back();
    }

    public function logout(){
        \Auth::logout();
        session()->flash('success','退出成功');
        return redirect('/');
    }
}
