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

    //获取spu商品列表
    List<SpuInfo> getSpuInfoList(SpuInfo spuInfo);

    //获取spu baseSaleAttr
    List<BaseSaleAttr> getBaseSaleAttrList();

    //保存和编辑spuInfo（商品信息）
    void saveSpuInfo(SpuInfo spuInfo);

    // 根据前台传递的spuId查询spuImage列表
    List<SpuImage> getSpuImageList(String spuId);
    // 根据前台传递的spuId查询销售属性列表
    List<SpuSaleAttr> getSpuSaleAttrList(String spuId);
    // sku保存信息
    void saveSku(SkuInfo skuInfo);

    //商品详情部分的获取skuInfo
    SkuInfo getSkuInfo(String skuId);

    List<SpuSaleAttr> selectSpuSaleAttrListCheckBySku(SkuInfo skuInfo);

    List<SkuSaleAttrValue> getSkuSaleAttrValueListBySpu(String spuId);

}
