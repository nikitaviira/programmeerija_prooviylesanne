package com.rik.programmeerija_prooviylesanne.util;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class EraseDbHelper {
    private final EntityManager entityManager;
    private List<String> tableNames;

    public EraseDbHelper(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @PostConstruct
    void afterPropertiesSet() {
        tableNames = entityManager.getMetamodel().getEntities().stream()
                .filter(entityType -> entityType.getJavaType().getAnnotation(Table.class) != null)
                .map(entityType -> entityType.getJavaType().getAnnotation(Table.class))
                .map(this::convertToTableName).toList();
    }

    private String convertToTableName(Table table) {
        String schema = table.schema();
        String tableName = table.name();

        String convertedSchema = StringUtils.hasText(schema) ? schema.toLowerCase() + "." : "";
        String convertedTableName = tableName.replaceAll("([a-z])([A-Z])", "$1_$2");

        return convertedSchema + convertedTableName;
    }

    @Transactional
    public void eraseDb() {
        for (String tableName : tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName + " CASCADE").executeUpdate();
        }
    }
}
