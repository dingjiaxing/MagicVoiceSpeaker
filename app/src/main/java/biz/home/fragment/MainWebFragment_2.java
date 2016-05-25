package biz.home.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import biz.home.InputViewActivity;
import biz.home.R;
import biz.home.util.ColorChangeUtil;
import biz.home.util.LogUtil;

/**
 * Created by adg on 2015/7/14.
 */
public class MainWebFragment_2 extends Fragment {
    private static final String TAG = "MainWebFragment";
    public static final String URL = "MainWebFragment_url";
    public static final String TEXT_SHOW = "MainWebFragment_Text";
    private static final String IS_GET_Data = "isGetData";
    public static final String WEB_QUESTION = "MainWebFragment_Question";
    private String url = null;
    //Text高度
    private static Integer webTextHeight = null;
    private static Integer webDefaultHeight = null;
    private static Integer web2Height = null;
    //触摸指示图片的触摸点的初始坐标
    private static Integer touchX = null;
    private static Integer touchY = null;

    public WebView mainWEb;
    private ImageView webDirect, webDirect1, broUp, broDown, broSelect, broShare;
    public  TextView webText,webClickEdit,webQuestion;
    //进度条
    private ProgressBar progressbar;

    public static MainWebFragment_2 newInstance() {
        MainWebFragment_2 fragment = new MainWebFragment_2();
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_GET_Data, false);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static MainWebFragment_2 newInstance(String url) {
        MainWebFragment_2 fragment = new MainWebFragment_2();
        Bundle bundle = new Bundle();
        bundle.putString(URL, url);
        bundle.putString(TEXT_SHOW, "");
        bundle.putBoolean(IS_GET_Data, true);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static MainWebFragment_2 newInstance(String url, String textShow) {
        MainWebFragment_2 fragment = new MainWebFragment_2();
        Bundle bundle = new Bundle();
        bundle.putString(URL, url);
        bundle.putString(TEXT_SHOW, textShow);
        bundle.putBoolean(IS_GET_Data, true);
        fragment.setArguments(bundle);
        return fragment;
    }
    public static MainWebFragment_2 newInstance(String url, String textShow,String question) {
        MainWebFragment_2 fragment = new MainWebFragment_2();
        Bundle bundle = new Bundle();
        bundle.putString(URL, url);
        bundle.putString(TEXT_SHOW, textShow);
        bundle.putBoolean(IS_GET_Data, true);
        bundle.putString(WEB_QUESTION, question);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_main_web, container, false);
        findById(view);
        setWebDirect(view);
        boolean isGetData = getArguments().getBoolean(IS_GET_Data);
        if (isGetData) {
            if (!TextUtils.isEmpty(getArguments().getString(TEXT_SHOW))) {
                String textShow = getArguments().getString(TEXT_SHOW);
                webText.setText(textShow);
            }

            String question=getArguments().getString(WEB_QUESTION);
            if(question!=null&&!question.equals("")){
                webQuestion.setText(question);
            }
            url = getArguments().getString(URL);
            mainWEb.loadUrl(url);
        }
        webText.requestFocus();//滚动文字
        return view;

    }

    private void setImgColor(ImageView imageView, Integer id) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), id);
        bitmap = ColorChangeUtil.changeImageColor(bitmap, getResources().getColor(R.color.bro_bar_bg));
        imageView.setImageDrawable(new BitmapDrawable(null, bitmap));
    }

    /**
     * 常规的初始化操作
     *
     * @param view
     */
    private void findById(View view) {
        broDown = (ImageView) view.findViewById(R.id.bro_go_down);      //后退按钮
        broUp = (ImageView) view.findViewById(R.id.bro_go_up);          //前进按钮
        broShare = (ImageView) view.findViewById(R.id.bro_share);       //分享按钮
        broSelect = (ImageView) view.findViewById(R.id.bro_select_bro);     //转发按钮
//        setImgColor(broDown, R.drawable.selector_brower_menu_n_01);             //设置图片
//        setImgColor(broUp, R.drawable.browser_menu_n_02);
//        setImgColor(broSelect, R.drawable.browser_menu_n_03);
//        setImgColor(broShare, R.drawable.browser_menu_n_04);
        mainWEb = (WebView) view.findViewById(R.id.assets_main_web);    //浏览器控件
        progressbar=(ProgressBar)view.findViewById(R.id.progressBarStyleHorizontal);        //进度条
//        progressbar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleHorizontal);
//        progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 3, 0, 0));
        //webView基本设置
        mainWEb.getSettings().setJavaScriptEnabled(true);
        //设置 缓存模式
        mainWEb.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        mainWEb.getSettings().setDomStorageEnabled(true);

        broUp.setOnClickListener(new View.OnClickListener() {       //前进按钮 效果实现
            @Override
            public void onClick(View view) {
                if (mainWEb.canGoForward()) {
                    mainWEb.goForward();
                }
            }
        });
        broDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {            //后退按钮效果实现
                if (mainWEb.canGoBack()) {
                    mainWEb.goBack();
                }
            }
        });
        broShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {           //分享按钮效果实现
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_SUBJECT, "共享链接");
                intent.putExtra(Intent.EXTRA_TEXT, mainWEb.getUrl());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, "共享链接"));
            }
        });
        broSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {            //转发按钮效果实现
                LogUtil.d(TAG, url);
                if (!TextUtils.isEmpty(url)) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(url);
                    intent.setData(content_url);
                    startActivity(intent);
                }
            }
        });
        mainWEb.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {      //为添加进度条而添加的代码
//                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    progressbar.setVisibility(View.GONE);
                } else {
                    if (progressbar.getVisibility() == View.GONE)
                        progressbar.setVisibility(View.VISIBLE);
                    progressbar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        mainWEb.setWebViewClient(new WebViewClient() {          //重写浏览器的函数，定向加载页面
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.startsWith("tel:")) {
                    //弹出打电话界面，需要拨号的号码已经显示出来，点击即可拨打次电话
//                    Intent intent = new Intent(Intent.ACTION_VIEW,
//                            Uri.parse(url));
//                    startActivity(intent);

                    //直接拨打电话
                    Intent intent=new Intent();
                    intent.setAction(Intent.ACTION_CALL);
//                    intent.setData(Uri.parse("tel:" + url));
                    intent.setData(Uri.parse(url));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(intent);
                } else {
                    view.loadUrl(url);
                }

                return true;
            }
        });

    }

    /**
     * webDirect的基本设置
     */
    private void setWebDirect(View view) {
        mainWEb = (WebView) view.findViewById(R.id.assets_main_web);            //网页内容
        webDirect = (ImageView) view.findViewById(R.id.assets_web_direct);      //隐藏文本、打开文本域的箭头  所在的横向矩形
        webDirect1 = (ImageView) view.findViewById(R.id.assets_web_direct_1);   //隐藏文本、打开文本域的箭头
        webText = (TextView) view.findViewById(R.id.assets_web_text_show);      //网页下面显示的文本信息
        webClickEdit=(TextView)view.findViewById(R.id.web_click_edit);          //“点击以编辑”的文本
        webQuestion=(TextView)view.findViewById(R.id.web_question);
        mainWEb.getSettings().setSupportZoom(true);                             //使浏览器支持缩放
        mainWEb.getSettings().setBuiltInZoomControls(true);                     //设置显示缩放按钮
        mainWEb.getSettings().setJavaScriptEnabled(true);                       //使浏览器 支持JavaScript功能

        webDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout.LayoutParams webLP = (LinearLayout.LayoutParams) mainWEb.getLayoutParams();
                if (webTextHeight == null) {
                    webTextHeight = webText.getHeight();
                    webDefaultHeight = mainWEb.getHeight();
                    web2Height = webTextHeight + webDefaultHeight;
                }
                if ((webLP.height >= webDefaultHeight + 0.5 * webTextHeight &&
                        webLP.height < web2Height) | webLP.height == webDefaultHeight) {
//                    webLP.height = web2Height;
                    webLP.height = webDefaultHeight - (2 * webTextHeight);
                } else {
                    webLP.height = webDefaultHeight;
                }
                if (webLP.height == webDefaultHeight) {
                    webDirect1.getBackground().setLevel(90);

                    changeTextshowToTitle();        //将显示文本改回为标题
                } else {
                    webDirect1.getBackground().setLevel(10);

                    changeTextshowToQuestion();         //将显示文本改为问题
                }
                mainWEb.setLayoutParams(webLP);
            }
        });
        webDirect.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                LinearLayout.LayoutParams webLP = (LinearLayout.LayoutParams) mainWEb.getLayoutParams();
                int level = webDirect1.getBackground().getLevel();
                if (webTextHeight == null) {
                    webTextHeight = webText.getHeight();
                    webDefaultHeight = mainWEb.getHeight();
                    web2Height = 2 * webTextHeight + webDefaultHeight;
                }
                int action = motionEvent.getAction();
                switch (action) {
                    //按下
                    case MotionEvent.ACTION_DOWN:
                        if (touchY == null) {
                            touchY = (int) motionEvent.getY();
                        }
                        break;
                    //移动
                    case MotionEvent.ACTION_MOVE:
                        float dx = motionEvent.getY() - touchY;
                        if (webLP.height + dx > webDefaultHeight - 2 * webTextHeight && webLP.height + dx < web2Height) {
                            webLP.height += dx;
                            if (webLP.height > webDefaultHeight - 1 * webTextHeight) {
                                webDirect1.getBackground().setLevel(90);
                                changeTextshowToTitle();        //将显示文本改回为标题
                            } else {
                                webDirect1.getBackground().setLevel(10);
                                changeTextshowToQuestion();         //将显示文本改为问题
                            }
                            mainWEb.setLayoutParams(webLP);
                        }
                        break;
                    //由于其他事件回合click事件冲突，所以我放到click里面去处理了
                    default:
                        break;
                }
                return false;
            }
        });
        webClickEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =getActivity().getIntent();
                intent.setClass(getActivity(), InputViewActivity.class);
                intent.putExtra(InputViewActivity.TOP_INFO, "");
                intent.putExtra("tst", webText.getText().toString());
                getActivity().startActivityForResult(intent, InputViewActivity.REQUEST_CODE);

            }
        });
    }
    public String getCurrentUrl(){
        String url="";
        try{
            url=mainWEb.getUrl();
            return url;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }
    public void changeTextshowToQuestion(){
        String question=getArguments().getString(WEB_QUESTION);
        if(question!=null&&!question.equals("")){
            webText.setText(question);
        }
    }
    public void changeTextshowToTitle(){
        String title=getArguments().getString(TEXT_SHOW);
        if(title!=null&&!title.equals("")){
            webText.setText(title);
        }
    }


}