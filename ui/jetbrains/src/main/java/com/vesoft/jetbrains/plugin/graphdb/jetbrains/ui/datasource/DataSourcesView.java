package com.vesoft.jetbrains.plugin.graphdb.jetbrains.ui.datasource;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionToolbarPosition;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.treeStructure.PatchedDefaultMutableTreeNode;
import com.intellij.ui.treeStructure.Tree;
import com.vesoft.jetbrains.plugin.graphdb.jetbrains.component.analytics.Analytics;
import com.vesoft.jetbrains.plugin.graphdb.jetbrains.component.datasource.DataSourcesComponent;
import com.vesoft.jetbrains.plugin.graphdb.jetbrains.component.datasource.metadata.DataSourcesComponentMetadata;
import com.vesoft.jetbrains.plugin.graphdb.jetbrains.component.datasource.state.DataSourceApi;
import com.vesoft.jetbrains.plugin.graphdb.jetbrains.database.DatabaseManagerService;
import com.vesoft.jetbrains.plugin.graphdb.jetbrains.database.DatabaseManagerServiceImpl;
import com.vesoft.jetbrains.plugin.graphdb.jetbrains.ui.datasource.actions.RefreshDataSourcesAction;
import com.vesoft.jetbrains.plugin.graphdb.jetbrains.ui.datasource.interactions.DataSourceInteractions;
import com.vesoft.jetbrains.plugin.graphdb.jetbrains.ui.datasource.metadata.DataSourceMetadataUi;
import com.vesoft.jetbrains.plugin.graphdb.jetbrains.ui.datasource.tree.*;
import com.vesoft.jetbrains.plugin.graphdb.jetbrains.ui.datasource.tree.model.LoadingModel;
import com.vesoft.jetbrains.plugin.graphdb.jetbrains.util.FileUtil;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DataSourcesView implements Disposable {

    private boolean initialized;

    private DataSourcesComponent component;
    private DataSourcesComponentMetadata componentMetadata;
    private DataSourceInteractions interactions;
    private PatchedDefaultMutableTreeNode treeRoot;
    private DefaultTreeModel treeModel;

    private JPanel toolWindowContent;
    private JPanel treePanel;
    private Tree dataSourceTree;
    private ToolbarDecorator decorator;
    private DataSourceMetadataUi dataSourceMetadataUi;
    private Project project;

    public DataSourcesView() {
        initialized = false;
    }

    public void initToolWindow(Project project, ToolWindow toolWindow) {
        if (!initialized) {
            this.project = project;
            ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
            Content content = contentFactory.createContent(toolWindowContent, "", false);
            toolWindow.getContentManager().addContent(content);

            component = project.getComponent(DataSourcesComponent.class);
            componentMetadata = project.getComponent(DataSourcesComponentMetadata.class);
            dataSourceMetadataUi = new DataSourceMetadataUi(componentMetadata);
            treeRoot = new PatchedDefaultMutableTreeNode(new RootTreeNodeModel());
            treeModel = new DefaultTreeModel(treeRoot, false);
            decorator = ToolbarDecorator.createDecorator(dataSourceTree);
            decorator.addExtraAction(new RefreshDataSourcesAction(this));

            configureDataSourceTree();
            decorateDataSourceTree();

            interactions = new DataSourceInteractions(project, this);

            replaceTreeWithDecorated();
            showDataSources();
            refreshDataSourcesMetadata();

            initialized = true;
        }
    }

    public DataSourcesComponent getComponent() {
        return component;
    }

    public Tree getDataSourceTree() {
        return dataSourceTree;
    }

    public PatchedDefaultMutableTreeNode getTreeRoot() {
        return treeRoot;
    }

    public DefaultTreeModel getTreeModel() {
        return treeModel;
    }

    public ToolbarDecorator getDecorator() {
        return decorator;
    }

    private void createUIComponents() {
        treePanel = new JPanel(new GridLayout(0, 1));
    }

    private void configureDataSourceTree() {
        dataSourceTree.getEmptyText().setText("Create a data source");
        dataSourceTree.setCellRenderer(new GraphColoredTreeCellRenderer(component));
        dataSourceTree.setModel(treeModel);
        dataSourceTree.setRootVisible(false);
        dataSourceTree.setToggleClickCount(0);
        dataSourceTree.addMouseListener(new TreeMouseAdapter(project));
    }

    private void decorateDataSourceTree() {
        decorator.setPanelBorder(BorderFactory.createEmptyBorder());
        decorator.setToolbarPosition(ActionToolbarPosition.TOP);
    }

    private void replaceTreeWithDecorated() {
        JPanel panel = decorator.createPanel();
        treePanel.add(panel);
    }

    private void showDataSources() {
        component.getDataSourceContainer().getDataSources()
                .forEach((dataSource) -> treeRoot.add(new PatchedDefaultMutableTreeNode(new DataSourceTreeNodeModel(dataSource))));
        treeModel.reload();
    }

    public void refreshDataSourcesMetadata() {
        Enumeration children = treeRoot.children();
        while (children.hasMoreElements()) {
            PatchedDefaultMutableTreeNode treeNode = (PatchedDefaultMutableTreeNode) children.nextElement();
            refreshDataSourceMetadata(treeNode);
            reloadTreeNode(treeNode);
        }
    }

    public void refreshDataSourceMetadata(PatchedDefaultMutableTreeNode treeNode) {
        TreeNodeModelApi userObject = (TreeNodeModelApi) treeNode.getUserObject();
        DataSourceApi nodeDataSource = userObject.getDataSourceApi();
        Analytics.event(nodeDataSource, "refreshMetadata");
        treeNode.removeAllChildren();
        treeNode.add(new PatchedDefaultMutableTreeNode(new LoadingModel("Loading...", nodeDataSource)));
        dataSourceMetadataUi.updateDataSourceMetadataUi(treeNode, nodeDataSource, project).thenAccept((isRefreshed) -> {
            if (isRefreshed) {
                reloadTreeNode(treeNode);
            }
        });
    }

    public void createDataSource(DataSourceApi dataSource) {
        Analytics.event(dataSource, "create");
        component.getDataSourceContainer().addDataSource(dataSource);
        TreeNodeModelApi model = new DataSourceTreeNodeModel(dataSource);
        PatchedDefaultMutableTreeNode treeNode = new PatchedDefaultMutableTreeNode(model);
        treeRoot.add(treeNode);
        refreshDataSourceMetadata(treeNode);
        reloadTreeNode(treeNode);
    }

    public void updateDataSource(PatchedDefaultMutableTreeNode treeNode, DataSourceApi oldDataSource, DataSourceApi newDataSource) {
        Analytics.event(newDataSource, "update");
        component.getDataSourceContainer().updateDataSource(oldDataSource, newDataSource);
        project.getComponent(DatabaseManagerService.class).updateDatabase(newDataSource);
        treeNode.setUserObject(new DataSourceTreeNodeModel(newDataSource));
        refreshDataSourceMetadata(treeNode);
        reloadTreeNode(treeNode);
    }

    private void reloadTreeNode(PatchedDefaultMutableTreeNode treeNode) {
        TreePath[] expandedNodes = getExpandedNodes(dataSourceTree);
        treeModel.reload();
        Optional.ofNullable(expandedNodes).ifPresent(treePaths -> Arrays.stream(treePaths).forEach(treePath -> dataSourceTree.expandPath(treePath)));
        if (treeNode != null) {
            dataSourceTree.expandPath(new TreePath(treeNode.getPath()));
        }
    }

    public void removeDataSources(Project project, List<DataSourceApi> dataSourcesForRemoval) {
        component.getDataSourceContainer().removeDataSources(dataSourcesForRemoval);

        dataSourcesForRemoval.stream()
                .peek((dataSourceApi -> {
                    Analytics.event(dataSourceApi, "remove");
                    ApplicationManager.getApplication().runWriteAction(() -> {
                        try {
                            FileUtil.getDataSourceFile(project, dataSourceApi).delete(project);
                        } catch (IOException e) {
                            // do nothing
                        }
                    });
                }))
                .map(DataSourceApi::getName)
                .map(name -> {
                    Enumeration enumeration = treeRoot.children();
                    while (enumeration.hasMoreElements()) {
                        DefaultMutableTreeNode element = (DefaultMutableTreeNode) enumeration.nextElement();
                        TreeNodeModelApi userObject = (TreeNodeModelApi) element.getUserObject();
                        DataSourceApi dataSource = userObject.getDataSourceApi();
                        if (dataSource.getName().equals(name)) {
                            return element;
                        }
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .forEach(treeRoot::remove);

        reloadTreeNode(null);
    }

    public TreePath[] getExpandedNodes(JTree tree) {
        Enumeration<TreePath> paths = tree.getExpandedDescendants(new TreePath(tree.getModel().getRoot()));
        if (paths == null) {
            return null;
        }
        List<TreePath> expandedPaths = new ArrayList<>();
        while (paths.hasMoreElements()) {
            TreePath path = paths.nextElement();
            expandedPaths.add(path);
        }
        return expandedPaths.toArray(new TreePath[0]);
    }

    @Override
    public void dispose() {
    }
}
