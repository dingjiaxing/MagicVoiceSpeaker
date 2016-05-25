package biz.home.assistActivity.resource;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import biz.home.R;
import biz.home.adapter.ResourceCollectionAdapter;
import biz.home.adapter.ResourceContactAdapter;
import biz.home.adapter.ResourceRecommendAdapter;
import biz.home.api.dao.ResourceCollectDao;
import biz.home.api.dao.ResourceContactDao;
import biz.home.api.myComponent.Topbar;
import biz.home.application.myUtils.TimeChangeUtil;
import biz.home.bean.MagicInfo;
import biz.home.bean.MagicResult;
import biz.home.bean.MagicResultResourceContact;
import biz.home.bean.MagicResultResourceFavorites;
import biz.home.bean.ResourceCollectionItemBean;
import biz.home.bean.ResourceContactedItemBean;
import biz.home.bean.ResourceRecommendItemBean;
import biz.home.model.MagicInfoApiEnum;
import biz.home.util.HttpHelp;
import biz.home.util.MagicUserInfoDao;
import biz.home.yibuTask.OpenResourceInfoTask;

/**
 * Created by admin on 2016/1/28.
 */
public class ContactedResourceList extends Activity {
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
        setContentView(R.layout.contacted_resource_list);
        context=ContactedResourceList.this;
        userInfoDao=new MagicUserInfoDao(context);
        userId=userInfoDao.findUid();
        listView= (ListView) findViewById(R.id.contacted_resource_listview);
        topbar= (Topbar) findViewById(R.id.resource_contacted_topbar);
        topbar.setOnLeftClickListener(new Topbar.returnClickListener() {
            @Override
            public void leftClick() {
                finish();
            }
        });

        ResourceContactTask task=new ResourceContactTask();
        task.execute(1000);
        /*
        List<ResourceRecommendItemBean> list=new ArrayList<>();
        for(int i=0;i<20;i++){
            ResourceRecommendItemBean bean=new ResourceRecommendItemBean(i+"","资源标题"+i,"发布人："+i,"联系时间："+"2015年1月29日");
            list.add(bean);
        }
        Adapter adapter=new ResourceRecommendAdapter(context,list);
        listView.setAdapter((ListAdapter) adapter);
        */

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ResourceContactedItemBean bean= (ResourceContactedItemBean) parent.getItemAtPosition(position);
                OpenResourceInfoTask task1=new OpenResourceInfoTask(context,bean.getResourceId());
                task1.execute(1000);

            }
        });
    }
    public class ResourceContactTask extends AsyncTask {
        List<ResourceContactedItemBean> list;
        List<String> postIdList;
        ResourceContactDao collectDao;
        public ResourceContactTask() {
            list=new ArrayList<>();
            postIdList=new ArrayList<>();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            collectDao=new ResourceContactDao(context);
            List<MagicResultResourceContact> collectList=collectDao.find();
            if(collectList.size()==0){

            }else{
                for(int i=0;i<collectList.size();i++){
                    MagicResultResourceContact mr=collectList.get(i);
                    postIdList.add(mr.getPostId());
                    ResourceContactedItemBean bean=new ResourceContactedItemBean();
                    bean.setResourceId(mr.getPostId());
                    bean.setResourceContactTime(mr.getContactDate());
                    bean.setResourceReleasedPerson(mr.getPublisher());
                    bean.setResourceTitle(mr.getTitle());
                    list.add(bean);
                }
            }
            MagicInfo info=new MagicInfo();
            info.setUid(userId);
            info.setApi(MagicInfoApiEnum.GETRESOURCECONTACT);
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
                List<MagicResultResourceContact> list1=magicResult.getResourceContact();
                if(list1!=null){
                    for(int i=0;i<list1.size();i++){
                        collectDao.add(list1.get(i));
                        ResourceContactedItemBean bean=new ResourceContactedItemBean(list1.get(i).getPostId(),list1.get(i).getTitle(),list1.get(i).getPublisher(),list1.get(i).getContactDate());
                        list.add(bean);
                    }
                }
            }else{
                if(resultString!=null){
                    if(resultString.equals("")){
                        Toast.makeText(context,"网络不好，无法获取服务器资源",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context,"暂无联系过的资源",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            for(int i=0;i<list.size();i++){
                list.get(i).setResourceContactTime("联系时间:"+TimeChangeUtil.time2date(list.get(i).getResourceContactTime()));
            }
            Adapter adapter=new ResourceContactAdapter(context,list);
            listView.setAdapter((ListAdapter) adapter);


        }
    }
}
