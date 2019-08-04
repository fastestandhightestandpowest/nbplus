import cn.itcast.utils.DateUtils;
import org.junit.Test;

import java.util.Date;

/**
 * @Author LiuJunShi
 * @create 2019/8/3 15:14
 */
public class DateTest {

    @Test
    public void test() throws Exception {
        Date firstDay4ThisMonth = DateUtils.getFirstDay4ThisMonth();
        String s = DateUtils.parseDate2String(firstDay4ThisMonth);
        System.out.println(s);
    }
    @Test
    public void dateTest() throws Exception {
        Date firstDayOfWeek = DateUtils.getThisWeekMonday(new Date());
        String s = DateUtils.parseDate2String(firstDayOfWeek);
        System.out.println(s);
    }
}
