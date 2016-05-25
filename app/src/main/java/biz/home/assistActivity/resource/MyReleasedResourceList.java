package biz.home.assistActivity.resource;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.xml.sax.helpers.LocatorImpl;

import java.util.ArrayList;
import java.util.List;

import biz.home.R;
import biz.home.adapter.ResourceContactAdapter;
import biz.home.adapter.ResourceReleasedAdapter;
import biz.home.api.dao.ResourceContactDao;
import biz.home.api.dao.ResourceReleaseContactDao;
import biz.home.api.dao.ResourceReleaseDao;
import biz.home.api.myComponent.Topbar;
import biz.home.application.myUtils.ConnectionDetector;
import biz.home.application.myUtils.TimeChangeUtil;
import biz.home.bean.MagicInfo;
import biz.home.bean.MagicResult;
import biz.home.bean.MagicResultResourceContact;
import biz.home.bean.MagicResultResourceContactToRelease;
import biz.home.bean.MagicResultResourceRelease;
import biz.home.bean.ResourceContactedItemBean;
import biz.home.bean.ResourceMyReleasedGroupBean;
import biz.home.bean.ResourceMyReleasedSonBean;
import biz.home.bean.ResourceReleaseContact;
import biz.home.model.MagicInfoApiEnum;
import biz.home.util.HttpHelp;
import biz.home.util.MagicUserInfoDao;

/**
 * Created by admin on 2016/1/28.
 */
public class MyReleasedResourceList extends Activity {
    private String TAG="MyReleasedResourceList";
    private String resultString;
    private ExpandableListView listView;
    private Context context;
    private Topbar topbar;
    private MagicResult magicResult;
    MagicUserInfoDao userInfoDao;
    private String userId;
    private String telephone;
    List<List<ResourceMyReleasedSonBean>> sonList;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=MyReleasedResourceList.this;
        userInfoDao=new MagicUserInfoDao(context);
        userId=userInfoDao.findUid();
        setContentView(R.layout.my_released_resource_list);
        topbar= (Topbar) findViewById(R.id.resource_released_topbar);
        listView= (ExpandableListView) findViewById(R.id.my_release_resource_expandableListView);
        topbar.setOnLeftClickListener(new Topbar.returnClickListener() {
            @Override
            public void leftClick() {
                finish();
            }
        });
        ResourceReleaseTask task=new ResourceReleaseTask();
        task.execute(1000);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                ResourceMyReleasedSonBean bean=sonList.get(groupPosition).get(childPosition);
                Intent intent=new Intent(MyReleasedResourceList.this,HisReleaseResourceList.class);
                System.out.println("userID:" +bean.getUserid() );
//                intent.putExtra(HisReleaseResourceList.TEL,bean.getTel());
                intent.putExtra(HisReleaseResourceList.USERID,bean.getUserid());
                intent.putExtra(HisReleaseResourceList.USERNAME,bean.getName());
                startActivity(intent);
                return false;
            }
        });
        /*
        for(int j=0;j<3;j++){
            ResourceMyReleasedSonBean b=new ResourceMyReleasedSonBean(j+"","第"+j+"个人","2016年2月1日尝试看过您的");
            sonl.add(b);
        }
        for(int i=0;i<10;i++){
            ResourceMyReleasedGroupBean bean=new ResourceMyReleasedGroupBean(i+"","资源标题"+i);
            groupList.add(bean);
            sonList.add(sonl);
        }

        ResourceReleasedAdapter adapter=new ResourceReleasedAdapter(context,
                groupList,sonList);
        listView.setAdapter(adapter);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return false;
            }
        });
        listView.expandGroup(1);
        */
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public class ResourceReleaseTask extends AsyncTask {
        ResourceReleaseDao releaseDao;
        ResourceReleaseContactDao releaseContactDao;
        List<ResourceMyReleasedGroupBean> groupList;


        public ResourceReleaseTask() {
            groupList=new ArrayList<>();
            sonList=new ArrayList<>();

        }

        @Override
        protected Object doInBackground(Object[] params) {
            releaseDao=new ResourceReleaseDao(context);
            releaseContactDao=new ResourceReleaseContactDao(context);

            List<MagicResultResourceRelease> releaseList=releaseDao.find();
            if(releaseList.size()==0){

            }else{
                for(int i=0;i<releaseList.size();i++){
                    MagicResultResourceRelease mr=releaseList.get(i);
                    ResourceMyReleasedGroupBean bean=new ResourceMyReleasedGroupBean();
                    bean.setGroupResourceId(mr.getPostId());
                    bean.setGroupTitle(mr.getTitle());
                    bean.setGroupBrowseCount(mr.getBrowseCount());
                    groupList.add(bean);
                    List<ResourceMyReleasedSonBean> sonList2=new ArrayList<>();
                    if(mr.getReleaseId()!=null){
                        List<ResourceReleaseContact> rcList=releaseContactDao.findByReleaseId(mr.getReleaseId());
                        for(int j=0;j<rcList.size();j++){
                            ResourceMyReleasedSonBean bean1=new ResourceMyReleasedSonBean(rcList.get(i).getUserId(),rcList.get(i).getRealName(),rcList.get(i).getContactTime());
                            sonList2.add(bean1);
                        }
                    }

                    sonList.add(sonList2);
                }
            }
            MagicInfo info=new MagicInfo();
            info.setUid(userId);
            info.setApi(MagicInfoApiEnum.GETRESOURCERELEASE);
            resultString= HttpHelp.send(info);
            magicResult=HttpHelp.transfer(resultString);
            if(magicResult!=null){
                releaseDao.deleteAll();
                groupList.clear();
                sonList.clear();
            }

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
                        releaseDao.add(list1.get(i));
                        MagicResultResourceRelease mrrr=list1.get(i);

                        List<MagicResultResourceContactToRelease> crList;
                        if(mrrr.getResourceContact()==null){
                             crList=new ArrayList<>();
                        }else{
                            crList=mrrr.getResourceContact();
                        }
                        List<ResourceMyReleasedSonBean> sonBeanList=new ArrayList<>();
                        for(int j=0;j<crList.size();j++){
                            ResourceMyReleasedSonBean sonBean=new ResourceMyReleasedSonBean(crList.get(j).getUserId(),crList.get(j).getRealName(), TimeChangeUtil.time2date(crList.get(j).getContactDate())+"尝试与您联系");
//                            sonBean.setTel(crList.get(j).getTelephone());
                            System.out.println("crList.get(j).getTelephone():" +crList.get(j).getTelephone() );
                            sonBeanList.add(sonBean);
                        }
                        ResourceMyReleasedGroupBean bean1=new ResourceMyReleasedGroupBean(mrrr.getPostId(),mrrr.getTitle(),mrrr.getBrowseCount());
                        bean1.setGroupContactCount(sonBeanList.size());
                        groupList.add(bean1);
                        sonList.add(sonBeanList);
                    }
                }
            }else{
                if(resultString!=null){
                    if(resultString.equals("")){
                        Toast.makeText(context,"网络不好，无法获取服务器资源",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context,"您还没有发布资源",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            ResourceReleasedAdapter adapter=  new ResourceReleasedAdapter(context,groupList,sonList);
            listView.setAdapter( adapter);


        }
    }
}
