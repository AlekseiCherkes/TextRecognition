/********************************************************************************
** Form generated from reading ui file 'AboutTextRecognition.jui'
**
** Created: �� 31. ��� 16:26:15 2009
**      by: Qt User Interface Compiler version 4.4.2
**
** WARNING! All changes made in this file will be lost when recompiling ui file!
********************************************************************************/

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

public class Ui_AboutTextRecognition
{
    public QGridLayout gridLayout;
    public QGridLayout gridLayout1;
    public QLabel label;
    public QTextEdit textEdit;
    public QSpacerItem spacerItem;
    public QSpacerItem spacerItem1;
    public QPushButton okButton;
    public QSpacerItem spacerItem2;

    public Ui_AboutTextRecognition() { super(); }

    public void setupUi(QDialog AboutTextRecognition)
    {
        AboutTextRecognition.setObjectName("AboutTextRecognition");
        AboutTextRecognition.resize(new QSize(625, 407).expandedTo(AboutTextRecognition.minimumSizeHint()));
        AboutTextRecognition.setAutoFillBackground(true);
        gridLayout = new QGridLayout(AboutTextRecognition);
        gridLayout.setObjectName("gridLayout");
        gridLayout1 = new QGridLayout();
        gridLayout1.setObjectName("gridLayout1");
        label = new QLabel(AboutTextRecognition);
        label.setObjectName("label");
        label.setPixmap(new QPixmap());
//        label.setAlignment(com.trolltech.qt.core.Qt.AlignmentFlag.createQFlags(com.trolltech.qt.core.Qt.AlignmentFlag.AlignAbsolute,com.trolltech.qt.core.Qt.AlignmentFlag.AlignBottom,com.trolltech.qt.core.Qt.AlignmentFlag.AlignCenter,com.trolltech.qt.core.Qt.AlignmentFlag.AlignHCenter,com.trolltech.qt.core.Qt.AlignmentFlag.AlignHorizontal_Mask,com.trolltech.qt.core.Qt.AlignmentFlag.AlignJustify,com.trolltech.qt.core.Qt.AlignmentFlag.AlignLeading,com.trolltech.qt.core.Qt.AlignmentFlag.AlignLeft,com.trolltech.qt.core.Qt.AlignmentFlag.AlignRight,com.trolltech.qt.core.Qt.AlignmentFlag.AlignTop,com.trolltech.qt.core.Qt.AlignmentFlag.AlignTrailing,com.trolltech.qt.core.Qt.AlignmentFlag.AlignVCenter,com.trolltech.qt.core.Qt.AlignmentFlag.AlignVertical_Mask));

        gridLayout1.addWidget(label, 0, 0, 1, 1);

        textEdit = new QTextEdit(AboutTextRecognition);
        textEdit.setObjectName("textEdit");
        QPalette palette= new QPalette();
        palette.setColor(QPalette.ColorGroup.Active, QPalette.ColorRole.Base, new QColor(224, 223, 227));
        palette.setColor(QPalette.ColorGroup.Inactive, QPalette.ColorRole.Base, new QColor(224, 223, 227));
        palette.setColor(QPalette.ColorGroup.Disabled, QPalette.ColorRole.Base, new QColor(224, 223, 227));
        textEdit.setPalette(palette);
        textEdit.setAcceptDrops(true);
        textEdit.setFrameShape(com.trolltech.qt.gui.QFrame.Shape.Box);
        textEdit.setUndoRedoEnabled(false);
        textEdit.setReadOnly(true);

        gridLayout1.addWidget(textEdit, 0, 1, 2, 1);

        spacerItem = new QSpacerItem(20, 221, com.trolltech.qt.gui.QSizePolicy.Policy.Minimum, com.trolltech.qt.gui.QSizePolicy.Policy.Expanding);

        gridLayout1.addItem(spacerItem, 1, 0, 1, 1);


        gridLayout.addLayout(gridLayout1, 0, 0, 1, 3);

        spacerItem1 = new QSpacerItem(40, 20, com.trolltech.qt.gui.QSizePolicy.Policy.Expanding, com.trolltech.qt.gui.QSizePolicy.Policy.Minimum);

        gridLayout.addItem(spacerItem1, 1, 0, 1, 1);

        okButton = new QPushButton(AboutTextRecognition);
        okButton.setObjectName("okButton");

        gridLayout.addWidget(okButton, 1, 1, 1, 1);

        spacerItem2 = new QSpacerItem(40, 20, com.trolltech.qt.gui.QSizePolicy.Policy.Expanding, com.trolltech.qt.gui.QSizePolicy.Policy.Minimum);

        gridLayout.addItem(spacerItem2, 1, 2, 1, 1);

        retranslateUi(AboutTextRecognition);
        okButton.clicked.connect(AboutTextRecognition, "accept()");

        AboutTextRecognition.connectSlotsByName();
    } // setupUi

    void retranslateUi(QDialog AboutTextRecognition)
    {
        label.setText(com.trolltech.qt.core.QCoreApplication.translate("AboutTextRecognition", ""));
        textEdit.setHtml(com.trolltech.qt.core.QCoreApplication.translate("AboutTextRecognition", "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0//EN\" \"http://www.w3.org/TR/REC-html40/strict.dtd\">\n"+
"<html><head><meta name=\"qrichtext\" content=\"1\" /><title>Image Viewer</title><style type=\"text/css\">\n"+
"p, li { white-space: pre-wrap; }\n"+
"</style></head><body style=\" font-family:'MS Shell Dlg 2'; font-size:8.25pt; font-weight:400; font-style:normal;\">\n"+
"<p align=\"center\" style=\" margin-top:16px; margin-bottom:12px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px; font-size:10pt; font-weight:600;\"><span style=\" font-size:x-large;\">TextRecognition</span></p>\n"+
"<p style=\" margin-top:12px; margin-bottom:12px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px; font-size:10pt;\">This application was written to show the various aspects of TextRecognition library.</p></body></html>"));
        okButton.setText(com.trolltech.qt.core.QCoreApplication.translate("AboutTextRecognition", "OK"));
    } // retranslateUi

}

