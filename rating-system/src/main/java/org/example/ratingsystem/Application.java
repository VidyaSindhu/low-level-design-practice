package org.example.ratingsystem;

import lombok.SneakyThrows;
import org.example.ratingsystem.services.RatingService;
import org.example.ratingsystem.services.RatingServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Application {

  private static final Random RANDOM = new Random();

  @SneakyThrows
  public static void main(String[] args) {

    final RatingService ratingService = RatingServiceImpl.getInstance();

    List<Thread> threadList = new ArrayList<>();

    for (int i = 0; i < 5; i++) {
      Thread thread = new Thread(() -> ratingService.addRating(1, RANDOM.nextInt(0, 11), "good", UUID.randomUUID().toString()));
      thread.start();
      threadList.add(thread);
    }

    threadList.forEach(thread -> {
      try {
        thread.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });

//    System.out.println(ratingService.getRating(3));

    System.out.println(ratingService.getAverageRating(1));

    ratingService.updateRating(3, 8, null);

//    System.out.println(ratingService.getRating(3));
    System.out.println(ratingService.getAverageRating(1));
  }
}