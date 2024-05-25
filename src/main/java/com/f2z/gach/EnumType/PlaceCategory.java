package com.f2z.gach.EnumType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PlaceCategory {
    BUILDING("교내 건물"), //(바람개비 동산, 비타 광장 추가)
    SMOKING("흡연 구역"),
    FOOD("음식점"),
    CAFE("카페"),
    CONV("편의점"),
    WELFARE("복지 시설"),
    PRINT("프린터"),
    BUSSTOP("버스 정류장"),;
    private final String placeCategoryName;

    public static PlaceCategory getPlaceCategory(String placeCategoryName) {
        for(PlaceCategory placeCategory : PlaceCategory.values()) {
            if(placeCategory.getPlaceCategoryName().equals(placeCategoryName)) {
                return placeCategory;
            }
        }
        return null;
    }

    public static PlaceCategory getPlaceCategoryContaining(String placeCategoryName) {
        for(PlaceCategory placeCategory : PlaceCategory.values()) {
            if(placeCategory.getPlaceCategoryName().contains(placeCategoryName)) {
                return placeCategory;
            }
        }
        return null;
    }
}
