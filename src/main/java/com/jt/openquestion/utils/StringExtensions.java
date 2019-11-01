package com.jt.openquestion.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringExtensions {

    public static int[] splitIntItems(String s){
        if(s == null || s.isEmpty()){
            return new int[0];
        }
        String[] items = s.split(",");
        int[] result = new int[items.length];
        for(int i=0;i< items.length;i++){
            result[i] = Integer.parseInt(items[i]);
        }
        return result;
    }
}
