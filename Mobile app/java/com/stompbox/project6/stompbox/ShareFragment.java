package com.stompbox.project6.stompbox;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mrpatel5 on 4/6/2017.
 */

public class ShareFragment extends Fragment{
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.share_fragment, container, false);
    }
}
