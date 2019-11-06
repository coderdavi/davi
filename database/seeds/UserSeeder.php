<?php

use Illuminate\Database\Seeder;
use \App\User;
class UserSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        factory(User::class,100)->create();
        $user = User::find(2);
        $user->name='datu';
        $user->email='3345121@qq.com';
        $user->password=bcrypt('abc123');
        $user->save();
    }
}
