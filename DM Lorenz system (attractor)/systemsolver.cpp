#include "systemsolver.h"

SystemSolver::SystemSolver(InputParams input, std::string name)
{
    this->input = input;
    this->name  = name;
    this->x0    = input.x0;
    this->y0    = input.y0;
    this->z0    = input.z0;
    this->sigma = input.sigma;
    this->r     = input.r;
    this->b     = input.b;
    this->dt    = input.dt;
    this->t     = input.t;
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

double SystemSolver::x(Vector3d p) {
    return x(p.x, p.y, p.z);
}

double SystemSolver::y(Vector3d p) {
    return y(p.x, p.y, p.z);
}

double SystemSolver::z(Vector3d p) {
    return z(p.x, p.y, p.z);
}

void SystemSolver::visualize(std::vector<double> *answer, bool minimize) {
    QWidget *window = new QWidget;
    window->setWindowTitle(QString::fromStdString("Lorenz system - " + name));
    window->setWindowFlag(Qt::MSWindowsFixedSizeDialogHint);
    window->move(0, 0);

    QVBoxLayout *vertical = new QVBoxLayout(window);
    QHBoxLayout *horizontal = new QHBoxLayout ();
    vertical->addLayout(horizontal);

    QVBoxLayout *verticalLeft = new QVBoxLayout ();
    horizontal->addLayout(verticalLeft);

    QWidget* mainGraph = visualize3D(answer);
    verticalLeft->addWidget(mainGraph, 1);

    if (!minimize) {
        QVBoxLayout *verticalRight = new QVBoxLayout ();
        horizontal->addLayout(verticalRight);

        const int width = static_cast<int>(mainGraph->width() * 0.9),
                  height = static_cast<int>(mainGraph->height() / 3);
        verticalRight->addWidget(visualize2D(answer[0], width, height, "xdt"));
        verticalRight->addWidget(visualize2D(answer[1], width, height, "ydt"));
        verticalRight->addWidget(visualize2D(answer[2], width, height, "zdt"));
    }

    QString paramStringValue = QString::asprintf(
        "Parameters: [x0 = %.4f, y0 = %.4f, z0 = %.4f, σ = %.4f, b = %.4f, r = %.4f, Δt = %0.4f, t = %.4f]",
        x0, y0, z0, sigma, b, r, dt, t);
    QLabel *paramsString = new QLabel(paramStringValue);
    vertical->addWidget(paramsString, 1);

    window->show();
}

QWidget* SystemSolver::visualize3D(std::vector<double> *data) {
    QtDataVisualization::Q3DScatter *graph = new QtDataVisualization::Q3DScatter();
    QWidget *container = QWidget::createWindowContainer(graph);

    QSize screenSize = graph->screen()->size();
    int width = screenSize.width() / 2,
        height = static_cast<int>(screenSize.height() * 0.8);
    container->setMinimumSize(QSize(width, height));

    //graph->activeTheme()->setType(QtDataVisualization::Q3DTheme::ThemeEbony);
    graph->scene()->activeCamera()->setCameraPreset(Q3DCamera::CameraPresetFront);
    graph->setShadowQuality(QAbstract3DGraph::ShadowQualitySoftLow);

    QScatter3DSeries *series = new QScatter3DSeries();
    series->setItemLabelFormat(QStringLiteral("@xTitle: @xLabel @yTitle: @yLabel @zTitle: @zLabel"));
    graph->addSeries(series);

    graph->axisX()->setTitle("X");
    graph->axisY()->setTitle("Y");
    graph->axisZ()->setTitle("Z");

    size_t m_itemCount = data->size();
    QScatterDataArray *dataArray = new QScatterDataArray;
    dataArray->resize(static_cast<int>(m_itemCount));
    QScatterDataItem *ptrToDataArray = &dataArray->first();

    for (size_t i = 0; i < m_itemCount; i++) {
        float x = static_cast<float>(data[0][i]),
              y = static_cast<float>(data[1][i]),
              z = static_cast<float>(data[2][i]);

        ptrToDataArray->setPosition(QVector3D(x, y, z));
        ptrToDataArray++;
    }

    graph->seriesList().at(0)->dataProxy()->resetArray(dataArray);

    return container;
}

QtCharts::QChartView* SystemSolver::visualize2D(std::vector<double> data, int width, int height, QString title) {
    QtCharts::QChart *chart = new QtCharts::QChart();
    QtCharts::QChartView *container = new QtCharts::QChartView (chart);
    container->setMinimumSize(QSize(width, height));

    QtCharts::QScatterSeries *series = new QtCharts::QScatterSeries();
    double time = 0;
    for (size_t i = 0; i < data.size(); i++) {
        series->append(static_cast<float>(time), static_cast<float>(data[i]));
        time += dt;
    }
    series->setMarkerSize(1);
    series->setColor(QColor(110, 165, 60));
    series->setBorderColor(QColor(110, 165, 60));

    chart->addSeries(series);
    chart->createDefaultAxes();
    chart->setTitle(title);
    chart->legend()->setVisible(false);

    return container;
}

SystemSolver::~SystemSolver() {}
