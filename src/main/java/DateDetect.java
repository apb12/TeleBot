public class DateDetect {
   static String dayD;
   static String yearD;
    static String monthD;

    public static void dateFind(String s) throws NumberFormatException{



        if(Integer.valueOf(s)<=31) {
            dayD = s;

        }


         else if(Integer.valueOf(s)>2000) {
             yearD = s;
         }

         else if (Integer.valueOf(s)>100&&Integer.valueOf(s)<2000 ){
             int i=Integer.valueOf(s)-100;
            monthD=String.valueOf(i);

        }
    }
}
