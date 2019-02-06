package com.example.gowthamprasath.kutty;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class mainpage extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private Button btn,btn1;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        btn=findViewById(R.id.button);
        btn1=findViewById(R.id.button2);
        tv=findViewById(R.id.textView);
       firebaseFirestore=FirebaseFirestore.getInstance();

btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        check();
        Map<String,String> map=new HashMap<>();
        map.put("name","kutty");
        firebaseFirestore.collection("bike").document("1").set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(mainpage.this, "Success", Toast.LENGTH_SHORT).show();
            }
        });
    }
});
btn1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        firebaseFirestore.collection("bike").document("1").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    Toast.makeText(mainpage.this,documentSnapshot.getString("name"), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
});

}

    @Override
    protected void onStart() {
        super.onStart();
        check();
    }

    protected void check() {
        super.onStart();
        Runtime runtime=Runtime.getRuntime();
        try{
            Process ip=runtime.exec("/system/bin/ping - 1 8.8.8.8");
            int value=ip.waitFor();
            tv.setText(Integer.toString(value));

        }catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
/*@Override
    protected void onStart() {
        super.onStart();
        firebaseFirestore.collection("bike").document("1").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Toast.makeText(mainpage.this, documentSnapshot.getString("name"), Toast.LENGTH_SHORT).show();
            }
        });

    }*/

//local change
}
