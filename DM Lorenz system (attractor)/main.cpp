#include "input.h"
#include <QApplication>

#include "systemsolver.h"

#include <QtDataVisualization/q3dscatter.h>
#include <QtDataVisualization/qabstract3dseries.h>

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    Input w;

    w.setWindowFlags(Qt::MSWindowsFixedSizeDialogHint);
    w.setWindowTitle("Lorenz system solver");
    w.show();

    QtDataVisualization::Q3DScatter *graph = new QtDataVisualization::Q3DScatter();
    QWidget *container = QWidget::createWindowContainer(graph);
    container->show();

    return a.exec();
}
