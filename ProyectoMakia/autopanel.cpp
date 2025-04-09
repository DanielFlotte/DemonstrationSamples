#include "autopanel.h"
#include "ui_autopanel.h"

AutoPanel::AutoPanel(QWidget *parent)
    : QDialog(parent)
    , ui(new Ui::AutoPanel)
{
    ui->setupUi(this);
}

AutoPanel::~AutoPanel()
{
    delete ui;
}
