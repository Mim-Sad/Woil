package ir.mosata.woil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


public class IntroFragment5 extends Fragment {

    TextView done;
    TextView back;
    ViewPager viewPager;

    public IntroFragment5() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_intro5, container, false);

        viewPager= getActivity().findViewById(R.id.view_pager);
        done=(TextView)view.findViewById(R.id.done_btn);
        back=(TextView)view.findViewById(R.id.back_btn4);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // im under the water
                MyPreferenceManager.getInstance(getActivity()).putIsFirstEntry(true);
                viewPager.setVisibility(view.INVISIBLE);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(3);
            }
        });

        return view;
    }
}