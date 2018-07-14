package com.atguigu.gmall.list.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.BaseAttrInfo;
import com.atguigu.gmall.bean.BaseAttrValue;
import com.atguigu.gmall.bean.SkuLsParams;
import com.atguigu.gmall.bean.SkuLsResult;
import com.atguigu.gmall.service.ListService;
import com.atguigu.gmall.service.ManageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.util.resources.cldr.ti.CalendarData_ti_ER;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
public class ListController {

    @Reference
    private ListService listService;

    @Reference
    private ManageService manageService;


    @RequestMapping(value = "list.html", method = RequestMethod.GET)
    public String list(SkuLsParams skuLsParams, Model model) {
        SkuLsResult search = listService.search(skuLsParams);
        System.out.println(JSON.toJSONString(search));
        model.addAttribute("skuLsInfoList", search.getSkuLsInfoList());
        List<String> attrValueIdList = search.getAttrValueIdList();
        List<BaseAttrInfo> baseAttrInfoList = manageService.getAttrList(attrValueIdList);
        model.addAttribute("attrList", baseAttrInfoList);
        ArrayList<BaseAttrValue> baseAttrValueArrayList = new ArrayList<>();
        //  做个url拼接 ,参数skuLsParams.
        String makeUrl = makeUrlParam(skuLsParams);
        for (Iterator<BaseAttrInfo> iterator = baseAttrInfoList.iterator(); iterator.hasNext(); ) {
            BaseAttrInfo baseAttrInfo = iterator.next();
            List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
            for (BaseAttrValue baseAttrValue : attrValueList) {
                if (baseAttrValue.getId() != null && baseAttrValue.getId().length() > 0) {
                    if (skuLsParams.getValueId() != null && skuLsParams.getValueId().length > 0) {
                        for (String valueId : skuLsParams.getValueId()) {
                            if (valueId.equals(baseAttrValue.getId())){
                                iterator.remove();
                                BaseAttrValue baseAttrValueSelected = new BaseAttrValue();
                                baseAttrValueSelected.setValueName(baseAttrInfo.getAttrName() + ":" + baseAttrValue.getValueName() );
                                String urlParam = makeUrlParam(skuLsParams, valueId);
                                baseAttrValueSelected.setUrlParam(urlParam);
                                baseAttrValueArrayList.add(baseAttrValueSelected);
                            }
                        }
                    }
                }
            }
        }

        int totalPages = (int) ((search.getTotal() + skuLsParams.getPageSize() - 1)/skuLsParams.getPageSize());
        model.addAttribute("totalPages",totalPages);
        model.addAttribute("pageNo",skuLsParams.getPageNo());
        model.addAttribute("baseAttrValueArrayList",baseAttrValueArrayList);
        model.addAttribute("urlParam",makeUrl);
        model.addAttribute("keyword",skuLsParams.getKeyword());
        return "list";
    }


    // 拼接方法 ，判断页面传递过来的参数，在makeUrl中是否存在
    private String makeUrlParam(SkuLsParams skuLsParams, String... excludeValueIds) {
        String makeUrl = "";
        if (skuLsParams.getKeyword() != null && skuLsParams.getKeyword().length() > 0) {
            makeUrl += "keyword=" + skuLsParams.getKeyword();
        }
        if (skuLsParams.getCatalog3Id() != null && skuLsParams.getCatalog3Id().length() > 0) {
            if (makeUrl.length() > 0) {
                makeUrl += "&";
            }
            makeUrl += "catalog3Id=" + skuLsParams.getCatalog3Id();
        }
        if (skuLsParams.getValueId() != null && skuLsParams.getValueId().length > 0) {
            for (int i = 0; i < skuLsParams.getValueId().length; i++) {
                String valueId = skuLsParams.getValueId()[i];
                if (excludeValueIds != null && excludeValueIds.length > 0) {
                    String excludeValueId = excludeValueIds[0];
                    if (excludeValueId.equals(valueId)) {
                        continue;
                    }
                }
                if (makeUrl.length() > 0) {
                    makeUrl += "&";
                }
                makeUrl += "valueId=" + valueId;
            }
        }
        return makeUrl;
    }

}
