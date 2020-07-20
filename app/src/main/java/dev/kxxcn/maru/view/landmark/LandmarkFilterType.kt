package dev.kxxcn.maru.view.landmark

import dev.kxxcn.maru.R

enum class LandmarkFilterType(
    val title: String,
    val imageRes: Int,
    val longitude: Double,
    val latitude: Double
) {

    SEOUL_FOREST(
        "서울숲",
        R.drawable.img_landmark_seoul_forest,
        127.037453,
        37.544490
    ),
    CHANGDEOKGUNG_PALACE(
        "창덕궁 후원",
        R.drawable.img_landmark_changdeokgung_palace,
        126.992946,
        37.582467
    ),
    HANOK_MAEUL(
        "남산골 한옥마을",
        R.drawable.img_landmark_hanok_maeul,
        126.993506,
        37.558609
    ),
    OLYMPIC_PARK(
        "서울 올림픽공원",
        R.drawable.img_landmark_olympic_park,
        127.121490,
        37.520848
    ),
    NAMISUM(
        "남이섬",
        R.drawable.img_landmark_namisum,
        127.525056,
        37.800298
    ),
    AMI_ART(
        "당진 아미미술관",
        R.drawable.img_landmark_ami_art,
        126.680406,
        36.862028
    ),
    DAECHEONG_DAM(
        "청주 대청댐",
        R.drawable.img_landmark_daecheong_dam,
        127.480751,
        36.477885
    ),
    UAM_PARK(
        "대전 남간정사",
        R.drawable.img_landmark_uam_park,
        127.456515,
        36.347881
    ),
    JEONGRANGGAK(
        "정란각",
        R.drawable.img_landmark_jeongranggak,
        129.042621,
        35.125881
    ),
    REED_FOREST(
        "순천만 갈대숲",
        R.drawable.img_landmark_reed_forest,
        127.510411,
        34.893674
    ),
    ALONE_TREE(
        "제주도 나홀로 나무",
        R.drawable.img_landmark_alone_tree,
        126.350410,
        33.351619
    ),
    SEOPIRANG(
        "서피랑",
        R.drawable.img_landmark_seopirang,
        128.418175,
        34.844848
    ),
    CAMELLIA_HILL(
        "카멜리아힐",
        R.drawable.img_landmark_camellia_hill,
        126.370160,
        33.289173
    ),
    NURI_PARK(
        "평화 누리 공원",
        R.drawable.img_landmark_nuri_park,
        126.743007,
        37.892401
    )
}
