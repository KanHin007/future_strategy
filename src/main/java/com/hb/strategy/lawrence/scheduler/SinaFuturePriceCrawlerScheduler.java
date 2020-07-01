package com.hb.strategy.lawrence.scheduler;

import com.alibaba.fastjson.JSONArray;
import com.hb.strategy.lawrence.config.SinaConfig;
import com.hb.strategy.lawrence.model.SinaPhpResponseModel;
import com.hb.strategy.lawrence.strategy.Strategy;
import com.hb.strategy.lawrence.util.DingDingSender;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * 新浪数据爬取
 */
@Component
public class SinaFuturePriceCrawlerScheduler {

    public static Logger logger = LoggerFactory.getLogger(SinaFuturePriceCrawlerScheduler.class);

    public static final String CRON = "50 5,10,15,20,25,30,35,40,45,50,55,0 * * * *";

    @Autowired
    SinaConfig sinaConfig;

    @Autowired
    RestTemplate restTemplate;

    /**
     * 暂时使用回调策略
     */
    @Resource(name = "callBackStrategy")
    Strategy strategy;

    /**
     * 执行定时任务的方法
     * 5s执行一次
     */
    @Scheduled(cron = CRON)
    public void schedule() {

        List<String> types = sinaConfig.getFutureTypes();
        System.out.println(types);
        types.parallelStream().forEach(v -> {
            checkPriceWithFiveMinutes(sinaConfig.getInnerUrlPrefix(), v);
        });

        //   checkPriceWithFiveMinutes(sinaConfig.getOuterUrlPrefix(), "CL");

    }

    /**
     * @param futureType 期货的品种
     */
    private void checkPriceWithFiveMinutes(String urlPrefix, String futureType) {
        String timestamp = String.valueOf((long) System.currentTimeMillis() / 1000);
        // 替换成新的urls
        String url = urlPrefix
                .replaceAll("@TIMESTAMP", timestamp)
                .replaceAll("@TYPE", futureType);

        //      logger.info("当前的品种链接为 ： {}" , url);
        // 获取结果
        String resultStr = restTemplate.getForObject(url, String.class);
        if (resultStr != null) {
            List<SinaPhpResponseModel> models = parseSinaPhpResponse(resultStr);
            // 策略检查并提醒
            strategy.checkAndRemind(futureType, models);
        }

    }

    /**
     * 将response转化为实体列表
     *
     * @param response
     * @return
     */
    public static List<SinaPhpResponseModel> parseSinaPhpResponse(String response) {
        if (response == null) {
            return Collections.emptyList();
        }
        int leftQuoteIndex = response.indexOf("(");
        int rightQuoteIndex = response.indexOf(")");
        List<SinaPhpResponseModel> resultArr = null;
        try {
            response = response.substring(leftQuoteIndex + 1, rightQuoteIndex);
            resultArr = JSONArray.parseArray(response, SinaPhpResponseModel.class);
        } catch (Exception e) {
            logger.error("substring response occur a error is {}", ExceptionUtils.getStackTrace(e));
            return Collections.emptyList();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("当前格式化出来的respoonse为：" + response);
        }
        return resultArr;
    }


    public SinaConfig getSinaConfig() {
        return sinaConfig;
    }

    public void setSinaConfig(SinaConfig sinaConfig) {
        this.sinaConfig = sinaConfig;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


}
