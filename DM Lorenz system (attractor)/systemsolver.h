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

    protected:
        double x0, y0, z0, sigma, r, b, dt;
};

#endif // SYSTEMSOLVER_H
