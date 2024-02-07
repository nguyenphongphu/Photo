package com.tp.photo.Utility;



import static java.util.Collections.swap;

import java.io.File;
import java.util.Date;
import java.util.List;

public class selectionSort {

    public List<String> selectionSort(List<String> list){
        int i, j,min;
        for (i = 0; i < list.size(); i++) {
            min = i;
            File file=new File(list.get(i));
            Date date=new Date(file.lastModified());
            for (j = i+1; j < list.size(); j++){
                File filej=new File(list.get(j));
                Date date1 = new Date(filej.lastModified());
                if (date.before(date1)){
                    min = j;
                }
                String temp = list.get(min);
                list.set(min,list.get(i));
                list.set(i,temp);
            }

        }
        return list;
    }

}
