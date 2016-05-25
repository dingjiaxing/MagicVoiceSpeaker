package biz.home.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import biz.home.api.MapLocation;
import biz.home.bean.MagicInfo;
import biz.home.bean.MagicResourceInfo;
import biz.home.bean.MagicResult;
import biz.home.bean.MagicResultResourceInfo;
import biz.home.model.MagicInfoApiEnum;
import biz.home.model.MagicUserInfo;

/**
 * Created by admin on 2015/8/5.
 */
public class HttpHelp {

    private static final String TAG ="HttpHelp" ;
//    private static String target="http://192.168.1.56:8080/MagicWeb/api";       //吴礼通
//    private static String target="http://192.168.1.225:8280/MagicWeb/api";    //杨军
    private static String target="http://139.196.24.86:80/MagicWeb/api";
    public static MagicResult send(String uid,String key,String text,String deviceId){
        String result="";
//        String target="http://192.168.15.109:8080/MagicWeb/api";
//        String target="http://139.196.24.86:8080/MagicWeb/api";
        URL url;
        try {
            url=new URL(target);
            try {
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setInstanceFollowRedirects(true);
                conn.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                DataOutputStream out=new DataOutputStream(conn.getOutputStream());
//                String param="uid="+ URLEncoder.encode(uid,"utf-8")
//                        +"&key="+ URLEncoder.encode(key,"utf-8")
//                        +"&text="+ URLEncoder.encode(text,"utf-8");
                MagicInfo info=new MagicInfo();
//                uid="18818216390";
                info.setUid(uid);
                info.setToken(key);
                info.setText(text);
                info.setApi(MagicInfoApiEnum.SEND);
                info.setDeviceId(deviceId);
                info.setLocation(MapLocation.location);
                Gson gs=new Gson();
//                String  param=gs.toJson(info);
//                String param=gs.toJson


                GsonBuilder gb =new GsonBuilder();
//                gb.disableHtmlEscaping();
                gb.disableInnerClassSerialization();
                String param=gb.create().toJson(info);
                param="json="+java.net.URLEncoder.encode(param,"utf-8");


//                param=java.net.URLEncoder.encode(param);

//                byte[] by= param.getBytes("UTF-8");
                Log.i("HttpHelp", "send param"+param);
//                param=new String(param.getBytes(), "utf-8");

//                String param="json={\"api\":\"SEND\",\"uid\":\"\",\"token\":\"SQe6100cdcee892111\",\"text\": \"1\"}";
                System.out.println(param);
                out.writeBytes(param);

                out.flush();
                out.close();
                if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                    InputStreamReader in=new InputStreamReader(conn.getInputStream());
                    BufferedReader buffer=new BufferedReader(in);
                    String inputLine=null;
                    while((inputLine=buffer.readLine())!=null){
                        result+=inputLine+"\n";
                    }
                    in.close();
                }
                conn.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try{
            Log.i("HttpResult", result);
        }catch (Exception e){
            e.printStackTrace();
        }
        return transfer(result);
    }
    public static MagicResult send(String uid,String key,String text,String deviceId,MagicResultResourceInfo resourceInfo){
        String result="";
//        String target="http://192.168.15.109:8080/MagicWeb/api";
//        String target="http://192.168.1.4:8080/MagicWeb/api";
//        String target="http://139.196.24.86:8080/MagicWeb/api";
        URL url;
        try {
            url=new URL(target);
            try {
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setInstanceFollowRedirects(true);
                conn.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                DataOutputStream out=new DataOutputStream(conn.getOutputStream());
//                String param="uid="+ URLEncoder.encode(uid,"utf-8")
//                        +"&key="+ URLEncoder.encode(key,"utf-8")
//                        +"&text="+ URLEncoder.encode(text,"utf-8");
                MagicInfo info=new MagicInfo();
//                uid="18818216390";
                info.setResourceInfo(resourceInfo);
                info.setUid(uid);
                info.setToken(key);
                info.setText(text);
                info.setApi(MagicInfoApiEnum.SEND);
                info.setDeviceId(deviceId);
                info.setLocation(MapLocation.location);
                Log.i(TAG, "send info.getLocation:" + info.getLocation());
                Gson gs=new Gson();
//                String  param=gs.toJson(info);
//                String param=gs.toJson


                GsonBuilder gb =new GsonBuilder();
//                gb.disableHtmlEscaping();
                gb.disableInnerClassSerialization();
                String param=gb.create().toJson(info);
                param="json="+java.net.URLEncoder.encode(param,"utf-8");


//                param=java.net.URLEncoder.encode(param);

//                byte[] by= param.getBytes("UTF-8");
                Log.i("HttpHelp", "send param"+param);
//                param=new String(param.getBytes(), "utf-8");

//                String param="json={\"api\":\"SEND\",\"uid\":\"\",\"token\":\"SQe6100cdcee892111\",\"text\": \"1\"}";
                System.out.println(param);
                out.writeBytes(param);

                out.flush();
                out.close();
                if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                    InputStreamReader in=new InputStreamReader(conn.getInputStream());
                    BufferedReader buffer=new BufferedReader(in);
                    String inputLine=null;
                    while((inputLine=buffer.readLine())!=null){
                        result+=inputLine+"\n";
                    }
                    in.close();
                }
                conn.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try{
            Log.i("HttpResult", result);
        }catch (Exception e){
            e.printStackTrace();
        }
        return transfer(result);
    }
    public static String send(MagicInfo info){
        String result="";
//        String target="http://192.168.1.4:8080/MagicWeb/api";
//        String target="http://139.196.24.86:8080/MagicWeb/api";
        URL url;
        try {
            url=new URL(target);
            try {
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setInstanceFollowRedirects(true);
                conn.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                conn.setConnectTimeout(5000);
                DataOutputStream out=new DataOutputStream(conn.getOutputStream());
                GsonBuilder gb =new GsonBuilder();
//                gb.disableHtmlEscaping();
                gb.disableInnerClassSerialization();
                String param=gb.create().toJson(info);
                param="json="+java.net.URLEncoder.encode(param,"utf-8");
                System.out.println(param);
                out.writeBytes(param);
                out.flush();
                out.close();
                if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                    InputStreamReader in=new InputStreamReader(conn.getInputStream());
                    BufferedReader buffer=new BufferedReader(in);
                    String inputLine=null;
                    while((inputLine=buffer.readLine())!=null){
                        result+=inputLine+"\n";
                    }
                    in.close();
                }
                conn.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try{
            Log.i("HttpResult", result);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("从服务器拿到的结果：" + result);
        Log.i(TAG, "send从服务器拿到的结果： "+result);
        return result;

    }
    public static MagicResult transfer(String s) {
        //最初的s格式为{"status":"success","json":"{\"resultText\":{\"content\":\"该用户已注册189019235859\"},\"timeLag\":0}"}
        try {
            Log.i(TAG, "transfer "+s);
            org.json.JSONObject json=new org.json.JSONObject(s);
            s=json.getString("json");
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }

        MagicResult result;
        Gson gson=new Gson();

        result=gson.fromJson(s,MagicResult.class);
        Log.i("HttpHelp", "transfer result" + result);
        return  result;
    }
    public static List<MagicResult> transferList(String s) {
        //最初的s格式为{"status":"success","json":"{\"resultText\":{\"content\":\"该用户已注册189019235859\"},\"timeLag\":0}"}
        try {
            Log.i(TAG, "transfer "+s);
            org.json.JSONObject json=new org.json.JSONObject(s);


            s=json.getString("json");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "transferList json:"+s);
//        JSONArray  jsonArray= (JSONArray) JSONArray.toArray(JSONArray.fromObject(s));
////        org.json.JSONArray jsonArray2=org.json.JSONArray.p
//        List list=JSONArray.toList(jsonArray,MagicResult.class);
//        return list;


        try {
            org.json.JSONArray lis=new org.json.JSONArray(s);
            List<MagicResult> da=new ArrayList<MagicResult>();
         for(int i=0;i<lis.length();i++)
         {
             MagicResult result;
             Gson gson=new Gson();
             result=gson.fromJson(lis.get(i).toString(),MagicResult.class);
             da.add(result);
         }
        return da;
        } catch (org.json.JSONException e) {
            e.printStackTrace();
            return  null;
        }


//        JSONArray lis=JSONArray.fromObject(s);
//        JSONObject lie=JSONObject.fromObject()
//
//        List<MagicResult> da=new ArrayList<MagicResult>();
//         for(int i=0;i<lis.size();i++)
//         {
//             MagicResult result;
//             Gson gson=new Gson();
//             result=gson.fromJson(lis.get(i).toString(),MagicResult.class);
//             da.add(result);
//         }
//        return da;
//        List<MagicResult> list;
//        Gson gson=new Gson();
//        list=gson.fromJson(s, );
//        Log.i("HttpHelp", "transfer result"+result);
//        return  list;
    }
    /**
        * android上传文件到服务器
        *
        * 下面为 http post 报文格式
        *
       POST/logsys/home/uploadIspeedLog!doDefault.html HTTP/1.1
      　　 Accept: text/plain,
      　　 Accept-Language: zh-cn
      　　 Host: 192.168.24.56
      　　 Content-Type:multipart/form-data;boundary=-----------------------------7db372eb000e2
      　　 User-Agent: WinHttpClient
      　　 Content-Length: 3693
      　　 Connection: Keep-Alive   注：上面为报文头
      　　 -------------------------------7db372eb000e2
      　　 Content-Disposition: form-data; name="file"; filename="kn.jpg"
      　　 Content-Type: image/jpeg
      　　 (此处省略jpeg文件二进制数据...）
     　　 -------------------------------7db372eb000e2--
        *
        * @param picPaths
        *            需要上传的文件路径集合
        * @param requestURL
        *            请求的url
        * @return 返回响应的内容
        */
    public static Map<String,String> transferPicResultToMap(String s){
        Map<String,String> map=new HashMap<String,String>();
        Gson gson=new Gson();
        map=gson.fromJson(s,Map.class);
        return map;
    }
    public static String uploadFile(String uid,String telephone,String[] picPaths,Map<String,String> map) {
        //        String target="http://192.168.1.4:8080/MagicWeb/api";
//        String target="http://139.196.24.86:8080/MagicWeb/api";
//        String requestURL="http://magic.4350.biz/magic/uploadimage.ashx";
//       String requestURL="http://192.168.1.225:8180/MagicWeb/resourceImageToUpload";
//       String requestURL="http://139.196.24.86:80/MagicWeb/resourceImageToUpload";
        String requestURL=target;
        String boundary = UUID.randomUUID().toString(); // 边界标识 随机生成
        String prefix = "--", end = "\r\n";
        String content_type = "multipart/form-data"; // 内容类型
        String CHARSET = "utf-8"; // 设置编码
        String result="";       //从服务器拿到的结果字符串
        int TIME_OUT = 10 * 10000000; // 超时时间
        try {
            URL url = null;
            try {
                url = new URL(requestURL);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            try {
                conn.setRequestMethod("POST"); // 请求方式
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            conn.setRequestProperty("Charset", "utf-8"); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", content_type + ";boundary=" + boundary);
            /**
                 * 当文件不为空，把文件包装并且上传
             */
            OutputStream outputSteam = conn.getOutputStream();
            DataOutputStream dos = new DataOutputStream(outputSteam);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(prefix);
            stringBuffer.append(boundary);
            stringBuffer.append(end);
            String name = "userName";
            GsonBuilder gb =new GsonBuilder();
//                gb.disableHtmlEscaping();
            gb.disableInnerClassSerialization();

            MagicInfo info=new MagicInfo();
            info.setApi(MagicInfoApiEnum.RESOURCEIMAGETOUPLOAD);
            info.setUid(uid);
            MagicResultResourceInfo resourceInfo=new MagicResultResourceInfo();
            resourceInfo.setImage(map);
            info.setResourceInfo(resourceInfo);
            String param=gb.create().toJson(info);
            param=java.net.URLEncoder.encode(param,"utf-8");
            System.out.println(param);
            dos.writeBytes(param);
            //将MagicInfo作为参数传输
            try {
                dos.write(stringBuffer.toString().getBytes());

//                dos.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"" + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"" + "json" + "\"" + end);
                dos.writeBytes(end);
                dos.writeBytes(param);
                dos.writeBytes(end);
            } catch (IOException e) {
                e.printStackTrace();
            }
            /*
            //将uid作为参数传输
            try {
                dos.write(stringBuffer.toString().getBytes());

//                dos.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"" + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"" + "uid" + "\"" + end);
                dos.writeBytes(end);
                dos.writeBytes(uid);
                dos.writeBytes(end);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //将telephone作为参数传输
            stringBuffer = new StringBuffer();
            stringBuffer.append(prefix);
            stringBuffer.append(boundary);
            stringBuffer.append(end);

            try {
                dos.write(stringBuffer.toString().getBytes());

//                dos.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"" + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"" + "phone" + "\"" + end);
                dos.writeBytes(end);
                dos.writeBytes(telephone);
                dos.writeBytes(end);
            } catch (IOException e) {
                e.printStackTrace();
            }
            */
            //将imageList作为参数传输
            stringBuffer = new StringBuffer();
            stringBuffer.append(prefix);
            stringBuffer.append(boundary);
            stringBuffer.append(end);

//            GsonBuilder gb =new GsonBuilder();
//            gb.disableInnerClassSerialization();
            String imageList=gb.create().toJson(map);
            try {
                dos.write(stringBuffer.toString().getBytes());
//                dos.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"" + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"" + "imageList" + "\"" + end);
                dos.writeBytes(end);
                dos.writeBytes(imageList);
                dos.writeBytes(end);
            } catch (IOException e) {
                e.printStackTrace();
            }

            for(int i = 0; i < picPaths.length; i++){
                 File file = new File(picPaths[i]);
                 StringBuffer sb = new StringBuffer();
                 sb.append(prefix);
                 sb.append(boundary);
                 sb.append(end);
                /**
                     * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                     * filename是文件的名字，包含后缀名的 比如:abc.png
                 */
                sb.append("Content-Disposition: form-data; name=\"" + i + "\"; filename=\"" + file.getName() + "\"" + end);
                sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + end);
                sb.append(end);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[8192];//8k
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                    }
                is.close();
                dos.write(end.getBytes());//一个文件结束标志
            }
            byte[] end_data = (prefix + boundary + prefix + end).getBytes();//结束 http 流
            dos.write(end_data);
            dos.flush();
            /**
                * 获取响应码 200=成功 当响应成功，获取响应的流
             */
            int res = conn.getResponseCode();
            Log.e("TAG", "response code:" + res);
            if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                InputStreamReader in=new InputStreamReader(conn.getInputStream());
                BufferedReader buffer=new BufferedReader(in);
                String inputLine=null;
                while((inputLine=buffer.readLine())!=null){
                    result+=inputLine+"\n";
                }
                in.close();
            }

//            if (res == 200) {
//                 return true;
//                   }
             } catch (MalformedURLException e) {
              e.printStackTrace();
              } catch (IOException e) {
               e.printStackTrace();
              }
        Log.i(TAG, "uploadFile result:"+result);
              return result;

//          return false;
         }
    public static String uploadVoiceFile(String uid,String messageId,String path) {
        //        String target="http://192.168.1.4:8080/MagicWeb/api";
//        String target="http://139.196.24.86:8080/MagicWeb/api";
        String requestURL=target;
        String boundary = UUID.randomUUID().toString(); // 边界标识 随机生成
        String prefix = "--", end = "\r\n";
        String content_type = "multipart/form-data"; // 内容类型
        String CHARSET = "utf-8"; // 设置编码
        String result="";       //从服务器拿到的结果字符串
        int TIME_OUT = 10 * 10000000; // 超时时间
        try {
            URL url = null;
            try {
                url = new URL(requestURL);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            try {
                conn.setRequestMethod("POST"); // 请求方式
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            conn.setRequestProperty("Charset", "utf-8"); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", content_type + ";boundary=" + boundary);
            /**
             * 当文件不为空，把文件包装并且上传
             */
            OutputStream outputSteam = conn.getOutputStream();
            DataOutputStream dos = new DataOutputStream(outputSteam);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(prefix);
            stringBuffer.append(boundary);
            stringBuffer.append(end);


            MagicInfo info=new MagicInfo();
            info.setUid(uid);
            info.setApi(MagicInfoApiEnum.VOICE);
            info.setMessageId(messageId);
            GsonBuilder gb =new GsonBuilder();
//                gb.disableHtmlEscaping();
            gb.disableInnerClassSerialization();
            String param=gb.create().toJson(info);
//            param="json="+java.net.URLEncoder.encode(param,"utf-8");
//            System.out.println(param);
//            out.writeBytes(param);
            //将telephone作为参数传输
            stringBuffer = new StringBuffer();
            stringBuffer.append(prefix);
            stringBuffer.append(boundary);
            stringBuffer.append(end);

            //将uid作为参数传输
            try {
                dos.write(stringBuffer.toString().getBytes());

//                dos.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"" + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"" + "json" + "\"" + end);
                dos.writeBytes(end);
                dos.writeBytes(java.net.URLEncoder.encode(param,"utf-8"));
                dos.writeBytes(end);
            } catch (IOException e) {
                e.printStackTrace();
            }




//            for(int i = 0; i < picPaths.length; i++){
                File file = new File(path);
                StringBuffer sb = new StringBuffer();
                sb.append(prefix);
                sb.append(boundary);
                sb.append(end);
                /**
                 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的 比如:abc.png
                 */
                sb.append("Content-Disposition: form-data; name=\"" + 1 + "\"; filename=\"" + file.getName() + "\"" + end);
                sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + end);
                sb.append(end);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[8192];//8k
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(end.getBytes());//一个文件结束标志
//            }
            byte[] end_data = (prefix + boundary + prefix + end).getBytes();//结束 http 流
            dos.write(end_data);
            dos.flush();
            /**
             * 获取响应码 200=成功 当响应成功，获取响应的流
             */
            int res = conn.getResponseCode();
            Log.e("TAG", "response code:" + res);
            if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                InputStreamReader in=new InputStreamReader(conn.getInputStream());
                BufferedReader buffer=new BufferedReader(in);
                String inputLine=null;
                while((inputLine=buffer.readLine())!=null){
                    result+=inputLine+"\n";
                }
                in.close();
            }

//            if (res == 200) {
//                 return true;
//                   }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "uploadVoiceFile result:"+result);
        return result;

//          return false;
    }
    public static String getJsonString(String s){
        try {
            Log.i(TAG, "transfer "+s);
            org.json.JSONObject json=new org.json.JSONObject(s);
            s=json.getString("json");
            return s;
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
        return s;
    }



}
