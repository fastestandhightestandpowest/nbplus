package cn.itcast.service.impl;

import cn.itcast.dao.MemberDao;
import cn.itcast.pojo.Member;
import cn.itcast.service.MemberService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

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



    @Override
    public List<Map<String, Object>> findSexCount() {

        return memberDao.findSexCount();
    }

    @Override
    public List<Map> getAges() {
        //调用会员所有数据
        List<Member> agea = memberDao.getAgea();
        //将获取到的数据封装，并返回
        List<Map> list = new ArrayList<>();
        //年龄的数量
        int a = 0;//0-18
        int b = 0;//18-30
        int c = 0;//
        int d = 0;
        int e = 0;//没有填写生日的会员数量
        for (Member member : agea) {
            //会员的生日
            Date birthday = member.getBirthday();
            //毫秒值
            long birthday1 = 0;
            //当前毫秒值时间
            Long date1 = new Date().getTime();
            if (birthday==null){
                e++;
            }else {
                birthday1=birthday.getTime();
            }
            //时间搓，获取年龄多少岁
            double adc= date1 - birthday1;
            double memberages = adc / (1000.0 * 1 * 60 * 60 * 24 * 365);

            //判断年龄范围，计算年龄数
            if (memberages > 0 && memberages <= 18) {
                a++;
            }
            if (memberages > 18 && memberages <= 30) {
                b++;
            }
            if (memberages > 30 && memberages <= 45) {
                c++;
            }
            if (memberages > 45) {
                d++;
            }

        }

        Map map1 = new HashMap<String,Object>();
        map1.put("name","0-18");
        map1.put("value",a);

        Map map2 = new HashMap<String,Object>();
        map2.put("name","18-30");
        map2.put("value",b);

        Map map3 = new HashMap<String,Object>();
        map3.put("name","30-45");
        map3.put("value",c);

        Map map4 = new HashMap<String,Object>();
        map4.put("name","45以上");
        map4.put("value",d);

        Map map5 = new HashMap<String,Object>();
        map5.put("name","没有填写生日的");
        map5.put("value",e);

        list.add(map1);
        list.add(map2);
        list.add(map3);
        list.add(map4);
        list.add(map5);
        return list;
    }
}
