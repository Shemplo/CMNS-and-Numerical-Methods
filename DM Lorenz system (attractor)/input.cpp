#include "input.h"
#include "ui_input.h"
#include <iostream>

Input::Input(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::Run)
{
    ui->setupUi(this);

    connect (ui->solve, &QPushButton::clicked,
             this, &Input::slotButtonSolve);
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
        solver->solve(10);
        delete solver;
    }
}

Input::~Input()
{
    delete ui;
}
