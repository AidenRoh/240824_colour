package com.colour.search.domain.enums;


import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Supplier;

public enum SearchField {
    ENGLISH("content_en", "en"),
    KOREAN("content_ko", "ko");

    private final String fieldName;
    private final String languageCode;

    SearchField(String fieldName, String languageCode) {
        this.fieldName = fieldName;
        this.languageCode = languageCode;
    }

    public static List<String> getSearchField(Locale locale, Supplier<Set<String>> supplier) {
        Set<String> fields = new HashSet<>();
        Set<String> languages = new HashSet<>(supplier.get());
        String localeLanguage = locale.getLanguage();

        for (SearchField each : values()) {
            if (localeLanguage.equals(each.languageCode)) fields.add(each.fieldName);
            if (languages.contains(each.languageCode)) fields.add(each.fieldName);
        }
        return List.copyOf(fields);
    }
}
