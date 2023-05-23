package com.jaszczurki.justbuild_it_web_portal.entity.dictionary;

public enum ServiceCategory {

    CONSTRUCTION("Budowa"),
    FINISHING_WORKS("Remont"),
    INSTALLATION("Instalacje"),
    ELECTRICITY("Elektryka"),
    EARTH_WORKS("Roboty ziemne"),
    GARDEN("Ogród");

    final String polishName;

    ServiceCategory(String polishName) {
        this.polishName = polishName;
    }

    @Override
    public String toString() {
        return polishName;
    }
}
