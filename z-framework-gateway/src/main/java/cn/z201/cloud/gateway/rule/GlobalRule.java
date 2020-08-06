package cn.z201.cloud.gateway.rule;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.Server;

import java.util.List;
import java.util.Optional;

/**
 * @author z201.coding@gmail.com
 **/
public class GlobalRule implements IRule {

    private ILoadBalancer iLoadBalancer;

    @Override
    public Server choose(Object key) {
        ILoadBalancer lb = getLoadBalancer();
        List<Server> server = lb.getAllServers();
        if (server.isEmpty()) {
            return null;
        }
        for(Server item : server){
            if (item.getId().equals(key)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public void setLoadBalancer(ILoadBalancer lb) {
        this.iLoadBalancer = lb;
    }

    @Override
    public ILoadBalancer getLoadBalancer() {
        return this.iLoadBalancer;
    }
}
