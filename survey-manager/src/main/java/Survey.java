import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Survey {
  int surveyId;
  String description;
  Map<Integer, Question> questionMap;
  private AtomicInteger autoIncrementId;

  public Survey(String description) {
    this.description = description;
    this.questionMap = new HashMap<>();
    this.autoIncrementId = new AtomicInteger(0);
  }

  public void addQuestion(Question question) {
    int questionId = autoIncrementId.getAndIncrement();
    question.setQuestionId(questionId);
    questionMap.put(questionId, question);
  }

  public void addQuestions(List<Question> questionList) {
    questionList.forEach(this::addQuestion);
  }

  public void addOptionToQuestion(int questionId, Option option) {
    questionMap.get(questionId).addOption(option);
  }

  public void removeQuestion(int questionId) {
    questionMap.remove(questionId);
  }
}
