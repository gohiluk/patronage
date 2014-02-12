package com.example.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gohilukk on 11.02.14.
 */
public class FileDialog extends ListActivity {
    private List<String> path = null;
    private String root="/";
    private TextView myPath;
    private final String[] okFileExtensions =  new String[] {"jpg", "png","jpeg"};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filedialog);
        myPath = (TextView)findViewById(R.id.path);

        getDir(root);
    }


    private void getDir(String dirPath)

    {
        myPath.setText("Location: " + dirPath);
        List<String> item = new ArrayList<>();
        path = new ArrayList<>();

        File f = new File(dirPath);
        File[] files;
        files = f.listFiles();

        if(!dirPath.equals(root))
        {
            item.add(root);
            path.add(root);

            item.add("../");
            path.add(f.getParent());
        }

        assert files != null;
        for (File file : files) {
            if (file.isDirectory()) {
                path.add(file.getPath());
                item.add(file.getName() + "/");
            } else {
                for (String extension : okFileExtensions) {
                    if (file.getName().toLowerCase().endsWith(extension)) {
                        path.add(file.getPath());
                        item.add(file.getName());
                    }
                }
            }
        }

        ArrayAdapter<String> fileList =
                new ArrayAdapter<>(this, R.layout.row, item);
        setListAdapter(fileList);
    }



    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        File file = new File(path.get(position));

        if (file.isDirectory())
        {
            if(file.canRead())
                getDir(path.get(position));
            else
            {
                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.ic_launcher)
                        .setTitle("[" + file.getName() + "] folder can't be read!")
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();
            }
        }
        else
        {
            Intent resultData = new Intent();
            resultData.putExtra(AddHoard.IMG_PATH, file.getPath());
            setResult(Activity.RESULT_OK, resultData);
            finish();
        }
    }
}

