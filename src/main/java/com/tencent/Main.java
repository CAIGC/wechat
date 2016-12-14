package com.tencent;

import com.tencent.common.Configure;
import com.tencent.common.Util;
import com.tencent.protocol.refund_protocol.RefundReqData;

public class Main {

    public static void main(String[] args) {

        try {

            //--------------------------------------------------------------------
            //温馨提示，第一次使用该SDK时请到com.tencent.common.Configure类里面进行配置
            //--------------------------------------------------------------------



            //--------------------------------------------------------------------
            //PART One:基础组件测试
            //--------------------------------------------------------------------

            //1）https请求可用性测试
            //HTTPSPostRquestWithCert.test();

            //2）测试项目用到的XStream组件，本项目利用这个组件将Java对象转换成XML数据Post给API
            //XStreamTest.test();


            //--------------------------------------------------------------------
            //PART Two:基础服务测试
            //--------------------------------------------------------------------

            //1）测试被扫支付API
            //PayServiceTest.test();

            //2）测试被扫订单查询API
            //PayQueryServiceTest.test();

            //3）测试撤销API
            //温馨提示，测试支付API成功扣到钱之后，可以通过调用PayQueryServiceTest.test()，将支付成功返回的transaction_id和out_trade_no数据贴进去，完成撤销工作，把钱退回来 ^_^v
            //ReverseServiceTest.test();

            //4）测试退款申请API
            //RefundServiceTest.test();

            //5）测试退款查询API
            //RefundQueryServiceTest.test();

            //6）测试对账单API
            //DownloadBillServiceTest.test();


            //本地通过xml进行API数据模拟的时候，先按需手动修改xml各个节点的值，然后通过以下方法对这个新的xml数据进行签名得到一串合法的签名，最后把这串签名放到这个xml里面的sign字段里，这样进行模拟的时候就可以通过签名验证了
           // Util.log(Signature.getSignFromResponseString(Util.getLocalXMLString("/test/com/tencent/business/refundqueryserviceresponsedata/refundquerysuccess2.xml")));

            //Util.log(new Date().getTime());
            //Util.log(System.currentTimeMillis());

            Configure.setAppID("wxddb369039dfc2b73");
            Configure.setMchID("1265885801");
            Configure.setCertLocalPath("D:\\setup\\cert\\apiclient_cert.p12");
            Configure.setCertPassword("1265885801");
            Configure.setKey("4hVCtZtewOYsPZmv4eTBH6i7CbTJ5QkS");


/*            System.out.println(WXPay.requestRefundQueryService(new RefundQueryReqData("1002110119201510281361630262",null,"1","1",null)));;*/
            /**
             * 请求退款服务
             * @param transactionID 是微信系统为每一笔支付交易分配的订单号，通过这个订单号可以标识这笔交易，它由支付订单API支付成功时返回的数据里面获取到。建议优先使用
             * @param outTradeNo 商户系统内部的订单号,transaction_id 、out_trade_no 二选一，如果同时存在优先级：transaction_id>out_trade_no
             * @param deviceInfo 微信支付分配的终端设备号，与下单一致
             * @param outRefundNo 商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
             * @param totalFee 订单总金额，单位为分
             * @param refundFee 退款总金额，单位为分,可以做部分退款
             * @param opUserID 操作员帐号, 默认为商户号
             * @param refundFeeType 货币类型，符合ISO 4217标准的三位字母代码，默认为CNY（人民币）
             */
            System.out.println( "-------------------\n"+WXPay.requestRefundService(new RefundReqData("1002110119201511111553951041", null, null, "1002110119201511111553951041", 1, 1, "1265885801", "")));
//            System.out.println(WXPay.requestScanPayQueryService(new ScanPayQueryReqData("1002110119201510281365171933", null)));
//            System.out.println(WXPay.requestDownloadBillService(new DownloadBillReqData("", "20151030", "ALL")));
//            System.out.println(WXPay.requestRefundQueryService(new RefundQueryReqData("1002110119201510281365171934", "", "","","")));
        } catch (Exception e){
            Util.log(e.getMessage());
        }

    }

}
