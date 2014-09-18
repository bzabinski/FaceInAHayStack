package com.mathengie.faceinahaystack;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class FileChooserDialog {
    private String ROOT_DIR;

    private Context mainContext;
    private TextView mainTitleView;

    private String currentDir;
    private String currentFile;
    private List<String> currentSubDirs;
    private ChosenDirectoryListener mainChosenDirectoryListener;
    private ArrayAdapter<String> mainListAdapter;

    public interface ChosenDirectoryListener {
        public void onChosenFile(String chosenFile);
    }

    public FileChooserDialog(Context context, ChosenDirectoryListener chosenDirectoryListener) {
        ROOT_DIR = Environment.getExternalStorageDirectory().getPath();
        mainContext = context;
        mainChosenDirectoryListener = chosenDirectoryListener;
        try {
            new File(ROOT_DIR).getCanonicalPath();
        } catch (IOException ioe) {
            //nothing
        }
    }

    public void chooseDirectory() {
        chooseDirectory(ROOT_DIR);
    }

    public void chooseDirectory(String dir) {
        if(dir == null)
        {
            dir = ROOT_DIR;
        }
        File dirFile = new File(dir);
        if (!(dirFile.exists() && dirFile.isDirectory())) {
            dir = ROOT_DIR;
        }

        try {
            dir = new File(dir).getCanonicalPath();
        } catch (IOException ioe) {
            return;
        }

        currentDir = dir;
        currentSubDirs = getDirectories(dir);

        class DirectoryOnClickListener implements OnClickListener {
            public void onClick(DialogInterface dialog, int item) {
                if (IsDirectory((String)((AlertDialog) dialog).getListView().getAdapter().getItem(item)))
                {
                    currentDir += "/" + ((AlertDialog) dialog).getListView().getAdapter().getItem(item);
                    updateDirectory();
                }
                else
                {
                    currentFile = currentDir + "/" + ((AlertDialog) dialog).getListView().getAdapter().getItem(item);
                }

            }
        }

        AlertDialog.Builder dialogBuilder = createDirectoryChooserDialog(dir, currentSubDirs, new DirectoryOnClickListener());
        dialogBuilder.setPositiveButton("OK", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mainChosenDirectoryListener != null) {
                    mainChosenDirectoryListener.onChosenFile(currentFile);
                }
            }
        }).setNegativeButton("Cancel", null);

        final AlertDialog dirsDialog = dialogBuilder.create();

        dirsDialog.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (currentDir.equals(ROOT_DIR)) {
                        return false;
                    } else {
                        currentDir = new File(currentDir).getParent();
                        updateDirectory();
                    }

                    return true;
                } else {
                    return false;
                }
            }
        });

        dirsDialog.show();
    }

    private boolean IsDirectory(String item)
    {
        File testFile = new File(currentDir + "/" + item);

        return testFile.exists() && testFile.isDirectory();

    }

    private List<String> getDirectories(String dir) {
        List<String> dirs = new ArrayList<String>();
        Log.d("hello", dir);

        try {
            File dirFile = new File(dir);
            Log.d("exists", String.valueOf(dirFile.exists()));
            Log.d("dir", String.valueOf(dirFile.isDirectory()));
            if (!(dirFile.exists() && dirFile.isDirectory())) {
                Log.d("what", "help");
                return dirs;
            }
            if(dirFile.list() == null)
            {
                Log.e("list", "oh no");
            }
            for (File file : dirFile.listFiles()) {
                Log.d("filenames", file.getName());
                dirs.add(file.getName());
                Log.d("subDir", file.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Collections.sort(dirs, new Comparator<String>() {
            @Override
            public int compare(String s, String s2) {
                return s.compareTo(s2);
            }
        });

        return dirs;
    }

    private AlertDialog.Builder createDirectoryChooserDialog(String title, List<String> listItems, OnClickListener onClickListener) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mainContext);

        LinearLayout titleLayout = new LinearLayout(mainContext);
        titleLayout.setOrientation(LinearLayout.VERTICAL);

        mainTitleView = new TextView(mainContext);
        mainTitleView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        mainTitleView.setTextColor(mainContext.getResources().getColor(android.R.color.white));
        mainTitleView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        mainTitleView.setText(title);

        titleLayout.addView(mainTitleView);

        dialogBuilder.setCustomTitle(titleLayout);

        mainListAdapter = createListAdapter(listItems);

        dialogBuilder.setSingleChoiceItems(mainListAdapter, -1, onClickListener);
        dialogBuilder.setCancelable(false);

        return dialogBuilder;
    }

    private void updateDirectory()
    {
        currentSubDirs.clear();
        currentSubDirs.addAll(getDirectories(currentDir));
        mainTitleView.setText(currentDir);

        mainListAdapter.notifyDataSetChanged();
    }

    private ArrayAdapter<String> createListAdapter(List<String> items)
    {
        return new ArrayAdapter<String>(mainContext, android.R.layout.select_dialog_item, android.R.id.text1, items)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View v = super.getView(position, convertView, parent);

                if(v instanceof TextView)
                {
                    TextView tv = (TextView) v;
                    tv.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
                    tv.setEllipsize(null);
                }
                return v;
            }
        };
    }
}