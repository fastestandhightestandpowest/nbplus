package cn.itcast.service;

import java.util.ArrayList;

/**
 * @Author hua
 * @date 2019/07/30 17:38
 **/
public interface MemberService {
    ArrayList<Integer> getMemberCountBeforeRegDate(ArrayList<String> months);
}
