package models;

import enums.VerificationMode;
import lombok.*;

import java.util.Random;

@Getter
@EqualsAndHashCode(callSuper = true)
public class OtpVerificationChannel extends VerificationChannel {
  private final Integer code;

  public OtpVerificationChannel() {
    super(VerificationMode.OTP);
    Random random = new Random();
    code = 100000 + random.nextInt(900000);
  }

  @Override
  public boolean isValid(VerificationChannel verificationChannel) {
    OtpVerificationChannel otpVerificationChannel = (OtpVerificationChannel) verificationChannel;
    return this.code.equals(otpVerificationChannel.getCode());
  }
}
