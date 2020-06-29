package com.hb.strategy.lawrence.util;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DingDingSender {

    public static Logger logger = LoggerFactory.getLogger(DingDingSender.class);

    public static final String DINGDING_ROBOT_URL = "https://oapi.dingtalk.com/robot/send?access_token=ed29afb248a1e68f3e5ade7179bb85c95844d5d0a7633eae3934afec7705defa";

    public static DingTalkClient client = null;

    static {
        client = new DefaultDingTalkClient(DINGDING_ROBOT_URL);
    }

    public static void sendMessage(String message) {

        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("text");
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        text.setContent("主人你好：" + message);
        request.setText(text);
        //OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        //at.setAtMobiles(Arrays.asList("132xxxxxxxx"));
        // isAtAll类型如果不为Boolean，请升级至最新SDK
        //at.setIsAtAll(true);
        // request.setAt(at);
        try {
            OapiRobotSendResponse response = client.execute(request);
        } catch (ApiException e) {
            e.printStackTrace();
            logger.error("dingding send Message occur a error : {}", ExceptionUtils.getStackTrace(e));
        }
    }


    public static void main(String[] args) {
        sendMessage("haha");
    }


}
