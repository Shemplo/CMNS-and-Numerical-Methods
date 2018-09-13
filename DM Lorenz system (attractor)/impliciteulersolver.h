#ifndef IMPLICITEULERSOLVER_H
#define IMPLICITEULERSOLVER_H

#include "systemsolver.h"

class ImplicitEulerSolver : public SystemSolver
{
    public:
        ImplicitEulerSolver(double, double, double, double, double, double, double, std::string);
        ~ImplicitEulerSolver();

        std::vector<double> *solve (double time);

};

#endif // IMPLICITEULERSOLVER_H
