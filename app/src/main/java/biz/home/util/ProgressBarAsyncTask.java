package biz.home.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AmrInputStream;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.speechsynthesizer.SpeechSynthesizer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import biz.home.InputViewActivity;
import biz.home.R;
import biz.home.SpeakToitActivity;
import biz.home.adapter.FragmentAdapter;
import biz.home.api.MapLocation;
import biz.home.api.jpush.MagicInfoJPushDevice;
import biz.home.application.myUtils.Pcm2Wav;
import biz.home.bean.AlarmInfo;
import biz.home.bean.MagicInfo;
import biz.home.bean.MagicMapLocation;
import biz.home.bean.MagicResourceInfo;
import biz.home.bean.MagicResult;
import biz.home.bean.MagicResultResourceInfo;
import biz.home.fragment.MainAlarmsFragment;
import biz.home.fragment.MainEditFragment;
import biz.home.fragment.MainWebFragment;
import biz.home.model.MagicInfoApiEnum;
import biz.home.model.MagicResultEnum;
import biz.home.model.MagicResultFunctionalEnum;
import biz.home.model.MagicResultUserInfoStatusEnum;
import biz.home.model.MagicUserInfo;
import biz.home.model.VoiceFileRecord;
import biz.home.yibuTask.CallTask;
import biz.home.yibuTask.MagicOperationMyApp;
import biz.home.yibuTask.MessageTask;
import biz.home.yibuTask.OpenResourceInfoTask;
import biz.home.yibuTask.QuestionTask;
import biz.home.yibuTask.ScheduleTask;
import cn.jpush.android.api.JPushInterface;

/**
 * 生成该类的对象，并调用execute方法之后
 * 首先执行的是onProExecute方法
 * 其次执行doInBackgroup方法
 *
 */
public class ProgressBarAsyncTask extends AsyncTask<Integer,Integer,String>  {
    private String uid="";
    private String messageId;  //消息id
    public static Boolean isUploadVoice=false;
//    private String waveFilePath="sdcard/outfile.wav";
    private String waveFilePath;    //将pcm声音文件转化为.wav文件的wav文件地址
    private String amrFilePath;     //将wav文件转化为.amr文件的amr文件地址
    Handler projectHandler;
    private String httpResultString="";
    Pattern p=Pattern.compile(".{0,3}查看.{0,3}日程.{0,3}|.{0,3}查看日程.{0,3}|.{0,3}打开日程.{0,3}|.{0,3}天的日程.{0,3}|.{0,3}天.{0,3}有.{0,3}要做的事|.{0,6}事没做|.{0,6}没做的事|.{0,6}待办事项|.{0,6}未完成的工作");
    Pattern p2=Pattern.compile(".{0,3}修改日程.{0,3}|.{0,3}修改.{0,3}日程.{0,3}");
    private int alarms;
    private AlarmInfoDao alarmInfoDao;
    private AlarmInfo[] alarmInfos;

    private static final String TAG ="ProgressBarAsyncTask";
    DrawerLayout drawer;
    private MainEditFragment mainEditFragment;
    private FragmentAdapter mFragmentAdapter;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private ViewPager mViewPager;
    private MagicResultResourceInfo resourceInfo;
    //    private TextView textView;
//    private ProgressBar progressBar;
    private static String content, endResult;
    private final int MaxFragmentSize = 10;
    public Context context;
    //百度语音合成相关参数
    protected static final int UI_LOG_TO_VIEW = 0;
    protected static final int UI_TOAST = 1;
    public SpeechSynthesizer speechSynthesizer;
    private TextView logView;
    private EditText inputTextView;
    private Button startButton;
    private Handler uiHandler;
    private Toast mToast;
    public static MagicResult magicResult;
    MainWebFragment mainWebFragment;

    //mainWebFragment相关的参数
    public static final String URL = "MainWebFragment_url";
    public static final String TEXT_SHOW = "MainWebFragment_Text";
    private static final String IS_GET_Data = "isGetData";
    //获取手机联系人用到的
    private String[] columns = { ContactsContract.Contacts._ID,// ���IDֵ
            ContactsContract.Contacts.DISPLAY_NAME,// �������
            ContactsContract.CommonDataKinds.Phone.NUMBER,// ��õ绰
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID, };
    ContactInfoDao dao;
    MagicUserInfoDao dao2;

    MagicResultUserInfoStatusEnum userInfoStatus;
    ViewGroup mContainer;


    /**
     * 这里的Integer参数对应AsyncTask中的第一个参数
     * 这里的String返回值对应AsyncTask的第三个参数
     * 该方法并不运行在UI线程当中，主要用于异步操作，所有在该方法中不能对UI当中的空间进行设置和修改
     * 但是可以调用publishProgress方法触发onProgressUpdate对UI进行操作
     */


    public ProgressBarAsyncTask(Handler projectHandler,DrawerLayout drawer,ViewGroup mContainer,Context context,MainWebFragment mainWebFragment,SpeechSynthesizer speechSynthesizer,String endResult, MainEditFragment mainEditFragment, FragmentAdapter mFragmentAdapter, List<Fragment> mFragmentList, ViewPager mViewPager) {
        super();
        this.projectHandler=projectHandler;
        this.drawer=drawer;
        this.mContainer=mContainer;
        this.endResult = endResult;
        this.mainEditFragment = mainEditFragment;
        this.mFragmentAdapter = mFragmentAdapter;
        this.mFragmentList = mFragmentList;
        this.mViewPager = mViewPager;
        this.speechSynthesizer=speechSynthesizer;
        this.mainWebFragment=mainWebFragment;
        this.context=context;
    }
    public ProgressBarAsyncTask(MagicResultResourceInfo resourceInfo,ViewGroup mContainer,Context context,MainWebFragment mainWebFragment,SpeechSynthesizer speechSynthesizer,String endResult, MainEditFragment mainEditFragment, FragmentAdapter mFragmentAdapter, List<Fragment> mFragmentList, ViewPager mViewPager) {
        super();
        this.resourceInfo=resourceInfo;
        this.mContainer=mContainer;
        this.endResult = endResult;
        this.mainEditFragment = mainEditFragment;
        this.mFragmentAdapter = mFragmentAdapter;
        this.mFragmentList = mFragmentList;
        this.mViewPager = mViewPager;
        this.speechSynthesizer=speechSynthesizer;
        this.mainWebFragment=mainWebFragment;
        this.context=context;
    }
    public ProgressBarAsyncTask(Handler projectHandler,MagicResultResourceInfo resourceInfo,ViewGroup mContainer,Context context,MainWebFragment mainWebFragment,SpeechSynthesizer speechSynthesizer,String endResult, MainEditFragment mainEditFragment, FragmentAdapter mFragmentAdapter, List<Fragment> mFragmentList, ViewPager mViewPager) {
        super();
        this.projectHandler=projectHandler;
        this.resourceInfo=resourceInfo;
        this.mContainer=mContainer;
        this.endResult = endResult;
        this.mainEditFragment = mainEditFragment;
        this.mFragmentAdapter = mFragmentAdapter;
        this.mFragmentList = mFragmentList;
        this.mViewPager = mViewPager;
        this.speechSynthesizer=speechSynthesizer;
        this.mainWebFragment=mainWebFragment;
        this.context=context;
    }

    @Override
    protected String doInBackground(Integer... params) {

        if(p.matcher(endResult).matches()){     //判断是否是查看日程指令
            alarmInfoDao=new AlarmInfoDao(context);
            alarms=alarmInfoDao.getCount();
            System.out.println("日程数量-alarms:" + alarms);
            if(alarms>0){
                alarmInfos=alarmInfoDao.find();
                System.out.println("日程列表 alarmInfos" + alarmInfos[0].toString());
            }
        }else if(p2.matcher(endResult).matches()){

        }
        else{
            try{
                SpeakToitActivity.MIROPHONE_STATE=2;
                dao2=new MagicUserInfoDao(context);
                Log.i("", "doInBackground:000000000 ");
        /*
        String s = SocketHelp.send("189019235859", "SQe6100cdcee892111", endResult);
        magicResult = SocketHelp.transfer(s);
        */


                MagicInfoJPushDevice mjd=new MagicInfoJPushDevice();
                if(dao2.getCount()!=0){
                    uid=dao2.findUid();
                }else{
                    //如果uid为空 就将 极光推送的 唯一标识id registerID 发送到服务器
                    mjd.setRegistrationID(JPushInterface.getRegistrationID(context));
                }
                String deviceId=DeviceIdUtil.comDeviceId(context);
                String token="SQe6100cdcee892111";
                MagicMapLocation location=MapLocation.location;
                String api="SEND";
                MagicInfo magicInfo=new MagicInfo(uid,token,endResult,location,deviceId, MagicInfoApiEnum.SEND,resourceInfo);
                //设置极光推送的 registerID
                magicInfo.setPushDevice(mjd);

                httpResultString = HttpHelp.send(magicInfo);      //向服务器发送请求，获得服务器回复的字符串
                magicResult=HttpHelp.transfer(httpResultString);         //将服务器回复的字符串转化为对象
                SpeakToitActivity.magicResult=magicResult;
                messageId=magicResult.getMessageId();
                if(!uid.equals("")){
                    //发送语音的线程
                    if(isUploadVoice){
                        isUploadVoice=false;
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                Pcm2Wav pw=new Pcm2Wav();
                                try {
                                    //初始化wav文件和amr文件的路径
                                    waveFilePath=context.getFilesDir()+File.separator+messageId+".wav";
                                    amrFilePath=context.getFilesDir()+File.separator+messageId+".amr";
                                    pw.convertAudioFiles("sdcard/outfile.pcm",waveFilePath);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.i(TAG, "run pcm转换wav失败");
                                }
                                try {
                                    wav2amr(waveFilePath);
                                    String s=HttpHelp.uploadVoiceFile(uid,messageId,amrFilePath);
                                    Boolean voiceFlag= Boolean.valueOf(HttpHelp.getJsonString(s));
                                    if(voiceFlag){
                                        //如果上传成功，则删除声音源文件
                                        File file1=new File(waveFilePath);
                                        File file2=new File(amrFilePath);
                                        file1.delete();
                                        file2.delete();
                                    }else {
//                                    如果上传失败,将文件信息记录在数据库，下次登陆时候再重新上传
                                        File file1=new File(waveFilePath);
                                        file1.delete();
                                        VoiceFileRecord vfr=new VoiceFileRecord(messageId,amrFilePath,false);
                                        VoiceFileDao vfDao=new VoiceFileDao(context);
                                        vfDao.add(vfr);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Log.i(TAG, "run wav转amr失败");
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }
                }


                //判断服务器回复的结果 是否 为用户注册之后的基本信息，如果是则将用户资料存入数据库
                try{
                    userInfoStatus=magicResult.getUserInfoStatus();
                    System.out.println("userInfoStatus" + userInfoStatus);
                    if(!MagicResultUserInfoStatusEnum.NULL.equals(userInfoStatus.toString().trim())){
                        dao2.deleteAll();
                        MagicUserInfo user=magicResult.getUserInfo();
                        dao2.add(user);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }




        return null;
    }

    /**
     * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）
     * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
     */
    @Override
    protected void onPostExecute(String result) {
        SpeakToitActivity.MIROPHONE_STATE=2;
        super.onPostExecute(result);
        if(p.matcher(endResult).matches()){
            if(alarms>0){
                System.out.println("查看日程onPostExecute" );
                speechSynthesizer.speak("您的日程安排如下：");

                MainAlarmsFragment alarmsFragment=new MainAlarmsFragment(alarmInfos,alarms);
                addFragment2List(alarmsFragment);
                mFragmentAdapter.notifyDataSetChanged();
                mViewPager.setCurrentItem(mFragmentList.size() - 1);
                mFragmentList.remove(mFragmentList.size() - 2);
                mFragmentAdapter.notifyDataSetChanged();
                SpeakToitActivity.MIROPHONE_STATE=0;       //使话筒初始化，达到
            }else{
                content="暂无日程安排";
                mainEditFragment.topShow.setText(content);
                Bundle bundle = new Bundle();
                bundle.putInt(MainEditFragment.IS_GET_STATE, MainEditFragment.BUTTON_DEFAULT);
                bundle.putBoolean(MainEditFragment.IS_NOT_DEFAULT, false);
                bundle.putString(MainEditFragment.TOP_TEXT_SHOW, content);
                bundle.putString(MainEditFragment.TITLE, "");
                bundle.putString(MainEditFragment.CENTER_TEXT_SHOW, endResult);
                mFragmentList.get(mFragmentList.size()-1).getArguments().putAll(bundle);
                SpeakToitActivity.MIROPHONE_STATE=3;
                speechSynthesizer.speak(content);
            }

        }else if(p2.matcher(endResult).matches()){
            content="您可以说“查看日程”来查看并删除日程，可以直接说“十分钟以后提醒我和王总开会”等信息可直接新增日程。";
            mainEditFragment.topShow.setText(content);
            Bundle bundle = new Bundle();
            bundle.putInt(MainEditFragment.IS_GET_STATE, MainEditFragment.BUTTON_DEFAULT);
            bundle.putBoolean(MainEditFragment.IS_NOT_DEFAULT, false);
            bundle.putString(MainEditFragment.TOP_TEXT_SHOW, content);
            bundle.putString(MainEditFragment.TITLE, "");
            bundle.putString(MainEditFragment.CENTER_TEXT_SHOW, endResult);
            mFragmentList.get(mFragmentList.size()-1).getArguments().putAll(bundle);
            SpeakToitActivity.MIROPHONE_STATE=3;
            speechSynthesizer.speak(content);
        }else{
            try{
                //判断用户是否是第一次登录，如果是，弹出引导界面，教用户使用左划用快捷菜单，右划看历史记录的功能
                if(MagicResultUserInfoStatusEnum.INSERT.equals(userInfoStatus)){
                    if(userInfoStatus.getCode().equals(MagicResultUserInfoStatusEnum.INSERT)){
                        LayoutInflater inflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View firstTeach = inflater.inflate(R.layout.activity_first_teach, null);
                        mContainer.addView(firstTeach);
                        firstTeach.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mContainer.removeView(view);
                            }
                        });
                    }
                }



                //判断服务器回发的消息的类型是什么，根据对应的类型，在app界面上显示对应的内容
                MagicResultEnum type=null;
                try{
                    type=magicResult.getType();
                }catch (Exception e){
                    speechSynthesizer.speak("服务器正在维护中，请稍后重试！");
                    MainEditFragment mainEditFragment = MainEditFragment.newInstance("", "服务器正在维护中，请稍后重试！", "", MainEditFragment.BUTTON_DEFAULT);;
                    addFragment2List(mainEditFragment);
                    mFragmentAdapter.notifyDataSetChanged();
                    mViewPager.setCurrentItem(mFragmentList.size() - 1);
                }

                if(type!=null){
                    if(type.equals(MagicResultEnum.TEXT)){
                        String question=magicResult.getQuestion().trim();
                        content=magicResult.getResultText().getContent();
                        Boolean b=magicResult.isExitDialogButton();
                        if(b){      //如果含有“退出场景”的按钮
                            if(question.equals(endResult.trim())){
//                                System.out.println("根据回答改变UI");
//                                mainEditFragment.topShow.setText(content);
//                                Bundle bundle = new Bundle();
//                                bundle.putInt(MainEditFragment.IS_GET_STATE, MainEditFragment.EXIT_SCENE);
//                                bundle.putBoolean(MainEditFragment.IS_NOT_DEFAULT, false);
//                                bundle.putString(MainEditFragment.TOP_TEXT_SHOW, content);
//                                bundle.putString(MainEditFragment.TITLE, "");
//                                bundle.putString(MainEditFragment.EXIT_SCENE_TEXT, magicResult.getExitDialogButtonName());
//                                bundle.putString(MainEditFragment.CENTER_TEXT_SHOW, endResult);
//                                mFragmentList.get(mFragmentList.size()-1).getArguments().putAll(bundle);
                                mainEditFragment = MainEditFragment.newInstance(null,content,question,MainEditFragment.EXIT_SCENE,magicResult.getExitDialogButtonName());
                                addFragment2List(mainEditFragment);
                                mFragmentAdapter.notifyDataSetChanged();
                                mViewPager.setCurrentItem(mFragmentList.size() - 1);
                                mFragmentList.remove(mFragmentList.size() - 2);
                                mFragmentAdapter.notifyDataSetChanged();
                            }else{
                                mainEditFragment = MainEditFragment.newInstance(null,content,question,MainEditFragment.EXIT_SCENE,magicResult.getExitDialogButtonName());
                                addFragment2List(mainEditFragment);
                                mFragmentAdapter.notifyDataSetChanged();
                                mViewPager.setCurrentItem(mFragmentList.size() - 1);
                                mFragmentList.remove(mFragmentList.size() - 2);
                                mFragmentAdapter.notifyDataSetChanged();
                            }
                        }else {
                            if(question.equals(endResult.trim())){
                                System.out.println("根据回答改变UI");
                                mainEditFragment.topShow.setText(content);
                                Bundle bundle = new Bundle();
                                bundle.putInt(MainEditFragment.IS_GET_STATE, MainEditFragment.BUTTON_DEFAULT);
                                bundle.putBoolean(MainEditFragment.IS_NOT_DEFAULT, false);
                                bundle.putString(MainEditFragment.TOP_TEXT_SHOW, content);
                                bundle.putString(MainEditFragment.TITLE, "");
                                bundle.putString(MainEditFragment.CENTER_TEXT_SHOW, endResult);
                                mFragmentList.get(mFragmentList.size()-1).getArguments().putAll(bundle);
                            }else{
                                mainEditFragment = MainEditFragment.newInstance(null,content,question,MainEditFragment.BUTTON_DEFAULT);
                                addFragment2List(mainEditFragment);
                                mFragmentAdapter.notifyDataSetChanged();
                                mViewPager.setCurrentItem(mFragmentList.size() - 1);
                                mFragmentList.remove(mFragmentList.size() - 2);
                                mFragmentAdapter.notifyDataSetChanged();
                            }
                        }
                        if(magicResult.isPopKeyboard()){        //如果服务器发送的消息 要求用户以键盘输入
                            InputViewActivity.popKeyboard=true;
                            TextView tv= (TextView) mainEditFragment.getView().findViewById(R.id.speak_center_show);
                            tv.callOnClick();
                        }
                        SpeakToitActivity.MIROPHONE_STATE=3;
                        Log.i("ProgressAsyncTask", "onPostExecute ");
                        speechSynthesizer.speak(content);
                    }else if(type.equals(MagicResultEnum.URL)){
                        String url= magicResult.getResultUrl().getUrl();
                        Log.i(url, "url ");
                        String content=magicResult.getResultUrl().getContent();
                        Log.i(content, "content ");
                        mainWebFragment = MainWebFragment.newInstance(url,content,magicResult.getQuestion());
                        addFragment2List(mainWebFragment);
                        mFragmentAdapter.notifyDataSetChanged();
                        mViewPager.setCurrentItem(mFragmentList.size() - 1);
                        mFragmentList.remove(mFragmentList.size() - 2);
                        mFragmentAdapter.notifyDataSetChanged();
                        SpeakToitActivity.MIROPHONE_STATE=3;
                        speechSynthesizer.speak(content);

                    }else if(type.equals(MagicResultEnum.QUESTION)){
                        QuestionTask asyncTask = new QuestionTask(context,magicResult,mainWebFragment,speechSynthesizer,endResult , mainEditFragment, mFragmentAdapter,mFragmentList,mViewPager);
                        asyncTask.execute(1000);
                    }else if(type.equals(MagicResultEnum.FUNCTIONAL)){
                        MagicResultFunctionalEnum funType=magicResult.getResultFunctional().getType();
                        //打电话时
                        if(funType.equals(MagicResultFunctionalEnum.TELEPHONE)){
                            CallTask asyncTask = new CallTask(context,magicResult,mainWebFragment,speechSynthesizer,endResult , mainEditFragment, mFragmentAdapter,mFragmentList,mViewPager);
                            asyncTask.execute(1000);
                        }
                        else if(funType.equals(MagicResultFunctionalEnum.MESSAGE)){
                            MessageTask asyncTask = new MessageTask(context,magicResult,mainWebFragment,speechSynthesizer,endResult , mainEditFragment, mFragmentAdapter,mFragmentList,mViewPager);
                            asyncTask.execute(1000);
                        }
                        else if(funType.equals(MagicResultFunctionalEnum.SCHEDULE)){
                            ScheduleTask asyncTask=new ScheduleTask(context,magicResult,mainWebFragment,speechSynthesizer,endResult , mainEditFragment, mFragmentAdapter,mFragmentList,mViewPager);
                            asyncTask.execute(1000);
                        }
                        else if(funType.equals(MagicResultFunctionalEnum.MAGICAPP)){
                            MagicOperationMyApp asyncTask=new MagicOperationMyApp(drawer,context,magicResult,mainWebFragment,speechSynthesizer,endResult , mainEditFragment, mFragmentAdapter,mFragmentList,mViewPager);
                            asyncTask.execute(1000);
                        }
                        else if(funType.equals(MagicResultFunctionalEnum.APP)){

                        }
                    }else if(type.equals(MagicResultEnum.RESOURCEINFO)){
                        //判断是否为用户发资源时的 资源详情，如果是，则弹出 MainProjectFrament来显示资源详情让用户确认
                        /*
                        speechSynthesizer.speak("发布请点击发布按钮，更改标题可直接修改，更改内容请点击更改内容按钮，继续其他问话直接点击话筒即可。");
                        MainProjectFragment mainProjectFragment=MainProjectFragment.newInstance(magicResult.getResourceInfo().getTitle(),magicResult.getResourceInfo().getContent());
                        addFragment2List(mainProjectFragment);
                        mFragmentAdapter.notifyDataSetChanged();
                        mViewPager.setCurrentItem(mFragmentList.size() - 1);
                        mFragmentList.remove(mFragmentList.size() - 2);
                        mFragmentAdapter.notifyDataSetChanged();
                        SpeakToitActivity.MIROPHONE_STATE=0;       //使话筒初始化，达到
                        */
                        SpeakToitActivity.MIROPHONE_STATE=0;       //使话筒初始化，达到
//                        OpenResourceInfoTask task=new OpenResourceInfoTask(context,magicResult.getResourceInfo().getPostId());
//                        task.execute(1000);

                        if(magicResult.getResourceInfo().getPostId()==null){
                            Message msg=new Message();
                            msg.what=1;
                            Bundle bundle=new Bundle();
                            bundle.putString("title",magicResult.getResourceInfo().getTitle());
                            bundle.putString("content",magicResult.getResourceInfo().getContent());
                            msg.setData(bundle);
                            projectHandler.sendMessage(msg);
                        }else{
                            String resultText=magicResult.getResultText().getContent();
                            speechSynthesizer.speak(resultText);
                            MainEditFragment mainEditFragment = MainEditFragment.newInstance(null,resultText,"",MainEditFragment.BUTTON_DEFAULT,magicResult.getExitDialogButtonName());
                            addFragment2List(mainEditFragment);
                            mFragmentAdapter.notifyDataSetChanged();
                            mViewPager.setCurrentItem(mFragmentList.size() - 1);
                            mFragmentList.remove(mFragmentList.size() - 2);
                            mFragmentAdapter.notifyDataSetChanged();
                            OpenResourceInfoTask task=new OpenResourceInfoTask(context,magicResult.getResourceInfo().getPostId());
                            task.execute(1000);
                        }
                    }else{

                    }
                }
            }catch (Exception e){
                speechSynthesizer.speak("网络连接失败，请稍后重试！");
                MainEditFragment mainEditFragment = MainEditFragment.newInstance("", "网络连接失败，请稍后重试！", "", MainEditFragment.BUTTON_DEFAULT);;
                addFragment2List(mainEditFragment);
                mFragmentAdapter.notifyDataSetChanged();
                mViewPager.setCurrentItem(mFragmentList.size() - 1);
//            speechSynthesizer.speak("网络连接失败，请稍后重试！");
//            mainEditFragment = MainEditFragment.newInstance("", "网络连接失败，请稍后重试！", endResult, MainEditFragment.BUTTON_DEFAULT);
//            addFragment2List(mainEditFragment);
//            mFragmentAdapter.notifyDataSetChanged();
//            mViewPager.setCurrentItem(mFragmentList.size() - 1);
                /*
                if(httpResultString.trim().toString().equals("{\"json\":\"\"}")){
                    SpeakToitActivity.MIROPHONE_STATE=0;
                    mainEditFragment.topShow.setText("");
                    Bundle bundle = new Bundle();
                    bundle.putInt(MainEditFragment.IS_GET_STATE, MainEditFragment.BUTTON_DEFAULT);
                    bundle.putBoolean(MainEditFragment.IS_NOT_DEFAULT, false);
                    bundle.putString(MainEditFragment.TOP_TEXT_SHOW, "");
                    bundle.putString(MainEditFragment.TITLE, "");
                    bundle.putString(MainEditFragment.CENTER_TEXT_SHOW, endResult);
                    mFragmentList.get(mViewPager.getCurrentItem()).getArguments().putAll(bundle);
//                    speechSynthesizer.speak("网络连接失败，请稍后重试！");
                }else{
//                    mainEditFragment.topShow.setText("网络连接失败，请稍后重试！");
//                    Bundle bundle = new Bundle();
//                    bundle.putInt(MainEditFragment.IS_GET_STATE, MainEditFragment.BUTTON_DEFAULT);
//                    bundle.putBoolean(MainEditFragment.IS_NOT_DEFAULT, false);
//                    bundle.putString(MainEditFragment.TOP_TEXT_SHOW, "网络连接失败，请稍后重试！");
//                    bundle.putString(MainEditFragment.TITLE, "");
//                    bundle.putString(MainEditFragment.CENTER_TEXT_SHOW, endResult);
                    speechSynthesizer.speak("网络连接失败，请稍后重试！");
                    MainEditFragment mainEditFragment = MainEditFragment.newInstance("", "网络连接失败，请稍后重试！", "", MainEditFragment.BUTTON_DEFAULT);;
                    addFragment2List(mainEditFragment);
                    mFragmentAdapter.notifyDataSetChanged();
                    mViewPager.setCurrentItem(mFragmentList.size() - 1);

//                    mFragmentList.get(mViewPager.getCurrentItem()).getArguments().putAll(bundle);

                }
                */
                e.printStackTrace();
            }
        }
    }
    private List<Fragment> addFragment2List(Fragment fragment) {
        int mSize = mFragmentList.size();
        if (mSize >= MaxFragmentSize) {
            mFragmentList.remove(0);
            return addFragment2List(fragment);
        }
        mFragmentList.add(fragment);
        return mFragmentList;
    }
    public void wav2amr(String wavFileName) throws IOException
    {
//        wavFileName="sdcard/record.wav";
        InputStream inStream;
        inStream = new FileInputStream(wavFileName);
        AmrInputStream aStream = new AmrInputStream(inStream);

        File file = new File(wavFileName.replace(".wav", ".amr"));
        file.createNewFile();
        OutputStream out = new FileOutputStream(file);

        byte[] x = new byte[1024];
        int len;
        out.write(0x23);
        out.write(0x21);
        out.write(0x41);
        out.write(0x4D);
        out.write(0x52);
        out.write(0x0A);
        while ((len = aStream.read(x)) > 0)
        {
            out.write(x, 0, len);
        }

        out.close();
        aStream.close();
    }


}
