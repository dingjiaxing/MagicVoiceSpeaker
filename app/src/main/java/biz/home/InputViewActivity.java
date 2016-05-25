package biz.home;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import biz.home.R;
import biz.home.fragment.MainEditFragment;


/**
 * Created by lmw on 2015/7/13.
 */
public class InputViewActivity extends Activity {
    /**
     * 是否弹出键盘让用户来输入信息，此标志为true时 输入框默认为空，否则为用来原本打算修改的内容
     */
    public static boolean popKeyboard=false;
    public final static int REQUEST_CODE = 1001;
    public final static String TOP_INFO="bakc_info";
    public final static String BOTTOM_INFO="input_info";
    private TextView backInfo;
    private EditText inputInfo;
    private Intent intent;
    private  String TAG="InputViewActivity";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.input_view);
        Intent intent=getIntent();
        //获取需要被修改的内容
        String ts=intent.getStringExtra("tst");
        this.inputInfo = (EditText) findViewById(R.id.input_info);
        if(popKeyboard){
            inputInfo.setText("");
            popKeyboard=false;
        }else {
            inputInfo.setText(ts);
        }

        this.backInfo = (TextView) findViewById(R.id.back_info);
        intent=getIntent();
        if(intent.hasExtra(TOP_INFO)) {
            //获取神奇秘书说的话
            this.backInfo.setText(intent.getStringExtra(TOP_INFO));

        }else{
            backInfo.setText("");
        }
        inputInfo.requestFocus();
        //this.inputInfo.setOnClickListener(new inputOnClickListener());
        this.backInfo.setOnClickListener(new backOnClickListener());
        this.inputInfo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    String text=inputInfo.getText().toString().trim();
                    Log.i(TAG, "onKey: inputInfo.getText():"+text);
                    //键盘中点击了回车键
                    if(text.length()==0)
                    {
                        Log.i(TAG, "onKey: 输入为空");
                        InputViewActivity.this.finish();
                        return true;
                    }else{
                        Log.i(TAG, "onKey: 有输入值："+text+"退出inputActivity，返回主activity");
                        Intent data=new Intent();
                        data.putExtra(BOTTOM_INFO,text);
                        setResult(SpeakToitActivity.RESULT_INPUT_DATA, data);
                        finish();
                        return true;
                    }
                }else if(event.getKeyCode()==KeyEvent.KEYCODE_BACK){
                    //用户点击了返回
                    Log.i(TAG, "onKey: 用户点击了返回");
                    InputViewActivity.this.finish();
                    return true;
                }else {
                    return false;
                }

            }
        });

    }
//
//    private class bkLayoutOnClickListener implements View.OnClickListener{
//        @Override
//        public void onClick(View v) {
//            InputViewActivity.this.finish();
//        }
//    }

    //对输入的点击的监听
    private class inputOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
        
    }

    //返回 按钮的单击的监听
    private class backOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            //得到InputMethodManager的实例
            if (imm.isActive()) {
                //如果开启
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
                //关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
            }
            InputViewActivity.this.finish();
        }
    }

}
