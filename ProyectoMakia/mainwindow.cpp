#include "mainwindow.h"
#include "./ui_mainwindow.h"
#include <QLayout>
#include <QPushButton>
#include <QDragEnterEvent>
#include <QDropEvent>
#include <QMimeData>
#include <QFile>
#include <QFileInfo>
#include <QMessageBox>
#include <QFontDatabase>
#include <QUiLoader>
#include <QFileDialog>
#include <QLineEdit>
#include <QDir>
#include <QUrl>
#include <unordered_set>
#include <algorithm>

#include "7zManager/7zManager.hpp"
#include "7zManager/FormatData.hpp"

using namespace m7z;

bool fileIsCompressed(const QString&);

MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    this->other_inits();
}


void MainWindow::other_inits() {

    set_7z_binary();

    this->setAcceptDrops(true);

    int font_id = QFontDatabase::addApplicationFont(":/fonts/fonts/Lato-Regular.ttf");
    if (font_id == -1) {
        QMessageBox::warning(nullptr, "Fatal Error", "Error reading the font!");
        exit(-1);
    }
    QString font_family = QFontDatabase::applicationFontFamilies(font_id).at(0);
    QApplication::setFont(QFont(font_family));
    this->setFont(QFont(font_family));

    QFile styles_file(":/style/style.qss");
    if (styles_file.open(QFile::ReadOnly)) {
        QTextStream stream(&styles_file);
        QString style = stream.readAll();
        qApp->setStyleSheet(style);
        styles_file.close();
    } else {
        QMessageBox::warning(nullptr, "Fatal Error", "Error reading the style");
        exit(-1);
    }

    this->setFixedSize(this->size());

    ui->top_btns_layout->setSpacing(0);
    this->tabs_buttongroup.setExclusive(true);

    QFont font_btn = this->font();
    font_btn.setPointSizeF(10);

    this->btn_auto = new QPushButton("Auto");
    btn_auto->setCheckable(true);
    btn_auto->setFixedWidth(35);
    btn_auto->setSizePolicy(QSizePolicy::Fixed, QSizePolicy::Expanding);
    QObject::connect(btn_auto, &QPushButton::clicked, [&](){
        this->setAutoMode();
    });
    btn_auto->setFont(font_btn);
    ui->top_btns_layout->addWidget(btn_auto);
    this->tabs_buttongroup.addButton(btn_auto, 1);

    this->btn_compress = new QPushButton("Compress");
    btn_compress->setCheckable(true);
    btn_compress->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Expanding);
    QObject::connect(btn_compress, &QPushButton::clicked, [&](){
        this->setCompressMode();
    });
    btn_compress->setFont(font_btn);
    ui->top_btns_layout->addWidget(btn_compress);
    this->tabs_buttongroup.addButton(btn_compress, 2);

    this->btn_decompress = new QPushButton("Decompress");
    btn_decompress->setCheckable(true);
    btn_decompress->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Expanding);
    QObject::connect(btn_decompress, &QPushButton::clicked, [&](){
        this->setDecompressMode();
    });
    btn_decompress->setFont(font_btn);
    ui->top_btns_layout->addWidget(btn_decompress);
    this->tabs_buttongroup.addButton(btn_decompress, 3);

    btn_auto->click();

    //Superior panel

    ui->drag_and_drop_label->setAlignment(Qt::AlignCenter);
    ui->or_label->setAlignment(Qt::AlignCenter);

    // Inferior panel
    QFont font_edits = this->font();
    font_edits.setPointSizeF(10);

    ui->password_lineedit->setEchoMode(QLineEdit::Password);
    ui->repeat_lineedit->setEchoMode(QLineEdit::Password);
    ui->password_lineedit->setFont(font_edits);
    ui->repeat_lineedit->setFont(font_edits);

    ui->cyper_pass_checkbox->setStyleSheet("QCheckBox::indicator { width: 19px; height: 19px; }");

    QVector<QString> archive_types = {"7z", "zip", "gz", "xz", "tar", "lzma"}; //7z always the first one
    for (auto& i : archive_types)
        ui->archive_type_combobox->addItem(i);

    ui->compression_level_slider->setMinimum(1);
    ui->compression_level_slider->setMaximum(9);
    ui->compression_level_slider->setTickPosition(QSlider::TicksBelow);
    ui->compression_level_slider->setTickInterval(1);
    connect(ui->compression_level_slider, &QSlider::valueChanged, this, &MainWindow::onSliderModifiedAction);
    ui->compression_level_slider->setValue(5);
}

void MainWindow::set_7z_binary() {
    std::string next_path; //path_7z not necessarily empty
    const std::array<QString, 4> options = {"./7zz", "./7z", "/bin/7zz", "/bin/7z"};
    for (auto it = options.begin(); it != options.end(); ++it) {
        if (QFile::exists(*it)) {
            next_path = it->toStdString();
            break;
        }
    }
    if (next_path.empty())
        next_path = options.back().toStdString();

    path_7z = next_path;
}

void MainWindow::dragEnterEvent(QDragEnterEvent* event) {
    if (!event->mimeData()->hasUrls()) {
        event->ignore();
        return;
    }

    event->acceptProposedAction();
    ui->drag_and_drop_label->setText(">>" + ui->drag_and_drop_label->text() + "<<");
    ui->drag_and_drop_label->setObjectName("drag_and_drop_label_dragging");
    ui->drag_and_drop_label->setStyleSheet(ui->drag_and_drop_label->styleSheet());
    qDebug() << "Dragging";
}

void MainWindow::dragLeaveEvent(QDragLeaveEvent*) {
    this->endDropEvent();
}

void MainWindow::dropEvent(QDropEvent* event) {
    this->endDropEvent();

    const QMimeData *mime_data = event->mimeData();

    if (!mime_data->hasUrls())
        return;

    QVector<QString> urls;
    for (auto& i : mime_data->urls())
        urls.emplace_back(i.toLocalFile());

    this->sendPathsToProperAction(urls);
}

void MainWindow::endDropEvent() {
    auto txt = ui->drag_and_drop_label->text().toStdString();
    ui->drag_and_drop_label->setText(QString::fromStdString(std::string(txt.begin() + 2, txt.end() - 2)));
    ui->drag_and_drop_label->setObjectName("drag_and_drop_label");
    ui->drag_and_drop_label->setStyleSheet(ui->drag_and_drop_label->styleSheet());

    qDebug() << "Drag ended";
}

void MainWindow::setAutoMode() {
    setAllOnDataPanel(false);
}

void MainWindow::setCompressMode() {
    setAllOnDataPanel(true);
}

void MainWindow::setDecompressMode() {
    setAllOnDataPanel(false);
    ui->password_label->setVisible(true);
    ui->password_lineedit->setVisible(true);

    ui->password_lineedit->setText("");
    ui->repeat_lineedit->setText("");
}

void MainWindow::setAllOnDataPanel(bool val) {
    ui->archive_type_combobox->setVisible(val);
    ui->archive_type_label->setVisible(val);

    ui->password_label->setVisible(val);
    ui->password_lineedit->setVisible(val);
    ui->repeat_label->setVisible(val);
    ui->repeat_lineedit->setVisible(val);

    ui->cyper_pass_checkbox->setVisible(val);

    ui->compression_level_label->setVisible(val);
    ui->compression_level_slider->setVisible(val);
}

void MainWindow::sendPathsToProperAction(const QVector<QString>& urls) {
    if (auto mode_setted = tabs_buttongroup.checkedButton(); mode_setted == btn_auto) {
        qDebug() << "Auto";
        doDefaultAction(urls);
    } else if (mode_setted == btn_compress) {
        qDebug() << "Comp";
        doCompressAction(urls);
    } else {
        qDebug() << "Decomp";
        doDecompressAction(urls);
    }
}

void MainWindow::doDefaultAction(const QVector<QString>& paths) {
    for (auto& i : paths)
        qDebug() << fileIsCompressed(i);
    qDebug() << "";

    bool all_compress = std::all_of(paths.begin(), paths.end(), [](const QString& p){return fileIsCompressed(p);});
    if (all_compress)
        doDecompressAction(paths);
    else
        doCompressAction(paths);
}

void MainWindow::doCompressAction(const QVector<QString>& paths) {
    qDebug() << "E";
    std::vector<std::string> paths_std;
    for (auto& i : paths)
        paths_std.push_back(i.toStdString());

    std::string compress_type = ui->archive_type_combobox->currentText().toStdString();
    qDebug() << "compress type: " << compress_type;
    if (!ui->password_lineedit->text().isEmpty() && (ui->password_lineedit->text() != ui->repeat_lineedit->text())) {
        QMessageBox::warning(this, "Warning", "Password confirmation is incorrect");
        qDebug() << "Error!";
        return;
    }
    std::string password = ui->password_lineedit->text().toStdString();
    bool cyper_names = ui->cyper_pass_checkbox->isChecked();
    uint compress_level = ui->compression_level_slider->value();

    CompressParameters params = {compress_type, password, cyper_names, compress_level};
    m7z::compress_files(paths_std, params);
}

void MainWindow::doDecompressAction(const QVector<QString>& paths) {
    DecompressParameters params = {ui->password_lineedit->text().toStdString()};

    for (auto& path : paths)
        m7z::decompress_file(path.toStdString(), params);
}

void MainWindow::onSliderModifiedAction(int val) {
    qDebug() << "Action";
    const static QString compress_level_label_OR_text = ui->compression_level_label->text();
    QString next_text = compress_level_label_OR_text + " " + QString::number(val);
    if (val == 5)
        next_text += " (default)";
    ui->compression_level_label->setText(next_text);
}

void MainWindow::on_browse_files_btn_clicked()
{
    QString archives = "All (*.*);;Compressed (*.7z *.zip *.xz *.rar *.gz *.tar *.xar *.lzma)";
    QVector<QString> files = QFileDialog::getOpenFileNames(this, "Select file(s)", "", archives);
    sendPathsToProperAction(files);
}

void MainWindow::on_repeat_lineedit_textChanged(const QString& text)
{
    auto refreshStyle = [&]() {
        ui->repeat_lineedit->style()->unpolish(ui->repeat_lineedit);
        ui->repeat_lineedit->style()->polish(ui->repeat_lineedit);
        ui->repeat_lineedit->update();
    };

    if (ui->password_lineedit->text() != text) {
        ui->repeat_lineedit->setObjectName("repeat_lineedit_whenwrong");
        refreshStyle();
    } else {
        ui->repeat_lineedit->setObjectName("repeat_lineedit");
        refreshStyle();
    }
}

void MainWindow::on_archive_type_combobox_currentIndexChanged(int index)
{
    //Set for x type
    std::string extension_wanted = ui->archive_type_combobox->currentText().toStdString();

    auto& format_data = getDataFromFormat(extension_wanted);

    ui->cyper_pass_checkbox->setEnabled(format_data.allows_cyper_names);

    ui->password_lineedit->setEnabled(format_data.allows_pass);
    ui->repeat_lineedit->setEnabled(format_data.allows_pass);
}

MainWindow::~MainWindow()
{
    delete ui;
}

//Non-class functions
using namespace std;

bool fileIsCompressed(const QString& path) {
    const static unordered_set<QString> compressed_exts = {"zip", "tar", "xz", "xar", "lzma", "7z", "rar", "gzip", "gz"};

    QFileInfo file_info(path);
    qDebug() << file_info.suffix();
    return compressed_exts.find(file_info.suffix()) != compressed_exts.end();
}







