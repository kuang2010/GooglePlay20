package com.kuang2010.googleplay20;

import com.kuang2010.googleplay20.manager.ActivityStack;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test1(){
        ActivityStack inastance1 = ActivityStack.getInastance();
        ActivityStack inastance2 = ActivityStack.getInastance();
        System.out.println(">>>>>>>>>>"+(inastance1==inastance2)+",inastance1="+inastance1);
        //>>>>>>>>>>true,inastance1=com.kuang2010.googleplay20.manager.ActivityStack@22927a81
    }

    @Test
    public void test2(){
        ActivityStack inastance1 = ActivityStack.getInastance();
        System.out.println(">>>>>>>>>>inastance1="+inastance1);
        //>>>>>>>>>>inastance1=com.kuang2010.googleplay20.manager.ActivityStack@22927a81
    }
}