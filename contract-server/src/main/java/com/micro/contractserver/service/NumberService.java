package com.micro.contractserver.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class NumberService {

    public String unit;
    public String sl_unit;
    public String digit;
    public double max_value;

    public NumberService() {
        unit = "万千佰拾亿千佰拾万千佰拾元角分";
        sl_unit = "万千佰拾亿千佰拾万千佰拾 ";
        digit = "零壹贰叁肆伍陆柒捌玖";
        max_value = 9999999999999.99D;
    }

    // 金额转中文
    public String moneyToChinese(double v) {
        String prefix = "";
        if(v < 0) {
            prefix = "负";
            v = Math.abs(v);
        }
        if (v > max_value) {
            return "参数非法!";
        }

        long l = Math.round(v * 100);
        if (l == 0) {
            return "零元整";
        }
        String strValue = l + "";
        // i用来控制数
        int i = 0;
        // j用来控制单位
        int j = unit.length() - strValue.length();
        String rs = "";
        boolean isZero = false;
        for (; i < strValue.length(); i++, j++) {
            char ch = strValue.charAt(i);
            if (ch == '0') {
                isZero = true;
                if (unit.charAt(j) == '亿' || unit.charAt(j) == '万' || unit.charAt(j) == '元') {
                    rs = rs + unit.charAt(j);
                    isZero = false;
                }
            } else {
                if (isZero) {
                    rs = rs + "零";
                    isZero = false;
                }
                rs = rs + digit.charAt(ch - '0') + unit.charAt(j);
            }
        }
        if (!rs.endsWith("分")) {
            rs = rs + "整";
        }
        rs = rs.replaceAll("亿万", "亿");
        return prefix + rs;
    }

    // 数量转中文
    public String slToChinese(double v) {
        String prefix = "";
        if(v < 0) {
            prefix = "负";
            v = Math.abs(v);
        }
        if (v > max_value) {
            return "参数非法!";
        }

        long l = Math.round(v);
        if (l == 0) {
            return "零";
        }
        String strValue = l + "";
        // i用来控制数
        int i = 0;
        // j用来控制单位
        int j = sl_unit.length() - strValue.length();
        String rs = "";
        boolean isZero = false;
        for (; i < strValue.length(); i++, j++) {
            char ch = strValue.charAt(i);
            if (ch == '0') {
                isZero = true;
                if (sl_unit.charAt(j) == '亿' || sl_unit.charAt(j) == '万' ) {
                    rs = rs + sl_unit.charAt(j);
                    isZero = false;
                }
            } else {
                if (isZero) {
                    rs = rs + "零";
                    isZero = false;
                }
                rs = rs + digit.charAt(ch - '0') + sl_unit.charAt(j);
            }
        }
        rs = rs.replaceAll("亿万", "亿");
        return prefix + rs;
    }

    public String transFormation(String userInput) {
        //1.定义一个匹配数组
        char[] capitaLization = {'零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'};
        char[] company = {'拾', '佰', '仟','万'};

        //2.把传入的字符串转换为数组遍历进行替换数字
        //2.1定义一个字符串存储拼接
        String p = "";
        //2.2定义字符串变量用作返回值
        String numFinal = "";
        //字符串转换为数字
        char[] ps = userInput.toCharArray();

        //定义变量统计单位下标
        int a = 0;
        //倒序循环字符串
        for (int i = ps.length - 1; i >= 0; i--) {
            //如果单位下标不越界
            if(i == ps.length - 1) {
                p += capitaLization[Integer.parseInt(String.valueOf(ps[i]))];
            } else if (a < company.length) {
                p += String.valueOf(company[a]);
                p += capitaLization[Integer.parseInt(String.valueOf(ps[i]))];
                a++;
            } else {
                //如果下标越界了然下标回到1这里使用0
                a = 0;
                p += String.valueOf(company[0]);
                p += capitaLization[Integer.parseInt(String.valueOf(ps[i]))];
                a++;
            }
        }
        //如果字符串长度大于9说明到亿位所以替换亿位和万位的单位
        //1.替换好的字符串转为数组 玖拾捌佰柒仟陆拾伍佰肆仟叁拾贰佰壹
        char[] userI = p.toCharArray();
        //如果字符数组长度大于17则说明有亿位
        if(userI.length >= 17) {
            //替换亿位单位15
            userI[15] = '亿';
        }
        //反转字符数组
        //倒循环数组对p赋值
        for (int i = userI.length-1; i >= 0; i--) {
            numFinal += String.valueOf(userI[i]);
        }

        System.out.println(numFinal);

        return numFinal;
    }
    
}
