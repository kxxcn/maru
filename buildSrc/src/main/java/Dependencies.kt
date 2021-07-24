object Versions {

    const val compatVersion = "1.1.0"
    const val coreVersion = "1.3.0"
    const val kotlinVersion = "1.4.10"
    const val coroutinesVersion = "1.3.7"
    const val constraintVersion = "2.0.0-beta3"
    const val cardVersion = "1.0.0"
    const val roomVersion = "2.2.4"
    const val archLifecycleVersion = "2.2.0"
    const val navigationVersion = "2.2.1"
    const val fragmentVersion = "1.2.0-rc02"
    const val androidXTestCoreVersion = "1.3.0"
    const val recyclerViewVersion = "1.1.0"
    const val glideVersion = "4.11.0"
    const val daggerVersion = "2.28.3"
    const val daggerAssistedVersion = "0.5.2"
    const val ankoVersion = "0.10.8"
    const val indicatorVersion = "4.1.2"
    const val bottomBarVersion = "1.7"
    const val roundProgressVersion = "2.1.1"
    const val waveViewVersion = "1.0.0"
    const val circleProgressVersion = "1.4"
    const val mpchartVersion = "v3.1.0"
    const val gsonVersion = "2.8.6"
    const val retrofitVersion = "2.9.0"
    const val loggerVersion = "2.2.0"
    const val firebaseAuthVersion = "19.1.0"
    const val firebaseStoreVersion = "21.2.1"
    const val firebaseStoreKtxVersion = "21.5.0"
    const val firebaseAnalyticsVersion = "17.4.4"
    const val firebaseMessagingVersion = "20.2.0"
    const val firebaseCrashlyticsVersion = "17.1.1"
    const val playServicesAuthVersion = "17.0.0"
    const val playServicesAdsVersion = "19.1.0"
    const val googleServicesVersion = "4.3.3"
    const val googlePlayVersion = "1.8.0"
    const val googlePlayKtxVersion = "1.8.1"
    const val crashlyticsGradleVersion = "2.2.0"
    const val billingVersion = "4.0.0"
    const val naverMapVersion = "3.12.0"
    const val interceptorVersion = "3.11.0"
    const val lottieVersion = "3.4.1"
    const val toggleVersion = "1.0.0"
    const val junitVersion = "4.12"
    const val junitExtVersion = "1.1.2"
    const val mockitoInlineVersion = "2.13.0"
    const val mockitoAndroidVersion = "3.6.0"
    const val espressoVersion = "3.3.0"
    const val roomTestingVersion = "2.2.5"
    const val mockkVersion = "1.10.2"
}

object Dependencies {

    /**
     * AndroidX
     */
    const val appCompat = "androidx.appcompat:appcompat:${Versions.compatVersion}"
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreVersion}"

    /**
     * Kotlin
     */
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlinVersion}"
    const val coroutines =
        "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Versions.coroutinesVersion}"

    /**
     * Layout
     */
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintVersion}"
    const val cardView = "androidx.cardview:cardview:$${Versions.cardVersion}"
    const val fragment = "androidx.fragment:fragment:${Versions.fragmentVersion}"
    const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerViewVersion}"
    const val anko = "org.jetbrains.anko:anko:${Versions.ankoVersion}"

    /**
     * Google
     */
    const val googlePlay = "com.google.android.play:core:${Versions.googlePlayVersion}"
    const val googlePlayKtx = "com.google.android.play:core-ktx:${Versions.googlePlayKtxVersion}"
    const val playServicesAd =
        "com.google.android.gms:play-services-ads:${Versions.playServicesAdsVersion}"
    const val playServicesAuth =
        "com.google.android.gms:play-services-auth:${Versions.playServicesAuthVersion}"
    const val firebaseAuth = "com.google.firebase:firebase-auth:${Versions.firebaseAuthVersion}"
    const val firebaseStore =
        "com.google.firebase:firebase-firestore:${Versions.firebaseStoreVersion}"
    const val firebaseStoreKtx =
        "com.google.firebase:firebase-firestore-ktx:${Versions.firebaseStoreKtxVersion}"
    const val firebaseAnalytics =
        "com.google.firebase:firebase-analytics:${Versions.firebaseAnalyticsVersion}"
    const val firebaseMessaging =
        "com.google.firebase:firebase-messaging:${Versions.firebaseMessagingVersion}"
    const val firebaseCrashlytics =
        "com.google.firebase:firebase-crashlytics:${Versions.firebaseCrashlyticsVersion}"
    const val gson = "com.google.code.gson:gson:${Versions.gsonVersion}"

    /**
     * Retrofit
     */
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofitVersion}"
    const val gsonConverter =
        "com.squareup.retrofit2:converter-gson:${Versions.retrofitVersion}"
    const val loggingInterceptor =
        "com.squareup.okhttp3:logging-interceptor:${Versions.interceptorVersion}"


    /**
     * In-App Purchase
     */
    const val billing = "com.android.billingclient:billing:${Versions.billingVersion}"
    const val billingKtx = "com.android.billingclient:billing-ktx:${Versions.billingVersion}"

    /**
     * Room
     */
    const val roomRuntime = "androidx.room:room-runtime:${Versions.roomVersion}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.roomVersion}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.roomVersion}"

    /**
     * Lifecycle
     */
    const val lifecycleExtensions =
        "androidx.lifecycle:lifecycle-extensions:${Versions.archLifecycleVersion}"
    const val lifecycleViewModelKtx =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.archLifecycleVersion}"
    const val lifecycleLiveDataKtx =
        "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.archLifecycleVersion}"
    const val lifecycleRuntimeKtx =
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.archLifecycleVersion}"

    /**
     * Navigation
     */
    const val navigationFragmentKtx =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigationVersion}"
    const val navigationUiKtx =
        "androidx.navigation:navigation-ui-ktx:${Versions.navigationVersion}"

    /**
     * Glide
     */
    const val glideRuntime = "com.github.bumptech.glide:glide:${Versions.glideVersion}"
    const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glideVersion}"

    /**
     * Dagger2
     */
    const val dagger = "com.google.dagger:dagger:${Versions.daggerVersion}"
    const val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.daggerVersion}"
    const val daggerAndroidSupport =
        "com.google.dagger:dagger-android-support:${Versions.daggerVersion}"
    const val daggerAndroidProcessor =
        "com.google.dagger:dagger-android-processor:${Versions.daggerVersion}"
    const val daggerAssistedAnnotations =
        "com.squareup.inject:assisted-inject-annotations-dagger2:${Versions.daggerAssistedVersion}"
    const val daggerAssistedProcessor =
        "com.squareup.inject:assisted-inject-processor-dagger2:${Versions.daggerAssistedVersion}"

    /**
     * Open Source
     */
    const val viewPagerDotsIndicator =
        "com.tbuonomo.andrui:viewpagerdotsindicator:${Versions.indicatorVersion}"
    const val smoothBottomBar =
        "com.github.ibrahimsn98:SmoothBottomBar:${Versions.bottomBarVersion}"
    const val roundCornerProgressBar =
        "com.akexorcist:round-corner-progress-bar:${Versions.roundProgressVersion}"
    const val waveView = "com.gelitenight.waveview:waveview:${Versions.waveViewVersion}"
    const val circleProgressView =
        "com.github.jakob-grabner:Circle-Progress-View:${Versions.circleProgressVersion}"
    const val mpChart = "com.github.PhilJay:MPAndroidChart:${Versions.mpchartVersion}"
    const val logger = "com.orhanobut:logger:${Versions.loggerVersion}"
    const val naverMap = "com.naver.maps:map-sdk:${Versions.naverMapVersion}"
    const val lottie = "com.airbnb.android:lottie:${Versions.lottieVersion}"
    const val toggle = "com.zcw:togglebutton-library:${Versions.toggleVersion}"

    /**
     * Testing
     */
    const val fragmentTesting = "androidx.fragment:fragment-testing:${Versions.fragmentVersion}"
    const val androidXTestCore = "androidx.test:core:${Versions.androidXTestCoreVersion}"
    const val roomTesting = "androidx.room:room-testing:${Versions.roomTestingVersion}"
    const val junit = "junit:junit:${Versions.junitVersion}"
    const val junitExt = "androidx.test.ext:junit:${Versions.junitExtVersion}"
    const val mockitoInline = "org.mockito:mockito-inline:${Versions.mockitoInlineVersion}"
    const val mockitoAndroid = "org.mockito:mockito-android:${Versions.mockitoAndroidVersion}"
    const val mockkAndroid = "io.mockk:mockk-android:${Versions.mockkVersion}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espressoVersion}"
    const val espressoContrib =
        "androidx.test.espresso:espresso-contrib:${Versions.espressoVersion}"
}
