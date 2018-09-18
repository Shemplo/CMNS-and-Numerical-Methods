#ifndef RUNGEKUTTASOLVER_H
#define RUNGEKUTTASOLVER_H

#include "systemsolver.h"

class RungeKuttaSolver : public SystemSolver
{
    public:
        RungeKuttaSolver(double, double, double, double, double, double, double, std::string);
        ~RungeKuttaSolver();

        std::vector<double> *solve (double time);

};


#endif // RUNGEKUTTASOLVER_H
