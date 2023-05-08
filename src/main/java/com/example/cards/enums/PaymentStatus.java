package com.example.cards.enums;

/** The enum Payment status. */
public enum PaymentStatus {
  /** Prepared payment status. */
  PREPARED("prepared"),

  /** Sent payment status. */
  SENT("sent");

  private final String statusName;

  PaymentStatus(String statusName) {
    this.statusName = statusName;
  }

  /**
   * Gets status name.
   *
   * @return the status name
   */
  public String getStatusName() {
    return statusName;
  }
}
