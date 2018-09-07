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
    std::cout << "Click" << std::endl;
}

Input::~Input()
{
    delete ui;
}
