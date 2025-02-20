import lombok.Setter;
import lombok.val;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Setter
public class SurveyManager {
  private static volatile SurveyManager SURVEY_MANAGER;

  private Map<Integer, Survey> surveyMap;
  private Map<Integer, Map<Integer, SurveyResponse>> surveyResponseMap;
  private AtomicInteger autoSurveyId;
  private AtomicInteger autoSurveyResponseId;

  private Map<Integer, Long> surveyRatingMap;

  private Map<Integer, Map<Integer, Long>> surveyQuestionRatingMap;


  public static SurveyManager getInstance() {
    if (SURVEY_MANAGER == null) {
      synchronized (SurveyManager.class) {
        if (SURVEY_MANAGER == null) {
          SURVEY_MANAGER = new SurveyManager();
        }
      }
    }

    return SURVEY_MANAGER;
  }

  private SurveyManager() {
    this.surveyMap = new HashMap<>();
    this.surveyResponseMap = new ConcurrentHashMap<>();
    this.autoSurveyId = new AtomicInteger(0);
    this.autoSurveyResponseId = new AtomicInteger(0);
    surveyRatingMap = new ConcurrentHashMap<>();
    surveyQuestionRatingMap = new ConcurrentHashMap<>();
  }

  public int createSurvey(Survey survey) {
    int newSurveyId = autoSurveyId.getAndIncrement();

    //we can add validation also here
    survey.setSurveyId(newSurveyId);
    Optional.of(survey).ifPresent(s -> surveyMap.put(newSurveyId, s));
    return newSurveyId;
  }

  public void addQuestions(int surveyId, List<Question> questionList) {
    surveyMap.get(surveyId).addQuestions(questionList);
  }

  public void addOption(int surveyId, int questionId, Option option) {
    surveyMap.get(surveyId).addOptionToQuestion(questionId, option);
  }

  public void removeQuestion(int surveyId, int questionId) {
    surveyMap.get(surveyId).removeQuestion(questionId);
  }

  public long getTotalSurveyRating(int surveyId) {
    return surveyRatingMap.get(surveyId);
  }

  public double getAverageSurveyRating(int surveyId) {
    return surveyRatingMap.get(surveyId)/(double)surveyResponseMap.get(surveyId).size();
  }

  public Map<Integer, Long> getSurveyRating(int surveyId) {
    System.out.println(surveyMap.get(surveyId));
    return surveyQuestionRatingMap.get(surveyId);
  }

  public Long getSurveyQuestionRating(int surveyId, int questionId) {
    return surveyQuestionRatingMap.get(surveyId).get(questionId);
  }

  public Map<Integer, Double> getAverageSurveyQuestionRating(int surveyId) {
    Map<Integer, Double> res = new HashMap<>();
    for (int questionId: surveyQuestionRatingMap.get(surveyId).keySet()) {
      res.put(questionId, surveyQuestionRatingMap.get(surveyId).get(questionId)/(double)(surveyResponseMap.get(surveyId).size()));
    }
    return res;
  }


  public void submitResponse(int surveyId, int userId, SurveyResponse surveyResponse) {

    if (surveyResponseMap.get(surveyId) != null && surveyResponseMap.get(surveyId).containsKey(userId)) {
      throw new RuntimeException("User has already submitted the response for survey");
    }

    surveyResponseMap.computeIfAbsent(surveyId, k -> new ConcurrentHashMap<>())
            .put(userId, surveyResponse);

    long totalSurveyResponseRating = surveyResponse.getTotalRating();
    val newSurveyQuestionRatings = surveyResponse.getQuestionRating();

    val surveyQuestionRatings = surveyQuestionRatingMap.computeIfAbsent(surveyId, k -> new ConcurrentHashMap<>());

    for (int questionId: newSurveyQuestionRatings.keySet()) {
      surveyQuestionRatings.put(questionId,
              surveyQuestionRatings.getOrDefault(questionId, 0L) + newSurveyQuestionRatings.get(questionId));
    }

    surveyRatingMap.put(surveyId,
            surveyRatingMap.getOrDefault(surveyId, 0L) + totalSurveyResponseRating);
  }
}
