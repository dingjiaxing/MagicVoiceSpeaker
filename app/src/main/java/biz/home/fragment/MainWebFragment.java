package biz.home.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.net.URLEncoder;

import biz.home.InputViewActivity;
import biz.home.R;
import biz.home.SpeakToitActivity;
import biz.home.util.ColorChangeUtil;
import biz.home.util.LogUtil;

import static android.widget.AbsListView.LayoutParams.*;

/**
 * Created by adg on 2015/7/14.
 */
public class MainWebFragment extends Fragment {
    private String loadFileUrl="";
    private Context context;
    public static int MAX=65;
    public static int MID=42;
    public static int MIN=18;
    LinearLayout ll;
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
    private ImageView webDirect, webDirect1, broUp, broDown, broSelect, broShare,broReturnMain;
    public  TextView webText,webClickEdit,webQuestion;
    //进度条
    private ProgressBar progressbar;
    private OnWebFragmentListener listener;

    public interface OnWebFragmentListener{
        public void onBackLastPage();
        public void onNewUrl(String newUrl);
        public void onReturnMain();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof OnWebFragmentListener){
            listener= (OnWebFragmentListener) activity;
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public static MainWebFragment newInstance() {
        MainWebFragment fragment = new MainWebFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_GET_Data, false);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static MainWebFragment newInstance(String url) {
        MainWebFragment fragment = new MainWebFragment();
        Bundle bundle = new Bundle();
        bundle.putString(URL, url);
        bundle.putString(TEXT_SHOW, "");
        bundle.putBoolean(IS_GET_Data, true);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static MainWebFragment newInstance(String url, String textShow) {
        MainWebFragment fragment = new MainWebFragment();
        Bundle bundle = new Bundle();
        bundle.putString(URL, url);
        bundle.putString(TEXT_SHOW, textShow);
        bundle.putBoolean(IS_GET_Data, true);
        fragment.setArguments(bundle);
        return fragment;
    }
    public static MainWebFragment newInstance(String url, String textShow,String question) {
        MainWebFragment fragment = new MainWebFragment();
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
        context=getActivity();
        MAX=dip2px(getActivity(),65);
        MID=dip2px(getActivity(),42);
        MIN=dip2px(getActivity(),18);

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
            Log.i(TAG, "onCreateView url:" + url);

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
        broReturnMain= (ImageView) view.findViewById(R.id.bro_return_main);
        ll=(LinearLayout)view.findViewById(R.id.web_line_bottom);  //底部的线性布局
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
//        mainWEb.getSettings().setAppCacheEnabled(true);
        mainWEb.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
//        mainWEb.getSettings().setAppCacheMaxSize(Long.MAX_VALUE);
        // 开启 DOM storage API 功能
        mainWEb.getSettings().setDomStorageEnabled(true);

        String user_agent =
                "Mozilla/4.0 (compatible; MSIE 6.0; ) Opera/UCWEB7.0.2.37/28/999";  //用户代理是 UC Opera
//        mainWEb.getSettings().setUserAgentString(user_agent);
        String ua=mainWEb.getSettings().getUserAgentString();
        Log.i(TAG, "findById ua:"+ua);
        broReturnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onReturnMain();
            }
        });
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
                }else{
                    listener.onBackLastPage();
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

            @Override
            public void onReachedMaxAppCacheSize(long requiredStorage, long quota, WebStorage.QuotaUpdater quotaUpdater) {
                super.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
            }
        });
        mainWEb.setWebViewClient(new WebViewClient() {          //重写浏览器的函数，定向加载页面
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                loadFileUrl=url;
                if(url.startsWith("http")){
                    view.loadUrl(url);
                    Log.i(TAG, "shouldOverrideUrlLoading url;" + url);
                    listener.onNewUrl(url);
                }else if(url.startsWith("tel:")){
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
                }else{
                    //不以http开头的可能是跳转到其他app的
                    Log.i("shouldOverrideUrlLoading", "处理自定义scheme");

//                    DownloaderTask task=new DownloaderTask();
//                    task.execute(url);

                    try {
                        Toast.makeText(getActivity(), "无法跳转，您未安装该应用", Toast.LENGTH_LONG)
                                .show();
                        // 以下固定写法
                        final Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(url));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        getActivity().startActivity(intent);
                    } catch (Exception e) {
                        // 防止没有安装的情况
                        e.printStackTrace();
                        /*
//                        Toast.makeText(getActivity(), "您没有安装该app", Toast.LENGTH_LONG)
//                            .show();
                        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(context);
                        alert.setCancelable(false);
                        alert.setTitle("温馨提示");
                        alert.setMessage("您没有安装该app，是否要下载安装");
                        alert.setPositiveButton("            下载              ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DownloaderTask task=new DownloaderTask();
                                task.execute(loadFileUrl);
//                    Uri uri = Uri.parse(updateAppUrl);
//                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                    context.startActivity(intent);
                            }
                        }).setNegativeButton("             取消            ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alert.show();
                        */
                    }
                    return true;
                }
            return  true;
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
        mainWEb.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //为浏览器设置缓存，使浏览器加载之前已经加载过得页面变快
//        mainWEb.setDownloadListener(new MyWebViewDownLoadListener());

        webDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RelativeLayout.LayoutParams llp = (RelativeLayout.LayoutParams) ll.getLayoutParams();

                int oldHeight = llp.height;
                Log.i(TAG, "onClick oldHeight:" + oldHeight+"max:"+MAX+"MID:"+MID+"min"+MIN);
                int newHeight = oldHeight;
                if (oldHeight < MID) {
                    newHeight = MID;
                } else if (oldHeight < (MAX+MID)/2) {
                    changeTextshowToQuestion();
                    newHeight = MAX;
                } else {
                    changeTextshowToTitle();
                    newHeight = MID;
                }
                Log.i(TAG, "onClick newHeight:" + newHeight);
                if (newHeight == MAX) {
                    webDirect1.getBackground().setLevel(10);
                } else {
                    webDirect1.getBackground().setLevel(90);
                }

                llp.height = newHeight;
                ll.setLayoutParams(llp);
            }
        });
        webDirect.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                RelativeLayout.LayoutParams llp = (RelativeLayout.LayoutParams) ll.getLayoutParams();

                int oldHeight = llp.height;
                float newHeight=0;
                int action=motionEvent.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        if (touchY == null) {
                            touchY = (int) motionEvent.getY();
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float dx = motionEvent.getY() - touchY;
                        newHeight=(float)oldHeight-dx;

                        if (newHeight >= (MAX+MID)/2) {
                            changeTextshowToQuestion();
                            if(newHeight>MAX){
                                newHeight=MAX;
                            }
                            webDirect1.getBackground().setLevel(10);
                        } else {
                            changeTextshowToTitle();
                            if(newHeight<MIN){
                                newHeight=MIN;
                            }
                            webDirect1.getBackground().setLevel(90);
                        }
                        llp.height =(int) newHeight;
                        ll.setLayoutParams(llp);

                    default:
                            break;
                }


                return false;
            }
        });
        webClickEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getActivity().getIntent();
                intent.setClass(getActivity(), InputViewActivity.class);
                intent.putExtra(InputViewActivity.TOP_INFO, "");
                intent.putExtra("tst", webText.getText().toString());
                getActivity().startActivityForResult(intent, InputViewActivity.REQUEST_CODE);

            }
        });
        webText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webText.getText().toString().trim().equals(webQuestion.getText().toString().trim())) {
                    Intent intent = getActivity().getIntent();
                    intent.setClass(getActivity(), InputViewActivity.class);
                    intent.putExtra(InputViewActivity.TOP_INFO, "");
                    intent.putExtra("tst", webText.getText().toString());
                    getActivity().startActivityForResult(intent, InputViewActivity.REQUEST_CODE);
                }
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
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    //内部类
    private class MyWebViewDownLoadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String loadFileUrl2, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            Log.i(TAG, "onDownloadStart: loadFileUrl2:"+loadFileUrl2+";contentDisposition:"+contentDisposition+";mimetype:"+mimetype+";contentLength:"+contentLength);
            loadFileUrl=loadFileUrl2;
            if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                Toast t=Toast.makeText(context, "需要SD卡。", Toast.LENGTH_SHORT);
//                t.setGravity(Gravity.CENTER, 0, 0);
                t.show();
                return;
            }
//            DownloaderTask task=new DownloaderTask();
//            task.execute(loadFileUrl);
            android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(context);
            alert.setCancelable(false);
            alert.setTitle("温馨提示");
            alert.setMessage("手机上没有该app，是否要下载安装");
            alert.setPositiveButton("            下载              ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DownloaderTask task=new DownloaderTask();
                    task.execute(loadFileUrl);
//                    Uri uri = Uri.parse(updateAppUrl);
//                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                    context.startActivity(intent);
                }
            }).setNegativeButton("             取消            ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alert.show();





        }

    }
    //内部类
    private class DownloaderTask extends AsyncTask<String, Void, String> {

        public DownloaderTask() {
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String url=params[0];
//          Log.i("tag", "url="+url);
            String fileName=url.substring(url.lastIndexOf("/")+1);
            fileName= URLDecoder.decode(fileName);
            Log.i(TAG, "fileName="+fileName);

            File directory=Environment.getExternalStorageDirectory();
            File file=new File(directory,fileName);
            if(file.exists()){
                Log.i(TAG, "The file has already exists.");
                return fileName;
            }
            try {
                HttpClient client = new DefaultHttpClient();
//                client.getParams().setIntParameter("http.socket.timeout",3000);//设置超时
                HttpGet get = new HttpGet(url);
                HttpResponse response = client.execute(get);
                if(HttpStatus.SC_OK==response.getStatusLine().getStatusCode()){
                    HttpEntity entity = response.getEntity();
                    InputStream input = entity.getContent();

                    writeToSDCard(fileName,input);

                    input.close();
//                  entity.consumeContent();
                    return fileName;
                }else{
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onCancelled() {
            // TODO Auto-generated method stub
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            closeProgressDialog();
            if(result==null){
                Toast t=Toast.makeText(context, "连接错误！请稍后再试！", Toast.LENGTH_LONG);
//                t.setGravity(Gravity.CENTER, 0, 0);
                t.show();
                return;
            }

            Toast t=Toast.makeText(context, "已保存到SD卡。", Toast.LENGTH_LONG);
//            t.setGravity(Gravity.CENTER, 0, 0);
            t.show();
            File directory=Environment.getExternalStorageDirectory();
            File file=new File(directory,result);
            Log.i(TAG, "Path="+file.getAbsolutePath());

            Intent intent = getFileIntent(file);

            context.startActivity(intent);

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            Toast.makeText(context,"正在下载中，请稍后",Toast.LENGTH_SHORT).show();
//            showProgressDialog();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
        }


    }
    private ProgressDialog mDialog;
    private void showProgressDialog(){
        if(mDialog==null){
            mDialog = new ProgressDialog(context);
            mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//设置风格为圆形进度条
            mDialog.setMessage("正在加载 ，请等待...");
            mDialog.setIndeterminate(false);//设置进度条是否为不明确
            mDialog.setCancelable(true);//设置进度条是否可以按退回键取消
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                    mDialog=null;
                }
            });
            mDialog.show();

        }
    }
    private void closeProgressDialog(){
        if(mDialog!=null){
            mDialog.dismiss();
            mDialog=null;
        }
    }
    public Intent getFileIntent(File file){
//       Uri uri = Uri.parse("http://m.ql18.com.cn/hpf10/1.pdf");
        Uri uri = Uri.fromFile(file);
        String type = getMIMEType(file);
        Log.i("tag", "type="+type);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, type);
        return intent;
    }

    public void writeToSDCard(String fileName,InputStream input){

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File directory=Environment.getExternalStorageDirectory();
            File file=new File(directory,fileName);
//          if(file.exists()){
//              Log.i("tag", "The file has already exists.");
//              return;
//          }
            try {
                FileOutputStream fos = new FileOutputStream(file);
                byte[] b = new byte[2048];
                int j = 0;
                while ((j = input.read(b)) != -1) {
                    fos.write(b, 0, j);
                }
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else{
            Log.i("tag", "NO SDCard.");
        }
    }

    private String getMIMEType(File f){
        String type="";
        String fName=f.getName();
      /* 取得扩展名 */
        String end=fName.substring(fName.lastIndexOf(".")+1,fName.length()).toLowerCase();

      /* 依扩展名的类型决定MimeType */
        if(end.equals("pdf")){
            type = "application/pdf";//
        }
        else if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||
                end.equals("xmf")||end.equals("ogg")||end.equals("wav")){
            type = "audio/*";
        }
        else if(end.equals("3gp")||end.equals("mp4")){
            type = "video/*";
        }
        else if(end.equals("jpg")||end.equals("gif")||end.equals("png")||
                end.equals("jpeg")||end.equals("bmp")){
            type = "image/*";
        }
        else if(end.equals("apk")){
        /* android.permission.INSTALL_PACKAGES */
            type = "application/vnd.android.package-archive";
        }
//      else if(end.equals("pptx")||end.equals("ppt")){
//        type = "application/vnd.ms-powerpoint";
//      }else if(end.equals("docx")||end.equals("doc")){
//        type = "application/vnd.ms-word";
//      }else if(end.equals("xlsx")||end.equals("xls")){
//        type = "application/vnd.ms-excel";
//      }
        else{
//        /*如果无法直接打开，就跳出软件列表给用户选择 */
            type="*/*";
        }
        return type;
    }
//    //调用系统浏览器下载文件的接口
//    private class MyWebViewDownLoadListener implements DownloadListener {
//
//        @Override
//        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
//                                    long contentLength) {
////            Uri uri = Uri.parse(url);
////            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
////            startActivity(intent);
//            Intent i = new Intent(Intent.ACTION_VIEW);
//            i.setData(Uri.parse(url));
//            startActivity(i);
//        }
//
//    }



}