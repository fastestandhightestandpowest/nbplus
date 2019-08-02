package cn.itcast.service;

import cn.itcast.entity.Result;
import cn.itcast.pojo.Member;

/**
 * @Author hua
 * @date 2019/07/27 10:04
 **/
public interface LoginService {


    Member findMemberByphoneNumber(String telephone);

    void addMember(Member member);
}
