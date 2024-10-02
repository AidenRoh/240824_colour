package com.colour.member.entity;

import lombok.Data;

@Data
public class MemberPii {

    private String name;
    private Integer age;
    private Gender gender;
    private Integer phone;
    private boolean privacyStatus;

    enum Gender {
        MALE, FEMALE, UNSTATED;
    }

    public static Gender getGender(String string) {
        if (string == null) return Gender.UNSTATED;
        String target = string.toUpperCase();
        return switch (target) {
            case "MALE" -> Gender.MALE;
            case "FEMALE" -> Gender.FEMALE;
            default -> Gender.UNSTATED;
        };
    }

    public MemberPii() {
        this.name = null;
        this.age = null;
        this.gender = Gender.UNSTATED;
        this.phone = null;
        this.privacyStatus = false;
    }
}
