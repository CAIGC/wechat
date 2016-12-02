package com.wechat.module.wxmessage.service;

import com.wechat.module.access_token.bean.AccessToken;
import com.wechat.module.access_token.utils.AccessTokenUtil;
import com.wechat.module.userInfo.bean.UserInfo;
import com.wechat.module.userInfo.service.UserInfoService;
import com.wechat.module.wxmessage.bean.response.Article;
import com.wechat.module.wxmessage.bean.response.NewsMessage;
import com.wechat.module.wxmessage.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by CAI_GC on 2016/12/2.
 */
@Service
public class WxMessageService {


    @Autowired
    private UserInfoService userInfoService;

    public String processRequest(HttpServletRequest request) {
        String respMessage = null;
        try {
            // 默认返回的文本消息内容
            // String respContent = "请求处理异常，请稍候尝试！";
            String respContent = null;
            // xml请求解析
            Map<String, String> requestMap = MessageUtil.parseXml(request);
            // 发送方帐号（open_id）
            String fromUserName = requestMap.get("FromUserName");
            // 公众帐号
            String toUserName = requestMap.get("ToUserName");
            // 消息类型
            String msgType = requestMap.get("MsgType");

            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
                // 事件类型
                String eventType = requestMap.get("Event");
                // 订阅
                if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {

                    NewsMessage newsMessage = new NewsMessage();
                    newsMessage.setToUserName(fromUserName);
                    newsMessage.setFromUserName(toUserName);
                    newsMessage.setCreateTime(new Date().getTime());
                    newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
                    newsMessage.setFuncFlag(0);

                    String eventKey = requestMap.get("EventKey");
                    Integer sceneId = eventKey.equals("") ? 0 : Integer.parseInt(eventKey.substring(eventKey.indexOf('_') + 1, eventKey.length()));

                    userInfoService.sentUserInfo(fromUserName);
                    //respContent = "欢迎"+user.getNickName()+"！谢谢您的关注！";
                    setArticles(newsMessage);
                    respMessage = MessageUtil.newsMessageToXml(newsMessage);
                }
                // 取消订阅
                else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
                    //取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
                    userInfoService.sentUserInfo(fromUserName);

                }
                // 自定义菜单点击事件
                else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {

                    String eventKey = requestMap.get("EventKey");

//                    if (eventKey.equals("contact_customer")) {
//                        moreCustomerService.createSession(fromUserName, moreCustomerService.getKfAccount(Config.CREATE_MENU_FLAG), moreCustomerService.responseContent());
//                    }

                }
            } else {
                // 回复文本消息
//                RespTextMessage textMessage = new RespTextMessage();
//                textMessage.setToUserName(fromUserName);
//                textMessage.setFromUserName(toUserName);
//                textMessage.setCreateTime(new Date().getTime());
//                textMessage.setMsgType(MessageUtil.TRANSFER_CUSTOMER_SERVICE);
//                textMessage.setFuncFlag(0);
                //推送给多客服系统
//                respMessage = " <xml><ToUserName><![CDATA[" + fromUserName + "]]></ToUserName><FromUserName><![CDATA[" + toUserName + "]]></FromUserName><CreateTime>" + new Date().getTime() + "</CreateTime><MsgType><![CDATA[transfer_customer_service]]></MsgType></xml>";
            }

//            if(respContent != null){
//                textMessage.setContent(respContent);
//                respMessage = MessageUtil.textMessageToXml(textMessage);
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return respMessage;
    }


    private void setArticles(NewsMessage newsMessage) {
        List<Article> articleList = new ArrayList<>();

        Article article1 = new Article();
        article1.setTitle("点击领取50元优惠券！");//关注有奖｜天上掉下来的钞票敢不敢要？
        article1.setPicUrl("http://other.youzhipai.cn/staticImg/pic.jpg");
        article1.setUrl("http://testweixin.youzhipai.cn/other/menu/coupon.html");

        Article article2 = new Article();
        article2.setTitle("犒劳自己，赠送他人，群发朋友，送礼玩出新花样！");
        article2.setPicUrl("http://other.youzhipai.cn/staticImg/LOGO-01.png");
        article2.setUrl("http://testweixin.youzhipai.cn/other/menu/about.html");

        Article article3 = new Article();
        article3.setTitle("派森百NFC橙汁，纯正天然 0添加 极品鲜榨澄汁 ");
        article3.setPicUrl("http://other.youzhipai.cn/staticImg/LOGO-02.png");
        article3.setUrl("http://testweixin.youzhipai.cn/youzhipai/product_html/13/152_product.html");

        Article article4 = new Article();
        article4.setTitle("正宗清远鸡 天然山林168天放养，口感美味、营养丰富");
        article4.setPicUrl("http://other.youzhipai.cn/staticImg/LOGO-03.png");
        article4.setUrl("http://testweixin.youzhipai.cn/youzhipai/product_html/13/155_product.html");

        Article article5 = new Article();
        article5.setTitle("正宗阳澄湖大闸蟹！螃蟹香飘千百里，疑是仙肴落人间");
        article5.setPicUrl("http://other.youzhipai.cn/staticImg/LOGO-04.png");
        article5.setUrl("http://testweixin.youzhipai.cn/youzhipai/product_html/13/158_product.html");

        articleList.add(article1);
        articleList.add(article2);
        articleList.add(article3);
        articleList.add(article4);
        articleList.add(article5);




    newsMessage.setArticleCount(articleList.size());
    newsMessage.setArticles(articleList);
}
}
