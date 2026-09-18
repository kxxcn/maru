package dev.kxxcn.maru.util

import android.content.res.Resources
import android.content.res.Resources.NotFoundException
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher


class RecyclerViewMatcher {

    companion object {

        fun atPosition(position: Int, itemMatcher: Matcher<View?>) =
            object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
                override fun describeTo(description: Description?) {
                    description?.appendText("Has item at position $position")
                    itemMatcher.describeTo(description)
                }

                override fun matchesSafely(view: RecyclerView?): Boolean {
                    val viewHolder = view
                        ?.findViewHolderForAdapterPosition(position)
                    return itemMatcher.matches(viewHolder?.itemView)
                }
            }

        fun atPositionOnView(position: Int, parentId: Int, targetId: Int) =
            object : TypeSafeMatcher<View>() {

                private var resources: Resources? = null
                private var childView: View? = null

                override fun describeTo(description: Description?) {
                    val idDescription: String = try {
                        resources?.getResourceName(parentId) ?: throw NotFoundException()
                    } catch (var4: NotFoundException) {
                        "$parentId (resource name not found)"
                    }

                    description?.appendText("With id: $idDescription")
                }

                override fun matchesSafely(view: View?): Boolean {
                    resources = view?.resources

                    if (childView == null) {
                        view?.rootView
                            ?.findViewById<RecyclerView>(parentId)
                            ?.let {
                                childView = it.findViewHolderForAdapterPosition(position)?.itemView
                            } ?: return false
                    }

                    return if (targetId == -1) {
                        view == childView
                    } else {
                        view == childView?.findViewById(targetId)
                    }
                }
            }

        inline fun <reified T : RecyclerView.ViewHolder> matchHolder(position: Int) =
            object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
                override fun describeTo(description: Description?) {
                    description?.appendText("Has item at position $position")
                }

                override fun matchesSafely(view: RecyclerView?): Boolean {
                    val viewHolder = view
                        ?.findViewHolderForAdapterPosition(position)
                        ?: return false
                    return viewHolder is T
                }
            }
    }
}
