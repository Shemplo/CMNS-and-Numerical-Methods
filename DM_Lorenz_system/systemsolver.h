#ifndef SYSTEMSOLVER_H
#define SYSTEMSOLVER_H

#define DIMENSION 3

#include <QApplication>
#include <QtGui/QScreen>

#include <QtDataVisualization/qabstract3dseries.h>
#include <QtDataVisualization/q3dscatter.h>
#include <QtCharts/QChartView>

#include <QtWidgets/QLabel>
#include <QtWidgets/QWidget>
#include <QtWidgets/QVBoxLayout>

#include <QRandomGenerator>

#include <iostream>
#include <vector>

#include "point3d.h"

using namespace QtDataVisualization;


class SystemSolver
{
    public:
        SystemSolver(double, double, double, double, double, double, double, std::string);
        virtual ~SystemSolver ();

        virtual std::vector<double> *solve (double);
        double x(double, double, double);
        double y(double, double, double);
        double z(double, double, double);

        double x(point3d p);
        double y(point3d p);
        double z(point3d p);

        void visualize(std::vector<double>*);


    protected:
        double x0, y0, z0, sigma, r, b, dt;
        std::string name;
};

#endif // SYSTEMSOLVER_H
