package cn.z201.cloud.auth.service;


import javax.servlet.http.HttpServletRequest;

/**
 * @author z201.coding@gmail.com
 **/
public interface PlatformAccountServiceI {

    /**
     * 检查图形验证码
     *
     * @param systemCode
     * @param imageId
     * @param code
     */
    void verifyImagesCode(String systemCode, String imageId, String code);

    /**
     * 从请求体里面获取jwt
     * @param request
     * @return
     */
    String getJwt(HttpServletRequest request);

    /**
     * 从请求体里面获取accountId
     * @param request
     * @return
     */
    Long getJwtAccountId(HttpServletRequest request);

    /**
     * 从请求体里面获取accountId 不抛异常
     * @param request
     * @return
     */
    Long getJwtAccountIdNotException(HttpServletRequest request);


}
