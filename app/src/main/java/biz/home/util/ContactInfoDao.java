package biz.home.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.util.Log;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import biz.home.model.ContactInfo;

/**
 * Created by admin on 2015/8/19.
 */
public class ContactInfoDao {
    private DBOpenHelper helper;
    private SQLiteDatabase db;
    public ContactInfoDao(Context context){
        helper=new DBOpenHelper(context);
    }
    public void add(ContactInfo ci){
        db=helper.getWritableDatabase();
        String sql="insert into tb_contactInfo (tel,name,namePinyin) values(?,?,?)";
        if(ci.getNamePinyin()==null){
            db.execSQL(sql,new Object[]{ci.getTel(), ci.getName(),getPinYin(ci.getName()).get(0)});
        }else{
            db.execSQL(sql,new Object[]{ci.getTel(), ci.getName(),ci.getNamePinyin()});
        }

    }
    public ContactInfo[] findAll(){
        ContactInfo[] cis=new ContactInfo[500];
        db=helper.getWritableDatabase();
        String sql="select tel,name,namePinyin from tb_contactInfo";
        Cursor cursor=db.rawQuery(sql,null);
        int i=0;
        while(cursor.moveToNext()){
            cis[i]= new ContactInfo(cursor.getString(cursor.getColumnIndex("tel")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("namePinyin")));
//            cis[i].setTel(cursor.getString(cursor.getColumnIndex("tel")));
//            cis[i].setName(cursor.getString(cursor.getColumnIndex("name")));
//            cis[i].setNamePinyin(cursor.getString(cursor.getColumnIndex("namePinyin")));
            i++;
        }
        cursor.close();
        db.close();

        if(i==0){
            return null;
        }else {
            return cis;
        }
    }
    public void update(ContactInfo ci){
        db=helper.getWritableDatabase();
        String sql="update tb_contactInfo set tel=?,name=?,namePinyin=? where tel=?";
        db.execSQL(sql,new Object[]{ci.getTel(),ci.getName(),getPinYin(ci.getName()).get(0),ci.getTel()});
    }
    //根据电话号码查找联系人
    public ContactInfo find(String tel){
        ContactInfo ci=new ContactInfo();
        db=helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select tel,name,namePinyin from tb_contactInfo where tel='"+tel+"'", null);
        if(cursor.moveToNext()){
            ci= new ContactInfo(cursor.getString(cursor.getColumnIndex("tel")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("namePinyin")));
            cursor.close();
            db.close();
            return ci;
        }else{
            return null;
        }

    }
    //删除某些不用的电话号码
    public void delete(String...tels){
        if(tels.length>0){
            StringBuffer sb=new StringBuffer();
            for(int i=0;i<tels.length;i++){
                sb.append('?').append(',');
            }
            sb.deleteCharAt(sb.length()-1);
            db=helper.getWritableDatabase();
            db.execSQL("delete from tb_contactInfo where tel in ("+sb+")",(Object[])tels);
        }
    }
    /**
     * 获取记录总数
     */
    public int getCount(){
        db=helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select count(tel) from tb_contactInfo",null);
        if(cursor.moveToNext()){
            return cursor.getInt(0);
        }
        return 0;
    }
    /**
     * 根据姓名来匹配对应的联系人信息
     */
    public ContactInfo[] match(String name){
        ContactInfo[] cis=new ContactInfo[5];
//        ContactInfo[] cis={new ContactInfo(),new ContactInfo(),new ContactInfo(),new ContactInfo(),new ContactInfo()};
        String namePinyin=getPinYin(name).get(0);
        Log.i("", "match :name:" + namePinyin);
        String sql="select tel,name,namePinyin from tb_contactInfo where namePinyin like '%"+namePinyin+"%'";
        Log.i("", "match sql:"+sql);
        db=helper.getWritableDatabase();
        Cursor cursor=db.rawQuery(sql,null);
        int i=0;
        while(cursor.moveToNext()){
            if(i<=4){
                cis[i]= new ContactInfo(cursor.getString(cursor.getColumnIndex("tel")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("namePinyin")));
//                cis[i].setTel(cursor.getString(cursor.getColumnIndex("tel")));
//                cis[i].setName(cursor.getString(cursor.getColumnIndex("name")));
//                cis[i].setNamePinyin(cursor.getString(cursor.getColumnIndex("namePinyin")));

//                Log.i("", "match :"+cis[i].getTel());
            }
            i++;

        }
        Log.i("ContactInfoDao", "match i："+i);
        cursor.close();
        db.close();
        if(i==0){
            return null;
        }else{
            return cis;
        }
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
