#ifndef EXPLICITEULERSOLVER_H
#define EXPLICITEULERSOLVER_H

#include "systemsolver.h"

class ExplicitEulerSolver : public SystemSolver
{
    public:
        ExplicitEulerSolver();

        int *solve ();
};

#endif // EXPLICITEULERSOLVER_H
