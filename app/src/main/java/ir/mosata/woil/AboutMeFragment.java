package ir.mosata.woil;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AboutMeFragment extends Fragment {

    TextView easterEgg,mySite,myEmail,myNumber;
    Integer stepsForEgg=1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about_me, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);
        easterEgg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast test = Toast.makeText(getActivity(),"Shshshshshsh!", Toast.LENGTH_SHORT);
                test.show();
                if (stepsForEgg==3){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://woildev.blog.ir/post/Shshshshshsh!"));
                    startActivity(browserIntent);
                }else stepsForEgg++;
            }
        });
        mySite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://aitac.blog.ir"));
                startActivity(browserIntent);
            }
        });
        myEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "mimsadart@gmail.com"));
                    startActivity(intent);
                }catch(ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "ಠ╭╮ಠ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        myNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:09300713083"));
                startActivity(sendIntent);
            }
        });
    }

    private void findViews(View view){
        easterEgg = (TextView) view.findViewById(R.id.easter_egg);
        mySite = (TextView) view.findViewById(R.id.my_site);
        myEmail = (TextView) view.findViewById(R.id.my_email);
        myNumber = (TextView) view.findViewById(R.id.my_number);
    }
}
