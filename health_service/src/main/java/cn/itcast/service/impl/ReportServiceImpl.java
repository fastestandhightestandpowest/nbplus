package cn.itcast.service.impl;

import cn.itcast.dao.MemberDao;
import cn.itcast.dao.OrderDao;
import cn.itcast.dao.PackageDao;
import cn.itcast.service.ReportService;
import cn.itcast.utils.DateUtils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author hua
 * @date 2019/07/31 11:17
 **/
@Service(interfaceClass = ReportService.class)
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private PackageDao packageDao;

    /**
     * 获得运营统计数据
     * Map数据格式：
     *      reportDate -> string
     *      todayNewMember -> number
     *      totalMember -> number
     *      thisWeekNewMember -> number
     *      thisMonthNewMember -> number
     *      todayOrderNumber -> number
     *      todayVisitsNumber -> number
     *      thisWeekOrderNumber -> number
     *      thisWeekVisitsNumber -> number
     *      thisMonthOrderNumber -> number
     *      thisMonthVisitsNumber -> number
     *      hotPackage -> List<Package>
     */
    @Override
    public Map<String, Object> generateReport() {
        Calendar calendar = Calendar.getInstance();
        Date mondayOfThisWeek = DateUtils.getThisWeekMonday();
        Date sundayOfThisWeek = DateUtils.getSundayOfThisWeek();
        Date firstDay4ThisMonth = DateUtils.getFirstDay4ThisMonth();
        Date lastDay4ThisMonth = DateUtils.getLastDay4ThisMonth();
        //reportDate
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String reportDate = dateFormat.format(new Date());
        //----------------会员数据统计--------------------------
        //todayNewMember
        Integer todayNewMember = memberDao.findTodayNewMember(reportDate);
        //totalMember
        Integer totalMember = memberDao.findAllMember();
        //thisWeekNewMember
        Integer thisWeekNewMember = memberDao.findBetweenRegDateNums(mondayOfThisWeek,sundayOfThisWeek);
        //thisMonthNewMember
        Integer thisMonthNewMember = memberDao.findBetweenRegDateNums(firstDay4ThisMonth, lastDay4ThisMonth);
        //-------------------预约到诊数量统计-------------------------
        //todayOrderNumber
        Integer todayOrderNumber = orderDao.findOrderCountByDate(new Date());
        //todayVisitsNumber
        Integer todayVisitsNumber = orderDao.findVisitedCountByDate(new Date());
        //thisWeekOrderNumber
        Integer thisWeekOrderNumber = orderDao.findBetweenOrderDateNums(mondayOfThisWeek,sundayOfThisWeek);
        //thisWeekVisitsNumber
        Integer thisWeekVisitsNumber = orderDao.findBeweenVisitedNums(mondayOfThisWeek,sundayOfThisWeek);
        //thisMonthOrderNumber
        Integer thisMonthOrderNumber = orderDao.findBetweenOrderDateNums(firstDay4ThisMonth, lastDay4ThisMonth);
        //thisMonthVisitsNumber
        Integer thisMonthVisitsNumber = orderDao.findBeweenVisitedNums(firstDay4ThisMonth, lastDay4ThisMonth);
        //--------------------------热门套餐  取前四名--------------------------
        List<Map<String,Object>> hotPackage = packageDao.getHotPackage();
        //--------------------------放入map中------------------------------
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("reportDate",reportDate);
        hashMap.put("todayNewMember",todayNewMember);
        hashMap.put("totalMember",totalMember);
        hashMap.put("thisWeekNewMember",thisWeekNewMember);
        hashMap.put("thisMonthNewMember",thisMonthNewMember);
        hashMap.put("todayOrderNumber",todayOrderNumber);
        hashMap.put("todayVisitsNumber",todayVisitsNumber);
        hashMap.put("thisWeekOrderNumber",thisWeekOrderNumber);
        hashMap.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        hashMap.put("thisMonthOrderNumber",thisMonthOrderNumber);
        hashMap.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        hashMap.put("hotPackage",hotPackage);
        return hashMap;
    }
}
