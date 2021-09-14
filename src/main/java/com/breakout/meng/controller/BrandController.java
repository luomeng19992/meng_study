package com.breakout.meng.controller;

import com.breakout.meng.common.api.CommonPage;
import com.breakout.meng.common.api.CommonResult;
import com.breakout.meng.mgb.model.PmsBrand;
import com.breakout.meng.service.BrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/brand")
@Api(value = "PmsBrandController",description = "品牌管理")
public class BrandController {

    @Autowired
    BrandService brandService;

    @ApiOperation(value = "分页查询品牌")
    @GetMapping("lsitBrand")
    public CommonResult<CommonPage<PmsBrand>> listBrand(@ApiParam(value = "第几页") @RequestParam(value = "pageNum",defaultValue = "0") Integer pageNum,
                                                        @ApiParam(value = "每页条数") @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        List<PmsBrand> pmsBrands = brandService.listBrand(pageNum, pageSize);
        CommonPage<PmsBrand> pmsBrandCommonPage = CommonPage.restPage(pmsBrands);
        return CommonResult.success(pmsBrandCommonPage);
    }
}
