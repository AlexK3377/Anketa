package ua.prog.kiev.anketa;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SurveyResource extends HttpServlet {

    private final Survey survey;

    public SurveyResource() {
        this.survey = initSurvey();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String title = survey.name();
        String surveyTemplate = loadResource("survey.html");
        String questionTemplate = loadResource("question.html");
        String userTemplate = loadResource("user.html");
        StringBuilder form = new StringBuilder();
        form.append(String.format(userTemplate, "firstName", "FirstName"));
        form.append(String.format(userTemplate, "lastName", "LastName"));
        form.append(String.format(userTemplate, "age","Age"));
        for (Question question : survey.questions()) {
            form.append(String.format("<br/>%s", question.question()));
            for (String answer : question.answers()) {
                form.append(String.format(questionTemplate, question.question(), answer, answer));
            }
            form.append("<br/>");
        }
        form.append("<input type=\"submit\">Send Answers</input>");
        resp.getWriter().println(String.format(surveyTemplate, title, form.toString()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        Map<String, String[]> answers = req.getParameterMap();
        Map<String, String> userAnswers = new HashMap<>();
        for (Map.Entry<String, String[]> answer : answers.entrySet()) {
            userAnswers.put(answer.getKey(), answer.getValue()[0]);
        }
        Anketa anketa = new Anketa(firstName, lastName, userAnswers);
        Map<String, AnswersStat> stats = survey.add(anketa);
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, AnswersStat> statEntry : stats.entrySet()) {
            builder.append(statEntry.getKey());
            builder.append("  -->  ");
            Map<String, AtomicInteger> answersStat = statEntry.getValue().stats();
            for (Map.Entry<String, AtomicInteger> answerStat : answersStat.entrySet()) {
                builder.append(answerStat.getKey()).append(": ").append(answerStat.getValue());
                builder.append("    ");
            }
            builder.append("\n\r");
        }
        resp.getWriter().println(builder.toString());
//        List<String> answers = new ArrayList<>();
//        String firstName = req.getParameter("firstName");
//        Anketa anketa = new Anketa(
//                req.getParameter("firstName"),
//                req.getParameter("lastName"),
//                answers
//        );
//        List<AnswersStat> statList = survey.add(anketa);
//        resp.getWriter();
    }


    //    public static void main(String[] args) {
//        Question question1 = new Question("abc", "a", "b", "c");
//        Question question2 = new Question("1+1", "0", "2", "-1", "infinity");
//        Question question3 = new Question("a+b", "ab", "0", "ba");
//        Survey survey = new Survey(question1, question2, question3);
//        Map<String, String> answers1 = new HashMap<>();
//        answers1.put("abc", "a");
//        answers1.put("1+1", "2");
//        answers1.put("a+b", "ab");
//        Anketa anketa1 = new Anketa("A", "B", answers1);
//        Map<String, String> answers2 = new HashMap<>();
//        answers2.put("abc", "c");
//        answers2.put("1+1", "infinity123");
//        answers2.put("a+b", "ba");
//        Anketa anketa2 = new Anketa("D", "E", answers2);
//
//        System.out.println(survey.add(anketa1));
//        System.out.println(survey.add(anketa2));
//    }
    private Survey initSurvey() {
        Question question1 = new Question("Do you like Formula 1?", "yes", "no", "hard to say");
        Question question2 = new Question("What is your favourite team?", "Ferrari", "Mercedes", "Red Bull");
        Question question3 = new Question("Who is your favourite driver", "Lecler", "Hamilton", "Verstapen");
        return new Survey("Survey #" + System.currentTimeMillis(), question1, question2, question3);
    }

    private String loadResource(String resource) {
        StringBuilder builder = new StringBuilder();
        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(resource);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

}
