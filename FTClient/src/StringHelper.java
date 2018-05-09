public class StringHelper {

    public static String getType(String file){
        String res = "";
        for(int i = file.length()-1; i >= 0; i--){
            if(file.charAt(i) == '.') {
                res = file.substring(i+1);
                return res;
            }
        }
        return res;
    }
}
