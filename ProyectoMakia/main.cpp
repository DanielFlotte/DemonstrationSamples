#include "mainwindow.h"

#include <QApplication>

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    a.setWindowIcon(QIcon(":/images/Icon.png"));
    MainWindow w;
    w.setWindowTitle("Makia");
    w.show();
    return a.exec();
}
