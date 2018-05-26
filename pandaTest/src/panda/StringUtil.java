package panda;

import com.alibaba.fastjson.*;

import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {



    public static void main(String[] args){
//        String encoding = "GBK";
//        File file = new File("cap.txt");
//        Long filelength = file.length();
//        byte[] filecontent = new byte[filelength.intValue()];
//        FileInputStream in = null;
        try {
//            in = new FileInputStream(file);
//            in.read(filecontent);
//            in.close();
//			String asB64 = Base64.getEncoder().encodeToString("change".getBytes("utf-8"));
            String source = new String("");
            byte[] dec = Base64.getDecoder().decode(source);
            String tostr = new String(dec, "gbk");
            System.out.println(tostr);
            JSONObject jso = JSON.parseObject(tostr);
            JSONArray jsa = jso.getJSONObject("cache").getJSONObject("replaced").getJSONArray("battle");
            JSONArray jsa2 = jsa.getJSONObject(0).getJSONObject("battleInfo").getJSONArray("enemyUserSvt");
            for(int i = 0; i < jsa2.size(); i++){
                JSONObject temp = jsa2.getJSONObject(i);
                temp.put("atk", 1);
            }
            String str = jso.toJSONString();
            String finalStep = str.replace("/", "\\/");
            String result = encode(finalStep, "GBK");
            System.out.println(result);
			System.out.println(Base64.getEncoder().encodeToString(result.getBytes("GBK")));
        }catch(Exception e){
            e.printStackTrace();
        }
    }



    public static String gbEncoding(final String gbString) {   //gbString = "测试"
        char[] utfBytes = gbString.toCharArray();   //utfBytes = [测, 试]
        String unicodeBytes = "";
        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
            String hexB = Integer.toHexString(utfBytes[byteIndex]);   //转换为16进制整型字符串
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        System.out.println("unicodeBytes is: " + unicodeBytes);
        return unicodeBytes;
    }



    public static String encode(String str, String charset) {
        Pattern p = Pattern.compile("[\\u4e00-\\u9fa5\\u3040-\\u309F\\u30A0-\\u30FF\\uFF00-\\uFFEF]+");
        Matcher m = p.matcher(str);
        StringBuffer b = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(b, gbEncoding(m.group(0)).replaceAll("u", "\\\\u"));
        }
        m.appendTail(b);
        return b.toString();
    }
}
