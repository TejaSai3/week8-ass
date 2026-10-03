import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract class Question {
    private String id;
    private String text;
    private int points;

    public Question(String id, String text, int points) {
        this.id = id;
        this.text = text;
        this.points = points;
    }

    public String getId() {
        return id;
    }

    public int getPoints() {
        return points;
    }

    public abstract boolean evaluate(String answer);
}

class MCQQuestion extends Question {
    private String correctAnswer;

    public MCQQuestion(String id, String text, int points, String correctAnswer) {
        super(id, text, points);
        this.correctAnswer = correctAnswer;
    }

    @Override
    public boolean evaluate(String answer) {
        return correctAnswer.equalsIgnoreCase(answer.trim());
    }
}

class TrueFalseQuestion extends Question {
    private boolean correctAnswer;

    public TrueFalseQuestion(String id, String text, int points, boolean correctAnswer) {
        super(id, text, points);
        this.correctAnswer = correctAnswer;
    }

    @Override
    public boolean evaluate(String answer) {
        return Boolean.parseBoolean(answer.trim()) == correctAnswer;
    }
}

class Student {
    private String id;
    private String name;

    public Student(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class Examination {
    private String title;
    private List<Question> questions = new ArrayList<>();

    public Examination(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public List<Question> getQuestions() {
        return questions;
    }
}

class Attempt {
    private Student student;
    private Examination examination;
    private Map<Question, String> recordedAnswers = new HashMap<>();
    private boolean isSubmitted = false;

    public Attempt(Student student, Examination examination) {
        this.student = student;
        this.examination = examination;
    }

    public void recordAnswer(Question question, String answer) {
        if (isSubmitted) {
            System.out.println("Cannot change answers for a submitted examination.");
            return;
        }
        recordedAnswers.put(question, answer);
        System.out.println("Answer recorded for " + question.getId() + ".");
    }

    public void submit() {
        if (isSubmitted) {
            System.out.println("Examination already submitted.");
            return;
        }
        isSubmitted = true;
        System.out.println(examination.getTitle() + " submitted by " + student.getName() + ".");
        calculateResults();
    }

    private void calculateResults() {
        int totalScore = 0;
        int maxScore = 0;
        StringBuilder resultSummary = new StringBuilder("Result: ");

        for (Question question : examination.getQuestions()) {
            maxScore += question.getPoints();
            String studentAnswer = recordedAnswers.get(question);
            boolean isCorrect = (studentAnswer != null) && question.evaluate(studentAnswer);

            if (isCorrect) {
                totalScore += question.getPoints();
                resultSummary.append(question.getId()).append(": Correct (").append(question.getPoints()).append(" points), ");
            } else {
                resultSummary.append(question.getId()).append(": Incorrect (0 points), ");
            }
        }

        if (resultSummary.length() > 8) {
            resultSummary.setLength(resultSummary.length() - 2);
        }

        System.out.println(resultSummary.toString() + ". Total score: " + totalScore + "/" + maxScore + ".");
    }
}

public class MainOnlineExam {
    public static void main(String[] args) {
        Examination examA = new Examination("Exam A");
        Question q1 = new MCQQuestion("Question 1", "What is OOP?", 5, "C");
        Question q2 = new TrueFalseQuestion("Question 2", "Java supports multiple inheritance for classes?", 5, false);

        examA.addQuestion(q1);
        examA.addQuestion(q2);

        Student student1 = new Student("S101", "Student 1");

        System.out.println(examA.getTitle() + " started by " + student1.getName() + ".");
        Attempt attempt = new Attempt(student1, examA);

        attempt.recordAnswer(q1, "C");
        attempt.recordAnswer(q2, "true");

        attempt.submit();

        attempt.recordAnswer(q1, "A");
    }
}