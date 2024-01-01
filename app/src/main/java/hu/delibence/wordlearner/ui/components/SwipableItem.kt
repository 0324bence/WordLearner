package hu.delibence.wordlearner.ui.components

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt


@Composable
fun SwipableItem(
    headLine: @Composable () -> Unit,
    isExpanded: Boolean,
    supporting: @Composable () -> Unit = {},
    trailingContent: @Composable () -> Unit = {},
    onExpand: () -> Unit = {},
    onCollapse: () -> Unit = {},
    actions: @Composable RowScope.(modifier: Modifier) -> Unit
) {
//    var isExpanded by remember { mutableStateOf(false) }
    var expandAmount by remember { mutableStateOf(0f) }
    var itemOffsetX by remember { mutableStateOf(expandAmount) }
    val itemOffsetXTransition by updateTransition(targetState = itemOffsetX, label = "").animateFloat(label = "") { it }
    Box(modifier = Modifier.fillMaxWidth().height(60f.dp)) {
        Row(modifier = Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
            actions(Modifier.onGloballyPositioned {
                expandAmount = it.size.width.toFloat()
                if (isExpanded) {
                    itemOffsetX += expandAmount
                } else {
                    itemOffsetX = 0f
                }
            })
        }
        ListItem(
            modifier = Modifier
                .offset { IntOffset(itemOffsetXTransition.roundToInt(), 0) }
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState {
                        itemOffsetX += if ((itemOffsetX + it) > 0 && (itemOffsetX + it) <= expandAmount) it else 0f
                    },
                    onDragStopped = {
                        if (itemOffsetX >= expandAmount/2) {
                            onExpand()
//                            isExpanded = true
                        } else if (itemOffsetX < expandAmount && isExpanded) {
                            onCollapse()
//                            isExpanded = false
                        }
                        itemOffsetX = if (isExpanded) expandAmount else 0f
                    }
                ),
            headlineContent = headLine,
            trailingContent = trailingContent,
            supportingContent = supporting
        )
    }
}