package cn.z201.cloud.gateway.controller;

import cn.z201.cloud.gateway.dto.VlinkGatewayDto;
import org.junit.Test;

public class VlinkGatewayControllerTest {

//            public String server = "http://localhost:9000";
    public String server = "http://122.224.34.237:9000";

    @Test
    public void overload() {
        String url = server + "/controller/overload";
        VlinkGatewayDto vlinkGatewayDto = VlinkGatewayDto.builder()
                .opId("eyJhY2NvdW50SWQiOjUwLCJjb2RlIjoiMTczNjE3MmEtNjU3MS00ZmQ4LWFmOWQtZTBjNTYwYmU0OWQxIiwic3lzdGVtQ29kZSI6IkZTMDAwMSIsInRpbWVzdGFtcCI6MTU3MjIzMTY3MjI3NX0=")
                .build();
        try {
            TestUtils.post(url, vlinkGatewayDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}