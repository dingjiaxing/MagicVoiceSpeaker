package biz.home.util;

import org.litepal.crud.DataSupport;

import biz.home.model.UserInfo;


/**
 * Created by lmw on 2015/7/29.
 */
public class DatabaseUtil {
    /**
     * 得到对应电话的User的UserInfo
     */
    public static UserInfo getUserInfoByTel(String tel){
        UserInfo userInfo;
        userInfo = DataSupport.where("tel=?", tel).find(UserInfo.class).get(0);
        return userInfo;
    }
}
