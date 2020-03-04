package ua.prog.kiev.anketa;

import java.util.*;

public class Survey {

    private final String name;
    private final Map<String, Question> questions;
    private final Map<String, AnswersStat> stats;

    public Survey(String name, Question... questions) {
        this.name = name;
        this.questions = new LinkedHashMap<>();
        this.stats = new LinkedHashMap<>();
        for (Question question : questions) {
            this.questions.put(question.question(), question);
            this.stats.put(question.question(), new AnswersStat(question));
        }
    }

    public Map<String, AnswersStat> add(Anketa anketa) {
        Map<String, String> answers = anketa.answers();
        for (Map.Entry<String, String> questionAnswer : answers.entrySet()) {
            Question question = questions.get(questionAnswer.getKey());
            if (question != null && question.valid(questionAnswer.getValue())) {
                stats.get(questionAnswer.getKey()).add(questionAnswer.getValue());
            }
        }
        return Collections.unmodifiableMap(this.stats);
    }

    public List<Question> questions() {
        return new ArrayList<>(this.questions.values());
    }

    public String name() {
        return this.name;
    }

}
