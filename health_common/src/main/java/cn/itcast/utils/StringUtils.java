package cn.itcast.utils;

/**
 * @Author hua
 * @date 2019/07/19 12:23
 **/
public class StringUtils {

    public static Boolean isEmity(String str){
        if ("".equals(str) || str == null || "null".equals(str)){
            return true;
        }else{
            return false;
        }
    }
}
