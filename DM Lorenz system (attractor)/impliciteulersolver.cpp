#include "impliciteulersolver.h"

ImplicitEulerSolver::ImplicitEulerSolver(double x0, double y0, double z0, double sigma, double r, double b, double dt, std::string name)
    : SystemSolver (x0, y0, z0, sigma, r, b, dt, name)
{

}

std::vector<double> *ImplicitEulerSolver::solve(double time) {
    std::cout << "Not implemented" << std::endl;
    return nullptr;
}

ImplicitEulerSolver::~ImplicitEulerSolver() {}
