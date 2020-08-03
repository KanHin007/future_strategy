package com.hb.strategy.lawrence.outer;

import com.alibaba.fastjson.JSONArray;
import com.hb.strategy.lawrence.model.SinaPhpResponseModel;
import com.hb.strategy.lawrence.outer.model.CrawlerResult;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public abstract class AbstractSimpleCrawler {

    private String futureName;

    private String requestUrl;

    private RestTemplate restTemplate;

    private boolean isMorning;

    //半夜收盘时间 化工类都是23点收盘
    private String nightNoonCloseTime = "23:00:00";

    private int weekendPreDayAmount;

    private int commonPreDayAmount;

    public AbstractSimpleCrawler(String requestUrl,
                                 RestTemplate restTemplate,
                                 String futureName, int weekendPreDayAmount,
                                 int commonPreDayAmount,String nightNoonCloseTime) {
        this.requestUrl = requestUrl;
        this.restTemplate = restTemplate;
        this.futureName = futureName;
        this.weekendPreDayAmount = weekendPreDayAmount;
        this.commonPreDayAmount = commonPreDayAmount;
        this.nightNoonCloseTime = nightNoonCloseTime;
    }

    public CrawlerResult crawler() {
        String responseStr = requestData();
        List<SinaPhpResponseModel> dataList = doWithResponse(responseStr);
        return parseCrawlerResult(dataList);

    }

    public List<SinaPhpResponseModel> doWithResponse(String responseStr) {
        if (responseStr == null) {
            return Collections.emptyList();
        }
        List<SinaPhpResponseModel> resultList = null;
        int leftQuoteIndex = responseStr.indexOf("(");
        int rightQuoteIndex = responseStr.indexOf(")");
        try {
            responseStr = responseStr.substring(leftQuoteIndex + 1, rightQuoteIndex);
            resultList = JSONArray.parseArray(responseStr, SinaPhpResponseModel.class);
        } catch (Exception e) {
            //  logger.error("substring response occur a error is {}", ExceptionUtils.getStackTrace(e));
            e.printStackTrace();
            return Collections.emptyList();
        }
        return resultList;
    }

    public String requestData() {
        return restTemplate.getForObject(requestUrl, String.class);
    }

    public CrawlerResult parseCrawlerResult(List<SinaPhpResponseModel> paramList) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat allSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 判断是不是晚上或早上
        Calendar c = Calendar.getInstance();
        Date when = getFifteenTime();
        // 定义要找的字符串
        String findString = "";
        // 说明是早上
        if (c.getTime().before(when)) {
            // 如果是周一
            if (c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                findString = simpleDateFormat.format(DateUtils.addDays(c.getTime(), weekendPreDayAmount)) + " " + nightNoonCloseTime;
            } else {
                findString = simpleDateFormat.format(DateUtils.addDays(c.getTime(), commonPreDayAmount)) + " " + nightNoonCloseTime;
            }
        } else {
            // 下午
            findString = simpleDateFormat.format(when) + " 15:00:00";
        }
        for (int i = paramList.size() - 1; i >= 0; i--) {
            SinaPhpResponseModel subPreValue = paramList.get(i);
            SinaPhpResponseModel subNowValue = paramList.get(paramList.size() - 1);
            if (allSimpleDateFormat.format(subPreValue.getDatetime()).equalsIgnoreCase(findString)) {
                return new CrawlerResult(futureName, subPreValue.getClose(), subNowValue.getClose());
            }
        }

        return null;
    }

    private Date getFifteenTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }


}
