@extends('layouts.default')
@section('content')
    <form action="{{route('FindPasswordUpdate')}}" method="post">
        @csrf
        <input type="text" hidden name="token" value="{{$user['email_token']}}">
        <div class="card">
            <div class="card-header">
                重置密码
            </div>
            <div class="card-body">
                <div class="form-group">
                    <label for="">邮箱</label>
                    <input type="text" disabled class="form-control" name="email" value="{{$user['email']}}">
                    <small id="helpId" class="form-text text-muted">
                        请输入你的注册邮箱
                    </small>
                </div>
                <div class="form-group">
                    <label for="">密码</label>
                    <input type="password" class="form-control" name="password">
                </div>
                <div class="form-group">
                    <label for="">确认密码</label>
                    <input type="password" class="form-control" name="password_comfirmation">
                </div>
            </div>
            <div class="card-footer text-muted">
                <button type="submit" class="btn btn-success">发送</button>
            </div>
        </div>
    </form>
@endsection
