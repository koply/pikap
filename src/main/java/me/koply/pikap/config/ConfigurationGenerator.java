package me.koply.pikap.config;

import java.lang.reflect.Field;

public class ConfigurationGenerator {

    public static void buildDefaultConfig(StringBuilder builder) {
        Class<?> clazz = Configuration.class;
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            ConfigProperty annotation = field.getAnnotation(ConfigProperty.class);
            if (annotation != null) {
                builder.append("# ")
                        .append(annotation.description())
                        .append("\n");

                String value = annotation.defaultValue();

                builder.append(field.getName())
                        .append(": ")
                        .append(value)
                        .append("\n\n");
            }
        }
    }

}