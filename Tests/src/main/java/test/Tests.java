package test;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class Tests {

	public static void main(String[] args){
		String str = "SUCCESS,SETTLED,REPEAL,REVERSALED,partRefund,refund";
		List<String> asList = Arrays.asList(str.split(","));
//		asList.remove(asList.indexOf("partRefund"));
		asList.removeAll(Arrays.asList(new String[]{"partRefund","refund"}));
		System.out.println(asList);
	}
	@Test
	public void text(){
		
	}
}
