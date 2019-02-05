package com.example.gowthamprasath.kutty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

public class welcome extends AppCompatActivity {
    float intx=0.0f,inty=0.0f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action=event.getAction();

            switch(action){
                case MotionEvent.ACTION_DOWN:
                    intx=  event.getX();
                    inty= event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    float finx,finy;
                    finx=(int)event.getX();
                    finy= (int) event.getY();
                    if(intx<finx && inty<finy){
                        Intent intent=new Intent(this,mainpage.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }


            }
        return super.onTouchEvent(event);
    }
}
