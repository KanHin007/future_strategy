package com.hb.strategy.lawrence.outer.model.future;

import com.hb.strategy.lawrence.outer.AbstractSimpleCrawler;
import org.springframework.web.client.RestTemplate;

/**
 * 原油主力爬取
 */
public class ZnMainCrawler extends AbstractSimpleCrawler {

    private static final String url = "https://gu.sina.cn/ft/api/jsonp.php/var%20_ZSD_5_" + System.currentTimeMillis() / 1000 + "=/GlobalService.getMink?symbol=ZSD&type=5";

    public ZnMainCrawler(RestTemplate restTemplate) {
        super(url, restTemplate, "伦锌", -2, 0,"01:00:00");
    }

}
