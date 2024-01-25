package com.rik.programmeerija_prooviylesanne.controller.test;

import com.rik.programmeerija_prooviylesanne.util.EraseDbHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Profile("uitest")
public class UITestController {
  private final EraseDbHelper eraseDbHelper;

  @GetMapping("reset-db")
  public void resetDatabase() {
    eraseDbHelper.eraseDb();
  }
}
