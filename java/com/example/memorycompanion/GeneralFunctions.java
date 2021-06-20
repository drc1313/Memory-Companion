package com.example.memorycompanion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeneralFunctions
{
    //Takes a string like categories and parses it to be a list.
    //As wall as other necessary changes like capitalization.
    public static List<String> stringToListConversion(String categories)
    {
        List<String> validCats = new ArrayList<>();
        if(categories.length() > 0)
        {
            //Make sure all cats in the list are valid.
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

                }
            }
        }
        return validCats;
    }
}
