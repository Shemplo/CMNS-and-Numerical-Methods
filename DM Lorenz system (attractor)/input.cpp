#include "input.h"
#include "ui_input.h"
#include "visualize.h"
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
        std::vector<double> *axises = solver->solve(100);
        delete solver;

        std::cout << axises->size() << std::endl;
        std::cout << axises[0].size() << std::endl;
        visualize(axises);

        QString cb = ui->solve_method->currentText();
        std::cout << cb.toStdString() << std::endl;
    }
}

void Input::resetDefault() {
    ui->x0->setText("10");
    ui->y0->setText("10");
    ui->z0->setText("10");
    ui->sigma->setText("10");
    ui->param_r->setText("24.06");
    ui->param_b->setText(QString::number(8.0 / 3.0));
    ui->delta_time->setText("0.0075");
}

Input::~Input()
{
    delete ui;
}
