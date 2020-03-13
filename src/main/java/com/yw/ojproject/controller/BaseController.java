package com.yw.ojproject.controller;

import com.yw.ojproject.service.BaseServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-03-13 13:26
**/
@Slf4j
public class BaseController<T> {
    /**
     * 需要在子类中具体注入
     */
    protected BaseServer baseService;

    /**
     * 根据条件分页查询，条件拼接方式为 and
     * 条件拼接方式  xx字段_xx操作
     * @param parameters
     * @return
     */
    @GetMapping("/findAllPageByParams")
    public Page<T> findAllPageByParams(@RequestParam Map<String, String> parameters) {
        return baseService.findAllPageByParams(parameters);
    }

    /**
     * 求和数据
     * 条件同上（分页条件查询） 注意~！！！ 不需要分页属性，但是要添加统计的条件
     * 求和统计条件  xxx字段_sum = xx类型（支持BigDecimal 和 Long）
     *
     * @param params 参数
     * @return 查询条件
     */
    @GetMapping("/getSumByParams")
    public Map<String, Object> getSumByParams(@RequestParam Map<String, String> params) throws Exception {
        return baseService.getSumByParams(params);
    }
}
