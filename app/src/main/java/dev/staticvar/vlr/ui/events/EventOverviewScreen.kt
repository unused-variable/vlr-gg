package dev.staticvar.vlr.ui.events

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.staticvar.vlr.R
import dev.staticvar.vlr.data.api.response.TournamentPreview
import dev.staticvar.vlr.ui.*
import dev.staticvar.vlr.ui.helper.CardView
import dev.staticvar.vlr.ui.helper.VLRTabIndicator
import dev.staticvar.vlr.ui.theme.VLRTheme
import dev.staticvar.vlr.utils.Waiting
import dev.staticvar.vlr.utils.onFail
import dev.staticvar.vlr.utils.onPass
import dev.staticvar.vlr.utils.onWaiting
import kotlinx.coroutines.launch

@Composable
fun EventScreen(viewModel: VlrViewModel) {

  val allTournaments by
    remember(viewModel) { viewModel.getTournaments() }.collectAsState(initial = Waiting())

  val primaryContainer = VLRTheme.colorScheme.primaryContainer
  val systemUiController = rememberSystemUiController()
  SideEffect { systemUiController.setStatusBarColor(primaryContainer) }

  Column(
    modifier = Modifier.fillMaxSize().statusBarsPadding(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    allTournaments
      .onPass {
        data?.let { list -> TournamentPreviewContainer(viewModel = viewModel, list = list) }
      }
      .onWaiting { LinearProgressIndicator() }
      .onFail { Text(text = message()) }
  }
}

@Composable
fun TournamentPreviewContainer(viewModel: VlrViewModel, list: List<TournamentPreview>) {
  val pagerState = rememberPagerState()
  val scope = rememberCoroutineScope()

  val (ongoing, upcoming, completed) =
    list.groupBy { it.status.startsWith("ongoing", ignoreCase = true) }.let {
      Triple(
        it[true].orEmpty(),
        it[false]
          ?.groupBy { it.status.startsWith("upcoming", ignoreCase = true) }
          ?.get(true)
          .orEmpty(),
        it[false]
          ?.groupBy { it.status.startsWith("upcoming", ignoreCase = true) }
          ?.get(false)
          .orEmpty()
      )
    }
  Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
    TabRow(
      selectedTabIndex = pagerState.currentPage,
      containerColor = VLRTheme.colorScheme.primaryContainer,
      indicator = { indicators -> VLRTabIndicator(indicators, pagerState.currentPage) }
    ) {
      Tab(
        selected = pagerState.currentPage == 0,
        onClick = { scope.launch { pagerState.scrollToPage(0) } }
      ) {
        Text(
          text = stringResource(R.string.ongoing),
          modifier = Modifier.padding(Local16DPPadding.current)
        )
      }
      Tab(
        selected = pagerState.currentPage == 1,
        onClick = { scope.launch { pagerState.scrollToPage(1) } }
      ) {
        Text(
          text = stringResource(R.string.upcoming),
          modifier = Modifier.padding(Local16DPPadding.current)
        )
      }
      Tab(
        selected = pagerState.currentPage == 2,
        onClick = { scope.launch { pagerState.scrollToPage(2) } }
      ) {
        Text(
          text = stringResource(R.string.completed),
          modifier = Modifier.padding(Local16DPPadding.current)
        )
      }
    }
    HorizontalPager(count = 3, state = pagerState, modifier = Modifier.fillMaxSize()) { tabPosition
      ->
      when (tabPosition) {
        0 -> {
          if (ongoing.isEmpty()) {
            Spacer(modifier = Modifier.weight(1f))
            Text(text = stringResource(R.string.no_ongoing_event))
            Spacer(modifier = Modifier.weight(1f))
          } else {
            LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
              items(ongoing) { TournamentPreview(tournamentPreview = it, viewModel.action) }
            }
          }
        }
        1 -> {
          if (upcoming.isEmpty()) {
            Spacer(modifier = Modifier.weight(1f))
            Text(text = stringResource(R.string.no_ongoing_event))
            Spacer(modifier = Modifier.weight(1f))
          } else {
            LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
              items(upcoming) { TournamentPreview(tournamentPreview = it, viewModel.action) }
            }
          }
        }
        else -> {
          if (completed.isEmpty()) {
            Spacer(modifier = Modifier.weight(1f))
            Text(text = stringResource(R.string.no_ongoing_event))
            Spacer(modifier = Modifier.weight(1f))
          } else {
            LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
              items(completed) { TournamentPreview(tournamentPreview = it, viewModel.action) }
            }
          }
        }
      }
    }
  }
}

@Composable
fun TournamentPreview(tournamentPreview: TournamentPreview, action: Action) {
  CardView(modifier = Modifier.clickable { action.event(tournamentPreview.id) }) {
    Column(modifier = Modifier.padding(Local8DPPadding.current)) {
      Text(
        text = tournamentPreview.title,
        style = VLRTheme.typography.titleSmall,
        modifier = Modifier.padding(Local4DPPadding.current),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        color = VLRTheme.colorScheme.primary,
      )

      Row(
        modifier = Modifier.padding(Local4DPPadding.current),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          Icons.Outlined.LocationOn,
          contentDescription = stringResource(R.string.location),
          modifier = Modifier.size(16.dp),
        )
        Text(text = tournamentPreview.location.uppercase(), style = VLRTheme.typography.labelMedium)
        Text(
          text = tournamentPreview.prize,
          modifier = Modifier.padding(Local4DPPadding.current).weight(1f),
          textAlign = TextAlign.Center,
          style = VLRTheme.typography.labelMedium
        )
        Icon(
          Icons.Outlined.DateRange,
          contentDescription = "Date",
          modifier = Modifier.size(16.dp),
        )
        Text(text = tournamentPreview.dates, style = VLRTheme.typography.labelMedium)
      }
    }
  }
}
