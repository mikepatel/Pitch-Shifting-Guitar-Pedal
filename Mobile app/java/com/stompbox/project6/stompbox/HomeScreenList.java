package com.stompbox.project6.stompbox;

import android.app.ListActivity;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by mrpatel5 on 4/23/2017.
 */

public class HomeScreenList extends ListActivity {

    InputRecorded inputRecorded;
    ArrayList<String> listItems;

    public HomeScreenList(){
        inputRecorded = new InputRecorded();
        File dir = new File(inputRecorded.dirPath);
        File[] fileList = dir.listFiles();
        listItems = new ArrayList<>(fileList.length);
        for(int i=0; i<fileList.length; i++){
            listItems.add(fileList[i].getName());
            //System.out.println(listItems.toString());
        }
    }


    /*
    ArrayList<String> itemList = new ArrayList<String>(); // list of array strings to serve as list items
    ArrayAdapter<String> arrayAdapter; // string adapter to serve as handler for data in listView

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemList);
        setListAdapter(arrayAdapter);
    }

    //
    public void addItems(View view){
        itemList.add(new RecordDialogFragment().saveField.getText().toString());
    }
    */
}
