package cn.z201.cloud.gateway.manager;

import cn.z201.cloud.gateway.service.impl.RouteServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 * @author z201.coding@gmail.com
 * 这玩意代码没看完，下次在看。
 **/
//@Service
public class DynamicRouteServiceImpl implements ApplicationEventPublisherAware {

    private static final Logger log = LoggerFactory.getLogger(RouteServiceImpl.class);

    private ApplicationEventPublisher publisher;

    /**
     * 这里的事件信息也可以刷新
     */
    public void refresh() {
        log.info("refresh ");
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }
}
