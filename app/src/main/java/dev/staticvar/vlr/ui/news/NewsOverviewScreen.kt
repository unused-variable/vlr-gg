package dev.staticvar.vlr.ui.news

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.get
import com.github.michaelbull.result.getError
import dev.staticvar.vlr.data.api.response.NewsResponseItem
import dev.staticvar.vlr.ui.Action
import dev.staticvar.vlr.ui.Local16DPPadding
import dev.staticvar.vlr.ui.Local4DPPadding
import dev.staticvar.vlr.ui.Local8DPPadding
import dev.staticvar.vlr.ui.VlrViewModel
import dev.staticvar.vlr.ui.common.ErrorUi
import dev.staticvar.vlr.ui.common.ScrollHelper
import dev.staticvar.vlr.ui.helper.CardView
import dev.staticvar.vlr.ui.scrim.StatusBarSpacer
import dev.staticvar.vlr.ui.scrim.StatusBarType
import dev.staticvar.vlr.ui.theme.VLRTheme
import dev.staticvar.vlr.utils.Waiting
import dev.staticvar.vlr.utils.onFail
import dev.staticvar.vlr.utils.onPass
import dev.staticvar.vlr.utils.onWaiting
import dev.staticvar.vlr.utils.readableDate
import dev.staticvar.vlr.utils.timeToEpoch

@Composable
fun NewsScreen(viewModel: VlrViewModel) {

  val newsInfo by
  remember(viewModel) { viewModel.getNews() }
    .collectAsStateWithLifecycle(initialValue = Waiting())
  var triggerRefresh by remember(viewModel) { mutableStateOf(true) }
  val updateState by
  remember(triggerRefresh) { viewModel.refreshNews() }
    .collectAsStateWithLifecycle(initialValue = Ok(false))

  val swipeRefresh =
    rememberPullRefreshState(updateState.get() ?: false, { triggerRefresh = triggerRefresh.not() })

  val modifier: Modifier = Modifier

  val resetScroll by
  remember { viewModel.resetScroll }.collectAsStateWithLifecycle(initialValue = false)
  val scrollState = rememberLazyListState()
  scrollState.ScrollHelper(resetScroll = resetScroll) { viewModel.postResetScroll() }

  Column(
    modifier = modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    newsInfo
      .onPass {
        data?.let { list ->
          val safeConvertedList =
            kotlin.runCatching { list.sortedByDescending { it.date.timeToEpoch } }

          AnimatedVisibility(
            visible = updateState.get() == true || swipeRefresh.progress != 0f,
            modifier = Modifier,
          ) {
            Column {
              StatusBarSpacer(statusBarType = StatusBarType.TRANSPARENT)
              LinearProgressIndicator(
                modifier
                  .fillMaxWidth()
                  .padding(Local16DPPadding.current)
                  .animateContentSize()
                  .testTag("common:loader")
                  .align(Alignment.CenterHorizontally)
              )
            }
          }
          Box(
            modifier = Modifier
              .pullRefresh(swipeRefresh)
              .fillMaxSize(),
          ) {
            LazyColumn(state = scrollState, modifier = modifier.testTag("newsOverview:root")) {
              item { StatusBarSpacer(statusBarType = StatusBarType.TRANSPARENT) }
              updateState.getError()?.let {
                item { ErrorUi(modifier = modifier, exceptionMessage = it.stackTraceToString()) }
              }

              items(
                if (safeConvertedList.isFailure) list else safeConvertedList.getOrElse { listOf() },
                key = { item -> item.link }
              ) { NewsItem(modifier, newsResponseItem = it, action = viewModel.action) }
            }
          }
        }
      }
      .onWaiting {
        LinearProgressIndicator(modifier.animateContentSize())
      }
      .onFail {
        Text(text = message())
      }
  }
}

@Composable
fun NewsItem(modifier: Modifier = Modifier, newsResponseItem: NewsResponseItem, action: Action) {
  CardView(
    modifier = modifier.clickable { action.news(newsResponseItem.link.split("/")[3]) },
  ) {
    Column(modifier = modifier.padding(Local8DPPadding.current)) {
      Text(
        text = newsResponseItem.title,
        style = VLRTheme.typography.titleLarge,
        modifier = modifier.padding(Local4DPPadding.current),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        color = VLRTheme.colorScheme.primary,
      )

      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Outlined.Person,
          contentDescription = "",
          modifier = modifier.size(16.dp),
        )
        Text(
          text = newsResponseItem.author,
          style = VLRTheme.typography.labelSmall,
          modifier = modifier
            .padding(Local4DPPadding.current)
            .weight(1f)
        )
        Icon(
          imageVector = Icons.Outlined.DateRange,
          contentDescription = "",
          modifier = modifier.size(16.dp),
        )
        val convertedDate = kotlin.runCatching { newsResponseItem.date.readableDate }
        Text(
          text =
          if (convertedDate.isSuccess) convertedDate.getOrDefault(newsResponseItem.date)
          else newsResponseItem.date,
          style = VLRTheme.typography.labelSmall,
          modifier = modifier.padding(Local4DPPadding.current)
        )
      }

      Text(
        text = newsResponseItem.description,
        style = VLRTheme.typography.bodyMedium,
        modifier = modifier.padding(Local4DPPadding.current),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
      )
    }
  }
}
