package com.example.myapplication.utils;

import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


/**
 * Created by heiduo on 2019/7/24.
 */
public class StringUtils {
    public final static String UTF_8 = "UTF-8";
    public final static String[] day = {"00:00:00", "01:00:00", "02:00:00", "03:00:00", "04:00:00",
            "05:00:00", "06:00:00", "0:00:00", "08:00:00", "09:00:00", "10:00:00", "11:00:00",
            "12:00:00", "13:00:00", "14:00:00", "15:00:00", "16:00:00", "17:00:00", "18:00:00",
            "19:00:00", "20:00:00", "21:00:00", "22:00:00", "23:00:00"};

    public static float float2Decimal(float data) {
        return (float) (Math.round(data * 100) / 100);
    }

    public static int[] bubbleSort(int[] args) {//冒泡排序算法
        for (int i = 0; i < args.length - 1; i++) {
            for (int j = i + 1; j < args.length; j++) {
                if (args[i] > args[j]) {
                    int temp = args[i];
                    args[i] = args[j];
                    args[j] = temp;
                }
            }
        }
        return args;
    }

    //获取最大的数 以及最大数的下标
    public static int[] getIntMax(int[] args) {
        int[] data = new int[2];
        int max = 0;
        data[0] = 0;
        for (int i = 0; i < args.length; i++) {
            if (args[i] > data[0]) {
                data[0] = args[i];
                max = i;
            }
        }
        data[1] = max;
        return data;
    }

    //获取最大的数 以及最大数的下标
    public static float[] getFloatMax(float[] args) {
        float[] data = new float[2];
        float max = 0;
        data[0] = 0;
        for (int i = 0; i < args.length; i++) {
            if (args[i] > data[0]) {
                data[0] = args[i];
                max = i;
            }
        }
        data[1] = max;
        return data;
    }

    /**
     * 获取boolean数组中第一个为false的下标
     *
     * @param args
     * @return
     */
    public static int getFalseIndex(boolean[] args) {
        for (int i = 0; i < args.length; i++) {
            if (!args[i]) {
                return i;
            }
        }
        return 0;
    }

    public static int getFalseIndex(boolean[] args,int index) {
        for (int i = index; i < args.length; i++) {
            if (!args[i]) {
                return i;
            }
        }

        return 0;
    }

    /**
     * 获取boolean数组,指定位置前一个为false的下标，若不存在为false的位置，返回传入的位置最近为false的下标
     *
     * @param args
     * @return
     */
    public static int getLastFalseIndex(boolean[] args, int index) {
        if (index >= args.length) {
            return 0;
        }
        for (int i = index; i >= 0; i--) {
            if (!args[i]) {
                return i;
            }
        }

        return getFalseIndex(args,index);
    }

    /**
     * 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false
     */
    public static boolean isEmpty(String value) {
        if (value != null && !"".equalsIgnoreCase(value.trim())
                && !"null".equalsIgnoreCase(value.trim())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 验证手机号码
     *
     * @param mobiles
     * @return [0-9]{5,9}
     */
    public boolean isMobileNO(String mobiles) {
        try {
            Pattern p = Pattern
                    .compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
            Matcher m = p.matcher(mobiles);
            return m.matches();
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 验证手机号格式
     *
     * @param number 以1开头 后面接10位
     * @return 返回是否匹配正则
     */
    public static boolean isMobile(String number) {
        String num = "^1\\d{10}$";
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }

    /**
     * 验证密码格式 ,英文字母or数字or _ . (6-12位）
     *
     * @param password 输入的密码
     * @return 返回值
     */
    public static boolean isCorrectPass(String password) {
        String num = "^[\\w.]{6,12}$";
        if (null == password) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return password.matches(num);
        }
    }

    /**
     * 判断验证码格式
     *
     * @param code 6位数字
     * @return 返回值
     */
    public static boolean isMobileCode(String code) {
        if (TextUtils.isEmpty(code)) {
            return false;
        }
        Pattern p = Pattern
                .compile("^\\d{6}$");
        Matcher m = p.matcher(code);
        return m.matches();
    }

    //验证设备Mac地址
    public static boolean isDeviceCode(String deviceCode) {
        if (deviceCode == null) {
            return false;
        }
        String num = "^([0-9a-fA-F]{2})(([:-][0-9a-fA-F]{2}){5})$";//Mac地址以空格,"-",":"分开
        if (TextUtils.isEmpty(deviceCode)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return deviceCode.matches(num);
        }
    }

    public static boolean isCorrectName(String name) {
        if (name == null || getWordCount(name) > 12) {
            return false;
        }
        String num = "^[a-zA-Z0-9\u4e00-\u9fa5]+$";
        if (TextUtils.isEmpty(name)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return name.matches(num);
        }
    }

    //是否输入正确的设备名
    public static boolean isCorrectDeviceName(String name) {
        String num = "^[A-Za-z0-9]{0,12}$";

        if (TextUtils.isEmpty(name)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return name.matches(num);
        }
    }

    /** 由于Java是基于Unicode编码的，因此，一个汉字的长度为1，而不是2。
     * 但有时需要以字节单位获得字符串的长度。例如，“123abc长城”按字节长度计算是10，而按Unicode计算长度是8。
     * 为了获得10，需要从头扫描根据字符的Ascii来获得具体的长度。如果是标准的字符，Ascii的范围是0至255，如果是汉字或其他全角字符，Ascii会大于255。
     * 因此，可以编写如下的方法来获得以字节为单位的字符串长度。*/
    public static int getWordCount(String s)
    {
        int length = 0;
        for(int i = 0; i < s.length(); i++)
        {
            int ascii = Character.codePointAt(s, i);
            if(ascii >= 0 && ascii <=255)
                length++;
            else
                length += 2;

        }
        return length;

    }

    public static float scale_16_to_10(String str_power, String str_child) {
      /*  String str_voltage = str_power ;
        if (str_child.length()==2){
            str_voltage = str_voltage + str_child;
        }else if (str_child.length()==1){
            str_voltage = str_voltage + "0" + str_child;
        }

        int voltage = (Integer.valueOf(str_voltage, 16));
        BigDecimal b1 = new BigDecimal(voltage+"");
        BigDecimal b2 ;
        if (Environments.RX_SERVICE_UUID_JQ.equals(Environments.RX_SERVICE_UUID_JQ)){
            b2 = new BigDecimal("1024");
        }else {
            b2 = new BigDecimal("4096");
        }
        double vo = (b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).doubleValue()) * 3.3  ;
        return (float) vo;*/
        int power = Integer.parseInt(str_power, 16);
        int child = Integer.parseInt(str_child, 16);
        int voltage = child * 256 + power;
        return (float) voltage;
    }


    public static byte[] str2HexByte(String str, int type) {
        byte[] data;
        switch (type) {
            case 1:
                data = new byte[1];
                short i = Short.valueOf(str);
                data[0] = (byte) (char) i;
                break;
            case 2:
                data = new byte[2];
                int integer = Integer.valueOf(str);
                if (integer > 255) {
                    int i1 = integer / 256;
                    int i2 = integer % 256;
                    data[0] = (byte) (char) i1;
                    data[1] = (byte) (char) i2;
                } else {
                    data[0] = (byte) (char) 0;
                    data[1] = (byte) (char) integer;
                }
                break;
            default:
                data = new byte[1];
                break;
        }

        return data;
    }

    /**
     * 普通字符转换成16进制字符串
     *
     * @param str
     * @return
     */
    public static String str2HexStr(String str) {
        byte[] bytes = str.getBytes();
        // 如果不是宽类型的可以用Integer
        BigInteger bigInteger = new BigInteger(1, bytes);
        return bigInteger.toString(16);
    }

    /**
     * 16进制的字符串转换成16进制字符串数组
     *
     * @param src
     * @return
     */
    public static byte[] HexString2Bytes(String src) {
        int len = src.length() / 2;
        byte[] ret = new byte[len];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < len; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    public static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0}));
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1}));
        return (byte) (_b0 ^ _b1);
    }

    /**
     * @param x     当前值
     * @param total 总值
     *              [url=home.php?mod=space&uid=7300]@return[/url] 当前百分比
     * @Description 返回百分之值
     */
    public static String getPercent(int x, int total) {
        String result = "";// 接受百分比的值
        double x_double = x * 1.0;
        double tempresult = x_double / total * 1000;
        // 百分比格式，后面不足2位的用0补齐 ##.00%
        DecimalFormat df1 = new DecimalFormat("0%");
        result = df1.format(tempresult);
        return result;
    }


    public static final String GZIP_ENCODE_UTF_8 = "UTF-8";

    public static final String GZIP_ENCODE_ISO_8859_1 = "ISO-8859-1";

    /**
     * 字符串压缩为GZIP字节数组
     *
     * @param str
     * @param encoding
     * @return
     */
    public static byte[] compress(String str, String encoding) {
        if (str == null || str.length() == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes(encoding));
            gzip.close();
        } catch (IOException e) {
            Log.e("gzip compress error.", e.getMessage());
        }
        return out.toByteArray();
    }

    /**
     * Gzip  byte[] 解压成字符串
     *
     * @param bytes
     * @return
     */
    public static String uncompressToString(byte[] bytes) {
        return uncompressToString(bytes, GZIP_ENCODE_UTF_8);
    }


    /**
     * Gzip  byte[] 解压成字符串
     *
     * @param bytes
     * @param encoding
     * @return
     */
    public static String uncompressToString(byte[] bytes, String encoding) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            return out.toString(encoding);
        } catch (IOException e) {
            Log.e("gzip compress error.", e.getMessage());
        }
        return null;
    }

    /**
     * 判断byte[]是否是Gzip格式
     *
     * @param data
     * @return
     */
    public static boolean isGzip(byte[] data) {
        int header = (int) ((data[0] << 8) | data[1] & 0xFF);
        return header == 0x1f8b;
    }

    /**
     * 获取字符串中包含几个字符
     */
    public static int getCharNumber(String str, char s) {
        int count = 0;
        int index = str.indexOf(s);
        while (index >= 0) {
            count++;
            index = str.indexOf(s, index + 1);
        }

        return count;
    }

}
