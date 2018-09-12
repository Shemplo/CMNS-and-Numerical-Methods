#ifndef SYSTEMSOLVER_H
#define SYSTEMSOLVER_H

#define DIMENSION 3

#include <iostream>
#include <vector>

class SystemSolver
{
    public:
        SystemSolver(double, double, double, double, double, double, double);
        virtual ~SystemSolver ();

        virtual std::vector<double> *solve (double);
        double x(double, double, double);
        double y(double, double, double);
        double z(double, double, double);


    protected:
        double x0, y0, z0, sigma, r, b, dt;
};

#endif // SYSTEMSOLVER_H
