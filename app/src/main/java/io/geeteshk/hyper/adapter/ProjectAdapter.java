package io.geeteshk.hyper.adapter;

import android.animation.Animator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.util.Collections;
import java.util.List;

import io.geeteshk.hyper.R;
import io.geeteshk.hyper.activity.ProjectActivity;
import io.geeteshk.hyper.helper.Constants;
import io.geeteshk.hyper.helper.Firebase;
import io.geeteshk.hyper.helper.Jason;
import io.geeteshk.hyper.helper.Project;

/**
 * Adapter to list all projects
 */
public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.MyViewHolder> {

    /**
     * Context used for various purposes such as loading files and inflating layouts
     */
    private Context mContext;

    /**
     * Array of objects to fill list
     */
    private List<String> mObjects;

    /**
     * Firebase class(es) to get user information
     * and perform specific Firebase functions
     */
    private FirebaseAuth mAuth;
    private FirebaseStorage mStorage;

    /**
     * public Constructor
     *
     * @param context loading files and inflating etc
     * @param objects objects to fill list
     * @param auth FirebaseAuth
     * @param storage FirebaseStorage
     */
    public ProjectAdapter(Context context, List<String> objects, FirebaseAuth auth, FirebaseStorage storage) {
        this.mContext = context;
        this.mObjects = objects;
        this.mAuth = auth;
        this.mStorage = storage;
    }

    public void add(String project) {
        mObjects.add(project);
        notifyDataSetChanged();
    }

    /**
     * When view holder is created
     *
     * @param parent parent view
     * @param viewType type of view
     * @return ProjectAdapter.ViewHolder
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Collections.sort(mObjects);
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_project, parent, false);
        return new MyViewHolder(itemView);
    }

    /**
     * Called when item is bound to position
     *
     * @param holder view holder
     * @param position position of item
     */
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final int newPos = holder.getAdapterPosition();

        holder.mTitle.setText(mObjects.get(position));
        holder.mDescription.setText(Jason.getProjectProperty(mObjects.get(position), "description"));
        holder.mFavicon.setImageBitmap(Project.getFavicon(mObjects.get(position)));

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProjectActivity.class);
                intent.putExtra("project", mObjects.get(newPos));
                intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

                if (Build.VERSION.SDK_INT >= 21) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                }

                ((AppCompatActivity) mContext).startActivityForResult(intent, 0);
            }
        });

        holder.mFavicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProjectActivity.class);
                intent.putExtra("project", mObjects.get(newPos));
                intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

                if (Build.VERSION.SDK_INT >= 21) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                }

                ((AppCompatActivity) mContext).startActivityForResult(intent, 0);
            }
        });

        holder.mLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(mContext.getString(R.string.delete) + " " + mObjects.get(newPos) + "?");
                builder.setMessage(R.string.change_undone);
                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Project.deleteProject(mContext, mObjects.get(newPos))) {
                            holder.itemView.animate().alpha(0).setDuration(300).setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    Firebase.removeProject(mAuth, mStorage, mObjects.get(newPos));
                                    Firebase.deleteProjectFiles(mAuth, mStorage, mObjects.get(newPos));
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {
                                    Firebase.removeProject(mAuth, mStorage, mObjects.get(newPos));
                                    Firebase.deleteProjectFiles(mAuth, mStorage, mObjects.get(newPos));
                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            });
                            Toast.makeText(mContext, mContext.getString(R.string.goodbye) + " " + mObjects.get(newPos) + ".", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(mContext, mContext.getString(R.string.oops_delete) + " " + mObjects.get(newPos) + ".", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton(R.string.cancel, null);
                builder.show();

                return true;
            }
        });

        holder.mFavicon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(mContext.getString(R.string.delete) + " " + mObjects.get(newPos) + "?");
                builder.setMessage(R.string.change_undone);
                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Project.deleteProject(mContext, mObjects.get(newPos))) {
                            holder.itemView.animate().alpha(0).setDuration(300).setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    Firebase.removeProject(mAuth, mStorage, mObjects.get(newPos));
                                    Firebase.deleteProjectFiles(mAuth, mStorage, mObjects.get(newPos));
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {
                                    Firebase.removeProject(mAuth, mStorage, mObjects.get(newPos));
                                    Firebase.deleteProjectFiles(mAuth, mStorage, mObjects.get(newPos));
                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            });
                            Toast.makeText(mContext, mContext.getString(R.string.goodbye) + " " + mObjects.get(newPos) + ".", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(mContext, mContext.getString(R.string.oops_delete) + " " + mObjects.get(newPos) + ".", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton(R.string.cancel, null);
                builder.show();

                return true;
            }
        });

        if (new File(Constants.HYPER_ROOT + File.separator + mObjects.get(position), ".git").exists() && new File(Constants.HYPER_ROOT + File.separator + mObjects.get(position), ".git").isDirectory()) {
            holder.mRepo.setImageResource(R.drawable.ic_repo);
        }

    }

    /**
     * Gets number of projects
     *
     * @return array size
     */
    @Override
    public int getItemCount() {
        return mObjects.size();
    }

    /**
     * View holder class for logs
     */
    class MyViewHolder extends RecyclerView.ViewHolder {

        /**
         * Views for view holder
         */
        TextView mTitle, mDescription;
        ImageView mFavicon, mRepo;
        LinearLayout mLayout;

        /**
         * public Constructor
         *
         * @param view root view
         */
        MyViewHolder(View view) {
            super(view);

            mTitle = (TextView) view.findViewById(R.id.title);
            mDescription = (TextView) view.findViewById(R.id.desc);
            mRepo = (ImageView) view.findViewById(R.id.git_repo);
            mFavicon = (ImageView) view.findViewById(R.id.favicon);
            mLayout = (LinearLayout) view.findViewById(R.id.project_layout);
        }
    }
}
