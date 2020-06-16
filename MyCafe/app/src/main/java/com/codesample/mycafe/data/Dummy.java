package com.codesample.mycafe.data;

import java.util.ArrayList;
import java.util.List;

public class Dummy {
    private static Dummy instance = new Dummy();
    private List<CafeMenu> menus;
    private Dummy() {
        menus = new ArrayList<>();
        CafeMenu m1 = new CafeMenu("c1", "아메리카노", 3000, "에스프레소와 뜨거운 물", "");
        CafeMenu m2 = new CafeMenu("c2", "카페라떼", 4500, "에스프레소와 스팀 밀크", "");
        CafeMenu m3 = new CafeMenu("c3", "카페모카", 4500, "에스프레소와 스팀 밀크, 초코 시럽", "");
        CafeMenu m4 = new CafeMenu("c4", "캬라멜마키아토", 4500, "에스프레소와 스팀 밀크, 캬라멜 시럽", "");
        CafeMenu m5 = new CafeMenu("c5", "밀크티", 4500, "홍차와 우유", "");
        menus.add(m1);
        menus.add(m2);
        menus.add(m3);
        menus.add(m4);
        menus.add(m5);
    }
    public static Dummy getInstance() { return instance; }
    public List<CafeMenu> getMenus() { return menus; }
}
