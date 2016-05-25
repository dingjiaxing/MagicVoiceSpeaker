package biz.home.assistActivity.resource;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import biz.home.R;
import biz.home.adapter.ResourceContactAdapter;
import biz.home.adapter.ResourceHisReleaseAdapter;
import biz.home.api.dao.ResourceContactDao;
import biz.home.api.myComponent.Topbar;
import biz.home.application.myUtils.TimeChangeUtil;
import biz.home.bean.MagicInfo;
import biz.home.bean.MagicResult;
import biz.home.bean.MagicResultResourceContact;
import biz.home.bean.MagicResultResourceRelease;
import biz.home.bean.ResourceContactedItemBean;
import biz.home.bean.ResourceHisReleaseItemBean;
import biz.home.model.MagicInfoApiEnum;
import biz.home.util.HttpHelp;
import biz.home.util.MagicUserInfoDao;
import biz.home.yibuTask.OpenResourceInfoTask;

/**
 * Created by admin on 2016/2/4.
 */
public class HisReleaseResourceList extends Activity {
    private String resultString;
    public static String USERID="userId";
    public static String USERNAME="userName";
    public static String TEL="telephone";
    private ListView listView;
    private Context context;
    private Topbar topbar;
    private MagicResult magicResult;
    MagicUserInfoDao userInfoDao;
    private String userId,telephone;
    private Button btn_call;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.his_released_resource_list);
        btn_call= (Button) findViewById(R.id.his_released_resource_button_call);
        context=HisReleaseResourceList.this;
        userId=getIntent().getStringExtra(USERID);
        String text=getIntent().getStringExtra(USERNAME);
//        userInfoDao=new MagicUserInfoDao(context);
//        userId=userInfoDao.findUid();
        listView= (ListView) findViewById(R.id.his_released_resource_listview);
        topbar= (Topbar) findViewById(R.id.his_released_resource_topbar);
        topbar.setText(text+"发布的资源");
        topbar.setOnLeftClickListener(new Topbar.returnClickListener() {
            @Override
            public void leftClick() {
                finish();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ResourceHisReleaseItemBean bean= (ResourceHisReleaseItemBean) parent.getItemAtPosition(position);
                OpenResourceInfoTask task=new OpenResourceInfoTask(context,bean.getResourceId());
                task.execute(1000);
            }
        });
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+telephone));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        ResourceHisReleaseTask task=new ResourceHisReleaseTask();
        task.execute(1000);
    }
    public class ResourceHisReleaseTask extends AsyncTask {
        List<ResourceHisReleaseItemBean> list;
        List<String> postIdList;
        ResourceContactDao collectDao;
        public ResourceHisReleaseTask() {
            list=new ArrayList<>();
            postIdList=new ArrayList<>();
        }

        @Override
        protected Object doInBackground(Object[] params) {

            MagicInfo info=new MagicInfo();
            info.setUid(userId);
            info.setApi(MagicInfoApiEnum.GETRESOURCERELEASE);
            if(postIdList.size()!=0){
                info.setExcludePostId(postIdList);
            }
            resultString= HttpHelp.send(info);
            magicResult=HttpHelp.transfer(resultString);

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);


            /*
            for(int i=0;i<20;i++){
                ResourceContactedItemBean bean=new ResourceContactedItemBean(i+"","资源标题"+i,"发布人："+i,"发布时间："+"2015年1月29日");
                list.add(bean);
            }
            */
            if(magicResult!=null){
                List<MagicResultResourceRelease> list1=magicResult.getResourceRelease();

                if(list1!=null){
                    for(int i=0;i<list1.size();i++){
//                        collectDao.add(list1.get(i));
                        ResourceHisReleaseItemBean bean=new ResourceHisReleaseItemBean(list1.get(i).getPostId(),list1.get(i).getTitle(),list1.get(i).getReleaseDate());
                        list.add(bean);
                        telephone=list1.get(i).getTelephone();
                    }
                }
            }else{
                if(resultString!=null){
                    if(resultString.equals("")){
                        Toast.makeText(context,"网络不好，无法获取服务器资源",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context,"暂无收藏资源",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            for(int i=0;i<list.size();i++){
                list.get(i).setReleaseTime("发布时间:"+ TimeChangeUtil.time2date(list.get(i).getReleaseTime()));
            }
            Adapter adapter=new ResourceHisReleaseAdapter(context,list);
            listView.setAdapter((ListAdapter) adapter);


        }
    }
}
