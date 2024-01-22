package com.rik.programmeerija_prooviylesanne.util;

import com.rik.programmeerija_prooviylesanne.Main;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.rik.programmeerija_prooviylesanne.util.PostgresqlContainer.getInstance;

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
    }
}
