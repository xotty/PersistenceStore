/**
 * 本例演示了SharedPreferences读写少量简单数据(数组和对象等复杂数据不支持，但可以将其序列化成String后读写)的方式：
 * 1）根据Context获取SharedPreferences对象,有三种方法：
 *    a）PreferenceManager.getDefaultSharedPreferences(context)
 *    b）getPreferences(Context.MODE_PRIVATE)，此时缺省Key（文件名）为当前类名
 *    c) getSharedPreferences("Key", Context.MODE_PRIVATE)
 * ---写
 * 2）利用edit()方法获取Editor对象
 * 3）通过Editor对象putXXX()方法存储key-value键值对数据
 * 4) 通过apply()或者commit()方法提交数据存储到设备
 *    a)apply(),先修改内存，异步提交修改硬盘数据，无成功失败信息返回
 *    b)commit(),同步修改内存和硬盘数据，成功返回true，失败返回false
 * ---读
 * 2）用SharedPreferences对象的getXXX()方法获取存储在SharedPreferences中的key-value键值对数据
 *
 * 5)可选：通过registerOnSharedPreferenceChangeListener可以监听其中的内容变化
 * <p>
 * <br/>Copyright (C), 2017-2018, Steve Chang
 * <br/>This program is protected by copyright laws.
 * <br/>Program Name:SharedPreferencesDEMO
 * <br/>Date:July，2017
 *
 * @author xottys@163.com
 * @version 1.0
 */

package org.xottys.SharedPrefernce;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText et = findViewById(R.id.et);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        /*另外两种获取SharedPreferences的方法：
        sp=getPreferences(MODE_PRIVATE);
        sp = getSharedPreferences("DemoData", Context.MODE_PRIVATE);*/
        findViewById(R.id.write).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et.getText().length() != 0) {
                    sp.edit().putString("DemoContent", et.getText().toString()).apply();
                    et.setText("");
                } else
                    Toast.makeText(MainActivity.this, "输入内容为空,上次保存的内容不变!", Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.read).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //通过Context获取SharedPreference实例
                et.setText(sp.getString("DemoContent", "DefaultContent"));
            }
        });
    }
}
