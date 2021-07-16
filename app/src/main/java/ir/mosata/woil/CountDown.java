package ir.mosata.woil;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import static ir.mosata.woil.SetLevelFragment.gameLevel;


public class CountDown extends Fragment {
    CountDownTimer countDownTimer;
    TextView count;
    String countShow;
    int intRemainingTime,showRemainingTime=3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_count_down,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        findViews(view);

        countDownTimer = new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long remainingTime) {
                Log.d("timer","onTick");
                count.setText(String.valueOf(showRemainingTime));
                showRemainingTime--;
            }

            @Override
            public void onFinish() {
                Log.d("timer","Finished");
                FragmentManager cd = getActivity().getSupportFragmentManager();
                switch (gameLevel){
                    case 2:
                        cd.popBackStack();
                        GameFragmentEasy gameFragmentEasy = new GameFragmentEasy();
                        getFragmentManager().beginTransaction()
                                .add(R.id.fragment_container, gameFragmentEasy)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case 3:
                        cd.popBackStack();
                        GameFragmentMedium gameFragmentMedium = new GameFragmentMedium();
                        getFragmentManager().beginTransaction()
                                .add(R.id.fragment_container, gameFragmentMedium)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case 4:
                        cd.popBackStack();
                        GameFragmentHard gameFragmentHard = new GameFragmentHard();
                        getFragmentManager().beginTransaction()
                                .add(R.id.fragment_container, gameFragmentHard)
                                .addToBackStack(null)
                                .commit();
                        break;
                }
            }
        };
        countDownTimer.start();


        super.onViewCreated(view, savedInstanceState);
    }

    public void findViews(View view){
        count = (TextView) view.findViewById(R.id.cont);
    }

    @Override
    public void onStart() {
        super.onStart();
        MainActivity.bkmenu.pause();
        MainActivity.countdown.seekTo(0);
        MainActivity.countdown.start();
    }

    @Override
    public void onDestroyView() {
        countDownTimer.cancel();
        MainActivity.countdown.pause();
        MainActivity.bkmenu.seekTo(0);
        MainActivity.bkmenu.start();
        super.onDestroyView();
    }

}
