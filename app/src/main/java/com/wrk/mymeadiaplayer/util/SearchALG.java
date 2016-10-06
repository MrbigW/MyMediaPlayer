package com.wrk.mymeadiaplayer.util;

import android.content.Context;

/**
 * Created by MrbigW on 2016/10/6.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: 搜索匹配的算法
 * -------------------=.=------------------------
 */

public class SearchALG {
    private Context context;
    /**
     * 将汉字转换成拼音的工具类
     */
    private CharacterParser characterParser;

    public SearchALG(Context context) {
        this.context = context;
        characterParser = CharacterParser.getInstance();
    }

    /**
     * 判断该条数据是否添加到提示列表中
     *
     * @param hint_data
     * @param changedText
     * @return
     */
    public boolean isAddToHintList(String hint_data, String changedText) {

        String hintFirst = getFirstCharacter(hint_data.substring(0, 1));
        String changedTextFirst = getFirstCharacter(changedText.substring(0, 1));

        boolean result = false;
        String selling = characterParser.getSelling(hint_data);
        boolean acronymEquals = isAcronymEquals(hint_data, changedText);
        if (hintFirst.equalsIgnoreCase(changedTextFirst)) {
            if (selling.contains(changedText.toLowerCase()) || selling.contains(changedText)) {
                result = true;
            }
        } else {
            result = false;
        }

        //首字母缩写和汉字全拼有一个为true就返回true
        if (result || acronymEquals) {
            result = true;
        } else {
            result = false;
        }

        return result;
    }

    /**
     * 判断首字母缩写是否匹配
     *
     * @param hint_data
     * @param changedText
     * @return
     */
    private boolean isAcronymEquals(String hint_data, String changedText) {
        boolean result = true;
        String hintFirst;
        String changedTextFirst;

        int startIndex = 0;
        int endIndex = 1;
        boolean isEquals = true;

        for (int i = 0; i < changedText.length() && isEquals; i++) {
            hintFirst = getFirstCharacter(hint_data.substring(startIndex, endIndex));
            changedTextFirst = getFirstCharacter(changedText.substring(startIndex, endIndex));
            if (!hintFirst.equalsIgnoreCase(changedTextFirst)) {
                isEquals = false;
                result = false;
            }
            startIndex++;
            endIndex++;
        }

        return result;
    }

    /**
     * 根据传入的汉字得到首字母
     *
     * @param data
     * @return
     */
    private String getFirstCharacter(String data) {
        String result = "";
        String singlePinyin = characterParser.convert(data);//逐个取出汉字并转成拼音
        result = singlePinyin.substring(0, 1);//取出首字母
        return result;
    }

}
