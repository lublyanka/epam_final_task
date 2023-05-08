package com.example.cards.entities.dict;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

/** The Currency entity. */
@Entity
@Table(name = "dict_currencies", schema = "public")
public class Currency {

  @Id
  @Column(name = "code", columnDefinition = "character(3)")
  @Getter
  String code;


  @Column(name = "name", length = 50, nullable = false)
  @Getter
  String name;

  /**
   * Instantiates a new Currency.
   *
   * @param code the code
   */
  public Currency(String code) {
    this.code = code;
  }

  /** Instantiates a new Currency. */
  public Currency() {}
}
