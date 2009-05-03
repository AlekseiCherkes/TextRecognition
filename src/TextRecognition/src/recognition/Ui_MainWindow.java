/********************************************************************************
** Form generated from reading ui file 'MainWindow.jui'
**
** Created: �� 3. ��� 20:57:21 2009
**      by: Qt User Interface Compiler version 4.4.2
**
** WARNING! All changes made in this file will be lost when recompiling ui file!
********************************************************************************/

package recognition;

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

public class Ui_MainWindow
{
    public QAction actionZoom_In;
    public QAction actionZoom_Out;
    public QAction actionExit;
    public QAction actionAbout_Text_Recognition;
    public QAction actionAbout_Qt_Jambi;
    public QAction actionSave;
    public QAction actionClose;
    public QAction actionAbout_Qt;
    public QAction actionOpen_Network;
    public QWidget centralwidget;
    public QMenuBar menubar;
    public QMenu menuHelp;
    public QMenu menuFile;
    public QStatusBar statusbar;
    public QDockWidget treeDock;
    public QWidget treeDockContents;
    public QGridLayout gridLayout;
    public QTreeView dirView;
    public QDockWidget tableDock;
    public QWidget tableDockContents;
    public QGridLayout gridLayout1;
    public QTreeView tableView;
    public QToolBar toolBar_2;

    public Ui_MainWindow() { super(); }

    public void setupUi(QMainWindow MainWindow)
    {
        MainWindow.setObjectName("MainWindow");
        MainWindow.resize(new QSize(714, 614).expandedTo(MainWindow.minimumSizeHint()));
        actionZoom_In = new QAction(MainWindow);
        actionZoom_In.setObjectName("actionZoom_In");
        actionZoom_In.setEnabled(false);
        actionZoom_Out = new QAction(MainWindow);
        actionZoom_Out.setObjectName("actionZoom_Out");
        actionZoom_Out.setEnabled(false);
        actionExit = new QAction(MainWindow);
        actionExit.setObjectName("actionExit");
        actionAbout_Text_Recognition = new QAction(MainWindow);
        actionAbout_Text_Recognition.setObjectName("actionAbout_Text_Recognition");
        actionAbout_Qt_Jambi = new QAction(MainWindow);
        actionAbout_Qt_Jambi.setObjectName("actionAbout_Qt_Jambi");
        actionSave = new QAction(MainWindow);
        actionSave.setObjectName("actionSave");
        actionSave.setEnabled(false);
        actionClose = new QAction(MainWindow);
        actionClose.setObjectName("actionClose");
        actionClose.setEnabled(false);
        actionAbout_Qt = new QAction(MainWindow);
        actionAbout_Qt.setObjectName("actionAbout_Qt");
        actionOpen_Network = new QAction(MainWindow);
        actionOpen_Network.setObjectName("actionOpen_Network");
        centralwidget = new QWidget(MainWindow);
        centralwidget.setObjectName("centralwidget");
        MainWindow.setCentralWidget(centralwidget);
        menubar = new QMenuBar(MainWindow);
        menubar.setObjectName("menubar");
        menubar.setGeometry(new QRect(0, 0, 714, 21));
        menuHelp = new QMenu(menubar);
        menuHelp.setObjectName("menuHelp");
        menuFile = new QMenu(menubar);
        menuFile.setObjectName("menuFile");
        MainWindow.setMenuBar(menubar);
        statusbar = new QStatusBar(MainWindow);
        statusbar.setObjectName("statusbar");
        MainWindow.setStatusBar(statusbar);
        treeDock = new QDockWidget(MainWindow);
        treeDock.setObjectName("treeDock");
        treeDock.setFeatures(com.trolltech.qt.gui.QDockWidget.DockWidgetFeature.createQFlags(com.trolltech.qt.gui.QDockWidget.DockWidgetFeature.DockWidgetFloatable,com.trolltech.qt.gui.QDockWidget.DockWidgetFeature.DockWidgetMovable));
        treeDockContents = new QWidget();
        treeDockContents.setObjectName("treeDockContents");
        gridLayout = new QGridLayout(treeDockContents);
        gridLayout.setSpacing(6);
        gridLayout.setMargin(9);
        gridLayout.setObjectName("gridLayout");
        dirView = new QTreeView(treeDockContents);
        dirView.setObjectName("dirView");

        gridLayout.addWidget(dirView, 0, 0, 1, 1);

        treeDock.setWidget(treeDockContents);
        MainWindow.addDockWidget(com.trolltech.qt.core.Qt.DockWidgetArea.resolve(1), treeDock);
        tableDock = new QDockWidget(MainWindow);
        tableDock.setObjectName("tableDock");
        tableDock.setMinimumSize(new QSize(274, 146));
        tableDock.setLayoutDirection(com.trolltech.qt.core.Qt.LayoutDirection.LeftToRight);
        tableDock.setFloating(false);
        tableDock.setFeatures(com.trolltech.qt.gui.QDockWidget.DockWidgetFeature.createQFlags(com.trolltech.qt.gui.QDockWidget.DockWidgetFeature.DockWidgetFloatable,com.trolltech.qt.gui.QDockWidget.DockWidgetFeature.DockWidgetMovable));
        tableDockContents = new QWidget();
        tableDockContents.setObjectName("tableDockContents");
        gridLayout1 = new QGridLayout(tableDockContents);
        gridLayout1.setSpacing(6);
        gridLayout1.setMargin(9);
        gridLayout1.setObjectName("gridLayout1");
        tableView = new QTreeView(tableDockContents);
        tableView.setObjectName("tableView");
        tableView.setAlternatingRowColors(true);
        tableView.setSelectionMode(com.trolltech.qt.gui.QAbstractItemView.SelectionMode.SingleSelection);
        tableView.setSelectionBehavior(com.trolltech.qt.gui.QAbstractItemView.SelectionBehavior.SelectRows);
        tableView.setRootIsDecorated(false);
        tableView.setUniformRowHeights(true);
        tableView.setItemsExpandable(false);

        gridLayout1.addWidget(tableView, 0, 0, 1, 1);

        tableDock.setWidget(tableDockContents);
        MainWindow.addDockWidget(com.trolltech.qt.core.Qt.DockWidgetArea.resolve(2), tableDock);
        toolBar_2 = new QToolBar(MainWindow);
        toolBar_2.setObjectName("toolBar_2");
        toolBar_2.setOrientation(com.trolltech.qt.core.Qt.Orientation.Horizontal);
        MainWindow.addToolBar(com.trolltech.qt.core.Qt.ToolBarArea.TopToolBarArea, toolBar_2);

        menubar.addAction(menuFile.menuAction());
        menubar.addAction(menuHelp.menuAction());
        menuHelp.addAction(actionAbout_Text_Recognition);
        menuHelp.addSeparator();
        menuHelp.addAction(actionAbout_Qt_Jambi);
        menuHelp.addAction(actionAbout_Qt);
        menuFile.addAction(actionOpen_Network);
        menuFile.addSeparator();
        menuFile.addAction(actionExit);
        toolBar_2.addAction(actionSave);
        toolBar_2.addAction(actionClose);
        retranslateUi(MainWindow);

        MainWindow.connectSlotsByName();
    } // setupUi

    void retranslateUi(QMainWindow MainWindow)
    {
        MainWindow.setWindowTitle(com.trolltech.qt.core.QCoreApplication.translate("MainWindow", "Qt Jambi Text Recognition"));
        actionZoom_In.setText(com.trolltech.qt.core.QCoreApplication.translate("MainWindow", "Zoom In"));
        actionZoom_Out.setText(com.trolltech.qt.core.QCoreApplication.translate("MainWindow", "Zoom Out"));
        actionExit.setText(com.trolltech.qt.core.QCoreApplication.translate("MainWindow", "E&xit"));
        actionAbout_Text_Recognition.setText(com.trolltech.qt.core.QCoreApplication.translate("MainWindow", "About &Text Recognition"));
        actionAbout_Qt_Jambi.setText(com.trolltech.qt.core.QCoreApplication.translate("MainWindow", "About Qt &Jambi"));
        actionSave.setText(com.trolltech.qt.core.QCoreApplication.translate("MainWindow", "&Save"));
        actionSave.setShortcut(com.trolltech.qt.core.QCoreApplication.translate("MainWindow", "Ctrl+S"));
        actionClose.setText(com.trolltech.qt.core.QCoreApplication.translate("MainWindow", "Cl&ose"));
        actionAbout_Qt.setText(com.trolltech.qt.core.QCoreApplication.translate("MainWindow", "About &Qt"));
        actionOpen_Network.setText(com.trolltech.qt.core.QCoreApplication.translate("MainWindow", "Open Network"));
        menuHelp.setTitle(com.trolltech.qt.core.QCoreApplication.translate("MainWindow", "&Help"));
        menuFile.setTitle(com.trolltech.qt.core.QCoreApplication.translate("MainWindow", "&File"));
        treeDock.setWindowTitle(com.trolltech.qt.core.QCoreApplication.translate("MainWindow", "Directories"));
        tableDock.setWindowTitle(com.trolltech.qt.core.QCoreApplication.translate("MainWindow", "Images "));
        toolBar_2.setWindowTitle(com.trolltech.qt.core.QCoreApplication.translate("MainWindow", "File toolbar"));
    } // retranslateUi

}

