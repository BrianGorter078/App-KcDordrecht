package nl.kcdordrecht.kcdordrechtnews.activity;

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
        getListOfFiles();
        createListview(rootView);
        return rootView;
    }


    public void getListOfFiles() {
        AssetManager assetManager = getActivity().getAssets();
        try {
            String[] fileNames = assetManager.list("");
            for (int i = 0; i < fileNames.length; i++) {
                if (fileNames[i].contains(".pdf")) {
                    if (fileNames[i].contains("wb")) {
                        fileNames[i] = fileNames[i].replace(".pdf", "");
                        fileNames[i] = fileNames[i].replace("wb", "");
                        clubbladen.add(fileNames[i]);
                    } else {

                    }
                }
            }
        } catch (IOException e) {
        }
    }

    public void createListview(View rootView) {
        final List<String> clubbladenList = Lists.reverse(clubbladen);
        ArrayAdapter clubbladenAdapter = new ArrayAdapter<>(getActivity(), R.layout.customlist, R.id.textxx, clubbladenList);

        ListView clubbladenView = (ListView) rootView.findViewById(R.id.listView);
        clubbladenView.setAdapter(clubbladenAdapter);

        clubbladenView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    OpenClubblad(clubbladenList.get(position));
                } catch (IOException e) {
                }
            }
        });
    }


    public void OpenClubblad(String clubbladNumber) throws IOException {

        AssetManager assetManager = getActivity().getAssets();

        InputStream in;
        OutputStream out;

        String fileName = "wb" + clubbladNumber + ".pdf";
        File file = new File(getActivity().getFilesDir(), "wb" + clubbladNumber + ".pdf");

        try {
            in = assetManager.open(fileName);
            out = getActivity().openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);

            copyFile(in, out);
            in.close();
            out.flush();
            out.close();

            openPDF(clubbladNumber);


        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }


    }


    public void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }


    }


    public void openPDF(String number) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(
                Uri.parse("file://" + getActivity().getFilesDir() + "/wb" + number + ".pdf"),
                "application/pdf");

        startActivity(intent);
    }
}
