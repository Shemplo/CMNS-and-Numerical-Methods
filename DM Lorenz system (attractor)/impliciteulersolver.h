#ifndef IMPLICITEULERSOLVER_H
#define IMPLICITEULERSOLVER_H

#include "systemsolver.h"
#include "point3d.h"

class ImplicitEulerSolver : public SystemSolver {
public:
    ImplicitEulerSolver(double, double, double, double, double, double, double, std::string); //TODO: Это отвратно! Не мог struct сделать ?!
    std::vector<double> *solve (double time);
private:
    std::vector<double> *transform_result(std::vector<point3d> &work_f);
};

#endif // IMPLICITEULERSOLVER_H
