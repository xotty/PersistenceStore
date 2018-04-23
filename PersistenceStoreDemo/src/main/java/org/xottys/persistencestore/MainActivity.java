package org.xottys.persistencestore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyOnClicklistener myOnClicklistener=new MyOnClicklistener();
        findViewById(R.id.bt1).setOnClickListener(myOnClicklistener);
        findViewById(R.id.bt2).setOnClickListener(myOnClicklistener);
        findViewById(R.id.bt3).setOnClickListener(myOnClicklistener);
    }


    class MyOnClicklistener implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt1:
                    intent=new Intent(MainActivity.this,SharedPreferencesActivity.class);

                    break;
                case R.id.bt2:
                    intent=new Intent(MainActivity.this,FileActivity.class);

                    break;
                case R.id.bt3:
                    intent=new Intent(MainActivity.this,SqliteActivity.class);

                    break;
                default:
                    break;
            }
            startActivity(intent);
        }
    }
}
