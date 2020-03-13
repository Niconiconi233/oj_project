package com.yw.ojproject.service;

import org.springframework.data.domain.Page;

import java.util.Map;

/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-03-13 00:09
**/
public interface BaseServer<T> {
    /**
     * 根据条件分页查询，条件拼接方式为 and
     * 条件拼接方式  xx字段_xx操作
     * @param params
     * @return
     */
    Page<T> findAllPageByParams(Map<String,String> params);

    /**
     * 求和数据
     * 条件同上（分页条件查询） 注意~！！！ 不需要分页属性，但是要添加统计的条件
     * 求和统计条件  xxx字段_sum = xx类型（支持BigDecimal 和 Long）
     *
     * @param params 参数
     * @return 查询条件
     */
    Map<String, Object> getSumByParams(Map<String, String> params) throws Exception;
}
