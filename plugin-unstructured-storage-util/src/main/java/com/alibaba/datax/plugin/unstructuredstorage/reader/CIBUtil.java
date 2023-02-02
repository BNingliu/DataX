package com.alibaba.datax.plugin.unstructuredstorage.reader;

import com.alibaba.datax.common.exception.DataXException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: DataX
 * @description: 兴业银行数据解析
 * @author: liuningbo
 * @create: 2022/12/28 13:53
 */
public class CIBUtil {
    static String cols = "1$$dqdh$$char(2)$$(1,2)\n" +
            "2$$jgdh$$char(3)$$(3,5)\n" +
            "3$$jgjb$$char(1)$$(6,6)\n" +
            "4$$qsjb$$char(1)$$(7,7)\n" +
            "5$$jgmc$$char(60)$$(8,67)\n" +
            "6$$jhdh$$char(5)$$(68,72)\n" +
            "7$$zhqsjg$$char(3)$$(73,75)\n" +
            "8$$csbm$$char(8)$$(76,83)\n" +
            "9$$dz$$char(60)$$(84,143)\n" +
            "10$$dhhm$$char(30)$$(144,173)\n" +
            "11$$lxr$$char(12)$$(174,185)\n" +
            "12$$jlsj$$char(6)$$(186,191)\n" +
            "13$$fhbm$$char(8)$$(192,199)\n" +
            "14$$jgflbz$$char(8)$$(200,207)\n" +
            "15$$djhjgh$$char(3)$$(208,210)\n" +
            "16$$sjjgdh$$char(3)$$(211,213)\n" +
            "17$$zwsj$$char(5)$$(214,218)\n" +
            "18$$jhrq$$date$$(219,226)\n" +
            "19$$dqcs$$smallint$$(227,238)\n" +
            "20$$dqxc$$smallint$$(239,250)\n" +
            "21$$jhjg$$smallint$$(251,262)\n" +
            "22$$zdjhcc$$smallint$$(263,274)\n" +
            "23$$dzlhbz$$char(1)$$(275,275)\n" +
            "24$$rhkhbz$$char(1)$$(276,276)\n" +
            "25$$jgzt$$char(1)$$(277,277)\n" +
            "26$$sfjywd$$char(1)$$(278,278)\n" +
            "27$$zfhhh$$char(12)$$(279,290)\n" +
            "28$$sxqhdm$$char(6)$$(291,296)\n" +
            "29$$wgbm$$char(12)$$(297,308)\n" +
            "30$$yjppbz$$char(1)$$(309,309)\n" +
            "31$$jlzt$$char(1)$$(310,310)";


    public static List<String> getColValueByIndex(String[] parseRows, List<ColumnEntry> column) {
        char[] chars = parseRows[0].toCharArray();
        List<Character> cc = new ArrayList<>();
        List<String> list = new ArrayList<>();

        for (char aChar : chars) {
            if (isChinese(aChar)) {
                cc.add(aChar);
                cc.add('\u1008');
            } else {
                cc.add(aChar);
            }
        }
        StringBuffer sb = null;
        for (ColumnEntry columnEntry : column) {
            String[] cibIndexSplit = columnEntry.getCIBIndex().split(",");
            if (cibIndexSplit.length != 2) {
                throw DataXException.asDataXException(
                        UnstructuredStorageReaderErrorCode.RUNTIME_EXCEPTION,
                        String.format("运行时异常 : 兴业银行下标格式不对！！"));
            }
            sb = new StringBuffer();
            Integer initValue = Integer.valueOf(cibIndexSplit[0].trim());
            Integer endValue = Integer.valueOf(cibIndexSplit[cibIndexSplit.length - 1].trim());
            for (int i = initValue - 1; i < endValue; i++) {
                if (i >= cc.size()) {
                    sb.append("");
                } else {
                    sb.append(cc.get(i).toString().trim());
                }
            }
            list.add(sb.toString().replace("\u1008", ""));

        }
        return list;

    }


    public static List<String> getColValueByIndex(String[] parseRows) {
        char[] chars = parseRows[0].toCharArray();
        List<Character> cc = new ArrayList<>();
        List<String> list = new ArrayList<>();

        for (char aChar : chars) {
            if (isChinese(aChar)) {
                cc.add(aChar);
                cc.add('\u1008');
            } else {
                cc.add(aChar);
            }
        }

        String regEx = "(?<=\\()(.+?)(?=\\))";
        Pattern p = Pattern.compile(regEx);

        String[] split = cols.split("\n");
        for (String sp : split) {
            String[] ss = sp.split("\\$\\$");
            Matcher matcher = p.matcher(ss[ss.length - 1]);

            StringBuffer sb = new StringBuffer();

            while (matcher.find()) {
                String[] diffValue = matcher.group().split(",");
                Integer initValue = Integer.valueOf(diffValue[0].trim());
                Integer endValue = Integer.valueOf(diffValue[diffValue.length - 1].trim());
//                Integer addValue = endValue - initValue + 1;
                sb = new StringBuffer();

                for (int i = initValue - 1; i < endValue; i++) {
                    if (i >= cc.size()) {
                        sb.append("");
                    } else {
                        sb.append(cc.get(i));
                    }
                }
                list.add(sb.toString().replace("\u1008", ""));
            }

        }
        return list;

    }

    public static final boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }
}
