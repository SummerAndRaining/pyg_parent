package cn.itcast.core.demo;

import com.alibaba.dubbo.common.utils.CollectionUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author: lshh
 * @Desc:
 * @Date: Created in 2019/07/12
 */
public class Demo {
    /**
     * 获取数组中出现次数最多的单词
     *
     * @param s 字符串数组
     * @return s中出现次数最多的单词。如果有多个字符串出现次数相同且同为最多，则返回任意一个即可
     * <p>
     * 例： 输入： ["long", "long", "ago"], 返回"long"
     */
    public static String getMaxCountWord(String[] s) {
        if (null != s && s.length > 0) {
            Map<String, Integer> map = new HashMap<String, Integer>();
            for (int i = 0; i < s.length; i++) {
                if (map.containsKey(s[i])) {
                    map.put(s[i], map.get(s[i]) + 1);
                } else {
                    map.put(s[i], 1);
                }
            }
            Collection<Integer> values = map.values();
            if (CollectionUtils.isNotEmpty(values)) {
//                Set<String> keys = map.keySet();
//                Iterator keys_Itera = keys.iterator();
//                String maxKey = (String) keys_Itera.next();
//                Integer maxValue = map.get(maxKey);
//                while (keys_Itera.hasNext()) {
//                    String temp = (String) keys_Itera.next();
//                    if (maxValue < map.get(temp)) {
//                        maxKey = temp;
//                        maxValue = map.get(temp);
//                    }
//                }
                Set<Map.Entry<String, Integer>> entries = map.entrySet();
                String maxKey = null;
                Integer maxValue = 1;
                for (Map.Entry<String, Integer> entry : entries) {
                    if (maxValue < entry.getValue()) {
                        maxValue = entry.getValue();
                        maxKey = entry.getKey();
                    }
                }
                return maxKey;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        String[] s = {"long", "long", "ago"};
        String maxCountWord = getMaxCountWord(s);
        System.out.println(maxCountWord);
    }
}
