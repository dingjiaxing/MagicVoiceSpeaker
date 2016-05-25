package biz.home.yibuTask;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.baidu.speechsynthesizer.SpeechSynthesizer;

import java.util.List;

import biz.home.SpeakToitActivity;
import biz.home.adapter.FragmentAdapter;
import biz.home.bean.MagicResult;
import biz.home.bean.MagicResultButton;
import biz.home.bean.MagicResultQuestionResource;
import biz.home.bean.MagicResultResourceInfo;
import biz.home.fragment.MainEditFragment;
import biz.home.fragment.MainWebFragment;
import biz.home.model.ContactInfo;
import biz.home.model.MagicResultButtonEnum;
import biz.home.model.MagicResultQuestionEnum;
import biz.home.util.ContactInfoDao;

/**
 * Created by admin on 2015/8/23.
 */
public class QuestionTask extends AsyncTask {
    private  String endResult;
    private  FragmentAdapter mFragmentAdapter;
    private  List<Fragment> mFragmentList;
    SpeechSynthesizer speechSynthesizer;
    ViewPager mViewPager;
    private final int MaxFragmentSize = 10;
    Context context;
    MagicResult magicResult;
    MainEditFragment mainEditFragment;
    MainWebFragment mainWebFragment;
    ContactInfoDao dao;
    String TAG="CallTask-Class";
    public static int size;                   //一个姓名对应了size个电话号码
    ContactInfo[] cin;
    MagicResultResourceInfo resource;
    /**
     * 返回类型2一般文本交互，用户选择
     */
    public static final int BUTTON_DETAIL = 1502;       //用户点击按钮确认

    public QuestionTask(Context context, MagicResult magicResult, MainWebFragment mainWebFragment, SpeechSynthesizer speechSynthesizer, String endResult, MainEditFragment mainEditFragment, FragmentAdapter mFragmentAdapter, List<Fragment> mFragmentList, ViewPager mViewPager) {
        super();
        this.context=context;
        this.magicResult=magicResult;
        this.endResult = endResult;
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
            MagicResultQuestionEnum type=magicResult.getResultQuestion().getType();
            Log.i(TAG, "doInBackground type"+type);
            if(type.getCode().equals(MagicResultQuestionEnum.RESOURCE.getCode())){
                resource=magicResult.getResultQuestion().getResource();
                String title=resource.getTitle();
//                String name=resource.getName();
                String time=resource.getDateTime();
                String position=resource.getPosition();
                String company=resource.getCompanyName();

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public QuestionTask() {
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        try{
            MagicResultQuestionEnum type=magicResult.getResultQuestion().getType();
            if(type.equals(MagicResultQuestionEnum.RESOURCE)){
                String title1=magicResult.getResultQuestion().getTitle();
                resource=magicResult.getResultQuestion().getResource();
                Log.i(TAG, "onPostExecute: magicResult.getResultQuestion().getValue():"+magicResult.getResultQuestion().getButton().get(0).getValue());
                if(magicResult.getResultQuestion().getButton()!=null){
                    for(MagicResultButton item:magicResult.getResultQuestion().getButton()){
                        if(item.getType().equals(MagicResultButtonEnum.RESOURCEINFO)){
                            resource.setPostId(item.getValue());
                        }
                    }
                }


                String title2=resource.getTitle();
                //String name=resource.getName();
                String name=resource.getRealName();
                String time=resource.getDateTime();
                String position=resource.getPosition();
                String company=resource.getCompanyName();
                String url=magicResult.getResultQuestion().getButton().get(0).getUrl();
                //title1+"\n"
                String topTitle=title1;
                String topText="标题："+title2+"\n"
                        +"发布人："+name+"  "+"\n"
                        +"发布时间："+time;
                String centerText=endResult;

                MainEditFragment mainEditFragment=MainEditFragment.newInstance(title1,topText,centerText,BUTTON_DETAIL,url,endResult,title2,resource);
                addFragment2List(mainEditFragment);
                mFragmentAdapter.notifyDataSetChanged();
                mViewPager.setCurrentItem(mFragmentList.size() - 1);
                mFragmentList.remove(mFragmentList.size() - 2);
                mFragmentAdapter.notifyDataSetChanged();
                SpeakToitActivity.MIROPHONE_STATE=3;
                speechSynthesizer.speak(title2 + "  " + topTitle);

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
