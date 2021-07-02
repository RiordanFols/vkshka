package ru.chernov.prosto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.*;

/**
 * @author Pavel Chernov
 */
@Getter
public enum Gender {
    @JsonProperty(value = "Мужчина")
    MALE("Мужчина", "stock_m.png"),

    @JsonProperty(value = "Женщина")
    FEMALE("Женщина", "stock_f.png"),

    @JsonProperty(value = "Не выбрано")
    UNDEFINED("Не выбрано", "stock_undefined.png");

    private final String stockAvatarFilename;
    private final String description;

    Gender(String description, String stockAvatarFilename) {
        this.description = description;
        this.stockAvatarFilename = stockAvatarFilename;
    }

    public static List<Map<String, String>> getAll() {
        List<Map<String, String>> genders = new LinkedList<>();
        for (Gender gender : Gender.values()) {
            Map<String, String> genderMap = new HashMap<>();
            genderMap.put("name", gender.name());
            genderMap.put("description", gender.getDescription());
            genders.add(genderMap);
        }

        return genders;
    }

}
