#ifndef AUTOPANEL_H
#define AUTOPANEL_H

#include <QDialog>

namespace Ui {
class AutoPanel;
}

class AutoPanel : public QDialog
{
    Q_OBJECT

public:
    explicit AutoPanel(QWidget *parent = nullptr);
    ~AutoPanel();

private:
    Ui::AutoPanel *ui;
};

#endif // AUTOPANEL_H
