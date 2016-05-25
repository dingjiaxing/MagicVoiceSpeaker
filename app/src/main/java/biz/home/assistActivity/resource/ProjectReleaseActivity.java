package biz.home.assistActivity.resource;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.speech.VoiceRecognitionService;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import biz.home.Constant;
import biz.home.R;
import biz.home.SpeakToitActivity;
import biz.home.api.ImageTools;
import biz.home.bean.MagicResourceInfo;
import biz.home.bean.MagicResultResourceInfo;
import biz.home.yibuTask.UploadFileTask2;

/**
 * Created by admin on 2015/11/18.
 */
public class ProjectReleaseActivity extends Activity implements View.OnClickListener,RecognitionListener {
    //与话筒帮助框相对应的变量
    private LinearLayout linear;
    private TextView voiceTextView;
    private Button voiceButtonLeft,voiceButtonRight;
    private int voiceFlag;
    private static int VOICE_INVISIBLE=0;
    private static int VOICE_VISIBLE=1;
    private static int VOICE_ERROR=2;
    //百度语音相关变量
    private static final int REQUEST_UI = 1;
    public static final int STATUS_None = 0;
    public static final int STATUS_WaitingReady = 2;
    public static final int STATUS_Ready = 3;
    public static final int STATUS_Speaking = 4;
    public static final int STATUS_Recognition = 5;
    private static final String TAG ="ProjectReleaseActivity" ;
    private SpeechRecognizer speechRecognizer;
    private int status = STATUS_None;
    public static int REQUEST_CODE=001;

    private EditText title,content;
    private ImageView exit,pImage,pCamera,pVoice,pOk;
    private TextView release;
    private ProgressBar progressBar;


    SpannableString mSpan1 ;
    public static Map<String,String> pics=new HashMap<String,String>();
    public static int num=0;
    private static final int TAKE_PICTURE = 0;
    private static final int CHOOSE_PICTURE = 1;
    private static final int CROP = 2;
    private static final int SCALE = 2;//照片缩小比例

    protected Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                setResult(SpeakToitActivity.RESULT_RELEASED_PROJECT_SHOW);
                finish();
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_release);
        Intent intent=getIntent();
        String gTitle=intent.getStringExtra("title");
        String gContent=intent.getStringExtra("content");
        findById();
        setOnClick();
        init();
        title.setText(gTitle);
        content.setText(gContent);

        content.requestFocus();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    //将保存在本地的图片取出并缩小后显示在界面上
                    Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/image.jpg");
                    Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
                    //由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
                    bitmap.recycle();

                    //将处理过的图片显示在界面上，并保存到本地
//                    iv_image.setImageBitmap(newBitmap);
//                    String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                    String path = this.getFilesDir().getPath()+File.separator+"resourceTempImages";
                    String pathName = String.valueOf(System.currentTimeMillis());
                    ImageTools.savePhotoToSDCard(newBitmap, path, pathName);
                    displayBitmapOnText(newBitmap, path + "//" + pathName + ".png");
                    break;

                case CHOOSE_PICTURE:
                    ContentResolver resolver = getContentResolver();
                    //照片的原始资源地址
                    Uri originalUri = data.getData();
                    System.out.println("originalUri" + originalUri);
                    try {
                        String img_path = getRealPathFromURI(originalUri);
                        //使用ContentProvider通过URI获取原始图片
                        Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                        if (photo != null) {
                            //为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                            Bitmap smallBitmap = ImageTools.zoomBitmap(photo, photo.getWidth() / SCALE, photo.getHeight() / SCALE);
                            //释放原始图片占用的内存，防止out of memory异常发生
                            photo.recycle();
                            path = this.getFilesDir().getPath()+File.separator+"resourceTempImages";
//                             path = Environment.getExternalStorageDirectory().getAbsolutePath();
                             pathName = String.valueOf(System.currentTimeMillis());
                            ImageTools.savePhotoToSDCard(smallBitmap, path, pathName);
                            displayBitmapOnText(smallBitmap, path + "//" + pathName + ".png");
//                            iv_image.setImageBitmap(smallBitmap);
//						content.append(Html.fromHtml("<img src='" + smallBitmap + "'/>", imageGetter, null));
                            //将originalUri转化为路径img_path
//                            System.out.println("img_path:" + img_path);
//                            displayBitmapOnText(photo,img_path);
//                            displayBitmapOnText(smallBitmap);
//                            System.out.println("" + content.getText().toString());
//                            Log.i("EditText", content.getText().toString());
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }



                    break;
                default:
                    break;
            }
        }
    }

    public void setOnClick(){
        release.setOnClickListener(this);
        exit.setOnClickListener(this);
        pImage.setOnClickListener(this);
        pCamera.setOnClickListener(this);
        pVoice.setOnClickListener(this);
        pOk.setOnClickListener(this);
        voiceButtonLeft.setOnClickListener(this);
        voiceButtonRight.setOnClickListener(this);

    }
    public void findById(){
        title= (EditText) findViewById(R.id.project_release_title);
        content= (EditText) findViewById(R.id.project_release_content);
        exit= (ImageView) findViewById(R.id.project_release_return);
        pImage= (ImageView) findViewById(R.id.project_release_image_button);
        pCamera= (ImageView) findViewById(R.id.project_icon_camera_button);
        pVoice= (ImageView) findViewById(R.id.project_icon_voice_button);
        pOk= (ImageView) findViewById(R.id.project_icon_ok_button);
        release= (TextView) findViewById(R.id.project_release_button);
        progressBar= (ProgressBar) findViewById(R.id.project_release_progressBar);
        linear= (LinearLayout) findViewById(R.id.project_release_voice_help_linear);
        voiceButtonLeft= (Button) findViewById(R.id.project_release_voice_button_left);
        voiceButtonRight= (Button) findViewById(R.id.project_release_button_right);
        voiceTextView= (TextView) findViewById(R.id.project_release_voice_error);

    }
    public void init(){
        voiceFlag=VOICE_INVISIBLE;
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this, new ComponentName(this, VoiceRecognitionService.class));
        speechRecognizer.setRecognitionListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.project_release_bottom:
                Toast.makeText(this,"project_release_bottom",Toast.LENGTH_SHORT).show();
            case R.id.project_release_return:
                this.finish();
                break;
            case R.id.project_release_button:
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setSecondaryProgress(78);
                release.setText("发布中");
                MagicResultResourceInfo resourceInfo=new MagicResultResourceInfo();
                resourceInfo.setTitle(title.getText().toString());
                resourceInfo.setContent(content.getText().toString());
                resourceInfo.setConfirm(true);
                SpeakToitActivity.pics=pics;
                UploadFileTask2 task2=new UploadFileTask2(progressBar,resourceInfo,getApplicationContext(),handler);
                task2.execute();
//                progressBar.setProgress(15);
//                UploadFileTask task=new UploadFileTask(progressBar, resourceInfo,mContainer,getApplicationContext(),mainWebFragment,speechSynthesizer,endResult, mainEditFragment, mFragmentAdapter, mFragmentList, mViewPager) ;
//                task.execute();
                break;
            case R.id.project_release_image_button:
                if(title.isFocused()){
                    android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(ProjectReleaseActivity.this);
                    alert.setTitle("系统提示：");
                    alert.setMessage("标题中不能加入图片");
                    alert.setPositiveButton("                                       确定                                       ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alert.show();
                }else{
                    int REQUEST_CODE;
                    final boolean crop = false;
                    Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    if (crop) {
                        REQUEST_CODE = CROP;
                    } else {
                        REQUEST_CODE = CHOOSE_PICTURE;
                    }
                    openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(openAlbumIntent, REQUEST_CODE);
                }

                break;
            case R.id.project_icon_camera_button:
                if(title.isFocused()){
                    android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(ProjectReleaseActivity.this);
                    alert.setTitle("系统提示：");
                    alert.setMessage("标题中不能加入图片");
                    alert.setPositiveButton("                                       确定                                       ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alert.show();
                }else{
                    Uri imageUri = null;
                    String fileName = null;
                    Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (false) {
                        REQUEST_CODE = CROP;
                        //删除上一次截图的临时文件
                        SharedPreferences sharedPreferences = getSharedPreferences("temp", Context.MODE_WORLD_WRITEABLE);
                        ImageTools.deletePhotoAtPathAndName(Environment.getExternalStorageDirectory().getAbsolutePath(), sharedPreferences.getString("tempName", ""));

                        //保存本次截图临时文件名字
                        fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("tempName", fileName);
                        editor.commit();
                    } else {
                        REQUEST_CODE = TAKE_PICTURE;
                        fileName = "image.jpg";
                    }
                    imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), fileName));
                    //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                    openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(openCameraIntent, REQUEST_CODE);
                }

                break;
            case R.id.project_icon_voice_button:
                pVoice.setBackgroundColor(Color.DKGRAY);
                 linear.setVisibility(View.VISIBLE);
                voiceButtonLeft.setText("说完了");
                voiceButtonRight.setText("关闭");
                voiceButtonLeft.setVisibility(View.VISIBLE);
                voiceButtonRight.setVisibility(View.VISIBLE);
                voiceTextView.setText("");
                voiceTextView.setVisibility(View.GONE);
                voiceFlag=VOICE_VISIBLE;

                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                boolean api = sp.getBoolean("api", false);
                if (api) {
                    switch (status) {
                        case STATUS_None:
                            Log.i(TAG, "onClick STATUS_None取消");

                            start();
                            status = STATUS_WaitingReady;
                            break;
                        case STATUS_WaitingReady:
                            Log.i(TAG, "onClick STATUS_WaitingReady开始");
                            cancel();
                            status = STATUS_None;
                            break;
                        case STATUS_Ready:
                            Log.i(TAG, "onClick STATUS_Ready");
                            cancel();
                            status = STATUS_None;
                            //btn.setText("开始");
                            break;
                        case STATUS_Speaking:
                            Log.i(TAG, "onClick STATUS_Speaking识别中");
                            stop();
                            status = STATUS_Recognition;
                            break;
                        case STATUS_Recognition:
                            Log.i(TAG, "onClick STATUS_Recognition");
                            cancel();
                            status = STATUS_None;
                            break;
                    }
                } else {
                    start();
                }
                break;
            case R.id.project_icon_ok_button:
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                boolean isOpen=imm.isActive();//isOpen若返回true，则表示输入法打开
                if(isOpen){
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
//                Toast.makeText(getApplicationContext(),"点击了ok"+isOpen,Toast.LENGTH_SHORT).show();

                break;
            case R.id.project_release_voice_button_left:
                if(voiceFlag==VOICE_VISIBLE){
                    stop();
                    linear.setVisibility(View.GONE);
                    voiceTextView.setVisibility(View.GONE);
                    pVoice.setBackgroundColor(Color.alpha(00));

                }else if(voiceFlag==VOICE_ERROR){
                    start();
                    voiceFlag=VOICE_VISIBLE;
                    voiceButtonLeft.setText("说完了");
                    voiceButtonRight.setText("关闭");
                    voiceTextView.setText("");
                    voiceTextView.setVisibility(View.GONE);
                }else{

                }
                break;
            case  R.id.project_release_button_right:
                if(voiceFlag==VOICE_VISIBLE||voiceFlag==VOICE_ERROR){
                    cancel();
                    linear.setVisibility(View.GONE);
                    pVoice.setBackgroundColor(Color.alpha(00));
                }else{

                }
                break;
            default:
                break;
        }
    }
    private void displayBitmapOnText(Bitmap thumbnailBitmap,String fileName) {

        if(thumbnailBitmap == null)
            return;
//        String s=bitmaptoString(thumbnailBitmap,1);
////		System.out.print("bitmap转换成的字符串：" +s );
//        Log.i("bitmap转换成的字符串：", ""+s);
//        System.out.print("" + s);
//        System.out.println("" + s.length());
//        System.out.println("" + s.getBytes().length);
//        thumbnailBitmap=stringtoBitmap(s);
        int start = content.getSelectionStart();
        mSpan1 = new SpannableString("-pic"+num+"-");
        //将图片的键值对信息存储在
        saveInMap("-pic"+num+"-",fileName);
        mSpan1.setSpan(new ImageSpan(thumbnailBitmap), mSpan1.length() - (num<10?6:7), mSpan1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        num++;
//        mSpan1.toString();
        if(content != null) {
            Editable et = content.getText();
//            et.insert(start, mSpan1);
            et.insert(start, mSpan1);
            content.setText(et);
            content.setSelection(start + mSpan1.length());
        }
        content.setLineSpacing(10f, 1f);
        //将图片的键值对信息存储在
//        saveInMap(mSpan1.toString(),fileName);
    }
    private void displayBitmapOnText(EditText et,String str) {
        //获取光标位置
        int index=et.getSelectionStart();
        Editable edit=et.getEditableText();
        if(index<0||index>=edit.length()){
            edit.append(str);
        }else{
            edit.insert(index,str);
        }
    }
    //将图片的键值对存储在Map  pics中
    public void saveInMap(String key, String value){
        pics.put(key, value);
    }
    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        status = STATUS_Ready;
    }

    @Override
    public void onBeginningOfSpeech() {
        status = STATUS_Speaking;
    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {
        status = STATUS_Recognition;
    }

    @Override
    public void onError(int error) {
        String sError="";
        switch (error) {
            case SpeechRecognizer.ERROR_AUDIO:
                sError="音频问题";
//                    Toast.makeText(this,"音频问题",Toast.LENGTH_SHORT).show();
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                sError="没有语音输入";
//                    Toast.makeText(this,"没有语音输入",Toast.LENGTH_SHORT).show();
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                sError="网络问题";
//                    Toast.makeText(this,"网络问题",Toast.LENGTH_SHORT).show();
                break;

            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                sError="权限不足";
//                    Toast.makeText(this,"权限不足",Toast.LENGTH_SHORT).show();
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                sError="没有匹配的识别结果";
//                    Toast.makeText(this,"没有匹配的识别结果",Toast.LENGTH_SHORT).show();
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                sError="引擎忙";
//                     Toast.makeText(this,"引擎忙",Toast.LENGTH_SHORT).show();
                break;
            case SpeechRecognizer.ERROR_SERVER:
                sError="服务端错误";
//                    Toast.makeText(this,"服务端错误",Toast.LENGTH_SHORT).show();
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                sError="连接超时";
//                    Toast.makeText(this,"连接超时",Toast.LENGTH_SHORT).show();
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                sError="其它客户端错误";
//                    Toast.makeText(this,"其它客户端错误",Toast.LENGTH_SHORT).show();
                break;
        }
        voiceFlag=VOICE_ERROR;
        voiceTextView.setVisibility(View.VISIBLE);
        voiceTextView.setText(sError);
        voiceButtonLeft.setText("重说");
        voiceButtonRight.setText("取消");

    }

    @Override
    public void onResults(Bundle results) {
        status = STATUS_None;
        voiceFlag=VOICE_INVISIBLE;
        linear.setVisibility(View.INVISIBLE);
        voiceTextView.setText("");
        voiceTextView.setVisibility(View.GONE);
        pVoice.setBackgroundColor(Color.alpha(00));

        ArrayList<String> nbest = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        Log.i("识别成功：", Arrays.toString(nbest.toArray(new String[nbest.size()])));
        String json_res = results.getString("origin_result");
        try {
            Log.i("origin_result=\n", new JSONObject(json_res).toString(4));
        } catch (Exception e) {
            //Log.i("origin_result=[warning: bad json]\n",json_res);
        }
        Log.i("开始","");
        String strEnd2Finish = "";
        Log.i(nbest.get(0), strEnd2Finish);
        Log.i("识别结果：", nbest.get(0));
        String result=nbest.get(0);
        if(title.isFocused()){
            displayBitmapOnText(title,result);
        }else if(content.isFocused()){
            displayBitmapOnText(content,result);
        }else{

        }
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }
    //百度语音识别相关控制函数
    private void start() {
        //txtLog.setText("");
        //print("点击了“开始”");
        Intent intent = new Intent();
        bindParams(intent);

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
        if (true) {
//            speechEndTime = -1;
            speechRecognizer.startListening(intent);
        } else {
            intent.setAction("com.baidu.action.RECOGNIZE_SPEECH");
            startActivityForResult(intent, REQUEST_UI);
        }
    /*
        //txtResult.setText("");
        intent.putExtra("sample", 16000); // 离线仅支持 16000 采样率
        intent.putExtra("language", "cmn-Hans-CN"); // 离线仅支持中文普通话
//        intent.putExtra("language", "sichuan-Hans-CN"); // 离线仅支持中文普通话
        intent.putExtra("prop", 20000); // 输入
//        intent.putExtra("prop", 10005); // 热词
//        intent.putExtra("prop", 10060); // 地图
//        intent.putExtra("prop", 10001); // 音乐
//        intent.putExtra("prop", 10003); // 应用
//        intent.putExtra("prop", 10004); //网页
//        intent.putExtra("prop", 10006); //购物
//        intent.putExtra("prop", 10008); // 电话
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
        intent.putExtra("asr-base-file-path", getApplicationInfo().dataDir+"/lib/libs_1.so");
//        intent.putExtra("asr-base-file-path", getApplicationInfo().dataDir+"/res/raw/s_1");
        // 语音输入附加资源，value替换为资源文件实际路径
        intent.putExtra("lm-res-file-path", getApplicationInfo().dataDir+"/lib/libs_2.so");
        intent.setAction("com.baidu.action.RECOGNIZE_SPEECH");
        speechRecognizer.startListening(intent);
        */
    }

    private void stop() {
        speechRecognizer.stopListening();

    }

    public void cancel() {
            speechRecognizer.cancel();

    }

    public void bindParams(Intent intent) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.getBoolean("tips_sound", true)) {
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
        if (sp.getBoolean(Constant.EXTRA_OUTFILE, false)) {
            intent.putExtra(Constant.EXTRA_OUTFILE, "sdcard/outfile.pcm");
        }
        if (sp.contains(Constant.EXTRA_SAMPLE)) {
            String tmp = sp.getString(Constant.EXTRA_SAMPLE, "").replaceAll(",.*", "").trim();
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
            intent.putExtra(Constant.EXTRA_OFFLINE_SLOT_DATA, buildTestSlotData());
        }
        */
    }
}
