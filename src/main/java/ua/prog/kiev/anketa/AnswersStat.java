package ua.prog.kiev.anketa;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AnswersStat {

    private Map<String, AtomicInteger> answersStats;

    public AnswersStat(Question question) {
        this.answersStats = new LinkedHashMap<>();
        for (String answer : question.answers()) {
            this.answersStats.put(answer, new AtomicInteger(0));
        }
    }

    public void add(String answer) {
        AtomicInteger count = answersStats.get(answer);
        if (count != null) {
            count.incrementAndGet();
        }
    }

    public Map<String, AtomicInteger> stats() {
        return Collections.unmodifiableMap(answersStats);
    }

    @Override
    public String toString() {
        return "AnswersStat{" +
                "answersStats=" + answersStats +
                '}';
    }
}
