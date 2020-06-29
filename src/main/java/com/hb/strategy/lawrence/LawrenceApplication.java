package com.hb.strategy.lawrence;

import com.hb.strategy.lawrence.config.SinaConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;

@EnableScheduling
@SpringBootApplication
public class LawrenceApplication {

    public static Logger logger = LoggerFactory.getLogger(LawrenceApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(LawrenceApplication.class, args);
        SinaConfig sinaConfig = applicationContext.getBean(SinaConfig.class);
        logger.info("当前加载出来的品种有：" + Arrays.toString(sinaConfig.getFutureTypes().toArray()));
    }

}
