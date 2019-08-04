package cn.itcast.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author hua
 * @date 2019/07/30 17:38
 **/
public interface MemberService {
    ArrayList<Integer> getMemberCountBeforeRegDate(ArrayList<String> months);

    List<Map<String,Object>> findSexCount();

    List<Map> getAges();
}
