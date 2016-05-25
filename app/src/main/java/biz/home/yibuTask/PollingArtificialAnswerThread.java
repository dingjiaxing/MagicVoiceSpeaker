package biz.home.yibuTask;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.List;

import biz.home.SpeakToitActivity;
import biz.home.bean.ArtificialAnswer;
import biz.home.bean.MagicInfo;
import biz.home.model.ContactInfo;
import biz.home.model.MagicInfoApiEnum;
import biz.home.util.HttpHelp;

/**
 * Created by admin on 2015/11/3.
 */
public class PollingArtificialAnswerThread extends Thread {
    private static final String TAG = "PollingArtificialAnswerThread";
    private int count=0;
    private Handler handler;

    public PollingArtificialAnswerThread(Handler handler) {
        this.handler=handler;
    }

    @Override
    public void run() {
        super.run();
        while (true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                if(SpeakToitActivity.MIROPHONE_STATE==0){
                    List<ArtificialAnswer> list= SpeakToitActivity.artificialAnswerList;

                    count=list.size();
                    if(count>0 && SpeakToitActivity.isForeground){
                        ArtificialAnswer aa=list.get(0);
                        SpeakToitActivity.artificialAnswerList.remove(0);
                        Message msg=new Message();
                        Bundle bundle=new Bundle();
                        bundle.putString("type",aa.getType());
                        bundle.putString("question",aa.getQuestion());
                        bundle.putString("answer",aa.getAnswer());
                        bundle.putString("url",aa.getUrl());
                        msg.setData(bundle);
                        msg.what=1;
                        handler.sendMessage(msg);
                        //将已阅读的消息id发送给服务器
                        MagicInfo mi=new MagicInfo();
                        mi.setApi(MagicInfoApiEnum.READMESSAGE);
                        mi.setMessageId(aa.getMessageId());
                        Log.i(TAG, "run messageId:" + aa.getMessageId());
                        HttpHelp.send(mi);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
