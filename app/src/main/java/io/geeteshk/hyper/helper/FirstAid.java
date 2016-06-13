package io.geeteshk.hyper.helper;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import io.geeteshk.hyper.Constants;
import io.geeteshk.hyper.R;
import io.geeteshk.hyper.util.JsonUtil;
import io.geeteshk.hyper.util.PreferenceUtil;
import io.geeteshk.hyper.util.ProjectUtil;
import io.geeteshk.hyper.util.ValidatorUtil;

public class FirstAid {

    private static int[] mStatus = new int[]{0, 0, 0, 0, 0, 0};

    private static boolean repair(Context context, String name, String author, String description, String keywords) {
        boolean success = true;
        if (mStatus[0] == 1) {
            success = success && JsonUtil.createProjectFile(name, author, description, keywords, "#000000");
            mStatus[0] = 0;
        }

        if (mStatus[1] == 1) {
            success = success && ProjectUtil.createFile(name, "index.html", ProjectUtil.INDEX.replace("@name", name).replace("@author", author).replace("@description", description).replace("@keywords", keywords).replace("@color", "#000000"));
            mStatus[1] = 0;
        }

        if (mStatus[2] == 1) {
            success = success && ProjectUtil.createDirectory(name + File.separator + "js");
            success = success && ProjectUtil.createFile(name, "js" + File.separator + "main.js", ProjectUtil.MAIN);
            mStatus[2] = 0;
        }

        if (mStatus[3] == 1) {
            success = success && ProjectUtil.createDirectory(name + File.separator + "css");
            success = success && ProjectUtil.createFile(name, "css" + File.separator + "style.css", ProjectUtil.STYLE);
            mStatus[3] = 0;
        }

        if (mStatus[4] == 1) {
            success = success && ProjectUtil.copyIcon(context, name);
            mStatus[4] = 0;
        }

        if (mStatus[5] == 1) {
            success = success && ProjectUtil.createDirectory(name + File.separator + "fonts");
            mStatus[5] = 0;
        }

        return success;
    }

    public static void repairAll(final Context context) {
        String[] objects = new File(Constants.HYPER_ROOT).list();
        for (final String object : objects) {
            if (isBroken(object)) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                View layout = inflater.inflate(R.layout.dialog_repair, null);

                final TextInputLayout authorLayout, descLayout, keyLayout;
                authorLayout = (TextInputLayout) layout.findViewById(R.id.author_layout);
                descLayout = (TextInputLayout) layout.findViewById(R.id.description_layout);
                keyLayout = (TextInputLayout) layout.findViewById(R.id.keywords_layout);

                AlertDialog.Builder builder;
                if (PreferenceUtil.get(context, "dark_theme", false)) {
                    builder = new AlertDialog.Builder(context, R.style.Hyper_Dark);
                } else {
                    builder = new AlertDialog.Builder(context);
                }

                builder.setTitle("Repair " + object);
                builder.setView(layout);
                builder.setPositiveButton("REPAIR", null);

                final AppCompatDialog dialog = builder.create();
                dialog.show();

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ValidatorUtil.validate(null, authorLayout, descLayout, keyLayout)) {
                            if (repair(context, object, authorLayout.getEditText().getText().toString(), descLayout.getEditText().getText().toString(), keyLayout.getEditText().getText().toString())) {
                                Toast.makeText(context, object + " repaired.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, object + " failed.", Toast.LENGTH_SHORT).show();
                            }

                            dialog.dismiss();
                        }
                    }
                });
            }
        }
    }

    private static boolean isBroken(String string) {
        boolean out = false;
        if (!new File(Constants.HYPER_ROOT + File.separator + string + File.separator + string + ".hyper").exists()) {
            out = true;
            mStatus[0] = 1;
        }

        if (!new File(Constants.HYPER_ROOT + File.separator + string + File.separator + "index.html").exists()) {
            out = true;
            mStatus[1] = 1;
        }

        if (!new File(Constants.HYPER_ROOT + File.separator + string + File.separator + "js" + File.separator + "main.js").exists()) {
            out = true;
            mStatus[2] = 1;
        }

        if (!new File(Constants.HYPER_ROOT + File.separator + string + File.separator + "css" + File.separator + "style.css").exists()) {
            out = true;
            mStatus[3] = 1;
        }

        if (!new File(Constants.HYPER_ROOT + File.separator + string + File.separator + "images" + File.separator + "favicon.ico").exists()) {
            out = true;
            mStatus[4] = 1;
        }

        if (!new File(Constants.HYPER_ROOT + File.separator + string + File.separator + "fonts").isDirectory()) {
            out = true;
            mStatus[5] = 1;
        }

        return out;
    }
}
