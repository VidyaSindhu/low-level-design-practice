package models;

import enums.VerificationMode;
import lombok.Data;

@Data
public abstract class VerificationChannel {
  private VerificationMode verificationMode;

  public VerificationChannel(VerificationMode verificationMode) {
    this.verificationMode = verificationMode;
  }

  public abstract boolean isValid(VerificationChannel verificationChannel);
}
