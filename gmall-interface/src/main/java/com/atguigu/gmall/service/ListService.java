package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.SkuLsInfo;
import com.atguigu.gmall.bean.SkuLsParams;
import com.atguigu.gmall.bean.SkuLsResult;

public interface ListService {

    //保存skuInfo到es中
    void saveSkuLsInfo(SkuLsInfo skuLsInfo);

    //查询es结果返回
    SkuLsResult search(SkuLsParams skuLsParams);
}
