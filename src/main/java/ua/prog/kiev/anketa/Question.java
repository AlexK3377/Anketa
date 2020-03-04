package ua.prog.kiev.anketa;

import java.util.Arrays;
import java.util.List;

public class Question {

    private String questionString;
    private List<String> answers;

    public Question(String questionString, String... answers) {
        this.questionString = questionString;
        this.answers = Arrays.asList(answers);
    }

    public String question() {
        return questionString;
    }

    public List<String> answers() {
        return answers;
    }

    public boolean valid(String answer) {
        return this.answers.contains(answer);
    }
}
