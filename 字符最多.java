import java.util.HashMap;
import java.util.Map;

public class 字符最多{
    public static void main(String[] args) {
        String input = "aaaabcsdf";
        //用一个map统计每个字符出现的次数
        Map<Character,Integer> charCount = new HashMap<>();

        for(char c : input.toCharArray()){
            // 核心行：统计每个字符的次数
            charCount.put(c,charCount.getOrDefault(c, 0)+1);
            //charCount.getOrDefault(c, 0) —— 找字符的当前次数，找不到就给 0
            // + 1 —— 次数加 1（统计本次出现）
            // charCount.put(c, ...) —— 把新次数存回 Map
        }   

        //找出出现次数最多的字符 和次数
        char maxChar = ' ';
        int maxCount = 0 ;
        //entrySet() 把 Map 里的每一组 key-value 封装成一个 Entry 对象的集合
		//通过entry对象 可以同时拿到 key  和 value
        for(Map.Entry<Character,Integer> entry : charCount.entrySet()){
            if(entry.getValue()>maxCount){
                maxCount = entry.getValue();
                maxChar = entry.getKey();
            }
        }
        System.out.println("出现最多的字符是"+ maxChar);
        System.out.println("对应的次数是"+ maxCount);

    }
}