package com.example.weightchecker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private EditText weightTextView;
    private EditText msgTextView;


    private FrameLayout frmLayout;
    private RelativeLayout rootLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //frmLayout = findViewById(R.id.frmLayout);
        rootLayout = findViewById(R.id.rootLayout);
        weightTextView = findViewById(R.id.editText1);
        msgTextView = findViewById(R.id.editText2);
        frmLayout = findViewById(R.id.frmLayout);
        // Get a reference to the "sensorData" path in the Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("WeightCapacity").child("Weight").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Retrieve the alcohol value from the dataSnapshot

                frmLayout.setBackgroundColor(getResources().getColor(R.color.yellow));
                String weightValue = dataSnapshot.child("Weight").getValue(String.class);
                System.out.println(weightValue);
                // Retrieve the food status value from the dataSnapshot

                double value = Double.parseDouble(weightValue);
                String capacityStatus;
                msgTextView.setTextColor(getResources().getColor(R.color.black));
                Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.purple_700)));
                if(value < 100)
                {
                    capacityStatus= "Weight is allowed";
                    Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.red)));
                    msgTextView.setTextColor(getResources().getColor(R.color.blue));

                }
                else
                {
                   capacityStatus= "Weight is not allowed!!!";
                    Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.purple_700)));
                    msgTextView.setTextColor(getResources().getColor(R.color.black));
                   // rootLayout.setBackgroundColor(getResources().getColor(R.color.red));
                    rootLayout.setBackgroundResource(R.drawable.border);
                }
                weightTextView.setTextColor(getResources().getColor(R.color.black));
                // Update the edittextViews with the retrieved values
                weightTextView.setText(weightValue);
                msgTextView.setText( capacityStatus);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that occur during the data retrieval

            }
        });
    }
}