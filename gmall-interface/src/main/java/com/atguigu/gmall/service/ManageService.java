package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.*;

import java.util.List;

public interface ManageService {


    public List<BaseCatalog1> getCatalog1();

    public List<BaseCatalog2> getCatalog2(String catalog1);

    public List<BaseCatalog3> getCatalog3(String catalog2);

    public List<BaseAttrInfo> getAttrInfo(String catalog3);

    public void saveAttrInfo(BaseAttrInfo baseAttrInfo);



}
