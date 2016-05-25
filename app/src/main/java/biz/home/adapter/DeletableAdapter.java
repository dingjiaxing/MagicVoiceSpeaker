package biz.home.adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import biz.home.R;
import biz.home.alarm.OneShotAlarm;
import biz.home.alarm.RepeatingAlarm;
import biz.home.assistActivity.MarqueeText;
import biz.home.bean.AlarmInfo;
import biz.home.util.AlarmInfoDao;

/**
 * Created by admin on 2015/10/8.
 */
public class DeletableAdapter extends BaseAdapter{
    private AlarmInfoDao alarmInfoDao;
    private Context context;
    private ArrayList<String> text,text2;

    public DeletableAdapter(Context context, ArrayList<String> text, ArrayList<String> text2) {
        this.context = context;
        this.text = text;
        this.text2=text2;
        alarmInfoDao=new AlarmInfoDao(context);
    }

    @Override
    public int getCount() {
        return text.size();
    }

    @Override
    public Object getItem(int position) {
        return text.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int index = position;
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_simple_list_item_2, null);
        }
        final MarqueeText textView = (MarqueeText) view
                .findViewById(R.id.simple_item_1);
        textView.setText(text.get(position));
        final TextView textView2 = (TextView) view
                .findViewById(R.id.simple_item_3);
        textView2.setText(text2.get(position));
        final ImageView imageView = (ImageView) view
                .findViewById(R.id.simple_item_2);
//        imageView.setBackgroundResource(android.R.drawable.ic_delete);
//        imageView.setBackgroundResource(R.drawable.alarms_list_ic_delete);
        imageView.setTag(position);
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
                text.remove(index);
                text2.remove(index);
                notifyDataSetChanged();
//                Toast.makeText(context, textView2.getText().toString(),
//                        Toast.LENGTH_SHORT).show();
                //在系统服务中删除该闹钟
                AlarmInfo alarmInfo = alarmInfoDao.find(Integer.parseInt(textView2.getText().toString().trim()));
                Intent intent;
                if (alarmInfo.getIsRepeat() == 0) {
                    intent = new Intent(context, OneShotAlarm.class);
                } else {
                    intent = new Intent(context, RepeatingAlarm.class);
                }
                PendingIntent sender = PendingIntent.getBroadcast(
                        context, alarmInfo.getAlarmId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                am.cancel(sender);

                //在数据库中删除该闹钟
                alarmInfoDao.delete(Integer.parseInt(textView2.getText().toString()));
            }
        });
        return view;
    }
}

