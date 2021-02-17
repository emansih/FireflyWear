object Dependencies{

    const val targetSdk = 30
    const val compileSdk = 30
    const val kotlinVersion = "1.4.21"
    private const val roomVersion = "2.2.5"
    private const val coroutineVersion = "1.3.7"
    private const val appCompatVersion = "1.3.0-beta01"
    private const val materialDesignVersion = "1.3.0-alpha04"
    private const val androidxCoreVersion = "1.5.0-alpha05"
    private const val androidxConstraintLayoutVersion = "2.0.3"
    private const val androidxWear = "1.1.0"
    private const val wearableVersion = "17.0.0"
    private const val googleWearableVersion = "2.8.1"
    private const val androidxPrefVersion = "1.1.1"

    val roomLibs = "androidx.room:room-runtime:$roomVersion"
    val roomExtension = "androidx.room:room-ktx:$roomVersion"
    val roomCompiler = "androidx.room:room-compiler:$roomVersion"
    val coroutineCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion"
    val appCompat = "androidx.appcompat:appcompat:$appCompatVersion"
    val materialDesign = "com.google.android.material:material:$materialDesignVersion"
    val androidxCore = "androidx.core:core-ktx:$androidxCoreVersion"
    val androidxConstraintLayout = "androidx.constraintlayout:constraintlayout:$androidxConstraintLayoutVersion"
    val kotlinLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion"
    val playServicesWearLib = "com.google.android.gms:play-services-wearable:$wearableVersion"
    val androidxWearLib = "androidx.wear:wear:$androidxWear"
    val googleSupportWearable = "com.google.android.support:wearable:$googleWearableVersion"
    val googleWearableLib = "com.google.android.wearable:wearable:$googleWearableVersion"
    val androidxPref = "androidx.preference:preference-ktx:$androidxPrefVersion"
}