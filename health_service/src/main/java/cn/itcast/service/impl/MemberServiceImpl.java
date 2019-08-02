package cn.itcast.service.impl;

import cn.itcast.dao.MemberDao;
import cn.itcast.service.MemberService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
 * @Author hua
 * @date 2019/07/30 17:39
 **/
@Service(interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;

    @Override
    public ArrayList<Integer> getMemberCountBeforeRegDate(ArrayList<String> months) {
        //遍历每个月,查询每个月之前所注册的会员
        ArrayList<Integer> list = new ArrayList<>();
        for (String month : months) {
            int count = memberDao.queryMemberCountBeforeRegDate(month+"-32");
            list.add(count);
        }
        return list;
    }
}
