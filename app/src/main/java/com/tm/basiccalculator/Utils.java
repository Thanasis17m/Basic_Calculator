package com.tm.basiccalculator;


public class Utils {//the functions used in MainActivity

    public static boolean isOperator(char c){ //Checks if a character is an operator
        if(c == '+' || c == '-' || c == '*' || c == '/' || c == '%'){
            return true;
        }else {
            return false;
        }
    }

    public static double calculate(double num1, char operator, double num2){//basic calculations between two numbers
        if(operator == '+'){
            return num1 + num2;
        }else if(operator == '-'){
            return num1 - num2;
        }else if(operator == '*'){
            return num1*num2;
        }else if(operator == '/'){
            return num1/num2;
        }
        return -1;
    }


}
