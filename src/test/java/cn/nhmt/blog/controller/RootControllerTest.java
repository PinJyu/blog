package cn.nhmt.blog.controller;

import org.junit.jupiter.api.Test;

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
    }

    @Test
    void testSend() {
    }

    @Test
    void get() {
    }
}