package com.example.memorycompanion;

import android.content.Context;
import android.icu.text.SymbolTable;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class QuestionHandler
{

    private QuestionHandler()
    {
        loadQuestionNodes();
    }

    public int addQuestionToArray(String categories, String question, String answer)
    {
        int writeIndex = -1;
        if(writableIndexes.size() <= 0)
        {
            System.out.println("No Free Space In Array!");
            //Consider doing this before it goes empty with a thread.
            //That way it does not hold up the app with larger arrays
            //Do not remove the extendArraySize function without accounting for writableIndexs needing to be repopulated.
            extendArraySize();
            System.out.println("Added Ten Additional Slots");
        }

        QuestionNode newQuestion = constructQuestionNode(categories, question, answer);

        //Write Node to first index in list
        //Removes the index from list.
        if(newQuestion != null)
        {
            writeIndex = writableIndexes.remove(0);
            if (questions[writeIndex].writable)
            {
                newQuestion.index = writeIndex;
                questions[writeIndex] = newQuestion;
            } else
            {
                System.out.println("ERROR! Attempting to write over existing data!");
                writeIndex = -1;
            }
            System.out.println(writableIndexes.size() + " Slots Remain Free");
        }

        return writeIndex;
    }

    public boolean replaceNodeAtIndex(String categories, String question, String answer, int index)
    {
        boolean success = false;
        QuestionNode replaceQuestion = constructQuestionNode(categories, question, answer);
        if (replaceQuestion != null)
        {
            replaceQuestion.index = index;
            questions[index] = replaceQuestion;
            success = true;
        }
        return success;
    }

    //WARNING: The resulting question does not set the index or add itself to the questionNode array.
    private QuestionNode constructQuestionNode(String categories, String question, String answer)
    {
        QuestionNode returnQuestion = null;
        if(categories.length() > 0)
        {
            //Make sure all cats in the list are valid.
            List<String> validCats = new ArrayList<>();
            List<String> tempCats = Arrays.asList(categories.split(","));
            for (int j = 0; j < tempCats.size(); j++)
            {
                String cat = tempCats.get(j).trim();
                //For loop ensure capitalization of first letter and lowercase for rest.
                //This extends to every word in cat
                String catCapitolized = "";
                for (String word : cat.split(" "))
                {
                    catCapitolized += word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase() + " ";
                    cat = catCapitolized.trim();
                }
                if (cat.length() > 0)
                {
                    validCats.add(cat);
                    knownCategories.add(cat);
                }
            }
            if (validCats.size() > 0 && question.trim().length() > 0 && answer.trim().length() > 0)
            {
                //Index does not get set during this call.
                returnQuestion = new QuestionNode(validCats, question, answer, 0);
            }
        }
        return returnQuestion;
    }

    public void removeQuestionFromArray(int index)
    {
        questions[index].writable = true;
        writableIndexes.add(0, index);
    }

    public QuestionNode getQuestionAtIndex(int index)
    {
        QuestionNode retNode = null;
        //Ensure not out of bounds and question exists at index.
        //writeable indicates empty slot in array.
        if (index < questions.length && !questions[index].writable)
        {
            retNode = questions[index];
        }
        return retNode;
    }

    public List<QuestionNode> getAllQuestions()
    {
        List<QuestionNode> retList = new ArrayList<>();
        for(QuestionNode qn : questions)
        {
            if(!qn.writable)
            {
                retList.add(qn);
            }
        }
        return retList;
    }

    public void saveQuestionNodes()
    {
        try
        {
            FileOutputStream fos = null;
            fos = App.getContext().openFileOutput("questionNodes.txt", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(questions);
            os.close();
            fos.close();

        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void loadQuestionNodes()
    {
        File file = App.getContext().getFileStreamPath("questionNodes.txt");
        if (file.exists())
        {
            try
            {
                FileInputStream fis = App.getContext().openFileInput("questionNodes.txt");
                ObjectInputStream ois = new ObjectInputStream(fis);
                questions = ((QuestionNode[]) ois.readObject());
                ois.close();
                fis.close();
                //This keeps track of open slots it can write questions to.
                System.out.println("Checking For Open Slots");

                for(int i = 0; i < questions.length; i++)
                {
                    if(questions[i].writable)
                    {
                        System.out.println(i);
                        writableIndexes.add(i);
                    }
                    else
                    {
                        //Collect all cats for reference
                        knownCategories.addAll(questions[i].categories);
                    }
                }

            } catch (FileNotFoundException ex) {
                System.out.println(ex);
            } catch (IOException ex) {
                System.out.println(ex);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else
        {
            //If no file is found then an array is create/populated instead.
            questions = new QuestionNode[10];
            for(int i = 0; i < 10; i++)
            {
                questions[i] = new QuestionNode(i);
                if(questions[i].writable)
                {
                    writableIndexes.add(i);
                }
            }
        }
    }

    private void extendArraySize()
    {
        int newArraySize = questions.length + 10;
        QuestionNode[] tempQuestions = new QuestionNode[newArraySize];

        System.arraycopy(questions, 0, tempQuestions, 0, questions.length);
        questions = new QuestionNode[newArraySize];
        System.arraycopy(tempQuestions, 0, questions, 0, questions.length);
        for(int i = questions.length - 10; i < questions.length; i++)
        {
            questions[i] = new QuestionNode(i);
            writableIndexes.add(i);
        }
        saveQuestionNodes();
    }

    public void reduceArraySize()
    {
        if(writableIndexes.size() > 20)
        {

            System.out.println("Reducing Array Size");
            // It would be better to have 10 or more slots freed up at a time.
            // If 11 questions exist. Array size should equal 30. (30 - 11 = 19) vs (20 - 11 = 9)
            int questionCount = questions.length - writableIndexes.size();
            // (11 + 10 = 21) / 10 = 2.1
            // (ceil(2.1) = 3) * 10 = 30
            int newArraySize = (int)(Math.ceil((float)(questionCount + 10) / 10) * 10);
            System.out.println("Array Size Was: " + questions.length);
            System.out.println("New Array Size is: " + newArraySize);

            System.out.println("Current Questions Count: " + questionCount);

            writableIndexes.clear();
            QuestionNode[] tempQuestions = new QuestionNode[newArraySize];

            int index = 0;
            for(QuestionNode qn : questions)
            {
                if(!qn.writable && index < newArraySize - 1)
                {
                    qn.index = index;
                    tempQuestions[index] = qn;
                    index++;
                }
            }
            questions = new QuestionNode[newArraySize];
            System.arraycopy(tempQuestions, 0, questions, 0, questions.length);
            for(int i = questionCount; i < questions.length; i++)
            {
                if(questions[i] == null)
                {
                    questions[i] = new QuestionNode(i);
                    writableIndexes.add(i);
                }
            }
        }
        saveQuestionNodes();
    }

    public static QuestionHandler getInstance()
    {
        if (single_instance == null)
            single_instance = new QuestionHandler();

        return single_instance;
    }

    private static QuestionHandler single_instance = null;

    private QuestionNode[] questions;
    private List<Integer> writableIndexes = new ArrayList<>();
    public Set<String> knownCategories = new HashSet<>();
}
