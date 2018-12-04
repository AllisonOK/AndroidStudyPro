package com.lianer.sort;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list.add("aaa");
        list.add("1st");
        list.add("8");
        list.add("b8d");
        list.add("ccc");
        list.add("李胜");
        list.add("是否");
        Collections.sort(list, new SortComparator());

        for (String str : list) {
            Log.i("asdfasasfd", str);
        }
    }

    /**
     * 联系人排序，最终的排序方法
     */
    private int compareCallNum = 0;//判读是否是第一层的比较（递归调用中）

    class SortComparator implements Comparator {
        @Override
        public int compare(Object lhs, Object rhs) {

            compareCallNum = 0;
            return compareString((String) lhs, (String) rhs);

        }
    }

    //只比较一个字符，递归调用
    public int compareString(String lhs, String rhs) {

        compareCallNum++;

        //判断第一个字符，汉字最前，其次字母，然后是数字，若有其他符号，放在最后

        String nameA = lhs;//.trim();//注意，由于递归调用，所以此处不能再使用trim了
        String nameB = rhs;//.trim();

        //若存在长度为0的情况：
        if ((nameA.length() == 0) && (nameB.length() == 0)) {
            return 0;
        } else if (nameA.length() == 0) {
            return -1;
        } else if (nameB.length() == 0) {
            return 1;
        }

        String firstStrA = nameA.substring(0, 1);
        String firstStrB = nameB.substring(0, 1);

        //先从类型上来区分：汉字>字母>数字>其他符号，若类型不同，立即出比较的结果
        //但是汉字与字母，由于存在首字母的分段，所以先不区分开
        int typeA = getFirstCharType(nameA);
        int typeB = getFirstCharType(nameB);
        if (typeA > typeB) {
            return -1;//返回负值，则往前排
        } else if (typeA < typeB) {
            return 1;
        }


        //类型相同，需要进行进一步的比较
        int compareResult;

        //不是字母与汉字
        if (typeA < 9 && typeB < 9) {
            compareResult = firstStrA.compareTo(firstStrB);
            if (compareResult != 0) {
                //若不同，立即出来比较结果
                return compareResult;
            } else {
                //若相同，则递归调用
                return compareString(nameA.substring(1), nameB.substring(1));
            }
        }

        //若首字的第一个字母相同，或不是首字，判断原字符是汉字还是字母，汉字排在前面
        typeA = getFirstCharType2(nameA);
        typeB = getFirstCharType2(nameB);
        if (typeA > typeB) {
            return -1;
        } else if (typeA < typeB) {
            return 1;
        }

        if (isLetter(nameA) && isLetter(nameB)) {
            //若是同一个字母，还要比较下大小写
            compareResult = firstStrA.compareTo(firstStrB);
            if (compareResult != 0) {
                return compareResult;
            }
        }

        //若相同，则进行下一个字符的比较（递归调用）
        return compareString(nameA.substring(1), nameB.substring(1));
    }

    boolean isLetter(String str) {
        char c = str.charAt(0);
        // 正则表达式，判断首字母是否是英文字母
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c + "").matches()) {
            return true;
        }
        return false;
    }

    boolean isNumber(String str) {
        char c = str.charAt(0);
        // 正则表达式，判断首字母是否是英文字母
        Pattern pattern = Pattern.compile("^[1-9]+$");
        if (pattern.matcher(c + "").matches()) {
            return true;
        }
        return false;
    }

    boolean isHanzi(String str) {
        char c = str.charAt(0);
        // 正则表达式，判断首字母是否是英文字母
        Pattern pattern = Pattern.compile("[\\u4E00-\\u9FA5]+");
        if (pattern.matcher(c + "").matches()) {
            return true;
        }
        return false;
    }

    int getFirstCharType(String str) {
        if (isHanzi(str)) {
            return 10;
        } else if (isLetter(str)) {
            return 10;
        } else if (isNumber(str)) {
            return 8;
        } else {
            return 0;
        }
    }

    int getFirstCharType2(String str) {
        if (isHanzi(str)) {
            return 7;
        } else if (isLetter(str)) {
            return 9;
        } else if (isNumber(str)) {
            return 8;
        } else {
            return 0;
        }
    }

}
