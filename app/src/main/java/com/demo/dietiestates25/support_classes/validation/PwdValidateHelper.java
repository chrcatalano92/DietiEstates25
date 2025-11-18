package com.demo.dietiestates25.support_classes.validation;

public class PwdValidateHelper {
    private static final String ANY_CHARACTER = ".*";
    private static final String UPPER_CASE = "[A-Z]";
    private static final String LOWER_CASE = "[a-z]";
    private static final String NUMBER = "[\\d]";
    private static final String SPECIAL_CHARS = "[\\W_]";
    private final String pwd;

    public PwdValidateHelper(String pwd) {
        this.pwd = pwd;
    }

    private boolean searchInString(String regex) { return pwd.matches(ANY_CHARACTER + regex + ANY_CHARACTER); }

    public boolean isHasUpperCase() { return searchInString(UPPER_CASE); }

    public boolean isHasLowerCase() { return searchInString(LOWER_CASE); }

    public boolean isHasNumber() { return searchInString(NUMBER); }

    public boolean isHasLeast2SpecialCharacters() { return pwd.matches(ANY_CHARACTER + SPECIAL_CHARS + ANY_CHARACTER + SPECIAL_CHARS + ANY_CHARACTER); }

    public boolean checkAllPwdRules() { return isHasUpperCase() && isHasLowerCase() && isHasNumber() && isHasLeast2SpecialCharacters(); }
}
