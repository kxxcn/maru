package dev.kxxcn.maru.view.input

enum class InputFilterType(
    val id: Int,
    val unit: Int
) {

    /**
     * 10,000(만원)
     */
    TEN_THOUSAND(0, 10000),

    /**
     * 100,000(십만원)
     */
    HUNDRED_THOUSAND(1, 100000),

    /**
     * 1,000,000(백만원)
     */
    ONE_MILLION(2, 1000000),

    /**
     * 10,000,000(천만원)
     */
    TEN_MILLION(3, 10000000)
}

enum class InputMoneyType {

    /**
     * 신랑 지출 금액
     */
    HUSBAND,

    /**
     * 신부 지출 금액
     */
    WIFE,

    /**
     * 잔금
     */
    REMAIN
}
