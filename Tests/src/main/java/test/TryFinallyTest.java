package test;
/**
 * ²âÊÔ try catch finally µÄÖ´ĞĞË³Ğò
 * @author Administrator
 */
public class TryFinallyTest {
	public static void main(String[] args) {
		test1();
	}
	
	public static  int test1(){
		int i = 0; 
		try {
			
			return test2(i);
		} catch (Exception e) {
			return i;
		}finally{
			i = 2;
		}
	}
	
	public static  int test2(int i){
		i = 1;
		return i/0;
	}
}
