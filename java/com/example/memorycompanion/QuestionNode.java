package com.example.memorycompanion;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionNode implements Serializable
{
    QuestionNode(int i)
    {
        question = new ArrayList<>();
        answer   = "";
        writable = true;
        index = i;
    }
    //As a way to save load time:
    //Questions answer correctly after x times will be archived if permited to do so.
    //After a period of time, the question will be added back in again.
    //If it is answered correctly it can either be delected or recarchived.
    QuestionNode(List<String> C, String Q, String A, int i)
    {
        categories = C;
        question = Arrays.asList(Q.split(" "));
        answer   = A;
        dateCreated = System.currentTimeMillis();
        dateAsked = dateCreated;
        writable = false;
        index = i;
    }

    List<String> question;
    String answer;

    List<String> categories = new ArrayList<>();

    int correct = 0;
    int wrong   = 0;
    int streak  = 0;
    long dateCreated;
    long dateAsked;
    boolean writable;
    int index;
}
