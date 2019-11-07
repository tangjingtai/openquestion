package com.jt.openquestion.entity;

public class KeywordRecord {
    public KeywordRecord(String keyword) {
        this(keyword, 1);
    }

    public KeywordRecord(String keyword, int count) {
        this.keyword = keyword;
        this.count = count;
    }

    private String keyword;
    private int count;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getWeight() {
        int byteLength = getByteLength(keyword);
        return byteLength * count * Math.sqrt(Math.log(1 + byteLength * count));
    }

    public void increment() {
        this.count++;
    }

    /**
     * 获取字符串的字节长度，中文byte算2
     *
     * @param s 字符串
     * @return 字符串的byte长度
     */
    public static int getByteLength(String s) {
        int length = 0;
        for (char c : s.toCharArray()) {
            if ((int) c <= 255)
                length++;
            else {
                length += 2;
            }
        }
        return length;
    }
}
