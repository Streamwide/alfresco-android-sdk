/*
 *
 * 	StreamWIDE (Team on The Run)
 *
 * @createdBy  AndroidTeam on ven., 6 mars 2020 12:06:44 +0100
 * @copyright  Copyright (c) 2020 StreamWIDE UK Ltd (Team on the Run)
 * @email      support@teamontherun.com
 *
 * 	© Copyright 2020 StreamWIDE UK Ltd (Team on the Run). StreamWIDE is the copyright holder
 * 	of all code contained in this file. Do not redistribute or
 *  	re-use without permission.
 *
 * @lastModifiedOn ven., 6 mars 2020 11:42:30 +0100
 */
package com.streamwide.smartms.lib.alfresco.ui.alfresco;

import com.streamwide.smartms.lib.alfresco.R;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public class AlfrescoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private AlfrescoClickListener mAlfrescoClickListener;


    @NonNull private AppCompatImageView folderIcon;
    @NonNull private AppCompatTextView folderItemName;
    @NonNull private AppCompatTextView folderItemInfos;
    @NonNull private AppCompatImageView folderArrow;

    AlfrescoViewHolder(View itemView, AlfrescoClickListener alfrescoClickListener)
    {
        super(itemView);
        this.mAlfrescoClickListener = alfrescoClickListener;
        this.folderIcon = itemView.findViewById(R.id.folder_icon);
        this.folderItemName = itemView.findViewById(R.id.folder_item_name);
        this.folderItemInfos = itemView.findViewById(R.id.folder_item_infos);
        this.folderArrow = itemView.findViewById(R.id.folder_arrow);
        this.itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(@NonNull View v)
    {
        if (mAlfrescoClickListener != null) {
            mAlfrescoClickListener.onAlfrescoItemClick(v, getAdapterPosition());
        }
    }

    @NonNull
     AppCompatImageView getFolderIcon() {
        return folderIcon;
    }

    @NonNull
     AppCompatTextView getFolderIteName() {
        return folderItemName;
    }

    @NonNull
     AppCompatTextView getFolderIteInfos() {
        return folderItemInfos;
    }

    @NonNull
     AppCompatImageView getFolderArrow() {
        return folderArrow;
    }


}
