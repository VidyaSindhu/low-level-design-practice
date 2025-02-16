package org.example.ratingsystem.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rating {
  private long id;
  private int points;
  private String review;
  private String userId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private long entityId;

  public void setReview(String review) {
    this.review = review;
    setUpdatedAt(LocalDateTime.now());
  }

  public void setPoints(int points) {
    this.points = points;
    setUpdatedAt(LocalDateTime.now());
  }

  public Rating(long id, int points, String userId, String review, long entityId) {
    this.id = id;
    this.points = points;
    this.userId = userId;
    this.review = review;
    this.entityId = entityId;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Rating rating = (Rating) o;
    return entityId == rating.entityId && Objects.equals(userId, rating.userId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, entityId);
  }
}
