package com.belak.scheduletimetable.utils;



import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
public class EmailValidator implements Predicate<String> {
    @Override
    public boolean test(String s) {
        // TO DO : Regex to validate email
        return true;
    }
}
