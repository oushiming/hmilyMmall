package com.hmily.service;

import com.github.pagehelper.PageInfo;
import com.hmily.common.ServerResponse;
import com.hmily.pojo.Shipping;

import java.util.Map;

public interface IShippingService {
    ServerResponse<String> add(Integer userId, Shipping shipping);

    ServerResponse<String> del(Integer userId, Integer shippingId);

    ServerResponse<String> update(Integer userId, Shipping shipping);

    ServerResponse<Shipping> select(Integer userId, Integer shippingId);

    ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize);
}
