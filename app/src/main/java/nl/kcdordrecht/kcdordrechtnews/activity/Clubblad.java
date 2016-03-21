package nl.kcdordrecht.kcdordrechtnews.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.brian.woonkamer.clubblad.R;
import com.google.common.collect.Lists;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brian on 23-1-2016.
 */
public class Clubblad extends Fragment {

    final ArrayList<String> clubbladen = new ArrayList<>();

    public Clubblad() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_clubblad, container, false);

        AssetManager assetManager = getActivity().getAssets();
        try {


            String[] paths = assetManager.list("");

            for (int i = 0; i < paths.length; i++) {

                if (paths[i].contains(".pdf")) {

                    if(paths[i].contains("zaalboek"))
                    {

                    }
                    else {


                        paths[i] = paths[i].replace(".pdf", "");
                        paths[i] = paths[i].replace("wb", "");
                        clubbladen.add(paths[i]);
                    }
                }

            }

            final List<String> listview = Lists.reverse(clubbladen);
            ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),R.layout.customlist,R.id.textxx, listview);




            ListView lv = (ListView) rootView.findViewById(R.id.listView);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    try {
                        OpenClubblad(listview.get(position));
                    }catch (IOException e)
                    {}
                }
            });

        }catch (IOException e)
        {

        }


        return rootView;
    }




    public void OpenClubblad(String number) throws IOException {

        AssetManager assetManager = getActivity().getAssets();
        InputStream in = null;
        OutputStream out = null;


        File file = new File(getActivity().getFilesDir(),"wb" + number +".pdf");



        try {
            in = assetManager.open("wb" + number +".pdf");
            out = getActivity().openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);

            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(
                Uri.parse("file://" + getActivity().getFilesDir() + "/wb" + number + ".pdf"),
                "application/pdf");

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {

        }
    }



    public void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }


    }




}
