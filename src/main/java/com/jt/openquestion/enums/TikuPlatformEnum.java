package com.jt.openquestion.enums;

public enum TikuPlatformEnum {
    Motk(1),
    XueKe(2),
    Jyeoo(3),
    WeiLaiNao(4),
    ManFen5(5),
    XiangPi(6),
    ZuJuan(7),
    KuaXue(8),
    Cooco(9),
    LeLeKeTang(10),
    ZuJuan21(11),
    TiKu(12),
    TiKu5U(13),
    GaoZhongShiJuan(14),
    DianLiangKeTang(15),
    Xkb1(16),
    JinTaiYang(17),
    MotkTest(18),
    MotkUat(19),


    UnKnow(99);

    private int value;
    private TikuPlatformEnum(int value) {
        this.value = value;
    }

    public Integer getValue(){
        return this.value;
    }

    public static TikuPlatformEnum parse(int platformType){
        TikuPlatformEnum[] values =  TikuPlatformEnum.values();
        for (TikuPlatformEnum value : values){
            if(value.getValue().equals(platformType)){
                return value;
            }
        }
        return TikuPlatformEnum.UnKnow;
    }
}
