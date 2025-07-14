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
 * @lastModifiedOn ven., 6 mars 2020 11:41:52 +0100
 */
package com.streamwide.smartms.lib.alfresco.ui.alfresco;

import java.util.Date;
import java.util.List;

import org.alfresco.mobile.android.api.model.Document;
import org.alfresco.mobile.android.api.model.Node;

import com.streamwide.smartms.lib.alfresco.R;
import com.streamwide.smartms.lib.alfresco.util.AttachmentUtil;
import com.streamwide.smartms.lib.alfresco.util.CollectionUtil;
import com.streamwide.smartms.lib.alfresco.util.Utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class AlfrescoRecyclerAdapter extends RecyclerView.Adapter<AlfrescoViewHolder> {

    private Context mContext;
    private List<Node> mListNodes;
    private AlfrescoClickListener mAlfrescoClickListener;

    public AlfrescoRecyclerAdapter(@NonNull Context context, @NonNull List<Node> listNodes, @NonNull AlfrescoClickListener alfrescoClickListener)
    {
        this.mContext = context;
        this.mListNodes = CollectionUtil.copyList(listNodes);
        this.mAlfrescoClickListener = alfrescoClickListener;
    }

    @Override
    @NonNull
    public AlfrescoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view =
            LayoutInflater.from(parent.getContext()).inflate(R.layout.alfresco_repository_item_view, parent, false);
        return new AlfrescoViewHolder(view, mAlfrescoClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AlfrescoViewHolder holder, int position)
    {
        Node currentNode = mListNodes.get(position);
        if (currentNode.isFolder()) {
            holder.getFolderArrow().setVisibility(View.VISIBLE);
            holder.getFolderIteInfos().setVisibility(View.GONE);
            holder.getFolderIcon().setImageResource(R.drawable.ic_alfresco_folder);
            holder.getFolderIcon().setBackgroundResource(0);
        } else if (currentNode.isDocument()) {

            String mimeType = ((Document) currentNode).getContentStreamMimeType();

            String contentLengthFormatted = android.text.format.Formatter.formatShortFileSize(mContext,
                            ((Document) currentNode).getContentStreamLength());

            long modificationDateTime = (currentNode).getModifiedAt().getTimeInMillis();
            String modificationDate = Utility.getDayStyle(mContext, new Date(modificationDateTime));

            holder.getFolderIteInfos().setVisibility(View.VISIBLE);
            holder.getFolderIteInfos().setText(mContext.getString(R.string.alfresco_repository_inofs,
                            contentLengthFormatted, modificationDate));
            holder.getFolderArrow().setVisibility(View.GONE);

            Integer iconResourceId = (AttachmentUtil.getIconFilekeyMap().get(mimeType));

            holder.getFolderIcon().setImageResource(iconResourceId == null ? R.drawable.icon_generic_attachment : iconResourceId);
            holder.getFolderIcon().setBackgroundResource(R.drawable.alfresco_bg_file_preview);
        }
        holder.getFolderIteName().setText(currentNode.getName());
    }

    @Override
    public int getItemCount()
    {
        return mListNodes == null ? 0 : mListNodes.size();
    }

    @Nullable
    public Node getItem(int position)
    {
        if (mListNodes != null) {
            return mListNodes.get(position);
        }
        return null;
    }
}
