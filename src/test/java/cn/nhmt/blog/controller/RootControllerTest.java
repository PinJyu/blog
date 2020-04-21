package cn.nhmt.blog.controller;

import org.junit.jupiter.api.Test;

import javax.naming.event.ObjectChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RootControllerTest {

    @Test
    void send() {
        String s = "x\nx\nx\nx\n\nx";
        System.out.println(s);
        String s1 = s.replaceAll("\n", "");
        System.out.println(s1);
    }

    @Test
    void index() {
        int i = 198;
        int i1 = i / 10;
        System.out.println(i1);
        int i2 = i % 10;
        System.out.println(i2);
    }

    @Test
    void about() {
        String x = " nihao\txxx\nggg\fjj j   j  ";
        System.out.println(x);
        System.out.println(x.replaceAll("\n", ""));
        System.out.println(x.replaceAll("\t" ,""));
        System.out.println(x.replaceAll("\\s+", ""));
    }

    @Test
    void testSend() {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder1 = new StringBuilder();
        for (int i = 0; i < 240; ++i) {
            if (i % 30 == 0) {
                stringBuilder.append("\n");
                stringBuilder1.append("\n");
            }
            stringBuilder.append("a");
            if (i % 2 == 0) {
                stringBuilder1.append("码");
            }
        }
        System.out.println("作为一名教育和保健护理工作者，我曾经和数不清的感染上艾滋病病毒的孩子打过交道。我和这些特殊的孩子之间的关系是生活赋予的恩".length());

        System.out.println(stringBuilder.toString());
        System.out.println(stringBuilder1.toString());
    }

    @Test
    void get() {
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        System.out.println(objectObjectHashMap.getClass());
        Object o = (Object) objectObjectHashMap;
        System.out.println(o.getClass());
        System.out.println(o instanceof HashMap);
    }
}