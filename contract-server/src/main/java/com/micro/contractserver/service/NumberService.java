package com.micro.contractserver.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class NumberService {

    /*//大写数字
    private final String[] NUMBERS = {"零","壹","贰","叁","肆","伍","陆","柒","捌","玖"};
    
    // 整数部分的单位
    private final String[] IUNIT = {"元","拾","佰","仟","万","拾","佰","仟","亿","拾","佰","仟","万","拾","佰","仟"};
    
    //小数部分的单位
    private final String[] DUNIT = {"角","分","厘"};
    
    //转成中文的大写金额
    public String toChinese(String str) {
        //判断输入的金额字符串是否符合要求
        if (StringUtils.isBlank(str) || !str.matches("(-)?[\\d]*(.)?[\\d]*")) {
            System.out.println("抱歉，请输入数字！");
            return str;
        }
        if("0".equals(str) || "0.00".equals(str) || "0.0".equals(str)) {
            return "零元";
        }
        //判断是否存在负号"-"
        boolean flag = false;
        if(str.startsWith("-")){
            flag = true;
            str = str.replaceAll("-", "");
        }

        str = str.replaceAll(",", "");//去掉","
        String integerStr;//整数部分数字
        String decimalStr;//小数部分数字

        // 初始化：分离整数部分和小数部分
        if(str.indexOf(".")>0) {
            integerStr = str.substring(0,str.indexOf("."));
            decimalStr = str.substring(str.indexOf(".")+1);
        } else if(str.indexOf(".")==0) {
            integerStr = "";
            decimalStr = str.substring(1);
        } else {
            integerStr = str;
            decimalStr = "";
        }

        //beyond超出计算能力，直接返回
        if(integerStr.length()>IUNIT.length) {
            System.out.println(str+"：超出计算能力");
            return str;
        }

        int[] integers = toIntArray(integerStr);//整数部分数字
        // 判断整数部分是否存在输入012的情况
        if (integers.length>1 && integers[0] == 0) {
            System.out.println("抱歉，请输入数字！");
            if (flag) {
            str = "-"+str;
            }
            return str;
        }
        boolean isWan = isWan5(integerStr);//设置万单位
        int[] decimals = toIntArray(decimalStr);//小数部分数字
        String result = getChineseInteger(integers,isWan)+getChineseDecimal(decimals);//返回最终的大写金额
        if(flag) {
            return "负"+result;//如果是负数，加上"负"
        } else {
            return result;
        }
    }

    //将字符串转为int数组
    private int[] toIntArray(String number) {
        int[] array = new int[number.length()];
        for(int i = 0;i<number.length();i++) {
            array[i] = Integer.parseInt(number.substring(i,i+1));
        }
        return array;
    }

    //将整数部分转为大写的金额
    public String getChineseInteger(int[] integers,boolean isWan) {
        StringBuffer chineseInteger = new StringBuffer("");
        int length = integers.length;
        if (length == 1 && integers[0] == 0) {
            return "";
        }
        for(int i=0;i<length;i++) {
            String key = "";
            if(integers[i] == 0) {
                if((length - i) == 13) {//万（亿）
                    key = IUNIT[4];
                }
                else if((length - i) == 9) {//亿
                    key = IUNIT[8];
                } else if((length - i) == 5 && isWan) {//万
                    key = IUNIT[4];
                } else if((length - i) == 1) {//元
                    key = IUNIT[0];
                }
                if((length - i)>1 && integers[i+1]!=0) {
                    key += NUMBERS[0];
                }
            }
            chineseInteger.append(integers[i]==0?key:(NUMBERS[integers[i]]+IUNIT[length - i -1]));
        }
        return chineseInteger.toString();
    }

    //将小数部分转为大写的金额
    private String getChineseDecimal(int[] decimals) {
        StringBuffer chineseDecimal = new StringBuffer("");
        for(int i = 0;i<decimals.length;i++) {
            if(i == 3) {
                break;
            }
            chineseDecimal.append(decimals[i]==0?"":(NUMBERS[decimals[i]]+DUNIT[i]));
        }
        return chineseDecimal.toString();
    }

    //判断当前整数部分是否已经是达到【万】
    private boolean isWan5(String integerStr) {
        int length = integerStr.length();
        if(length > 4) {
            String subInteger = "";
            if(length > 8) {
                subInteger = integerStr.substring(length- 8,length -4);
            } else {
                subInteger = integerStr.substring(0,length - 4);
            }
            return Integer.parseInt(subInteger) > 0;
        } else {
            return false;
        }
    }*/

    private final String UNIT = "万千佰拾亿千佰拾万千佰拾元角分";
    private final String SL_UNIT = "万千佰拾亿千佰拾万千佰拾 ";
    private final String DIGIT = "零壹贰叁肆伍陆柒捌玖";
    private final double MAX_VALUE = 9999999999999.99D;

    // 金额转中文
    public String moneyToChinese(double v) {
        String prefix = "";
        if(v < 0 ) {
            prefix = "负";
            v = Math.abs(v);
        }
        if (v > MAX_VALUE) {
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
        int j = UNIT.length() - strValue.length();
        String rs = "";
        boolean isZero = false;
        for (; i < strValue.length(); i++, j++) {
            char ch = strValue.charAt(i);
            if (ch == '0') {
                isZero = true;
                if (UNIT.charAt(j) == '亿' || UNIT.charAt(j) == '万' || UNIT.charAt(j) == '元') {
                    rs = rs + UNIT.charAt(j);
                    isZero = false;
                }
            } else {
                if (isZero) {
                    rs = rs + "零";
                    isZero = false;
                }
                rs = rs + DIGIT.charAt(ch - '0') + UNIT.charAt(j);
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
        if(v < 0 ) {
            prefix = "负";
            v = Math.abs(v);
        }
        if (v > MAX_VALUE) {
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
        int j = SL_UNIT.length() - strValue.length();
        String rs = "";
        boolean isZero = false;
        for (; i < strValue.length(); i++, j++) {
            char ch = strValue.charAt(i);
            if (ch == '0') {
                isZero = true;
                if (SL_UNIT.charAt(j) == '亿' || SL_UNIT.charAt(j) == '万' ) {
                    rs = rs + SL_UNIT.charAt(j);
                    isZero = false;
                }
            } else {
                if (isZero) {
                    rs = rs + "零";
                    isZero = false;
                }
                rs = rs + DIGIT.charAt(ch - '0') + SL_UNIT.charAt(j);
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
            if(i == ps.length - 1){
                p += capitaLization[Integer.parseInt(String.valueOf(ps[i]))];
            }else if (a < company.length) {
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
        if(userI.length>=17){
            //替换亿位单位15
            userI[15] = '亿';
        }
        //反转字符数组
        //倒循环数组对p赋值
        for (int i = userI.length-1; i >=0; i--) {
            numFinal += String.valueOf(userI[i]);
        }
        return numFinal;
    }
    
}
