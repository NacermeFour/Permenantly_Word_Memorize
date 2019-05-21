## What is Interstitial Ad?

It is the ad format that takes up the entire area on the device screen. According to banner advertising, advertising revenue is more. An ad format used when moving from one page to a page. I'm trying to use this type of advertising in my own applications. Users of this type of advertising are often not happy to see them according to the frequency of use I suggest you pay attention :)

## Add an Interstitial Ad to the app


![Interstitial Ad](https://cdn-images-1.medium.com/max/800/1*vOhwMX-Plm4plkEhHxG2qw.png)

He asks us what ad to add to the app. We're choosing it so Interstitial ad.

![Interstitial Ad](https://cdn-images-1.medium.com/max/800/1*knYXrfr1zqXAyR_FqUbG5Q.png)

After selecting the Interstitial ad type, we're giving a name to our ad. Then we will be able to keep track of how much we can earn from this type of advertising between this name and our ads. We choose what we can show in our ad. I chose both video and text. Choose what you want according to your preference. I also choose automatic renewal by Google. Finally, we're moving to the final stage by creating the ad unit.

![Interstitial Ad](https://cdn-images-1.medium.com/max/800/1*0OvOjmgGqWgCu5wyp4DXIA.png)

We have completed our successful operation. Together with the given id value, we will be able to show the interstitial ads in our application.

# Application Part

After making the adjustments, we switch to the Android section. I suggest following the steps below step by step.

- build.gradle (Module : app) into the following code: :
```
dependencies {
 ...
    implementation 'com.google.android.gms:play-services-ads:17.1.1'
}
```

- AndroidManifest.xml into the following code: :
```
<manifest>
    <application>
        <meta-data
            android:name="com.google.android.gms.ads.APPLICATION_ID"
            android:value="ca-app-pub-4317187826403980~1326261366"/>
    </application>
</manifest>
```

Google recommends that you use the id values that they give yourself if you are working on virtual additions first. I'm gonna use them. You can update with your own id values when you want to throw them to Play Store.

```
public class MainActivity extends AppCompatActivity {

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, "ca-app-pub-4317187826403980~1326261366");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

    }
```
We have created a private-type transition ad object. We have given the id value of the object. We made a request to upload the ad.

```
public void showAds(View view) {

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

    }
```
We've made a request for advertising and will display on the screen if the ad is loaded. If it is not installed, it will print a message in the form of an ad not yet loaded on the Log screen. We made these events in the method for clicking the button.

# Happy ending

![Interstitial Ad](https://cdn-images-1.medium.com/max/800/1*bEOfuNrGaPBUB_OSp4_1jQ.png)


# MY APPLICATION ( Permenantly Word Memorize )

![Interstitial Ad](https://play.google.com/store/apps/details?id=com.gokyazilim.word_memorize)

I'll be happy if you support my application.
