import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * @Author hua
 * @date 2019/07/29 11:16
 **/
public class TestSome {



    @Test
    public void test01(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String admin = bCryptPasswordEncoder.encode("123");
        System.out.println(admin);
        boolean b = bCryptPasswordEncoder.matches("admin", "$2a$10$FmEYCImGBPvWfDKEI3RbTeJRVGTcLNrqoLtKPWc6qn/x/fFB7Bq/u");
        System.out.println(b);
//        System.out.println(admin);
    }

    @Test
    public void test02(){
        for (int i = 2; i < 27; i++) {
            String str = " INSERT into t_role_permission values (3," +i +");";
            System.out.println(str);
        }

    }

    @Test
    public void test03(){
        Calendar calendar = Calendar.getInstance();
        //计算当前日期的前12个月
        calendar.add(Calendar.MONTH, -12);
        ArrayList<String> months = new ArrayList<>();
        ArrayList<Integer> memberCount = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH,1 );
            String month = format.format(calendar.getTime());
            months.add(month);
        }
        System.out.println(months);
    }

    @Test
    public void test04(){
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-1;
        if (dayOfWeek == 0){
            dayOfWeek = 7;
        }
        int firstDayOfWeek = calendar.getFirstDayOfWeek();
        System.out.println("这周的第"+dayOfWeek+"天");

        //算本周第一天
        calendar.add(Calendar.DAY_OF_WEEK,firstDayOfWeek-dayOfWeek);
        System.out.println(calendar.get(Calendar.DAY_OF_MONTH));
    }


    @Test
    public void test05(){
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.get(Calendar.MONTH));

    }
}
