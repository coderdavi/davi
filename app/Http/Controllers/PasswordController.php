<?php

namespace App\Http\Controllers;

use App\Notifications\FindPasswordNotify;
use App\User;
use Illuminate\Http\Request;
use Illuminate\Validation\ValidationException;

class PasswordController extends Controller
{
    public function email()
    {
        return view('password.email');
    }

    public function send(Request $request)
    {
        $user = User::where('email', $request->email)->first();
        //  dd($user->email_token);
        \Notification::send($user, new FindPasswordNotify($user->email_token));
        return view('password.send',compact('user'));
    }

    public function edit($token)
    {
        $user = User::where("email_token", $token)->first();

        return view('password.edit', compact('user'));
    }

    public function update(Request $request)
    {
        try {
            $this->validate(
                $request,
                [
                    'password' => 'required|min:5|confirmed',
                ]
            );
        } catch (ValidationException $e) {
        }
        $user=$this->getUserByToken($request->token);
        $user->password = bcrypt($request->password);
        $user->save();
        session()->flash('success','修改密码成功');
        return redirect()->route('login');
    }
    protected function getUserByToken($token){
        return User::where('email_token',$token)->first();
    }
}
