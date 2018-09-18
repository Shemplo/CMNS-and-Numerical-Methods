#ifndef RUNGEKUTTASOLVER_H
#define RUNGEKUTTASOLVER_H

#include "systemsolver.h"

class RungeKuttaSolver : public SystemSolver
{
    public:
        RungeKuttaSolver(InputParams, std::string);
        ~RungeKuttaSolver();

        std::vector<double> *solve (double time);

};


#endif // RUNGEKUTTASOLVER_H
