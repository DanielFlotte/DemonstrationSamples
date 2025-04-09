#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QButtonGroup>
#include <QPushButton>

QT_BEGIN_NAMESPACE
namespace Ui {
class MainWindow;
}
QT_END_NAMESPACE

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    MainWindow(QWidget *parent = nullptr);
    ~MainWindow();

private slots:
    void on_browse_files_btn_clicked();


    void on_repeat_lineedit_textChanged(const QString &arg1);

    void on_archive_type_combobox_currentIndexChanged(int index);

private:
    Ui::MainWindow *ui;

    void other_inits();
    void set_7z_binary();

    void dragEnterEvent(QDragEnterEvent*) override;
    void dragLeaveEvent(QDragLeaveEvent*) override;
    void dropEvent(QDropEvent*) override;
    void endDropEvent();

    void setAutoMode();
    void setCompressMode();
    void setDecompressMode();
    void setAllOnDataPanel(bool);
    void sendPathsToProperAction(const QVector<QString>&);
    void doDefaultAction(const QVector<QString>&);
    void doCompressAction(const QVector<QString>&);
    void doDecompressAction(const QVector<QString>&);
    void onSliderModifiedAction(int);

    QButtonGroup tabs_buttongroup;
    QPushButton* btn_auto = nullptr;
    QPushButton* btn_compress = nullptr;
    QPushButton* btn_decompress = nullptr;
};
#endif // MAINWINDOW_H
