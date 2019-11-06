<?php
namespace App;
use Illuminate\Database\Eloquent\Model;

class Student extends Model
{
    const SEX_UN='0';//未知
    const SEX_BOY='1';//男
    const SEX_GIRL='2';//女

//    表名
    protected $table='student';

    //     指定允许批量赋值的字段
    protected $fillable=['name','age','sex'];

    //  指定不允许批量赋值的字段
    protected $guarded=[];

//    自动维护时间戳
    public $timestamps=true;

//自动维护时间戳时，获取的时间戳
    protected function getDateFormat()
    {
        return time();
    }

//    获取的时间数据不进行格式化
    protected  function asDateTime($val)
    {
        return $val;
    }

    public function sexDell($ind=null)
    {
        $arr=[
            self::SEX_UN=>'未知',
            self::SEX_BOY=>'男',
            self::SEX_GIRL=>'女',
        ];
        if($ind!==null)
        {
            return array_key_exists($ind,$arr) ? $arr[$ind] : $arr[self::SEX_UN];
        }
        return $arr;
    }

}



