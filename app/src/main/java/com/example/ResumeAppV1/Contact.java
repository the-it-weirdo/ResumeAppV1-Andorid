package com.example.ResumeAppV1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.net.InetAddress;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Contact.OnContactFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Contact#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Contact extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

    //private String mParam1;
    //private String mParam2;

    static final String numberKey = "NUMBER";

    private OnContactFragmentInteractionListener mListener;

    public Contact() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Contact.
     */
    // TODO: Rename and change types and number of parameters
    public static Contact newInstance(String param1, String param2) {
        Contact fragment = new Contact();
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(getActivity().getResources().getString(R.string.contactLabel));

        ImageButton callButton = (ImageButton) getView().findViewById(R.id.phoneImageButton);
        ImageButton mailButton = (ImageButton) getView().findViewById(R.id.mailImageButton);
        ImageButton smsButton = (ImageButton) getView().findViewById(R.id.messageImageButton);
        ImageButton webButton = (ImageButton) getView().findViewById(R.id.webImageButton);

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCallButtonPressed(Uri.parse("tel:7478755667"));
            }
        });

        mailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMailButtonPressed();
            }
        });

        smsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(numberKey, "7478755667");
                onSmsButtonPressed(bundle);
            }
        });

        final String link = getActivity().getResources().getString(R.string.blogLink);
        webButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onWebButtonPressed(link);
            }
        });
    }


    private void onCallButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onCallButtonPressed(uri);
        }
    }

    private void onMailButtonPressed() {
        if (mListener != null) {
            mListener.onMailButtonPressed();
        }
    }

    private void onSmsButtonPressed(Bundle bundle) {
        if (mListener != null) {
            mListener.onSmsButtonPressed(bundle);
        }
    }

    private void onWebButtonPressed(String url) {
        if (mListener != null) {
            if (isInternetConnected()) {
                mListener.onWebButtonPressed(url);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.noInternetMessage).setTitle(R.string.noInternetTitle);
                builder.setCancelable(false);
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                //mListener.internetNotConnected();
            }
        }
    }

    private boolean isInternetConnected() {
        if(getActivity() != null)
        {
            return DetectConnection.checkInternetConnection(getActivity());
        }
        else
        {
            return false;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnContactFragmentInteractionListener) {
            mListener = (OnContactFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnContactFragmentInteractionListener {
        // TODO: Update argument type and name
        void onCallButtonPressed(Uri uri);

        void onMailButtonPressed();

        void onSmsButtonPressed(Bundle bundle);

        void onWebButtonPressed(String url);

    }
}
