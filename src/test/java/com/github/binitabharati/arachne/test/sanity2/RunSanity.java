package com.github.binitabharati.arachne.test.sanity2;

import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class RunSanity {
	
	public static void main(String[] args) throws Exception {
		//Class testClass = Class.forName("com.github.binitabharati.arachne.test.sanity."+args[0]);
		JUnitCore engine = new JUnitCore();
        engine.addListener(new TextListener(System.out)); // required to print reports - This will print to the redirected file , ie runSanity.log
        engine.run(TestCases.class);
		
	}

}
