package biz.home.api.iml;

import com.android.volley.RequestQueue;
import biz.home.api.VoiceUniversal;
import biz.home.model.VoiceAudio;
import biz.home.model.VoiceText;
import biz.home.model.VoicePersonal;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by adg on 2015/7/30.
 */
public class VoiceUniversalIml implements VoiceUniversal {
    /**
     * 需要上传的语音json数据
     */
    private VoiceAudio voiceAudio;
    /**
     * 需要上传的文本json数据
     */
    private VoiceText voiceText;
    /**
     * 需要上传的用户注册
     */
    private VoicePersonal voicePersonal;
    private static final String SUB="iat";
    private static final String KEY="SQe6100cdcee892111";
    private Gson gson;

    /**
     * 发送请求的json和接收请求的json
     * @param queue
     * @param push
     * @return
     */
    @Override
    public JSONObject Listener(RequestQueue queue, String push) {
        //{uid:"189019235859",key:"SQe6100cdcee892111",sub："iat",audio:"stream语音数据流"}
        gson = new Gson();
        voiceAudio = new VoiceAudio("189019235859",KEY,SUB,"15335135351513");
        String str=gson.toJson(voiceAudio);
        return null;
    }

    @Override
    public JSONObject Compose(RequestQueue queue, String push) {
        return null;
    }

    @Override
    public JSONObject AudioResole(RequestQueue queue, String push) {
        return null;
    }

    @Override
    public JSONObject TextResolve(RequestQueue queue, String push) {
        return null;
    }

    @Override
    public boolean UserRegister(RequestQueue queue, String push) {
        return false;
    }
}
