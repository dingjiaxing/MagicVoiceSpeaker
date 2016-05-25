package biz.home.api;

import com.android.volley.RequestQueue;

import org.json.JSONObject;

/**
 * Created by adg on 2015/7/20.
 */
public interface  VoiceUniversal {
    /**
     * 语音听写
     * @param queue
     * @param push
     * @return
     */
    JSONObject Listener(RequestQueue queue,String push);

    /**
     * 语音合成
     * @param queue
     * @param push
     * @return
     */
    JSONObject Compose(RequestQueue queue,String push);

    /**
     * 音频解析
     * 返回格式有五种，在实现方法中需要进行处理
     * @param queue
     * @param push
     * @return
     */
    JSONObject AudioResole(RequestQueue queue,String push);

    /**
     * 文本解析
     * 返回格式有五种，在实现方法中需要进行处理
     * @param queue
     * @param push
     * @return
     */
    JSONObject TextResolve(RequestQueue queue,String push);

    /**
     * 用户注册
     * @param queue
     * @param push
     * @return
     */
    boolean UserRegister(RequestQueue queue,String push);
}
