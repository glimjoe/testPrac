package panda;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Test526 {

    public static String readFile(String filePath){
        StringBuffer sb = new StringBuffer();
        File file = new File(filePath);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String temp = null;
            while ((temp = br.readLine()) != null){
                sb.append(temp);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static void writeFile(String fileContent){
        File file = new File("E:\\tempTest526.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fo = new FileOutputStream(file);
            byte[] bytes = fileContent.getBytes();
            fo.write(bytes);
            fo.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static <E> List<E> getDuplicateElements(List<E> list) {
        return list.stream()                              // list 对应的 Stream
                .collect(Collectors.toMap(e -> e, e -> 1, Integer::sum)) // 获得元素出现频率的 Map，键为元素，值为元素出现的次数
                .entrySet().stream()                   // 所有 entry 对应的 Stream
                .filter(entry -> entry.getValue() > 1) // 过滤出元素出现次数大于 1 的 entry
                .filter(entry -> entry.getValue() < 3) // 过滤出元素出现次数大于 1 的 entry
                .map(entry -> entry.getKey())          // 获得 entry 的键（重复元素）对应的 Stream
                .collect(Collectors.toList());         // 转化为 List
    }

    public static void main(String[] args){
//        String a = readFile("E:\\tempTest526a.txt");
//        String b = readFile("E:\\tempTest526b.txt");
//        StringBuffer sb = new StringBuffer();
//        sb.append(a).append(b);
//        char[] temp = sb.toString().toCharArray();
//        Arrays.sort(temp);
//        writeFile(String.valueOf(temp));
        List<Integer> list = Arrays.asList(1,3,8,9,15,12,15,3,3,9);
        List<Integer> res = getDuplicateElements(list);
        for(int temp: res){
            System.out.println(temp);
        }
    }
}
