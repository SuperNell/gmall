package com.atguigu.gmall.item.controller;


import ch.qos.logback.core.net.SyslogOutputStream;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.SkuInfo;
import com.atguigu.gmall.bean.SkuSaleAttrValue;
import com.atguigu.gmall.bean.SpuSaleAttr;
import com.atguigu.gmall.config.LoginRequire;
import com.atguigu.gmall.service.ListService;
import com.atguigu.gmall.service.ManageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ItemController {

    @Reference
    private ManageService manageService;

    @Reference
    private ListService listService;

    /*@RequestMapping("{skuId}.html")
    public String skuInfoPage(@PathVariable("skuId")String skuId,Map map){
        SkuInfo skuInfo = manageService.getSkuInfo(skuId);
        map.put("skuInfo",skuInfo);
        List<SpuSaleAttr> spuSaleAttr =  manageService.selectSpuSaleAttrListCheckBySku(skuInfo);
        map.put("spuSaleAttr",spuSaleAttr);
        List<SkuSaleAttrValue> skuSaleAttrValueListBySpu = manageService.getSkuSaleAttrValueListBySpu(skuInfo.getSpuId());
        // "1|20" : "17"  "2|10" : "17" valueIdsKey="1|20"  map.put(valueIdsKey,skuId);
        // 先声明一个字符串
        String valueIdsKey = "";
        // 需要定一个map集合
        HashMap<String, String> map1 = new HashMap<>();
        // 循环拼接
        for (int i = 0; i <skuSaleAttrValueListBySpu.size() ; i++) {
            // 取得第一个值
            SkuSaleAttrValue skuSaleAttrValue = skuSaleAttrValueListBySpu.get(i);
            // 什么时候加|
            if (valueIdsKey.length()>0){
                valueIdsKey+="|";
            }
            valueIdsKey+=skuSaleAttrValue.getSaleAttrValueId();

            // 什么时候停止拼接
            if ((i+1)==skuSaleAttrValueListBySpu.size()|| !skuSaleAttrValue.getSkuId().equals(skuSaleAttrValueListBySpu.get(i+1).getSkuId())){
                map1.put(valueIdsKey,skuSaleAttrValue.getSkuId());
                valueIdsKey="";
            }
        }
        // 将map 转换成json字符串
        String valueJson = JSON.toJSONString(map1);
        System.out.println("valueJson:="+valueJson);
        // 放到前台使用！
        map.put("valuesSkuJson",valueJson);
        return "item";
    }*/

    @LoginRequire
    @RequestMapping("/{skuId}.html")
    public String skuInfo(@PathVariable(value = "skuId") String skuId, Model model){


        /*根据skuId查询商品信息，以及商品对应的skuImg信息*/
        SkuInfo skuInfo =  manageService.getSkuInfo(skuId);
        /*保存对象，在页面中显示*/
        model.addAttribute("skuInfo",skuInfo);

        // 显示销售属性，销售属性值
        List<SpuSaleAttr> saleAttrList = manageService.selectSpuSaleAttrListCheckBySku(skuInfo);
        model.addAttribute("spuSaleAttr",saleAttrList);
        // 组装后台传递到前台的json字符串
        List<SkuSaleAttrValue> skuSaleAttrValueListBySpu = manageService.getSkuSaleAttrValueListBySpu(skuInfo.getSpuId());
        // "1|20" : "17"  "2|10" : "17" valueIdsKey="1|20"  map.put(valueIdsKey,skuId);
        // 先声明一个字符串
        String valueIdsKey = "";
        // 需要定一个map集合
        HashMap<String, String> map = new HashMap<>();
        // 循环拼接
        for (int i = 0; i <skuSaleAttrValueListBySpu.size() ; i++) {
            // 取得第一个值
            SkuSaleAttrValue skuSaleAttrValue = skuSaleAttrValueListBySpu.get(i);
            // 什么时候加|
            if (valueIdsKey.length()>0){
                valueIdsKey+="|";
            }
            valueIdsKey+=skuSaleAttrValue.getSaleAttrValueId();

            // 什么时候停止拼接
            if ((i+1)==skuSaleAttrValueListBySpu.size()|| !skuSaleAttrValue.getSkuId().equals(skuSaleAttrValueListBySpu.get(i+1).getSkuId())){
                map.put(valueIdsKey,skuSaleAttrValue.getSkuId());
                valueIdsKey="";
            }
        }
        // 将map 转换成json字符串
        String valueJson = JSON.toJSONString(map);
        System.out.println("valueJson:="+valueJson);
        // 放到前台使用！
        model.addAttribute("valuesSkuJson",valueJson);
        listService.incrHotScore(skuId);
        return "item";
    }
}
