package com.rik.programmeerija_prooviylesanne.util;

import com.rik.programmeerija_prooviylesanne.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;

import static com.rik.programmeerija_prooviylesanne.util.DateUtil.isTest;
import static com.rik.programmeerija_prooviylesanne.util.PostgresqlContainer.getInstance;
import static java.time.temporal.ChronoUnit.DAYS;

@Testcontainers
@SpringBootTest(classes = Main.class)
@ActiveProfiles({"integration"})
public class IntTestBase {
    @Autowired
    EraseDbHelper eraseDbHelper;

    @Container
    public static PostgreSQLContainer<PostgresqlContainer> postgreSQLContainer = getInstance();

    @AfterEach
    void afterEach() {
        eraseDbHelper.eraseDb();
        DateUtil.resetMockNow();
        DateUtil.isTest = false;
    }

    @BeforeEach
    void beforeEach() {
        DateUtil.isTest = true;
        DateUtil.setMockNow(Instant.now().minus(1, DAYS));
    }
}
