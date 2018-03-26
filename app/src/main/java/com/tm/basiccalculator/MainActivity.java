package com.tm.basiccalculator;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.tm.basiccalculator.Utils.calculate;
import static com.tm.basiccalculator.Utils.isOperator;


public class MainActivity extends AppCompatActivity {

    private TextView screen;
    private String display;//the text displayed on screen
    private double result;//the calculations result
    private String pressedButton;//the last button pressed
    private boolean firstOperator;//indicates whether an operator has already been pressed
    private boolean infiniteValue;//indicates whether the last calculated result was infinite
    private boolean notNumber;//indicates if the result was Not A Number


    private void updateScreenAppend(String characterToAppend){//Updates the text displayed on screen by adding the characterToAppend
        display += characterToAppend;
        screen.setText(display);
    }

    private void updateScreenNewValue(String newVal){//Updates the text displayed on screen by setting a new value
        display = newVal;
        screen.setText(display);
    }

    private void resetScreen(){//resets the app to its initial state,called when C button is pressed
        updateScreenNewValue("0");
        result = 0;
        pressedButton = "C";
        firstOperator = false;
    }

    private void infiniteValue(Button... buttons){//infinite value handler
        updateScreenNewValue("Infinity");
        for(Button b : buttons){
            b.setEnabled(false);
        }
        infiniteValue = true;
    }

    private void enableButtonsAfterInfiniteOrNan(Button... buttons){//enables buttons disabled due to infinite value
        for(Button b : buttons){
            b.setEnabled(true);
        }
    }

    private void notNumber(Button... buttons){//NaN value handler
        updateScreenNewValue("NaN");
        for(Button b : buttons){
            b.setEnabled(false);
        }
        notNumber = true;
    }

    private void correctError(){
        if(display.length() == 1){//if there is only one number on screen
            resetScreen();
        }else {
            if(isOperator(display.charAt(display.length()-1))){//if you will delete the first operator,update flag
                firstOperator = false;
            }
            //remove last character and update screen
            updateScreenNewValue(display.substring(0, display.length() - 1));
            //the last pressed button before the error correction
            pressedButton = display.substring(display.length() - 1);
        }
    }

    private void numberFunction(String number){//called when a number's button is pressed(for numbers 1-9)
        if(pressedButton.equals("C")){
            updateScreenNewValue(number);
        }else{
            updateScreenAppend(number);
        }
        pressedButton = number;
    }

    private void zeroFunction(){//called when zero button is pressed
        if(!pressedButton.equals("C")){//if the last pressed button was "C",don't do anything
            if(!pressedButton.equals("0")) {
                updateScreenAppend("0");
                pressedButton = "0";
            }else{//if the last pressed button was zero
                if(!isOperator(display.charAt(display.length()-2))){//if the second last pressed button isn't an operator,update screen(e.g. 5+20 and we pressed zero again)
                    updateScreenAppend("0");
                }//if the second last pressed button is an operator(e.g.5+0 and we pressed zero again),we don't change anything
            }
        }
    }

    private void equality(Button... buttons){
        double num1;//the two numbers to be calculated
        double num2;

        for(int i=0; i < display.length();i++) {
            if (isOperator(display.charAt(i))) {//find the operator

                if(i > 0 && display.charAt(i-1) == 'E'){
                    continue;       //if a number is written like exponentially(e.g. 5E-6), ignore the operator "-"
                }

                if (i == 0 && display.charAt(i) == '-') { //if the operator is minus and in the first place,then ignore it.
                    continue;   // It is a negative number not the subtraction operator
                }

                if(i == display.length()-1){//if the operator is in the last position
                    break;//don't do anything
                }

                num1 = Double.parseDouble(display.substring(0, i));//find the two numbers
                num2 = Double.parseDouble(display.substring(i + 1, display.length()));

                Double res = calculate(num1, display.charAt(i), num2);//make calculation
                result = res;

                if(Double.isNaN(result)){//if the result is Not A Number
                    notNumber(buttons);
                    break;
                }
                if(Double.isInfinite(result)){//if the result is infinite
                    infiniteValue(buttons);
                    break;
                }

                if ((res == Math.floor(res)) && !Double.isInfinite(res)) {//if the result is an integer value

                    if(res > Long.MAX_VALUE || res < Long.MIN_VALUE){//if the result can't be a long number
                        infiniteValue(buttons);
                        break;
                    }else {
                        display = String.valueOf((long) result);//text to be displayed,don't show decimal values
                    }
                }else{//if the result is a decimal number
                    display = String.valueOf(result);
                }
                break;
            }
        }
    }

    private void operation(String operation, Button... buttons){//For addition,subtraction,multiplication and division
        if(!firstOperator){ //if this is the first operator
            firstOperator = true;
            updateScreenAppend(operation);
            pressedButton = operation;
        }else { //if this is the second operator
            if(isOperator(pressedButton.charAt(0))){//if the button pressed last was an operator
                correctError();
                //replace the other operator and update screen
                updateScreenAppend(operation);
                pressedButton = operation;
                firstOperator = true;
            }else {
                equality(buttons);
                updateScreenAppend(operation);
                pressedButton = operation;//update last pressed button
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        screen = findViewById(R.id.numbers_screen);//the calculator screen

        final Button buttonCancel = findViewById(R.id.buttonCancel);
        final Button buttonCorrectError = findViewById(R.id.buttonCorrectError);
        final Button buttonEquals = findViewById(R.id.buttonEquals);
        final Button buttonSubtraction = findViewById(R.id.buttonSubtraction);
        final Button buttonAddition = findViewById(R.id.buttonAddition);
        final Button buttonMultiplication = findViewById(R.id.buttonMultiplication);
        final Button buttonDivision = findViewById(R.id.buttonDivision);
        final Button buttonPoint = findViewById(R.id.buttonPoint);
        final Button buttonPercentage = findViewById(R.id.buttonPercentage);
        final Button buttonZero = findViewById(R.id.button0);
        final Button buttonOne = findViewById(R.id.button1);
        final Button buttonTwo = findViewById(R.id.button2);
        final Button buttonThree = findViewById(R.id.button3);
        final Button buttonFour = findViewById(R.id.button4);
        final Button buttonFive = findViewById(R.id.button5);
        final Button buttonSix = findViewById(R.id.button6);
        final Button buttonSeven = findViewById(R.id.button7);
        final Button buttonEight = findViewById(R.id.button8);
        final Button buttonNine = findViewById(R.id.button9);

        resetScreen();

        //The Cancel button
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetScreen();
                if(infiniteValue || notNumber){
                    enableButtonsAfterInfiniteOrNan(buttonCorrectError,buttonEquals,buttonSubtraction,buttonAddition,buttonMultiplication,buttonDivision,buttonPoint,buttonPercentage,
                            buttonZero,buttonOne,buttonTwo,buttonThree,buttonFour,buttonFive,buttonSix,buttonSeven,buttonEight,buttonNine);
                    infiniteValue = false;
                    notNumber = false;
                }
            }
        });


        //The CorrectError(CE) button
        buttonCorrectError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!pressedButton.equals("C")){
                    correctError();
                }
            }
        });

        //The Equals button
        //color the Equals button holo_red_light
        buttonEquals.getBackground().setColorFilter(Color.parseColor("#ffff4444"), PorterDuff.Mode.MULTIPLY);
        buttonEquals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equality(buttonCorrectError,buttonEquals,buttonSubtraction,buttonAddition,buttonMultiplication,buttonDivision,buttonPoint,buttonPercentage,
                        buttonZero,buttonOne,buttonTwo,buttonThree,buttonFour,buttonFive,buttonSix,buttonSeven,buttonEight,buttonNine);
                updateScreenNewValue(display);
                pressedButton = "=";
            }
        });

        //The Subtraction button
        buttonSubtraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operation("-",buttonCorrectError,buttonEquals,buttonSubtraction,buttonAddition,buttonMultiplication,buttonDivision,buttonPoint,buttonPercentage,
                        buttonZero,buttonOne,buttonTwo,buttonThree,buttonFour,buttonFive,buttonSix,buttonSeven,buttonEight,buttonNine);
            }
        });


        //The Addition button
        buttonAddition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               operation("+",buttonCorrectError,buttonEquals,buttonSubtraction,buttonAddition,buttonMultiplication,buttonDivision,buttonPoint,buttonPercentage,
                       buttonZero,buttonOne,buttonTwo,buttonThree,buttonFour,buttonFive,buttonSix,buttonSeven,buttonEight,buttonNine);
            }
        });

        //The Multiplication button
        buttonMultiplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operation("*",buttonCorrectError,buttonEquals,buttonSubtraction,buttonAddition,buttonMultiplication,buttonDivision,buttonPoint,buttonPercentage,
                        buttonZero,buttonOne,buttonTwo,buttonThree,buttonFour,buttonFive,buttonSix,buttonSeven,buttonEight,buttonNine);
            }
        });

        //The Division button
        buttonDivision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operation("/",buttonCorrectError,buttonEquals,buttonSubtraction,buttonAddition,buttonMultiplication,buttonDivision,buttonPoint,buttonPercentage,
                        buttonZero,buttonOne,buttonTwo,buttonThree,buttonFour,buttonFive,buttonSix,buttonSeven,buttonEight,buttonNine);
            }
        });

        //The Point button
        buttonPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean pointFound = false;

                for(int i=0; i < display.length(); i++){
                    if(display.charAt(i) == '.'){//if you find another point in the display text
                        pointFound = true;
                        for(int j=i+1; j < display.length()-1; j++){//search from the point's position til the end if there is an operator
                            if(isOperator(display.charAt(j))){
                                updateScreenAppend(".");
                                pressedButton = ".";
                                break;
                            }
                        }
                        break;
                    }
                }
                if(!pointFound){//if there is no point at all,add one
                    updateScreenAppend(".");
                    pressedButton = ".";
                }
            }
        });

        //The Percentage button
        buttonPercentage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean notNumber = false;
                for(int i=0; i < display.length(); i++){
                    if(isOperator(display.charAt(i)) && display.charAt(i-1) != 'E'){
                        notNumber = true;
                        break;
                    }
                }
                if(!notNumber) {
                    updateScreenAppend("*0.01");
                    equality(buttonCorrectError, buttonEquals, buttonSubtraction, buttonAddition, buttonMultiplication, buttonDivision, buttonPoint, buttonPercentage,
                            buttonZero, buttonOne, buttonTwo, buttonThree, buttonFour, buttonFive, buttonSix, buttonSeven, buttonEight, buttonNine);
                    updateScreenNewValue(display);
                    pressedButton = "%";
                }
            }
        });

        //The Zero button
        buttonZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zeroFunction();
            }
        });

        //The One button
        buttonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberFunction("1");
            }
        });

        //The Two button
        buttonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberFunction("2");
            }
        });

        //The Three button
        buttonThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberFunction("3");
            }
        });

        //The Four button
        buttonFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberFunction("4");
            }
        });

        //The Five button
        buttonFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberFunction("5");
            }
        });

        //The Six button
        buttonSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberFunction("6");
            }
        });

        //The Seven button
        buttonSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberFunction("7");
            }
        });

        //The Eight button
        buttonEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberFunction("8");
            }
        });

        //The Nine button
        buttonNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberFunction("9");
            }
        });

    }

}
