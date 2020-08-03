package com.hb.strategy.lawrence.outer.model.future;

import com.hb.strategy.lawrence.outer.AbstractSimpleCrawler;
import org.springframework.web.client.RestTemplate;

/**
 * 原油主力爬取
 */
public class AuMainCrawler extends AbstractSimpleCrawler {

    private static final String url = "https://gu.sina.cn/ft/api/jsonp.php/var%20_GC_5_" + System.currentTimeMillis() / 1000 + "=/GlobalService.getMink?symbol=GC&type=5";

    public AuMainCrawler(RestTemplate restTemplate) {
        super(url, restTemplate, "美黄金", -2, 0,"02:30:00");
    }

}
