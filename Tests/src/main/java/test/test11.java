package test;

/**
 * ƴ�Ӳ�ѯ���
 * @author Administrator
 */

public class test11 {
	public static void main(String[] args) {
		StringBuilder sqlOr = new StringBuilder("");
		final int LENGTH = 1;
		String[] customers = {
				"9614271697",
				"9614271695",
				"9614271689",
				"9614271668"
		};
		for(int i=0; i<customers.length; i++){
   			if(i % LENGTH == 0){
   				sqlOr.append(" or s.customer_no in (");
   			}
   			sqlOr.append("'").append(customers[i].trim()).append("'").append(",");
   			if(i% (LENGTH) == LENGTH-1){
   				sqlOr.deleteCharAt(sqlOr.length()-1);
   				sqlOr.append(" )");
   			}
   		}
   		
   		//��һ�� or �滻�� and
   		int index = sqlOr.indexOf("or", 0);
		sqlOr.replace( index, index+"or".length(), "and");
		
		String sql = "	select s.customer_no customerNo "
					+ "	from posp_boss.customer_business_license s "
					+ "	where s.local_update_time is not null "
					+ "	AND S.PARAM2 in ( 'SUCCESS','�Ѿ��޸������������̻�����') "
					+ "	AND  S.PARAM1 not  LIKE '%Res%' " +sqlOr.toString();
		
		System.out.println(sql);
	}
}
