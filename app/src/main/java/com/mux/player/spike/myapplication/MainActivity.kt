package com.mux.player.spike.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.theoplayer.android.api.THEOplayerView
import com.theoplayer.android.api.ads.ima.GoogleImaIntegrationFactory
import com.theoplayer.android.api.event.ads.AdsEventTypes
import com.theoplayer.android.api.event.player.PlayerEventTypes
import com.theoplayer.android.api.player.Player
import com.theoplayer.android.api.source.SourceDescription
import com.theoplayer.android.api.source.SourceType
import com.theoplayer.android.api.source.TypedSource
import com.theoplayer.android.api.source.addescription.GoogleImaAdDescription

class MainActivity : AppCompatActivity() {

  private lateinit var theoPlayerView: THEOplayerView
  private lateinit var playPauseToggle: ImageButton
  private val player get() = theoPlayerView.player

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    theoPlayerView = findViewById(R.id.theoplayer)
    setupPlayer(player)

    playPauseToggle = findViewById<ImageButton>(R.id.playpause)
    playPauseToggle.setOnClickListener {
      if (player.isPaused) {
        player.play()
        playPauseToggle.setImageResource(android.R.drawable.ic_media_pause)
      } else {
        player.pause()
        playPauseToggle.setImageResource(android.R.drawable.ic_media_play)
      }
    }
  }

  override fun onResume() {
    super.onResume()
    theoPlayerView.onResume()

    if (player.isPaused) {
      playSomething()
    }
  }

  override fun onPause() {
    theoPlayerView.onPause()
    super.onPause()
  }

  override fun onDestroy() {
    theoPlayerView.onDestroy()
    super.onDestroy()
  }

  private fun setupPlayer(player: Player) {
    player.addIntegration(GoogleImaIntegrationFactory.createGoogleImaIntegration(theoPlayerView))
    player.addEventListener(PlayerEventTypes.PAUSE) {
      playPauseToggle.setImageResource(android.R.drawable.ic_media_play)
    }
    player.addEventListener(PlayerEventTypes.PLAY) {
     playPauseToggle.setImageResource(android.R.drawable.ic_media_pause)
    }
    player.ads.addEventListener(AdsEventTypes.AD_BREAK_BEGIN) {
      // not called?
      Log.d("ADAD", "event ${it.type}")
    }
    player.ads.addEventListener(AdsEventTypes.AD_BEGIN) {
      // not called?
      Log.d("ADAD", "event ${it.type}")
    }
    player.ads.addEventListener(AdsEventTypes.AD_END) {
      // not called?
      Log.d("ADAD", "event ${it.type}")
    }
    player.ads.addEventListener(AdsEventTypes.AD_BREAK_END) {
      // not called?
      Log.d("ADAD", "event ${it.type}")
    }
  }

  private fun playSomething() {
    val sourceDesc =
      TypedSource.Builder(TEST_STREAM_URL)
        .type(SourceType.HLS)
        .let { SourceDescription.Builder(it.build()) }
        .ads( GoogleImaAdDescription.Builder(TEST_VMAP).build() )
        .build();

    player.source = sourceDesc
    //player.isAutoplay = true
  }

  companion object {
    const val TEST_STREAM_URL = "https://test-streams.mux.dev/tos_ismc/main.m3u8"
    //https://developers.google.com/interactive-media-ads/docs/sdks/html5/client-side/tags#vmap-pre-,-mid-,-and-post-rolls,-single-ads
    const val TEST_VMAP = "https://pubads.g.doubleclick.net/gampad/ads?iu=/21775744923/external/vmap_ad_samples&sz=640x480&cust_params=sample_ar%3Dpremidpost&ciu_szs=300x250&gdfp_req=1&ad_rule=1&output=vmap&unviewed_position_start=1&env=vp&impl=s&cmsid=496&vid=short_onecue&correlator="
  }
}