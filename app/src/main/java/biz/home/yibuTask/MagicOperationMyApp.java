package biz.home.yibuTask;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;

import com.baidu.speechsynthesizer.SpeechSynthesizer;

import java.util.List;

import biz.home.SpeakToitActivity;
import biz.home.adapter.FragmentAdapter;
import biz.home.bean.MagicResult;
import biz.home.fragment.MainEditFragment;
import biz.home.fragment.MainWebFragment;
import biz.home.util.ContactInfoDao;

/**
 * Created by admin on 2015/9/25.
 */
public class MagicOperationMyApp extends AsyncTask{
    DrawerLayout drawer;
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
    String TAG="MessageTask-Class";
    public MagicOperationMyApp() {
    }
    public MagicOperationMyApp(DrawerLayout drawer,Context context, MagicResult magicResult, MainWebFragment mainWebFragment, SpeechSynthesizer speechSynthesizer, String endResult, MainEditFragment mainEditFragment, FragmentAdapter mFragmentAdapter, List<Fragment> mFragmentList, ViewPager mViewPager) {

        super();
        this.drawer=drawer;
        this.endResult = endResult;
        this.mFragmentAdapter = mFragmentAdapter;
        this.mFragmentList = mFragmentList;
        this.speechSynthesizer = speechSynthesizer;
        this.mViewPager = mViewPager;
        this.context = context;
        this.magicResult = magicResult;
        this.mainEditFragment = mainEditFragment;
        this.mainWebFragment = mainWebFragment;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        try{
            String question=magicResult.getQuestion();
            String content=magicResult.getResultFunctional().getMagicApp().getContent();
            if(question.equals(mainEditFragment.centerShow)){
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
            }
            SpeakToitActivity.MIROPHONE_STATE=3;
            Log.i("ProgressAsyncTask", "onPostExecute ");
            speechSynthesizer.speak(content);

            drawer.openDrawer(Gravity.RIGHT);
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

        super.onPostExecute(o);
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
