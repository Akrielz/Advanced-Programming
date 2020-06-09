package com.example.sgbd.Controllers;

import com.example.sgbd.Database;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@RestController
@RequestMapping("/question")
public class QuizController {
    private final Database database;

    private final int TOTAL_QUESTIONS = 10;

    public QuizController() throws SQLException, ClassNotFoundException {
        database = Database.getInstance();
    }

    @PostMapping(value = "/login", consumes = "application/json")
    public int loginEmail(@RequestBody Request request) throws SQLException {
        String query = "SELECT COUNT(*) FROM emails WHERE email = '" + request.getEmail() + "' AND hash_code = '" +
                request.getHashCode() + "'";
        ResultSet resultSet = database.executeQuery(query);

        int answer = 0;
        if (resultSet.next()){
            answer = resultSet.getInt(1);
        }

        return answer;
    }

    @PostMapping(value = "/register", consumes = "application/json")
    public String registerEmail(@RequestBody Request request) throws NoSuchAlgorithmException, SQLException {
        String email = request.getEmail();

        System.out.println(email);

        String queryExist = "SELECT COUNT(*) FROM emails WHERE email = '" + email + "'";
        ResultSet resultSet = database.executeQuery(queryExist);
        int answer = 0;
        if (resultSet.next()){
            answer = resultSet.getInt(1);
        }

        System.err.println(answer);

        if (answer == 1){
            return "Already exists!";
        }

        MessageDigest md = MessageDigest.getInstance("MD5");
        String currentTime = String.valueOf(System.nanoTime());

        String input = email + currentTime;
        md.update(input.getBytes());
        byte[] digest = md.digest();

        String hashCode = DatatypeConverter.printHexBinary(digest).toUpperCase();

        String queryCnt = "SELECT COUNT(*) FROM emails";
        resultSet = database.executeQuery(queryCnt);

        int id = 0;
        if (resultSet.next()){
            id = resultSet.getInt(1);
        }

        String insert = "INSERT INTO emails(id, email, hash_code) VALUES(?,?,?)";
        PreparedStatement preparedStatement = database.getConnection().prepareStatement(insert);
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, email);
        preparedStatement.setString(3, hashCode);
        preparedStatement.execute();

        return hashCode;
    }

    @PostMapping(value = "/obj", consumes = "application/json")
    public Question getQuestion(@RequestBody Request request) throws SQLException {
        if (loginEmail(request) == 0){
            return new Question("Incorrect HashCode");
        }

        if (! hasTest(request.getEmail())) {
            createTest(request.getEmail());
            return getQuestionById(request.getEmail(), 0);
        }

        int id = getNrQuestions(request.getEmail());

        if (request.getAnswer() == null || request.getAnswer().length() == 0) {
            if (id >= TOTAL_QUESTIONS-1){
                return calculateAnswer(request);
            }
            return getQuestionById(request.getEmail(), id);
        }

        if (id < TOTAL_QUESTIONS-1){
            updateAnswer(request, id);
            return getQuestionById(request.getEmail(), id+1);
        }

        if (id == TOTAL_QUESTIONS-1){
            updateAnswer(request, id);
        }

        return calculateAnswer(request);
    }

    private void createTest(String email) throws SQLException {
        String queryDomains = "SELECT UNIQUE DOMENIU FROM INTREBARI";
        ResultSet resultSetDomains = database.executeQuery(queryDomains);

        List<String> domains = new ArrayList<>();
        while (resultSetDomains.next()){
            String domain = resultSetDomains.getString(1);
            domains.add(domain);
        }

        Collections.shuffle(domains);

        for (int i = 0; i < TOTAL_QUESTIONS; i++){
            String domain = domains.get(i);

            String queryQuestions = "SELECT ID FROM INTREBARI WHERE DOMENIU = '" + domain + "'";
            ResultSet resultSetQuestions = database.executeQuery(queryQuestions);

            List<String> questions = new ArrayList<>();
            while (resultSetQuestions.next()){
                String question = resultSetQuestions.getString(1);
                questions.add(question);
            }

            Collections.shuffle(questions);
            String question = questions.get(0);

            List <String> answersID = new ArrayList<>();

            String queryAnswers = "SELECT * FROM RASPUNSURI WHERE Q_ID = '" + question + "' AND CORECT = '1'" ;

            List <String> goodAnswers = new ArrayList<>();
            ResultSet resultSetAnswers = database.executeQuery(queryAnswers);
            if (resultSetAnswers.next()){
                String questionId = resultSetAnswers.getString(2);
                answersID.add(questionId);
                goodAnswers.add(questionId);
            }

            String queryOtherAnswers = "SELECT * FROM RASPUNSURI WHERE Q_ID = '" + question + "' AND ID != '" + answersID.get(0) + "'";
            ResultSet resultSetOthers = database.executeQuery(queryOtherAnswers);

            int j = 0;
            while (resultSetOthers.next() && j < 5){
                String questionId = resultSetOthers.getString(2);
                answersID.add(questionId);

                if (resultSetOthers.getString(4).equals("1")){
                    goodAnswers.add(questionId);
                }

                j++;
            }

            int pos = (int)(Math.random()*j);
            Collections.swap(answersID, 0, pos);

            String queryCount = "SELECT COUNT(*) FROM TESTE";
            ResultSet resultSetCount = database.executeQuery(queryCount);

            String id = "";
            if (resultSetCount.next()){
                id = "T" + String.valueOf(resultSetCount.getString(1));
            }

            String tid = "I" + String.valueOf(i);

            String queryInsert = "INSERT INTO TESTE(id, email, q_id, t_id, s_ans, r_ans) VALUES(?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = database.getConnection().prepareStatement(queryInsert);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, question);
            preparedStatement.setString(4, tid);

            String answersCSV = "";
            for (int k = 0; k < answersID.size(); k++) {
                answersCSV += answersID.get(k);
                if (k < answersID.size() - 1) {
                    answersCSV += ",";
                }
            }
            preparedStatement.setString(5, answersCSV);

            String goodCSV = "";
            for (int k = 0; k < goodAnswers.size(); k++) {
                goodCSV += goodAnswers.get(k);
                if (k < goodAnswers.size() - 1) {
                    goodCSV += ",";
                }
            }
            System.out.println(id);
            System.out.println(email);
            System.out.println(question);
            System.out.println(tid);
            System.out.println(answersCSV);
            System.out.println(goodCSV);
            System.out.println("");

            preparedStatement.setString(6, goodCSV);

            preparedStatement.execute();
        }
    }

    private int getNrQuestions(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM TESTE WHERE EMAIL = '" + email + "' AND G_ANS IS NOT NULL";
        ResultSet resultSet = database.executeQuery(query);
        if (resultSet.next()){
            return resultSet.getInt(1);
        }

        return 0;
    }

    private boolean hasTest(String email) throws SQLException {
        String hasTest = "SELECT COUNT(*) FROM TESTE WHERE EMAIL = '" + email + "'";
        ResultSet resultSet = database.executeQuery(hasTest);
        if (resultSet.next()){
            return resultSet.getInt(1) != 0;
        }
        return false;
    }

    private void updateAnswer(Request request, int id) throws SQLException {
        String query = "UPDATE TESTE SET G_ANS=? WHERE T_ID=? AND EMAIL=?";

        PreparedStatement preparedStatement = database.getConnection().prepareStatement(query);
        preparedStatement.setString(1, request.getAnswer());
        preparedStatement.setString(2, "I" + String.valueOf(id));
        preparedStatement.setString(3, request.getEmail());

        preparedStatement.execute();

        return;
    }


    private Question getQuestionById(String email, int id) throws SQLException {
        String tid = "I" + String.valueOf(id);
        String query = "SELECT * FROM TESTE WHERE EMAIL = '" + email + "' AND T_ID= '" + tid + "'";
        ResultSet resultSet = database.executeQuery(query);

        String vId;
        String vQId;
        String vTId;
        String vSAns;
        String vRAns;

        if (resultSet.next()){
            vId   = resultSet.getString(1);
            vQId  = resultSet.getString(3);
            vTId  = resultSet.getString(4);
            vSAns = resultSet.getString(5);
            vRAns = resultSet.getString(6);
        }
        else{
            return null;
        }

        query = "SELECT TEXT_INTREBARE FROM INTREBARI WHERE ID = '" + vQId + "'";
        resultSet = database.executeQuery(query);

        String questionDescription;
        if (resultSet.next()){
            questionDescription = resultSet.getString(1);
        }
        else {
            return null;
        }

        List<String> answersDescription = new ArrayList<>();
        String[] values = vSAns.split(",");
        for (String idAnswer : values){
            query = "SELECT TEXT_RASPUNS FROM RASPUNSURI WHERE ID = '" + idAnswer + "'";
            resultSet = database.executeQuery(query);
            if (resultSet.next()){
                answersDescription.add(resultSet.getString(1));
            }
        }

        Question question = new Question(vId, email, vQId, vTId, vSAns, vRAns);
        question.setAnswersDescription(answersDescription);
        question.setQuestionDescription(questionDescription);

        question.setQuestionDescription(questionDescription);

        return question;
    }


    private Question calculateAnswer(Request request) throws SQLException {
        String query = "SELECT * FROM TESTE WHERE EMAIL = '" + request.getEmail() + "'";
        ResultSet resultSet = database.executeQuery(query);

        String explanation = "";
        double score = 0;

        int nrQuestion = 0;
        while (resultSet.next()){
            nrQuestion++;
            explanation += "Q" + nrQuestion + ": ";

            String []realAnswers  = resultSet.getString(6).split(",");
            String []givenAnswers = resultSet.getString(7).split(",");
            String qId            = resultSet.getString(3);

            String queryQuestion = "SELECT text_intrebare FROM INTREBARI WHERE ID = '" + qId + "'";
            ResultSet rs = database.executeQuery(queryQuestion);
            if (rs.next()){
                explanation += rs.getString(1);
            }

            explanation += "\n    Real Answers: ";
            for (String realAnswer : realAnswers){
                String queryAnswerDescription = "SELECT text_raspuns FROM RASPUNSURI WHERE ID = '" + realAnswer + "'";
                rs = database.executeQuery(queryAnswerDescription);
                if (rs.next()){
                    explanation += "'" + rs.getString(1) + "' ";
                }
            }

            explanation += "\n    Given Answers: ";
            for (String givenAnswer : givenAnswers){
                String queryAnswerDescription = "SELECT text_raspuns FROM RASPUNSURI WHERE ID = '" + givenAnswer + "'";
                rs = database.executeQuery(queryAnswerDescription);
                if (rs.next()){
                    explanation += "'" + rs.getString(1) + "' ";
                }
                else{
                    explanation += "'Answer description not found'";
                }
            }

            explanation += "\nObtained: ";

            double localScore = 0;
            for (String search : givenAnswers){
                boolean found = false;

                for (String answer : realAnswers){
                    if (answer.equals(search)){
                        localScore += 10.0 / realAnswers.length;
                        found = true;
                        break;
                    }
                }

                if (!found){
                    localScore -= 10.0 / realAnswers.length;
                }
            }

            if (localScore < 0) {
                localScore = 0;
            }

            explanation += localScore;
            explanation += "\n";
            score += localScore;
        }

        explanation += "Total score: " + score;

        System.out.println(explanation);
        Question question = new Question(explanation);

        return question;
    }
}
