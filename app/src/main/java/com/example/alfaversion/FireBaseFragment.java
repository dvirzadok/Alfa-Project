package com.example.alfaversion;

import static android.icu.text.DisplayOptions.DisplayLength.LENGTH_SHORT;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FireBaseFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    EditText etName, etAge;
    DatabaseReference ref;
    FirebaseDatabase DBref;
    private String mParam1;
    private String mParam2;
    public String name;
    int age;
    Button btnAdd;


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
        DBref = FirebaseDatabase.getInstance();
        ref = DBref.getReference("person");
        btnAdd = view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeToFB();
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
}