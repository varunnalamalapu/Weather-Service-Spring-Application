package com.weather;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TechEvalProcess {
    public int checkForNonRepeatingCharacter(String s) {
        if(StringUtils.isBlank(s)){
            return -1;
        }

        Map<Character, Integer> map = new HashMap<>();
        for (Character c : s.toCharArray()) {
            if (map.containsKey(c)) {
                map.put(c, map.get(c) + 1);
            } else {
                map.put(c, 1);
            }
        }

        for(int i=0;i<s.length();i++){
            if(map.get(s.charAt(i))==1){
                return i;
            }
        }

        return -1;
    }

    public void print(){
        List<String> list = new ArrayList<>();
        list.add("apple");
        list.add("cat");
        list.add("ball");
        list.add("baloon");
        list.add("ant");
        list.add("dog");

        list.stream()
                .sorted()
                .collect(Collectors.groupingBy(s -> s.charAt(0)))
                .forEach((key, value) -> System.out.println(value));
    }
/*
input  is array or list {"apple","cat","ball","baloon","ant","dog"}

output should print arrays

{"apple","ant"}
{"ball","baloon"}
{"cat"}
{"dog"}
* */
}
