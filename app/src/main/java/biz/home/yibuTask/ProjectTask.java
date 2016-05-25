package biz.home.yibuTask;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.speechsynthesizer.SpeechSynthesizer;

import java.util.ArrayList;
import java.util.List;

import biz.home.R;
import biz.home.SpeakToitActivity;
import biz.home.adapter.FragmentAdapter;
import biz.home.bean.MagicInfo;
import biz.home.bean.MagicResourceInfo;
import biz.home.bean.MagicResult;
import biz.home.bean.MagicResultResourceInfo;
import biz.home.fragment.MainEditFragment;
import biz.home.fragment.MainWebFragment;
import biz.home.model.MagicResultEnum;
import biz.home.model.MagicResultFunctionalEnum;
import biz.home.model.MagicResultUserInfoStatusEnum;
import biz.home.model.MagicUserInfo;
import biz.home.util.ContactInfoDao;
import biz.home.util.DeviceIdUtil;
import biz.home.util.HttpHelp;
import biz.home.util.MagicUserInfoDao;

/**
 * Created by admin on 2015/9/4.
 */
public class ProjectTask extends AsyncTask {
    private  MagicInfo magicInfo;
    private MainEditFragment mainEditFragment;
    private FragmentAdapter mFragmentAdapter;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private ViewPager mViewPager;
    //    private TextView textView;
//    private ProgressBar progressBar;
    private static String content, endResult;
    private final int MaxFragmentSize = 10;
    public Context context;
    //百度语音合成相关参数
    protected static final int UI_LOG_TO_VIEW = 0;
    protected static final int UI_TOAST = 1;
    private SpeechSynthesizer speechSynthesizer;
    private TextView logView;
    private EditText inputTextView;
    private Button startButton;
    private Handler uiHandler;
    private Toast mToast;
    private String str="a";
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

    public ProjectTask(Context applicationContext, MainWebFragment mainWebFragment, SpeechSynthesizer speechSynthesizer, MagicResourceInfo resourceInfo, MainEditFragment mainEditFragment, FragmentAdapter mFragmentAdapter, List<Fragment> mFragmentList, ViewPager mViewPager) {

    }
    public ProjectTask(Context context, MagicResult magicResult, MainWebFragment mainWebFragment, SpeechSynthesizer speechSynthesizer, MagicInfo magicInfo, MainEditFragment mainEditFragment, FragmentAdapter mFragmentAdapter, List<Fragment> mFragmentList, ViewPager mViewPager) {
        super();
        this.context=context;
        this.magicResult=magicResult;
        this.magicInfo=magicInfo;
        this.mainEditFragment = mainEditFragment;
        this.mFragmentAdapter = mFragmentAdapter;
        this.mFragmentList = mFragmentList;
        this.mViewPager = mViewPager;
        this.speechSynthesizer=speechSynthesizer;
        this.mainWebFragment=mainWebFragment;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try{
            MagicResultResourceInfo resourceInfo=new MagicResultResourceInfo();
            resourceInfo.setTitle(magicInfo.getResourceInfo().getTitle());
            resourceInfo.setContent(magicInfo.getResourceInfo().getContent());
            resourceInfo.setConfirm(magicInfo.getResourceInfo().isConfirm());

            dao2=new MagicUserInfoDao(context);
            String uid="";
            if(dao2.getCount()!=0){
                uid=dao2.findUid();
            }
            String deviceId= DeviceIdUtil.comDeviceId(context);
            magicResult = HttpHelp.send(uid, "SQe6100cdcee892111", "", deviceId,resourceInfo);
            userInfoStatus=magicResult.getUserInfoStatus();
            if(!MagicResultUserInfoStatusEnum.NULL.getCode().equals(userInfoStatus.getCode())){
                dao2.deleteAll();
                MagicUserInfo user=magicResult.getUserInfo();
                dao2.add(user);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        try{
            //textView.setText("异步操作执行结束" + result);
            //将识别结果显示在界面上
            super.onPostExecute(o);
            if(MagicResultUserInfoStatusEnum.INSERT.getCode().equals(userInfoStatus.getCode())){
                //如果第一次登录成功，则显示出来知道界面，教用户使用左划用快捷菜单，右划看历史记录的功能
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

            MagicResultEnum type=magicResult.getType();
            if(type.equals(MagicResultEnum.TEXT)){
                try{
                    System.out.println("根据回答改变UI");
                    mainEditFragment.topShow.setText(content);
                    Bundle bundle = new Bundle();
                    bundle.putInt(MainEditFragment.IS_GET_STATE, MainEditFragment.BUTTON_DEFAULT);
                    bundle.putBoolean(MainEditFragment.IS_NOT_DEFAULT, false);
                    bundle.putString(MainEditFragment.TOP_TEXT_SHOW, content);
                    bundle.putString(MainEditFragment.TITLE, "");
                    bundle.putString(MainEditFragment.CENTER_TEXT_SHOW, endResult);
                    mFragmentList.get(mViewPager.getCurrentItem()).getArguments().putAll(bundle);
                    speechSynthesizer.speak(content);
                    SpeakToitActivity.MIROPHONE_STATE=2;
                }catch(Exception e){
                    System.out.println(e);
                }
            }else if(type.equals(MagicResultEnum.URL)){
                String url= magicResult.getResultUrl().getUrl();
                Log.i(url, "url ");
                String content=magicResult.getResultUrl().getContent();
                Log.i(content, "content ");
                mainWebFragment = MainWebFragment.newInstance(url,content,endResult);
//                mainWebFragment = MainWebFragment.newInstance(url);
//                mainWebFragment.webText.setText(content);
                //调用添加方法
                addFragment2List(mainWebFragment);

                mFragmentAdapter.notifyDataSetChanged();
                mViewPager.setCurrentItem(mFragmentList.size() - 1);
                mFragmentList.remove(mFragmentList.size() - 2);
                mFragmentAdapter.notifyDataSetChanged();
/*
                Bundle bundle = new Bundle();
                bundle.putString(URL, url);
                bundle.putString(TEXT_SHOW, content);
                bundle.putBoolean(IS_GET_Data, true);
                mFragmentList.get(mViewPager.getCurrentItem()).getArguments().putAll(bundle);
*/
//                mainWebFragment.
                speechSynthesizer.speak(content);
                SpeakToitActivity.MIROPHONE_STATE=2;
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
                else if(funType.equals(MagicResultFunctionalEnum.APP)){

                }
            }else{

            }
        }catch (Exception e){
            e.printStackTrace();
            String error="服务器错误，请稍后重试！";
            mainEditFragment = MainEditFragment.newInstance(null,error,"",MainEditFragment.BUTTON_DEFAULT);
            addFragment2List(mainEditFragment);
            mFragmentAdapter.notifyDataSetChanged();
            mViewPager.setCurrentItem(mFragmentList.size() - 1);
            SpeakToitActivity.MIROPHONE_STATE=3;
            speechSynthesizer.speak(error);
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
}
