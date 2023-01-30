package com.amrg.herafi.ui.screens.home_screen

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.VolunteerActivism
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Login
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.amrg.herafi.R
import com.amrg.herafi.shared.Constants
import com.amrg.herafi.shared.UiEvent
import com.amrg.herafi.ui.navigation.Screen
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.hero.ataa.ui.components.AppBar
import com.hero.ataa.ui.components.LoadingDialog
import kotlinx.coroutines.launch
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val scrollState = rememberScrollState()
    val scaffoldState = rememberScaffoldState()

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = uiEvent.message.asString(context),
                    )
                }
                is UiEvent.Navigate -> {
                    navController.navigate(uiEvent.route) {
                        popUpTo(route = Screen.HomeScreen.route) {
                            this.inclusive = true
                        }
                    }
                }
                else -> Unit
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { state ->
            SnackbarHost(state) { data ->
                Snackbar(
                    backgroundColor = MaterialTheme.colors.secondary,
                    contentColor = MaterialTheme.colors.onSecondary,
                    snackbarData = data,
                )
            }
        },
        backgroundColor = MaterialTheme.colors.background,
        topBar = {
            HomeAppBar(scaffoldState = scaffoldState, scrollState = scrollState)
        },
        drawerShape = RoundedCornerShape(7.dp),
        drawerBackgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.onBackground,
        drawerContent = {
            AppDrawer(
                navController = navController,
                viewModel = viewModel
            )
        },
        drawerContentColor = MaterialTheme.colors.onBackground
    ) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = false),
            onRefresh = { },
            modifier = Modifier,
            indicator = { state, trigger ->
                SwipeRefreshIndicator(
                    state = state,
                    refreshTriggerDistance = trigger,
                    scale = true,
                    contentColor = MaterialTheme.colors.primary,
                    backgroundColor = MaterialTheme.colors.surface
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                SearchButton(navController = navController)
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    text = stringResource(id = R.string.welcome_to_ataa_for_charity_projects),
                    style = MaterialTheme.typography.h4.copy(color = MaterialTheme.colors.onBackground),
                )
                Spacer(modifier = Modifier.height(10.dp))
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    text = stringResource(id = R.string.trending_donations),
                    style = MaterialTheme.typography.h4.copy(color = MaterialTheme.colors.onBackground),
                )
                Spacer(modifier = Modifier.height(10.dp))
                MostImportantRow(navController)
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    text = stringResource(id = R.string.contribute_with_us),
                    style = MaterialTheme.typography.h4.copy(color = MaterialTheme.colors.onBackground),
                )
                Spacer(modifier = Modifier.height(10.dp))
                ContributeWithUsRow(navController)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun SearchButton(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(horizontal = 16.dp)
            .clip(shape = RoundedCornerShape(7.dp))
            .background(MaterialTheme.colors.surface)
            .border(
                width = 0.1.dp,
                color = MaterialTheme.colors.secondaryVariant,
                shape = RoundedCornerShape(7.dp)
            )
            .clickable {
                // navController.navigate(Screen.SearchScreen.route)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search_icon),
                contentDescription = "",
                modifier = Modifier.size(14.dp),
                tint = MaterialTheme.colors.primary
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F),
                text = stringResource(id = R.string.search_for_a_project),
                style = MaterialTheme.typography.subtitle1.copy(color = MaterialTheme.colors.primaryVariant),
            )
        }
    }
}

@Composable
private fun HomeAppBar(scaffoldState: ScaffoldState, scrollState: ScrollState) {
    val coroutineScope = rememberCoroutineScope()
    AppBar(
        title = {
            Text(
                text = stringResource(id = R.string.home),
                style = MaterialTheme.typography.h4.copy(color = MaterialTheme.colors.onBackground),
                textAlign = TextAlign.Center,
            )
        },
        leading = {
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.open()
                    }
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_menu_icon),
                    contentDescription = "",
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        elevation = if (scrollState.value > 0) 1.dp else 0.dp
    )
}

@Composable
private fun AppDrawer(
    viewModel: HomeViewModel,
    navController: NavController,
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    if (viewModel.logOutPopUpDialogState.value) {
        LogoutAlertDialog(viewModel = viewModel)
    }
    if (viewModel.loadingDialogState.value) {
        LoadingDialog()
    }
    Column(modifier = Modifier.verticalScroll(scrollState)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(185.dp)
                .background(MaterialTheme.colors.primary)
        )
        Spacer(modifier = Modifier.height(8.dp))
        when (viewModel.loggedInFlow.collectAsState().value) {
            true -> {
                DrawerButton(
                    text = stringResource(id = R.string.my_account),
                    icon = Icons.Rounded.AccountCircle,
                ) {
                    navController.navigate(Screen.ProfileScreen.route)
                }
            }
            false -> {
                DrawerButton(
                    text = stringResource(id = R.string.login),
                    icon = Icons.Rounded.Login,
                ) {
                    navController.navigate(Screen.LoginScreen.route)
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        if(viewModel.loggedInFlow.collectAsState().value) {
            DrawerButton(
                text = stringResource(id = R.string.volunteer_with_us),
                icon = Icons.Outlined.VolunteerActivism,
            ) {
              //  navController.navigate(Screen.VolunteerOuterScreen.route)
            }
            Spacer(modifier = Modifier.height(4.dp))
            DrawerButton(
                text = stringResource(id = R.string.beneficiary_application),
                icon = Icons.Outlined.Article,
            ) {
              //  navController.navigate(Screen.BeneficiaryScreen.route)
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
        DrawerButton(
            text = stringResource(id = R.string.settings),
            icon = Icons.Outlined.Settings,
        ) {
           navController.navigate(Screen.SettingsScreen.route)
        }
        Spacer(modifier = Modifier.height(4.dp))
        DrawerButton(
            text = stringResource(id = R.string.about_us),
            icon = Icons.Outlined.Info,
        ) {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/amrg101/"))
            startActivity(context, browserIntent, null)
        }
        when (viewModel.loggedInFlow.collectAsState().value) {
            true -> {
                Spacer(modifier = Modifier.height(4.dp))
                DrawerButton(
                    text = stringResource(id = R.string.log_out),
                    icon = Icons.Rounded.Logout,
                ) {
                    viewModel.logOutPopUpDialogState.value = true
                }
            }
            false -> Unit
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun DrawerButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "",
                tint = MaterialTheme.colors.primary
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                modifier = Modifier.weight(1F),
                text = text,
                style = MaterialTheme.typography.subtitle2.copy(color = MaterialTheme.colors.onBackground)
            )
        }
    }
}

@Composable
private fun MostImportantRow(navController: NavController) {
    val language = Locale.getDefault().language
    val localConfig = LocalConfiguration.current
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        mainAxisSpacing = (localConfig.screenWidthDp * 2.5F / 100.0F).dp,
        crossAxisSpacing = (localConfig.screenWidthDp * 2.5F / 100.0F).dp,
        mainAxisAlignment = if (language == "ar") FlowMainAxisAlignment.End else FlowMainAxisAlignment.Start
    ) {

        val designer = stringResource(id = R.string.designer)
        val contractor = stringResource(id = R.string.contractor)
        val assistance = stringResource(id = R.string.assistance)
        val allCategory = stringResource(id = R.string.all_cat)

        CategoryItem(
            name = allCategory,
            icon = painterResource(id = R.drawable.ic_yateem_icon),
            iconSize = 23.dp,
        ) {
            navController.navigate(Screen.ProjectsScreen.route + "/${allCategory}/${Constants.CategoryApiKey.ALL_CATEGORIES}")
        }
        CategoryItem(
            name = designer,
            icon = painterResource(id = R.drawable.ic_yateem_icon),
            iconSize = 23.dp,
        ) {
            navController.navigate(Screen.ProjectsScreen.route + "/${designer}/${Constants.CategoryApiKey.DESIGNER}")
        }
        CategoryItem(
            name = contractor,
            icon = painterResource(id = R.drawable.ic_health_icon),
            iconSize = 22.dp,
        ) {
            navController.navigate(Screen.ProjectsScreen.route + "/${contractor}/${Constants.CategoryApiKey.CONTRACTOR}")
        }
        CategoryItem(
            name = assistance,
            icon = painterResource(id = R.drawable.ic_moon_icon),
        ) {
            navController.navigate(Screen.ProjectsScreen.route + "/${assistance}/${Constants.CategoryApiKey.ASSISTANCE}")
        }
    }
}

@Composable
private fun ContributeWithUsRow(navController: NavController) {
    val language = Locale.getDefault().language
    val localConfig = LocalConfiguration.current
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        mainAxisSpacing = (localConfig.screenWidthDp * 2.5F / 100.0).dp,
        crossAxisSpacing = (localConfig.screenWidthDp * 2.5F / 100.0).dp,
        mainAxisAlignment = if (language == "ar") FlowMainAxisAlignment.End else FlowMainAxisAlignment.Start
    ) {
        val carpenter = stringResource(id = R.string.carpenter)
        val accountant = stringResource(id = R.string.accountant)
        val teacher = stringResource(id = R.string.teacher)
        val lawyer = stringResource(id = R.string.lawyer)
        val engineer = stringResource(id = R.string.engineer)
        val doctor = stringResource(id = R.string.doctor)
        val electrician = stringResource(id = R.string.elect)

        CategoryItem(
            name = carpenter,
            icon = painterResource(id = R.drawable.ic_mosque_icon),
            iconSize = 25.dp,
        ) {
            navController.navigate(Screen.ProjectsScreen.route + "/${carpenter}/${Constants.CategoryApiKey.CARPENTER}")
        }
        CategoryItem(
            name = accountant,
            icon = painterResource(id = R.drawable.ic_education_icon),
            iconSize = 28.dp,
        ) {
            navController.navigate(Screen.ProjectsScreen.route + "/${accountant}/${Constants.CategoryApiKey.ACCOUNTANT}")
        }
        CategoryItem(
            name = teacher,
            icon = painterResource(id = R.drawable.ic_food_icon),
            iconSize = 25.dp,
        ) {
            navController.navigate(Screen.ProjectsScreen.route + "/${teacher}/${Constants.CategoryApiKey.TEACHER}")
        }
        CategoryItem(
            name = lawyer,
            icon = painterResource(id = R.drawable.ic_housing_icon),
            iconSize = 25.dp,
        ) {
            navController.navigate(Screen.ProjectsScreen.route + "/${lawyer}/${Constants.CategoryApiKey.LAWYER}")
        }
        CategoryItem(
            name = engineer,
            icon = painterResource(id = R.drawable.ic_shirt_icon),
        ) {
            navController.navigate(Screen.ProjectsScreen.route + "/${engineer}/${Constants.CategoryApiKey.ENGINEER}")
        }
        CategoryItem(
            name = doctor,
            icon = painterResource(id = R.drawable.ic_helping_icon),
            iconSize = 26.dp,
        ) {
            navController.navigate(Screen.ProjectsScreen.route + "/${doctor}/${Constants.CategoryApiKey.DOCTOR}")
        }
        CategoryItem(
            name = electrician,
            icon = painterResource(id = R.drawable.ic_wheel_chair_icon),
            iconSize = 27.dp,
        ) {
            navController.navigate(Screen.ProjectsScreen.route + "/${electrician}/${Constants.CategoryApiKey.ELECTRICIAN}")
        }
    }
}
@Composable
private fun CategoryItem(
    name: String,
    icon: Painter,
    iconSize: Dp = 24.dp,
    onClick: () -> Unit,
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val size = min(((screenWidth - 32) / 3.2F).dp, 140.dp)
    Box(
        modifier = Modifier
            .height(size)
            .width(size)
            .clip(RoundedCornerShape((7.dp)))
            .background(MaterialTheme.colors.surface)
            .border(
                width = 0.1.dp,
                color = MaterialTheme.colors.secondaryVariant,
                shape = RoundedCornerShape(7.dp)
            )
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = "",
                modifier = Modifier.size(iconSize),
                tint = MaterialTheme.colors.primary
            )
            Spacer(modifier = Modifier.height(7.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.caption.copy(color = MaterialTheme.colors.onBackground),
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun LogoutAlertDialog(viewModel: HomeViewModel) {
    AlertDialog(
        onDismissRequest = { viewModel.logOutPopUpDialogState.value = false },
        title = {
            Text(
                text = stringResource(id = R.string.log_out_question),
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.h5.copy(color = MaterialTheme.colors.onBackground)
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    viewModel.logOutPopUpDialogState.value = false
                    viewModel.logout()
                }
            ) {
                Text(
                    text = stringResource(id = R.string.yes),
                    style = MaterialTheme.typography.body2.copy(color = MaterialTheme.colors.primary),
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { viewModel.logOutPopUpDialogState.value = false }) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    style = MaterialTheme.typography.body2.copy(color = MaterialTheme.colors.primary),
                )
            }
        },
        shape = RoundedCornerShape(7.dp),
    )
}