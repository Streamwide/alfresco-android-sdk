/*
 *
 * 	StreamWIDE (Team on The Run)
 *
 * @createdBy  AndroidTeam on lun., 25 juil. 2022 16:00:03 +0200
 * @copyright  Copyright (c) 2022 StreamWIDE UK Ltd (Team on the Run)
 * @email      support@teamontherun.com
 *
 * 	© Copyright 2022 StreamWIDE UK Ltd (Team on the Run). StreamWIDE is the copyright holder
 * 	of all code contained in this file. Do not redistribute or
 *  	re-use without permission.
 *
 * @lastModifiedOn lun., 25 juil. 2022 16:00:03 +0200
 */

package com.streamwide.smartms.lib.alfresco.data.item;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.alfresco.cmis.client.impl.AlfrescoDocumentImpl;
import org.alfresco.mobile.android.api.model.Node;
import org.alfresco.mobile.android.api.model.impl.DocumentImpl;
import org.alfresco.mobile.android.api.model.impl.DocumentLinkImpl;
import org.alfresco.mobile.android.api.model.impl.FolderImpl;
import org.alfresco.mobile.android.api.model.impl.NodeImpl;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlfrescoNodeComparatorTest {

    @Test
    public void compare_two_documents() {
        final Node documentA = createDocument();
        final Node documentB = createDocument();

        final List<Node> nodes = new ArrayList<>();
        Collections.addAll(nodes, documentB, documentA);

        Collections.sort(nodes, new AlfrescoNodeComparator());

        final List<Node> expected = new ArrayList<>();
        Collections.addAll(expected, documentB, documentA);

        Assertions.assertEquals(expected, nodes);
    }

    @Test
    public void compare_two_folders() {
        final Node folderA = createFolder();
        final Node folderB = createFolder();

        final List<Node> nodes = new ArrayList<>();
        Collections.addAll(nodes, folderB, folderA);

        Collections.sort(nodes, new AlfrescoNodeComparator());

        final List<Node> expected = new ArrayList<>();
        Collections.addAll(expected, folderB, folderA);

        Assertions.assertEquals(expected, nodes);
    }

    @Test
    public void compare_document_and_folder() {
        final Node document = createDocument();
        final Node folder = createFolder();

        final List<Node> nodes = new ArrayList<>();
        Collections.addAll(nodes, document, folder);

        Collections.sort(nodes, new AlfrescoNodeComparator());

        final List<Node> expected = new ArrayList<>();
        Collections.addAll(expected, folder, document);

        Assertions.assertEquals(expected, nodes);
    }

    private Node createDocument() {
        Node node = mock(Node.class);
        given(node.isDocument()).willReturn(true);
        given(node.isFolder()).willReturn(false);
        return node;
    }

    private Node createFolder() {
        Node node = mock(Node.class);
        given(node.isDocument()).willReturn(false);
        given(node.isFolder()).willReturn(true);
        return node;
    }
}