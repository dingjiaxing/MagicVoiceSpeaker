package biz.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import biz.home.R;
import biz.home.bean.ResourceMyReleasedGroupBean;
import biz.home.bean.ResourceMyReleasedSonBean;
import biz.home.yibuTask.OpenResourceInfoTask;

/**
 * Created by admin on 2016/1/29.
 */
public class ResourceReleasedAdapter extends BaseExpandableListAdapter {
    private Context context;
    List<ResourceMyReleasedGroupBean> group;
    List<List<ResourceMyReleasedSonBean>> child;

    public ResourceReleasedAdapter(Context context, List<ResourceMyReleasedGroupBean> group, List<List<ResourceMyReleasedSonBean>> child) {
        this.context = context;
        this.group = group;
        this.child = child;
    }

    @Override
    public int getGroupCount() {
        return group.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return child.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return child.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final ViewHolderGroup holder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.resource_my_released_group_item,null);
            holder=new ViewHolderGroup();
            holder.groupId= (TextView) convertView.findViewById(R.id.resource_my_released_group_id);
            holder.groupTitle= (TextView) convertView.findViewById(R.id.resource_my_released_group_title);
            holder.groupBrowseCount= (TextView) convertView.findViewById(R.id.resource_my_released_group_browseCount);
            holder.groupButton= (Button) convertView.findViewById(R.id.resource_my_released_group_button);
            holder.groupArrow= (ImageView) convertView.findViewById(R.id.resource_my_released_group_img);
            holder.groupContactCount= (TextView) convertView.findViewById(R.id.resource_my_released_group_contact_count);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolderGroup) convertView.getTag();
        }
        ResourceMyReleasedGroupBean bean=group.get(groupPosition);
        holder.groupId.setText(bean.getGroupResourceId());
        holder.groupTitle.setText(bean.getGroupTitle());
        holder.groupBrowseCount.setText("浏览量："+bean.getGroupBrowseCount());
        holder.groupContactCount.setText("联系量："+bean.getGroupContactCount());
        holder.groupButton.setFocusable(false);
        holder.groupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenResourceInfoTask task=new OpenResourceInfoTask(context,holder.groupId.getText().toString());
                task.execute(1000);
//                Toast.makeText(context,"您点击了按钮",Toast.LENGTH_SHORT).show();
            }
        });
        /*
        if(isExpanded){	//如果处于展开状态
            holder.groupArrow.setImageResource(R.drawable.drawer_arrow_down);
        }else {
            holder.groupArrow.setImageResource(R.drawable.drawer_arrow_up);
        }
        */
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderItem holder;
        if(convertView==null){
            convertView=LayoutInflater.from(context).inflate(R.layout.resource_my_released_son_item,null);
            holder=new ViewHolderItem();
            holder.uid= (TextView) convertView.findViewById(R.id.resource_my_released_son_uid);
            holder.name= (TextView) convertView.findViewById(R.id.resource_my_released_son_person);
            holder.hint= (TextView) convertView.findViewById(R.id.resource_my_released_son_hint);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolderItem) convertView.getTag();
        }
        ResourceMyReleasedSonBean bean=child.get(groupPosition).get(childPosition);
        holder.uid.setText(bean.getUserid());
        holder.name.setText(bean.getName());
        holder.hint.setText(bean.getContactTime());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    class ViewHolderGroup{
        TextView groupId;
        TextView groupTitle;
        TextView groupBrowseCount;
        TextView groupContactCount;
        Button groupButton;
        ImageView groupArrow;
    }
    class ViewHolderItem{
        TextView uid;
        TextView name;
        TextView hint;
    }
}
