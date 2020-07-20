package dev.kxxcn.maru.view.order

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import dev.kxxcn.maru.R

enum class OrderItem(
    @DrawableRes val imageRes: Int,
    @StringRes val textRes: Int
) {

    OPENING(R.drawable.img_order_opening, R.string.order_opening),
    NOTICE(R.drawable.img_order_notice, R.string.order_notice),
    LIGHT(R.drawable.img_order_light, R.string.order_light),
    INTRODUCE(R.drawable.img_order_introduce, R.string.order_introduce),
    HUSBAND(R.drawable.img_order_husband, R.string.order_husband),
    WIFE(R.drawable.img_order_wife, R.string.order_wife),
    GREETING(R.drawable.img_order_greeting, R.string.order_greeting),
    VOWS(R.drawable.img_order_vows, R.string.order_vows),
    DECLARATION(R.drawable.img_order_declaration, R.string.order_declaration),
    CONGRATULATORY(R.drawable.img_order_congratulatory, R.string.order_congratulatory),
    BLESSING(R.drawable.img_order_blessing, R.string.order_blessing),
    ANTHEM(R.drawable.img_order_anthem, R.string.order_anthem),
    THANKS(R.drawable.img_order_thanks, R.string.order_thanks),
    MARCH(R.drawable.img_order_march, R.string.order_march),
    ENDING(R.drawable.img_order_ending, R.string.order_ending),
    STEP(R.drawable.img_order_step, R.string.order_step),
    ENTRANCE(R.drawable.img_order_entrance, R.string.order_entrance),
    PRAYER(R.drawable.img_order_prayer, R.string.order_prayer),
    WITNESS(R.drawable.img_order_witness, R.string.order_witness),
    PLEDGE(R.drawable.img_order_pledge, R.string.order_pledge),
    OFFERING(R.drawable.img_order_offering, R.string.order_offering),
    SACRAMENT(R.drawable.img_order_sacrament, R.string.order_sacrament),
    SIGNATURE(R.drawable.img_order_signature, R.string.order_signature),
    BELIEVER(R.drawable.img_order_believer, R.string.order_believer),
    LITURGY(R.drawable.img_order_liturgy, R.string.order_liturgy),
    BOW(R.drawable.img_order_bow, R.string.order_bow),
}

fun basicItems() = listOf(
    OrderItem.OPENING,
    OrderItem.NOTICE,
    OrderItem.LIGHT,
    OrderItem.INTRODUCE,
    OrderItem.HUSBAND,
    OrderItem.WIFE,
    OrderItem.GREETING,
    OrderItem.VOWS,
    OrderItem.DECLARATION,
    OrderItem.CONGRATULATORY,
    OrderItem.ANTHEM,
    OrderItem.THANKS,
    OrderItem.MARCH,
    OrderItem.ENDING
)

fun noOfficiateItems() = listOf(
    OrderItem.OPENING,
    OrderItem.NOTICE,
    OrderItem.LIGHT,
    OrderItem.HUSBAND,
    OrderItem.WIFE,
    OrderItem.GREETING,
    OrderItem.VOWS,
    OrderItem.DECLARATION,
    OrderItem.BLESSING,
    OrderItem.ANTHEM,
    OrderItem.THANKS,
    OrderItem.MARCH,
    OrderItem.ENDING
)

fun cathedralItems() = listOf(
    OrderItem.STEP,
    OrderItem.ENTRANCE,
    OrderItem.PRAYER,
    OrderItem.WITNESS,
    OrderItem.PLEDGE,
    OrderItem.OFFERING,
    OrderItem.SACRAMENT,
    OrderItem.SIGNATURE,
    OrderItem.BELIEVER,
    OrderItem.LITURGY,
    OrderItem.BOW,
    OrderItem.ANTHEM,
    OrderItem.ENDING
)
