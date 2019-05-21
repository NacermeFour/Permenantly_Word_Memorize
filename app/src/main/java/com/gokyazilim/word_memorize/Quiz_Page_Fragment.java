package com.gokyazilim.word_memorize;


import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class Quiz_Page_Fragment extends Fragment {

    private InterstitialAd mInterstitialAd;


    ImageView star_1, star_2, star_3, star_4, star_5, slash,win,finish,bos;
    TextView word, kacinci_kelime, toplam_kelime,word_mean, message, message2, true_skor_title,false_skor_title,true_skor,false_skor;
    Button see_word_mean,true_,false_,repeat,next;

    ArrayList<HashMap<String, String>> unknownList;

    int unKnownCount, random, false_count = 0, true_count = 0, arraylist_temp;
    ArrayList<String> arrayList;

    private int ads_count = 0;

   //private Animation mBounceAnimation;



    public Quiz_Page_Fragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.quiz_page, container, false);

        MobileAds.initialize(getContext(), "ca-app-pub-xxxxxxx~xxxxxxxxx");
        mInterstitialAd = new InterstitialAd(getContext());

        mInterstitialAd.setAdUnitId("ca-app-pub-xxxxxxx/xxxxxxx");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        // ImageView
        star_1 = view.findViewById(R.id.star_1);
        star_2 = view.findViewById(R.id.star_2);
        star_3 = view.findViewById(R.id.star_3);
        star_4 = view.findViewById(R.id.star_4);
        star_5 = view.findViewById(R.id.star_5);
        slash = view.findViewById(R.id.slash);
        win = view.findViewById(R.id.win);
        finish = view.findViewById(R.id.finish);
        bos = view.findViewById(R.id.bos);

        // TextView
        word = view.findViewById(R.id.word);
        kacinci_kelime = view.findViewById(R.id.kacinci_kelime);
        toplam_kelime = view.findViewById(R.id.toplam_kelime);
        word_mean = view.findViewById(R.id.word_mean);
        message = view.findViewById(R.id.message);
        message2 = view.findViewById(R.id.message2);
        true_skor = view.findViewById(R.id.true_skor);
        false_skor = view.findViewById(R.id.false_skor);
        true_skor_title = view.findViewById(R.id.true_skor_title);
        false_skor_title = view.findViewById(R.id.false_skor_title);

        // Button
        see_word_mean = view.findViewById(R.id.see_word_mean);
        true_ = view.findViewById(R.id.true_);
        false_ = view.findViewById(R.id.false_);
        repeat = view.findViewById(R.id.repeat);
        next = view.findViewById(R.id.next);

        arrayList = new ArrayList<String>();

        Database db = new Database(getContext()); // Db bağlantısı oluşturuyoruz.
        unknownList = db.wordUnknown();

        unKnownCount = unknownList.size();

        if(unKnownCount <= 0){
            word_empty_screen();
        }else{

            default_screen();

            toplam_kelime.setText(String.valueOf(unKnownCount));

            for (int i=0;i<unKnownCount;i++)
                arrayList.add(String.valueOf(i)+"s");

            arraylist_temp = arrayList.size();

            kacinci_kelime.setText(String.valueOf(Math.abs(arraylist_temp - arrayList.size() ) + 1 ));

            Random r = new Random();
            random = r.nextInt(unKnownCount);

            arrayList.remove(String.valueOf(random)+"s");

            word.setText(unknownList.get(random).get("word_origin"));
            word_mean.setText(unknownList.get(random).get("word_mean"));

            int star = Integer.parseInt(unknownList.get(random).get("word_star"));

            if(star >= 0)
                yellow_star(star);
            else
                black_star(star);

        }


        mInterstitialAd.setAdListener(new AdListener() {

            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });



        true_.setOnTouchListener (new View.OnTouchListener () {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction ()){

                    case MotionEvent.ACTION_UP:{


                        true_count++;

                        ads_count++;

                        if(ads_count%15 == 0){
                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                            }
                        }

                        Button view = (Button) v;
                        view.getBackground ().clearColorFilter ();
                        view.invalidate ();

                        int x = Integer.parseInt(unknownList.get(random).get("id"));
                        int y = Integer.parseInt(unknownList.get(random).get("word_star"));
                        Database db = new Database(getContext());
                        db.wordUpdate(x,y,"true_");

                        if(y+1 >= 5){
                            yellow_star(y+1);
                            win_screen();
                            break;
                        }else{

                            Random r = new Random();
                            random = r.nextInt(unKnownCount);

                            if(arrayList.size() <= 0){

                                if (mInterstitialAd.isLoaded()) {
                                    mInterstitialAd.show();
                                }

                                true_skor.setText(String.valueOf(true_count));
                                false_skor.setText(String.valueOf(false_count));

                                quiz_finish();
                            }else{
                                while( !( arrayList.contains(String.valueOf(random)+"s") ) ){

                                    random = r.nextInt(unKnownCount);
                                }

                                kacinci_kelime.setText(String.valueOf(Math.abs(arraylist_temp - arrayList.size() ) + 1 ));

                                arrayList.remove(String.valueOf(random)+"s");
                                default_screen();

                                int star_state = Integer.parseInt(unknownList.get(random).get("word_star"));

                                if(star_state >= 0)
                                    yellow_star(star_state);
                                else
                                    black_star(star_state);

                                word.setText(unknownList.get(random).get("word_origin"));
                                word_mean.setText(unknownList.get(random).get("word_mean"));
                            }
                        }

                        break;
                    }
                    case MotionEvent.ACTION_DOWN:{
                        Button view = (Button) v;
                        view.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_CANCEL:{
                        Button view = (Button) v;
                        view.getBackground ().clearColorFilter ();
                        view.invalidate ();
                        break;
                    }
                }
            return false; }

        });

        false_.setOnTouchListener (new View.OnTouchListener () {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction ()){

                    case MotionEvent.ACTION_UP:{

                        false_count++;

                        ads_count++;

                        if(ads_count%15 == 0){
                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                            }
                        }

                        Button view = (Button) v;
                        view.getBackground ().clearColorFilter ();
                        view.invalidate ();

                        int x = Integer.parseInt(unknownList.get(random).get("id"));
                        int y = Integer.parseInt(unknownList.get(random).get("word_star"));

                        if(y > -5) {
                            Database db = new Database(getContext());
                            db.wordUpdate(x, y, "false_");
                        }


                            Random r = new Random();
                            random = r.nextInt(unKnownCount);

                        if(arrayList.size() <= 0){

                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                            }

                            true_skor.setText(String.valueOf(true_count));
                            false_skor.setText(String.valueOf(false_count));

                            quiz_finish();
                        }else{
                            while( !( arrayList.contains(String.valueOf(random)+"s") ) ){

                                random = r.nextInt(unKnownCount);
                            }

                            kacinci_kelime.setText(String.valueOf(Math.abs(arraylist_temp - arrayList.size() ) + 1 ));

                            arrayList.remove(String.valueOf(random)+"s");
                            default_screen();

                            int star_state = Integer.parseInt(unknownList.get(random).get("word_star"));

                            if(star_state >= 0)
                                yellow_star(star_state);
                            else
                                black_star(star_state);

                            word.setText(unknownList.get(random).get("word_origin"));
                            word_mean.setText(unknownList.get(random).get("word_mean"));
                        }


                        break;
                    }
                    case MotionEvent.ACTION_DOWN:{
                        Button view = (Button) v;
                        view.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_CANCEL:{
                        Button view = (Button) v;
                        view.getBackground ().clearColorFilter ();
                        view.invalidate ();
                        break;
                    }
                }
                return false; }

        });


        see_word_mean.setOnTouchListener (new View.OnTouchListener () {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction ()){

                    case MotionEvent.ACTION_UP:{

                            cevap_screen();

                          Button view = (Button) v;
                         view.setBackground(getResources().getDrawable(R.drawable.sonuk));
                        break;
                    }
                    case MotionEvent.ACTION_DOWN:{
                        Button view = (Button) v;
                        view.setBackground(getResources().getDrawable(R.drawable.yanik));
                        break;
                    }
                    case MotionEvent.ACTION_CANCEL:{
                         Button view = (Button) v;
                         view.setBackground(getResources().getDrawable(R.drawable.sonuk));
                        break;
                    }
                }
                return false; }

        });

        next.setOnTouchListener (new View.OnTouchListener () {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction ()){

                    case MotionEvent.ACTION_UP:{

                        Random r = new Random();
                        random = r.nextInt(unKnownCount);

                        ads_count++;

                        if(ads_count%15 == 0){
                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                            }
                        }


                        if(arrayList.size() <= 0){

                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                            }

                            true_skor.setText(String.valueOf(true_count));
                            false_skor.setText(String.valueOf(false_count));

                            quiz_finish();
                        }else{
                            while( !( arrayList.contains(String.valueOf(random)+"s") ) ){

                                random = r.nextInt(unKnownCount);
                            }

                            kacinci_kelime.setText(String.valueOf(arrayList.size()));

                            arrayList.remove(String.valueOf(random)+"s");
                            default_screen();

                            int star_state = Integer.parseInt(unknownList.get(random).get("word_star"));

                            if(star_state >= 0)
                                yellow_star(star_state);
                            else
                                black_star(star_state);

                            word.setText(unknownList.get(random).get("word_origin"));
                            word_mean.setText(unknownList.get(random).get("word_mean"));
                        }

                        Button view = (Button) v;
                        view.getBackground ().clearColorFilter ();
                        view.invalidate ();
                        break;
                    }
                    case MotionEvent.ACTION_DOWN:{
                        Button view = (Button) v;
                        view.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_CANCEL:{

                        Button view = (Button) v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return false; }

        });


        repeat.setOnTouchListener (new View.OnTouchListener () {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction ()){

                    case MotionEvent.ACTION_UP:{

                        FragmentTransaction ft3 = getFragmentManager().beginTransaction();
                        ft3.replace(R.id.page_fragment,new Quiz_Page_Fragment());
                        ft3.commit();

                        //  Button view = (Button) v;
                        // view.setBackground(R.drawable.add));
                        Button view = (Button) v;
                        view.getBackground ().clearColorFilter ();
                        view.invalidate ();
                        break;
                    }
                    case MotionEvent.ACTION_DOWN:{
                        Button view = (Button) v;
                        view.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        // Button view = (Button) v;
                        // view.setBackground(R.drawable.add_pressed));
                        break;
                    }
                    case MotionEvent.ACTION_CANCEL:{
                        // Button view = (Button) v;
                        // view.setBackground(R.drawable.add));
                        Button view = (Button) v;
                        view.getBackground ().clearColorFilter ();
                        view.invalidate ();
                        break;
                    }
                }
                return false; }

        });


        return view;
    }


    void cevap_screen(){

        star_1.setVisibility(View.VISIBLE);
        star_2.setVisibility(View.VISIBLE);
        star_3.setVisibility(View.VISIBLE);
        star_4.setVisibility(View.VISIBLE);
        star_5.setVisibility(View.VISIBLE);
        slash.setVisibility(View.VISIBLE);
        win.setVisibility(View.INVISIBLE);
        finish.setVisibility(View.INVISIBLE);
        bos.setVisibility(View.INVISIBLE);


        word.setVisibility(View.VISIBLE);
        kacinci_kelime.setVisibility(View.VISIBLE);
        toplam_kelime.setVisibility(View.VISIBLE);
        word_mean.setVisibility(View.VISIBLE);
        message.setVisibility(View.INVISIBLE);
        message2.setVisibility(View.INVISIBLE);
        true_skor_title.setVisibility(View.INVISIBLE);
        false_skor_title.setVisibility(View.INVISIBLE);
        true_skor.setVisibility(View.INVISIBLE);
        false_skor.setVisibility(View.INVISIBLE);

        see_word_mean.setVisibility(View.INVISIBLE);
        true_.setVisibility(View.VISIBLE);
        false_.setVisibility(View.VISIBLE);
        repeat.setVisibility(View.INVISIBLE);
        next.setVisibility(View.INVISIBLE);

    }

    void quiz_finish(){

        star_1.setVisibility(View.INVISIBLE);
        star_2.setVisibility(View.INVISIBLE);
        star_3.setVisibility(View.INVISIBLE);
        star_4.setVisibility(View.INVISIBLE);
        star_5.setVisibility(View.INVISIBLE);
        slash.setVisibility(View.INVISIBLE);
        win.setVisibility(View.INVISIBLE);
        finish.setVisibility(View.VISIBLE);
        bos.setVisibility(View.INVISIBLE);


        word.setVisibility(View.INVISIBLE);
        kacinci_kelime.setVisibility(View.INVISIBLE);
        toplam_kelime.setVisibility(View.INVISIBLE);
        word_mean.setVisibility(View.INVISIBLE);
        message.setVisibility(View.INVISIBLE);
        message2.setVisibility(View.INVISIBLE);
        true_skor_title.setVisibility(View.VISIBLE);
        false_skor_title.setVisibility(View.VISIBLE);
        true_skor.setVisibility(View.VISIBLE);
        false_skor.setVisibility(View.VISIBLE);

        see_word_mean.setVisibility(View.INVISIBLE);
        true_.setVisibility(View.INVISIBLE);
        false_.setVisibility(View.INVISIBLE);
        repeat.setVisibility(View.VISIBLE);
        next.setVisibility(View.INVISIBLE);


    }

    void word_empty_screen(){

        star_1.setVisibility(View.INVISIBLE);
        star_2.setVisibility(View.INVISIBLE);
        star_3.setVisibility(View.INVISIBLE);
        star_4.setVisibility(View.INVISIBLE);
        star_5.setVisibility(View.INVISIBLE);
        slash.setVisibility(View.INVISIBLE);
        win.setVisibility(View.INVISIBLE);
        finish.setVisibility(View.INVISIBLE);
        bos.setVisibility(View.VISIBLE);

        word.setVisibility(View.INVISIBLE);
        kacinci_kelime.setVisibility(View.INVISIBLE);
        toplam_kelime.setVisibility(View.INVISIBLE);
        word_mean.setVisibility(View.INVISIBLE);
        message.setVisibility(View.VISIBLE);
        message2.setVisibility(View.VISIBLE);
        true_skor_title.setVisibility(View.INVISIBLE);
        false_skor_title.setVisibility(View.INVISIBLE);
        true_skor.setVisibility(View.INVISIBLE);
        false_skor.setVisibility(View.INVISIBLE);

        see_word_mean.setVisibility(View.INVISIBLE);
        true_.setVisibility(View.INVISIBLE);
        false_.setVisibility(View.INVISIBLE);
        repeat.setVisibility(View.INVISIBLE);
        next.setVisibility(View.INVISIBLE);



    }

    void win_screen(){

        star_1.setVisibility(View.VISIBLE);
        star_2.setVisibility(View.VISIBLE);
        star_3.setVisibility(View.VISIBLE);
        star_4.setVisibility(View.VISIBLE);
        star_5.setVisibility(View.VISIBLE);
        slash.setVisibility(View.VISIBLE);
        win.setVisibility(View.VISIBLE);
        finish.setVisibility(View.INVISIBLE);
        bos.setVisibility(View.INVISIBLE);


        word.setVisibility(View.VISIBLE);
        kacinci_kelime.setVisibility(View.VISIBLE);
        toplam_kelime.setVisibility(View.VISIBLE);
        word_mean.setVisibility(View.VISIBLE);
        message.setVisibility(View.INVISIBLE);
        message2.setVisibility(View.INVISIBLE);
        true_skor_title.setVisibility(View.INVISIBLE);
        false_skor_title.setVisibility(View.INVISIBLE);
        true_skor.setVisibility(View.INVISIBLE);
        false_skor.setVisibility(View.INVISIBLE);

        see_word_mean.setVisibility(View.INVISIBLE);
        true_.setVisibility(View.INVISIBLE);
        false_.setVisibility(View.INVISIBLE);
        repeat.setVisibility(View.INVISIBLE);
        next.setVisibility(View.VISIBLE);

    }

    void default_screen(){

        star_1.setVisibility(View.VISIBLE);
        star_2.setVisibility(View.VISIBLE);
        star_3.setVisibility(View.VISIBLE);
        star_4.setVisibility(View.VISIBLE);
        star_5.setVisibility(View.VISIBLE);
        slash.setVisibility(View.VISIBLE);
        win.setVisibility(View.INVISIBLE);
        finish.setVisibility(View.INVISIBLE);
        bos.setVisibility(View.INVISIBLE);


        word.setVisibility(View.VISIBLE);
        kacinci_kelime.setVisibility(View.VISIBLE);
        toplam_kelime.setVisibility(View.VISIBLE);
        word_mean.setVisibility(View.INVISIBLE);
        message.setVisibility(View.INVISIBLE);
        message2.setVisibility(View.INVISIBLE);
        true_skor_title.setVisibility(View.INVISIBLE);
        false_skor_title.setVisibility(View.INVISIBLE);
        true_skor.setVisibility(View.INVISIBLE);
        false_skor.setVisibility(View.INVISIBLE);

        see_word_mean.setVisibility(View.VISIBLE);
        true_.setVisibility(View.INVISIBLE);
        false_.setVisibility(View.INVISIBLE);
        repeat.setVisibility(View.INVISIBLE);
        next.setVisibility(View.INVISIBLE);

    }

    void yellow_star(int start_count){

        switch (start_count){
            case 0:
                star_1.setImageResource(R.drawable.bos_arti);
                star_2.setImageResource(R.drawable.bos_arti);
                star_3.setImageResource(R.drawable.bos_arti);
                star_4.setImageResource(R.drawable.bos_arti);
                star_5.setImageResource(R.drawable.bos_arti);
                break;
            case 1:
                star_1.setImageResource(R.drawable.dolu_arti);
                star_2.setImageResource(R.drawable.bos_arti);
                star_3.setImageResource(R.drawable.bos_arti);
                star_4.setImageResource(R.drawable.bos_arti);
                star_5.setImageResource(R.drawable.bos_arti);
                break;
            case 2:
                star_1.setImageResource(R.drawable.dolu_arti);
                star_2.setImageResource(R.drawable.dolu_arti);
                star_3.setImageResource(R.drawable.bos_arti);
                star_4.setImageResource(R.drawable.bos_arti);
                star_5.setImageResource(R.drawable.bos_arti);
                break;
            case 3:
                star_1.setImageResource(R.drawable.dolu_arti);
                star_2.setImageResource(R.drawable.dolu_arti);
                star_3.setImageResource(R.drawable.dolu_arti);
                star_4.setImageResource(R.drawable.bos_arti);
                star_5.setImageResource(R.drawable.bos_arti);
                break;
            case 4:
                star_1.setImageResource(R.drawable.dolu_arti);
                star_2.setImageResource(R.drawable.dolu_arti);
                star_3.setImageResource(R.drawable.dolu_arti);
                star_4.setImageResource(R.drawable.dolu_arti);
                star_5.setImageResource(R.drawable.bos_arti);
                break;
            case 5:
                star_1.setImageResource(R.drawable.dolu_arti);
                star_2.setImageResource(R.drawable.dolu_arti);
                star_3.setImageResource(R.drawable.dolu_arti);
                star_4.setImageResource(R.drawable.dolu_arti);
                star_5.setImageResource(R.drawable.dolu_arti);
                break;
        }

    }

    void black_star(int start_count){

        switch (start_count){
            case -1:
                star_1.setImageResource(R.drawable.dolu_eksi);
                star_2.setImageResource(R.drawable.bos_eksi);
                star_3.setImageResource(R.drawable.bos_eksi);
                star_4.setImageResource(R.drawable.bos_eksi);
                star_5.setImageResource(R.drawable.bos_eksi);
                break;
            case -2:
                star_1.setImageResource(R.drawable.dolu_eksi);
                star_2.setImageResource(R.drawable.dolu_eksi);
                star_3.setImageResource(R.drawable.bos_eksi);
                star_4.setImageResource(R.drawable.bos_eksi);
                star_5.setImageResource(R.drawable.bos_eksi);
                break;
            case -3:
                star_1.setImageResource(R.drawable.dolu_eksi);
                star_2.setImageResource(R.drawable.dolu_eksi);
                star_3.setImageResource(R.drawable.dolu_eksi);
                star_4.setImageResource(R.drawable.bos_eksi);
                star_5.setImageResource(R.drawable.bos_eksi);
                break;
            case -4:
                star_1.setImageResource(R.drawable.dolu_eksi);
                star_2.setImageResource(R.drawable.dolu_eksi);
                star_3.setImageResource(R.drawable.dolu_eksi);
                star_4.setImageResource(R.drawable.dolu_eksi);
                star_5.setImageResource(R.drawable.bos_eksi);
                break;
            case -5:
                star_1.setImageResource(R.drawable.dolu_eksi);
                star_2.setImageResource(R.drawable.dolu_eksi);
                star_3.setImageResource(R.drawable.dolu_eksi);
                star_4.setImageResource(R.drawable.dolu_eksi);
                star_5.setImageResource(R.drawable.dolu_eksi);
                break;
        }

    }


}
