package com.breakout.meng.service;

import com.breakout.meng.mgb.model.PmsBrand;

import java.util.List;

public interface BrandService {
    /**
     * 查询品牌列表
     * @return
     * @param pageNum
     * @param pageSize
     */
    List<PmsBrand> listBrand(Integer pageNum, Integer pageSize);
}
