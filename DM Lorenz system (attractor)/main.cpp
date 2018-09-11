#include "input.h"
#include <QApplication>

#include "systemsolver.h"

#include <QtDataVisualization/q3dscatter.h>
#include <QtDataVisualization/qabstract3dseries.h>

#include <QtWidgets/QWidget>

#include <QRandomGenerator>

using namespace QtDataVisualization;

QVector3D randVector();

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

    //graph->activeTheme()->setType(QtDataVisualization::Q3DTheme::ThemeEbony);
    graph->scene()->activeCamera()->setCameraPreset(Q3DCamera::CameraPresetFront);
    graph->setShadowQuality(QAbstract3DGraph::ShadowQualitySoftLow);

    QScatter3DSeries *series = new QScatter3DSeries();
    series->setItemLabelFormat(QStringLiteral("@xTitle: @xLabel @yTitle: @yLabel @zTitle: @zLabel"));
    graph->addSeries(series);

    graph->axisX()->setTitle("X");
    graph->axisY()->setTitle("Y");
    graph->axisZ()->setTitle("Z");

    int m_itemCount = 10;
    QScatterDataArray *dataArray = new QScatterDataArray;
    dataArray->resize(m_itemCount);
    QScatterDataItem *ptrToDataArray = &dataArray->first();

    for (int i = 0; i < m_itemCount; i++) {
        ptrToDataArray->setPosition(randVector());
        ptrToDataArray++;
    }

    graph->seriesList().at(0)->dataProxy()->resetArray(dataArray);

    return a.exec();
}

QVector3D randVector()
{
    return QVector3D(
                (float)(QRandomGenerator::global()->bounded(100)) / 2.0f -
                (float)(QRandomGenerator::global()->bounded(100)) / 2.0f,
                (float)(QRandomGenerator::global()->bounded(100)) / 100.0f -
                (float)(QRandomGenerator::global()->bounded(100)) / 100.0f,
                (float)(QRandomGenerator::global()->bounded(100)) / 2.0f -
                (float)(QRandomGenerator::global()->bounded(100)) / 2.0f);
}
