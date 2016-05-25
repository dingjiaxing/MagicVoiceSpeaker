package biz.home.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import biz.home.InputViewActivity;
import biz.home.R;
import biz.home.SpeakToitActivity;
import biz.home.bean.MagicResultResourceInfo;

/**
 * Created by adg on 2015/7/14.
 * fragment
 */
public class MainEditFragment extends Fragment {

    public static final String TOP_TEXT_SHOW = "MainEditFragment_TopShow";
    public static final String CENTER_TEXT_SHOW = "MainEditFragment_CenterShow";
    public static final String TITLE = "MainEditFragment_title";
    public static final String IS_GET_STATE = "isGetState";
    public static final String IS_NOT_DEFAULT = "isNotDefault";
    public static final String QUESTION = "question";
    public static final String TEXT_SHOW = "text_show";
    public static final String URL = "url";
    public static final String RESOURCEINFO="resource_info";
    private static final String TAG = "MainEditFragment";
    public static final String EXIT_SCENE_TEXT = "MainEditFragment_exit_scene_string";
    /**
     * 记录当前状态,此处不能用static关键词，因此static不会被清空，每次使用的时候可能会对下次记载进行干扰
     */
    private int BUTTON_STATE = 0;                           //按钮初始状态
    /**
     * 返回类型1一般文本交互
     */
    public static final int BUTTON_DEFAULT = 1500;      //文本交互
    /**
     * 返回类型3 打电话
     */
    public static final int BUTTON_CALL_PHONE = 1501;   //打电话
    /**
     * 返回类型2一般文本交互，用户选择
     */
    public static final int BUTTON_DETAIL = 1502;       //用户点击按钮确认
    /**
     * 首次进入
     */
    public static final int BUTTON_YES = 1503;          //首次进入
    /**
     * 返回类型4一般用于发短信
     */
    public static final int BUTTON_SEND_MESSAGE = 1504;  //发短信
    /**
     * 返回类型5网上需找资源如，天气查询，百度地图
     */
    public static final int BUTTON_SEND_HTTP = 1505;    //网上查找需要资源
    /**
     * 返回类型3 打电话或者发短信
     */
    public static final int BUTTON_D_CALL = 1506;        //打电话或者发短信

    public static final int EXIT_SCENE = 1507;        //打电话或者发短信

    private RelativeLayout fTextRL, fContentRl;         //fTextRL为下半部分可编辑区域，fContentRL为整个区域
    private boolean isNotDefault = false;
    private static Integer TextDefaultHeight = null;
    private static Integer RLDefaultHeight = null;

    private OnActivityListener mOnActivityListener;
    /**
     * 页面控件
     */
    public TextView topShow, centerShow, bottomShow, agreement;    //topShow为机器人所说的话，centenrShow为用户所说的话
                                            //bottomShow为“用户可编辑”几个字，agreement为协议内容
    private Button chickDetail, nextMessage;           //chickDetail为“查看详情”按钮，nextMessage为“下一条”信息按钮

    private OnChickButton chickButtonListener;
    private OnNextMessageButton nextMessageButtonListener;
    private OnExitScene exitSceneListener;
    private  String url;
    private String question;
    private String textShow;

    /**
     * 和退出场景相关的变量
     */
    private String exitSceneString;
    private LinearLayout exit_scene_linear;
    private ImageView exit_scene_icon;
    private TextView exit_scene_text;

    public static MainEditFragment newInstance() {
        MainEditFragment fragment = new MainEditFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(IS_GET_STATE, BUTTON_DEFAULT);
        bundle.putBoolean(IS_NOT_DEFAULT, true);
        bundle.putString(TITLE, "");
        fragment.setArguments(bundle);
        return fragment;
    }

    public static MainEditFragment newInstance(@Nullable String topText, @Nullable String centerText, @Nullable int buttonState) {
        MainEditFragment fragment = new MainEditFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(IS_GET_STATE, buttonState);
        bundle.putBoolean(IS_NOT_DEFAULT, false);
        bundle.putString(TOP_TEXT_SHOW, topText);
        bundle.putString(TITLE, "");
        bundle.putString(CENTER_TEXT_SHOW, centerText);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static MainEditFragment newInstance(@Nullable String title, @Nullable String topText, @Nullable String centerText, @Nullable int buttonState) {
        MainEditFragment fragment = new MainEditFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(IS_GET_STATE, buttonState);
        bundle.putBoolean(IS_NOT_DEFAULT, false);
        bundle.putString(TOP_TEXT_SHOW, topText);
        bundle.putString(CENTER_TEXT_SHOW, centerText);
        bundle.putString(TITLE, title);
        fragment.setArguments(bundle);
        return fragment;
    }
    //含有退出场景的按钮
    public static MainEditFragment newInstance(@Nullable String title, @Nullable String topText, @Nullable String centerText, @Nullable int buttonState,@Nullable String exitSceneString) {
        MainEditFragment fragment = new MainEditFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(IS_GET_STATE, buttonState);
        bundle.putBoolean(IS_NOT_DEFAULT, false);
        bundle.putString(TOP_TEXT_SHOW, topText);
        bundle.putString(CENTER_TEXT_SHOW, centerText);
        bundle.putString(TITLE, title);
        Log.i(TAG, "newInstance exitSceneString:" + exitSceneString);
        bundle.putString(EXIT_SCENE_TEXT, exitSceneString);
        fragment.setArguments(bundle);
        return fragment;
    }
    //主要是资源和问答有关的环节
    public static MainEditFragment newInstance(@Nullable String title, @Nullable String topText, @Nullable String centerText, @Nullable int buttonState, String url0, String question0, String textShow0, MagicResultResourceInfo resourceInfo) {
//        url=url0;
//        question=question0;
//        textShow=textShow0;
        MainEditFragment fragment = new MainEditFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(RESOURCEINFO,resourceInfo);
        bundle.putString(URL,url0);
        bundle.putString(QUESTION, question0);
        bundle.putString(TEXT_SHOW, textShow0);
        bundle.putInt(IS_GET_STATE, buttonState);
        bundle.putBoolean(IS_NOT_DEFAULT, false);
        bundle.putString(TOP_TEXT_SHOW, topText);
        bundle.putString(CENTER_TEXT_SHOW, centerText);
        bundle.putString(TITLE, title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mOnActivityListener = (OnActivityListener) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(activity instanceof  OnChickButton){
            chickButtonListener=(OnChickButton)activity;

        }else{
            try {
                throw new Exception("activityy should implements OnButtonClick Interface");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(activity instanceof  OnNextMessageButton){
            nextMessageButtonListener=(OnNextMessageButton)activity;

        }else{
            try {
                throw new Exception("activityy should implements OnButtonClick Interface");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(activity instanceof OnExitScene){
            exitSceneListener= (OnExitScene) activity;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
//        if(getArguments().getInt(IS_GET_STATE)==BUTTON_DEFAULT){
//            View view = inflater.inflate(R.layout.activity_main_edit, container, false);
//        }else {
//            View view = inflater.inflate(R.layout.activity_main_edit_2, container, false);
//        }
//        View view = inflater.inflate(R.layout.activity_main_edit_2, container, false);
        View view = inflater.inflate(R.layout.activity_main_edit, container, false);
        finById(view);
        setOnClick();
        StateSelected();
        return view;
    }

    private void StateSelected() {
        BUTTON_STATE = getArguments().getInt(IS_GET_STATE);
        switch (BUTTON_STATE) {
            case BUTTON_CALL_PHONE:
                chickDetail.setVisibility(View.INVISIBLE);
                nextMessage.setVisibility(View.INVISIBLE);
                break;
            case BUTTON_DEFAULT:
                chickDetail.setVisibility(View.INVISIBLE);
                nextMessage.setVisibility(View.INVISIBLE);
                exit_scene_linear.setVisibility(View.GONE);
                agreement.setText(getArguments().getString(TITLE));
                break;
            case BUTTON_DETAIL:
                exit_scene_linear.setVisibility(View.GONE);
                chickDetail.setText(getResources().getString(R.string.button_detail));
                nextMessage.setText(getResources().getString(R.string.button_next));
                agreement.setText(getArguments().getString(TITLE));
                break;
            case BUTTON_SEND_MESSAGE:
                chickDetail.setVisibility(View.INVISIBLE);
                nextMessage.setVisibility(View.INVISIBLE);
                break;
            case BUTTON_YES:
                chickDetail.setText(getResources().getString(R.string.button_yes));
                nextMessage.setText(getResources().getString(R.string.button_no));
                agreement.setText(Html.fromHtml(getResources().getString(R.string.agreement)));
                agreement.setClickable(true);
                break;
            case BUTTON_D_CALL:
                chickDetail.setVisibility(View.INVISIBLE);
                nextMessage.setVisibility(View.INVISIBLE);
                break;
            case EXIT_SCENE:
                chickDetail.setVisibility(View.INVISIBLE);
                nextMessage.setVisibility(View.INVISIBLE);
                String str=getArguments().getString(EXIT_SCENE_TEXT);
                Log.i(TAG, "StateSelected 退出场景按钮上的文字str:"+str);
                exit_scene_text.setText(str);
                break;
            default:
                break;
        }
        topShow.setText(getArguments().getString(TOP_TEXT_SHOW));
        centerShow.setText(getArguments().getString(CENTER_TEXT_SHOW));
//        int num=topShow.length();
//        System.out.println("字数： " + num);
//        num=num/22;     //每行约为 26 个字，求出总行数 num
//        if(num>=4){
//            SetTextHeight();
//        }
    }

    private void finById(View view) {
        fTextRL = (RelativeLayout) view.findViewById(R.id.user_edit_layout);
        fContentRl = (RelativeLayout) view.findViewById(R.id.edit_content);
        topShow = (TextView) view.findViewById(R.id.speak_top_show);
        centerShow = (TextView) view.findViewById(R.id.speak_center_show);
        bottomShow = (TextView) view.findViewById(R.id.speak_bottom_show);
        chickDetail = (Button) view.findViewById(R.id.button_deatil);
        nextMessage = (Button) view.findViewById(R.id.button_next);
        agreement = (TextView) view.findViewById(R.id.agreement);

        exit_scene_linear= (LinearLayout) view.findViewById(R.id.main_edit_exit_scene_liner);
        exit_scene_icon= (ImageView) view.findViewById(R.id.main_edit_exit_scene_icon);
        exit_scene_text= (TextView) view.findViewById(R.id.main_edit_exit_scene_text);
//        topShow.setMovementMethod(ScrollingMovementMethod.getInstance());
        centerShow.setMovementMethod(ScrollingMovementMethod.getInstance());
//        if(getArguments().getInt(IS_GET_STATE)==BUTTON_DEFAULT){
//            RelativeLayout rl= (RelativeLayout) view.findViewById(R.id.user_edit_layout);
//            int h=rl.getHeight();
//            RelativeLayout.LayoutParams r=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (1.3*h));
//            r.setMargins();
//            rl.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (1.3*h)));
//
//        }

    }

    private void setOnClick() {
        exit_scene_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //用户点击了“退出场景”
//                getActivity();
                exitSceneListener.onExitScene(true);
            }
        });
        isNotDefault = getArguments().getBoolean(IS_NOT_DEFAULT);
        final ViewTreeObserver fTextRLObserver = fContentRl.getViewTreeObserver();
//        fTextRLObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                if (RLDefaultHeight == null) {
//                    RLDefaultHeight = fTextRL.getMeasuredHeight();
//                    TextDefaultHeight = topShow.getHeight();
//                }
//                RelativeLayout.LayoutParams RLP = (RelativeLayout.LayoutParams) topShow.getLayoutParams();
//                if (isNotDefault) {
//                    RLP.height += RLDefaultHeight;
//                    if (isAdded()) {
//                        topShow.setText(getResources().getString(R.string.good_morning));
//                        topShow.setTextSize(40);
//                    }
//                } else {
//                    RLP.height = TextDefaultHeight;
//                }
//                topShow.setLayoutParams(RLP);
//                fContentRl.getViewTreeObserver().removeOnPreDrawListener(this);
//                return true;
//            }
//        });
//        agreement.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!TextUtils.isEmpty(agreement.getText())) {
//                    mOnActivityListener.ShowAgreement();
//                }
//            }
//        });

//        chickDetail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mOnActivityListener.setChickDetailOnClick(BUTTON_STATE);
//
//            }
//        });
        chickDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chickButtonListener != null) {
                    url=getArguments().getString(URL);
                    textShow=getArguments().getString(TEXT_SHOW);
                    MagicResultResourceInfo resourceInfo=(MagicResultResourceInfo)getArguments().getSerializable(RESOURCEINFO);
                    chickButtonListener.onChickButtonClick(resourceInfo);
                }
            }
        });
        nextMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nextMessageButtonListener!=null){
                    question=getArguments().getString(QUESTION);
                    nextMessageButtonListener.onNextMessageButtonClick(question);
                }
            }
        });
//        nextMessage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mOnActivityListener.setNextMessageOnClick(BUTTON_STATE);
//            }
//        });
        bottomShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*mOnActivityListener.onInputView(topShow.getText().toString(), centerShow.getText().toString());*/
//                Intent intent = new Intent();
//                intent.putExtra("tst", centerShow.getText());
//                intent.setClass(getActivity().getApplication(), InputViewActivity.class);
//                MainEditFragment.this.startActivity(intent);
                SpeakToitActivity speakToitActivity=(SpeakToitActivity)getActivity();
                speakToitActivity.cancel();

                //Intent intent =getActivity().getIntent();
                //intent.setClass(getActivity(), InputViewActivity.class);
                Intent intent=new Intent(getActivity(),InputViewActivity.class);
                intent.putExtra(InputViewActivity.TOP_INFO,topShow.getText().toString());
                intent.putExtra("tst", centerShow.getText().toString());
                getActivity().startActivityForResult(intent, InputViewActivity.REQUEST_CODE);
            }
        });
        centerShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpeakToitActivity speakToitActivity=(SpeakToitActivity)getActivity();
                speakToitActivity.cancel();

                //Intent intent =getActivity().getIntent();
                //intent.setClass(getActivity(), InputViewActivity.class);
                Intent intent=new Intent(getActivity(),InputViewActivity.class);
                intent.putExtra(InputViewActivity.TOP_INFO, topShow.getText().toString());
                intent.putExtra("tst", centerShow.getText().toString());
                getActivity().startActivityForResult(intent, InputViewActivity.REQUEST_CODE);
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public interface OnActivityListener {
        void onInputView(String topShow, String bottomShow);

        void setChickDetailOnClick(int state);

        void setNextMessageOnClick(int state);

        void ShowAgreement();

    }

    public void DefaultTextHeight() {
        if (isNotDefault) {
            RelativeLayout.LayoutParams RLP = (RelativeLayout.LayoutParams) topShow.getLayoutParams();
            RLP.height = TextDefaultHeight;
            topShow.setLayoutParams(RLP);
        }
    }

    public void SetTextHeight() {
//        if (isNotDefault) {
            RelativeLayout.LayoutParams RLP = (RelativeLayout.LayoutParams) topShow.getLayoutParams();
//            RLP.height += RLDefaultHeight;
            RLP.height += RLP.height;
            RLP.topMargin=550;
            topShow.setLayoutParams(RLP);
//        }
    }

    /**
     *
     * @param n 表示 行数
     */
    public void SetTextHeight(int n) {
//        if (isNotDefault) {
        n-=4;
        RelativeLayout.LayoutParams RLP = (RelativeLayout.LayoutParams) topShow.getLayoutParams();
//            RLP.height += RLDefaultHeight;
        RLP.height += n/4*RLP.height;
//        RLP.topMargin=550;
        topShow.setLayoutParams(RLP);
//        }
    }

    public interface OnChickButton{
        public void onChickButtonClick(MagicResultResourceInfo resourceInfo);
    }
    public interface  OnNextMessageButton{
        public void onNextMessageButtonClick(String again);
    }
    public interface OnExitScene{
        public void onExitScene(Boolean b);
    }

    public TextView getTopShow() {
        return topShow;
    }

    public void setTopShow(TextView topShow) {
        this.topShow = topShow;
//        int num=topShow.length();
//        System.out.println("字数： " +num );
//        num=num/22;     //每行约为 26 个字，求出总行数 num
//        if(num>=4){
//            SetTextHeight();
//        }
    }

    public TextView getCenterShow() {
        return centerShow;
    }

    public void setCenterShow(TextView centerShow) {
        this.centerShow = centerShow;
    }
    public Boolean getButtonState(){
        BUTTON_STATE = getArguments().getInt(IS_GET_STATE);
        if(BUTTON_STATE==BUTTON_DEFAULT){
            return true;
        }
        return false;
    }
}
