#include "systemsolver.h"

SystemSolver::SystemSolver(double x0, double y0, double z0, double sigma, double r, double b, double dt, std::string name)
{
    this->sigma = sigma; this->r = r; this->b = b;
    this->x0 = x0; this->y0 = y0; this->z0 = z0;
    this->dt = dt;
    this->name = name;
}

std::vector<double> *SystemSolver::solve (double time) {
    std::cout << "Error: called stub of function \"solve\"" << std::endl;
    std::cout << "Request time: " << time << std::endl;
    return nullptr;
}

double SystemSolver::x(double xk, double yk, double zk) {
    return sigma * (yk - xk) + zk * 0;
}

double SystemSolver::y(double xk, double yk, double zk) {
    return (xk * (r - zk) - yk);
}

double SystemSolver::z(double xk, double yk, double zk) {
    return (xk * yk - b * zk);
}

void SystemSolver::visualize(std::vector<double> *answer) {
    QWidget *window = new QWidget;
    window->setWindowTitle(QString::fromStdString("Lorenz system - " + name));
    window->setWindowFlag(Qt::MSWindowsFixedSizeDialogHint);

    QVBoxLayout *vertical = new QVBoxLayout (window);
    QHBoxLayout *horizontal = new QHBoxLayout ();
    vertical->addLayout(horizontal);

    const int AXIS_NUMBER = 3;
    QtCharts::QChartView **axisChartViews = new QtCharts::QChartView * [AXIS_NUMBER];
    for (int i = 0; i < AXIS_NUMBER; i++) {
        axisChartViews [i] = new QtCharts::QChartView (new QtCharts::QChart);
        // here put common styles
        horizontal->addWidget(axisChartViews [i]);
    }

    QtDataVisualization::Q3DScatter *graph = new QtDataVisualization::Q3DScatter();
    QWidget *container = QWidget::createWindowContainer(graph);

    QSize screenSize = graph->screen()->size();
    int width = screenSize.width() / 2,
        height = static_cast<int>(screenSize.height() / 1.5);
    container->setMinimumSize(QSize(width, height));
    vertical->addWidget(container, 1);

    QString paramStringValue = QString::asprintf(
        "Parameters: [x0 = %.4f, y0 = %.4f, z0 = %.4f, σ = %.4f, b = %.4f, r = %.4f, Δt = %0.4f]",
        x0, y0, z0, sigma, b, r, dt);
    QLabel *paramsString = new QLabel(paramStringValue);
    vertical->addWidget(paramsString, 1);

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

    window->show();
}

SystemSolver::~SystemSolver() {}
