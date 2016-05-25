package biz.home.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.graphics.Paint;
import android.location.GpsStatus;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import biz.home.R;
import biz.home.SpeakToitActivity;

import static biz.home.R.layout.send_message;

/**
 * Created by admin on 2015/8/20.
 */
public class MainMessageFragment extends Fragment{
    private OnMessageCancel listener;
    private OnMessageSend listener_message_send;
    private OnClickContactName listener_click_contactName;
    public TextView message_name;
    public EditText message_content;
    public static String name;
    public static String content;
    public static String tel;
    public  Button send,cancel;
    public MainMessageFragment() {
        super();
    }
    public static MainMessageFragment newInstance(String name1,String tel1,String content1){
        name=name1;
        content=content1;
        tel=tel1;
        MainMessageFragment fragment=new MainMessageFragment();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.send_message, container, false);
        findById(view);
        init();
        setOnClick();
        System.out.println("MainMessageFragment.onCreateView");

        return view;
    }
    public void init(){
        message_name.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        message_name.setText(name);
        message_content.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        message_content.setText(content);

    }
    //自定义接口，目的是用户点取消的时候传值给Activity让她读“我没有发送”
    public interface  OnMessageCancel{
        public void onMessageCancelClick(String s);
    }
    public interface OnMessageSend{
        public void onMessageSendClick(String s);
    }
    public interface OnClickContactName{
        public void onClickContactName(String messageContent);
    }
    public void setOnClick(){
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = message_content.getText().toString();
//                SendSMS(tel, content);
                sendSMS2(tel, content);
                if (listener_message_send != null) {
                    listener_message_send.onMessageSendClick("短信已发送");
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onMessageCancelClick("我没有发送短信");
                }
            }
        });
        message_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity().getApplicationContext(),"点击了姓名",Toast.LENGTH_SHORT).show();
                if(listener_click_contactName!=null){
                    listener_click_contactName.onClickContactName(message_content.getText().toString());
                }
            }
        });
    }



    public  void findById(View view){
        message_name=(TextView)view.findViewById(R.id.message_name);
        message_content=(EditText)view.findViewById(R.id.message_content);
        send=(Button)view.findViewById(R.id.message_send);
        cancel=(Button)view.findViewById(R.id.message_cancel);

        message_content.requestFocus();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof  OnMessageCancel){
            listener=(OnMessageCancel)activity;

        }else{
            try {
                throw new Exception("activityy should implements OnButtonClick Interface");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(activity instanceof  OnMessageSend){
            listener_message_send=(OnMessageSend)activity;

        }else{
            try {
                throw new Exception("activityy should implements OnButtonClick Interface");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(activity instanceof  OnClickContactName){
            listener_click_contactName=(OnClickContactName)activity;

        }else{
            try {
                throw new Exception("activityy should implements OnButtonClick Interface");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    //显示出来手机自带的短信发送界面
    public void SendSMS(String phoneNumber,String message){
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
        intent.putExtra("sms_body", message);
        startActivity(intent);
    }
    //调用系统服务直接发送短信
    public void sendSMS2(String phoneNumber, String message) {
        // 获取短信管理器
        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
        // 拆分短信内容（手机短信长度限制）
        List<String> divideContents = smsManager.divideMessage(message);
        for (String text : divideContents) {
            try{
                smsManager.sendTextMessage(phoneNumber, null, text, null, null);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }


}
