package biz.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import biz.home.model.MagicUserInfo;
import biz.home.model.UserInfo;
import biz.home.util.DatabaseUtil;
import biz.home.util.MagicUserInfoDao;
import biz.home.yibuTask.UpdateUserInfoTask;

import org.litepal.crud.DataSupport;

/**
 * 个人信息设置
 * Created by lmw on 2015/7/27.
 */
public class SettingInformationsActivity extends Activity implements View.OnClickListener {
    private ImageView back;                         //返回
    private TextView title, edit;    //文本
    private EditText telephone,userName,nickName,realName,company,position,email;        //可编辑框：电话，姓名，公司
    private Button confirm, cancel;             //按钮
    /**
     * 主活动的请求码
     */
    public static final Integer REQUEST_MAIN_ACTIVITY = 1601;
    /**
     * 设置活动的请求码
     */
    public static final Integer REQUEST_SET_ACTIVITY = 1602;
    private String keyTel = "15088888888";
    private MagicUserInfoDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_information);
        initView();         //获取控件，设置监听
        initData();         //初始化数据
    }

    private void initView() {
        dao=new MagicUserInfoDao(getApplicationContext());
        back = (ImageView) findViewById(R.id.setting_information_back_img);
        title = (TextView) findViewById(R.id.setting_information_title);
        edit = (TextView) findViewById(R.id.setting_information_edit);
        telephone = (EditText) findViewById(R.id.setting_information_tel);
        userName = (EditText) findViewById(R.id.setting_information_userName);
        nickName = (EditText) findViewById(R.id.setting_information_nickName);
        realName = (EditText) findViewById(R.id.setting_information_realName);
        company = (EditText) findViewById(R.id.setting_information_company);
        position = (EditText) findViewById(R.id.setting_information_position);
        email = (EditText) findViewById(R.id.setting_information_email);
//        confirm = (Button) findViewById(R.id.setting_information_confirm);
//        cancel = (Button) findViewById(R.id.setting_information_cancel);
        edit.setOnClickListener(this);
        back.setOnClickListener(this);
//        confirm.setOnClickListener(this);
//        cancel.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        MagicUserInfo user=dao.findUser();
        if(user!=null){
            setInfo(user);
        }else{
            new AlertDialog.Builder(this)
                    .setMessage("需要使用该功能，请先登录")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            Intent intent = new Intent(SettingInformationsActivity.this, LoginActivity.class);
//                            startActivity(intent);
                            finish();
                        }
                    })
                    .show();
        }
        setEditable(false);
//        Intent intent = getIntent();
//        title.setText(intent.getStringExtra("title"));
//        createInfo();
//        if (keyTel.equals("") || keyTel.equals(null)) {
//            new AlertDialog.Builder(this)
//                    .setMessage("需要使用该功能，请先登录")
//                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Intent intent = new Intent(SettingInformationsActivity.this, LoginActivity.class);
//                            startActivity(intent);
//                        }
//                    })
//                    .setNegativeButton("取消", null)
//                    .show();
//        }
//        setInfo(DatabaseUtil.getUserInfoByTel(keyTel));
//        setEditable(false);
    }



    /**
     * 改变编辑标签
     */
    private void changeFlag() {
        if (edit.getText().equals("编辑"))
            setEditable(true);
        else
            setEditable(false);
    }

    /**
     * 初始化可编辑文本框的数据
     */
    private void setInfo(MagicUserInfo user) {
        telephone.setText(user.getTelephone());
        userName.setText(user.getRealName());
        nickName.setText(user.getNickName());
        realName.setText(user.getRealName());
        company.setText(user.getCompanyName());
        position.setText(user.getPosition());
        email.setText(user.getEmail());
    }

    /**
     * 更新可编辑文本框的数据
     */
    private void changeInfo() {
        MagicUserInfo user=new MagicUserInfo();
        user.setTelephone(telephone.getText().toString());
        user.setUserName(userName.getText().toString());
        user.setNickName(nickName.getText().toString());
        user.setRealName(realName.getText().toString());
        user.setCompanyName(company.getText().toString());
        user.setPosition(position.getText().toString());
        user.setEmail(email.getText().toString());
        UpdateUserInfoTask task=new UpdateUserInfoTask(this,user,dao);
        task.execute(1000);
    }

    /**
     * 批量设置可编辑文本框的编辑状态
     *
     * @param editable
     */
    private void setEditable(boolean editable) {
        if (editable) {
            unLock(userName);
            unLock(nickName);
            unLock(realName);
            unLock(company);
            unLock(position);
            unLock(email);
            edit.setText("保存");
            edit.setClickable(true);
//            confirm.setVisibility(View.VISIBLE);
//            cancel.setVisibility(View.VISIBLE);
            userName.requestFocusFromTouch();
        } else {
            lock(userName);
            lock(nickName);
            lock(realName);
            lock(company);
            lock(position);
            lock(email);
            edit.setText("编辑");
            edit.setClickable(true);
//            confirm.setVisibility(View.GONE);
//            cancel.setVisibility(View.GONE);
        }
    }

    /**
     * 设置可编辑文板框可编辑状态
     *
     * @param et
     */
    private void unLock(EditText et) {
//        et.setBackgroundResource(R.drawable.input_text);
        et.setBackground(null);
        et.setEnabled(true);
        et.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                return null;
            }
        }});
    }

    /**
     * 设置可编辑文板框不可编辑状态
     *
     * @param et
     */
    private void lock(EditText et) {
        et.setBackground(null);
        et.setEnabled(false);
        et.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                return source.length() < 1 ? dest.subSequence(dstart, dend) : "";
            }
        }});
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_information_back_img:
                finish();
                break;
            case R.id.setting_information_edit:
                if(edit.getText().toString().equals("编辑")){
                    changeFlag();
                    realName.requestFocus();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    boolean isOpen=imm.isActive();//isOpen若返回true，则表示输入法打开
                    if(isOpen){
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }

                }else {
                    new AlertDialog.Builder(this)
                            .setMessage("您确定要修改您的个人信息吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    changeInfo();
                                    changeFlag();
//                                    Toast.makeText(SettingInformationsActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                }

                break;
//            case R.id.setting_information_cancel:
//                setInfo(dao.findUser());
//                changeFlag();
//                break;
            default:
                break;
        }
    }
}
