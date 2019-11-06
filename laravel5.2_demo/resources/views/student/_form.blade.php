{{--action="{{url('student/save')}}"--}}
<form class="form-horizontal" method="post" action="">
    {{--任何指向 web 中 POST, PUT 或 DELETE 路由的 HTML表单--}}
    {{--请求都应该包含一个 CSRF 令牌(CSRF token)，否则，这个请求将会被拒绝。 --}}
    {{--例如：form标签下加  csrf_field() 就是解决此问题的. --}}
    {{--另外还可以在 Http\Middleware\VerifyCsrfToken.php 文件中设置$except eg：'/student/save', --}}

    {{ csrf_field() }}


    <div class="form-group">
        <label for="name" class="col-sm-2 control-label">姓名</label>
        <div class="col-sm-5">
            <input type="text" name="Student[name]"
                   value="{{old('Student')['name'] ? old('Student')['name'] : $student->name }}"
                   class="form-control" id="name" placeholder="请输入学生姓名">
        </div>
        <div class="col-sm-5">
            <p class="form-control-static text-danger">{{$errors->first('Student.name')}}</p>
        </div>
    </div>
    <div class="form-group">
        <label for="age" class="col-sm-2 control-label">年龄</label>
        <div class="col-sm-5">
            <input type="text" name="Student[age]"
                   value="{{old('Student')['age'] ? old('Student')['age'] : $student->age }}"
                   class="form-control" id="age" placeholder="请输入学生年龄">
        </div>
        <div class="col-sm-5">
            <p class="form-control-static text-danger">{{$errors->first('Student.age')}}</p>
        </div>
    </div>
    <div class="form-group">
        <label for="sex" class="col-sm-2 control-label">性别</label>
        <div class="col-sm-5">

            @foreach($student->sexDell() as $ind=>$val)

                <label class="radio-inline">
                    <input type="radio" name="Student[sex]"
                           {{isset($student->sex) && $student->sex==$ind ? 'checked' : ''}}
                           value="{{$ind}}">{{$val}}
                </label>

            @endforeach
        </div>
        <div class="col-sm-5">
            <p class="form-control-static text-danger">{{$errors->first('Student.sex')}}</p>
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-primary">提交</button>
        </div>
    </div>

</form>