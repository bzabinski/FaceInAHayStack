package com.mathengie.faceinahaystack;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    public String chosenDir;
    public String chosenFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new StartingFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class StartingFragment extends Fragment {

        String tempDir;

        public StartingFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_select_folder, container, false);
            return rootView;
        }
    }

    public static class MiddleFragment extends Fragment {


        public MiddleFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_get_picture, container, false);
            return rootView;
        }
    }

    public void gotoFolderList(View view) {

        DirectoryChooserDialog directoryChooserDialog = new DirectoryChooserDialog(MainActivity.this, new DirectoryChooserDialog.ChosenDirectoryListener() {
            @Override
            public void onChosenDir(String chosenDir2) {
                chosenDir = chosenDir2;
                Log.d("chosenDir", chosenDir);
                TextView t = (TextView)findViewById(R.id.folderText);
                t.setText(chosenDir);
                Button bt = (Button)findViewById(R.id.nextButton);
                bt.setEnabled(true);
            }
        });
        directoryChooserDialog.chooseDirectory(chosenDir);
    }

    public void gotoFileSelect(View view) {
        // Create new fragment and transaction
        Fragment newFragment = new MiddleFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }


    public void gotoFileList(View view) {

        FileChooserDialog directoryChooserDialog = new FileChooserDialog(MainActivity.this, new FileChooserDialog.ChosenDirectoryListener() {
            @Override
            public void onChosenFile(String chosenFile2) {
                chosenFile = chosenFile2;
                Log.d("chosenDir", chosenFile);
                TextView t = (TextView)findViewById(R.id.fileView);
                t.setText(chosenFile);
                Button bt = (Button)findViewById(R.id.nextButton2);
                bt.setEnabled(true);
            }
        });
        directoryChooserDialog.chooseDirectory();
    }
}
