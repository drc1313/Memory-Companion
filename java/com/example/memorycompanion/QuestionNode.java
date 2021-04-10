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
        question = "";
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
        question = Q;
        answer   = A;
        dateCreated = System.currentTimeMillis();
        writable = false;
        index = i;
    }

    String question;
    String answer;

    List<String> categories = new ArrayList<>();

    int correct = 0;
    int wrong   = 0;
    int streak  = 0;
    long dateCreated;
    boolean writable;
    int index;
}
