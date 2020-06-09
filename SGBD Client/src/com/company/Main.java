package com.company;

import com.google.gson.Gson;
import com.squareup.okhttp.*;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static String emailHash;
    private static boolean successful;
    private static String loggedEmail;

    Main (){
        successful = false;
    }

    public static void main(String[] args) throws IOException {
        Scanner keyboard = new Scanner(System.in);

        while(true){
            System.out.println("1. Register");
            System.out.println("2. Login");
            if (successful) {
                System.out.println("3. Continue");
            }
            System.out.println("9. Exit");

            if (successful) {
                System.out.println("\nLogged in as: " + loggedEmail);
            }
            else{
                System.out.println("\nNot logged in");
            }

            System.out.print("Your command: ");

            int nr = keyboard.nextInt();

            switch (nr){
                case 1:
                    register();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    if(successful) {
                        answer();
                    }
                    else{
                        System.out.println("You are not logged in");
                    }
                    break;
                case 9:
                    System.out.println("Goodbye");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Incorrect input");
                    break;
            }
        }
    }

    public static void answer() throws IOException {

        String computedAnswer = "";
        while (true) {

            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n\t\"email\" : \"" + loggedEmail + "\",\n\t\"hashCode\" : \"" +
                    emailHash + "\",\n\t\"answer\" : \"" + computedAnswer + "\"\n}");
            Request request = new Request.Builder()
                    .url("http://localhost:4069/question/obj")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String answer = response.body().string();
            Gson gson = new Gson();
            Question question = gson.fromJson(answer, Question.class);

            if (question.getShowAnswers() != null){
                String[] answersId = question.getShowAnswers().split(",");

                System.out.println(question.getId() + ". " + question.getQuestionDescription());

                for (int i = 0; i < 6; i++) {
                    System.out.print((i + 1) + ". ");
                    System.out.println(question.getAnswersDescription().get(i));
                }

                Scanner keyboard = new Scanner(System.in);
                String userAnswer = keyboard.nextLine();

                if (userAnswer.equals("exit")){
                    return;
                }

                computedAnswer = "";
                boolean start = true;
                String[] userAnswers = userAnswer.split(",");
                for (String singleAnswer : userAnswers) {
                    int i = Integer.parseInt(singleAnswer);
                    i--;

                    if (start) {
                        computedAnswer += answersId[i];
                        start = false;
                    } else {
                        computedAnswer += "," + answersId[i];
                    }
                }
            }
            else {
                System.out.println(question.getQuestionDescription());
                Scanner keyboard = new Scanner(System.in);
                String userAnswer = keyboard.nextLine();

                if (userAnswer.equals("exit")){
                    return;
                }
            }
        }
    }

    public static void login() throws IOException {
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Introduce your email: ");
        String email = keyboard.nextLine();
        email = email.toUpperCase();

        System.out.print("Introduce your hash: ");
        String hashCode = keyboard.nextLine();

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n\t\"email\" : \"" + email +
                "\",\n\t\"hashCode\" : \"" + hashCode + "\"\n}");
        Request request = new Request.Builder()
                .url("http://localhost:4069/question/login")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()){
            throw new IOException("Unexpected code " + response);
        }
        String answer = response.body().string();
        if (answer.equals("0")){
            successful = false;
            return;
        }

        loggedEmail = email;
        emailHash = hashCode;
        successful = true;
    }

    public static void register() throws IOException {
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Introduce your email: ");
        String email = keyboard.nextLine();
        email = email.toUpperCase();

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n\t\"email\" : \"" + email + "\"\n}");
        Request request = new Request.Builder()
                .url("http://localhost:4069/question/register")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()){
            throw new IOException("Unexpected code " + response);
        }
        String answer = response.body().string();
        if (answer.equals("Already exists!")){
            successful = false;
            return;
        }

        emailHash = answer;
        loggedEmail = email;
        System.out.println("Registration succesful! \nThis is your hash: " + emailHash);
        successful = true;
    }
}
