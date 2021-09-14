package com.breakout.meng.service.impl;

import com.breakout.meng.mgb.mapper.PmsBrandMapper;
import com.breakout.meng.mgb.model.PmsBrand;
import com.breakout.meng.mgb.model.PmsBrandExample;
import com.breakout.meng.service.BrandService;
import com.github.pagehelper.PageHelper;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService, BeanPostProcessor {
    @Autowired
    PmsBrandMapper pmsBrandMapper;
    @Override
    public List<PmsBrand> listBrand(Integer pageNum, Integer pageSize) {
        PmsBrandExample pmsBrandExample = new PmsBrandExample();
        PageHelper.startPage(pageNum,pageSize);
        return pmsBrandMapper.selectByExample(pmsBrandExample);
    }
}
