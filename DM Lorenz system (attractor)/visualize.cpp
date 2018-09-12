#include "visualize.h"

void visualize(std::vector<double> *answer) {

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

    size_t m_itemCount = answer->size();
    QScatterDataArray *dataArray = new QScatterDataArray;
    dataArray->resize(static_cast<int>(m_itemCount));
    QScatterDataItem *ptrToDataArray = &dataArray->first();


    for (size_t i = 0; i < m_itemCount; i++) {
        ptrToDataArray->setPosition(QVector3D(static_cast<float>(answer[0][i]), static_cast<float>(answer[1][i]), static_cast<float>(answer[2][i])));
        ptrToDataArray++;
    }

    graph->seriesList().at(0)->dataProxy()->resetArray(dataArray);
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
