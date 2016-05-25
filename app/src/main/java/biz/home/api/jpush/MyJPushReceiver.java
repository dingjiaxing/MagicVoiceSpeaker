package biz.home.api.jpush;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import biz.home.R;
import biz.home.SpeakToitActivity;
import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyJPushReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";
	private List<NotificationMessage> notificationList=new ArrayList<>();
	public static List<Integer> list=new ArrayList<>();
	public static int count=0;
	private static  Boolean flag=false;
	private static String alertContent="";
	public static Boolean isWillOpenMain=false;
	NotificationManager nm = null;

	@Override
	public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
//		String s=bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
//		Log.i(TAG, "[MyReceiver] 接收Registration Id : " + s);
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
                        
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
        	Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
			if(!SpeakToitActivity.isForeground){
				count++;
				int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
				processCustomMessage(context, bundle);
				String service = Context.NOTIFICATION_SERVICE;
				nm = (NotificationManager) context.getSystemService(service);          //获得系统级服务，用于管理消息
				NotificationCompat.Builder builder=new NotificationCompat.Builder(context);
				builder.setSmallIcon(R.drawable.app_icon_small);
				builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.app_theme));
				builder.setNumber(count);
				builder.setTicker(alertContent);
				builder.setWhen(System.currentTimeMillis());
				Notification n=builder.build();
				n.flags |=Notification.FLAG_AUTO_CANCEL;
				/*
				Notification n = new Notification();                                        //定义一个消息类
				n.icon = R.drawable.app_icon_small;                                              //设置图标
				n.tickerText = alertContent;                                        // 设置消息
				n.number=count;
				n.flags |=Notification.FLAG_AUTO_CANCEL;
				n.when = System.currentTimeMillis();                             //设置时间
// Notification n1 =new Notification(icon,tickerText,when);    //也可以用这个构造创建
				*/
				Intent realIntent = new Intent(context, SpeakToitActivity.class);
				realIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				Intent clickIntent=new Intent(context,MessageClickReceiver.class);
				clickIntent.putExtra("realIntent", realIntent);

//			clickIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			intent.putExtra(SpeakToitActivity.KEY_MESSAGE,alertContent);
				PendingIntent pi = PendingIntent.getBroadcast(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);       //消息触发后调用
				n.setLatestEventInfo(context, "神奇小秘书", alertContent, pi); //设置事件信息就是拉下标题后显示的内容
				nm.notify(1001, n);      //发送通知
			}


        
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {

            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

			//将该通知存入list
//			NotificationMessage m=new NotificationMessage();
//			m.setNotificationId((long) notifactionId);
//			m.setIsOpen(false);
//			notificationList.add(m);
			if(notifactionId!=100000000){
				list.add(notifactionId);
				int count=list.size();
				if(count>1){
					Random ran=new Random();
					int id=ran.nextInt(10000);
					JPushInterface.clearAllNotifications(context);
					/*
					//自定义通知
					JPushLocalNotification ln = new JPushLocalNotification();
					ln.setBuilderId(0);
					ln.setContent(alertContent);
					ln.setTitle("Magic神奇秘书（"+count+"条消息)");
					ln.setNotificationId(100000000);
					ln.setBroadcastTime(System.currentTimeMillis());
					//自定义消息
					Map<String , Object> map = new HashMap<String, Object>() ;
					map.put("name", "jpush") ;
					map.put("test", "111") ;
					JSONObject json = new JSONObject(map) ;
					ln.setExtras(json.toString()) ;
					JPushInterface.addLocalNotification(context, ln);
					*/
					String service = Context.NOTIFICATION_SERVICE;
					nm = (NotificationManager) context.getSystemService(service);          //获得系统级服务，用于管理消息
					Notification n = new Notification();                                        //定义一个消息类
					n.icon = R.drawable.app_icon_small;                                         //设置图标
					n.tickerText = alertContent;                                        // 设置消息
					n.number=count;
					n.flags=Notification.FLAG_AUTO_CANCEL;
					n.when = System.currentTimeMillis();                             //设置时间
// Notification n1 =new Notification(icon,tickerText,when);    //也可以用这个构造创建
					intent = new Intent(context, SpeakToitActivity.class);
					PendingIntent pi = PendingIntent.getActivity(context, 0,intent, 0);       //消息触发后调用
					n.setLatestEventInfo(context, "Magic神奇秘书（"+count+"条消息)", alertContent,pi); //设置事件信息就是拉下标题后显示的内容
					nm.notify(1, n);      //发送通知
				}else {
					String service = Context.NOTIFICATION_SERVICE;
					nm = (NotificationManager) context.getSystemService(service);          //获得系统级服务，用于管理消息
					Notification n = new Notification();                                        //定义一个消息类
					n.icon = R.drawable.app_icon_small;                                              //设置图标
					n.tickerText = alertContent;                                        // 设置消息
					n.number=count;
					n.flags=Notification.FLAG_AUTO_CANCEL;
					n.when = System.currentTimeMillis();                             //设置时间
// Notification n1 =new Notification(icon,tickerText,when);    //也可以用这个构造创建
					intent = new Intent(context, SpeakToitActivity.class);
					PendingIntent pi = PendingIntent.getActivity(context, 0,intent, 0);       //消息触发后调用
					n.setLatestEventInfo(context, "Magic神奇秘书", alertContent,pi); //设置事件信息就是拉下标题后显示的内容
					nm.notify(1, n);      //发送通知
					list.add(200000000);
				}
			}else{

			}


        	
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
			isWillOpenMain=true;
			int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			JPushInterface.clearAllNotifications(context);
			JPushInterface.clearNotificationById(context,10000);
			list.clear();
        	//打开自定义的Activity
        	Intent i = new Intent(context, SpeakToitActivity.class);
        	i.putExtras(bundle);
//        	i.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        	context.startActivity(i);
        	
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        	
        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
        	boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
        	Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
        	Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if(key.equals(JPushInterface.EXTRA_MESSAGE)){
				Log.i(TAG, "printBundle 提醒内容"+bundle.getString(key));
				alertContent=bundle.getString(key);
			}
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
					Log.i(TAG, "This message has no Extra data");
					continue;
				}

				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it =  json.keys();

					while (it.hasNext()) {
						String myKey = it.next().toString();
						sb.append("\nkey:" + key + ", value: [" +
								myKey + " - " +json.optString(myKey) + "]");

					}
				} catch (JSONException e) {
					Log.e(TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}
	
	//send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
		if (SpeakToitActivity.isForeground) {
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Intent msgIntent = new Intent(SpeakToitActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(SpeakToitActivity.KEY_MESSAGE, message);
			if (!ExampleUtil.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					if (null != extraJson && extraJson.length() > 0) {
						msgIntent.putExtra(SpeakToitActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			context.sendBroadcast(msgIntent);
		}
	}
}
