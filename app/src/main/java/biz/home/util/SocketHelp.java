package biz.home.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import biz.home.bean.MagicInfo;
import biz.home.bean.MagicResult;
import biz.home.bean.MagicResultFunctional;
import biz.home.bean.MagicResultQuestion;
import biz.home.bean.MagicResultText;
import biz.home.bean.MagicResultUrl;
import biz.home.model.MagicResultEnum;

/**
 * Created by admin on 2015/8/12.
 */
public class SocketHelp {
    //经纬度需要的参数
    LocationManager loctionManager;
    String contextService= Context.LOCATION_SERVICE;
//通过系统服务，取得LocationManager对象

    public static String send(String uid, String key, String text) {
        String result = "";
        byte[] send;
        MagicInfo info = new MagicInfo();
        info.setUid(uid);
        info.setToken(key);
        info.setText(text);


//        String str = info.toString();
        String param="json={\"uid\":\"189019235859\",\"app_key\":\"SQe6100cdcee892111\",\"text\": \"1\"}";
        String text2 = "你能为我做什么";


        // 构造数据报的包
        String str2 = "{\"uid\":\"189019235859\",\"token\":\"SQe6100cdcee892111\",\"text\": \"" + text + "\"}";

        // 用了public DatagramPacket(byte buf[], int length,InetAddress address,
        // int port)形式

        try {
            send = str2.getBytes("UTF-8");
            System.out.println(str2);
            DatagramSocket datagramSocket = null;
            try {
                datagramSocket = new DatagramSocket();
            } catch (SocketException e) {
                e.printStackTrace();
            }
//             用了public DatagramPacket(byte buf[], int length,InetAddress address,
            // int port)形式
            DatagramPacket packet = null;
            try {
//                packet = new DatagramPacket(str2.getBytes(),
                //    str.length(), InetAddress.getByName("192.168.15.50"), 7000);
                packet = new DatagramPacket(send,
                        send.length, InetAddress.getByName("192.168.15.39"), 7000);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }

            // 发送数据包
            try {
                datagramSocket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 接收数据包
            byte[] buffer = new byte[1024 * 60];
            DatagramPacket packet2 = new DatagramPacket(buffer, buffer.length);
            try {
                datagramSocket.receive(packet2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 输出接收到的数据
            result = new String(buffer, 0, packet2.getLength());
            System.out.println(result);

            datagramSocket.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 构造数据报的包
        return result;
    }


    public static MagicResult transfer(String s) {
        MagicResult result;
        Gson gson=new Gson();
        result=gson.fromJson(s,MagicResult.class);
        return  result;
    }
    public static String sHA1(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        byte[] cert = info.signatures[0].toByteArray();

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] publicKey = md.digest(cert);
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < publicKey.length; i++) {
            String appendString = Integer.toHexString(publicKey[i]&0xFF).toUpperCase(Locale.US);
            if (appendString.length() == 1)
                hexString.append("0");
            hexString.append(appendString);
            hexString.append(":");
        }
        return hexString.toString();
    }

}
