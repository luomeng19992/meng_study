package com.breakout.meng.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.breakout.meng.mgb.mapper","com.breakout.meng.dao"})
public class MybatisConfig {
}
