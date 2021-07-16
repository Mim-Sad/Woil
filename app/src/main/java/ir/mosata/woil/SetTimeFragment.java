package ir.mosata.woil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class SetTimeFragment extends Fragment{

    public static int gameTime=60;
    Button s60,s30,s10,s5;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_set_time, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);
        //getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

        s60.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameTime=60;
                FragmentManager fm = getActivity().getSupportFragmentManager();
                MainActivity.bell.start();
                fm.popBackStack();
            }
        });
        s30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameTime=30;
                FragmentManager fm = getActivity().getSupportFragmentManager();
                MainActivity.bell.start();
                fm.popBackStack();
            }
        });
        s10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameTime=10;
                FragmentManager fm = getActivity().getSupportFragmentManager();
                MainActivity.bell.start();
                fm.popBackStack();
            }
        });
        s5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameTime=5;
                FragmentManager fm = getActivity().getSupportFragmentManager();
                MainActivity.bell.start();
                fm.popBackStack();;
            }
        });
    }

    private void findViews(View view) {
        s60 = (Button) view.findViewById(R.id.time60);
        s30 = (Button) view.findViewById(R.id.time30);
        s10 = (Button) view.findViewById(R.id.time10);
        s5 = (Button) view.findViewById(R.id.time5);
    }
    public SetTimeFragment() {
    }
}
