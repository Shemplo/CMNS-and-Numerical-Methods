#include "systemsolver.h"

SystemSolver::SystemSolver(double x0, double y0, double z0, double sigma, double r, double b, double dt)
{
    this->sigma = sigma; this->r = r; this->b = b;
    this->x0 = x0; this->y0 = y0; this->z0 = z0;
    this->dt = dt;
}

std::vector<double> *SystemSolver::solve (double time) {
    std::cout << "Error: called stub of function \"solve\"" << std::endl;
    std::cout << "Request time: " << time << std::endl;
    return nullptr;
}

SystemSolver::~SystemSolver() {}
