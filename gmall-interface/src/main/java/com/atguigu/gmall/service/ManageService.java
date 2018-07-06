package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.*;

import java.util.List;

public interface ManageService {


    List<BaseCatalog1> getCatalog1();

    List<BaseCatalog2> getCatalog2(String catalog1);

    List<BaseCatalog3> getCatalog3(String catalog2);

    List<BaseAttrInfo> attrInfoList(String catalog3);

    void saveAttrInfo(BaseAttrInfo baseAttrInfo);

    //获取属性值数据
    BaseAttrInfo getAttrInfo(String attrId);

    //或者spu商品列表
    List<SpuInfo> getSpuInfoList(SpuInfo spuInfo);
}
