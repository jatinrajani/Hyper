package io.geeteshk.hyper;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import io.geeteshk.hyper.adapter.ResourceAdapter;
import io.geeteshk.hyper.util.PreferenceUtil;

/**
 * Activity to list resources of a certain project
 */
public class ResourcesActivity extends AppCompatActivity {

    List<String> mListDataHeader;
    HashMap<String, List<String>> mListDataChild;

    /**
     * Called when the activity is created
     *
     * @param savedInstanceState restored when onResume is called
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Resources");
        }

        prepare();

        ExpandableListView listView = (ExpandableListView) findViewById(R.id.resources_list);
        ResourceAdapter adapter = new ResourceAdapter(this, mListDataHeader, mListDataChild);
        assert listView != null;
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    final int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                    final int childPosition = ExpandableListView.getPackedPositionChild(id);

                    AlertDialog.Builder builder = new AlertDialog.Builder(ResourcesActivity.this);
                    builder.setTitle("Delete Resource?");
                    builder.setMessage("Are you sure?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (!mListDataChild.get(mListDataHeader.get(groupPosition)).get(childPosition).equals("index.html")
                                    && !mListDataChild.get(mListDataHeader.get(groupPosition)).get(childPosition).equals("style.css")
                                    && !mListDataChild.get(mListDataHeader.get(groupPosition)).get(childPosition).equals("main.js")
                                    && !mListDataChild.get(mListDataHeader.get(groupPosition)).get(childPosition).equals("favicon.ico")) {
                                if (Resources.remove(getIntent().getStringExtra("project"), mListDataHeader.get(groupPosition).toLowerCase(), mListDataChild.get(mListDataHeader.get(groupPosition)).get(childPosition))) {
                                    Toast.makeText(ResourcesActivity.this, "Successfully deleted.", Toast.LENGTH_SHORT).show();
                                    recreate();
                                } else {
                                    Toast.makeText(ResourcesActivity.this, "Unable to delete resource.", Toast.LENGTH_SHORT).show();
                                    recreate();
                                }
                            } else {
                                Toast.makeText(ResourcesActivity.this, "You can't delete this file!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    AppCompatDialog dialog = builder.create();
                    dialog.show();

                    return true;
                }

                return false;
            }
        });

        if (!PreferenceUtil.get(this, "dark_theme", false)) {

        } else {

        }
    }

    private void prepare() {
        mListDataHeader = new ArrayList<>();
        mListDataChild = new HashMap<>();

        mListDataHeader.add("IMAGES");
        mListDataHeader.add("FONTS");
        mListDataHeader.add("CSS");
        mListDataHeader.add("JS");

        for (int i = 0; i < mListDataHeader.size(); i++) {
            mListDataChild.put(mListDataHeader.get(i), Arrays.asList(Resources.get(getIntent().getStringExtra("project"), mListDataHeader.get(i).toLowerCase())));
        }
    }

    /**
     * Helper class to get and remove resources
     */
    private static class Resources {

        /**
         * Gets a list of resources
         *
         * @param project  name of project
         * @param resource type of resource to list
         * @return list of resources
         */
        public static String[] get(String project, String resource) {
            return new File(Constants.HYPER_ROOT + File.separator + project + File.separator + resource).list();
        }

        /**
         * Removes a specific resource from project
         *
         * @param project  name of project
         * @param resource type of resource to delete
         * @param name     of resource
         * @return true if deleted
         */
        public static boolean remove(String project, String resource, String name) {
            return new File(Constants.HYPER_ROOT + File.separator + project + File.separator + resource + File.separator + name).delete();
        }
    }
}
