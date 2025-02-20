

/*
Design and implement an in-memory Rating Service. Using this rating service administrator should be able to create a new survey.
Inside this survey, the admin should be able to add, update and delete questions.
A question can have multiple options, where options have weights assigned.
The average of answers’ weight is the rating of the survey, an average of all ratings is the overall survey rating.

A survey can be shared with registered users.

Below functionality should be available
Admin :
Create a survey
Create questions and options for surveys
Define the weightage for each answer
Survey response should be rated, overall rating should be calculated, and shown to admin
Admin should be able to see the average rating of each question

User
Users should be able to respond to the provided survey,
Users should not be allowed to re-submit a survey

Survey - {
  map<questions>

}

question {
  map<options>
}

surveyResponseClass {
  int totalRating;
  map<userId, AnswerSheet> surveyREsponse
}

answerSheet{
 question - answer
}

singelton class for managing surveys {
  map<id, survy> surveys;
  map<id, surveyResponse>

  id - createSurvey(Survey);
  addSurveyQuestions(List<Queustions>)
  giveAnswerSheetforTheSurvey()
  getQuestionRating(surveyId, questionId);
  getSurveyRating(surveyId)
}

surveyId
 */

import java.util.List;

public class Application {
  public static SurveyManager surveyManager;
  public static void main(String[] args) {

    surveyManager = SurveyManager.getInstance();

    Survey survey1 = new Survey("Survey for E vehicle");

    int surveyId = surveyManager.createSurvey(survey1);

    Question question1 = new Question("How good is charging infrastructure in your place?");
    Option question1Option1 = Option.builder()
                    .optionText("Not Good")
            .weight(3)
                            .build();

    Option question1Option2 = Option.builder()
            .optionText("Average")
            .weight(5)
            .build();


    Option question1Option3 = Option.builder()
            .optionText("Good")
            .weight(8)
            .build();

    Option question1Option4 = Option.builder()
            .optionText("Very Good")
            .weight(10)
            .build();
    question1.addOptions(List.of(question1Option1, question1Option2, question1Option3, question1Option4));

    Question question2 = new Question("How good is charging infrastructure in your place?");
    Option question2Option1 = Option.builder()
            .optionText("Good")
            .weight(7)
            .build();

    Option question2Option2 = Option.builder()
            .optionText("Average")
            .weight(-100)
            .build();


    surveyManager.addQuestions(survey1.getSurveyId(), List.of(question1, question2));

    SurveyResponse surveyResponse1 = new SurveyResponse(survey1.getSurveyId());
    QuestionResponse questionResponse1 = new QuestionResponse(question1.getQuestionId(), question1Option3.getWeight());
    QuestionResponse question2Response1 = new QuestionResponse(question2.getQuestionId(), question2Option1.getWeight());
    surveyResponse1.addQuestionResponses(List.of(questionResponse1, question2Response1));


    SurveyResponse surveyResponse2 = new SurveyResponse(survey1.getSurveyId());
    QuestionResponse question1Response2 = new QuestionResponse(question1.getQuestionId(), question1Option2.getWeight());
    QuestionResponse question2Response2 = new QuestionResponse(question2.getQuestionId(), question2Option2.getWeight());
    surveyResponse2.addQuestionResponses(List.of(question1Response2, question2Response2));

    surveyManager.submitResponse(surveyId, 1, surveyResponse1);
    surveyManager.submitResponse(surveyId, 2, surveyResponse2);

    System.out.println(surveyManager.getSurveyRating(surveyId));
    System.out.println(surveyManager.getAverageSurveyRating(surveyId));

    System.out.println(surveyManager.getAverageSurveyQuestionRating(surveyId));


  }
}
