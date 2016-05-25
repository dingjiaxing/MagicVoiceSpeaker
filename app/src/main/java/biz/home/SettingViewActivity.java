package biz.home;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import biz.home.R;
import biz.home.assistActivity.SettingThanks;
import biz.home.bean.MagicInfo;
import biz.home.model.MagicInfoApiEnum;
import biz.home.util.HttpHelp;
import biz.home.util.MagicUserInfoDao;
import biz.home.yibuTask.UpdateAppTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by lmw on 2015/7/13.
 */
public class SettingViewActivity extends Activity implements AdapterView.OnItemClickListener,
        View.OnClickListener,View.OnLongClickListener{
    public final static int REQUEST_CODE = 1002;
    private ImageView backImg;
    private ListView settingList;
    private String[] listText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.setting_view);
        this.backImg = (ImageView) findViewById(R.id.back_img);
        this.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        this.settingList = (ListView) findViewById(R.id.setting_list);
        this.settingList.setOnItemClickListener(this);
        initList();
    }

    private void initList() {
        int[] img = new int[]{R.drawable.setting_self,
                R.drawable.setting_set,
                R.drawable.setting_communicate,
                R.drawable.setting_share,
                R.drawable.setting_about,
                R.drawable.setting_about,};
        listText=getResources().getStringArray(R.array.setting_items);
        List<Map<String, String>> listData = new ArrayList<Map<String, String>>();
        for (int i = 0; i < img.length; i++) {
            Map<String, String> data = new HashMap<String, String>();
            data.put("img", String.valueOf(img[i]));
            data.put("text", listText[i]);
            listData.add(data);
        }
        SimpleAdapter adapter = new SimpleAdapter(this,
                listData,
                R.layout.setting_view_item,
                new String[]{"img", "text"},
                new int[]{R.id.list_item_img, R.id.list_item_text});
        this.settingList.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent;
        switch(position){
            case 0:
                intent=new Intent(this,SettingInformationsActivity.class);
                intent.putExtra("title",listText[position]);
                startActivity(intent);
                break;
            case 1:
                intent=new Intent(this,SettingImageryActivity.class);
                intent.putExtra("title", listText[position]);
                startActivity(intent);
                break;
            case 2:
                intent=new Intent(this,SettingCommunicateActivity.class);
                intent.putExtra("title", listText[position]);
                startActivity(intent);
                break;
            case 3:
//                intent=new Intent(this,SettingShareActivity.class);
//                intent.putExtra("title", listText[position]);
//                startActivity(intent);
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        try{
                            MagicUserInfoDao dao=new MagicUserInfoDao(getApplicationContext());
                            String uid=dao.findUid();
                            MagicInfo info=new MagicInfo();
                            info.setUid(uid);
                            info.setApi(MagicInfoApiEnum.ANDROIDAPP);
                            String str= HttpHelp.send(info);
                            String updateAppUrl=HttpHelp.getJsonString(str);
                            Intent intent=new Intent(Intent.ACTION_SEND);
                            intent.setType("image/*");
                            intent.putExtra(Intent.EXTRA_SUBJECT, "共享软件");
                            intent.putExtra(Intent.EXTRA_TEXT, "这个超级火！免费的秘书太方便了！以后生意上的事情都交给她吧！推荐你快点下载"+updateAppUrl);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(Intent.createChooser(intent, getTitle()));
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }.start();
                break;
//                intent=new Intent(Intent.ACTION_SEND);
//                intent.setType("image/*");
//                intent.putExtra(Intent.EXTRA_SUBJECT, "共享软件");
//                intent.putExtra(Intent.EXTRA_TEXT, "这个超级火！免费的秘书太方便了！以后生意上的事情都交给她吧！推荐你快点下载http://t.cn/XXXXXXX");
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(Intent.createChooser(intent, getTitle()));

//                File file = new File(Environment.getExternalStorageDirectory().getPath()+"/00.png");
//                Uri uri = Uri.parse("android.resource://biz.home/drawable/about.png");
//                Uri uri =  Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
//                                + getResourcePackageName(R.drawable.about));
//        Uri uri=new Uri("http://www.4350.biz/4350.ico") ;
//                intent.putExtra(Intent.EXTRA_STREAM, uri);

//                ClipboardManager cmb = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
//                ClipData data=ClipData.newPlainText("info","这个超级火！免费的秘书太方便了！以后生意上的事情都交给她吧！推荐你快点下载http://t.cn/XXXXXXX");
//                cmb.setPrimaryClip(data);
//                Toast.makeText(getBaseContext(), "推荐内容已复制到剪贴板", Toast.LENGTH_SHORT).show();


//                startActivityForResult(Intent.createChooser(intent,  "请选择邮件发送软件" ), 1001 );
            case 4:
                intent=new Intent(SettingViewActivity.this, SettingThanks.class);
                startActivity(intent);
                break;
            case 5:
                intent=new Intent(this,SettingIntroduceActivity.class);
                intent.putExtra("title", listText[position]);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        /*new AlertDialog.Builder(this)
                .setPositiveButton("复制", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText shareInfo=(EditText)popupView.findViewById(R.id.setting_share_text);
                        String info=shareInfo.getText().toString();
                        ClipboardManager cmb = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData data=ClipData.newPlainText("info",info);
                        cmb.setPrimaryClip(data);
                        Toast.makeText(SettingViewActivity.this,"粘贴文本成功",Toast.LENGTH_SHORT).show();
                    }
                })
                .show();*/
        return true;
    }
}
