package com.breakout.meng.config;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

//@Configuration
public class EsConfig {
    @PostConstruct
    void init() {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }
}
