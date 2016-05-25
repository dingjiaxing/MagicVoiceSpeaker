package biz.home.fragment;

import android.app.Activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import biz.home.R;
import biz.home.SpeakToitActivity;
import biz.home.adapter.FragmentAdapter;
import biz.home.model.ContactInfo;
import biz.home.util.LogUtil;

/**
 * Created by admin on 2015/8/18.
 */
public class MainPreMessageFragment extends Fragment  {
    private OnMessageRecieverSure listener;
    private OnMessageContactReturn messageContactReturnListener;
    public static final String  NUMBER1 = "Number1";
    public static final String  NUMBER2 = "Number2";
    public static final String  NUMBER3 = "Number3";
    public static final String  NUMBER4 = "Number4";
    public static final String  NUMBER5 = "Number5";
    public static final String  NAME1 = "Name1";
    public static final String  NAME2 = "Name2";
    public static final String  NAME3 = "Name3";
    public static final String  NAME4= "Name4";
    public static final String  NAME5 = "Name5";
    public static  int SIZE;
    public static ContactInfo[] ciss;
    public static String content;
    private Button button;


    public static final String CENTER_TEXT_SHOW = "MainEditFragment_CenterShow";
    ;
    private ListView listView;
    private String TAG = MainPreMessageFragment.class.getName();
    Context context;
    private List<Map<String, String>> dataList = null;
    private SimpleAdapter adapter = null;
    private String[] columns = { ContactsContract.Contacts._ID,// ���IDֵ
            ContactsContract.Contacts.DISPLAY_NAME,// �������
            ContactsContract.CommonDataKinds.Phone.NUMBER,// ��õ绰
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID, };
    public static MainPreMessageFragment  newInstance() {
        return null;
    }
    public static MainPreMessageFragment  newInstance(@Nullable ContactInfo[] cis,@Nullable int size) {
        SIZE=size;
        ciss=cis;
        MainPreMessageFragment fragment = new MainPreMessageFragment();
        Bundle bundle = new Bundle();
        String name="NAME";
        String num="NUMBER";
        for(int i=0;i<size;i++){
            bundle.putString(name+i,cis[i].getName());
            bundle.putString(num+i,cis[i].getTel());
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.contact_information_list, container, false);
        finById(view);
        initDrawerList();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageContactReturnListener != null) {
                    messageContactReturnListener.onMessageContactReturn();
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> map = new HashMap<>();
                map = (Map<String, String>) adapter.getItem(position);
                String info = map.get("function");
                Log.i(TAG, "onItemClick position:" + position + "  id:" + id);
                Log.i("电话号码", "onItemClick " + info);
//                Toast.makeText(context,info,Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onItemClick " + info);
                //获取上下文对象
                context = getActivity().getApplicationContext();
                //进入发短信页面
                String name = ciss[position].getName();
                String tel = ciss[position].getTel();
                if (listener != null) {
                    listener.onButtonClick(name, tel, position);
                }
//                SpeakToitActivity.addFragment2List(mc);
//                mFragmentAdapter.notifyDataSetChanged();
//                mViewPager.setCurrentItem(mFragmentList.size() - 1);
//                mFragmentList.remove(mFragmentList.size() - 2);
//                mFragmentAdapter.notifyDataSetChanged();
            }
        });


        return view;
    }
    //自定义接口，目的是传值给SpeakToitActivity
    public interface OnMessageRecieverSure{
        public void onButtonClick(String name,String tel,int position);
    }

    public interface  OnMessageContactReturn{
        public void onMessageContactReturn();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
        messageContactReturnListener=null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof  OnMessageRecieverSure){
            listener=(OnMessageRecieverSure)activity;
        }else{
            try {
                throw new Exception("activityy should implements OnButtonClick Interface");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(activity instanceof  OnMessageContactReturn){
            messageContactReturnListener=(OnMessageContactReturn)activity;
        }else{
            try {
                throw new Exception("activityy should implements OnButtonClick Interface");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
//            mOnActivityListener = (MainEditFragment.OnActivityListener) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void finById(View v){
        listView=(ListView) v.findViewById(R.id.contact_information);
        button=(Button)v.findViewById(R.id.contact_information_return);
    }
    private void setOnClick(){

    }
    private List<? extends Map<String, ?>> getData(String[] strs) {
        List<Map<String ,Object>> list = new ArrayList<Map<String,Object>>();

        for (int i = 0; i < strs.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", strs[i]);
            list.add(map);

        }

        return list;
    }
    private void initDrawerList() {
        dataList = new ArrayList<>();
//        String[] functions = getResources().getStringArray(R.array.drawer);     //从xml中获取数据
        String[] functions=new String[SIZE];
        int[] img=new int[SIZE];
        for(int i=0;i<SIZE;i++){
            functions[i]=ciss[i].getName()+"  "+ciss[i].getTel();
            img[i]=R.drawable.contactor_header_icon;
        }

//        int[] img = {R.drawable.drawer_01, R.drawable.drawer_02,
//                R.drawable.drawer_03, };
        for (int i = 0; i < functions.length; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("img", String.valueOf(img[i]));
            map.put("function", functions[i]);
            dataList.add(map);
        }
        adapter = new SimpleAdapter(getActivity(),
                dataList,
                R.layout.drawer_list_item,
                new String[]{"img", "function"},
                new int[]{R.id.img, R.id.function});
        listView.setAdapter(adapter);
    }
    private String getQueryData() {

        StringBuilder sb = new StringBuilder();// ���ڱ����ַ�
        ContentResolver resolver = getActivity().getContentResolver();// ���ContentResolver����
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);// ��ѯ��¼
        while (cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndex(columns[0]);// ���IDֵ������
            int displayNameIndex = cursor.getColumnIndex(columns[1]);// �����������
            int id = cursor.getInt(idIndex);// ���id
            String displayName = cursor.getString(displayNameIndex);// ������
            displayName= String.valueOf(getPinYin(displayName));
            Cursor phone = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, columns[3] + "=" + id, null, null);
            while (phone.moveToNext()) {

                int phoneNumberIndex = phone.getColumnIndex(columns[2]);// ��õ绰����
                String phoneNumber = phone.getString(phoneNumberIndex);// ��õ绰
                sb.append(displayName + ": " + phoneNumber + "\n");// �������
            }
        }
        cursor.close();// �ر��α�
        return sb.toString();
    }
    /**
     * 汉字转换拼音，字母原样返回，都转换为小写
     * created by adg 15/7/27
     * 由于汉字具有多音字情况，所以返回的名字拼音是数组
     *
     * @param input
     * @return
     */
    public static ArrayList<String> getPinYin(@Nullable String input) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        //设置转化后字符的大小写
        //HanyuPinyinCaseType.UPPERCASE 转化为大写
        //HanyuPinyinCaseType.LOWERCASE 转化为小写
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);//小写
        //设置转化后音调的表示方式
        //HanyuPinyinToneType.WITH_TONE_MARK 带音调 dǎ
        //HanyuPinyinToneType.WITH_TONE_NUMBER 以数字代表音调 da3
        //HanyuPinyinToneType.WITHOUT_TONE 不带音调 da
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        //设置转化后ü这个辅音的呈现形式
        //HanyuPinyinVCharType.WITH_U_AND_COLON 表现为 u:
        //HanyuPinyinVCharType.WITH_U_UNICODE 表现为 ü
        //HanyuPinyinVCharType.WITH_V 表现为v
        //因为存在多音字，变音字的存在，所以把一个名字转成拼音的情况全部都保存下来然后返回。
        ArrayList<String> nameList = new ArrayList<String>();
        Map<String, ArrayList<String>> cacheNameList = new HashMap<String, ArrayList<String>>();
        /**
         * 下面这个循环是找到这个名字的所有拼音
         */
        for (int i = 0; i < input.length(); i++) {
            //名字里面的单个字符
            char c = input.charAt(i);
            String[] vals = new String[0];
            //判断是不是汉字字符
            if (checkChar(c)) {
                try {
                    vals = PinyinHelper.toHanyuPinyinStringArray(c, format);
                    //去重，例如wang2和wang4，无音调均是wang，只要保留一个即可
                    //例如单有dan1和shan4，无音调是都要保留的
                    ArrayList<String> list = new ArrayList<String>();
                    for (int j = 0; j < vals.length; j++) {
                        if (!list.contains(vals[j]))
                            list.add(vals[j]);
                    }
                    cacheNameList.put(String.valueOf(c), list);
                } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                    badHanyuPinyinOutputFormatCombination.printStackTrace();
                }
                LogUtil.d("", Arrays.toString(vals));
            } else {
                ArrayList<String> list = new ArrayList<String>();
                list.add(String.valueOf(c));
                cacheNameList.put(String.valueOf(c), list);
            }
        }
        /**
         * 下面这个循环是找到这个名字的所有字的拼音所有组合情况
         */
        for (int j = 0; j < input.length(); j++) {
            //名字里面的单个字符
            char c = input.charAt(j);
            ArrayList<String> oneName = cacheNameList.get(String.valueOf(c));
            if (j == 0) {
                nameList = oneName;
            } else {
                ArrayList<String> cacheName = new ArrayList<String>();
                for (int i = 0; i < nameList.size(); i++) {
                    for (int k = 0; k < oneName.size(); k++) {
                        cacheName.add(nameList.get(i) + oneName.get(k));
                    }
                }
                nameList = cacheName;
            }
        }

        return nameList;
    }

    /**
     * 判断是不是中文字符
     * crated by adg 15/07/28
     *
     * @param oneChar
     * @return
     */
    public static boolean checkChar(char oneChar) {
        if ((oneChar >= '\u4e00' && oneChar <= '\u9fa5')
                || (oneChar >= '\uf900' && oneChar <= '\ufa2d')) {
            return true;
        }
        return false;
    }

}
