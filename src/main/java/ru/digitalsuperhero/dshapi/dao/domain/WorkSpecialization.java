package ru.digitalsuperhero.dshapi.dao.domain;

public enum WorkSpecialization {
    OFFICES(1),
    BANKS(2),
    HOUSES(3),
    FIELDS(4);

    private final Integer levelCode;

    WorkSpecialization(int levelCode) {
        this.levelCode = levelCode;
    }

    public int getLevelCode() {
        return this.levelCode;
    }
}
