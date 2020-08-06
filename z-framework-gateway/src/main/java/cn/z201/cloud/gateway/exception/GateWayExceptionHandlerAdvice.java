package cn.z201.cloud.gateway.exception;

import cn.z201.cloud.alarm.core.AlarmNoticeManage;
import cn.z201.cloud.gateway.dto.Result;
import io.netty.channel.ConnectTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@Component
public class GateWayExceptionHandlerAdvice {

    private static final Logger log = LoggerFactory.getLogger(GateWayExceptionHandlerAdvice.class);

    @Autowired
    AlarmNoticeManage alarmNoticeManage;

    @ExceptionHandler(value = {ResponseStatusException.class})
    @ResponseStatus(HttpStatus.OK)
    public Result handle(ResponseStatusException ex) {
        log.error("response status exception:{}", ex.getMessage());
        if (ex.getStatus().equals(HttpStatus.NOT_FOUND)) {
            return Result.fail("404！");
        }
        if (ex.getStatus().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
            return Result.fail("服务故障,管理人员处理中！");
        }
        return Result.fail("服务开小差,请稍后尝试！");
    }

    @ExceptionHandler(value = {ConnectTimeoutException.class})
    @ResponseStatus(HttpStatus.OK)
    public Result handle(ConnectTimeoutException ex) {
        log.error("connect timeout exception:{}", ex.getMessage());
        sendAlarmNotice(ex,"");
        return Result.fail("服务开小差,请稍后尝试！");
    }

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result handle(NotFoundException ex) {
        log.error("not found exception:{}", ex.getMessage());
        return Result.fail("404！");
    }

    @ExceptionHandler(value = {RuntimeException.class})
    @ResponseStatus(HttpStatus.OK)
    public Result handle(RuntimeException ex) {
        log.error("runtime exception:{}", ex.getMessage());
        sendAlarmNotice(ex,"");
        return Result.fail();
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.OK)
    public Result handle(Exception ex) {
        log.error("exception:{}", ex.getMessage());
        sendAlarmNotice(ex,"");
        return Result.fail();
    }

    @ExceptionHandler(value = {Throwable.class})
    @ResponseStatus(HttpStatus.OK)
    public Result handle(Throwable throwable) {
        Result result = Result.fail();
        if (throwable instanceof ResponseStatusException) {
            result = handle((ResponseStatusException) throwable);
        } else if (throwable instanceof ConnectTimeoutException) {
            result = handle((ConnectTimeoutException) throwable);
        } else if (throwable instanceof NotFoundException) {
            result = handle((NotFoundException) throwable);
        } else if (throwable instanceof RuntimeException) {
            result = handle((RuntimeException) throwable);
        } else if (throwable instanceof Exception) {
            result = handle((Exception) throwable);
        }
        return result;
    }

    private void sendAlarmNotice(Throwable e, String ext) {
        if (null != alarmNoticeManage) {
            alarmNoticeManage.createNotice(e,"", "");
        }else {
            log.error("预警失败 未开启alarm模块");
        }
    }
}
