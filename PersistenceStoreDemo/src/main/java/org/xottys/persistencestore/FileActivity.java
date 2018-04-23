/**
 * 本例演示了File的读写，主要使用java io流的相关工具
 * 1）FileOutputStream／FileInputStream：以字节为单位读写文件，常用于读写二进制文件，如图片、声音、影像等文件
 * 2）OutputStreamWriter／InputStreamReader：以字符为单位读写文件，常用于读写文本，数字等类型的文件
 * 3）FileWriter／FileReader：以行为单位读写文件，常用于读面向行的文本文件
 * 4）RandomAccessFile.read/Write:随机读写字节文件
 * 5）PrintWriter／PrintStream：格式化写文件
 * 6）Scanner：读文件
 * 另外读写文件时也常用Bufferedxxx装饰流进行缓存封装，以提高效率
 * <p>
 * <br/>Copyright (C), 2017-2018, Steve Chang
 * <br/>This program is protected by copyright laws.
 * <br/>Program Name:SharedPreferencesDEMO
 * <br/>Date:July，2017
 *
 * @author xottys@163.com
 * @version 1.0
 */
package org.xottys.persistencestore;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Scanner;

import static android.content.ContentValues.TAG;

public class FileActivity extends Activity {
    final String FILE_NAME = "xottys.txt";
    private EditText edit1, edit2;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        edit1 = (EditText) findViewById(R.id.edit1);
        edit2 = (EditText) findViewById(R.id.edit2);

        //使edit2不可编辑
        edit2.setKeyListener(null);
    }
    //以字节方式写文件
    public void btnClick_FileOutputStream(View v) {
        try {
            dosth();
            //构造字节输出流文件
            FileOutputStream fos = new FileOutputStream(getFilesDir() + "/" + FILE_NAME);
            /*android Context还提供了一个简化构造器
             FileOutputStream fos = openFileOutput(FILE_NAME,MODE_PRIVATE);

             也可以将FileOutputStream包装成PrintStream,遇到\r\n会自动flush
             PrintStream ps = new PrintStream(fos, true);*/

            //写文件内容
            fos.write(content.getBytes("utf-8"));
            fos.flush();
            /*ps.print(content);*/

            Log.i(TAG, "write: " + content);

            // 关闭文件输出流
            fos.close();
            /* ps.close();*/

            edit1.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //以字符方式写文件
    public void btnClick_OutputStreamWriter(View v) {
        try {
            dosth();
            //用指定字符集构造输出流
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(
                    getFilesDir() + "/" + FILE_NAME), "utf-8");

            //写文件内容
            osw.write(content);
            osw.flush();

            Log.i(TAG, "OutputStreamWriter: " + content);

            // 关闭文件输出流
            osw.close();

            edit1.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //以字符方式写文件
    public void btnClick_FileWriter(View v) {
        try {
            dosth();
            //使用缺省字符集构造输出流，它是OutputStreamWriter的子类
            FileWriter fw = new FileWriter(getFilesDir() + "/" + FILE_NAME, false);
            fw.write(content);
            fw.flush();
            Log.i(TAG, "write: " + content);

            // 关闭文件输出流
            fw.close();

            edit1.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //字节方式随机写文件
    public void btnClick_RandomAccessFileWrite(View v) {
        dosth();
        try {
            File file = new File(getFilesDir(), FILE_NAME);
            // 以指定文件创建 RandomAccessFile对象
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            /*此时也可以用nio中的FileChannel来进一步操作，FileChannel
              可以由FileInputStream，FileOutputStream，RandomAccessFile三个类来产生
               FileChannel randomChannel = raf.getChannel();*/

            // 将文件记录指针移动到最后,即为向原文件追加内容
            raf.seek(file.length());
            /*randomChannel.position(file.length());*/

            // 输出文件内容
            raf.write(content.getBytes("utf-8"));
            /*ByteBuffer buff= ByteBuffer.wrap(content.getBytes("UTF-8"));
              randomChannel.write(buff);*/

            // 关闭RandomAccessFile
            raf.close();
            /*randomChannel.close();*/

            edit1.setText("");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //字符方式格式化写文件
    public void btnClick_PrintWriter(View v) {
        try {
            dosth();
            File file = new File(getFilesDir(), FILE_NAME);
            //用指定字符集构造文件输出流
            PrintWriter pw = new PrintWriter(file, "utf-8");

            //写文件内容,遇到prinftln会自动flush
            pw.println(content);

            // 关闭文件输出流
            pw.close();
            edit1.setText("");
            Log.i(TAG, "PrintWriter: " + content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //以字节为单位读取文件内容，一次读一个或多个字节
    public void btnClick_FileInputStream(View v) {
        try {
            // 打开文件输入流
            FileInputStream fis = new FileInputStream(getFilesDir() + "/" + FILE_NAME);
            //android Context还提供了一个简化构造器
            //FileInputStream fis = openFileInput(FILE_NAME);
            byte[] buff = new byte[30];
            int hasRead = 0;
            StringBuilder sb = new StringBuilder("");
            //一次读取30个字节
            while ((hasRead = fis.read(buff)) > 0) {
                //将字节数组转换成字符串
                sb.append(new String(buff, 0, hasRead));
            }
            //关闭文件输入流
            fis.close();

            edit2.setText(sb.toString());
            Log.i(TAG, "FileInputStream: " + sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //以字符为单位读取文件内容，一次读多个字节
    public void btnClick_InputStreamReader(View v) {
        StringBuilder sb;

        //一次读30个字符
        char[] tempchars = new char[30];
        int charread = 0;
        InputStreamReader reader = null;
        sb = new StringBuilder("");
        try {

            reader = new InputStreamReader(new FileInputStream(getFilesDir() + "/" + FILE_NAME), "utf-8");

            //读入多个字符到字符数组中，charread为一次读取字符数
            while ((charread = reader.read(tempchars)) != -1) {
                sb.append(new String(tempchars, 0, charread));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }

        edit2.setText(sb.toString());
        Log.i(TAG, "InputStreamReader: " + sb.toString());
    }

    //以字符方式读文件，用BufferedReader封装后可以以行为单位读取
    public void btnClick_FileReader(View v) {
        try {
            FileReader fr = new FileReader(getFilesDir() + "/" + FILE_NAME);
            BufferedReader br = new BufferedReader(fr,512);
            String tempString = null;
            StringBuilder sb = new StringBuilder("");
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = br.readLine()) != null) {
                sb.append(tempString+"\n");
            }
            br.close();
            /* 如果不用 BufferedReader封装，读取方法与InputStreamReader完全一样，如下
           char[] chars = new char[512];
            int hasRead = 0;
            StringBuilder sb = new StringBuilder("");
            //一次读取512字符
            while ((hasRead = fr.read(chars)) > 0) {
                sb.append(new String(chars, 0, hasRead));
            }
            // 关闭文件输入流
            fr.close();
            */
            edit2.setText(sb.toString());
            Log.i(TAG, "FileReader: " + sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //随机读文件，可以从任意指定位置读取
    public void btnClick_RandomAccessFileRead(View v) {
        dosth();
        try (
                RandomAccessFile raf = new RandomAccessFile(
                        getFilesDir() + "/" + FILE_NAME, "r")) {

            // 移动raf的文件记录指针的位置，从第4个字节开始读入
            raf.seek(3);
            byte[] buff = new byte[1024];
            // 用于保存实际读取的字节数
            int hasRead = 0;

            StringBuilder sb = new StringBuilder("");
            // 使用循环来重复读取过程
            while ((hasRead = raf.read(buff)) > 0) {
                // 取出一个个字节，将字节数组拼接成字符串
                sb.append(new String(buff, 0, hasRead));
            }
            raf.close();
            edit2.setText(sb.toString());
            Log.i(TAG, "RandomAccessFileRead: " + sb.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //以指定字符集装入文件后读取，按照分界符读取字符文件
    public void btnClick_Scanner(View v) {

        try {
            //将文件直接装入Scanner扫描读取
            File file = new File(getFilesDir(), FILE_NAME);
            Scanner sc = new Scanner(file, "utf-8");

            //也可以用另外一种方法：将标准输入重定向到fis输入流
            //FileInputStream fis = new FileInputStream("RedirectIn.java"))
            //System.setIn(fis);
            //Scanner sc = new Scanner(System.in)

            sc.useDelimiter("\n");
            StringBuilder sb = new StringBuilder("");
            //读取文件内容
            while (sc.hasNext()) {
                sb.append(sc.next() + "\n");
            }
            // 关闭文件输入流
            sc.close();

            edit2.setText(sb.toString());
            Log.i(TAG, "Scanner: " + sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void dosth() {
        //关闭软键盘
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        /*关闭软键盘
        im.hideSoftInputFromWindow(getWindow().peekDecorView().getWindowToken(), 0);
         */
        edit1.clearFocus();
        content = edit1.getText().toString();
    }
}
