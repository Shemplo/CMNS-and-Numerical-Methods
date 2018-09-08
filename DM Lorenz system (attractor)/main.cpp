#include "input.h"
#include <QApplication>

#include "systemsolver.h"

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    Input w;

    w.setWindowFlags(Qt::MSWindowsFixedSizeDialogHint);
    w.setWindowTitle("Lorenz system solver");
    w.show();

    return a.exec();
}
