package ir.mosata.woil;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.veer.hiddenshot.HiddenShot;

import java.util.Random;

import static ir.mosata.woil.MainActivity.newrec;
import static ir.mosata.woil.SetLevelFragment.gameLevel;

public class GameFragmentHard extends Fragment {

    private Button leftNumber, rightNumber, leftestNumber, rightestNumber, see_best, equal;
    private TextView score, level, machTime, ai_dialog;
    private ImageView shareRecord, saveRecord, partyPoppers, watermark;
    private LinearLayout saveAndShareRecordBar;
    private Bitmap shot;

    private int leftestNumberInt;
    private int leftNumberInt;
    private int rightNumberInt;
    private int rightestNumberInt;
    private int levelInt = 1;
    private int scoreInt = 0;
    private int timeOfGame;
    private int trueResponse;
    private int falseResponse;
    private int protectTime = 180;
    private boolean gameInProgress;
    private boolean gameIsStart = false;
    private boolean actionPermission = true;
    private long blockTime = 0;

    // private final int MAX_LEVEL=20;
    private final int LEFTEST_BUTTON = 1;
    private final int LEFT_BUTTON = 2;
    private final int RIGHT_BUTTON = 3;
    private final int RIGHTEST_BUTTON = 4;
    private final int EQUAL = 0;
    private int STORAGE_PERMISSION_CODE=1;

    Toast blockCheater;

    CountDownTimer countDownTimer, antiCheat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_hard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MainActivity.letsgo.seekTo(0);
        MainActivity.letsgo.start();

        timeOfGame = SetTimeFragment.gameTime;
        Log.d("TAG", "Time of Game : " + timeOfGame);

        if (timeOfGame == 60) {
            trueResponse = 10 * gameLevel;
            falseResponse = 5 * gameLevel;
        } else if (timeOfGame == 30) {
            trueResponse = 20 * gameLevel;
            falseResponse = 10 * gameLevel;
        } else if (timeOfGame == 10) {
            trueResponse = 60 * gameLevel;
            falseResponse = 30 * gameLevel;
        } else if (timeOfGame == 5) {
            trueResponse = 120 * gameLevel;
            falseResponse = 60 * gameLevel;
        } else {
            return;
        }

        countDownTimer = new CountDownTimer(timeOfGame * 1000, 1000) {
            @Override
            public void onTick(long remainingTime) {
                Log.d("timer", "onTick");
                level.setText(getString(R.string.remaining_time, (int) (remainingTime / 1000)));

            }

            @Override
            public void onFinish() {
                Log.d("timer", "onFinish");
                gameInProgress = false;
                level.setText(getString(R.string.finish));
                ai_dialog.setText(getString(R.string.do_not_be_tired));
                ai_dialog.setTextColor(getResources().getColor(R.color.teal_700));

                MainActivity.bkgame.stop();
                MainActivity.comp.start();

                leftestNumber.setEnabled(false);
                leftNumber.setEnabled(false);
                rightNumber.setEnabled(false);
                rightestNumber.setEnabled(false);
                equal.setEnabled(false);

                updateHighScore();
                saveAndShareRecordBar.setVisibility(View.VISIBLE);

                see_best.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager mm = getActivity().getSupportFragmentManager();
                        MainActivity.bell.start();
                        mm.popBackStack();
                        BestScoreFragment bestScoreFragment = new BestScoreFragment();
                        getFragmentManager().beginTransaction()
                                .add(R.id.fragment_container, bestScoreFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                });

                shareRecord.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shot = takeShot();

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text_message));
                        String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), shot, getString(R.string.share_record_intent_title), null);
                        Uri screenshotUri = Uri.parse(path);

                        intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                        intent.setType("image/*");
                        startActivity(Intent.createChooser(intent, getString(R.string.share_record_tab_dialog)));
                    }
                });
                saveRecord.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shot = takeShot();

//                        Bitmap myShot = addWaterMark(shot);
//                        HiddenShot.getInstance().saveShot(getActivity(), myShot, "view");

                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            HiddenShot.getInstance().saveShot(getActivity(), shot, "view");
                            Toast.makeText(v.getContext(), getString(R.string.alert_save_record), Toast.LENGTH_SHORT).show();
                        } else {
                            requestStoragePermission();
                        }

                    }
                });

            }
        };


        findViews(view);
        configureViews();
        generateNumber();

        machTime.setText(getString(R.string.subtitle_challenge_mode_text) + " " + String.valueOf(timeOfGame) + " " + getString(R.string.subtitle_challenge_mode_time) + " " + getString(R.string.hard));


        super.onViewCreated(view, savedInstanceState);
    }

    private Bitmap takeShot() {
        watermark.setVisibility(View.VISIBLE);
        saveAndShareRecordBar.setVisibility(View.GONE);
        if (partyPoppers.getVisibility() == View.VISIBLE) {
            partyPoppers.setVisibility(View.GONE);
            shot = HiddenShot.getInstance().buildShot(getActivity());
            partyPoppers.setVisibility(View.VISIBLE);
        } else {
            shot = HiddenShot.getInstance().buildShot(getActivity());
        }
        saveAndShareRecordBar.setVisibility(View.VISIBLE);
        watermark.setVisibility(View.GONE);
        return shot;
    }

    private void updateHighScore() {
        int previousHighScore = MyPreferenceManager.getInstance(getActivity()).getHighScore();
        Log.d("TAG", "current high score" + previousHighScore);
        if (scoreInt > previousHighScore) {
            MyPreferenceManager.getInstance(getActivity()).putHighScore(scoreInt);
            ai_dialog.setText(getString(R.string.new_best_record));
            ai_dialog.setTextColor(getResources().getColor(R.color.purple_500));

            newrec.start();

            Animation animShow = AnimationUtils.loadAnimation(getContext(), R.anim.view_show);
            Animation animHide = AnimationUtils.loadAnimation(getContext(), R.anim.view_hide);

            partyPoppers.setVisibility(View.VISIBLE);
            partyPoppers.startAnimation(animShow);
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    partyPoppers.startAnimation(animHide);
                    partyPoppers.setVisibility(View.GONE);
                }
            }, 6000);

        } else {
            ai_dialog.setText(getString(R.string.do_not_be_tired));
            ai_dialog.setTextColor(getResources().getColor(R.color.teal_700));
        }
    }

    private void findViews(View view) {
        leftestNumber = (Button) view.findViewById(R.id.leftestNumber);
        leftNumber = (Button) view.findViewById(R.id.leftNumber);
        rightNumber = (Button) view.findViewById(R.id.rightNumber);
        rightestNumber = (Button) view.findViewById(R.id.rightestNumber);
        equal = (Button) view.findViewById(R.id.equal);
        see_best = (Button) view.findViewById(R.id.see_best);
        score = (TextView) view.findViewById(R.id.score);
        level = (TextView) view.findViewById(R.id.level);
        machTime = (TextView) view.findViewById(R.id.mach_time);
        ai_dialog = (TextView) view.findViewById(R.id.ai_dialog);
        shareRecord = (ImageView) view.findViewById(R.id.share_from_device);
        saveRecord = (ImageView) view.findViewById(R.id.save_on_device);
        saveAndShareRecordBar = (LinearLayout) view.findViewById(R.id.save_and_share_record);
        partyPoppers = (ImageView) view.findViewById(R.id.party_poppers);
        watermark = (ImageView) view.findViewById(R.id.watermark);
    }

    private void configureViews() {
        countDownTimer.start();
        gameInProgress = true;
        leftestNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionPermission == true) {
                    evaluateAndContinue(1);
                    antiSpam();
                } else {
                    ai_dialog.setText(getString(R.string.cheater_alert));
                    ai_dialog.setTextColor(getResources().getColor(R.color.high_light));
                    MainActivity.bpre.start();
                    // Hard anti cheating!
                    antiCheat.cancel();
                    antiCheat.start();
                    //blockCheater = Toast.makeText(getActivity(),getString(R.string.cheater_alert),Toast.LENGTH_SHORT);
                    //blockCheater.show();
                }
            }
        });
        leftNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionPermission == true) {
                    evaluateAndContinue(2);
                    antiSpam();
                } else {
                    ai_dialog.setText(getString(R.string.cheater_alert));
                    ai_dialog.setTextColor(getResources().getColor(R.color.high_light));
                    MainActivity.bpre.start();
                    // Hard anti cheating!
                    antiCheat.cancel();
                    antiCheat.start();
                    //blockCheater = Toast.makeText(getActivity(),getString(R.string.cheater_alert),Toast.LENGTH_SHORT);
                    //blockCheater.show();
                }
            }
        });
        rightNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionPermission == true) {
                    evaluateAndContinue(3);
                    antiSpam();
                } else {
                    ai_dialog.setText(getString(R.string.cheater_alert));
                    ai_dialog.setTextColor(getResources().getColor(R.color.high_light));
                    MainActivity.bpre.start();
                    // Hard anti cheating!
                    antiCheat.cancel();
                    antiCheat.start();
                    //blockCheater = Toast.makeText(getActivity(),getString(R.string.cheater_alert),Toast.LENGTH_SHORT);
                    //blockCheater.show();
                }
            }
        });
        rightestNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionPermission == true) {
                    evaluateAndContinue(4);
                    antiSpam();
                } else {
                    ai_dialog.setText(getString(R.string.cheater_alert));
                    ai_dialog.setTextColor(getResources().getColor(R.color.high_light));
                    MainActivity.bpre.start();
                    // Hard anti cheating!
                    antiCheat.cancel();
                    antiCheat.start();
                    //blockCheater = Toast.makeText(getActivity(),getString(R.string.cheater_alert),Toast.LENGTH_SHORT);
                    //blockCheater.show();
                }
            }
        });
        equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionPermission == true) {
                    evaluateAndContinue(0);
                    antiSpam();
                } else {
                    ai_dialog.setTextColor(getResources().getColor(R.color.high_light));
                    ai_dialog.setText(getString(R.string.cheater_alert));
                    MainActivity.bpre.start();
                    // Hard anti cheating!
                    antiCheat.cancel();
                    antiCheat.start();
                    //blockCheater = Toast.makeText(getActivity(),getString(R.string.cheater_alert),Toast.LENGTH_SHORT);
                    //blockCheater.show();

                    //ai_dialog.setText(getString(R.string.cheater_unblock_alert));
                    //ai_dialog.setTextColor(getResources().getColor(R.color.red));
                }
            }
        });
    }

    private int randomInt() {
        Random myNumber = new Random();
        Integer myInt = myNumber.nextInt();
        if (myInt < 0) {
            myInt *= -1;
        }
        myInt %= 90;
        myInt += 10;
        return myInt;
    }

    private void generateNumber() {
        levelInt++;
        leftestNumberInt = randomInt();
        leftNumberInt = randomInt();
        rightNumberInt = randomInt();
        rightestNumberInt = randomInt();
        leftestNumber.setText(String.valueOf(leftestNumberInt));
        leftNumber.setText(String.valueOf(leftNumberInt));
        rightNumber.setText(String.valueOf(rightNumberInt));
        rightestNumber.setText(String.valueOf(rightestNumberInt));
    }

    private void evaluateAndContinue(int whichPressed) {
        if (gameInProgress == false) {
            return;
        }
        evaluate(whichPressed);
        score.setText(getString(R.string.score, scoreInt));
        generateNumber();
    }

    private void evaluate(int whatPressed) {
        String[] trueBank = getResources().getStringArray(R.array.true_dialog);
        String[] falseBank = getResources().getStringArray(R.array.false_dialog);

        boolean verge = (leftNumberInt == rightNumberInt && leftestNumberInt == rightestNumberInt && leftestNumberInt == rightNumberInt);
        if (whatPressed == LEFTEST_BUTTON) {
            if (leftestNumberInt >= rightNumberInt && leftestNumberInt >= rightestNumberInt && leftestNumberInt >= leftNumberInt && !verge) {
                scoreInt += trueResponse;
                ai_dialog.setTextColor(getResources().getColor(R.color.green));
                ai_dialog.setText(trueBank[new Random().nextInt(trueBank.length)]);
                MainActivity.mepo.start();
            } else {
                if (scoreInt != 0) {
                    scoreInt -= falseResponse;
                }
                ai_dialog.setTextColor(getResources().getColor(R.color.red));
                ai_dialog.setText(falseBank[new Random().nextInt(falseBank.length)]);
                MainActivity.brel.start();
            }
        } else if (whatPressed == LEFT_BUTTON) {
            if (leftNumberInt >= rightNumberInt && leftNumberInt >= rightestNumberInt && leftNumberInt >= leftestNumberInt && !verge) {
                scoreInt += trueResponse;
                ai_dialog.setTextColor(getResources().getColor(R.color.green));
                ai_dialog.setText(trueBank[new Random().nextInt(trueBank.length)]);
                MainActivity.mepo.start();
            } else {
                if (scoreInt != 0) {
                    scoreInt -= falseResponse;
                }
                ai_dialog.setTextColor(getResources().getColor(R.color.red));
                ai_dialog.setText(falseBank[new Random().nextInt(falseBank.length)]);
                MainActivity.brel.start();
            }
        } else if (whatPressed == RIGHT_BUTTON) {
            if (rightNumberInt >= leftNumberInt && rightNumberInt >= leftestNumberInt && rightNumberInt >= rightestNumberInt && !verge) {
                scoreInt += trueResponse;
                ai_dialog.setTextColor(getResources().getColor(R.color.green));
                ai_dialog.setText(trueBank[new Random().nextInt(trueBank.length)]);
                MainActivity.mepo.start();
            } else {
                if (scoreInt != 0) {
                    scoreInt -= falseResponse;
                }
                ai_dialog.setTextColor(getResources().getColor(R.color.red));
                ai_dialog.setText(falseBank[new Random().nextInt(falseBank.length)]);
                MainActivity.brel.start();
            }
        } else if (whatPressed == RIGHTEST_BUTTON) {
            if (rightestNumberInt >= rightNumberInt && rightestNumberInt >= leftNumberInt && rightestNumberInt >= leftestNumberInt && !verge) {
                scoreInt += trueResponse;
                ai_dialog.setTextColor(getResources().getColor(R.color.green));
                ai_dialog.setText(trueBank[new Random().nextInt(trueBank.length)]);
                MainActivity.mepo.start();
            } else {
                if (scoreInt != 0) {
                    scoreInt -= falseResponse;
                }
                ai_dialog.setTextColor(getResources().getColor(R.color.red));
                ai_dialog.setText(falseBank[new Random().nextInt(falseBank.length)]);
                MainActivity.brel.start();
            }
        } else if (whatPressed == EQUAL) {
            if (verge) {
                scoreInt += trueResponse;
                ai_dialog.setTextColor(getResources().getColor(R.color.green));
                ai_dialog.setText(trueBank[new Random().nextInt(trueBank.length)]);
                MainActivity.mepo.start();
            } else {
                if (scoreInt != 0) {
                    scoreInt -= falseResponse;
                }
                ai_dialog.setTextColor(getResources().getColor(R.color.red));
                ai_dialog.setText(falseBank[new Random().nextInt(falseBank.length)]);
                MainActivity.brel.start();
            }
        }
    }

    private void antiSpam() {
        antiCheat = new CountDownTimer(protectTime, 1000) {
            @Override
            public void onTick(long blockTime) {
                Log.d("timer", "dis");
                actionPermission = false;
            }

            @Override
            public void onFinish() {
                Log.d("timer", "ena");
                actionPermission = true;
            }

        };
        antiCheat.start();
    }

    private void requestStoragePermission() {
        new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.permission_needed_title))
                .setMessage(getString(R.string.permission_needed_text))
                .setIcon(R.drawable.ic_round_new_releases)
                .setPositiveButton(getString(R.string.permission_needed_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                    }
                })
                .setNegativeButton(getString(R.string.permission_needed_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Toast.makeText(getView().getContext(), getString(R.string.permission_needed_else), Toast.LENGTH_SHORT).show();
                    }
                })
                .create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        MainActivity.bkmenu.pause();
//        MainActivity.letsgo.seekTo(0);
//        MainActivity.letsgo.start();
        MainActivity.bkgame.seekTo(0);
        MainActivity.bkgame.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity.bkgame.pause();
        MainActivity.letsgo.pause();
        MainActivity.bkmenu.seekTo(0);
        MainActivity.bkmenu.start();
        newrec.pause();
    }

    @Override
    public void onPause() {
        super.onPause();
        countDownTimer.cancel();
        MainActivity.bkgame.pause();
        MainActivity.bkmenu.pause();
        MainActivity.letsgo.pause();
        newrec.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.bkmenu.pause();
        MainActivity.bkgame.start();
//        MainActivity.letsgo.start();
    }
}
