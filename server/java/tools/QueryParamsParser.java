package tools;

import java.util.Arrays;

public class QueryParamsParser {
    private String queryParams;
    private String passwordValue;
    private String loginValue;
    public QueryParamsParser(String queryParams)
    {
        this.queryParams = queryParams;
        parse();
    }
    private void parse()
    {
     String[] paramsWithValues = queryParams.split("&");
     System.out.println(Arrays.toString(paramsWithValues));
     if(paramsWithValues.length != 2)
         throw new RuntimeException("Incorrect amount of params");
     String loginWithValue = null, passwordWithValue = null;
     for(String parameter : paramsWithValues)
     {
         if(parameter.toLowerCase().startsWith("login=")){
             loginWithValue = parameter;
             continue;
         }
         if(parameter.toLowerCase().startsWith("password=")){
             passwordWithValue = parameter;
         }
     }
     if(loginWithValue == null || passwordWithValue == null)
     throw new RuntimeException("params are not found or incorrect");
    loginValue = loginWithValue.split("=")[1];
    passwordValue = passwordWithValue.split("=")[1];
    if(loginValue.length() == 0 || passwordValue.length() == 0)
        throw new RuntimeException("empty login or password");
    }

    public String getPasswordValue() {
        return passwordValue;
    }

    public String getLoginValue() {
        return loginValue;
    }
}
