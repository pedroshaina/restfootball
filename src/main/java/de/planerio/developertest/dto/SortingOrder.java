package de.planerio.developertest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum SortingOrder {
    ASC, DESC
}
