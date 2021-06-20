package com.example.memorycompanion;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class FilterHandler
{
    QuestionHandler questionHandler = QuestionHandler.getInstance();
    static Filter selectedFilter = null;
    List<Filter> filters = new ArrayList<>();

    private FilterHandler(){}

    //TODO: Auto capitolize categories
    boolean createNewFilter(String title, String[] dateRange, int[] correctPRange, String inclCats,
                            String exclCats, String inclKeywords, String exclKeywords)
    {
        boolean validFilter = false;
        Filter filter;

        //Get the title
        List<String> obtainedList = stringValidator(title, true);
        if(obtainedList.size() > 0) { title = obtainedList.get(0); }        //Get the title
        List<String> includeCats = new ArrayList<>();
        if(inclCats.length() > 0)
        {
            includeCats = Arrays.asList(inclCats.split(","));
        }
        List<String> excludeCats = new ArrayList<>();
        if(exclCats.length() > 0)
        {
            excludeCats = Arrays.asList(exclCats.split(","));
        }

        List<String> includeKeywords = stringValidator(inclKeywords,false);
        List<String> excludeKeywords = stringValidator(exclKeywords,false);

        long minDate = convertTextDateToLongDate(dateRange[0]);
        long maxDate = convertTextDateToLongDate(dateRange[1]);
        long[] dateArray = {minDate,maxDate};

        if(title.length() > 0)
        {
            filter = new Filter(title, dateArray, correctPRange, includeCats, excludeCats, includeKeywords, excludeKeywords);
            filters.add(filter);
            indexQuestionsToFilter(filter);
            validFilter = true;
        }

        return  validFilter;
    }

    //Used to validate / organize text in filter specifications
    private ArrayList<String> stringValidator(String str, boolean isTitle)
    {
        ArrayList<String> returnList = new ArrayList<>();
        if(str.length() > 0)
        {
            //If it is a title this ensures it capitolizes all words
            //If not, then the string must be filter speicifications
            if(isTitle)
            {
                String[] tempArr = str.split(" ");
                StringBuilder newTitle = new StringBuilder();
                for(int i = 0; i < tempArr.length; i++)
                {
                    tempArr[i] = tempArr[i].substring(0,1).toUpperCase() + tempArr[i].substring(1);
                    newTitle.append(tempArr[i] + " ");
                }
                returnList.add(newTitle.toString());
            }
            else
            {
                for(int i = 0; i < str.length(); i++)
                {
                    //If a quote is found it searches for the closing quote and the text within will
                    //be taken as literal
                    if(str.charAt(i) == '"')
                    {
                        boolean validQuote = false;
                        int forwardIndex = i+2;
                        for(; forwardIndex < str.length(); forwardIndex++)
                        {
                            if(str.charAt(forwardIndex) == '"') { validQuote = true; break; }
                        }
                        if(validQuote)
                        {
                            //Set the new string to quoted text
                            //set the i index to just after the end quote
                            returnList.add(str.substring(i+1, forwardIndex).toLowerCase());
                            i = forwardIndex + 1;
                        }
                    }
                    else if(str.charAt(i) != ' ')
                    {
                        int forwardIndex = i+1;
                        for(; forwardIndex < str.length(); forwardIndex++)
                        {
                            if(str.charAt(forwardIndex) == ' ') { break; }
                        }
                        //Set the new string to quoted text
                        //set the i index to just after the end quote
                        returnList.add(str.substring(i, forwardIndex).toLowerCase());
                        i = forwardIndex;
                    }
                }
            }

        }
        return returnList;
    }

    public long convertTextDateToLongDate(String myDate)
    {
        long longDate = -1;
        if(myDate.length() == 8)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
            Date date = null;
            try
            {
                date = sdf.parse(myDate);
            } catch (ParseException e)
            {
                e.printStackTrace();
            }
            if(date != null)
            {
                longDate = date.getTime();
            }
        }
        return longDate;
    }

    public String convertLongDateToTextDate(long dateTime)
    {
        //creating Date from millisecond
        Date currentDate = new Date(dateTime);

        DateFormat df = new SimpleDateFormat("MM/dd/yy");

        //formatted value of current Date
        return df.format(currentDate);
    }

    //This will add valid questions to filter specifications
    public void indexQuestionsToFilter(Filter filter)
    {
        //Wipe all indexes of list before reindexing
        filter.questionIndexesList.clear();

        boolean addQuestion;
        for(QuestionNode question : questionHandler.getAllQuestions())
        {
            addQuestion = true;
            //Check question creation date is in range
            if(filter.dateRange[0] != -1 && filter.dateRange[1] != -1)
            {
                if(question.dateCreated < filter.dateRange[0] || question.dateCreated > filter.dateRange[1])
                {
                    System.out.println(("Skipping Question " + question.question + "Out of Date Range"));
                    addQuestion = false;
                }
            }

            //Check question accuracy is in range
            int questionAcc = 0;
            if((question.correct + question.wrong) > 0)
            {
                questionAcc = (int)((((float)question.correct) / (question.correct + question.wrong)) * 100);
                System.out.println("ACC "+questionAcc);
            }
            else
            {
                //Set accuracy to 50% if question was never answered.
                questionAcc = 50;
                System.out.println("ACC "+questionAcc);
            }

            if(addQuestion && (questionAcc < filter.correctPercentageRange[0] || questionAcc > filter.correctPercentageRange[1]))
            {
                System.out.println(("Skipping Question " + question.question + "Out of Correct Range"));
                addQuestion = false;
            }

            //Check question categories are matched in include/exclude cats
            if(addQuestion && (filter.includeCategories.size()>0 || filter.excludeCategories.size()>0))
            {
                boolean isIncluded = false;
                for (String cat : question.categories)
                {
                    if(!isIncluded && filter.includeCategories.contains(cat))
                    {
                        isIncluded = true;
                    }
                    if(filter.excludeCategories.contains(cat))
                    {
                        System.out.println(("Skipping Question " + question.question + "Excluded"));
                        addQuestion = false;
                        break;
                    }
                }
                if(filter.includeCategories.size() > 0 && !isIncluded)
                {
                    System.out.println(("Skipping Question " + question.question + "Not Included"));
                    addQuestion = false;
                }
            }

            //Add question if it matches all filters
            if(addQuestion)
            {
                System.out.println("Adding Question " + question.question);
                filter.questionIndexesList.add(question.index);
            }

        }
    }

    public void saveFilters()
    {
        try
        {
            FileOutputStream fos = null;
            fos = App.getContext().openFileOutput("questionFilters.txt", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(filters);
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

    public void loadQuestionFilters()
    {
        File file = App.getContext().getFileStreamPath("questionFilters.txt");
        if (file.exists())
        {
            try
            {
                FileInputStream fis = App.getContext().openFileInput("questionFilters.txt");
                ObjectInputStream ois = new ObjectInputStream(fis);
                filters = (List<Filter>) ois.readObject();
                ois.close();
                fis.close();

            } catch (FileNotFoundException ex) {
                System.out.println(ex);
            } catch (IOException ex) {
                System.out.println(ex);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static FilterHandler getInstance()
    {
        if (single_instance == null)
            single_instance = new FilterHandler();

        return single_instance;
    }
    private static FilterHandler single_instance = null;



}
