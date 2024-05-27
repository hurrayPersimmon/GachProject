package com.f2z.gach.EnumType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum College {
    경영대학("경영대학", "경영대"),
    가천리버럴아츠칼리지("가천리버럴아츠칼리지", "가리아"),
    사회과학대학("사회과학대학", "사과대"),
    인문대학("인문대학", "인문대"),
    법과대학("법과대학", "법대"),
    공과대학("공과대학", "공대"),
    바이오나노대학("바이오나노대학", "바나대"),
    IT융합대학("IT융합대학", "아융대"),
    한의과대학("한의과대학", "한의대"),
    예술체육대학("예술체육대학", "예체대"),
    반도체대학("반도체대학", "반도체대"),;

    private final String college;
    private final String collegeNickname;

    public static College getCollege(String college) {
        for(College collegeType : College.values()) {
            if(collegeType.getCollege().equals(college)) {
                return collegeType;
            }
        }
        return null;
    }

    public static College getCollegeContaining(String college) {
        for(College collegeType : College.values()) {
            if(collegeType.getCollege().contains(college) || collegeType.getCollegeNickname().contains(college)) {
                return collegeType;
            }
        }
        return null;
    }
}
