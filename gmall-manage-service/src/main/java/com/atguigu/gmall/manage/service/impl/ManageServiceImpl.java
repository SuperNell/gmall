package com.atguigu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.*;
import com.atguigu.gmall.config.RedisUtil;
import com.atguigu.gmall.manage.constant.ManageConstant;
import com.atguigu.gmall.manage.mapper.*;
import com.atguigu.gmall.service.ManageService;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.List;

@Service
public class ManageServiceImpl implements ManageService {

    @Autowired
    private BaseCatalog1Mapper baseCatalog1Mapper;
    @Autowired
    private BaseCatalog2Mapper baseCatalog2Mapper;
    @Autowired
    private BaseCatalog3Mapper baseCatalog3Mapper;
    @Autowired
    private BaseAttrInfoMapper baseAttrInfoMapper;
    @Autowired
    private BaseAttrValueMapper baseAttrValueMapper;
    @Autowired
    private SpuInfoMapper spuInfoMapper;
    @Autowired
    private BaseSaleAttrMapper baseSaleAttrMapper;
    @Autowired
    private SpuImageMapper spuImageMapper;
    @Autowired
    private SpuSaleAttrMapper spuSaleAttrMapper;
    @Autowired
    private SpuSaleAttrValueMapper spuSaleAttrValueMapper;
    @Autowired
    private SkuInfoMapper skuInfoMapper;
    @Autowired
    private SkuImageMapper skuImageMapper;
    @Autowired
    private SkuSaleAttrValueMapper skuSaleAttrValueMapper;
    @Autowired
    private SkuAttrValueMapper skuAttrValueMapper;
    @Autowired
    private RedisUtil redisUtil;



    @Override
    public List<BaseCatalog1> getCatalog1() {
        return baseCatalog1Mapper.selectAll();
    }

    @Override
    public List<BaseCatalog2> getCatalog2(String catalog1) {
        BaseCatalog2 catalog2 = new BaseCatalog2();
        catalog2.setCatalog1Id(catalog1);
        return baseCatalog2Mapper.select(catalog2);
    }

    @Override
    public List<BaseCatalog3> getCatalog3(String catalog2) {
        BaseCatalog3 catalog3 = new BaseCatalog3();
        catalog3.setCatalog2Id(catalog2);
        return baseCatalog3Mapper.select(catalog3);
    }

    @Override
    public List<BaseAttrInfo> attrInfoList(String catalog3) {
       /* BaseAttrInfo baseAttrInfo = new BaseAttrInfo();
        baseAttrInfo.setCatalog3Id(catalog3);
        return baseAttrInfoMapper.select(baseAttrInfo);*/
       return baseAttrInfoMapper.getBaseAttrInfoListByCatalog3Id(Long.parseLong(catalog3));
    }

    @Override
    public void saveAttrInfo(BaseAttrInfo baseAttrInfo) {
        // 说明value_name的值没有拿到！
        // 保存数据：编辑数据放到一起来处理。
        // 是否有主键,操作都是指的是平台属性操作
        if (baseAttrInfo.getId() != null && baseAttrInfo.getId().length() > 0) {
            // 有主键 ，则修改
            baseAttrInfoMapper.updateByPrimaryKey(baseAttrInfo);
        } else {
            // 没有主键则需要添加,注意一下，当没有主键的时候，数据的id要设置成null，如果不设置有可能会出现空字符串
            if (baseAttrInfo.getId().length() == 0) {
                baseAttrInfo.setId(null);
            }
            // 开始插入数据
            baseAttrInfoMapper.insertSelective(baseAttrInfo);
        }

        // 操作属性值，先将属性值情况
        BaseAttrValue baseAttrValue = new BaseAttrValue();
        baseAttrValue.setAttrId(baseAttrInfo.getId());
        baseAttrValueMapper.delete(baseAttrValue);

        // 开始操作属性值列表
        if (baseAttrInfo.getAttrValueList() != null && baseAttrInfo.getAttrValueList().size() > 0) {
            // 循环数据 itar : a-->array iter : each
            for (BaseAttrValue attrValue : baseAttrInfo.getAttrValueList()) {
                // 做插入操作
                if (attrValue.getId().length() == 0) {
                    attrValue.setId(null);
                }
                attrValue.setAttrId(baseAttrInfo.getId());
                /*   baseAttrValueMapper.insertSelective(baseAttrValue);*/
                baseAttrValueMapper.insertSelective(attrValue);
            }
        }
    }


   /* @Override
    public void saveAttrInfo(BaseAttrInfo baseAttrInfo) {
        if (baseAttrInfo.getId() != null && baseAttrInfo.getId().length() > 0){
            baseAttrInfoMapper.updateByPrimaryKey(baseAttrInfo);
        }else {
            if (baseAttrInfo.getId().length() == 0){
                baseAttrInfo.setId(null);
            }
            baseAttrInfoMapper.insertSelective(baseAttrInfo);
        }

        // 操作属性值，先将属性值情况
        BaseAttrValue baseAttrValue = new BaseAttrValue();
        baseAttrValue.setAttrId(baseAttrInfo.getId());
        baseAttrValueMapper.delete(baseAttrValue);

        if (baseAttrInfo.getAttrValueList() != null && baseAttrInfo.getAttrValueList().size() > 0){
            for (BaseAttrValue attrValue : baseAttrInfo.getAttrValueList()) {
                if (attrValue.getId().length() == 0){
                    attrValue.setId(null);
                }
                attrValue.setAttrId(baseAttrInfo.getId());
                baseAttrValueMapper.insertSelective(attrValue);

            }
        }
    }*/

    @Override
    public BaseAttrInfo getAttrInfo(String attrId) {
        BaseAttrInfo baseAttrInfo = baseAttrInfoMapper.selectByPrimaryKey(attrId);
        BaseAttrValue baseAttrValue = new BaseAttrValue();
        baseAttrValue.setAttrId(baseAttrInfo.getId());
        List<BaseAttrValue> baseAttrValueList = baseAttrValueMapper.select(baseAttrValue);
        baseAttrInfo.setAttrValueList(baseAttrValueList);
        return baseAttrInfo;
    }


    //获取spu属性列表
    @Override
    public List<SpuInfo> getSpuInfoList(SpuInfo spuInfo) {
        List<SpuInfo> spuInfoList = spuInfoMapper.select(spuInfo);
        return spuInfoList;
    }

    @Override
    public List<BaseSaleAttr> getBaseSaleAttrList() {
        return baseSaleAttrMapper.selectAll();
    }

    @Override
    public void saveSpuInfo(SpuInfo spuInfo) {
        if (spuInfo.getId() != null && spuInfo.getId().length() > 0) {
            spuInfoMapper.updateByPrimaryKey(spuInfo);
        } else {
            if (spuInfo.getId()!=null && spuInfo.getId().length() == 0) {
                spuInfo.setId(null);
            }
            spuInfoMapper.insertSelective(spuInfo);
        }

        SpuImage spuImage = new SpuImage();
        spuImage.setSpuId(spuInfo.getId());
        spuImageMapper.delete(spuImage);

        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
        for (SpuImage image : spuImageList) {
            if (image.getId() != null && image.getId().length() == 0) {
                image.setId(null);
            }
            image.setSpuId(spuInfo.getId());
            spuImageMapper.insertSelective(image);
        }

        SpuSaleAttr spuSaleAttr = new SpuSaleAttr();
        spuSaleAttr.setSaleAttrId(spuInfo.getId());
        spuSaleAttrMapper.delete(spuSaleAttr);

        SpuSaleAttrValue spuSaleAttrValue = new SpuSaleAttrValue();
        spuSaleAttrValue.setSaleAttrId(spuInfo.getId());
        spuSaleAttrValueMapper.delete(spuSaleAttrValue);

        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        for (SpuSaleAttr saleAttr : spuSaleAttrList) {
            if (saleAttr.getId() != null && saleAttr.getId().length() == 0){
                saleAttr.setId(null);
            }
            saleAttr.setSpuId(spuInfo.getId());
            spuSaleAttrMapper.insertSelective(saleAttr);

            List<SpuSaleAttrValue> spuSaleAttrValueList = saleAttr.getSpuSaleAttrValueList();
            for (SpuSaleAttrValue saleAttrValue : spuSaleAttrValueList) {
                if (saleAttrValue.getId() != null && saleAttrValue.getId().length() == 0){
                    saleAttrValue.setId(null);
                }
                saleAttrValue.setSpuId(spuInfo.getId());
                spuSaleAttrValueMapper.insertSelective(saleAttrValue);
            }
        }
    }

    @Override
    public List<SpuImage> getSpuImageList(String spuId) {
        // 创建一个spuImage对象
        SpuImage spuImage = new SpuImage();
        spuImage.setSpuId(spuId);
        return spuImageMapper.select(spuImage);
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrList(String spuId) {
        // 销售属性，对应销售属性值，也是多表关联。
        List<SpuSaleAttr> spuSaleAttrList =  spuSaleAttrMapper.selectSpuSaleAttrList(Long.parseLong(spuId));
        return spuSaleAttrList;
    }

    @Override
    public void saveSku(SkuInfo skuInfo) {
        //添加SkuInfo数据
        if (skuInfo.getId()==null || skuInfo.getId().length()==0){
            skuInfo.setId(null);
            skuInfoMapper.insertSelective(skuInfo);
        } else {
            skuInfoMapper.updateByPrimaryKey(skuInfo);
        }

        // 先删除，再添加
        SkuAttrValue skuAttrValue = new SkuAttrValue();
        // SkuId = SkuInfo.id
        skuAttrValue.setSkuId(skuInfo.getId());
        skuAttrValueMapper.delete(skuAttrValue);

        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        for (SkuAttrValue attrValue : skuAttrValueList) {
            // 坑！
            attrValue.setSkuId(skuInfo.getId());
            if (attrValue.getId()!=null&& attrValue.getId().length()==0){
                attrValue.setId(null);
            }
            skuAttrValueMapper.insertSelective(attrValue);
        }
        // 属性值添加
        SkuSaleAttrValue skuSaleAttrValue = new SkuSaleAttrValue();
        skuSaleAttrValue.setSkuId(skuInfo.getId());
        skuSaleAttrValueMapper.delete(skuSaleAttrValue);

        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        for (SkuSaleAttrValue saleAttrValue : skuSaleAttrValueList) {
            saleAttrValue.setSkuId(skuInfo.getId());
            if (saleAttrValue.getId()!=null && saleAttrValue.getId().length()==0){
                saleAttrValue.setSkuId(null);
            }
            skuSaleAttrValueMapper.insertSelective(saleAttrValue);
        }
        // 图片添加
        SkuImage skuImage = new SkuImage();
        skuImage.setSkuId(skuInfo.getId());
        skuImageMapper.delete(skuImage);

        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        for (SkuImage image : skuImageList) {
            image.setSkuId(skuInfo.getId());
            if (image.getId()!=null && image.getId().length()==0){
                image.setId(null);
            }
            skuImageMapper.insertSelective(image);
        }

    }

    @Override
    public SkuInfo getSkuInfo(String skuId) {
        Jedis jedis = redisUtil.getJedis();
        SkuInfo skuInfo = null;
        String skuInfoKey = ManageConstant.SKUKEY_PREFIX + skuId + ManageConstant.SKUKEY_SUFFIX;
        if (jedis.exists(skuInfoKey)){
            String skuInfoJson = jedis.get(skuInfoKey);
            if (skuInfoJson != null || !"".equals(skuInfoJson)){
                skuInfo = JSON.parseObject(skuInfoJson,SkuInfo.class);
                return skuInfo;
            }
        }else {
            skuInfo = getSkuInfoDB(skuId);
            jedis.setex(skuInfoKey,ManageConstant.SKUKEY_TIMEOUT,JSON.toJSONString(skuInfo));
            return skuInfo;
        }
        return skuInfo;
    }

    private SkuInfo getSkuInfoDB(String skuId) {
        SkuInfo skuInfo = skuInfoMapper.selectByPrimaryKey(skuId);
        SkuImage skuImage = new SkuImage();
        skuImage.setSkuId(skuId);
        List<SkuImage> skuImageList = skuImageMapper.select(skuImage);
        skuInfo.setSkuImageList(skuImageList);
        //添加sku属性（es）
        SkuAttrValue skuAttrValue = new SkuAttrValue();
        skuAttrValue.setSkuId(skuId);
        List<SkuAttrValue> skuAttrValues = skuAttrValueMapper.select(skuAttrValue);
        skuInfo.setSkuAttrValueList(skuAttrValues);
        //添加sku属性值（es）
        SkuSaleAttrValue skuSaleAttrValue = new SkuSaleAttrValue();
        skuAttrValue.setSkuId(skuId);
        List<SkuSaleAttrValue> skuSaleAttrValues = skuSaleAttrValueMapper.select(skuSaleAttrValue);
        skuInfo.setSkuSaleAttrValueList(skuSaleAttrValues);
        return skuInfo;
    }

    @Override
    public List<SpuSaleAttr> selectSpuSaleAttrListCheckBySku(SkuInfo skuInfo) {
        List<SpuSaleAttr> spuSaleAttrList = spuSaleAttrMapper.
                selectSpuSaleAttrListCheckBySku(Long.parseLong(skuInfo.getId()), Long.parseLong(skuInfo.getSpuId()));
        return spuSaleAttrList;
    }

    @Override
    public List<SkuSaleAttrValue> getSkuSaleAttrValueListBySpu(String spuId) {
        return skuSaleAttrValueMapper.selectSkuSaleAttrValueListBySpu(spuId);
    }


}
