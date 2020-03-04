package ua.prog.kiev.anketa;

import java.util.Collections;
import java.util.Map;

public class Anketa {

    private final String firstName;
    private final String lastName;
    private final Map<String, String> answers;

    public Anketa(String firstName, String lastName, Map<String, String> answers) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.answers = answers;
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }

    public Map<String, String> answers() {
        return Collections.unmodifiableMap(answers);
    }
}
