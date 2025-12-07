package me.koply.pikap.database;

import java.lang.reflect.RecordComponent;
import java.sql.Timestamp;

public class TableGenerator {

    public static String generateCreateTableSqlQuery(Class<? extends Record> recordClass) {
        StringBuilder query = new StringBuilder("CREATE TABLE IF NOT EXISTS ");

        // Track to tracks
        query.append(recordClass.getSimpleName().toLowerCase()).append("s (");

        RecordComponent[] fields = recordClass.getRecordComponents();
        for (int i = 0; i < fields.length; ++i) {
            RecordComponent component = fields[i];
            String name = component.getName();
            Class<?> type = component.getType();

            query.append(name).append(" ");

            if (name.equals("id")) {
                query.append("INTEGER PRIMARY KEY AUTOINCREMENT");
            } else if (type == int.class || type == long.class || type == Integer.class || type == Long.class) {
                query.append("INTEGER");
            } else if (type == String.class) {
                query.append("TEXT");
            } else if (type == Timestamp.class) {
                query.append("INTEGER");
            } else {
                query.append("TEXT");
            }
            // TODO: double, float

            if (i < fields.length -1) {
                query.append(",");
            }
        }
        query.append(");");
        return query.toString();
    }
}
