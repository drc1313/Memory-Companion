package com.example.memorycompanion;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class QuestionViewerActivity extends AppCompatActivity
{
    QuestionHandler questionHandler = QuestionHandler.getInstance();
    QuestionFilterHandler questionFilterHandler = QuestionFilterHandler.getInstance();
    DisplayMetrics displayMetrics;
    QuestionNode selectedQuestion = null;
    Random r = new Random();
    LinearLayout questionLayout;
    TextView questionAnswerText;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_viewer);

        loadButtons();

        loadQuestionLayout();
        loadQuestion();
    }

    private void loadQuestion()
    {
        selectedQuestion = null;
        int selectedIndex = r.nextInt(questionFilterHandler.filters.get(0).questionIndexesList.size());

        System.out.println(selectedIndex);
        TextView filterName = findViewById(R.id.filterNameText);
        TextView questionText = findViewById(R.id.questionText);

        questionLayout.setTranslationX(0);
        questionLayout.setTranslationY(displayMetrics.heightPixels);
        questionLayout.setRotation(0);

        //The while loop is temporary until filters are done
        while(selectedIndex >= 0)
        {
            selectedQuestion = questionHandler.getQuestionAtIndex(questionFilterHandler.filters.get(0).questionIndexesList.get(selectedIndex));
            if(selectedQuestion != null)
            {
                filterName.setText("Filter Name");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    questionText.setText(String.join(" ", selectedQuestion.question));
                }

                //Animates the question layout to come from the bottom of the screen to the center.
                ObjectAnimator animationX = ObjectAnimator.ofFloat(questionLayout, "translationY", 0);
                animationX.setDuration(250);
                animationX.start();
                break;
            }
            selectedIndex--;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    //TODO: Add swipe up animation. This is to skip the question entirely.
    private void loadQuestionLayout()
    {
        questionLayout = findViewById(R.id.questionLayout);
        questionAnswerText = findViewById(R.id.questionText);

        questionLayout.setOnTouchListener(new View.OnTouchListener() {
            float startTouchX = 0;
            float movementX = 0;
            float movementZ = 0;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    startTouchX = motionEvent.getX();

                }
                if(motionEvent.getAction() == MotionEvent.ACTION_MOVE)
                {

                    movementX = ((startTouchX - motionEvent.getX()) ) ;
                    movementZ = ((startTouchX - motionEvent.getX())/30) ;

                    questionLayout.setTranslationX(questionLayout.getTranslationX() - movementX);
                    questionLayout.setRotation(questionLayout.getRotation() - movementZ);
                }

                if(motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    //If the card is swiped less than a certain amount it will return to its original position.
                    //Otherwise it goes off screen.
                    if(Math.abs(questionLayout.getTranslationX())<250)
                    {
                        ObjectAnimator animationX = ObjectAnimator.ofFloat(view, "translationX", 0);

                        animationX.setDuration(200);
                        animationX.start();

                        ObjectAnimator animationZ = ObjectAnimator.ofFloat(view, "rotation", view.getRotation(), 0);
                        animationZ.setDuration(200);
                        animationZ.start();
                    }
                    else
                    {
                        //If positive then it was swiped right indicating correct answer
                        float touchRelease = questionLayout.getTranslationX();
                        if(touchRelease > 0)
                        {
                            selectedQuestion.correct +=1;
                        }
                        else
                        {
                            selectedQuestion.wrong +=1;
                        }
                        System.out.println("Correct: " +selectedQuestion.correct);
                        System.out.println("Wrong: " + selectedQuestion.wrong);
                        ObjectAnimator animationX = ObjectAnimator.ofFloat(view, "translationX", touchRelease* 2.5f);

                        animationX.addListener(new Animator.AnimatorListener(){
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }
                            @Override
                            public void onAnimationEnd(Animator animation)
                            {
                                System.out.println("LOADING QUESTION");
                                loadQuestion();
                            }
                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }
                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                        animationX.setDuration(90);
                        animationX.start();
                    }

                    questionAnswerText.setText(selectedQuestion.answer);
                }
                return true;
            }
        });
    }

    private void loadButtons()
    {
        Button buttonStart = findViewById(R.id.buttonBack);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent activityIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(activityIntent);
            }
        });
    }
}

