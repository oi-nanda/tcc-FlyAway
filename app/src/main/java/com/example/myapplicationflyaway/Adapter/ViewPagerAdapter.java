package com.example.myapplicationflyaway.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplicationflyaway.R;

public class ViewPagerAdapter extends PagerAdapter {

    Context context;

    int images[] = {

            R.drawable.casal_aviaopng,
            R.drawable.amigos_na_natureza,
            R.drawable.casal_pesquisando_atracoes,
            R.drawable.casal_olhando_mapa,

    };
    int descriptions[]= {

            R.string.roteiros_facilitados,
            R.string.atracoes_locais,
            R.string.contexto,
            R.string.geolocalizacao
    };

    public ViewPagerAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return descriptions.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position){

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout,container,false);

        ImageView slideTitleImage = (ImageView) view.findViewById(R.id.imageView);
        TextView slideDescription = (TextView) view.findViewById(R.id.textDescriptionOne);


        slideTitleImage.setImageResource(images[position]);
        slideDescription.setText(descriptions[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object){
        container.removeView((LinearLayout) object);
    }
}
