package cn.itcast.core.demo;

import java.util.*;

/**
 * @Author: lshh
 * @Desc:
 * @Date: Created in 2019/07/07
 */
public class DemoTest {
    private static final Integer THREE_TIME = 3;
    private static final Integer INDEX = -1;

    /**
     * @return java.util.Set<java.lang.Character>
     * @desc 查找字符串数组所有元素中字符出现至少3次的字符
     * @params [arr]
     */
    public static Set<Character> getCharacterOccurThreeTimes(String[] arr) {
        if (null == arr || arr.length < 1) {
            return Collections.emptySet();
        }
        Set<Character> characterSet = new HashSet<Character>();
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            char[] chars = arr[i].toCharArray();
            for (char cc : chars) {
                map.put(cc, map.containsKey(cc) ? map.get(cc) + 1 : 1);
            }
        }
        Set<Map.Entry<Character, Integer>> entrySet = map.entrySet();
        for (Map.Entry<Character, Integer> charSet : entrySet) {
            if (charSet.getValue() >= THREE_TIME) {
                characterSet.add(charSet.getKey());
            }
        }
        return characterSet;
    }

    /**
     * @return java.lang.Integer
     * @desc 查找整数数组中指定整数出现的索引
     * @params [nums, target]
     */
    public static Integer getIndexIntegerOccur(Integer[] nums, Integer target) {
        if (null != nums && nums.length > 0 && null != target) {
            for (int i = 0; i < nums.length; i++) {
                if (nums[i].equals(target)) {
                    return i;
                }
            }
        }
        return INDEX;
    }

    public static void main(String[] args) {
        //String[] arr1 = {" "};
        String[] arr1 = {"bella", "label", "roller"};
        String[] arr2 = {"cool", "lock", "cook"};
        System.out.println("input:[\"bella\", \"label\", \"roller\"],output:" + getCharacterOccurThreeTimes(arr1));
        System.out.println("input:[\"cool\", \"lock\", \"cook\"],output:" + getCharacterOccurThreeTimes(arr2));
        Integer[] nums = {-1, 0, 3, 5, 9, 12};
        Integer target = 9;
        System.out.println("input:nums = " + Arrays.toString(nums) + ",target = " + target + ",output:" + getIndexIntegerOccur(nums, target));
        target = 2;
        System.out.println("input:nums = " + Arrays.toString(nums) + ",target = " + target + ",output:" + getIndexIntegerOccur(nums, target));
    }
}
