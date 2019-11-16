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

    /**
     * 获取关键字的权重
     * @return 关键字权重
     */
    public double fetchWeight() {
        int byteLength = getByteLength(keyword);
        return Math.pow(byteLength, 1.3) * Math.sqrt(count);
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
    private static int getByteLength(String s) {
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
