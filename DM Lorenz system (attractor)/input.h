#ifndef INPUT_H
#define INPUT_H

#include <QMainWindow>

#include "expliciteulersolver.h"
#include "impliciteulersolver.h"
#include "rungekuttasolver.h"
#include "adamssolver.h"

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
        void resetDefault();
        void loadPreset();

    private:
        Ui::Run *ui;
        int preset;

};

#endif // INPUT_H
