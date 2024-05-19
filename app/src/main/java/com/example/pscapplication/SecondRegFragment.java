package com.example.pscapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SecondRegFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecondRegFragment extends Fragment {

    EditText etRegContactPerson, etRegCompName, etRegEmail, etRegPhoneNum;
    View v;
    RegistrationActivity registrationActivity;
    String contact_person, company_name, email, phone_num;
    Button btnGoReg;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SecondRegFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SecondRegFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SecondRegFragment newInstance(String param1, String param2) {
        SecondRegFragment fragment = new SecondRegFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_second_reg, container, false);
        registrationActivity = (RegistrationActivity) getActivity();
        btnGoReg = v.findViewById(R.id.btnoFinishReg);
        etRegCompName = v.findViewById(R.id.etRegCompanyName);
        etRegContactPerson = v.findViewById(R.id.etRegFullNameContact);
        etRegEmail = v.findViewById(R.id.etRegEmail);
        etRegPhoneNum = v.findViewById(R.id.etRegPhoneNumContact);

        btnGoReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                company_name = String.valueOf(etRegCompName.getText());
                contact_person = String.valueOf(etRegContactPerson.getText());
                email = String.valueOf(etRegEmail.getText());
                phone_num = String.valueOf(etRegPhoneNum.getText());
                Log.e("CompName", company_name);
                Log.e("ContactPerson", contact_person);
                Log.e("Email", email);
                Log.e("PhoneNum", phone_num);

                if (company_name.isEmpty() || contact_person.isEmpty() || email.isEmpty() || phone_num.isEmpty()) {
                    registrationActivity.notAllFieldsAreFilledDialog();
                }
                else {
                    registrationActivity.setCompName(company_name);
                    registrationActivity.setContactPerson(contact_person);
                    registrationActivity.setEmail(email);
                    registrationActivity.setPhoneNum(phone_num);
                    registrationActivity.goReg();
                }
            }
        });
        return v;
    }
}