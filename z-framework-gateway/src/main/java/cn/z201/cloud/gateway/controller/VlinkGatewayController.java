package cn.z201.cloud.gateway.controller;

import cn.z201.cloud.gateway.dto.Result;
import cn.z201.cloud.gateway.dto.VlinkGatewayDto;
import cn.z201.cloud.gateway.service.impl.RouteServiceImpl;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author z201.coding@gmail.com
 **/
@RestController
@RequestMapping(VlinkGatewayController.ROUTER_INDEX)
public class VlinkGatewayController {

    public static final String ROUTER_INDEX = "/controller";

    public static final String OP_ID = "Mip11D56lpQymxlx";
    @Autowired
    private RouteServiceImpl routeService;

    @PostMapping(value = "/overload")
    public Object overload(@RequestBody(required = true) VlinkGatewayDto vlinkGatewayDto) {
        String opId = vlinkGatewayDto.getOpId();
        if (null == opId || Strings.isEmpty(opId) || Strings.isEmpty(opId.trim())) {
            return Result.fail();
        }
        routeService.loadRouteDefinition();
        return Result.success();
    }

}
