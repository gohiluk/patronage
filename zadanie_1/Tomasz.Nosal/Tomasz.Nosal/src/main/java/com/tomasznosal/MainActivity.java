package com.tomasznosal;

import android.app.Activity;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener {

    private static final char PLUS='+';
    private static final char MINUS='-';
    private static final char MULTI='*';
    private static final char DIV='/';
    private static final String CANTDIVIDEBY0 = "Can't / by 0";
    private static final String DOT=".";
    private static final Float ZERO=0.f;
    private static final Integer WAITING_TIME=1000;
    private static final Float ERROR_CODE=-1.f;
    private static final Float NEUTRAL_NUMBER=1.f;
    EditText textResult;
    EditText smallTextResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textResult = (EditText) findViewById(R.id.textResult);
        smallTextResult = (EditText) findViewById(R.id.smallTextResult);
        int idList[] = { R.id.button0, R.id.button1, R.id.button2,
                R.id.button3, R.id.button4, R.id.button5, R.id.button6,
                R.id.button7, R.id.button8, R.id.button9, R.id.buttonAdd,
                R.id.buttonSub, R.id.buttonMulti, R.id.buttonDiv,
                R.id.buttonEqualsSign, R.id.buttonDot, R.id.buttonCancel };
        for (int i : idList) {
            View v = findViewById(i);
            v.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        String prevResult = textResult.getText().toString();
        String smallPrevResult = smallTextResult.getText().toString();
        int lengthSPR = smallPrevResult.length();
        switch (v.getId()) {
            case R.id.buttonAdd:
                buttonAddPressed(prevResult, smallPrevResult, lengthSPR);
                break;
            case R.id.buttonSub:
                buttonSubPressed(prevResult, smallPrevResult, lengthSPR);
                break;
            case R.id.buttonMulti:
                buttonMultiPressed(prevResult, smallPrevResult, lengthSPR);
                break;
            case R.id.buttonDiv:
                buttonDivPressed(prevResult, smallPrevResult, lengthSPR);
                break;
            case R.id.buttonEqualsSign:
                buttonEqualsSignPressed(prevResult, smallPrevResult, lengthSPR);
                break;
            case R.id.buttonDot:
                if (!prevResult.contains(DOT)) {
                    textResult.setText(prevResult + DOT);
                }
                break;
            case R.id.buttonCancel:
                smallTextResult.setText("");
                textResult.setText("");
                break;
            default:
                String number = ((Button) v).getText().toString();
                textResult.setText(prevResult + number);
        }
    }

    private void buttonAddPressed(String prevResult, String smallPrevResult, int lengthSPR){
        if (!smallPrevResult.isEmpty()) {
            if (checkIfThereIsntAnySignAndIfItIsPossibleToAdd(prevResult, smallPrevResult, lengthSPR)) {
                smallTextResult.setText(prevResult + PLUS);
                textResult.setText("");
            }
            if (ifLastSignIsDifferentThanPlus(smallPrevResult, lengthSPR)) {
                String tmp = smallPrevResult.substring(0, lengthSPR - 1);
                smallTextResult.setText(tmp + PLUS);
            }
        } else {
            if (!prevResult.isEmpty()) {
                smallTextResult.setText(prevResult + PLUS);
                textResult.setText("");
            }
        }
    }

    private boolean checkIfThereIsntAnySignAndIfItIsPossibleToAdd(String prevResult, String smallPrevResult, int lengthSPR){
        return smallPrevResult.isEmpty()
                && !prevResult.isEmpty()
                && (smallPrevResult.charAt(lengthSPR - 1) != PLUS
                || smallPrevResult.charAt(lengthSPR - 1) != MINUS
                || smallPrevResult.charAt(lengthSPR - 1) != MULTI
                || smallPrevResult.charAt(lengthSPR - 1) != DIV);
    }

    private boolean ifLastSignIsDifferentThanPlus(String smallPrevResult, int lengthSPR){
        return smallPrevResult.charAt(lengthSPR - 1) == MINUS
                || smallPrevResult.charAt(lengthSPR - 1) == MULTI
                || smallPrevResult.charAt(lengthSPR - 1) == DIV;
    }

    private void buttonSubPressed(String prevResult, String smallPrevResult, int lengthSPR){
        if (!smallPrevResult.isEmpty()) {
            if (checkIfThereIsntAnySignAndIfItIsPossibleToAdd(prevResult, smallPrevResult, lengthSPR)) {
                smallTextResult.setText(prevResult + MINUS);
                textResult.setText("");
            }
            if (ifLastSignIsDifferentThanMinus(smallPrevResult, lengthSPR)) {
                String tmp = smallPrevResult.substring(0, lengthSPR - 1);
                smallTextResult.setText(tmp + MINUS);
            }
        } else {
            if (!prevResult.isEmpty()) {
                smallTextResult.setText(prevResult + MINUS);
                textResult.setText("");
            } else {
                textResult.setText(MINUS);
            }
        }
    }

    private boolean ifLastSignIsDifferentThanMinus(String smallPrevResult, int lengthSPR){
        return smallPrevResult.charAt(lengthSPR - 1) == PLUS
                || smallPrevResult.charAt(lengthSPR - 1) == MULTI
                || smallPrevResult.charAt(lengthSPR - 1) == DIV;
    }

    private void buttonMultiPressed(String prevResult, String smallPrevResult, int lengthSPR){
        if (!smallPrevResult.isEmpty()) {
            if (checkIfThereIsntAnySignAndIfItIsPossibleToAdd(prevResult, smallPrevResult, lengthSPR)) {
                smallTextResult.setText(prevResult + MULTI);
                textResult.setText("");
            }
            if (ifLastSignIsDifferentThanMultiplicationSign(smallPrevResult, lengthSPR)) {
                String tmp = smallPrevResult.substring(0, lengthSPR - 1);
                smallTextResult.setText(tmp + MULTI);
            }
        } else {
            if (!prevResult.isEmpty()) {
                smallTextResult.setText(prevResult + MULTI);
                textResult.setText("");
            }
        }
    }

    private boolean ifLastSignIsDifferentThanMultiplicationSign(String smallPrevResult, int lengthSPR){
        return smallPrevResult.charAt(lengthSPR - 1) == PLUS
                || smallPrevResult.charAt(lengthSPR - 1) == MINUS
                || smallPrevResult.charAt(lengthSPR - 1) == DIV;
    }

    private void buttonDivPressed(String prevResult, String smallPrevResult, int lengthSPR){
        if (!smallPrevResult.isEmpty()) {
            if (checkIfThereIsntAnySignAndIfItIsPossibleToAdd(prevResult, smallPrevResult, lengthSPR)) {
                smallTextResult.setText(prevResult + DIV);
                textResult.setText("");
            }
            if (ifLastSignIsDifferentThanDivisionSign(smallPrevResult, lengthSPR)) {
                String tmp = smallPrevResult.substring(0, lengthSPR - 1);
                smallTextResult.setText(tmp + DIV);
            }
        } else {
            if (!prevResult.isEmpty()) {
                smallTextResult.setText(prevResult + DIV);
                textResult.setText("");
            }
        }
    }

    private boolean ifLastSignIsDifferentThanDivisionSign(String smallPrevResult, int lengthSPR){
        return smallPrevResult.charAt(lengthSPR - 1) == PLUS
                || smallPrevResult.charAt(lengthSPR - 1) == MINUS
                || smallPrevResult.charAt(lengthSPR - 1) == MULTI;
    }

    private void buttonEqualsSignPressed(String prevResult, String smallPrevResult, int lengthSPR){
        if (!smallPrevResult.isEmpty() && !prevResult.isEmpty()) {
            String num1String = smallPrevResult.substring(0, lengthSPR - 1);
            Float num1 = ERROR_CODE;
            Float num2 = NEUTRAL_NUMBER;
            try {
                num1 = Float.parseFloat(num1String);
                num2 = Float.parseFloat(prevResult);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            if (smallPrevResult.charAt(lengthSPR - 1) == PLUS) {
                smallTextResult.setText("");
                Float result = num1 + num2;
                textResult.setText(result.toString());
            }
            if (smallPrevResult.charAt(lengthSPR - 1) == MINUS) {
                smallTextResult.setText("");
                Float result = num1 - num2;
                textResult.setText(result.toString());
            }
            if (smallPrevResult.charAt(lengthSPR - 1) == MULTI) {
                smallTextResult.setText("");
                Float result = num1 * num2;
                textResult.setText(result.toString());
            }
            if (smallPrevResult.charAt(lengthSPR - 1) == DIV) {
                smallTextResult.setText("");
                if (!ZERO.equals(num2)) {
                    Float result = num1 / num2;
                    textResult.setText(result.toString());
                } else {
                    textResult.setText(CANTDIVIDEBY0);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            textResult.setText("");
                            smallTextResult.setText("");
                        }
                    }, WAITING_TIME);

                }
            }
        }
    }

}
