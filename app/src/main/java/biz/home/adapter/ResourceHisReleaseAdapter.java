package biz.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

import biz.home.R;
import biz.home.bean.ResourceContactedItemBean;
import biz.home.bean.ResourceHisReleaseItemBean;

/**
 * Created by admin on 2016/2/4.
 */
public class ResourceHisReleaseAdapter extends BaseAdapter {
    private Context context;
    private List<ResourceHisReleaseItemBean> list;
    private LayoutInflater inflater;

    public ResourceHisReleaseAdapter(Context context, List<ResourceHisReleaseItemBean> list) {
        this.context = context;
        this.list = list;
        inflater= LayoutInflater.from(context);
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
            convertView=inflater.inflate(R.layout.resource_his_release_item,null);
            viewHolder.tv_id= (TextView) convertView.findViewById(R.id.resource_his_release_item_id);
            viewHolder.tv_title= (TextView) convertView.findViewById(R.id.resource_his_release_item_title);
            viewHolder.tv_time= (TextView) convertView.findViewById(R.id.resource_his_release_item_time);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_id.setText(list.get(position).getResourceId());
        viewHolder.tv_title.setText(list.get(position).getResourceTitle());
        viewHolder.tv_time.setText(list.get(position).getReleaseTime());
        return convertView;
    }
    class ViewHolder {
        public TextView tv_id;
        public TextView tv_title;
        public TextView tv_time;
    }
}
