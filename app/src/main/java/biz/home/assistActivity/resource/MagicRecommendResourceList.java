package biz.home.assistActivity.resource;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import biz.home.R;
import biz.home.adapter.ResourceRecommendAdapter;
import biz.home.api.myComponent.Topbar;
import biz.home.application.myUtils.TimeChangeUtil;
import biz.home.bean.MagicInfo;
import biz.home.bean.MagicResourceInfo;
import biz.home.bean.MagicResult;
import biz.home.bean.MagicResultResourceInfo;
import biz.home.bean.ResourceContactedItemBean;
import biz.home.bean.ResourceRecommendItemBean;
import biz.home.model.MagicInfoApiEnum;
import biz.home.model.MagicResultResourcePush;
import biz.home.util.HttpHelp;
import biz.home.util.MagicUserInfoDao;
import biz.home.yibuTask.OpenResourceInfoTask;

/**
 * Created by admin on 2016/1/28.
 */
public class MagicRecommendResourceList extends Activity {
    private String resultString;
    private ListView listView;
    private Context context;
    private Topbar topbar;
    private MagicResult magicResult;
    MagicUserInfoDao userInfoDao;
    private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.magic_recommend_resource_list);
        context=MagicRecommendResourceList.this;
        listView= (ListView) findViewById(R.id.magic_recommend_resource_listview);
        topbar= (Topbar) findViewById(R.id.magic_recommend_resource_topbar);
        topbar.setOnLeftClickListener(new Topbar.returnClickListener() {
            @Override
            public void leftClick() {
                finish();
            }
        });
        userInfoDao=new MagicUserInfoDao(context);
        userId=userInfoDao.findUid();

        ResourceRecommendTask task=new ResourceRecommendTask();
        task.execute(1000);
        /*
        List<ResourceContactedItemBean> list=new ArrayList<>();
        for(int i=0;i<20;i++){
            ResourceContactedItemBean bean=new ResourceContactedItemBean(i+"","资源标题"+i,"发布人："+i,"发布时间："+"2015年1月29日");
            list.add(bean);
        }
        Adapter adapter=new ContatedResourceAdapter(context,list);
        listView.setAdapter((ListAdapter) adapter);
        */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ResourceRecommendItemBean bean= (ResourceRecommendItemBean) parent.getItemAtPosition(position);
                OpenResourceInfoTask task1=new OpenResourceInfoTask(context,bean.getResourceId());
                task1.execute(1000);

            }
        });

    }
    public class ResourceRecommendTask extends AsyncTask{
        List<ResourceRecommendItemBean> list;

        public ResourceRecommendTask() {
            userInfoDao=new MagicUserInfoDao(context);
            list=new ArrayList<>();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            MagicInfo info=new MagicInfo();
            info.setUid(userId);
            info.setApi(MagicInfoApiEnum.GETRESOURCEPUSH);
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
                List<MagicResultResourcePush> list1=magicResult.getResourcePush();
                for(int i=0;i<list1.size();i++){
                    ResourceRecommendItemBean bean=new ResourceRecommendItemBean(list1.get(i).getPostId(),list1.get(i).getTitle(),list1.get(i).getPublisher(),list1.get(i).getPushDate());
                    list.add(bean);
                }
                for(int i=0;i<list.size();i++){
                    if( !list.get(i).getResourceReleasedTime().startsWith("发布时间")){
                        list.get(i).setResourceReleasedTime("发布时间:"+ TimeChangeUtil.time2date(list.get(i).getResourceReleasedTime()));
                    }
                }
                Adapter adapter=new ResourceRecommendAdapter(context,list);
                listView.setAdapter((ListAdapter) adapter);
            }else{
                if(resultString!=null){
                    if(resultString.equals("")){
                        Toast.makeText(context,"网络不好，无法获取服务器资源",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context,"暂无推荐资源",Toast.LENGTH_SHORT).show();
                    }
                }
            }


        }
    }
}
