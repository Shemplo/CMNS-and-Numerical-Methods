#include "input.h"
#include "ui_input.h"
#include <iostream>

Input::Input(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::Run)
{
    ui->setupUi(this);
    //ui->f0t->setRenderHint(QPainter::Antialiasing);
    resetDefault();

    connect (ui->solve, &QPushButton::clicked,
             this, &Input::slotButtonSolve);
    connect (ui->restore, &QPushButton::clicked,
             this, &Input::resetDefault);
}

void Input::slotButtonSolve () {
    double x0 = ui->x0->text().toDouble(),
           y0 = ui->y0->text().toDouble(),
           z0 = ui->z0->text().toDouble();
    double sigma = ui->sigma->text().toDouble(),
           r  = ui->param_r->text().toDouble(),
           b  = ui->param_b->text().toDouble(),
           dt = ui->delta_time->text().toDouble();
    if (dt > 0) {
        std::cout << "Solving system" << std::endl;

        SystemSolver *solver = new ExplicitEulerSolver (x0, y0, z0, sigma, r, b, dt);
        std::vector<double> *axises = solver->solve(10);
        delete solver;

        /*
        QLineSeries *xseries = new QLineSeries();
        for (unsigned int i = 0; i < axises [0].size(); i++) {
            //std::cout << i * dt << " " << axises [0][i] << " " << axises [1][i] << " " << axises [2][i] << std::endl;
            *xseries << QPointF (i * dt, axises [0][i]);
        }

        QAreaSeries *area = new QAreaSeries(xseries, xseries);
        area->setName("x(t)");
        QPen pen (0x059605);
        pen.setWidth(1);
        area->setPen(pen);

        QChart *chart = new QChart();
        chart->addSeries(area);
        chart->createDefaultAxes();
        chart->axisY()->setVisible(false);

        ui->f0t->setChart(chart);*/
    }
}

void Input::resetDefault() {
    ui->x0->setText("1");
    ui->y0->setText("0");
    ui->z0->setText("0");
    ui->sigma->setText("10");
    ui->param_r->setText("1");
    ui->param_b->setText(QString::number(8.0 / 3.0));
    ui->delta_time->setText("0.0001");
}

Input::~Input()
{
    delete ui;
}
