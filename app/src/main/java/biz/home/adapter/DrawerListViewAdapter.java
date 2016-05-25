package biz.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import biz.home.R;

/**
 * expandableListView������
 *
 */
public class DrawerListViewAdapter extends BaseExpandableListAdapter {
	private Context context;
	private List<String> group;
	private List<List<String>> child;
	private int[] groupIcon;
	private List<String> groupHint;

	public DrawerListViewAdapter(Context context, List<String> group,
								 List<List<String>> child) {
		this.context = context;
		this.group = group;
		this.child = child;
	}
	public DrawerListViewAdapter(Context context, List<String> group,
								 List<List<String>> child, int[] groupIcon, List<String> groupHint) {
		this.context = context;
		this.group = group;
		this.child = child;
		this.groupIcon=groupIcon;
		this.groupHint=groupHint;
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
		return child.get(childPosition).get(childPosition);
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
	
	/**
	 * ��ʾ��group
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ViewHolderGroup holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.drawer_group_item, null);
			holder = new ViewHolderGroup();
			holder.groupIcon = (ImageView) convertView
					.findViewById(R.id.drawer_group_icon);
			holder.groupTitle= (TextView) convertView.findViewById(R.id.drawer_group_title);
			holder.groupArrow= (ImageView) convertView.findViewById(R.id.drawer_group_arrow);
			holder.groupHint= (TextView) convertView.findViewById(R.id.drawer_group_hint);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolderGroup) convertView.getTag();
		}
		holder.groupIcon.setImageResource(groupIcon[groupPosition]);
		holder.groupTitle.setText(group.get(groupPosition));
		holder.groupHint.setText(groupHint.get(groupPosition));
		if(isExpanded){	//如果处于展开状态
			holder.groupArrow.setImageResource(R.drawable.drawer_arrow_down);
		}else {
			holder.groupArrow.setImageResource(R.drawable.drawer_arrow_up);
		}
//		holder.textView.setTextSize(25);
//		holder.textView.setPadding(36, 10, 0, 10);
		return convertView;

	}
	
	/**
	 * ��ʾ��child
	 */
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ViewHolderItem holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.drawer_son_item, null);
			holder = new ViewHolderItem();
			holder.textView = (TextView) convertView
					.findViewById(R.id.list_item_tv);
			holder.imageView= (ImageView) convertView.findViewById(R.id.list_item_iv);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolderItem) convertView.getTag();
		}
		holder.textView.setText(child.get(groupPosition).get(childPosition));
		holder.imageView.setImageResource(R.drawable.drawer_son_icon);
//		holder.textView.setTextSize(20);
//		holder.textView.setPadding(72, 10, 0, 10);
		return convertView;
	}

	class ViewHolder {
		TextView textView;
	}
	class ViewHolderGroup {
		ImageView groupIcon;
		TextView groupTitle;
		TextView groupHint;
		ImageView groupArrow;
	}
	class ViewHolderItem {
		TextView textView;
		ImageView imageView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
