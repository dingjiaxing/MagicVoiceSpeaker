package biz.home.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import biz.home.R;
import biz.home.SpeakToitActivity;
import biz.home.assistActivity.ProjectContentChangeActivity;

/**
 * Created by admin on 2015/9/4.
 */
public class MainProjectFragment extends Fragment {
    public ProgressBar progressBar;
    public static final String TITLE="Project_Title";       //静态常量，传送资源信息的title的key
    public static final String CONTENT="Project_Content";   //静态常量，传送资源信息的content的key
    public static final int CHANGE_TITLE=1000;              //修改过资源信息的标题 的 返回码
    public static final int CHANGE_CONTENT=1001;            //修改过资源信息的内容 的返回码
    private static final String TAG ="MainProjectFragment";

    public TextView title;          //标题、内容的文本控件
    private TextView content;
    public Button confirm,change;                  //确定发布的按钮
    private onSendProjectInformation sendProjectListener;           //监听器，当用户点击了发布按钮之后，SpeakToitActivity因实现了该接口而可以做出反应
    public MainProjectFragment() {      //空 的 构造函数

    }
    //MainProjectFragment的实例化函数
    public static MainProjectFragment newInstance(@Nullable String title,@Nullable String content){
        MainProjectFragment fragment=new MainProjectFragment();
        Bundle bundle=new Bundle();
        bundle.putString(TITLE,title);
        bundle.putString(CONTENT,content);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {       //联系函数，如果被联系的activity是接口onSendProjectInformation的实力的话，给监听器赋初值
        super.onAttach(activity);
        if(activity instanceof onSendProjectInformation){
            sendProjectListener=(onSendProjectInformation)activity;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_main_project, container, false);
        findById(view);
        setOnClick(view);
        return view;
    }
    public void findById(View view){
        title= (TextView) view.findViewById(R.id.project2_title);
        content=(TextView)view.findViewById(R.id.project2_content);
        confirm=(Button)view.findViewById(R.id.project2_confirm);
        change=(Button)view.findViewById(R.id.project2_change_content_button);
        progressBar=(ProgressBar)view.findViewById(R.id.project_progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        String title0=getArguments().getString(TITLE);
        String content0=getArguments().getString(CONTENT);
        title.setText(title0);
        title.requestFocus();
        content.setText(content0);
        if(SpeakToitActivity.pics!=null){
            displayPicOnText(content, SpeakToitActivity.pics);
        }
    }
    public void setOnClick(View view){      //对界面上的内容进行监听
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(sendProjectListener!=null)
            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProjectContentChangeActivity.class);
                intent.putExtra(CONTENT, content.getText().toString());
                startActivityForResult(intent, CHANGE_CONTENT);
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sendProjectListener != null) {
                    sendProjectListener.OnSendProjectButtonClick(title.getText().toString(), content.getText().toString(),progressBar);
                }
            }
        });
    }

    //打开其他Activity对资源内容进行修改的时候，根据返回码resultCode判断返回类型，并处理返回值
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("onActivityResult");
        switch (resultCode){
            case CHANGE_TITLE:
                break;
            case CHANGE_CONTENT:
                System.out.println("resultCode" +resultCode );
                if(data.hasExtra(CONTENT)){
                    String content0=data.getStringExtra(CONTENT);
                    System.out.println("content0" + content0);
                    content.setText(content0);
                    System.out.println("content.getText().toString():" + content.getText().toString());
//                    Map<String ,String>  pics= SpeakToitActivity.pics;
//                    for(String key:pics.keySet()){
//                        System.out.println("fragemnt key:" + key);
//                    }
//                    displayPicOnText(content, SpeakToitActivity.pics);
                }
                break;
        }
    }

    /**
     * 自定义接口，用于当用户点击“确认发布”按钮后，SpeakToitActivity做出反应
     */
    public interface onSendProjectInformation{
        public void OnSendProjectButtonClick(String title,String content,ProgressBar progressBar);
    }
    private void displayPicOnText(TextView editText2,Map<String,String> map){
        //正则表达式为  ^\[pic\d{1}\]$
//        String regex="^\\[pic\\d{1}\\]$";
        SpannableString mSpan1;
        Map<String,String> map2=new HashMap<String,String>();
        int num=1;
        String text="";
        text=editText2.getText().toString();
        System.out.println("text:" +text );
//        text=text.replaceAll("\\[pic\\d{1}\\]", "PICCCC");
        String[] texts=text.split("\\-pic\\d{1}\\-");
//        text
        for(int i=0;i<texts.length;i++){
            Log.i(TAG, "displayPicOnText texts["+i+"]:"+texts[i]);
        }

        SpannableString[] spanStrings=new SpannableString[10];
        int startIndex=0;
//        startIndexs[0]=-6;
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        for (String key : map.keySet()) {
//            System.out.println("key= "+ key + " and value= " + map.get(key));
            String value=map.get(key);
//            Log.i(TAG, "displayPicOnText key:"+key+";value"+value);
            System.out.println("" + "displayPicOnText key:" + key + ";value" + value);
            int [] list=new int [10];

            if(editText2.getText().toString().contains(key)){
                //map2为去掉了用户插入图片后又删除图片 后 的map数组
                map2.put(key,value);
                startIndex=text.indexOf(key);
                Bitmap thumbnailBitmap = BitmapFactory.decodeFile(value);
                if(thumbnailBitmap == null)
                    return;
                builder.setSpan(
                        new ImageSpan(getActivity(),thumbnailBitmap), startIndex, startIndex+6
                                , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

//                startIndex=text.indexOf(key);
//                Bitmap thumbnailBitmap = BitmapFactory.decodeFile(value);
//                if(thumbnailBitmap == null)
//                    return;
//                ImageSpan imgSpan =new ImageSpan(getActivity(),thumbnailBitmap);
//                spanStrings[num] = new SpannableString(key);
//                list[num]=startIndex;
//                spanStrings[num].setSpan(imgSpan, 0, 6, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//                num++;
            }
        }
        map=map2;
//        SpeakToitActivity.pics=map2;
        editText2.setText(builder);
//        editText2.setText("");
//        editText2.append(texts[0]);
//        for(int i=1;i<num;i++){
//            Log.i("MainProjectFragment", "displayPicOnText i:" + i);
//            System.out.println("" + texts[i - 1]);
//            System.out.println("" + spanStrings[i]);
//
//            editText2.append(spanStrings[i]);
//            editText2.append(texts[i]);
//        }
//        editText2.append("后一段");
    }
}
