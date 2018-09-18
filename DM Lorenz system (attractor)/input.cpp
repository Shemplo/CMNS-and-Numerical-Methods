#include "systemsolver.h"
#include "ui_input.h"
#include "input.h"

#include <iostream>
#include <array>

Input::Input(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::Run)
{
    ui->setupUi(this);
    //ui->f0t->setRenderHint(QPainter::Antialiasing);
    preset = 0;
    resetDefault();

    connect (ui->solve, &QPushButton::clicked,
             this, &Input::slotButtonSolve);
    connect (ui->restore, &QPushButton::clicked,
             this, &Input::resetDefault);
    connect (ui->preset, &QComboBox::currentTextChanged,
             this, &Input::loadPreset);
}

void Input::slotButtonSolve () {
    double x0    = ui->x0->text().toDouble(),
           y0    = ui->y0->text().toDouble(),
           z0    = ui->z0->text().toDouble(),
           sigma = ui->sigma->text().toDouble(),
           r     = ui->param_r->text().toDouble(),
           b     = ui->param_b->text().toDouble(),
           dt    = ui->delta_time->text().toDouble(),
           t     = ui->time->text().toDouble();
    InputParams input = {x0, y0, z0, sigma, r, b, dt, t};

    if (dt > 0) {
        SystemSolver *solver;
        int method = ui->solve_method->currentIndex();
        switch(method) {
            case 0:
                std::cout << "Solving system via Explicit Euler method" << std::endl;
                solver = new ExplicitEulerSolver (input, "Explicit Euler");
                break;
            case 1:
                std::cout << "Solving system via Implicit Euler method" << std::endl;
                solver = new ImplicitEulerSolver (input, "Impilicit Euler");
                break;
            case 2:
                std::cout << "Solving system via Runge-Kutta method" << std::endl;
                solver = new RungeKuttaSolver (input, "Runge-Kutta");
                break;
            case 3:
                std::cout << "Solving system via Adams method" << std::endl;
                solver = new AdamsSolver (input, "Adams");
                break;
        }

        if (solver) {
            std::vector<double> *axises = solver->solve(t);
            bool minimize = ui->minimize->isChecked();

            if (axises) solver->visualize(axises, minimize);
            delete solver;
        } else {
            std::cout << "Unknown method" << std::endl;
        }
    }
}

void Input::resetDefault() {
    QString x0 = "10.0", y0 = "10.0", z0 = "10.0",
            sigma = "10.0", r = "24.06", b = QString::number(8.0 / 3.0),
            delta_time = "0.0075", time = "100.0";

    switch(preset) {
        case 1:
            r = "1";
            break;
        case 2:
            r = "3";
            break;
        case 3:
            r = "16";
            break;
        case 4:
            r = "28";
            break;
        case 5:
            r = "80";
            break;
        case 6:
            r = "100";
            break;
    }

    ui->x0->setText(x0);
    ui->y0->setText(y0);
    ui->z0->setText(z0);
    ui->sigma->setText(sigma);
    ui->param_r->setText(r);
    ui->param_b->setText(b);
    ui->delta_time->setText(delta_time);
    ui->time->setText(time);
}

void Input::loadPreset() {
    preset = ui->preset->currentIndex();
    resetDefault();
}

Input::~Input()
{
    delete ui;
}
