package org.example.ratingsystem.services;

import org.example.ratingsystem.models.Rating;

import java.util.List;

public interface RatingService {
  void addRating(long entityId, int ratingPoints, String review, String userName);
  void updateRating(long ratingId, Integer ratingPoints, String review);
  void removeRating(long ratingId);
  Rating getRating(long ratingId);
  List<Rating> getRatings(long entityId);
  Double getAverageRating(long entityId);
  Rating getMaxRating(long entityId);
}
