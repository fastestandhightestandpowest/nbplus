package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.entity.Result;
import cn.itcast.service.MemberService;
import cn.itcast.service.PackageService;
import cn.itcast.service.ReportService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author hua
 * @date 2019/07/30 17:19
 **/
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;
    @Reference
    private PackageService packageService;
    @Reference
    private ReportService reportService;

    /**
     *指定日期之间的会员数量
     * @return
     */
    @PostMapping("/getMemberReport")
    public Map<String, Object> getMemberReport(@RequestBody Map<String,Object> dateMap) {
        /*Calendar calendar = Calendar.getInstance();
        //计算当前日期的前12个月
        calendar.add(Calendar.MONTH, -12);*/
        ArrayList<String> months = new ArrayList<>();
        ArrayList<Integer> memberCount = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        /*for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH, 1);
            String month = format.format(calendar.getTime());
            months.add(month);
        }*/
        String startTime = (String) dateMap.get("startTime");
        String endTime = (String) dateMap.get("endTime");
        String[] starts = startTime.split("-");
        String[] ends = endTime.split("-");
        //开始时间
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Integer.parseInt(starts[0]),Integer.parseInt(starts[1])-1 ,Integer.parseInt(starts[2]));
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Integer.parseInt(ends[0]), Integer.parseInt(ends[1]), Integer.parseInt(ends[2]));
        while (true){
            if (startCalendar.compareTo(endCalendar)>0){
                break;
            }
            months.add(format.format(startCalendar.getTime()));
            startCalendar.add(Calendar.MONTH, 1);
        }
        memberCount = memberService.getMemberCountBeforeRegDate(months);
        Map<String, Object> map = new HashMap<>();
        map.put("months", months);
        map.put("memberCount", memberCount);
        return map;
    }

    /**
     * 每个套餐的预约数量表
     */
    @GetMapping("/getPackageReport")
    public Result getPackageReport() {
        try {
            List<Map<String, Integer>> list = packageService.findEveryPackageOrderNum();
            HashMap<String, Object> hashMap = new HashMap<>();
            ArrayList<Object> packageNames = new ArrayList<>();
//            ArrayList<Object> packageCount = new ArrayList<>();
            list.forEach(res -> {
                packageNames.add(res.get("name"));
//                packageCount.add(res.get("value"));
            });
            hashMap.put("packageNames", packageNames);
//            hashMap.put("packageCount", packageCount);
            hashMap.put("list", list);
            return new Result(true, null, hashMap);
        } catch (Exception e) {
            return new Result(false, "查询失败");
        }
    }

    /**
     * 查询报表信息,如下
     * reportDate
     * todayNewMember
     * totalMember
     * thisWeekNewMember
     * thisMonthNewMember
     * todayOrderNumber
     * todayVisitsNumber
     * thisWeekOrderNumber
     * thisWeekVisitsNumber
     * thisMonthOrderNumber
     * thisMonthVisitsNumber
     * hotPackage 热门套餐
     */
    @GetMapping("/getBusinessReportData")
    public Result getBusinessReportData() {
        try {
            Map<String, Object> map = reportService.generateReport();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, map);
        } catch (Exception e) {
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    /**
     * 写入数据进报表,并将报表下载
     */
    @RequestMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        //得到项目中的模板的地址
        String path = request.getSession().getServletContext().getRealPath("template/report_template.xlsx");

        //设置响应头信息
        String fileName = "运营数据报表.xlsx";
        try {
            //进行转码,不然就会中文文件名就会乱码
            fileName = new String(fileName.getBytes(),"ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.addHeader("Content-Disposition","attachment;filename=" + fileName);
        // 设置http的内容体的格式
        response.setContentType("application/vnd.ms-excel");

        try (
                XSSFWorkbook workbook = new XSSFWorkbook(path);
                OutputStream out = response.getOutputStream();
        ) {
            //拿到数据集合
            Map<String, Object> map = reportService.generateReport();
            XSSFSheet sheet = workbook.getSheetAt(0);
            //日期
            sheet.getRow(2).getCell(5).setCellValue((String)map.get("reportDate"));
            //-------------------会员数量统计--------------------
            sheet.getRow(4).getCell(5).setCellValue(map.get("todayNewMember").toString());
            sheet.getRow(4).getCell(7).setCellValue(map.get("totalMember").toString());
            sheet.getRow(5).getCell(5).setCellValue(map.get("thisWeekNewMember").toString());
            sheet.getRow(5).getCell(7).setCellValue(map.get("thisMonthNewMember").toString());
            //----------------------预约到诊数量统计------------------
            sheet.getRow(7).getCell(5).setCellValue(map.get("todayOrderNumber").toString());
            sheet.getRow(7).getCell(7).setCellValue(map.get("todayVisitsNumber").toString());
            sheet.getRow(8).getCell(5).setCellValue(map.get("thisWeekOrderNumber").toString());
            sheet.getRow(8).getCell(7).setCellValue(map.get("thisWeekVisitsNumber").toString());
            sheet.getRow(9).getCell(5).setCellValue(map.get("thisMonthOrderNumber").toString());
            sheet.getRow(9).getCell(7).setCellValue(map.get("thisMonthVisitsNumber").toString());
            //--------------------热门套餐-------------------
            int cell = 12;
            List<Map<String,Object>> hotPackage = (List<Map<String, Object>>) map.get("hotPackage");
            if (hotPackage != null && hotPackage.size() > 0) {
                for (Map<String, Object> hotMap : hotPackage) {
                    XSSFRow row = sheet.getRow(cell);
                    row.getCell(4).setCellValue((String) hotMap.get("name"));
                    row.getCell(5).setCellValue(hotMap.get("count").toString());
                    row.getCell(6).setCellValue(((BigDecimal) hotMap.get("proportion")).doubleValue());
                    row.getCell(7).setCellValue((String) hotMap.get("remark"));
                    cell++;
                }
            }
            workbook.write(out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/getSex")
    public Result getSex(){

        List<Map<String,Object>> list = memberService.findSexCount();

        Map<String,Object> map = new HashMap<>();
        map.put("list",list);

        List<String> memberSex = new ArrayList<>();
        for (Map<String, Object> m : list) {
            String name = (String) m.get("name");
            m.put("name", Integer.parseInt(name)==1?"男":"女");
            memberSex.add((String) m.get("name"));
        }
        map.put("memberSex",memberSex);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }

    //年龄段比例
    @RequestMapping("/getAges")
    public Result getAges() {
        //调用业务层，获取会员的年龄数据
        List<Map> setMemberCount = memberService.getAges();
        //获取成功
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, setMemberCount);
    }
}
