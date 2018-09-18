#ifndef IMPLICITEULERSOLVER_H
#define IMPLICITEULERSOLVER_H

#include "systemsolver.h"
#include "vector3d.h"
#include "squarejacobimatrix3d.h"

#include <functional>

class ImplicitEulerSolver : public SystemSolver {
public:
    ImplicitEulerSolver(InputParams, std::string);
    std::vector<double> *solve (double time);
private:
    std::vector<double> *transform_result(std::vector<Vector3d> &work_f);
    Vector3d Newton_method(std::function<Vector3d(Vector3d)> f, Vector3d x_0, double eps, std::size_t n_iterations);
};

#endif // IMPLICITEULERSOLVER_H
