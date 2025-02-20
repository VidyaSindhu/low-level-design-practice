import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
public class SurveyResponse {
  int surveyId;
  AtomicLong totalRating;
  Map<Integer, QuestionResponse> questionResponseMap;


  public SurveyResponse(int surveyId) {
    this.surveyId = surveyId;
    this.totalRating = new AtomicLong(0);
    this.questionResponseMap = new ConcurrentHashMap<>();
  }

  public void addQuestionResponse(QuestionResponse questionResponse) {
    questionResponseMap.put(questionResponse.questionId, questionResponse);
    totalRating.addAndGet(questionResponse.getOptionResponse());
  }

  public long addQuestionResponses(List<QuestionResponse> questionResponses) {
    questionResponses.forEach(this::addQuestionResponse);
    return totalRating.get();
  }

  public long getTotalRating() {
    return this.totalRating.get();
  }

  public Map<Integer, Long> getQuestionRating() {
    Map<Integer, Long> questionRating = new HashMap<>();
    for (int questionId: questionResponseMap.keySet()) {
      questionRating.put(questionId, (long) questionResponseMap.get(questionId).getOptionResponse());
    }
    return questionRating;
  }
}
