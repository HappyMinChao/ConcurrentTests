package test;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class SqlConcat {
	
	private static int  LENGTH = 2;
	
	public static void main(String[] args) {
		String[] customers = new String[]{"   ","8628233517","	  ","8628233521","8628233523","8628233525","8628233537"};
		//List customerList = new ArrayList();
		List<String> realCustoers = new ArrayList<String>();
		
		for(int i =0, j=0; i< customers.length; i++){
			if("".equals(customers[i].trim())){
				continue;
			}
			realCustoers.add(customers[i].trim());
		}
		
		if(realCustoers.size() == 0){
			return ;
		}
		
   		StringBuilder sqlOr = new StringBuilder("");
   		for(int i=0; i<realCustoers.size(); i++){
   			if(i % LENGTH == 0){
   				sqlOr.append(" or s.customer_no in (");
   			}
   			sqlOr.append("'").append(realCustoers.get(i)).append("'").append(",");
   			if(i% (LENGTH) == LENGTH-1 || i == realCustoers.size()-1){
   				sqlOr.deleteCharAt(sqlOr.length()-1);
   				sqlOr.append(" )");
   			}
   		}
   		
   		//第一个 or 替换成 and
   		int index = sqlOr.indexOf("or", 0);
		//sqlOr.replace( index, index+"or".length(), "and");
   		sqlOr.delete(index, index+"or".length());
		
		String sql = "	select s.customer_no customerNo "
					+ "	from posp_boss.customer_business_license s "
					+ "	where s.local_update_time is not null "
					+ "	AND S.PARAM2 in ( 'SUCCESS','已经修改完上送银行商户名称') "
					+ "	AND  S.PARAM1 not  LIKE '%Res%' and (" +sqlOr.toString() +")";
		BigDecimal bigDecimal = new BigDecimal("0.01");
		bigDecimal.add(new BigDecimal("0.000001"));
		bigDecimal.doubleValue();
		
   		System.out.println(sql);
	}
}
