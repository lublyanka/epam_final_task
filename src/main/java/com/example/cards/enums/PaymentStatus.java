package com.example.cards.enums;

public enum PaymentStatus {
  PREPARED("prepared"),

  SENT("sent");

  private final String statusName;

  PaymentStatus(String statusName) {
    this.statusName = statusName;
  }

  public String getStatusName() {
    return statusName;
  }
}
