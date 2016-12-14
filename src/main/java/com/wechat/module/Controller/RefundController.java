package com.wechat.module.Controller;

import com.wechat.commons.Response;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by CAI_GC on 2016/12/9.
 */
@Controller
@Scope("prototype")
public class RefundController {


    public Object refund(@RequestParam(required = false) String transactionID, @RequestParam(required = false) String outTradeNo,
                         @RequestParam(required = false) String deviceInfo, @RequestParam String outRefundNo,
                         @RequestParam int totalFee, @RequestParam int refundFee,
                         @RequestParam(required = false) String opUserID){
        return Response.success();
    }
}
