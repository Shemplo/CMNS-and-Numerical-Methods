#ifndef ADAMSSOLVER_H
#define ADAMSSOLVER_H

#include "systemsolver.h"

class AdamsSolver : public SystemSolver
{
    public:
        AdamsSolver(double, double, double, double, double, double, double);
        ~AdamsSolver();

        std::vector<double> *solve (double time);

};


#endif // ADAMSSOLVER_H
