package cn.z201.cloud.gateway;

import cn.z201.cloud.gateway.dto.entity.GatewayRoute;
import cn.z201.cloud.gateway.service.GatewayRouteServiceI;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.z201.cloud.alarm.core.AlarmNoticeManage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZFrameworkGatewayApplication.class)
//@ActiveProfiles("dev")
public class VlinkFrameworkGatewayApplicationTest {

    @Autowired
    AlarmNoticeManage alarmNoticeManage;

    @Autowired
    GatewayRouteServiceI gatewayRouteService;

    @Test
    public void add() {
        GatewayRoute gatewayRoute = new GatewayRoute();
        gatewayRoute.setId("auth-server");
        gatewayRoute.setUri("lb://AUTH-SERVER");
        gatewayRoute.setOrders(1);
        List<FilterDefinition> filterDefinitionList = new ArrayList<>();
        List<PredicateDefinition> predicateDefinitionList = new ArrayList<>();
        PredicateDefinition predicateDefinition = new PredicateDefinition();
        predicateDefinition.setName("Path");
        Map<String, String> args = new HashMap<>();
        args.put("pattern", "/api/v1/mobile/auth/**");
        predicateDefinition.setArgs(args);
        predicateDefinitionList.add(predicateDefinition);
        FilterDefinition filterDefinition = new FilterDefinition();
        predicateDefinition.setName("StripPrefix");
        args.clear();
        args.put("parts", "3");
        filterDefinition.setArgs(args);
        filterDefinitionList.add(filterDefinition);
        try {
            gatewayRoute.setDescription(new ObjectMapper().writeValueAsString(predicateDefinitionList));
            gatewayRoute.setFilters(new ObjectMapper().writeValueAsString(filterDefinitionList));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        gatewayRouteService.add(gatewayRoute);
    }

    @Test
    public void alarm() {
//        alarmNoticeManage.createNotice(new IllegalAccessException(), "测试默认预警", "");
//
//        alarmNoticeManage.createNotice(new IllegalAccessException(), "测试单人预警", "lx");

//        alarmNoticeManage.createNoticeMany(new IllegalAccessException(),"测试多人预警","zqf","wzn","zss");

        alarmNoticeManage.createNoticeAll(new IllegalAccessException(),"测试全体预警");

    }

}