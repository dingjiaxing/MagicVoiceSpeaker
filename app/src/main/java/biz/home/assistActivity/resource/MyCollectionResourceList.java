package biz.home.assistActivity.resource;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import biz.home.adapter.ResourceRecommendAdapter;
import biz.home.api.dao.ResourceCollectDao;
import biz.home.api.myComponent.Topbar;
import biz.home.application.myUtils.TimeChangeUtil;
import biz.home.bean.MagicInfo;
import biz.home.bean.MagicResult;
import biz.home.bean.MagicResultResourceFavorites;
import biz.home.bean.ResourceCollectionItemBean;
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
public class MyCollectionResourceList extends Activity  {
    private String TAG="MyCollectionResourceList";
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
        setContentView(R.layout.my_collection_resource_list);
        context=MyCollectionResourceList.this;
        listView= (ListView) findViewById(R.id.resource_collection_listview);
        topbar= (Topbar) findViewById(R.id.resource_collection_topbar);
        topbar.setOnLeftClickListener(new Topbar.returnClickListener() {
            @Override
            public void leftClick() {
                finish();
            }
        });
        userInfoDao=new MagicUserInfoDao(context);
        userId=userInfoDao.findUid();



//        List<ResourceContactedItemBean> list=new ArrayList<>();
//        for(int i=0;i<20;i++){
//            ResourceContactedItemBean bean=new ResourceContactedItemBean(i+"","资源标题"+i,"发布人："+i,"收藏时间："+"2015年1月29日");
//            list.add(bean);
//        }
//        Adapter adapter=new ResourceCollectionAdapter(context,list);
//        listView.setAdapter((ListAdapter) adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ResourceCollectionItemBean bean= (ResourceCollectionItemBean) parent.getItemAtPosition(position);
                OpenResourceInfoTask task1=new OpenResourceInfoTask(context,bean.getResourceId());
                task1.execute(1000);

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(context,"长按了",Toast.LENGTH_SHORT).show();
                android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(context);
                alert.setTitle("温馨提示");
                alert.setMessage("是否确定删除");
                alert.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();

                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
        ResourceCollectTask task=new ResourceCollectTask();
        task.execute(1000);
    }

    public class ResourceCollectTask extends AsyncTask{
        List<ResourceCollectionItemBean> list;
        List<String> postIdList;
        ResourceCollectDao collectDao;
        public ResourceCollectTask() {
            list=new ArrayList<>();
            postIdList=new ArrayList<>();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            collectDao=new ResourceCollectDao(context);
            List<MagicResultResourceFavorites> collectList=collectDao.find();
            if(collectList.size()==0){

            }else{
                for(int i=0;i<collectList.size();i++){
                    MagicResultResourceFavorites mr=collectList.get(i);
                    postIdList.add(mr.getPostId());
                    ResourceCollectionItemBean bean=new ResourceCollectionItemBean();
                    bean.setResourceId(mr.getPostId());
                    bean.setCollectionTime(mr.getFavoritesDate());
                    bean.setResourceReleasedPerson(mr.getPublisher());
                    bean.setResourceTitle(mr.getTitle());
                    list.add(bean);
                }
            }
            MagicInfo info=new MagicInfo();
            info.setUid(userId);
            info.setApi(MagicInfoApiEnum.GETRESOURCEFAVORITES);
            if(postIdList.size()!=0){
//                info.setExcludePostId(postIdList);
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
                list.clear();
                List<MagicResultResourceFavorites> list1=magicResult.getResourceFavorites();
                for(int i=0;i<list1.size();i++){
//                    collectDao.add(list1.get(i));
                    ResourceCollectionItemBean bean=new ResourceCollectionItemBean(list1.get(i).getPostId(),list1.get(i).getTitle(),list1.get(i).getPublisher(),"收藏时间："+TimeChangeUtil.time2date(list1.get(i).getFavoritesDate()));
                    list.add(bean);
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
                if( !list.get(i).getCollectionTime().startsWith("收藏时间")){
                    list.get(i).setCollectionTime("收藏时间:"+ TimeChangeUtil.time2date(list.get(i).getCollectionTime()));
                }
            }
            Adapter adapter=new ResourceCollectionAdapter(context,list);
            listView.setAdapter((ListAdapter) adapter);


        }
    }
}
