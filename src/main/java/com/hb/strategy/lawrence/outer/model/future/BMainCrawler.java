package com.hb.strategy.lawrence.outer.model.future;

import com.hb.strategy.lawrence.outer.AbstractSimpleCrawler;
import org.springframework.web.client.RestTemplate;

/**
 * 原油主力爬取
 */
public class BMainCrawler extends AbstractSimpleCrawler {

    private static final String url = "https://gu.sina.cn/ft/api/jsonp.php/var%20_S_5_" + System.currentTimeMillis() / 1000 + "=/GlobalService.getMink?symbol=S&type=5";

    public BMainCrawler(RestTemplate restTemplate) {
        super(url, restTemplate, "美豆", -3, -1,"23:00:00");
    }

}
