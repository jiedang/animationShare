package com.race604.ticker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;

import com.example.dangjie.animationtest.R;
import com.race604.animation.Valuable;
import com.race604.utils.UiHelper;

public class TicketShowView extends View {
    private Drawable bg,ticket;
    private int w,h,ticketW,ticketH,ticketL,ticketR,ticketT,ticketB ;
    public TicketShowView(Context context) {
        super(context);
        init(context);
    }

    public TicketShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
            h = w = UiHelper.dp2px(context, 110);
            bg =  getResources().getDrawable(R.drawable.ticket_show_bg);
            bg.setBounds(0, 0, w, h);

            ticket = getResources().getDrawable(R.drawable.ticket_show_ticket);
            ticketW = UiHelper.dp2px(context,60);
            ticketH = UiHelper.dp2px(context,50);
            ticketL = (w - ticketW)/2;
            ticketR = ticketL + ticketW;
            ticketT = UiHelper.dp2px(context,46);
            ticketB = ticketT + ticketH;
            ticket.setBounds(ticketL,ticketT,ticketR,ticketB);
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        float value = getValue();

        if(value <= 100){
            float currentHeight = value  / 100 * ticketH;
            canvas.save();
            canvas.translate(0,currentHeight - ticketH);
            canvas.clipRect(ticketL, ticketT + ticketH - currentHeight,ticketR,ticketB);
            ticket.draw(canvas);
            canvas.restore();
        }

    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        bg.draw(canvas);
    }
    private float getValue() {
        Animation anim = getAnimation();
        if (anim != null) {
            return ((Valuable) anim).getValue();
        }
        return 0;
    }

}
