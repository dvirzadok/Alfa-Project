package com.example.alfaversion;

import static android.icu.text.DisplayOptions.DisplayLength.LENGTH_SHORT;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FireBaseFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    EditText etName, etAge;
    TextView tvData;
    DatabaseReference ref;
    FirebaseDatabase DBref;
    private String mParam1;
    private String mParam2;
    public String name;
    int age;
    Button btnAdd, btnDelete, btnShow;


    public static FireBaseFragment newInstance(String param1, String param2) {
        FireBaseFragment fragment = new FireBaseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FireBaseFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fire_base, container, false);

        etName = view.findViewById(R.id.etName);
        etAge = view.findViewById(R.id.etAge);
        tvData = view.findViewById(R.id.tvData);
        DBref = FirebaseDatabase.getInstance();
        ref = DBref.getReference("person");
        btnAdd = view.findViewById(R.id.btnAdd);
        btnShow = view.findViewById(R.id.btnShow);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllData();
            }

        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeToFB();
            }

        });

        btnDelete = view.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteName();
            }
            });

        return view;
    }


    public boolean writeToFB() {

        Person person = new Person(etName.getText().toString(),
                Integer.parseInt(etAge.getText().toString()));

    ref.child(person.getName()).setValue(person);
        Toast.makeText(getContext(), "written", Toast.LENGTH_SHORT).show();
        return true;
    }

    public boolean deleteName(){

        ref.child(etName.getText().toString()).removeValue();

        return true;
    }

    public void showAllData(){
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Person> personList = new ArrayList<>();
                ArrayList<String> StringList = new ArrayList<>();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Person person = dataSnapshot.getValue(Person.class);
                    personList.add(person);
                    //StringList.add(dataSnapshot.getKey());
                }
                String text = "";
                for (int i = 0; i < personList.size(); i++){
                    text += "name: " + personList.get(i).getName();
                    text += " age: " + personList.get(i).getAge();
                    text += "\n";
                }
                tvData.setText(text);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}