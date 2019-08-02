package cn.itcast.service;

import cn.itcast.entity.Result;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * @Author hua
 * @date 2019/07/26 10:07
 **/
public interface OrderService {
    Result order(Map map) throws Exception;

    Map<String,Object> getOrderSuccessInfoById(int id);
}
