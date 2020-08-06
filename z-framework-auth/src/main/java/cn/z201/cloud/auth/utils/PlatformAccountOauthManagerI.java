package cn.z201.cloud.auth.utils;

/**
 * @author z201.coding@gmail.com
 **/
public interface PlatformAccountOauthManagerI {

    /**
     * app qq授权 正式、测试
     */
    String APP_QQ_OAUTH_URL = "https://graph.qq.com/user/get_user_info?access_token=%s&oauth_consumer_key=%s&openid=%s";

    /**
     *  app 微信授权 授权地址
     */
    String APP_WX_OAUTH_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    /**
     * app 微信授权 获取用户信息
     */
    String APP_WX_OAUTH_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";

    /**
     * app 微博授权 正式、测试
     */
    String APP_WB_OAUTH_URL = "https://api.weibo.com/2/users/show.json?access_token=%s&uid=%s";

    /**
     * WAP-H5 微博授权 正式、测试
     */
    String WAP_OAUTH_WEB_URL = "https://api.weibo.com/oauth2/access_token?client_id=%s&client_secret=%s&grant_type=%s&code=%s&redirect_uri=%s";

    /**
     * 微信小程序 js 授权地址
     */
    String MINI_APP_WX_OAUTH_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";



}
