package cn.z201.cloud.auth.exception;

import cn.z201.cloud.alarm.core.AlarmNoticeManage;
import cn.z201.cloud.auth.ZSpringContent;
import cn.z201.cloud.auth.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author z201.coding@gmail.com
 **/
@ControllerAdvice
@ResponseBody
@Slf4j
public class CommonExceptionHandler {

    @Resource
    AlarmNoticeManage alarmNoticeManage;

    @Autowired
    ZSpringContent vlinkSpringContent;

    /**
     * 处理业务常规异常
     * 基于@InnerException
     *
     * @param
     */
    @ExceptionHandler(AuthException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object authException(HttpServletRequest request, HttpServletResponse response, AuthException e) {
        response.setContentType("application/json;charset=UTF-8");
        log.error("authException {} \n {} ", e.getMessage(), e);
        return Result.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = Throwable.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object defaultErrorHandler(HttpServletRequest httpServletRequest, HttpServletResponse response, Throwable e) {
        response.setContentType("application/json;charset=UTF-8");
        log.error("defaultErrorHandler {} \n {} ", e.getMessage(), e);
        sendAlarmNotice(e, "defaultErrorHandler");
        return Result.error(Result.Code.NO_UPDATE.getCode(), "网络开小差，请稍后在尝试！");
    }

    private void sendAlarmNotice(Throwable e, String ext) {
        if (null != alarmNoticeManage) {
            String extMsg = ext + vlinkSpringContent.instanceInfo();
            alarmNoticeManage.createNoticeMany(e, "", extMsg, "");
        } else {
            log.error("预警失败 未开启alarm模块");
        }
    }

}
