package cn.itcast.service.impl;

import cn.itcast.dao.MemberDao;
import cn.itcast.entity.Result;
import cn.itcast.pojo.Member;
import cn.itcast.service.LoginService;
import cn.itcast.utils.MD5Utils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author hua
 * @date 2019/07/27 10:05
 **/
@Service(interfaceClass = LoginService.class)
public class LoginServiceImpl implements LoginService {

    @Autowired
    private MemberDao memberDao;
    /**
     * 根据phoneNumber查找用户
     * @param telephone
     * @return
     */
    @Override
    public Member findMemberByphoneNumber(String telephone) {
        return memberDao.findByphoneNumber(telephone);
    }

    @Override
    @Transactional
    public void addMember(Member member) {
        //注册的时候密码需要使用MD5加密
        if (member.getPassword() != null){
            member.setPassword(MD5Utils.md5(member.getPassword()));
        }
        memberDao.add(member);
    }
}
