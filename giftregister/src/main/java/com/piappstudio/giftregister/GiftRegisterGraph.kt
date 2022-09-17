/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.giftregister

import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.piappstudio.giftregister.ui.event.EventHome
import com.piappstudio.giftregister.ui.event.editguest.EditGuestScreen
import com.piappstudio.giftregister.ui.event.guestlist.GuestHome
import com.piappstudio.giftregister.ui.event.list.EventEmptyScreen
import com.piappstudio.giftregister.ui.gift.GiftDetailScreen
import com.piappstudio.giftregister.ui.gift.GiftListScreen
import com.piappstudio.pitheme.route.Root
import com.piappstudio.pitheme.route.Route


fun NavGraphBuilder.homeGraph() {
    navigation(startDestination = Root.Home.EVENTS, route = Root.HOME) {
        eventGraph()
        giftGraph()
    }

}


@OptIn(ExperimentalMaterialApi::class)
fun NavGraphBuilder.eventGraph() {

    navigation(startDestination = Route.Home.EVENT.LIST, route = Root.Home.EVENTS) {

        composable(Route.Home.EVENT.LIST) {
            EventHome()
        }
        composable(Route.Home.EVENT.EVENT_EMPTY_SCREEN) {
            EventEmptyScreen()
        }


    }
}

@OptIn(ExperimentalMaterialApi::class)
fun NavGraphBuilder.giftGraph() {

    navigation(startDestination = Route.Home.GUEST.LIST, route = Root.Home.GIFTS) {

        composable(Route.Home.GUEST.LIST, arguments = listOf(navArgument(Route.Home.GUEST.Argument.eventId){type = NavType.LongType})) {
            val eventId = it.arguments?.getLong(Route.Home.GUEST.Argument.eventId)
            GuestHome(eventId = eventId)
        }
    }
}

