package io.github.akumosstl.auth.payload.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageResponse {
  private String message;
  private int code;

  public MessageResponse(String message, int code) {
    this.message = message;
    this.code = code;
  }

}
