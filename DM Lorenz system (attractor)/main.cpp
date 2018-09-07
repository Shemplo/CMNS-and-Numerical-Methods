#include "input.h"
#include <QApplication>

#include "systemsolver.h"
#include "expliciteulersolver.h"

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    Input w;

    w.setWindowFlags(Qt::MSWindowsFixedSizeDialogHint);
    w.setWindowTitle("Lorenz system solver");
    w.show();

    SystemSolver *solver = new ExplicitEulerSolver ();
    solver->solve();

    return a.exec();
}
