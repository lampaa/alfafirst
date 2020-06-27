package com.lampa.alfabattle.first.filters;

import lombok.Data;

@Data
public class NearestFilter {
    private Double latitude = 0D;
    private Double longitude = 0D;
    private Boolean payments;
    private Integer alfik;
}
