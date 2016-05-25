package biz.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import biz.home.R;
import biz.home.bean.ResourceContactedItemBean;
import biz.home.bean.ResourceRecommendItemBean;

/**
 * Created by admin on 2016/1/28.
 */
public class ResourceContactAdapter extends BaseAdapter {
    private Context context;
    private List<ResourceContactedItemBean> list;
    private LayoutInflater inflater;
    public ResourceContactAdapter(Context context, List<ResourceContactedItemBean> list) {
        this.context=context;
        this.list=list;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.resource_contacted_item,null);
            viewHolder.tv_id= (TextView) convertView.findViewById(R.id.resource_contacted_item_id);
            viewHolder.tv_title= (TextView) convertView.findViewById(R.id.resource_contacted_item_title);
            viewHolder.tv_person= (TextView) convertView.findViewById(R.id.resource_contacted_item_person);
            viewHolder.tv_time= (TextView) convertView.findViewById(R.id.resource_contacted_item_time);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        ResourceContactedItemBean bean=list.get(position);
        viewHolder.tv_id.setText(bean.getResourceId());
        viewHolder.tv_title.setText(bean.getResourceTitle());
        viewHolder.tv_person.setText(bean.getResourceReleasedPerson());
        viewHolder.tv_time.setText(bean.getResourceContactTime());

        return convertView;
    }
    class ViewHolder{
        private TextView tv_id;
        private TextView tv_title;
        private TextView tv_person;
        private TextView tv_time;
    }
}
