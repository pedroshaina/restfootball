package de.planerio.developertest.dto.player;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum PlayerPosition {
    GK,
    CB,
    RB,
    LB,
    LWB,
    RWB,
    CDM,
    CM,
    LM,
    RM,
    CAM,
    ST,
    CF
}
