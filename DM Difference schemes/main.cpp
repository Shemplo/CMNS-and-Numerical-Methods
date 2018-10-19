#include "input.h"
#include <QApplication>

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    Input w;
    w.show();

    return a.exec();
}
