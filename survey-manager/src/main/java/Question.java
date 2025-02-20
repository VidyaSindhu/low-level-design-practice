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
public class Question {
  Integer questionId;
  String text;
  Map<Integer, Option> optionMap;

  private AtomicInteger autoIncrementId;

  public Question(String text) {
    this.text = text;
    this.text = text;
    this.optionMap = new HashMap<>();
    this.autoIncrementId = new AtomicInteger(0);
  }

  public Question(Integer questionId, String text) {
    this.questionId = questionId;
    this.text = text;
    this.optionMap = new HashMap<>();
    this.autoIncrementId = new AtomicInteger(0);
  }



  public void addOption(Option option) {
    if (autoIncrementId == null) {
      autoIncrementId = new AtomicInteger();
    }
    int optionId = autoIncrementId.getAndIncrement();
    option.setOptionId(optionId);
    optionMap.put(optionId, option);
  }

  public void addOptions(List<Option> options) {
    options.forEach(this::addOption);
  }

  public void removeOption(Integer id) {
    optionMap.remove(id);
  }
}
