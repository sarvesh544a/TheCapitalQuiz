package com.kodeco.android.worldcountriesquiz.ui.screens

sealed interface Screen {
    val path: String

    data object List : Screen {
        override val path = "list"
    }

    data object Details : Screen {
        override val path = "details"
    }

    data object About : Screen {
        override val path = "about"
    }

    data object Settings : Screen {
        override val path = "settings"
    }

    data object Splash : Screen {
        override val path = "splashScreen"
    }

    data object Start : Screen {
        override val path = "StartScreen"
    }

    data object Quiz : Screen {
        override val path = "QuizScreen"
    }

}
