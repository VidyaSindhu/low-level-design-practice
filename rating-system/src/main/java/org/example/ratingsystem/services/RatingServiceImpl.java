package org.example.ratingsystem.services;

import org.example.ratingsystem.models.Rating;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class RatingServiceImpl implements RatingService {

  private static volatile RatingService RATING_SERVICE;

  public static RatingService getInstance() {
    if (RATING_SERVICE == null) {
      synchronized (RatingServiceImpl.class) {
        if (RATING_SERVICE == null) {
          RATING_SERVICE = new RatingServiceImpl();
        }
      }
    }

    return RATING_SERVICE;
  }

  private RatingServiceImpl() {
    this.entityRatings = new ConcurrentHashMap<>();
    this.ratingMap = new ConcurrentHashMap<>();
    this.autoincrementId = new AtomicInteger(0);
  }

  private final Map<Long, Rating> ratingMap;
  private final Map<Long, HashSet<Rating>> entityRatings;
  private final AtomicInteger autoincrementId;


  @Override
  public void addRating(long entityId, int ratingPoints, String review, String userName) {
    Rating rating = new Rating(autoincrementId.getAndIncrement(), ratingPoints, userName, review, entityId);

    ratingMap.put(rating.getId(), rating);
    entityRatings.computeIfAbsent(entityId, k -> new HashSet<>()).add(rating);
  }

  @Override
  public void updateRating(long ratingId, Integer ratingPoints, String review) {
    Optional.ofNullable(ratingMap.get(ratingId))
            .map(rating -> {
              rating.setPoints(ratingPoints);
              rating.setReview(review);
              return rating;
            })
            .orElseThrow(() -> new RuntimeException("rating is not present with ratingId: " + ratingId));
  }

  @Override
  public void removeRating(long ratingId) {
    Rating rating = getRating(ratingId);
    entityRatings.get(rating.getEntityId()).remove(rating);
    ratingMap.remove(rating.getId());
  }

  @Override
  public Rating getRating(long ratingId) {
    return Optional.ofNullable(ratingMap.get(ratingId))
            .orElseThrow(() -> new RuntimeException("rating is not present with ratingId: " + ratingId));
  }

  @Override
  public List<Rating> getRatings(long entityId) {
    return entityRatings.getOrDefault(entityId, new HashSet<>()).stream()
            .sorted(Comparator.comparing(Rating::getUpdatedAt))
            .toList();

  }

  @Override
  public Double getAverageRating(long entityId) {
    return getRatings(entityId).stream()
            .mapToInt(Rating::getPoints)
            .average().getAsDouble();
  }

  @Override
  public Rating getMaxRating(long entityId) {
    return getRatings(entityId).stream()
            .sorted(Comparator.comparing(Rating::getPoints, Comparator.reverseOrder()))
            .limit(1)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No rating available for this entity"));
  }
}
