package com.Quiz.Website.Quiz.Website.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Quiz.Website.Quiz.Website.model.Question;
import com.Quiz.Website.Quiz.Website.model.Quiz;
import com.Quiz.Website.Quiz.Website.service.QuizService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @GetMapping("/select-subject")
    public String selectSubject(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username); // Fixed syntax error here
        return "quiz/select-subject";
    }

    @PostMapping("/start")
    public String startQuiz(@RequestParam String subject, HttpSession session, Model model) {
        List<Question> questions = quizService.getQuestionsBySubject(subject);
        session.setAttribute("questions", questions);
        session.setAttribute("currentIndex", 0);
        session.setAttribute("score", 0);
        session.setAttribute("skippedQuestions", new HashSet<Integer>());
        session.setAttribute("attemptedQuestions", new HashSet<Integer>());
        session.setAttribute("userAnswers", new HashMap<Integer, String>());

        System.out.println("Quiz started with questions: " + questions);

        return "redirect:/quiz/questions";
    }

    @GetMapping("/questions")
    public String showQuestion(@RequestParam(value = "index", required = false) Integer index,
                               HttpSession session, Model model) {
        @SuppressWarnings("unchecked")
        List<Question> questions = (List<Question>) session.getAttribute("questions");
        if (questions == null) {
            return "redirect:/quiz/select-subject";
        }

        Integer currentIndex = index != null ? index : (Integer) session.getAttribute("currentIndex");
        if (currentIndex >= questions.size()) {
            return "redirect:/quiz/result";
        }

        model.addAttribute("question", questions.get(currentIndex));
        model.addAttribute("questionNumber", currentIndex + 1);
        model.addAttribute("currentIndex", currentIndex);
        model.addAttribute("skippedQuestions", session.getAttribute("skippedQuestions"));
        model.addAttribute("attemptedQuestions", session.getAttribute("attemptedQuestions"));

        @SuppressWarnings("unchecked")
        Map<Integer, String> userAnswers = (Map<Integer, String>) session.getAttribute("userAnswers");
        model.addAttribute("userAnswer", userAnswers.get(currentIndex));

        session.setAttribute("currentIndex", currentIndex);

        return "quiz/questions";
    }

    @PostMapping("/questions")
    public String submitAnswer(@RequestParam(value = "answer", required = false) String answer,
                               @RequestParam(value = "skip", required = false) String skip,
                               HttpSession session) {
        Integer currentIndex = (Integer) session.getAttribute("currentIndex");
        @SuppressWarnings("unchecked")
        List<Question> questions = (List<Question>) session.getAttribute("questions");
        Integer score = (Integer) session.getAttribute("score");
        @SuppressWarnings("unchecked")
        Set<Integer> skippedQuestions = (Set<Integer>) session.getAttribute("skippedQuestions");
        @SuppressWarnings("unchecked")
        Set<Integer> attemptedQuestions = (Set<Integer>) session.getAttribute("attemptedQuestions");
        @SuppressWarnings("unchecked")
        Map<Integer, String> userAnswers = (Map<Integer, String>) session.getAttribute("userAnswers");

        System.out.println("Current index: " + currentIndex);
        System.out.println("Questions: " + questions);
        System.out.println("Score: " + score);

        if (currentIndex == null || questions == null || score == null) {
            return "redirect:/quiz/select-subject";
        }

        if (skip != null) {
            skippedQuestions.add(currentIndex);
            userAnswers.remove(currentIndex);
        } else {
            Question currentQuestion = questions.get(currentIndex);
            String previousAnswer = userAnswers.get(currentIndex);

            if (previousAnswer != null) {
                if (previousAnswer.equals(currentQuestion.getCorrectAnswer())) {
                    score -= 4;
                } else {
                    score += 1;
                }
            }

            if (currentQuestion.getCorrectAnswer().equals(answer)) {
                score += 4;
            } else {
                score -= 1;
            }

            attemptedQuestions.add(currentIndex);
            skippedQuestions.remove(currentIndex);
            userAnswers.put(currentIndex, answer);
            session.setAttribute("score", score);
        }

        session.setAttribute("currentIndex", currentIndex + 1);
        session.setAttribute("skippedQuestions", skippedQuestions);
        session.setAttribute("attemptedQuestions", attemptedQuestions);
        session.setAttribute("userAnswers", userAnswers);

        return "redirect:/quiz/questions";
    }

    @GetMapping("/result")
    public String showResult(HttpSession session, Model model) {
        Integer score = (Integer) session.getAttribute("score");
        @SuppressWarnings("unchecked")
        List<Question> questions = (List<Question>) session.getAttribute("questions");

        if (score == null || questions == null || questions.isEmpty()) {
            return "redirect:/quiz/select-subject";
        }

        String subject = questions.get(0).getSubject();
        String username = (String) session.getAttribute("username");

        Quiz quiz = new Quiz();
        quiz.setUsername(username);
        quiz.setSubject(subject);
        quiz.setScore(score);
        quiz.setQuestions(questions);

        quizService.saveQuiz(quiz);

        model.addAttribute("score", score);
        return "quiz/result";
    }
}
