package com.hb.strategy.lawrence.scheduler;

import com.alibaba.fastjson.JSONArray;
import com.hb.strategy.lawrence.config.SinaConfig;
import com.hb.strategy.lawrence.model.SinaPhpResponseModel;
import com.hb.strategy.lawrence.util.DingDingSender;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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
     * 执行定时任务的方法
     * 5s执行一次
     */
    @Scheduled(cron = CRON)
    public void schedule() {

        List<String> types = sinaConfig.getFutureTypes();
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

        //      System.out.println("当前的品种链接为 ： " + url);
        // 获取结果
        String resultStr = restTemplate.getForObject(url, String.class);
        if (resultStr != null) {
            List<SinaPhpResponseModel> models = parseSinaPhpResponse(resultStr);
            int modelSize = models.size();
            if (modelSize >= 3) {
                Date currentTime = new Date();
                // 取出前两个5分钟model进行比较
                SinaPhpResponseModel preModel = models.get(modelSize - 3);
                SinaPhpResponseModel middleModel = models.get(modelSize - 2);
                BigDecimal preDValue = preModel.getClose().subtract(preModel.getOpen());
                BigDecimal middleDValue = middleModel.getClose().subtract(middleModel.getOpen());
                //  logger.info("品种{},,,,preModel is {}", futureType, preModel);
                //  logger.info("品种{},,,,middle is {}", futureType, middleModel);
                // 进行检查
                // 阳包阴吞没
                if (preDValue.compareTo(BigDecimal.ZERO) > 0
                        && middleDValue.compareTo(BigDecimal.ZERO) < 0
                        && preModel.getClose().compareTo(middleModel.getOpen()) <= 0
                        && preModel.getOpen().compareTo(middleModel.getClose()) >= 0) {
                    // 钉钉通知
                    logger.info("时间{},,,当前品种：{}   出现了阳包阴吞没", currentTime, futureType);
                    DingDingSender.sendMessage("当前时间 " + currentTime + " | 当前品种 " + futureType + " | 出现了后阴包前阳吞没");
                }
                // 阴包阳吞没
                if (preDValue.compareTo(BigDecimal.ZERO) < 0
                        && middleDValue.compareTo(BigDecimal.ZERO) > 0
                        && preModel.getClose().compareTo(middleModel.getOpen()) >= 0
                        && preModel.getOpen().compareTo(middleModel.getClose()) <= 0) {
                    // 钉钉通知
                    logger.info("时间{},,,当前品种：{}   出现了阴包阳吞没", currentTime, futureType);
                    DingDingSender.sendMessage("当前时间 " + currentTime + " | 当前品种 " + futureType + " | 出现了后阳包前阴吞没");
                }

            }
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
