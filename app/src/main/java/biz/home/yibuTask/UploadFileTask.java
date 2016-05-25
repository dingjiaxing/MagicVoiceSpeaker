package biz.home.yibuTask;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.speechsynthesizer.SpeechSynthesizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import biz.home.SpeakToitActivity;
import biz.home.adapter.FragmentAdapter;
import biz.home.bean.MagicResourceInfo;
import biz.home.bean.MagicResult;
import biz.home.bean.MagicResultResourceInfo;
import biz.home.fragment.MainEditFragment;
import biz.home.fragment.MainWebFragment;
import biz.home.util.ContactInfoDao;
import biz.home.util.HttpHelp;
import biz.home.util.MagicUserInfoDao;
import biz.home.util.ProgressBarAsyncTask;

/**
 * Created by admin on 2015/9/16.
 */
public class UploadFileTask extends AsyncTask<String, Integer, String> {
    public static Map<String,String > pics;
    Map<String,String> map;

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
    MagicUserInfoDao dao;
    ProgressBar progressBar;

    Boolean userInfoStatus;
    ViewGroup mContainer;
    public UploadFileTask(ProgressBar progressBar,MagicResultResourceInfo resourceInfo,ViewGroup mContainer,Context context,MainWebFragment mainWebFragment,SpeechSynthesizer speechSynthesizer,String endResult, MainEditFragment mainEditFragment, FragmentAdapter mFragmentAdapter, List<Fragment> mFragmentList, ViewPager mViewPager) {
        super();
        this.progressBar=progressBar;
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
        this.pics= SpeakToitActivity.pics;
    }

    @Override
    protected String doInBackground(String... params) {
        try{
            //上传文件
            publishProgress(25);
            dao=new MagicUserInfoDao(context);
            String uid=dao.findUid();
            publishProgress(30);
            String telephone=dao.findTelephone();
            publishProgress(35);
            String text=resourceInfo.getContent();
            Map<String,String> map2=new HashMap<>();
            int i=0;
            publishProgress(40);
            for(String key:pics.keySet()){
                if(text.contains(key)){
                    String value=pics.get(key);
                    map2.put(key,value);
                }
            }
            publishProgress(50);
            pics=map2;
            String[] picPaths=new String[map2.size()];
            publishProgress(60);
            for(String key:map2.keySet()){
                String value=pics.get(key);
                picPaths[i]=value;
                String newValue=value.substring(value.lastIndexOf("/")+1);
                pics.put(key,newValue);
                i++;
            }
            publishProgress(70);
            System.out.println("picPaths.length:" + picPaths.length);
            System.out.println(" HttpHelp.uploadFile(uid, telephone, picPaths, pics):" + uid + telephone + picPaths + pics);
            String result= HttpHelp.uploadFile(uid, telephone, picPaths, pics);
            publishProgress(85);
            map=HttpHelp.transferPicResultToMap(result);
            for(String key:map.keySet()){
                System.out.println("map key  value" +key+map.get(key) );
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        try{
            progressBar.setProgress(90);
            //上传文本信息
            resourceInfo.setImage(map);
//        ProjectTask asyncTask=new ProjectTask(getApplicationContext(),mainWebFragment,speechSynthesizer,info.getResourceInfo() , mainEditFragment, mFragmentAdapter,mFragmentList,mViewPager);
            ProgressBarAsyncTask asyncTask=new ProgressBarAsyncTask( resourceInfo,mContainer,context,mainWebFragment,speechSynthesizer,endResult, mainEditFragment, mFragmentAdapter, mFragmentList, mViewPager) ;
            asyncTask.execute(2000);
            progressBar.setProgress(100);
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

        super.onPostExecute(s);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        System.out.println("values[0]" + values[0]);
        progressBar.setProgress(values[0]);
//        Toast.makeText(context,values[0].toString(),Toast.LENGTH_SHORT).show();
        super.onProgressUpdate(values);
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
