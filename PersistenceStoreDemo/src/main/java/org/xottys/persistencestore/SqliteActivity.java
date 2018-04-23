/**
 * 本例演示了SQLite数据库读写的三种方式：
 * 1）SQL语句读写    2）Android特定方法读写   3）GreenDao第三方库方式读写
 * <p>
 * <br/>Copyright (C), 2017-2018, Steve Chang
 * <br/>This program is protected by copyright laws.
 * <br/>Program Name:SqliteDEMO
 * <br/>Date:July，2017
 *
 * @author xottys@163.com
 * @version 1.0
 */
package org.xottys.persistencestore;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SqliteActivity extends Activity {
    private final String TAG = "SqliteDemo";
    private Button bt1, bt2, bt3;
    private TextView tv_age, tv_class;
    private ListView lv;

    private SQLiteDatabase db;
    private MyDatabaseHelper dbHelper;


    private BankAccountDao bankAccountDao;
    private List<BankAccount> accountList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);
        bt1 = (Button) findViewById(R.id.bt1);
        bt2 = (Button) findViewById(R.id.bt2);
        bt3 = (Button) findViewById(R.id.bt3);

        TextView tv_name = (TextView) findViewById(R.id.tv_name);
        tv_age = (TextView) findViewById(R.id.tv_age);
        tv_class = (TextView) findViewById(R.id.tv_class);

        bt1.setBackgroundColor(0xbd292f34);
        bt1.setTextColor(0xFFFFFFFF);
        bt2.setBackgroundColor(0xbd292f34);
        bt2.setTextColor(0xFFFFFFFF);
        bt3.setBackgroundColor(0xbd292f34);
        bt3.setTextColor(0xFFFFFFFF);
        lv = (ListView) findViewById(R.id.lv);

        tv_name.setText("姓名");

        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(SqliteActivity.this.getApplicationContext(), "myDBx", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        bankAccountDao = daoSession.getBankAccountDao();

        //SQL语句直接操作数据库
        bt1.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                tv_age.setText("年龄");
                tv_class.setText("工资");
                if (bt1.getText().equals("Start\n execSQL")) {
                    //创建或打开数据库（此处需要使用绝对路径）
                    db = SQLiteDatabase.openOrCreateDatabase(getFilesDir().toString() + "/myDB", null);
                    //创建表
                    db.execSQL("create table IF NOT EXISTS worker(_id integer"
                            + " primary key autoincrement,"
                            + " name text not null,"
                            + " age int not null,"
                            + " salary real)");
                    //用两种不同形式添加2条记录
                    db.execSQL("insert into worker values(null,?,?,? )", new Object[]{"张三", 45, 3100.5});
                    db.execSQL("insert into worker(name,age,salary) values('王五',30,2000.5)");
                    //用replace添加1条记录,该记录不会重复添加
                    db.execSQL("replace into worker(_id,name,age,salary) values(1,'姚七',240,1230.87)");
                    //查询获取数据库全部记录
                    Cursor cursor = db.rawQuery("select * from worker", null);
                    //在listview中显示查询结果
                    inflateListview(cursor, 1);

                    bt1.setText("Next\n Update");
                    Log.i(TAG, "数据库记录添加完成");

                    bt1.setBackgroundColor(0xFFD7D7D7);
                    bt1.setTextColor(0xbd292f34);
                    bt2.setTextColor(0xFFA0A0A0);
                    bt3.setTextColor(0xFFA0A0A0);
                    bt2.setBackgroundColor(0xbd292f34);
                    bt3.setBackgroundColor(0xbd292f34);
                    bt2.setEnabled(false);
                    bt3.setEnabled(false);
                } else if (bt1.getText().equals("Next\n Update")) {
                    //用两种不同形式修改2条记录
                    db.execSQL("update worker set name=?,age=?,salary=? where name=?", new Object[]{"李四", 50, 5000.6, "张三"});
                    db.execSQL("update worker set name='赵六', age=20,salary=3000.6 where name='王五'");

                    Cursor cursor = db.rawQuery("select * from worker", null);
                    inflateListview(cursor, 1);

                    bt1.setText("Next\n Delete");
                    Log.i(TAG, "数据库记录修改完成");
                } else if (bt1.getText().equals("Next\n Delete")) {

                    //用两种不同形式删除2条记录
                    db.execSQL("delete from worker  where name=?", new String[]{"李四"});
                    db.execSQL("delete from worker  where name='赵六'");

                    Cursor cursor = db.rawQuery("select * from worker", null);
                    inflateListview(cursor, 1);

                    bt1.setText("Start\n execSQL");
                    Log.i(TAG, "数据库记录删除完成");

                    //关闭SQLiteDatabase
                    if (db != null && db.isOpen()) {
                        db.close();
                        Log.i(TAG, "数据库关闭");
                    }
                    bt2.setTextColor(0xFFFFFFFF);
                    bt3.setTextColor(0xFFFFFFFF);
                    bt2.setEnabled(true);
                    bt3.setEnabled(true);
                }
            }
        });
        //android CRUD语句操作数据库
        bt2.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                tv_age.setText("年龄");
                tv_class.setText("班级");
                if (bt2.getText().equals("Start\n CRUD")) {
                    // 创建MyDatabaseHelper对象，指定数据库版本为1，此处使用相对路径即可，
                    // 数据库文件自动会保存在程序的数据文件夹的databases目录下。
                    dbHelper = new MyDatabaseHelper(SqliteActivity.this, "myDB", 2);
                    db = dbHelper.getReadableDatabase();

                    //添加1条记录
                    ContentValues values = new ContentValues();
                    values.put("name", "张三");
                    values.put("age", 16);
                    values.put("class", "六班");
                    db.insert("student", null, values);

                    //查询获取数据库全部记录
                    Cursor cursor = db.query(true, "student", null, null, null, null, null, null, null);
                    //在listview中显示查询结果
                    inflateListview(cursor, 2);

                    bt2.setText("Next\n Update");
                    Log.i(TAG, "数据库记录添加完成");

                    bt2.setBackgroundColor(0xFFD7D7D7);
                    bt2.setTextColor(0xbd292f34);

                    bt1.setTextColor(0xFFA0A0A0);
                    bt3.setTextColor(0xFFA0A0A0);
                    bt1.setBackgroundColor(0xbd292f34);
                    bt3.setBackgroundColor(0xbd292f34);
                    bt1.setEnabled(false);
                    bt3.setEnabled(false);
                } else if (bt2.getText().equals("Next\n Update"))

                {//修改姓名为张三的记录，姓名改为王五
                    ContentValues values = new ContentValues();
                    values.put("name", "王五");
                    db.update("student", values, "name=?", new String[]{"张三"});

                    Cursor cursor = db.query(true, "student", null, null, null, null, null, null, null);
                    inflateListview(cursor, 2);

                    bt2.setText("Next\n Delete");
                    Log.i(TAG, "数据库记录修改完成");


                } else if (bt2.getText().equals("Next\n Delete"))

                {  //删除姓名为王五的记录
                    db.delete("student", "name=?", new String[]{"王五"});

                    Cursor cursor = db.query(true, "student", null, null, null, null, null, null, null);
                    inflateListview(cursor, 2);

                    bt2.setText("Start\n CRUD");
                    Log.i(TAG, "数据库记录删除完成");

                    //关闭SQLiteOpenHelper
                    dbHelper.close();

                    bt1.setTextColor(0xFFFFFFFF);
                    bt3.setTextColor(0xFFFFFFFF);
                    bt1.setEnabled(true);
                    bt3.setEnabled(true);

                }
            }
        });
        //GreenDao操作数据库
        bt3.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                tv_age.setText("账号");
                tv_class.setText("账户余额");
                if (bt3.getText().equals("Start\n GreenDao")) {
                    //添加1条记录
                    BankAccount account = new BankAccount(1L, "赵六", "BJ2467", 4500);
                    bankAccountDao.save(account);
                    //换种方法再添加1条记录
                    account = new BankAccount(null, "张三", "SZ16123", 3000);
                    bankAccountDao.insert(account);

                    //用raw方法查询数据库全部记录
                    Cursor cursor = bankAccountDao.getDatabase().rawQuery("select * from BANK_ACCOUNT", null);
                    inflateListview(cursor, 3);
                    bt3.setText("Next\n Update");
                    Log.i(TAG, "数据库记录添加完成");

                    bt3.setBackgroundColor(0xFFD7D7D7);
                    bt3.setTextColor(0xbd292f34);
                    bt1.setTextColor(0xFFA0A0A0);
                    bt2.setTextColor(0xFFA0A0A0);
                    bt1.setBackgroundColor(0xbd292f34);
                    bt2.setBackgroundColor(0xbd292f34);
                    bt1.setBackgroundColor(0xbd292f34);
                    bt1.setEnabled(false);
                    bt2.setEnabled(false);
                } else if (bt3.getText().equals("Next\n Update")) {
                    //查找要修改的记录
                    BankAccount findAccount = bankAccountDao.queryBuilder().where(BankAccountDao.Properties.Name.eq("张三")).limit(1).build().unique();
                    if (findAccount != null) {
                        //修改找到的记录，姓名改为李四，每次只能改1条记录
                        findAccount.setName("李四");
                        findAccount.setBalance(1000);
                        bankAccountDao.update(findAccount);
                        Log.i(TAG, "数据库记录修改成功");
                    } else {
                        Log.i(TAG, "数据库记录修改失败，没有找到要修改的记录");
                    }

                    //用load方法获取数据库全部记录
                    accountList = bankAccountDao.loadAll();
                    inflateListview(null, 4);

                    bt3.setText("Next\n Delete");
                } else if (bt3.getText().equals("Next\n Delete")) {
                    //用DeleteQuery删除记录，可以批量删除满足特定条件的记录
                    bankAccountDao.queryBuilder().where(BankAccountDao.Properties.Name.eq("李四")).buildDelete().
                            executeDeleteWithoutDetachingEntities();
                   /*另一种记录删除方法
                   accountList = bankAccountDao.queryBuilder().where(BankAccountDao.Properties.Name.eq("王五")).build().list();
                   for(BankAccount acct:accountList) {
                       //删除数据库记录，每次只能删1条记录
                        bankAccountDao.delete(acct);
                    }*/

                    //查询数据库中满足条件的前30条记录，并排序，查询结果用Cursor保存
                    Cursor cursor = bankAccountDao.queryBuilder()
                            .where(BankAccountDao.Properties.Id.notEq(999))
                            .orderAsc(BankAccountDao.Properties.Id)
                            .limit(30)
                            .buildCursor().query();
                    inflateListview(cursor, 3);

                    bt3.setText("Start\n GreenDao");
                    Log.i(TAG, "数据库记录删除完成");

                    bt1.setTextColor(0xFFFFFFFF);
                    bt2.setTextColor(0xFFFFFFFF);
                    bt1.setEnabled(true);
                    bt2.setEnabled(true);
                }

            }
        });
    }

    private void inflateListview(Cursor cursor, int kind) {

        ListAdapter adapter = null;
        switch (kind) {
            case 1:
                adapter = new SimpleCursorAdapter(
                        this,
                        R.layout.line_items, cursor,
                        new String[]{"name", "age", "salary"}
                        , new int[]{R.id.name, R.id.age, R.id.salary},
                        CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
                break;
            case 2:
                adapter = new SimpleCursorAdapter(
                        this,
                        R.layout.line_items, cursor,
                        new String[]{"name", "age", "class"}
                        , new int[]{R.id.name, R.id.age, R.id.salary},
                        CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
                break;
            case 3:
                adapter = new SimpleCursorAdapter(
                        this,
                        R.layout.line_items, cursor,
                        new String[]{"NAME", "ACCOUNT_NUMBER", "BALANCE"}
                        , new int[]{R.id.name, R.id.age, R.id.salary},
                        CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
                break;
            case 4:

                List<Map<String, Object>> listems = new ArrayList<>();
                for (BankAccount acct : accountList) {
                    Map<String, Object> listem = new HashMap<>();
                    listem.put("name", acct.getName());
                    listem.put("accountNumber", acct.getAccountNumber());
                    listem.put("balance", acct.getBalance());
                    listems.add(listem);
                }
                adapter = new SimpleAdapter(SqliteActivity.this, listems,
                        R.layout.line_items, new String[]{"name", "accountNumber", "balance"},
                        new int[]{R.id.name, R.id.age, R.id.salary});
                // lv.setAdapter(adapter);
                break;
        }
        // 显示数据
        lv.setAdapter(adapter);
    }
}
