package com.petid.petid.type

/**
 * ALL, RECOMMENDED, ABOUTPET, TIPS, VENUE: 펫블로그
 * TERMS - 약관
 * PRIVACY_POLICY - 개인정보 처리방침
 * NOTICE - 공지사항
 * FAQ - 자주하는 질문
 */
enum class ContentCategoryType(val title: String) {
    ALL("전체"),
    RECOMMENDED("추천"),
    ABOUTPET("어바웃펫"),
    TIPS("반려TIP"),
    VENUE("장소"),
    SUPPORT("지원"),
    TERMS("서비스 이용약관"),
    PRIVACY_POLICY("개인정보 처리방침"),
    FAQ("자주하는 질문"),
    NOTICE("공지사항"),
}