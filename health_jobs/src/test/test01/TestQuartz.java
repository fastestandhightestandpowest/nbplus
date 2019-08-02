import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author hua
 * @date 2019/07/22 9:51
 **/
public class TestQuartz {

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("applicationContext-jobs.xml");
    }
}
