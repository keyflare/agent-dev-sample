package com.keyflare.exchange.core.utilityscreen.api

import com.keyflare.exchange.core.icons.AppIcon
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenUiEvent.OnNodeClick

public data class UtilityScreenViewState(val nodes: List<ScreenNode>) {

    public sealed interface Screen {
        public val title: String

        public data class General(
            val id: String,
            override val title: String,
            val nodes: List<ScreenNode>,
        ) : Screen

        public interface Custom : Screen
    }

    public sealed interface ScreenNode {
        public val id: String

        public data class Group(
            override val id: String,
            val title: String,
            val nodes: List<ScreenNode>,
        ) : ScreenNode {

            public companion object {
                public fun noTitleGroup(id: String, vararg nodes: ScreenNode): Group =
                    Group(id = id, title = "", nodes = nodes.asList())
            }
        }

        public interface Leaf : ScreenNode {

            public interface Clickable : Leaf {
                public val onClick: UtilityScreenUiEvent get() = OnNodeClick(nodeId = id)
            }

            public data class Text(
                override val id: String,
                val text: String,
            ) : Leaf

            public sealed interface ListItem : Leaf {
                public val label: String
                public val drawLine: Boolean

                public data class Simple(
                    override val id: String,
                    override val label: String,
                    override val drawLine: Boolean = true,
                    override val onClick: UtilityScreenUiEvent = OnNodeClick(nodeId = id),
                    val additional: Content = Content.None,
                ) : ListItem, Clickable

                public data class Switcher(
                    override val id: String,
                    override val label: String,
                    override val drawLine: Boolean = true,
                    val value: Boolean,
                    val onValueChange: (Boolean) -> Unit,
                ) : ListItem

                public sealed interface Input : ListItem {

                    public data class Text(
                        override val id: String,
                        override val label: String,
                        override val drawLine: Boolean = true,
                        val allowCopy: Boolean = false,
                        val initialValue: String,
                        val onValueChange: (String) -> Unit,
                    ) : Input

                    public data class IntNumber(
                        override val id: String,
                        override val label: String,
                        override val drawLine: Boolean = true,
                        val value: Int,
                        val onValueChange: (Int) -> Unit,
                    ) : Input

                    public data class FloatNumber(
                        override val id: String,
                        override val label: String,
                        override val drawLine: Boolean = true,
                        val value: Float,
                        val onValueChange: (Float) -> Unit,
                    ) : Input
                }
            }

            public data class Button(
                override val id: String,
                val text: String,
            ) : Clickable
        }
    }

    public sealed interface Content {
        public data class Icon(val icon: AppIcon) : Content
        public data object None : Content
    }
}
