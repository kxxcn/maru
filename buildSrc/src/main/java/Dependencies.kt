object Versions {

    const val compatVersion = "1.1.0"
    const val coreVersion = "1.13.1"
    const val kotlinVersion = "2.3.21"
    const val coroutinesVersion = "1.3.7"
    const val constraintVersion = "2.0.0-beta3"
    const val cardVersion = "1.0.0"
    const val roomVersion = "2.8.4"
    const val archLifecycleVersion = "2.2.0"
    const val navigationVersion = "2.9.8"
    const val fragmentVersion = "1.2.0-rc02"
    const val androidXTestCoreVersion = "1.5.0"
    const val recyclerViewVersion = "1.1.0"
    const val glideVersion = "4.11.0"
    const val daggerVersion = "2.60.1"
    const val indicatorVersion = "4.3"
    const val bottomBarVersion = "1.7"
    const val roundProgressVersion = "2.1.2"
    const val circleProgressVersion = "1.4"
    const val mpchartVersion = "v3.1.0"
    const val gsonVersion = "2.8.6"
    const val retrofitVersion = "2.9.0"
    const val loggerVersion = "2.2.0"
    const val firebaseAuthVersion = "24.2.0"
    const val firebaseStoreVersion = "26.6.0"
    const val firebaseAnalyticsVersion = "23.2.0"
    const val firebaseMessagingVersion = "25.1.2"
    const val firebaseCrashlyticsVersion = "20.1.0"
    const val playServicesAuthVersion = "21.6.0"
    const val playServicesAdsVersion = "25.4.0"
    const val googleServicesVersion = "4.4.2"
    const val googlePlayReviewVersion = "2.0.2"
    const val crashlyticsGradleVersion = "3.0.2"
    const val billingVersion = "9.1.0"
    const val naverMapVersion = "3.23.3"
    const val interceptorVersion = "3.11.0"
    const val lottieVersion = "3.4.1"
    const val junitVersion = "4.12"
    const val junitExtVersion = "1.1.2"
    const val mockitoInlineVersion = "2.13.0"
    const val mockitoAndroidVersion = "3.6.0"
    const val espressoVersion = "3.3.0"
    const val roomTestingVersion = "2.8.4"
    const val mockkVersion = "1.10.2"
}

object BuildDependencies {

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
    const val cardView = "androidx.cardview:cardview:${Versions.cardVersion}"
    const val fragment = "androidx.fragment:fragment:${Versions.fragmentVersion}"
    const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerViewVersion}"

    /**
     * Google
     */
    const val googlePlayReview =
        "com.google.android.play:review:${Versions.googlePlayReviewVersion}"
    const val googlePlayReviewKtx =
        "com.google.android.play:review-ktx:${Versions.googlePlayReviewVersion}"
    const val playServicesAd =
        "com.google.android.gms:play-services-ads:${Versions.playServicesAdsVersion}"
    const val playServicesAuth =
        "com.google.android.gms:play-services-auth:${Versions.playServicesAuthVersion}"
    const val firebaseAuth = "com.google.firebase:firebase-auth:${Versions.firebaseAuthVersion}"
    const val firebaseStore =
        "com.google.firebase:firebase-firestore:${Versions.firebaseStoreVersion}"
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
        "androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1"
    const val lifecycleLiveDataKtx =
        "androidx.lifecycle:lifecycle-livedata-ktx:2.5.1"
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
    /**
     * Open Source
     */
    const val viewPagerDotsIndicator =
        "com.tbuonomo:dotsindicator:${Versions.indicatorVersion}"
    const val smoothBottomBar =
        "com.github.ibrahimsn98:SmoothBottomBar:${Versions.bottomBarVersion}"
    const val roundCornerProgressBar =
        "com.akexorcist:round-corner-progress-bar:${Versions.roundProgressVersion}"
    const val circleProgressView =
        "com.github.jakob-grabner:Circle-Progress-View:${Versions.circleProgressVersion}"
    const val mpChart = "com.github.PhilJay:MPAndroidChart:${Versions.mpchartVersion}"
    const val logger = "com.orhanobut:logger:${Versions.loggerVersion}"
    const val naverMap = "com.naver.maps:map-sdk:${Versions.naverMapVersion}"
    const val lottie = "com.airbnb.android:lottie:${Versions.lottieVersion}"

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
