package com.example.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {
private TextView tv;
    private Button button;
    private String name;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv= (TextView) findViewById(R.id.tv);
        button= (Button) findViewById(R.id.btn);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                    method();
//            }
//
//
//        });
        getData();
    }


    private void getData() {
        Method[] methods = MainActivity.class.getDeclaredMethods();
        for (Method m:methods){
            Annota annotation = m.getAnnotation(Annota.class);
            if (annotation != null) {
                name=annotation.name1();
                id=annotation.id();
            }
        }
    }

    @Annota(name1 = "aa",id = 21)
public void method(){

    tv.setText(name+id);
}
}
