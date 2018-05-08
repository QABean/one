package cn.migu.newportal.selection.service.selection;

import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.TestResponseOuterClass.TestResponse;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;

public class TestInfoImpl extends ServiceMethodImpl<TestResponse, ComponentRequest>
{
    private static final String METHOD_NAME = "info";

    
    public TestInfoImpl()
    {
        super(METHOD_NAME);
    }
    @ImplementMethod
    public InvokeResult<TestResponse> info(ServiceController ic,ComponentRequest request) throws Exception 
    {
        TestResponse.Builder response = TestResponse.newBuilder();
        response.putRspHeaderMap("rsp_head_key1", "sss");
        response.putRspHeaderMap("rsp_head_key2", "erer");
        return new InvokeResult<TestResponse>(response.build());
    }
    
}
