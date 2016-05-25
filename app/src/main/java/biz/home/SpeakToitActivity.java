package biz.home;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import biz.home.adapter.DrawerListViewAdapter;
import biz.home.adapter.FragmentAdapter;
import biz.home.api.MapLocation;
import biz.home.api.jpush.MyJPushReceiver;
import biz.home.api.notification.NotificationBroadcastReceiver;
import biz.home.assistActivity.resource.MyInformationTopMenu;
import biz.home.assistActivity.PollingService;
import biz.home.assistActivity.resource.ProjectReleaseActivity;
import biz.home.assistActivity.resource.ResourceInfo;
import biz.home.bean.ArtificialAnswer;
import biz.home.bean.MagicInfo;
import biz.home.bean.MagicResourceInfo;
import biz.home.bean.MagicResult;
import biz.home.bean.MagicResultResourceInfo;
import biz.home.fragment.MainEditFragment;
import biz.home.fragment.MainMessageFragment;
import biz.home.fragment.MainPreMessageFragment;
import biz.home.fragment.MainPreMessageFragment.OnMessageContactReturn;
import biz.home.fragment.MainProjectFragment;
import biz.home.fragment.MainWebFragment;
import biz.home.fragment.MainWebFragment.OnWebFragmentListener;
import biz.home.model.MagicInfoApiEnum;
import biz.home.model.UserSetting;
import biz.home.util.ArtificialAnswerDao;
import biz.home.util.BackgroudChangeUtil;
import biz.home.util.ConversationHistoryDao;
import biz.home.util.DeviceIdUtil;
import biz.home.util.HttpHelp;
import biz.home.util.LogUtil;
import biz.home.util.MagicUserInfoDao;
import biz.home.util.ProgressBarAsyncTask;
import biz.home.util.UserSettingDao;
import biz.home.util.VoiceUtil;
import biz.home.yibuTask.ChangeMessageNameTask;
import biz.home.yibuTask.OpenResourceInfoTask;
import biz.home.yibuTask.PollingArtificialAnswerThread;
import biz.home.yibuTask.UpdateAppTask;
import biz.home.yibuTask.UploadFileTask;
import cn.jpush.android.api.JPushInterface;
import com.baidu.speech.VoiceRecognitionService;
import com.baidu.speechsynthesizer.SpeechSynthesizer;
import com.baidu.speechsynthesizer.SpeechSynthesizerListener;
import com.baidu.speechsynthesizer.publicutility.DataInfoUtils;
import com.baidu.speechsynthesizer.publicutility.SpeechError;
import com.baidu.speechsynthesizer.publicutility.SpeechLogger;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by adg 7/14
 * MainPreMessageFragment.OnMessageRecieverSure接口为用户发短信时选好联系人之后触发的
 * MainMessageFragment.OnMessageCancel接口为用户在发短信界面点击了取消适合触发的
 * MainMessageFragment.OnMessageSend接口为用户在发短信界面点击了发送时触发的
 * onSendProjectInformation接口为用户在点击了确定发布资源信息之后出发的
 * MainMessageFragment.OnClickContactName接口为 用户在发短信界面点击了 姓名之后 触发的
 */
public class SpeakToitActivity extends ActionBarActivity implements View.OnClickListener,
        MainEditFragment.OnActivityListener,RecognitionListener,SpeechSynthesizerListener,
        MainPreMessageFragment.OnMessageRecieverSure,MainMessageFragment.OnMessageCancel,MainMessageFragment.OnMessageSend,
        MainEditFragment.OnChickButton,MainEditFragment.OnNextMessageButton,MainProjectFragment.onSendProjectInformation,
        MainMessageFragment.OnClickContactName,OnMessageContactReturn ,MainEditFragment.OnExitScene ,OnWebFragmentListener {
    //<editor-fold desc="Variables declaration">
    private RelativeLayout rl,rl_voice,rl_sound,rl_slide,rl_menu;
    private int count=0;

    ArtificialAnswer aa;    //人工客服的回复
    private static Boolean is_just_start_recognize_flag=false;  //是否刚刚启动百度语音识别的标志，如果刚刚启动，将此变量置为true
//    和极光推送有关的变量
    public static final String MESSAGE_RECEIVED_ACTION = "biz.zm.magic.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public static boolean isForeground = false;  //该Activity是否在前台运行的标识

    private ImageView imageGirl;
    public static MagicResultResourceInfo projectReleaseResourceInfo;
    private List<String> group;
    private List<List<String>> child;
    ArtificialAnswerDao artificialAnswerDao;
    ResponseArtificialHandler responseArtificialHandler=new ResponseArtificialHandler();
    public static ArrayList<ArtificialAnswer> artificialAnswerList=new ArrayList<>();
   // private int back_mark=0;        //返回键的标志位，连续点击两次返回键才会退出程序
    private UserSettingDao userSettingDao;
    public static boolean isHuihua=false;   //是否为会话模式
    public static Map<String,String > pics;        //存储上传资源时的图片
    private static final String TAG = "SpeakToitActivity";
    private static final String CONTENT = "content";
    private static final String TITLE = "title";
    private static final String URL = "url";
    private static final String TEXT = "text";
//    public static int pre_recognize=-1;     //百度语音识别预处理，0时为正在做预处理，-1时为正常，1时 为第一次点击话筒，由于“引擎忙”，话筒背后的圈无法及时跟进
    public static int MIROPHONE_STATE  = 0;  //0时为初始状态，1时为正在录音状态，2时为录音结束等待回答状态，3时为回答状态
//    private static final int MIN_CLICK_TIME = 50;
//    private long lastClickTime = 0;
    private String endResult="";
//    private String content;
//    private Handler handler = null;
    /**
     * ViewPager相关
     */
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private ViewPager mViewPager;
    //lmw:菜单栏组件定义
    private ImageView menuImgKeyborad;
    private ImageView menuImgMicrophone;
    private ImageView menuVoiceBackgroud;
    private ImageView menuImgMenu;
    private ImageView assetsUser, assetsSet;
    private VoiceUtil voiceUtil;
    //lmw:handler 用于根据声音大小更新UI
    private Handler voidHandler;
    //lmw:抽屉界面组件
    private DrawerLayout drawer;
    private ExpandableListView drawerList;
    private List<Map<String, String>> dataList = null;
    private DrawerListViewAdapter adapter = null;
    private FragmentAdapter mFragmentAdapter;
    private ViewGroup mContainer;
    // 需要加载的fragment
    private MainWebFragment mainWebFragment;
    private MainEditFragment mainEditFragment;
    //  ViewPager当前页面
    private int currentIndex;
    //返回码
    public static final int RESULT_INPUT_DATA = 1101;
    public static final int RESULT_SET_DATA = 1102;
    public static final int RETURN_FALSE = 1103;
    public static final int RESULT_LOGIN_DATA = 1104;
    public static final int RESULT_REGISTER_DATA = 1105;
    public static final int RESULT_PERSONAL_INFO = 1106;
    public static final int RESULT_RELEASED_PROJECT_SHOW=1107;
    //最大历史记录数量
    private final int MaxFragmentSize = 10;

    //百度语音相关变量
    private long baiduTouchTime;
    private View speechTips;
    private View speechWave;

    public static final int STATUS_None = 0;
    public static final int STATUS_WaitingReady = 2;
    public static final int STATUS_Ready = 3;
    public static final int STATUS_Speaking = 4;
    public static final int STATUS_Recognition = 5;
    private SpeechRecognizer speechRecognizer;
    private int status = STATUS_None;
    private long speechEndTime = -1;
    private static final int EVENT_ERROR = 11;
//    private static final int REQUEST_UI = 1;

    //百度语音合成相关参数
//    public static String con;
//    protected static final int UI_LOG_TO_VIEW = 0;
//    protected static final int UI_TOAST = 1;
    public SpeechSynthesizer speechSynthesizer;
    private TextView logView;
//    private EditText inputTextView;
//    private Button startButton;
//    private Handler uiHandler;
//    private Toast mToast;
    //全局变量，用于将异步线程处理的结果 返回到主线程来
    public static  MagicResult magicResult;
    //个人账户有关的数据库操作
    MagicUserInfoDao dao=new MagicUserInfoDao(SpeakToitActivity.this);
    //操作历史消息的dao
    private ConversationHistoryDao conversationHistoryDao;
    //</editor-fold>
//    PollingService pollingService;
//    Intent pollingIntent;
//    ServiceConnection conn=new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder binder) {
//            pollingService=((PollingService.MyBinder)binder).getService();
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            pollingService.stopSelf();
//            pollingService=null;
//
//        }
//    };

    //退出场景时候 回调此方法
    @Override
    public void onExitScene(Boolean b) {
        if(speechSynthesizer.getPlayerStatus()==speechSynthesizer.PLAYER_STATE_PLAYING){
            speechSynthesizer.cancel();
        }
        speechSynthesizer.speak("好的，那还需要为您做些什么呢？");
        new Thread(new Runnable() {
            @Override
            public void run() {
                MagicInfo info=new MagicInfo();
                info.setUid(dao.findUid());
                info.setApi(MagicInfoApiEnum.SEND);
                info.setExitDialog(true);
                info.setDeviceId(DeviceIdUtil.comDeviceId(getApplicationContext()));
                HttpHelp.send(info);
            }
        }).start();
        mainEditFragment = MainEditFragment.newInstance("", "好的，那还需要为您做些什么呢？", "", MainEditFragment.BUTTON_DEFAULT);
        addFragment2List(mainEditFragment);
        mFragmentAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(mFragmentList.size() - 1);

    }


    Handler projectHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle=msg.getData();
            Intent intent=new Intent(SpeakToitActivity.this,ProjectReleaseActivity.class);
            intent.putExtra("title",bundle.getString("title"));
            intent.putExtra("content",bundle.getString("content"));
            startActivityForResult(intent,ProjectReleaseActivity.REQUEST_CODE);
        }
    };

    @Override
    public void onBackLastPage() {
        changeLastPage();
    }

    @Override
    public void onNewUrl(String newUrl) {
        try{
            if(!newUrl.endsWith(".apk")){
                if(mFragmentList.get(currentIndex) instanceof MainWebFragment){
                    MainWebFragment mwf= (MainWebFragment) mFragmentList.get(currentIndex);
                    Bundle bundle=mwf.getArguments();
                    bundle.putString(MainWebFragment.URL,newUrl);
                    Log.i(TAG, "onNewUrl: "+newUrl+"; currentIndex:"+currentIndex);
                    mwf.setArguments(bundle);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onReturnMain() {
        speechSynthesizer.speak("欢迎回来，我还能为您做些什么呢");
        MainEditFragment mainEditFragment = MainEditFragment.newInstance("", "欢迎回来，我还能为您做些什么呢", "", MainEditFragment.BUTTON_DEFAULT);
        addFragment2List(mainEditFragment);
        mFragmentAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(mFragmentList.size() - 1);
    }

    class ResponseArtificialHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            try {
                if(msg.what==1){
                    Bundle bundle= msg.getData();
                    aa=new ArtificialAnswer(bundle.getString("type"),bundle.getString("question"),bundle.getString("answer"),
                            bundle.getString("url"));
                    if(aa.getType().equals("text")){
                        Log.i(TAG, "handleMessage: aa.getAnswer():"+aa.getAnswer());
                        speechSynthesizer.speak(aa.getAnswer());
                        MainEditFragment mainEditFragment = MainEditFragment.newInstance("", aa.getAnswer(), aa.getQuestion(), MainEditFragment.BUTTON_DEFAULT);
                        addFragment2List(mainEditFragment);
                        mFragmentAdapter.notifyDataSetChanged();
                        mViewPager.setCurrentItem(mFragmentList.size() - 1);
                        SpeakToitActivity.MIROPHONE_STATE=3;
                    }else if(aa.getType().equals("url")){
                        speechSynthesizer.speak(aa.getAnswer());
                        MainWebFragment mainWebFragment = MainWebFragment.newInstance(aa.getUrl(),aa.getAnswer(),aa.getQuestion());
                        addFragment2List(mainWebFragment);
                        mFragmentAdapter.notifyDataSetChanged();
                        mViewPager.setCurrentItem(mFragmentList.size() - 1);
                        SpeakToitActivity.MIROPHONE_STATE=3;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        JPushInterface.onPause(this);
        isForeground = false;
        PollingService.stopPollingFlag=true;
//        pollingService.stopPolling();
        //将ViePager中的消息记录存到数据库
        conversationHistoryDao.deleteAll();
        conversationHistoryDao.addList(mFragmentList);
    }


    /**
     * SpeakToIt停止运行的时候的回调函数
     */
    @Override
    protected void onStop() {
        //将会话历史消息存在sqllite数据库
        super.onStop();
        try{
            PollingService.stopPollingFlag=true;
            JPushInterface.resumePush(this);

            if(artificialAnswerList.size()!=0){
                //如果当前类变量中的列表中还有值，就把这些结果放到 数据库中 以防客服回复的结果丢失-
                artificialAnswerDao.deleteAll();
                artificialAnswerDao.add(artificialAnswerList);
            }

//            pollingService.stopPolling();
            //存入数据库
//            conversationHistoryDao.deleteAll();
//            conversationHistoryDao.addList(mFragmentList);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            JPushInterface.init(getApplicationContext());
            String id=JPushInterface.getRegistrationID(this);
            Log.i("JPush", "onCreate getRegistrationID:" + id);
            startService(new Intent(SpeakToitActivity.this,PollingService.class));

            artificialAnswerDao=new ArtificialAnswerDao(getApplicationContext());
            conversationHistoryDao=new ConversationHistoryDao(getApplicationContext());
            mFragmentList=conversationHistoryDao.findAll();
            int c=conversationHistoryDao.getCount();
            setContentView(R.layout.activity_main);
            findById();
            //语音合成
            userSettingDao=new UserSettingDao(getApplicationContext());
            preHecheng();
            //语音识别
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this, new ComponentName(this, VoiceRecognitionService.class));
            speechRecognizer.setRecognitionListener(this);
            init();
            setBackgroundBar();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            MapLocation map=new MapLocation(this,getApplicationContext());
            map.execute(1000);
            Log.i(TAG, "onCreate ");
            //检查是否要更新
            UpdateAppTask task=new UpdateAppTask(this,UpdateAppTask.SPEAKEMAIN);
            task.execute(1000);





//            pollingIntent=new Intent(SpeakToitActivity.this,PollingService.class);
//            try{
//                bindService(pollingIntent, conn, BIND_AUTO_CREATE);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
            //轮询SpeakToIt类中的客服回复列表（类变量artificialAnswerList）的线程
            PollingArtificialAnswerThread paaThread=new PollingArtificialAnswerThread(responseArtificialHandler);
            paaThread.start();

            //将数据库中存储的 客服回复结果列表 拿出来放到本地类变量 list中
            artificialAnswerList=artificialAnswerDao.findAll();
            artificialAnswerDao.deleteAll();
            /*
            new Handler().postDelayed(new Thread(){
                @Override
                public void run() {
                    startActivity(new Intent(SpeakToitActivity.this, GuideHelpActivity.class));
                }
            },1000);
            */
        }catch (Exception e){
            e.printStackTrace();
        }


    }
     /**
     * 设置状态栏背景色
     * 由于设置过后会有好多问题，所以没有添加到里面
     */
    private void setBackgroundBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window windows = getWindow();
            // 透明状态栏
            windows.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 创建状态栏的管理实例
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            // 激活状态栏设置
            tintManager.setStatusBarTintEnabled(true);
            // 设置一个颜色给系统栏
            tintManager.setTintColor(getResources().getColor(R.color.speakToit_bg));
            //当请求布局时，窗口可能出现在状态栏的上面或下面，
            // 从而造成遮挡。当设置这一选项后，窗口管理器将确保窗口内容不会被装饰条（状态栏）盖住。
            windows.setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR,
                    WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR);
        }
    }
    /**
     * 控件初始化
     */
    private void findById() {
//        mImgRL = (RelativeLayout) findViewById(R.id.girl_content_background);
        mViewPager = (ViewPager) findViewById(R.id.id_page_vp);
        assetsSet = (ImageView) findViewById(R.id.assets_setting);      //主界面左上角的“设置”小按钮
        assetsUser = (ImageView) findViewById(R.id.assets_user);        //主界面右上角的“用户”小按钮
        assetsSet.setOnClickListener(this);
        assetsUser.setOnClickListener(this);
        //lmw:初始化菜单组件
        menuImgKeyborad = (ImageView) findViewById(R.id.menu_bottom_keyboard);
        menuImgMicrophone = (ImageView) findViewById(R.id.menu_bottom_microphone);
        menuVoiceBackgroud = (ImageView) findViewById(R.id.menu_bottom_voice);
        menuImgMenu = (ImageView) findViewById(R.id.menu_bottom_menu);
        //lmw:初始化抽屉数据
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawerList = (ExpandableListView) findViewById(R.id.right_drawer);
        mContainer = (ViewGroup) findViewById(android.R.id.content);
        imageGirl= (ImageView) findViewById(R.id.ic_girl_happy);
//        drawerList.setOnItemClickListener(this);
        initDrawerList();

        rl= (RelativeLayout) findViewById(R.id.guide_root_relativeLayout);
        rl_voice= (RelativeLayout) findViewById(R.id.guide_voice_relativeLayout);
        rl_sound= (RelativeLayout) findViewById(R.id.guide_sound_relativeLayout);
        rl_slide= (RelativeLayout) findViewById(R.id.guide_slide_relativeLayout);
        rl_menu= (RelativeLayout) findViewById(R.id.guide_menu_relativeLayout);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.guide_root_relativeLayout:
                        count++;
                        switch (count){
                            case 1:
                                rl_voice.setVisibility(View.INVISIBLE);
                                rl_sound.setVisibility(View.VISIBLE);
                                break;
                            case 2:
                                rl_sound.setVisibility(View.INVISIBLE);
                                rl_slide.setVisibility(View.VISIBLE);
                                break;
                            case 3:
                                rl_slide.setVisibility(View.INVISIBLE);
                                rl_menu.setVisibility(View.VISIBLE);
                                break;
                            default:
                                rl.setVisibility(View.INVISIBLE);
                                break;
                        }
                    default:
                        break;
                }
            }
        });
        //通过判断是否是第一次登录来判断是否弹出引导界面
        SharedPreferences sp1=getSharedPreferences("firstSign",Context.MODE_PRIVATE);
        if(sp1.getBoolean("firstSignFlag",true)){
            SharedPreferences.Editor editor=sp1.edit();
            editor.putBoolean("firstSignFlag",false);
            editor.commit();
            rl.setVisibility(View.VISIBLE);
        }else {
            rl.setVisibility(View.INVISIBLE);
        }


    }
    /**
     * @param requestCode 请求码
     * @param resultCode  返回码
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(speechSynthesizer.getPlayerStatus()==speechSynthesizer.PLAYER_STATE_PLAYING){
            speechSynthesizer.cancel();
        }
        //将话筒画面 置为初始状态
        ViewGroup.LayoutParams param = menuVoiceBackgroud.getLayoutParams();
        param.height = 0;
        param.width = 0;
        menuVoiceBackgroud.setLayoutParams(param);
        menuImgMicrophone.setImageDrawable(BackgroudChangeUtil.
                getDrawable(getBaseContext(), BitmapFactory.decodeResource(getResources(), R.drawable.microphone_control),
                        getResources().getString(R.string.rgb_prepare_microphone_not_press), getResources().getString(R.string.rgb_prepare_microphone_press)));
        menuImgMicrophone.setBackgroundColor(Color.parseColor("#00000000"));
        menuImgMicrophone.setContentDescription("0");


        //System.out.println("MainActivity -resultCode" + resultCode);
        Log.i(TAG, "onActivityResult: resultCode:"+resultCode);

        switch (resultCode) {
            case RESULT_INPUT_DATA:
                //点击主界面左下角的键盘之后开始编辑，将编辑的结果返回到此处进行处理
                LogUtil.d(TAG, "测试添加");
                if (data.hasExtra(InputViewActivity.BOTTOM_INFO)) {
                    //这个是编辑界面输入的信息
                    String response = data.getStringExtra(InputViewActivity.BOTTOM_INFO);
                     String response1 = data.getStringExtra(InputViewActivity.TOP_INFO);
                    Intent intent=getIntent();
                   String newInfo= intent.getStringExtra(InputViewActivity.BOTTOM_INFO);
                    Log.i(TAG, "onActivityResult 改变之后的数据："+newInfo+response1+response);
//                    ResolveDataFromServer();
                    //将识别结果显示在界面上
                    executeInfo(response);
                }
                break;
            case MainProjectFragment.CHANGE_CONTENT:
                if(data.hasExtra(MainProjectFragment.CONTENT)){
                    String content0=data.getStringExtra(MainProjectFragment.CONTENT);
                    System.out.println("content0" + content0);
                    //判断是否为用户发资源时的 资源详情，如果是，则弹出 MainProjectFrament来显示资源详情让用户确认
                    MainProjectFragment mainProjectFragment=MainProjectFragment.newInstance(magicResult.getResourceInfo().getTitle(), content0);
                    addFragment2List(mainProjectFragment);
                    mFragmentAdapter.notifyDataSetChanged();
                    mViewPager.setCurrentItem(mFragmentList.size() - 1);
                    mFragmentList.remove(mFragmentList.size() - 2);
                    mFragmentAdapter.notifyDataSetChanged();
                }
                break;
            case RESULT_RELEASED_PROJECT_SHOW:
//        ProjectTask asyncTask=new ProjectTask(getApplicationContext(),mainWebFragment,speechSynthesizer,info.getResourceInfo() , mainEditFragment, mFragmentAdapter,mFragmentList,mViewPager);
                ProgressBarAsyncTask asyncTask=new ProgressBarAsyncTask(projectHandler, projectReleaseResourceInfo,mContainer,getApplicationContext(),mainWebFragment,speechSynthesizer,endResult, mainEditFragment, mFragmentAdapter, mFragmentList, mViewPager) ;
                asyncTask.execute(2000);
                break;
            case RESULT_SET_DATA:
                break;
            case RETURN_FALSE:
                break;
            case RESULT_LOGIN_DATA:
                break;
            case RESULT_REGISTER_DATA:
                break;
            case RESULT_PERSONAL_INFO:
                break;
            default:
                break;

        }

    }
    /**
     * 初始化
     */
    private void init() {

        try{
            if(userSettingDao.find(dao.findTelephone()).getStateItem2()==1){
                start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //使小秘书眨眼
        imageGirl.setImageResource(R.drawable.activity_girl_eye_animation);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageGirl.getDrawable();
        animationDrawable.start();
        //增加当前控件的布局监听
//        ViewTreeObserver mImgRLTreeObserver = mImgRL.getViewTreeObserver();
//        mImgRLTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                ImgRLDefaultHeight = mImgRL.getMeasuredHeight();
//            }
//        });
        //lmw:初始化菜单组件动作
        voidHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    ViewGroup.LayoutParams param = menuVoiceBackgroud.getLayoutParams();
                    int addPx = msg.getData().getInt(VoiceUtil.BACK_INFO);
                    param.height = menuImgMicrophone.getHeight() + 2 * addPx + 1;
                    param.width = menuImgMicrophone.getHeight() + 2 * addPx + 1;
                    menuVoiceBackgroud.setLayoutParams(param);
                } else if (msg.what == 0) {
                    ViewGroup.LayoutParams param = menuVoiceBackgroud.getLayoutParams();
                    param.height = 0;
                    param.width = 0;
                    menuVoiceBackgroud.setLayoutParams(param);
                    menuImgMicrophone.setImageDrawable(BackgroudChangeUtil.
                            getDrawable(getBaseContext(), BitmapFactory.decodeResource(getResources(), R.drawable.microphone_control),
                                    getResources().getString(R.string.rgb_prepare_microphone_not_press), getResources().getString(R.string.rgb_prepare_microphone_press)));
                    menuImgMicrophone.setBackgroundColor(Color.parseColor("#00000000"));
                    menuImgMicrophone.setContentDescription("0");
                }
            }
        };
        voiceUtil = new VoiceUtil(voidHandler);
        menuImgKeyborad.setImageDrawable(BackgroudChangeUtil.
                getDrawable(this, BitmapFactory.decodeResource(getResources(), R.drawable.keyboard_control),
                        getResources().getString(R.string.rgb_menu_not_press), getResources().getString(R.string.rgb_menu_press)));
        menuImgMicrophone.setImageDrawable(BackgroudChangeUtil.
                getDrawable(this, BitmapFactory.decodeResource(getResources(), R.drawable.microphone_control),
                        getResources().getString(R.string.rgb_prepare_microphone_not_press), getResources().getString(R.string.rgb_prepare_microphone_press)));
        menuImgMenu.setImageDrawable(BackgroudChangeUtil.
                getDrawable(this, BitmapFactory.decodeResource(getResources(), R.drawable.menu_control),
                        getResources().getString(R.string.rgb_menu_not_press), getResources().getString(R.string.rgb_menu_press)));
        //增加百度语音弹窗，默认为隐藏
        speechTips = View.inflate(this, R.layout.bd_asr_popup_speech, null);
        speechWave = speechTips.findViewById(R.id.wave);
        speechTips.setVisibility(View.GONE);
        addContentView(speechTips, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        menuImgMicrophone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(SpeakToitActivity.this,"点击了话筒",Toast.LENGTH_SHORT).show();
//            }
//        });
        menuImgMicrophone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
//                        baiduTouchTime=System.currentTimeMillis();

                        if(speechSynthesizer.getPlayerStatus()==speechSynthesizer.PLAYER_STATE_PLAYING){
                            speechSynthesizer.cancel();
                        }
                        menuImgMicrophone.setImageDrawable(BackgroudChangeUtil.
                                getDrawable(getBaseContext(), BitmapFactory.decodeResource(getResources(), R.drawable.microphone_control),
                                        getResources().getString(R.string.rgb_start_microphone_not_press), getResources().getString(R.string.rgb_start_microphone_press)));
                        menuImgMicrophone.setBackgroundResource(R.drawable.menu_bottom_microphone_backgroud_change);
                        speechTips.setVisibility(View.VISIBLE);
                        speechRecognizer.cancel();
                        Intent intent = new Intent();
                        bindParams(intent);
                        intent.putExtra("vad", "touch");
                        //txtResult.setText("");
                        //txtLog.setText("");
                        speechRecognizer.startListening(intent);
                        MIROPHONE_STATE=1;
                        return true;

//                    case MotionEvent.ACTION_MOVE:
//                        if(System.currentTimeMillis()-baiduTouchTime>500 && MIROPHONE_STATE==1){
//
//                        }
//                        break;
                       // return true;
                    case MotionEvent.ACTION_UP:
                        speechRecognizer.stopListening();
                        MIROPHONE_STATE=2;
                        //speechRecognizer.cancel();
                        speechTips.setVisibility(View.GONE);
                        //将话筒置为初始状态
                        ViewGroup.LayoutParams param = menuVoiceBackgroud.getLayoutParams();
                        param.height = 0;
                        param.width = 0;
                        menuVoiceBackgroud.setLayoutParams(param);
                        menuImgMicrophone.setImageDrawable(BackgroudChangeUtil.
                                getDrawable(getBaseContext(), BitmapFactory.decodeResource(getResources(), R.drawable.microphone_control),
                                        getResources().getString(R.string.rgb_prepare_microphone_not_press), getResources().getString(R.string.rgb_prepare_microphone_press)));
                        menuImgMicrophone.setBackgroundColor(Color.parseColor("#00000000"));
                        menuImgMicrophone.setContentDescription("0");
                        /*
                        if(MIROPHONE_STATE==0){
                            if(System.currentTimeMillis()-baiduTouchTime<=500){
                                Toast.makeText(SpeakToitActivity.this,"说话的时间过短",Toast.LENGTH_SHORT).show();
                            }
                        }else if(MIROPHONE_STATE==1){

                        }

                        baiduTouchTime=0;
                        */
                        break;
                }
                return false;
            }
        });
//        menuImgMicrophone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //MIROPHONE_STATE=1;
//                if(!is_just_start_recognize_flag){  //如果刚刚启动百度语音识别，则不理会用户的单击事件，如果不是刚刚启动百度语音识别，则响应用户的单击事件
//                    Log.i("MIROPHONE_STATE-0", MIROPHONE_STATE + "");
//                    if (MIROPHONE_STATE == 1) {
//                        Log.i("MIROPHONE_STATE-0.5", MIROPHONE_STATE + "");
//                        stop();
//                        Log.i("MIROPHONE_STATE-1", MIROPHONE_STATE + "");
//                        //speechRecognizer.stopListening();
//                        // MIROPHONE_STATE=2;
//                        //MIROPHONE_STATE=0;
//                    /*
//                    if (mFragmentList.get(mViewPager.getCurrentItem()) instanceof MainEditFragment) {
//                        ((MainEditFragment) mFragmentList.get(mViewPager.getCurrentItem())).SetTextHeight();
//                    }
//                    menuImgMicrophone.setImageDrawable(BackgroudChangeUtil.
//                            getDrawable(getBaseContext(), BitmapFactory.decodeResource(getResources(), R.drawable.microphone_control),
//                                    getResources().getString(R.string.rgb_prepare_microphone_not_press), getResources().getString(R.string.rgb_prepare_microphone_press)));
//                    menuImgMicrophone.setBackgroundColor(Color.parseColor("#00000000"));
//                    */
//
//                    } else if (MIROPHONE_STATE == 0) {
//                        is_just_start_recognize_flag=true;
//                        //一个延迟执行的线程，如果启动百度语音识别，则将is_just_start_recognize_flag标志位改为true，800ms后自动将标志位改为false
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                is_just_start_recognize_flag=false;
//                            }
//                        },800);
//                   /* if (mFragmentList.get(mViewPager.getCurrentItem()) instanceof MainEditFragment) {
//                        ((MainEditFragment) mFragmentList.get(mViewPager.getCurrentItem())).DefaultTextHeight();
//                    }//判断按钮是否被按下，0表示没有被按下，1表示被按下
//                    */
//                        menuImgMicrophone.setImageDrawable(BackgroudChangeUtil.
//                                getDrawable(getBaseContext(), BitmapFactory.decodeResource(getResources(), R.drawable.microphone_control),
//                                        getResources().getString(R.string.rgb_start_microphone_not_press), getResources().getString(R.string.rgb_start_microphone_press)));
//                        menuImgMicrophone.setBackgroundResource(R.drawable.menu_bottom_microphone_backgroud_change);
//                        Log.i("MIROPHONE_STATE-2", MIROPHONE_STATE + "");
//                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                        boolean api = sp.getBoolean("api", false);
//                        if (api) {
//                            switch (status) {
//                                case STATUS_None:
//                                    Log.i(TAG, "onClick STATUS_None取消");
//                                    start();
//                                    status = STATUS_WaitingReady;
//                                    break;
//                                case STATUS_WaitingReady:
//                                    Log.i(TAG, "onClick STATUS_WaitingReady开始");
//                                    cancel();
//                                    status = STATUS_None;
//                                    break;
//                                case STATUS_Ready:
//                                    Log.i(TAG, "onClick STATUS_Ready");
//                                    cancel();
//                                    status = STATUS_None;
//                                    //btn.setText("开始");
//                                    break;
//                                case STATUS_Speaking:
//                                    Log.i(TAG, "onClick STATUS_Speaking识别中");
//                                    stop();
//                                    status = STATUS_Recognition;
//                                    break;
//                                case STATUS_Recognition:
//                                    Log.i(TAG, "onClick STATUS_Recognition");
//                                    cancel();
//                                    status = STATUS_None;
//                                    break;
//                            }
//                        } else {
//                            start();
//
//                        }
//                    }else if(MIROPHONE_STATE == 2){
//                        Toast.makeText(getApplicationContext(),"不要着急哦，我正在想该怎么回答您更好呢？",Toast.LENGTH_SHORT).show();
////                    android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(SpeakToitActivity.this);
////                    alert.setTitle("温馨提示：");
////                    alert.setMessage("不要着急哦，我正在想该怎么回答您更好呢？");
////                    alert.setPositiveButton("                                       确定                                       ", new DialogInterface.OnClickListener() {
////                        @Override
////                        public void onClick(DialogInterface dialog, int which) {
////
////                        }
////                    });
////                    alert.show();
//                    }else if (MIROPHONE_STATE == 3) {
//                        Log.i(TAG, "onClick MIROPHONE_STATE == 3");
//                        speechSynthesizer.cancel();
//                    }
//                }
//
//            }
//
//
//        });
//        menuImgMicrophone.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                if(MIROPHONE_STATE==0){
//                    Toast.makeText(getApplicationContext(),"点击一下说话我就可以听到了，一直按着不累嘛",Toast.LENGTH_SHORT).show();
////                    android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(SpeakToitActivity.this);
////                    alert.setTitle("温馨提示：");
////                    alert.setMessage("点击一下说话我就可以听到了，一直按着不累嘛？");
////                    alert.setPositiveButton("                                       确定                                       ", new DialogInterface.OnClickListener() {
////                        @Override
////                        public void onClick(DialogInterface dialog, int which) {
////                        }
////                    });
////                    alert.show();
//                }else{
//                    cancel();       //取消录音
//                    if(speechSynthesizer.getPlayerStatus()==SpeechSynthesizer.PLAYER_STATE_PLAYING){
//                        speechSynthesizer.cancel();     //取消朗读
//                    }
//                    MIROPHONE_STATE=0;
//                    Toast.makeText(getApplicationContext(),"话筒已完成初始化",Toast.LENGTH_SHORT).show();
////                    android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(SpeakToitActivity.this);
////                    alert.setTitle("温馨提示：");
////                    alert.setMessage("话筒已完成初始化，单击话筒即可说话！");
////                    alert.setPositiveButton("                                       确定                                       ", new DialogInterface.OnClickListener() {
////                        @Override
////                        public void onClick(DialogInterface dialog, int which) {
////
////                        }
////                    });
////                    alert.show();
//                }
//
//                return false;
//
//            }
//        });

        menuImgMenu.setOnClickListener(this);
        menuImgKeyborad.setOnClickListener(this);

        //这里是初始数据，在app启动的时候应该是调用本地缓存，读取记录然后添加到mFragmentList
        // 里面，我已经创建了一个方法，只保留最新添加的20次记录，另外这里的读取缓存任务应该是异步任务。这里没写
//        try{
////            dao.deleteUser();
//            dao.createUser();
//        }catch(Exception e){
//            e.printStackTrace();
//        }
        int count=dao.getCount();   //个人账户数据库表中是否有数据，无则count为0，有则count>0
//        pref = PreferenceManager.getDefaultSharedPreferences(this);
//        boolean isNotFirst = pref.getBoolean(LoginActivity.IS_FIRST, false);
        if(count==0){       //数据库中没有数据，第一次登录
            String topShow = getResources().getString(R.string.speak_default_content_1);
            String centerShow = getResources().getString(R.string.speak_default_center);
//            speechSynthesizer.speak(topShow);
//            MIROPHONE_STATE=1;
            speechSynthesizer.speak(topShow);
            mainEditFragment = MainEditFragment.newInstance(topShow, null, MainEditFragment.BUTTON_DEFAULT);
            addFragment2List(mainEditFragment);
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (count==1) {     //数据库中有数据
            if(MyJPushReceiver.isWillOpenMain){
                MyJPushReceiver.isWillOpenMain=false;
            }else {
                //如果是第一次登录
                //获取手机号，但是不一定能获得
//                TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
//                String TeLPN = tm.getLine1Number();
////            String topShow = getResources().getString(R.string.speak_default_content_1);
//                String topShow="主人好，再次见到主人真的好开心啊！";
//            if (!TextUtils.isEmpty(TeLPN)) {
//                topShow += "您的手机号是" + TeLPN + "吗";
//            } else {
//                topShow += "如有帐号请点击‘是'登录，没有请点击‘否’注册";
//            }
//                String centerShow = getResources().getString(R.string.speak_default_center);
//                mainEditFragment = MainEditFragment.newInstance("主人好，再次见到主人真的好开心啊！", "", MainEditFragment.BUTTON_DEFAULT);
//                addFragment2List(mainEditFragment);
//            speechSynthesizer.speak(topShow);
//            MIROPHONE_STATE=1;
            }

        }
        mFragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.setCurrentItem(mFragmentList.size() - 1);
        currentIndex=mFragmentList.size() - 1;

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /**
             * position :下一个页面，及你点击滑动的页面 offset:当前页面偏移的百分比
             * offsetPixels:当前页面偏移的像素位置
             */
            @Override
            public void onPageScrolled(int position, float offset, int offsetPixels) {
                //position是正在滑动，某个界面正在滑动时候会调用此函数
                Log.i(TAG, "onPageScrolled position:"+position+";offset:"+offset);

            }

            @Override
            public void onPageSelected(int position) {
                //back_mark=0;
                if (mFragmentList.get(position) instanceof MainWebFragment) {
                    assetsSet.setVisibility(View.INVISIBLE);
                    assetsUser.setVisibility(View.INVISIBLE);
                } else {
                    assetsSet.setVisibility(View.VISIBLE);
                    assetsUser.setVisibility(View.VISIBLE);
                }
                currentIndex = position;
            }

            /**
             * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。
             */
            @Override
            public void onPageScrollStateChanged(int position) {
//                if (speechSynthesizer.getPlayerStatus() == speechSynthesizer.PLAYER_STATE_PLAYING) {
//                    speechSynthesizer.cancel();
//                }
//                if(position==2){

//                }


            }
        });
        //第一加入界面如果不是edit就设置两个按钮隐藏
        if (mFragmentList.get(mFragmentList.size()-1) instanceof MainWebFragment) {
            assetsSet.setVisibility(View.INVISIBLE);
            assetsUser.setVisibility(View.INVISIBLE);
        }
        //判断用户设置中是否为会话模式，如果是，则将SpeakToIt中的标志isHuihua改为true

        try{
            UserSetting userSetting=userSettingDao.find(dao.findTelephone());
            int num=userSetting.getStateItem1();
            if(num==1){
                SpeakToitActivity.isHuihua=true;
            }else{
                SpeakToitActivity.isHuihua=false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 添加fragment到list
     *
     * @param fragment
     * @return
     */
    private List<Fragment> addFragment2List(Fragment fragment) {
        int mSize = mFragmentList.size();
        if (mSize >= MaxFragmentSize) {
            mFragmentList.remove(0);
            return addFragment2List(fragment);
        }
        mFragmentList.add(fragment);
        return mFragmentList;
    }
    /**
     * Instantiating the drawer view
     */
    private void initDrawerList() {
        dataList = new ArrayList<>();
        String[] functions = getResources().getStringArray(R.array.drawer);     //从xml中获取数据
        int[] groupIcon = {R.drawable.drawer_icon_schedule, R.drawable.drawer_icon_resource,R.drawable.drawer_icon_resource,
                R.drawable.drawer_icon_airplane, R.drawable.drawer_icon_train,
                R.drawable.drawer_icon_navigate, R.drawable.drawer_icon_webbrower,
                R.drawable.drawer_icon_news,R.drawable.drawer_icon_resource,
                R.drawable.drawer_icon_resource,R.drawable.drawer_icon_resource,
                R.drawable.drawer_icon_resource,};
        group=new ArrayList<String>();
        List<String> groupHint=new ArrayList<String>();
        child=new ArrayList<List<String>>();
        addInfo("日程安排",new String[]{"10分钟后提醒我和员工开会","明天下午三点提醒我去见客户","查看日程","修改日程"});
        groupHint.add("比如查看日程");
        addInfo("我需要对接资源和人脉", new String[]{"我要发资源", "我要找关于机械方面的资源","我要找移动互联网方面的项目"});
        groupHint.add("比如我要找关于机械方面的资源");
        addInfo("我需要商务服务", new String[]{"我要找融资担保的供应商", "我需要公司策划方面的服务","我要注册一家新的公司","我想注册商标"});
        groupHint.add("比如我要找融资担保的供应商");
        addInfo("我想订飞机票", new String[]{"我要订飞机票", "我要订从北京到上海的飞机票","我要订下周五从上海到深圳的飞机票"});
        groupHint.add("比如我要订从北京到上海的火车票");
        addInfo("我想订火车票", new String[]{"我要订火车票","我要订从上海到深圳的火车票","我要订下周五从上海到深圳的火车票"});
        groupHint.add("比如我要订从上海到深圳的火车票");
        addInfo("导航", new String[]{"从上海大学到世纪公园怎么走", "帮我找一下从人民广场到徐家汇的路线"});
        groupHint.add("比如上海大学到世纪公园怎么走");
        addInfo("打开网站", new String[]{"打开新浪", "打开搜狐财经", "帮我百度一下马云"});
        groupHint.add("比如打开新浪");
        addInfo("查看新闻", new String[]{"最近有什么重大新闻", "这两天关于新加坡的新闻","帮我找一下关于巴黎恐怖袭击的新闻"});
        groupHint.add("比如帮我找一下关于美国的新闻");

        addInfo("附近预订", new String[]{"我要订附近的酒店", "我要找附近的川菜","我要订外卖"});
        groupHint.add("比如我要订附近的酒店");
        addInfo("打电话发短信", new String[]{"给王打电话", "给王发短信我十分钟后到"});
        groupHint.add("比如给王打电话");
        addInfo("招聘", new String[]{"我要招聘","帮我招聘几个人才"});
        groupHint.add("比如我要招聘");
        addInfo("其他", new String[]{"给我讲个笑话吧","明天天气怎么样", "衣服的英语怎么说","512乘以689等于多少","中国工商银行的股票走势"});
        groupHint.add("比如给我讲个笑话吧");

        adapter=new DrawerListViewAdapter(this,group,child,groupIcon,groupHint);
//        for (int i = 0; i < functions.length; i++) {
//            Map<String, String> map = new HashMap<>();
//            map.put("img", String.valueOf(img[i]));
//            map.put("function", functions[i]);
//            dataList.add(map);
//        }
//        adapter = new SimpleAdapter(this,
//                dataList,
//                R.layout.drawer_list_item,
//                new String[]{"img", "function"},
//                new int[]{R.id.img, R.id.function});
        drawerList.setAdapter(adapter);
        drawerList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String info=child.get(groupPosition).get(childPosition);
                MIROPHONE_STATE=2;
                if(speechSynthesizer.getPlayerStatus()==speechSynthesizer.PLAYER_STATE_PLAYING){
                    speechSynthesizer.cancel();
                }
                Map<String, String> map = new HashMap<>();
//        map = (Map<String, String>) adapter.getItem(position);

                //将识别结果显示在界面上
                mainEditFragment = MainEditFragment.newInstance("", "请稍等，小秘正在努力帮你解答中。。。", info, MainEditFragment.BUTTON_DEFAULT);
                addFragment2List(mainEditFragment);
                mFragmentAdapter.notifyDataSetChanged();
                mViewPager.setCurrentItem(mFragmentList.size() - 1);

                try{
                    System.out.println("准备执行异步线程");
                    ProgressBarAsyncTask asyncTask = new ProgressBarAsyncTask(projectHandler,drawer,mContainer,getApplicationContext(),mainWebFragment,speechSynthesizer,info , mainEditFragment, mFragmentAdapter,mFragmentList,mViewPager);
                    asyncTask.execute(2000);
                    System.out.println("执行异步线程结束");
                }catch(Exception e){
                    System.out.println(e);
                }
                drawer.closeDrawer(Gravity.RIGHT);
                return false;
            }
        });
        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
//                cancel();
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        }
    private void addInfo(String g,String[] c) {
        group.add(g);
//        groupHint.add("“比如我要订明天的从北京到上海的火车票”");
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < c.length; i++) {
            list.add(c[i]);
        }
        child.add(list);
    }
    /*
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode==KeyEvent.KEYCODE_BACK){
            finish();
        }
        return  true;
    }
    */
    public  void onBackPressed(){    //重写back键  使得抽屉回到上一界面
        DrawerLayout mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer);
        if (mDrawerLayout.isDrawerOpen(findViewById(R.id.right_drawer))){
            mDrawerLayout.closeDrawers();
            Log.i(TAG, "onBackPressed 关闭抽屉");
        }
       else {
            finish();
            Log.i(TAG, "onBackPressed 返回键退出程序");
        }

        return;

    }
    @Override
    public void onClick(View view) {
//        cancel();

        int count=dao.getCount();
        if(speechSynthesizer.getPlayerStatus()==speechSynthesizer.PLAYER_STATE_PLAYING){
            speechSynthesizer.cancel();
        }
        switch (view.getId()) {
            case R.id.assets_user:
                if(count!=0){
                    //startActivityForResult(new Intent(this, SettingInformationsActivity.class), SettingInformationsActivity.REQUEST_MAIN_ACTIVITY);
                    startActivityForResult(new Intent(this, MyInformationTopMenu.class), SettingInformationsActivity.REQUEST_MAIN_ACTIVITY);
                }else{
                    android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(SpeakToitActivity.this);
                    alert.setTitle("温馨提示：");
                    alert.setMessage("登录后才可使用此功能");
                    alert.setPositiveButton("                                       确定                                       ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alert.show();
                }
                break;
            case R.id.assets_setting:
                if(count!=0){
                    startActivityForResult(new Intent(this, SettingViewActivity.class), SettingViewActivity.REQUEST_CODE);
                }else{
                    android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(SpeakToitActivity.this);
                    alert.setTitle("温馨提示：");
                    alert.setMessage("登录后才可使用此功能");
                    alert.setPositiveButton("                                       确定                                       ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alert.show();
                }
                break;
            case R.id.menu_bottom_keyboard:
                //Intent intent = this.getIntent();
                //intent.setClass(this, InputViewActivity.class);
                Intent intent=new Intent(SpeakToitActivity.this,InputViewActivity.class);
                intent.putExtra(InputViewActivity.TOP_INFO, "");
                intent.putExtra("tst","");
                startActivityForResult(intent, InputViewActivity.REQUEST_CODE);
                break;
            case R.id.menu_bottom_menu:
                if(count!=0){
                    drawer.openDrawer(Gravity.RIGHT);
//                    startActivityForResult(new Intent(this, ProjectReleaseActivity.class), ProjectReleaseActivity.REQUEST_CODE);

//                    startActivityForResult(new Intent(this, SettingViewActivity.class), SettingViewActivity.REQUEST_CODE);
//                    Intent it=new Intent(this, SettingViewActivity.class);
//                    startActivity(it);
//                    overridePendingTransition(R.anim.activity_open, 0);
                }else{
                    android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(SpeakToitActivity.this);
                    alert.setTitle("温馨提示：");
                    alert.setMessage("登录后才可使用此功能");
                    alert.setPositiveButton("                                       确定                                       ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alert.show();
                }
                break;
            default:
                break;
        }

    }


    @Override
    public void onInputView(String topShow, String bottomShow) {
        Intent intent = new Intent(this, InputViewActivity.class);
        intent.putExtra(InputViewActivity.TOP_INFO, topShow);
        intent.putExtra(InputViewActivity.BOTTOM_INFO, bottomShow);
        startActivityForResult(intent, InputViewActivity.REQUEST_CODE);
    }

    /**
     * 点击产看详细时候的点击事件
     * adg 15/07/24
     */
    @Override
    public void setChickDetailOnClick(int state) {
        switch (state) {
            case MainEditFragment.BUTTON_CALL_PHONE:
                LogUtil.d(TAG, "click BUTTON_CALL_PHONE");
                break;
            case MainEditFragment.BUTTON_DEFAULT:
                LogUtil.d(TAG, "click BUTTON_DEFAULT");
                break;
            case MainEditFragment.BUTTON_DETAIL:
                LogUtil.d(TAG, "click BUTTON_DETAIL");
                break;
            case MainEditFragment.BUTTON_SEND_MESSAGE:
                break;
            case MainEditFragment.BUTTON_YES:
//                startActivityForResult(new Intent(this, LoginActivity.class), LoginActivity.LOGIN_CODE);
                LogUtil.d(TAG, "click BUTTON_YES");
                break;
            case MainEditFragment.BUTTON_D_CALL:
                break;
            default:
                break;
        }
    }
    /**
     * 点击下一条的点击事件
     * adg 15/07/24
     */
    @Override
    public void setNextMessageOnClick(int state) {
        switch (state) {
            case MainEditFragment.BUTTON_CALL_PHONE:
                break;
            case MainEditFragment.BUTTON_DEFAULT:
                LogUtil.d(TAG, "click BUTTON_DEFAULT");
                break;
            case MainEditFragment.BUTTON_DETAIL:
                LogUtil.d(TAG, "click NEXT");
                break;
            case MainEditFragment.BUTTON_SEND_MESSAGE:
                break;
            case MainEditFragment.BUTTON_YES:
                LogUtil.d(TAG, "click BUTTON_YES");
//                startActivityForResult(new Intent(this, RegisterActivity.class), RegisterActivity.REGISTER_HOME_REQUEST_CODE);
                break;
            case MainEditFragment.BUTTON_D_CALL:
                break;
            default:
                break;
        }
    }
    /**
     * 显示条款
     */
    /*private WebView webView;*/
    public void ShowAgreement() {
        LogUtil.d(TAG, "click ShowAgreement");
        String url="http://magic.4350.biz/terms.htm";
        Intent intent=new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
      /*  webView=new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://www.baidu.com");
        setContentView(webView);*/
        /*AgreementPoPWindow(this.getCurrentFocus());*/
    }
    //The listener of the ListView in the right drawer
    //主界面右边隐藏的历史消息记录的单击操作

    /*
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MIROPHONE_STATE=2;
        if(speechSynthesizer.getPlayerStatus()==speechSynthesizer.PLAYER_STATE_PLAYING){
            speechSynthesizer.cancel();
        }
        Map<String, String> map = new HashMap<>();
//        map = (Map<String, String>) adapter.getItem(position);
        String info = map.get("function");
        //将识别结果显示在界面上
        mainEditFragment = MainEditFragment.newInstance("", "请稍等，小秘正在努力帮你解答中。。。", info, MainEditFragment.BUTTON_DEFAULT);
        addFragment2List(mainEditFragment);
        mFragmentAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(mFragmentList.size() - 1);

        try{
            System.out.println("准备执行异步线程");
            ProgressBarAsyncTask asyncTask = new ProgressBarAsyncTask(drawer,mContainer,getApplicationContext(),mainWebFragment,speechSynthesizer,info , mainEditFragment, mFragmentAdapter,mFragmentList,mViewPager);
            asyncTask.execute(2000);
            System.out.println("执行异步线程结束");
        }catch(Exception e){
            System.out.println(e);
        }
        drawer.closeDrawer(Gravity.RIGHT);
    }
    */

    //百度语音识别相关控制函数
    private void start() {
        MIROPHONE_STATE=1;
        //txtLog.setText("");
        //print("点击了“开始”");
        Intent intent = new Intent();
        bindParams(intent);
        /*
       // bindParams(intent);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        {

            String args = sp.getString("args", "");
            if (null != args) {
                //print("参数集：" + args);
               // intent.putExtra("args", args);
            }
        }
        boolean api = sp.getBoolean("api", false);
        if (api) {
            speechEndTime = -1;
            speechRecognizer.startListening(intent);
        } else {
           // intent.setAction("com.baidu.action.RECOGNIZE_SPEECH");
          //  startActivityForResult(intent, REQUEST_UI);
        }
    **/
        //txtResult.setText("");
        intent.putExtra("sample", 8000); // 离线仅支持 16000 采样率
        intent.putExtra("language", "cmn-Hans-CN"); // 离线仅支持中文普通话
//        intent.putExtra("language", "sichuan-Hans-CN"); // 离线仅支持中文普通话
        intent.putExtra("prop", 20000); // 输入
//        intent.putExtra("prop", 10005); // 热词
//        intent.putExtra("prop", 10060); // 地图
//        intent.putExtra("prop", 10001); // 音乐
//        intent.putExtra("prop", 10003); // 应用
//        intent.putExtra("prop", 10004); //网页
//        intent.putExtra("prop", 10006); //购物
        intent.putExtra("prop", 10008); // 电话
//        intent.putExtra("prop", 10054); //娱乐餐饮
//        intent.putExtra("prop", 10055); //财经
//        intent.putExtra("prop", 10056); //游戏
//        intent.putExtra("prop", 10057); //菜谱
//        intent.putExtra("prop", 10058); //助手
//        intent.putExtra("prop", 100014); // 联系人
//        intent.putExtra("prop", 100016); // 手机设置
//        intent.putExtra("prop", 100018); // 电视指令
//        intent.putExtra("prop", 100019); // 播放器指令
//        intent.putExtra("prop", 100020); // 收音机指令
//        intent.putExtra("prop", 100021); // 命令词
//        intent.putExtra("asr-base-file-path", getApplicationInfo().dataDir+"/lib/libs_1.so");
//        intent.putExtra("asr-base-file-path", getApplicationInfo().dataDir+"/res/raw/s_1");
        // 语音输入附加资源，value替换为资源文件实际路径
//        intent.putExtra("lm-res-file-path", getApplicationInfo().dataDir+"/lib/libs_2.so");
        intent.setAction("com.baidu.action.RECOGNIZE_SPEECH");
        speechRecognizer.startListening(intent);
    }

    private void stop() {
       // speechRecognizer.stopListening();
        Log.i("MIROPHONE_STATE-3", MIROPHONE_STATE + "");
        if(MIROPHONE_STATE==1){
            speechRecognizer.stopListening();
        }
        MIROPHONE_STATE=2;
        Log.i("MIROPHONE_STATE-4", MIROPHONE_STATE + "");

        //print("点击了“说完了”");

    }

    public void cancel() {
        if(MIROPHONE_STATE==1){
            speechRecognizer.cancel();
        }
        status = STATUS_None;
        MIROPHONE_STATE=0;
        //将话筒画面 置为初始状态
        ViewGroup.LayoutParams param = menuVoiceBackgroud.getLayoutParams();
        param.height = 0;
        param.width = 0;
        menuVoiceBackgroud.setLayoutParams(param);
        menuImgMicrophone.setImageDrawable(BackgroudChangeUtil.
                getDrawable(getBaseContext(), BitmapFactory.decodeResource(getResources(), R.drawable.microphone_control),
                        getResources().getString(R.string.rgb_prepare_microphone_not_press), getResources().getString(R.string.rgb_prepare_microphone_press)));
        menuImgMicrophone.setBackgroundColor(Color.parseColor("#00000000"));
        menuImgMicrophone.setContentDescription("0");
        //print("点击了“取消”");
    }

    public void bindParams(Intent intent) {

        intent.putExtra(Constant.EXTRA_OFFLINE_SLOT_DATA, buildTestSlotData());

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor e=sp.edit();
        e.putString(Constant.EXTRA_SAMPLE,"8000");

        if (sp.getBoolean("tips_sound", false)) {
            intent.putExtra(Constant.EXTRA_SOUND_START, R.raw.bdspeech_recognition_start);
            intent.putExtra(Constant.EXTRA_SOUND_END, R.raw.bdspeech_speech_end);
            intent.putExtra(Constant.EXTRA_SOUND_SUCCESS, R.raw.bdspeech_recognition_success);
            intent.putExtra(Constant.EXTRA_SOUND_ERROR, R.raw.bdspeech_recognition_error);
            intent.putExtra(Constant.EXTRA_SOUND_CANCEL, R.raw.bdspeech_recognition_cancel);
        }
        if (sp.contains(Constant.EXTRA_INFILE)) {
            String tmp = sp.getString(Constant.EXTRA_INFILE, "").replaceAll(",.*", "").trim();
            intent.putExtra(Constant.EXTRA_INFILE, tmp);
        }
//        if (sp.getBoolean(Constant.EXTRA_OUTFILE, true)) {

            intent.putExtra(Constant.EXTRA_OUTFILE, "sdcard/outfile.pcm");
//        }

        if (sp.contains(Constant.EXTRA_SAMPLE)) {
            String tmp = sp.getString(Constant.EXTRA_SAMPLE, "8000").replaceAll(",.*", "").trim();
            if (null != tmp && !"".equals(tmp)) {
                intent.putExtra(Constant.EXTRA_SAMPLE, Integer.parseInt(tmp));
            }
        }
        if (sp.contains(Constant.EXTRA_LANGUAGE)) {
            String tmp = sp.getString(Constant.EXTRA_LANGUAGE, "").replaceAll(",.*", "").trim();
            if (null != tmp && !"".equals(tmp)) {
                intent.putExtra(Constant.EXTRA_LANGUAGE, tmp);
            }
        }
        if (sp.contains(Constant.EXTRA_NLU)) {
            String tmp = sp.getString(Constant.EXTRA_NLU, "").replaceAll(",.*", "").trim();
            if (null != tmp && !"".equals(tmp)) {
                intent.putExtra(Constant.EXTRA_NLU, tmp);
            }
        }

        if (sp.contains(Constant.EXTRA_VAD)) {
            String tmp = sp.getString(Constant.EXTRA_VAD, "").replaceAll(",.*", "").trim();
            if (null != tmp && !"".equals(tmp)) {
                intent.putExtra(Constant.EXTRA_VAD, tmp);
            }
        }
        String prop = null;
        if (sp.contains(Constant.EXTRA_PROP)) {
            String tmp = sp.getString(Constant.EXTRA_PROP, "").replaceAll(",.*", "").trim();
            if (null != tmp && !"".equals(tmp)) {
                intent.putExtra(Constant.EXTRA_PROP, Integer.parseInt(tmp));
                prop = tmp;
            }
        }

        // offline asr
        /*
        {
            intent.putExtra(Constant.EXTRA_OFFLINE_ASR_BASE_FILE_PATH, "/sdcard/easr/s_1");
            intent.putExtra(Constant.EXTRA_LICENSE_FILE_PATH, "/sdcard/easr/license-tmp-20150530.txt");
            if (null != prop) {
                int propInt = Integer.parseInt(prop);
                if (propInt == 10060) {
                    intent.putExtra(Constant.EXTRA_OFFLINE_LM_RES_FILE_PATH, "/sdcard/easr/s_2_Navi");
                } else if (propInt == 20000) {
                    intent.putExtra(Constant.EXTRA_OFFLINE_LM_RES_FILE_PATH, "/sdcard/easr/s_2_InputMethod");
                }
            }

        }
        */
        intent.putExtra(Constant.EXTRA_SAMPLE, Integer.parseInt("8000"));
    }

    private String buildTestSlotData() {
        JSONObject slotData = new JSONObject();
        JSONArray name = new JSONArray().put("丁家星").put("吴礼通");
        JSONArray song = new JSONArray().put("七里香").put("发如雪");
        JSONArray artist = new JSONArray().put("周杰伦").put("李世龙");
        JSONArray app = new JSONArray().put("手机百度").put("百度地图");
        JSONArray usercommand = new JSONArray().put("关灯").put("开门");
        try {
            slotData.put(Constant.EXTRA_OFFLINE_SLOT_NAME, name);
            slotData.put(Constant.EXTRA_OFFLINE_SLOT_SONG, song);
            slotData.put(Constant.EXTRA_OFFLINE_SLOT_ARTIST, artist);
            slotData.put(Constant.EXTRA_OFFLINE_SLOT_APP, app);
            slotData.put(Constant.EXTRA_OFFLINE_SLOT_USERCOMMAND, usercommand);
        } catch (JSONException e) {

        }
        return slotData.toString();
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        try{
            if(speechSynthesizer.getPlayerStatus()==speechSynthesizer.PLAYER_STATE_PLAYING){
                speechSynthesizer.cancel();
            }
            status = STATUS_Ready;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBeginningOfSpeech() {
        try {
            if(speechSynthesizer.getPlayerStatus()==speechSynthesizer.PLAYER_STATE_PLAYING){
                speechSynthesizer.cancel();
            }
            status = STATUS_Speaking;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRmsChanged(float f) {
        //f 变化范围为 从 0  到 1万
        try{
            if(speechSynthesizer.getPlayerStatus()==speechSynthesizer.PLAYER_STATE_PLAYING){
                speechSynthesizer.cancel();
            }
            final int VTAG = 0xFF00AA01;
            Integer rawHeight = (Integer) speechWave.getTag(VTAG);
            if (rawHeight == null) {
                rawHeight = speechWave.getLayoutParams().height;
                speechWave.setTag(VTAG, rawHeight);
            }

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) speechWave.getLayoutParams();
            params.height = (int) (rawHeight * f * 0.01);
            params.height = Math.max(params.height , speechWave.getMeasuredWidth());
            speechWave.setLayoutParams(params);

            Log.d(TAG, "onRmsChanged:" + f);
            /*
            if(MIROPHONE_STATE==1||MIROPHONE_STATE==0){
//        if(MIROPHONE_STATE==1){
                if(f!=0){
                    Message msg;
                    Bundle data;
                    int radio = (int)(f / 600);
                    int db = 0;
                    if (radio > 0)
                        db = (int) (20 * Math.log10(radio));
                    switch (db / 4) {
                        case 0:
                            // System.out.println("DB:" + db);
                            msg = new Message();
                            data = new Bundle();
                            data.putInt("color", 1);
                            msg.setData(data);
                            msg.what = 1;
                            //handler.sendMessage(msg);
                            break;
                        default:
                            //System.out.println("DB:" + db);
                            msg = new Message();
                            data = new Bundle();
                            data.putInt("color", 2 * db);
                            msg.setData(data);
                            msg.what = 1;
                            //handler.sendMessage(msg);
                            //time=0;
                            break;
                    }
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        System.out.println("try..catch捕获到了异常");
                    }
                    if (msg.what == 1) {
                        //将话筒描为 橙红色色背景
                        menuImgMicrophone.setImageDrawable(BackgroudChangeUtil.
                                getDrawable(getBaseContext(), BitmapFactory.decodeResource(getResources(), R.drawable.microphone_control),
                                        getResources().getString(R.string.rgb_start_microphone_not_press), getResources().getString(R.string.rgb_start_microphone_press)));
                        menuImgMicrophone.setBackgroundResource(R.drawable.menu_bottom_microphone_backgroud_change);

                        //话筒周围的灰色圆圈，根据用户说话声音的大小来变化
                        ViewGroup.LayoutParams param = menuVoiceBackgroud.getLayoutParams();
                        int addPx = msg.getData().getInt(VoiceUtil.BACK_INFO);
                        param.height = menuImgMicrophone.getHeight() + 2 * addPx + 1;
                        param.width = menuImgMicrophone.getHeight() + 2 * addPx + 1;
                        menuVoiceBackgroud.setLayoutParams(param);
                    } else if (msg.what == 0) {
                        //将话筒画面 置为初始状态
                        ViewGroup.LayoutParams param = menuVoiceBackgroud.getLayoutParams();
                        param.height = 0;
                        param.width = 0;
                        menuVoiceBackgroud.setLayoutParams(param);
                        menuImgMicrophone.setImageDrawable(BackgroudChangeUtil.
                                getDrawable(getBaseContext(), BitmapFactory.decodeResource(getResources(), R.drawable.microphone_control),
                                        getResources().getString(R.string.rgb_prepare_microphone_not_press), getResources().getString(R.string.rgb_prepare_microphone_press)));
                        menuImgMicrophone.setBackgroundColor(Color.parseColor("#00000000"));
                        menuImgMicrophone.setContentDescription("0");
                    }

                }
            }
            else if(MIROPHONE_STATE==2){
                //将话筒画面 置为初始状态
                ViewGroup.LayoutParams param = menuVoiceBackgroud.getLayoutParams();
                param.height = 0;
                param.width = 0;
                menuVoiceBackgroud.setLayoutParams(param);
                menuImgMicrophone.setImageDrawable(BackgroudChangeUtil.
                        getDrawable(getBaseContext(), BitmapFactory.decodeResource(getResources(), R.drawable.microphone_control),
                                getResources().getString(R.string.rgb_prepare_microphone_not_press), getResources().getString(R.string.rgb_prepare_microphone_press)));
                menuImgMicrophone.setBackgroundColor(Color.parseColor("#00000000"));
                menuImgMicrophone.setContentDescription("0");
            }
            else if(MIROPHONE_STATE==3){
                //当处于录回复状态时，声音变化则什么也不做
            }
            */
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {
        try{
            speechEndTime = System.currentTimeMillis();
            status = STATUS_Recognition;
//        Toast.makeText(App.getContext(),"声音录取完毕",Toast.LENGTH_SHORT).show();
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ViewGroup.LayoutParams param = menuVoiceBackgroud.getLayoutParams();
            param.height = 0;
            param.width = 0;
            menuVoiceBackgroud.setLayoutParams(param);
            menuImgMicrophone.setImageDrawable(BackgroudChangeUtil.
                    getDrawable(getBaseContext(), BitmapFactory.decodeResource(getResources(), R.drawable.microphone_control),
                            getResources().getString(R.string.rgb_prepare_microphone_not_press), getResources().getString(R.string.rgb_prepare_microphone_press)));
            menuImgMicrophone.setBackgroundColor(Color.parseColor("#00000000"));
            menuImgMicrophone.setContentDescription("0");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onError(int error) {
        try{
            if(speechSynthesizer.getPlayerStatus()==speechSynthesizer.PLAYER_STATE_PLAYING){
                speechSynthesizer.cancel();
            }
            ProgressBarAsyncTask.isUploadVoice=false;
            status = STATUS_None;
            StringBuilder sb = new StringBuilder();
            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    sb.append("音频问题");
//                    Toast.makeText(this,"音频问题",Toast.LENGTH_SHORT).show();
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    sb.append("没有语音输入");
//                    Toast.makeText(this,"没有语音输入",Toast.LENGTH_SHORT).show();
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    sb.append("网络问题");
//                    Toast.makeText(this,"网络问题",Toast.LENGTH_SHORT).show();
                    break;

                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    sb.append("权限不足");
//                    Toast.makeText(this,"权限不足",Toast.LENGTH_SHORT).show();
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    sb.append("没有匹配的识别结果");
//                    Toast.makeText(this,"没有匹配的识别结果",Toast.LENGTH_SHORT).show();
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    sb.append("引擎忙");
//                     Toast.makeText(this,"引擎忙",Toast.LENGTH_SHORT).show();
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    sb.append("服务端错误");
//                    Toast.makeText(this,"服务端错误",Toast.LENGTH_SHORT).show();
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    sb.append("连接超时");
//                    Toast.makeText(this,"连接超时",Toast.LENGTH_SHORT).show();
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    sb.append("我听不到您的声音");
//                    Toast.makeText(this,"其它客户端错误",Toast.LENGTH_SHORT).show();
                    break;
            }
            MIROPHONE_STATE=0;
            //将话筒视图置为初始状态
            ViewGroup.LayoutParams param = menuVoiceBackgroud.getLayoutParams();
            param.height = 0;
            param.width = 0;
            menuVoiceBackgroud.setLayoutParams(param);
            menuImgMicrophone.setImageDrawable(BackgroudChangeUtil.
                    getDrawable(getBaseContext(), BitmapFactory.decodeResource(getResources(), R.drawable.microphone_control),
                            getResources().getString(R.string.rgb_prepare_microphone_not_press), getResources().getString(R.string.rgb_prepare_microphone_press)));
            menuImgMicrophone.setBackgroundColor(Color.parseColor("#00000000"));
            menuImgMicrophone.setContentDescription("0");

            if(error!=SpeechRecognizer.ERROR_RECOGNIZER_BUSY&&error!=SpeechRecognizer.ERROR_NO_MATCH){

                //向用户显示出错信息
//                speechSynthesizer.speak(sb.toString()+"，可能是因为您的网络未连接，请稍后重试！");
                mainEditFragment = MainEditFragment.newInstance("", sb.toString()+"，可能是网络不稳定，请稍后重试！", "", MainEditFragment.BUTTON_DEFAULT);
                addFragment2List(mainEditFragment);
                mFragmentAdapter.notifyDataSetChanged();
                mViewPager.setCurrentItem(mFragmentList.size() - 1);
            }

            sb.append(":" + error);
            Log.i("错误内容", sb.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        try{
            if (mFragmentList.get(currentIndex) instanceof MainWebFragment) {
                assetsSet.setVisibility(View.INVISIBLE);
                assetsUser.setVisibility(View.INVISIBLE);
            }
        }catch (Exception e){
            currentIndex=-1;
            try {
                MainEditFragment mainEditFragment = MainEditFragment.newInstance("", "欢迎回来，我能为您做什么呢", "", MainEditFragment.BUTTON_DEFAULT);
                addFragment2List(mainEditFragment);
                mFragmentAdapter.notifyDataSetChanged();
                mViewPager.setCurrentItem(mFragmentList.size() - 1);
                currentIndex=mFragmentList.size() - 1;
                e.printStackTrace();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            try{
                if(MIROPHONE_STATE==0){
                    //                    如果是从通知栏的话筒按钮打开应用的话，判断一下是否要打开话筒
                    //Boolean flag=getIntent().getBooleanExtra(NotificationBroadcastReceiver.OPENMICROPHONEFLAG,false);
                    if(NotificationBroadcastReceiver.flagIsOpenMicrophone){
                        //this.onBackPressed();
                        start();
                        NotificationBroadcastReceiver.flagIsOpenMicrophone=false;
                    }

//                    if(userSettingDao.find(dao.findTelephone()).getStateItem2()==1){
//                        start();
//                    }else {
//
//                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            //取消所有通知
            NotificationManager nm= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//            nm.cancelAll();
            nm.cancel(1001);
            JPushInterface.resumePush(this);
            //清除推送中的内容
//            JPushInterface.clearAllNotifications(this);
            MyJPushReceiver.list.clear();
//            JPushInterface.clearNotificationById(this, 100000000);
            isForeground = true;
            PollingService.stopPollingFlag=false;

//            pollingService.startPolling();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //back_mark=0;
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(4*1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                pollingService.startPolling();
//            }
//        }).start();
        //当app 在后台运行时，客服给app发web消息，当app读出来之后，再打开app就会出现 左上角 和 右上角 两个 按钮就会显示出来
        //下面两行代码 是为了 避免 他们 显示 出来
        try{
            if (mFragmentList.get(currentIndex) instanceof MainWebFragment) {
                assetsSet.setVisibility(View.INVISIBLE);
                assetsUser.setVisibility(View.INVISIBLE);
            }
            //将数据库中存储的 客服回复结果列表 拿出来放到本地类变量 list中
            artificialAnswerList=artificialAnswerDao.findAll();
            artificialAnswerDao.deleteAll();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onResults(Bundle results) {
        try{
            if(MIROPHONE_STATE==1){
                cancel();
                Log.i(TAG, "onResults MIROPHONE_STATE==1查看stop01");
//                stop();
                ViewGroup.LayoutParams param = menuVoiceBackgroud.getLayoutParams();
                param.height = 0;
                param.width = 0;
                menuVoiceBackgroud.setLayoutParams(param);
                menuImgMicrophone.setImageDrawable(BackgroudChangeUtil.
                        getDrawable(getBaseContext(), BitmapFactory.decodeResource(getResources(), R.drawable.microphone_control),
                                getResources().getString(R.string.rgb_prepare_microphone_not_press), getResources().getString(R.string.rgb_prepare_microphone_press)));
                menuImgMicrophone.setBackgroundColor(Color.parseColor("#00000000"));
                menuImgMicrophone.setContentDescription("0");
            }
            MIROPHONE_STATE=2;
            long end2finish = System.currentTimeMillis() - speechEndTime;
            status = STATUS_None;
            ArrayList<String> nbest = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            Log.i("识别成功：" ,Arrays.toString(nbest.toArray(new String[nbest.size()])));
            String json_res = results.getString("origin_result");
            try {
                Log.i("origin_result=\n", new JSONObject(json_res).toString(4));
            } catch (Exception e) {
                //Log.i("origin_result=[warning: bad json]\n",json_res);
            }
            Log.i("开始","");
            String strEnd2Finish = "";
            if (end2finish < 60 * 1000) {
                strEnd2Finish = "(waited " + end2finish + "ms)";
            }
            Log.i(nbest.get(0), strEnd2Finish);
            Log.i("识别结果：", nbest.get(0));
            endResult=nbest.get(0);
//        content=null;
            //将识别结果显示在界面上
            mainEditFragment = MainEditFragment.newInstance("", "请稍等，小秘正在努力帮你解答中。。。", endResult, MainEditFragment.BUTTON_DEFAULT);
            addFragment2List(mainEditFragment);
            mFragmentAdapter.notifyDataSetChanged();

            mViewPager.setCurrentItem(mFragmentList.size() - 1);

            try{
                System.out.println("准备执行异步线程");
                ProgressBarAsyncTask.isUploadVoice=true;
                ProgressBarAsyncTask asyncTask = new ProgressBarAsyncTask(projectHandler,drawer,mContainer,getApplicationContext(),mainWebFragment,speechSynthesizer,endResult , mainEditFragment, mFragmentAdapter,mFragmentList,mViewPager);
                asyncTask.execute(2000);
                System.out.println("执行异步线程结束");
            }catch(Exception e){
                System.out.println(e);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

//        }

    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        try{
            /*
            ArrayList<String> nbest = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if (nbest.size() > 0) {
                //print("~临时识别结果：" + Arrays.toString(nbest.toArray(new String[0])));
                //txtResult.setText(nbest.get(0));
                //log.i("result",Arrays.toString(nbest.toArray(new String[0])));
                Log.i(TAG, Arrays.toString(nbest.toArray(new String[0])));
                MainEditFragment mainEditFragment2=null;
                if (mFragmentList.get(currentIndex) instanceof MainEditFragment) {
                    mainEditFragment2= (MainEditFragment) mFragmentList.get(currentIndex);
                }
                mainEditFragment2.centerShow.setText(Arrays.toString(nbest.toArray(new String[0])));
            }
            */
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        try{
            switch (eventType) {
                case EVENT_ERROR:
                    String reason = params.get("reason") + "";
                    Log.i("EVENT_ERROR, " , reason);
                    break;
                case VoiceRecognitionService.EVENT_ENGINE_SWITCH:
                    int type = params.getInt("engine_type");
                    Log.i("*引擎切换至" ,(type == 0 ? "在线" : "离线"));
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            JPushInterface.init(getApplicationContext());

            speechRecognizer.destroy();
            if(artificialAnswerList.size()!=0){
                //如果当前类变量中的列表中还有值，就把这些结果放到 数据库中 以防客服回复的结果丢失-
                artificialAnswerDao.deleteAll();
                artificialAnswerDao.add(artificialAnswerList);
            }

//            unbindService(conn);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    //百度语音合成相关函数
    public void preHecheng(){
        try {
            // 部分版本不需要BDSpeechDecoder_V1
            try {
                userSettingDao=new UserSettingDao(this);
                System.loadLibrary("gnustl_shared");
                System.loadLibrary("BDSpeechDecoder_V1");
                System.loadLibrary("bd_etts");
                System.loadLibrary("bds");
            } catch (UnsatisfiedLinkError e) {
                SpeechLogger.logD("load BDSpeechDecoder_V1 failed, ignore");
            }


            speechSynthesizer =
                    SpeechSynthesizer.newInstance(SpeechSynthesizer.SYNTHESIZER_AUTO, getApplicationContext(), "holder",
                            this);
            // 请替换为语音开发者平台注册应用得到的apikey和secretkey (在线授权)
            speechSynthesizer.setApiKey("NblCxGR38UsVdz0NXSE4dOH2", "41578a57753b516a9650e97e544ebe67");
            // 请替换为语音开发者平台上注册应用得到的App ID (离线授权)
            speechSynthesizer.setAppId("6548586");
            // 设置授权文件路径
            // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_LICENCE_FILE, LICENCE_FILE_NAME);
            // TTS所需的资源文件，可以放在任意可读目录，可以任意改名
            String ttsTextModelFilePath =
                    getApplicationContext().getApplicationInfo().dataDir + "/lib/libbd_etts_text.dat.so";
            String ttsSpeechModelFilePath =
                    getApplicationContext().getApplicationInfo().dataDir + "/lib/libbd_etts_speech_female.dat.so";
            speechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, ttsTextModelFilePath);
            speechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, ttsSpeechModelFilePath);
            DataInfoUtils.verifyDataFile(ttsTextModelFilePath);
            DataInfoUtils.getDataFileParam(ttsTextModelFilePath, DataInfoUtils.TTS_DATA_PARAM_DATE);
            DataInfoUtils.getDataFileParam(ttsTextModelFilePath, DataInfoUtils.TTS_DATA_PARAM_SPEAKER);
            DataInfoUtils.getDataFileParam(ttsTextModelFilePath, DataInfoUtils.TTS_DATA_PARAM_GENDER);
            DataInfoUtils.getDataFileParam(ttsTextModelFilePath, DataInfoUtils.TTS_DATA_PARAM_CATEGORY);
            DataInfoUtils.getDataFileParam(ttsTextModelFilePath, DataInfoUtils.TTS_DATA_PARAM_LANGUAGE);
            speechSynthesizer.initEngine();
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
//        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onStartWorking(SpeechSynthesizer speechSynthesizer) {
        try{
            if(userSettingDao.find(dao.findTelephone()).getStateItem3()==0){
                MIROPHONE_STATE=0;
                speechSynthesizer.cancel();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        MIROPHONE_STATE=2;
        Log.i(TAG, "onStartWorking :开始工作，请等待数据...");    
    }

    @Override
    public void onSpeechStart(SpeechSynthesizer speechSynthesizer) {
        try {
            MIROPHONE_STATE=3;
            Log.i(TAG, "onSpeechStart :朗读开始");
        /*
        menuImgMicrophone.setImageDrawable(BackgroudChangeUtil.
                getDrawable(getBaseContext(), BitmapFactory.decodeResource(getResources(), R.drawable.voice_horn),
                        getResources().getString(R.string.rgb_prepare_microphone_not_press), getResources().getString(R.string.rgb_prepare_microphone_press)));
                        */
            menuImgMicrophone.setImageResource(R.drawable.voice_horn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNewDataArrive(SpeechSynthesizer speechSynthesizer, byte[] bytes, boolean b) {
        try {
            MIROPHONE_STATE=3;
            Log.i(TAG, "onNewDataArrive 新的音频数据： " + bytes.length + (b ? ("end") : ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBufferProgressChanged(SpeechSynthesizer speechSynthesizer, int i) {
//        uiHandler.sendMessage(uiHandler.obtainMessage(UI_TOAST, "缓冲进度" + i));
    }

    @Override
    public void onSpeechProgressChanged(SpeechSynthesizer speechSynthesizer, int i) {
//        uiHandler.sendMessage(uiHandler.obtainMessage(UI_TOAST, "朗读进度" + i));
    }

    @Override
    public void onSpeechPause(SpeechSynthesizer speechSynthesizer) {
        MIROPHONE_STATE=0;
        Log.i(TAG, "onSpeechPause :朗读已暂停"); 
    }

    @Override
    public void onSpeechResume(SpeechSynthesizer speechSynthesizer) {
        MIROPHONE_STATE=3;
        Log.i(TAG, "onSpeechResume :朗读继续");
    }

    @Override
    public void onCancel(SpeechSynthesizer speechSynthesizer) {
        try {
            Log.i(TAG, "onCancel :已取消");
            MIROPHONE_STATE=0;
            //初始化话筒
            ViewGroup.LayoutParams param = menuVoiceBackgroud.getLayoutParams();
            param.height = 0;
            param.width = 0;
            menuVoiceBackgroud.setLayoutParams(param);
            menuImgMicrophone.setImageDrawable(BackgroudChangeUtil.
                    getDrawable(getBaseContext(), BitmapFactory.decodeResource(getResources(), R.drawable.microphone_control),
                            getResources().getString(R.string.rgb_prepare_microphone_not_press), getResources().getString(R.string.rgb_prepare_microphone_press)));
            menuImgMicrophone.setBackgroundColor(Color.parseColor("#00000000"));
            menuImgMicrophone.setContentDescription("0");
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSynthesizeFinish(SpeechSynthesizer speechSynthesizer) {
        MIROPHONE_STATE=3;
        Log.i(TAG, "onSynthesizeFinish :合成已完成");    
    }

    @Override
    public void onSpeechFinish(SpeechSynthesizer speechSynthesizer) {
        try {
            Log.i(TAG, "onSpeechFinish :朗读已停止");
            MIROPHONE_STATE=0;
            //初始化话筒
            ViewGroup.LayoutParams param = menuVoiceBackgroud.getLayoutParams();
            param.height = 0;
            param.width = 0;
            menuVoiceBackgroud.setLayoutParams(param);
            menuImgMicrophone.setImageDrawable(BackgroudChangeUtil.
                    getDrawable(getBaseContext(), BitmapFactory.decodeResource(getResources(), R.drawable.microphone_control),
                            getResources().getString(R.string.rgb_prepare_microphone_not_press), getResources().getString(R.string.rgb_prepare_microphone_press)));
            menuImgMicrophone.setBackgroundColor(Color.parseColor("#00000000"));
            menuImgMicrophone.setContentDescription("0");

            if(mFragmentList.get(mViewPager.getCurrentItem()) instanceof MainEditFragment){
                MainEditFragment fragment= (MainEditFragment) mFragmentList.get(mViewPager.getCurrentItem());
                if (MIROPHONE_STATE!=1&fragment.getButtonState()&isHuihua&!drawer.isDrawerOpen(Gravity.RIGHT)&getTopActivity(this).equals("ComponentInfo{biz.zm.magic/biz.home.SpeakToitActivity}")) {
                    start();
                }else{

                }
            }else{
                MIROPHONE_STATE=0;
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(SpeechSynthesizer speechSynthesizer, SpeechError speechError) {
        Log.i(TAG, "onError 发生错误：" + speechError);
        MIROPHONE_STATE=0;
    }

    //MainPreMessageFragment里面定义的接口，此处为接口回调
    @Override
    public void onButtonClick(String name, String tel, int position) {
        Log.i(TAG, "onButtonClick :name,tel,position:"+name+tel+position);
        String content=magicResult.getResultFunctional().getMessage().getContent();
        MainMessageFragment mc=MainMessageFragment.newInstance(name,tel,content);
        //调用添加方法
        addFragment2List(mc);
        mFragmentAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(mFragmentList.size() - 1);
        mFragmentList.remove(mFragmentList.size() - 2);
        mFragmentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMessageSendClick(String s) {
        MainEditFragment mc=new MainEditFragment().newInstance("", s, "", MainEditFragment.BUTTON_DEFAULT);
        addFragment2List(mc);
        mFragmentAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(mFragmentList.size() - 1);
        mFragmentList.remove(mFragmentList.size() - 2);
        mFragmentAdapter.notifyDataSetChanged();
        speechSynthesizer.speak(s);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            DrawerLayout mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer);
            if (mDrawerLayout.isDrawerOpen(findViewById(R.id.right_drawer))){
                mDrawerLayout.closeDrawers();
                Log.i(TAG, "onKeyDown 关闭抽屉");
            }else {
                if(mFragmentList.size()>0){
                    //如果当前页面为 MainWebFragment，点击屏幕上的返回按钮时，返回到浏览器的上个页面
                    if(mFragmentList.get(mViewPager.getCurrentItem()) instanceof MainWebFragment){
                        WebView mainWEb=((MainWebFragment) mFragmentList.get(mViewPager.getCurrentItem())).mainWEb;
                        if (mainWEb.canGoBack()) {
                            mainWEb.goBack();
                        }else{
                            changeLastPage();
                            //back_mark++;
                        /*
                        int index=mViewPager.getCurrentItem();
                        if(index>0){
                            mViewPager.setCurrentItem(index - 1);
                            mFragmentAdapter.notifyDataSetChanged();
                           // currentIndex=index - 1;
                        }else{
                            Toast.makeText(this,"没有更多的消息记录了",Toast.LENGTH_SHORT).show();
                        }
                        */
                        }
                    }else{
                        changeLastPage();
                        //                finish();
                        //back_mark++;
                    /*
                    int index=mViewPager.getCurrentItem();
                    if(index>0){
                        mViewPager.setCurrentItem(index - 1);
                        mFragmentAdapter.notifyDataSetChanged();
                        //currentIndex=index - 1;
                    }else{
                        Toast.makeText(this,"没有更多的消息记录了",Toast.LENGTH_SHORT).show();
                    }
                    */
                    }
                }else{
                    finish();
                }

                /*
                if(back_mark==1){
                    Toast.makeText(this,"再点击一次退出程序",Toast.LENGTH_SHORT).show();
                }else if(back_mark>1){
                    moveTaskToBack(false);
                    Log.i(TAG, "onKeyDown 返回键退出程序");
                }
                */
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onChickButtonClick(MagicResultResourceInfo resourceInfo) {
        OpenResourceInfoTask task=new OpenResourceInfoTask(SpeakToitActivity.this,resourceInfo.getPostId());
        task.execute(1000);
//        Intent intent=new Intent(SpeakToitActivity.this, ResourceInfo.class);
//        intent.putExtra("resourceInfo",resourceInfo);
//        startActivity(intent);

        /*
        MainWebFragment mc=MainWebFragment.newInstance(url,textShow,magicResult.getQuestion());
        //调用添加方法
        addFragment2List(mc);
        mFragmentAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(mFragmentList.size() - 1);
//        mFragmentList.remove(mFragmentList.size() - 2);
        mFragmentAdapter.notifyDataSetChanged();
        */
    }

    @Override
    public void onNextMessageButtonClick(String again) {
        if(speechSynthesizer.getPlayerStatus()==speechSynthesizer.PLAYER_STATE_PLAYING){
            speechSynthesizer.cancel();
        }
        endResult=again;
        mainEditFragment = MainEditFragment.newInstance("", null, endResult, MainEditFragment.BUTTON_DEFAULT);
        addFragment2List(mainEditFragment);
        mFragmentAdapter.notifyDataSetChanged();

        mViewPager.setCurrentItem(mFragmentList.size() - 1);

        try{
            System.out.println("准备执行异步线程");
            ProgressBarAsyncTask asyncTask = new ProgressBarAsyncTask(projectHandler,drawer,mContainer,getApplicationContext(),mainWebFragment,speechSynthesizer,endResult , mainEditFragment, mFragmentAdapter,mFragmentList,mViewPager);
            asyncTask.execute(2000);
            System.out.println("执行异步线程结束");
        }catch(Exception e){
            System.out.println(e);
        }
    }
    public void executeInfo(String info){
        //将识别结果显示在界面上
        MainEditFragment mainEditFragment = MainEditFragment.newInstance("", "请稍等，小秘正在努力帮你解答中。。。", info, MainEditFragment.BUTTON_DEFAULT);
        addFragment2List(mainEditFragment);
        mFragmentAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(mFragmentList.size() - 1);
        try{
            System.out.println("准备执行异步线程");
            ProgressBarAsyncTask asyncTask = new ProgressBarAsyncTask(projectHandler,drawer,mContainer,getApplicationContext(),mainWebFragment,speechSynthesizer,info , mainEditFragment, mFragmentAdapter,mFragmentList,mViewPager);
            asyncTask.execute(2000);
            System.out.println("执行异步线程结束");
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Override
    public void OnSendProjectButtonClick(String title, String content,ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(10);
        speechSynthesizer.speak("请稍等，正在为您发布资源。");
        MagicResultResourceInfo resourceInfo=new MagicResultResourceInfo();
        resourceInfo.setTitle(title);
        resourceInfo.setContent(content);
        resourceInfo.setConfirm(true);
        progressBar.setProgress(15);
        UploadFileTask task=new UploadFileTask(progressBar, resourceInfo,mContainer,getApplicationContext(),mainWebFragment,speechSynthesizer,endResult, mainEditFragment, mFragmentAdapter, mFragmentList, mViewPager) ;
        task.execute();
    }

    @Override
    public void onClickContactName(String messageContent) {
        magicResult.getResultFunctional().getMessage().setContent(messageContent);
        ChangeMessageNameTask task=new ChangeMessageNameTask(getApplicationContext(),magicResult,mainWebFragment,speechSynthesizer,endResult, mainEditFragment, mFragmentAdapter, mFragmentList, mViewPager);
        task.execute(2000);
    }

    @Override
    public void onMessageCancelClick(String s) {
        MainEditFragment mc=new MainEditFragment().newInstance("",s,"",MainEditFragment.BUTTON_DEFAULT);
        addFragment2List(mc);
        mFragmentAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(mFragmentList.size() - 1);
        mFragmentList.remove(mFragmentList.size() - 2);
        mFragmentAdapter.notifyDataSetChanged();
        speechSynthesizer.speak(s);
    }

    @Override
    public void onMessageContactReturn() {
        mFragmentAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(mFragmentList.size() - 2);
        mFragmentList.remove(mFragmentList.size() - 1);
        mFragmentAdapter.notifyDataSetChanged();
    }
    //获取顶部activity
    String getTopActivity(Activity context){
        ActivityManager manager = (ActivityManager)context.getSystemService(ACTIVITY_SERVICE) ;
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1) ;
        if(runningTaskInfos != null){
            Log.i(TAG, "getTopActivity "+(runningTaskInfos.get(0).topActivity).toString());
            return (runningTaskInfos.get(0).topActivity).toString();
        }
        else
            return null ;
    }

    /**
     * 切换到上个Fragment页面
     */
    public  void changeLastPage(){
        if(currentIndex>0){
            mFragmentAdapter.notifyDataSetChanged();
            int lastIndex=currentIndex - 1;
            if(currentIndex==0){
                lastIndex=mFragmentList.size()-1;
            }
            mViewPager.setCurrentItem(lastIndex);
//        mFragmentList.remove(mFragmentList.size() - 2);
            mFragmentAdapter.notifyDataSetChanged();
        }else{
            Toast.makeText(this,"没有更多的历史消息了",Toast.LENGTH_SHORT).show();
        }

    }
}
