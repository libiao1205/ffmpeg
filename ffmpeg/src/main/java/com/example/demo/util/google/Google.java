package com.example.demo.util.google;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author libiao
 * @date 2022/2/14
 */
public class Google {
    /**
     * Key -> Value
     * 语言     -> 单词表示
     */
    private static final Map<String, String> LANGUAGE = new HashMap<String, String>();
    // 定义互译语言对常数变量，符合google页面相关对译语言对的值
    public static final String LANGPAIR_CN_EN = "zh-CN|en"; // 汉语到英语
    public static final String LANGPAIR_EN_CN = "en|zh-CN"; // 英语到汉语
    public static final String LANGPAIR_EN_JA = "en|ja"; // 英语到日语
    // 定义编码常数
    public static final String CHARSET_CN = "GBK";
    public static final String CHARSET_JA = "Shift_JIS";

    static {
        LANGUAGE.put("阿尔巴尼亚语", "sq");
        LANGUAGE.put("阿拉伯语", "ar");
        LANGUAGE.put("阿塞拜疆语", "az");
        LANGUAGE.put("爱尔兰语", "ga");
        LANGUAGE.put("爱沙尼亚语", "et");
        LANGUAGE.put("巴斯克语", "eu");
        LANGUAGE.put("白俄罗斯语", "be");
        LANGUAGE.put("保加利亚语", "bg");
        LANGUAGE.put("冰岛语", "is");
        LANGUAGE.put("波兰语", "pl");
        LANGUAGE.put("波斯语", "fa");
        LANGUAGE.put("布尔语", "af");
        LANGUAGE.put("南非荷兰语", "af");
        LANGUAGE.put("丹麦语", "da");
        LANGUAGE.put("德语", "de");
        LANGUAGE.put("俄语", "ru");
        LANGUAGE.put("法语", "fr");
        LANGUAGE.put("菲律宾语", "tl");
        LANGUAGE.put("芬兰语", "fi");
        LANGUAGE.put("格鲁吉亚语", "ka");
        LANGUAGE.put("古吉拉特语", "gu");
        LANGUAGE.put("海地克里奥尔语", "ht");
        LANGUAGE.put("韩语", "ko");
        LANGUAGE.put("荷兰语", "nl");
        LANGUAGE.put("加利西亚语", "gl");
        LANGUAGE.put("加泰罗尼亚语", "ca");
        LANGUAGE.put("捷克语", "cs");
        LANGUAGE.put("卡纳达语", "kn");
        LANGUAGE.put("克罗地亚语", "hr");
        LANGUAGE.put("拉丁语", "la");
        LANGUAGE.put("拉脱维亚语", "lv");
        LANGUAGE.put("老挝语", "lo");
        LANGUAGE.put("立陶宛语", "lt");
        LANGUAGE.put("罗马尼亚语", "ro");
        LANGUAGE.put("马耳他语", "mt");
        LANGUAGE.put("马来语", "ms");
        LANGUAGE.put("马其顿语", "mk");
        LANGUAGE.put("孟加拉语", "bn");
        LANGUAGE.put("挪威语", "no");
        LANGUAGE.put("葡萄牙语", "pt");
        LANGUAGE.put("日语", "ja");
        LANGUAGE.put("瑞典语", "sv");
        LANGUAGE.put("塞尔维亚语", "sr");
        LANGUAGE.put("世界语", "eo");
        LANGUAGE.put("斯洛伐克语", "sk");
        LANGUAGE.put("斯洛文尼亚语", "sl");
        LANGUAGE.put("斯瓦希里语", "sw");
        LANGUAGE.put("泰卢固语", "te");
        LANGUAGE.put("泰米尔语", "ta");
        LANGUAGE.put("泰语", "th");
        LANGUAGE.put("土耳其语", "tr");
        LANGUAGE.put("威尔士语", "cy");
        LANGUAGE.put("乌尔都语", "ur");
        LANGUAGE.put("乌克兰语", "uk");
        LANGUAGE.put("希伯来语", "iw");
        LANGUAGE.put("希腊语", "el");
        LANGUAGE.put("西班牙语", "es");
        LANGUAGE.put("匈牙利语", "hu");
        LANGUAGE.put("亚美尼亚语", "hy");
        LANGUAGE.put("意大利语", "it");
        LANGUAGE.put("意第绪语", "yi");
        LANGUAGE.put("印地语", "hi");
        LANGUAGE.put("印尼语", "id");
        LANGUAGE.put("英语", "en");
        LANGUAGE.put("越南语", "vi");
        LANGUAGE.put("中文繁体", "zh-TW");
        LANGUAGE.put("中文简体", "zh-CN");

    }

    // 将文本进行URL编码
    private static String encodeText(String text) {
        String str = null;
        try {
            str = java.net.URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String translate(String translateText, String langpair) {
// text是google翻译页面提交时对于欲翻译文字的变量名
// langpair是google翻译页面提交时对于采用何种互对语言的变量名
        String urlstr = "http://translate.google.com/translate_t?text=" + encodeText(translateText) + "&langpair=zh-CN|en";

        URL url = null;
        try {
            url = new URL(urlstr);
        } catch (MalformedURLException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
// System.out.println(url);
        URLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        connection.setRequestProperty("User-agent", "IE/9.0"); // 必须，否则报错，到于FF的怎么写，没做过测试
        try {
            connection.connect();
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        String charset = getCharsetFromLangpair(langpair); // 自动获取目标语言的编码
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset));
        } catch (UnsupportedEncodingException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        } // 使用指定编码接收数据
        String line = null;
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            in.close();
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        String translation = getContent(sb.toString());
        return translation;
    }

    private static String getCharsetFromLangpair(String langpair) {
// 当翻译的目标语言为日语时，采用Shift+JIS编码接收数据
        if (langpair.equals(LANGPAIR_EN_JA)) {
            return CHARSET_JA;
        } else {
            return CHARSET_CN;
        }
    }

    /** */
    /**
     * 从获得的源文件中剥取翻译内容
     * 分析google翻译生成的html源码来看
     * 翻译内容被置于
     * 和
     * 标签之间
     *
     * @param htmltext 获得的网页源代码
     */
    private static String getContent(String htmltext) {
        String ss = "";
        String se = "";


        String[] info = htmltext.split("</span>")[7].split(">");

        return info[info.length - 1];
    }
}
