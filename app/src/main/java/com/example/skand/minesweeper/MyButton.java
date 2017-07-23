package com.example.skand.minesweeper;

import android.content.Context;
import android.widget.Button;

/**
 * Created by Skand on 1/31/2017.
 */

public class MyButton extends Button {

    private boolean isBomb;
    private boolean isRevealed;
    private  boolean isFlag;
    private  int bombCount;
    private int i;
    private int j;

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public boolean isBomb() {
        return isBomb;
    }

    public boolean isFlag() {
        return isFlag;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public int getBombCount() {
        return bombCount;
    }

    public void setBomb(boolean bomb) {
        isBomb = bomb;
    }

    public void setBombCount(int bombCount) {
        this.bombCount = bombCount;
    }

    public void setFlag(boolean flag) {
        if(flag)
        {
            isFlag = flag;
            this.setText("!");
        }
        else
        {
            isFlag = flag;
            this.setText("");
        }
    }

    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
        setColor();
    }
    public MyButton(Context context) {
        super(context);
        isBomb=false;
        isFlag=false;
        isRevealed=false;
        bombCount=0;
        setColor();
    }
    public void setColor()
    {
        if(isRevealed==true)
            this.setBackgroundResource(R.color.reveal_blue);
        else
            this.setBackgroundResource(R.color.blue);
    }

}
