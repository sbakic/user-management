package com.sbakic.usermanagement.controller.advice;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FieldErrorVM implements Serializable {

  private final String objectName;
  private final String field;
  private final String message;

}

