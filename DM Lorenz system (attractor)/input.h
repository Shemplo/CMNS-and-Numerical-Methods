#ifndef INPUT_H
#define INPUT_H

#include <QMainWindow>

namespace Ui {
    class Run;
}

class Input : public QMainWindow
{

    Q_OBJECT

    public:
        explicit Input(QWidget *parent = nullptr);
        ~Input();

    private slots:
        void slotButtonSolve ();

    private:
        Ui::Run *ui;

};

#endif // INPUT_H
