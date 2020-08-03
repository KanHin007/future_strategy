package com.hb.strategy.lawrence.outer.model.future;

import com.hb.strategy.lawrence.outer.AbstractSimpleCrawler;
import org.springframework.web.client.RestTemplate;

/**
 * 原油主力爬取
 */
public class AgMainCrawler extends AbstractSimpleCrawler {

    private static final String url = "https://gu.sina.cn/ft/api/jsonp.php/var%20_SI_5_" + System.currentTimeMillis() / 1000 + "=/GlobalService.getMink?symbol=SI&type=5";

    public AgMainCrawler(RestTemplate restTemplate) {
        super(url, restTemplate, "美白银", -2, 0,"02:30:00");
    }

}
