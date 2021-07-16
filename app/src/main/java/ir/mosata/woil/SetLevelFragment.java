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

public class SetLevelFragment extends Fragment {

    public static int gameLevel=1;
    Button hard,medium,easy;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_level, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        findViews(view);

        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameLevel=4;
                FragmentManager fm = getActivity().getSupportFragmentManager();
                MainActivity.bell.start();
                fm.popBackStack();

                CountDown countDown=new CountDown();
                getFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, countDown)
                        .addToBackStack(null)
                        .commit();
            }
        });

        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameLevel=3;
                FragmentManager fm = getActivity().getSupportFragmentManager();
                MainActivity.bell.start();
                fm.popBackStack();

                CountDown countDown=new CountDown();
                getFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, countDown)
                        .addToBackStack(null)
                        .commit();
            }
        });

        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameLevel=2;
                FragmentManager fm = getActivity().getSupportFragmentManager();
                MainActivity.bell.start();
                fm.popBackStack();

                CountDown countDown=new CountDown();
                getFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, countDown)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    private void findViews(View view) {
        hard = (Button) view.findViewById(R.id.hard_game);
        medium = (Button) view.findViewById(R.id.medium_game);
        easy = (Button) view.findViewById(R.id.easy_game);
    }

}

