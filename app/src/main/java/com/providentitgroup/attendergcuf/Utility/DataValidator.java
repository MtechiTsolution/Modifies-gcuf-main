package com.providentitgroup.attendergcuf.Utility;



public  class DataValidator {

    //Trimmer and Case Converter
    public static String filterData(String input,  int dataCase){
        if(input==null)return "";
        input =input.trim();
        switch (dataCase){
            //UPPER CASE
            case 1:
                input= input.toUpperCase();
                break;
            case 2:
                input=input.toLowerCase();
                break;
            case 3:
                input= toTitleCase(input.toLowerCase());
            default:
                break;
        }
        return input;
    }

    public static boolean isEmailValid(String email){
        if(email==null || email.length()<=0)return false;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    public static boolean isPasswordValid(String pass){
        return !(pass==null || pass.length()<8);
    }

    public static String toTitleCase(String givenString) {
        if(givenString.trim().length()<=0)return "";
        String[] arr = givenString.trim().split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
    }
}
