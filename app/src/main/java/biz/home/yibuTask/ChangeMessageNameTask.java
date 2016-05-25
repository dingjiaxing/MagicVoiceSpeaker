package biz.home.yibuTask;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.baidu.speechsynthesizer.SpeechSynthesizer;

import java.util.List;

import biz.home.SpeakToitActivity;
import biz.home.adapter.FragmentAdapter;
import biz.home.bean.MagicResult;
import biz.home.fragment.MainEditFragment;
import biz.home.fragment.MainMessageFragment;
import biz.home.fragment.MainPreMessageFragment;
import biz.home.fragment.MainWebFragment;
import biz.home.model.ContactInfo;
import biz.home.util.ContactInfoDao;

/**
 * Created by admin on 2015/9/5.
 */
public class ChangeMessageNameTask extends AsyncTask {
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
    String TAG="MessageTask-Class";
    public static int size;                   //一个姓名对应了size个电话号码
    ContactInfo[] cin;

    //获取手机联系人用到的
    private String[] columns = { ContactsContract.Contacts._ID,// ���IDֵ
            ContactsContract.Contacts.DISPLAY_NAME,// �������
            ContactsContract.CommonDataKinds.Phone.NUMBER,// ��õ绰
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID, };
    public ChangeMessageNameTask(Context context,MagicResult magicResult,MainWebFragment mainWebFragment,SpeechSynthesizer speechSynthesizer,String endResult, MainEditFragment mainEditFragment, FragmentAdapter mFragmentAdapter, List<Fragment> mFragmentList, ViewPager mViewPager) {
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
            dao=new ContactInfoDao(context);
//            getQueryData();//将用户的信息添加到数据库sqllite中
//        Log.i("", "onPostExecute dao.getCount" + dao.getCount());
//        String name=magicResult.getResultFunctional().getMessage().getName();
//        Log.i("", "onPostExecute "+name);
//        cin=dao.match(name);
            cin=dao.findAll();
            size=dao.getCount();
//        try{
//            while(cin[size]!=null){
//                size++;
//            }
//        }catch(Exception e){
//            e.printStackTrace();
//        }

            Log.i(TAG, "doInBackground :size:" + size);
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        try{
            Log.i(TAG, "onPostExecute :size"+size);
            MainPreMessageFragment mc=MainPreMessageFragment.newInstance(cin,size);
            //调用添加方法
            addFragment2List(mc);
            mFragmentAdapter.notifyDataSetChanged();
            mViewPager.setCurrentItem(mFragmentList.size() - 1);
//        mFragmentList.remove(mFragmentList.size() - 2);
            mFragmentAdapter.notifyDataSetChanged();
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
    //获取手机内存储的联系人列表
    private String getQueryData() {

        StringBuilder sb = new StringBuilder();// ���ڱ����ַ�
        ContentResolver resolver = context.getContentResolver();// ���ContentResolver����
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);// ��ѯ��¼
        while (cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndex(columns[0]);// ���IDֵ������
            int displayNameIndex = cursor.getColumnIndex(columns[1]);// �����������
            int id = cursor.getInt(idIndex);// ���id
            String displayName = cursor.getString(displayNameIndex);// 联系人姓名
//            displayName= String.valueOf(ContactInfoDao.getPinYin(displayName));
            Cursor phone = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, columns[3] + "=" + id, null, null);
            while (phone.moveToNext()) {
                int phoneNumberIndex = phone.getColumnIndex(columns[2]);// ��õ绰����
                String phoneNumber = phone.getString(phoneNumberIndex);// 联系人电话
                ContactInfo ci=new ContactInfo(phoneNumber,displayName);
                Log.i("", "getQueryData phoneNumber" + phoneNumber + ";name:" + displayName);
                if(dao.find(phoneNumber)==null){
                    try{
                        dao.add(ci);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }else{
                    try {
                        dao.update(ci);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
                sb.append(displayName + ": " + phoneNumber + "\n");// �������
                System.out.println("MessageTask:getQueryData():"+sb);
                Log.i(TAG, "getQueryData "+sb.toString());
            }
        }
        cursor.close();// �ر��α�
        return sb.toString();
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
