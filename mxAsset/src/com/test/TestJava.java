package com.test;

import org.apache.commons.lang.math.NumberUtils;

public class TestJava {
	public static void main(String[] args) {
		new TestJava();
	}
	TestJava(){
		test1();
	}
	void test1(){
		System.out.println(NumberUtils.toLong("a"));
	}
}
