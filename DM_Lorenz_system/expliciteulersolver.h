#ifndef EXPLICITEULERSOLVER_H
#define EXPLICITEULERSOLVER_H

#include "systemsolver.h"

class ExplicitEulerSolver : public SystemSolver
{
    public:
        ExplicitEulerSolver(double, double, double, double, double, double, double, std::string);
        ~ExplicitEulerSolver();

        std::vector<double> *solve (double time);

};

#endif // EXPLICITEULERSOLVER_H
